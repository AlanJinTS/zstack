package org.zstack.scheduler;

import org.springframework.http.HttpMethod;
import org.zstack.header.core.scheduler.SchedulerJobInventory;
import org.zstack.header.identity.Action;
import org.zstack.header.query.APIQueryMessage;
import org.zstack.header.query.AutoQuery;
import org.zstack.header.rest.RestRequest;

import java.util.List;
import static java.util.Arrays.asList;

/**
 * Created by Mei Lei<meilei007@gmail.com> on 7/18/16.
 */
@Action(category = SchedulerConstant.ACTION_CATEGORY, names = {"read"})
@AutoQuery(replyClass = APIQuerySchedulerJobReply.class, inventoryClass = SchedulerJobInventory.class)
@RestRequest(
        path = "/scheduler/jobs",
        optionalPaths = {"/scheduler/jobs/{uuid}"},
        method = HttpMethod.GET,
        responseClass = APIQuerySchedulerJobReply.class
)
public class APIQuerySchedulerJobMsg extends APIQueryMessage {

    public static List<String> __example__() {
        return asList("name=TestScheduler", "state=Enabled");
    }
}
