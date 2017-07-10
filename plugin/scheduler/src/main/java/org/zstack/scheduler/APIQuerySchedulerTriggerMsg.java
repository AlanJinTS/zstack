package org.zstack.scheduler;

import org.springframework.http.HttpMethod;
import org.zstack.header.core.scheduler.SchedulerTriggerInventory;
import org.zstack.header.identity.Action;
import org.zstack.header.query.APIQueryMessage;
import org.zstack.header.query.AutoQuery;
import org.zstack.header.rest.RestRequest;

import java.util.List;

import static java.util.Arrays.asList;

/**
 * Created by AlanJager on 2017/6/8.
 */

@Action(category = SchedulerConstant.ACTION_CATEGORY, names = {"read"})
@AutoQuery(replyClass = APIQuerySchedulerTriggerReply.class, inventoryClass = SchedulerTriggerInventory.class)
@RestRequest(
        path = "/scheduler/triggers",
        optionalPaths = {"/scheduler/triggers/{uuid}"},
        method = HttpMethod.GET,
        responseClass = APIQuerySchedulerTriggerReply.class
)
public class APIQuerySchedulerTriggerMsg extends APIQueryMessage {
    public static List<String> __example__() {
        return asList("name=TestSchedulerTrigger", "name=trigger");
    }
}
