package ykt.ios4miui3.gavrique.Core;

import ykt.ios4miui3.gavrique.Main;
import ykt.ios4miui3.gavrique.utils.Net;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by satisfaction on 15.07.16.
 */
public class Bot {

    public static final String API_URL = "https://api.telegram.org";

    public static String getResponse(String methodName) {
        return getResponse(methodName, new HashMap<>());
    }

    public static String getResponse(String methodName, HashMap<String, String> params) {
        String url = API_URL + "/bot" + Main.getBotToken() + "/" + methodName;
        if (!params.isEmpty()) {
            url += "?";
            for (Map.Entry<String, String> entry : params.entrySet()) {
                url += entry.getKey() + "=" + entry.getValue() + "&";
            }
            url = url.substring(0, url.length() - 1);
        }
        return Net.getContentFromHttpsRequest(url);
    }
}
