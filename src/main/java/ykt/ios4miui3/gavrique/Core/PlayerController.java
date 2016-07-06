package ykt.ios4miui3.gavrique.Core;

import org.eclipse.jetty.util.StringUtil;
import spark.Request;
import spark.Response;
import spark.Route;
import ykt.ios4miui3.gavrique.Main;
import ykt.ios4miui3.gavrique.models.GavFile;
import ykt.ios4miui3.gavrique.utils.AudioFilePlayer;

/**
 * Created by satisfaction on 06.07.16.
 */
public class PlayerController implements Route {
    @Override
    public Object handle(Request request, Response response) throws Exception {
        String alias = StringUtil.nonNull(request.params(":alias"));
        GavFile gavFile = GavFile.getByAlias(alias);
        if (alias != null) {
            AudioFilePlayer player = new AudioFilePlayer();
            player.play(Main.FILES_PATH + Main.PATH_SEPARATOR + gavFile.getPath());
        }
        return new JsonResult("ok","");
    }
}
