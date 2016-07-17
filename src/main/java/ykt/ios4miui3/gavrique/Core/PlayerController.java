package ykt.ios4miui3.gavrique.Core;

import org.eclipse.jetty.util.StringUtil;
import spark.Request;
import spark.Response;
import spark.Route;
import ykt.ios4miui3.gavrique.models.PlayCommand;
import ykt.ios4miui3.gavrique.threads.QueueManager;

/**
 * Created by satisfaction on 06.07.16.
 */
public class PlayerController implements Route {
    @Override
    public Object handle(Request request, Response response) throws Exception {
        String alias = StringUtil.nonNull(request.params(":alias"));
        if (alias.isEmpty()) {
            return new JsonResult("error", "alias is empty");
        }
        QueueManager.putAliasToQueue(new PlayCommand(-1, null, alias));
        return new JsonResult("ok", "");
    }
}
