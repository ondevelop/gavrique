package ykt.ios4miui3.gavrique.threads;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import ykt.ios4miui3.gavrique.Core.Bot;
import ykt.ios4miui3.gavrique.Core.Logger;

import java.util.HashMap;

/**
 * Created by satisfaction on 15.07.16.
 */
public class BotUpdates {
    private static long lasUpdateId = 0;

    public static void check() {
        try {
            HashMap<String, String> params = new HashMap<>();
            params.put("offset", "" + lasUpdateId);
            String response = Bot.getResponse("getupdates", params);
            if (response == null || response.isEmpty()) {
                throw new Exception("null response");
            }
            JsonParser parser = new JsonParser();
            JsonElement jsonResponse = parser.parse(response);
            JsonObject json = jsonResponse.getAsJsonObject();
            if (!json.get("ok").getAsString().equals("true")) {
                throw new Exception("not ok json response");
            }
            JsonArray result = json.getAsJsonArray("result");
            for (JsonElement item : result) {
                long updateId = item.getAsJsonObject().get("update_id").getAsLong();
                JsonObject message = item.getAsJsonObject().getAsJsonObject("message");
                String userName = message.getAsJsonObject("from").get("username").getAsString();
                String text = message.get("text").getAsString();

                //Logger.get().info(userName + ": " + text);
            }

        } catch (Exception e) {
            Logger.get().error("bot error", e);
            try {
                Thread.sleep(20000);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
        }
    }
}
