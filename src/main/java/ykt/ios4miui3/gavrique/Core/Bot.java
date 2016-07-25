package ykt.ios4miui3.gavrique.Core;

import ykt.ios4miui3.gavrique.Main;
import ykt.ios4miui3.gavrique.utils.Net;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by satisfaction on 15.07.16.
 */
public class Bot {

    public static final String HELP_TEXT = "HOW TO SAVE VOICE/AUDIO: send me in private chat a voice/audio, send alias of voice/audio \n" +
            "HOT TO PLAY: send `/play[alias]` in group or `play[alias]` in private chat \n" +
            "HOT TO REMOVE: send `/remove [alias]` in group or in private chat \n" +
            "LIST OF COMMANDS: /list";

    public static final String API_URL = "https://api.telegram.org";

    public static String getResponse(String methodName) {
        return getResponse(methodName, new HashMap<>());
    }

    public static String getResponse(String methodName, HashMap<String, String> params) {
        String url = API_URL + "/bot" + Main.getBotToken() + "/" + methodName;
        if (!params.isEmpty()) {
            url += "?";
            for (Map.Entry<String, String> entry : params.entrySet()) {
                try {
                    url += entry.getKey() + "=" + URLEncoder.encode(entry.getValue(), "UTF-8") + "&";
                } catch (UnsupportedEncodingException e) {
                    Logger.get().error("Bad param value:" + entry.getValue());
                }
            }
            url = url.substring(0, url.length() - 1);
        }
        return Net.getContentFromHttpsRequest(url);
    }
}
