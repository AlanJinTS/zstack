package org.zstack.mediator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.zstack.core.cloudbus.ReplyMessagePreSendingExtensionPoint;
import org.zstack.core.db.DatabaseFacade;
import org.zstack.header.message.Message;
import org.zstack.header.vm.VmNicInventory;
import org.zstack.network.service.eip.APIGetEipAttachableVmNicsReply;
import org.zstack.network.service.portforwarding.APIGetPortForwardingAttachableVmNicsReply;
import org.zstack.utils.CollectionUtils;
import org.zstack.utils.function.Function;

import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 */
public class EipPortForwardingAttachableMarshaler implements ReplyMessagePreSendingExtensionPoint {
    @Autowired
    private DatabaseFacade dbf;

    @Override
    public List<Class> getReplyMessageClassForPreSendingExtensionPoint() {
        List<Class> clz = new ArrayList<Class>();
        clz.add(APIGetEipAttachableVmNicsReply.class);
        clz.add(APIGetPortForwardingAttachableVmNicsReply.class);
        return clz;
    }

    @Override
    public void marshalReplyMessageBeforeSending(Message msg) {
        if (msg instanceof APIGetEipAttachableVmNicsReply) {
            marshal((APIGetEipAttachableVmNicsReply) msg);
        } else if (msg instanceof APIGetPortForwardingAttachableVmNicsReply) {
            marshal((APIGetPortForwardingAttachableVmNicsReply) msg);
        }
    }

    @Transactional(readOnly = true)
    private void marshal(APIGetPortForwardingAttachableVmNicsReply msg) {
        if (msg.getInventories().isEmpty()) {
            return;
        }

        List<String> nicUuids = msg.getInventories().stream().map(VmNicInventory::getUuid).collect(Collectors.toList());
        String sql = "select nic.uuid from VmNicVO nic where nic.uuid in (:nicUuids)" +
                " and nic.vmInstanceUuid not in (select vm.uuid from VmNicVO n, VmInstanceVO vm, EipVO eip where n.vmInstanceUuid = vm.uuid" +
                " and n.uuid = eip.vmNicUuid)";
        TypedQuery<String> q = dbf.getEntityManager().createQuery(sql, String.class);
        q.setParameter("nicUuids", nicUuids);
        List<String> uuids = q.getResultList();
        msg.setInventories(msg.getInventories().stream().filter(n -> uuids.contains(n.getUuid())).collect(Collectors.toList()));
    }

    @Transactional(readOnly = true)
    private void marshal(APIGetEipAttachableVmNicsReply msg) {
        if (msg.getInventories().isEmpty()) {
            return;
        }

        List<String> nicUuids = msg.getInventories().stream().map(VmNicInventory::getUuid).collect(Collectors.toList());

        String sql = "select nic.uuid from VmNicVO nic where nic.uuid in (:nicUuids)" +
                " and nic.vmInstanceUuid not in (select vm.uuid from VmNicVO n, VmInstanceVO vm, PortForwardingRuleVO pf where n.vmInstanceUuid = vm.uuid" +
                " and n.uuid = pf.vmNicUuid)";
        TypedQuery<String> q = dbf.getEntityManager().createQuery(sql, String.class);
        q.setParameter("nicUuids", nicUuids);
        List<String> uuids = q.getResultList();
        msg.setInventories(msg.getInventories().stream().filter(n -> uuids.contains(n.getUuid())).collect(Collectors.toList()));

    }
}
