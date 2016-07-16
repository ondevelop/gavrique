package ykt.ios4miui3.gavrique.threads;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import ykt.ios4miui3.gavrique.Core.Bot;
import ykt.ios4miui3.gavrique.Core.Logger;
import ykt.ios4miui3.gavrique.Main;
import ykt.ios4miui3.gavrique.models.GavFile;
import ykt.ios4miui3.gavrique.utils.Net;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

/**
 * Created by satisfaction on 15.07.16.
 */
public class BotUpdates {
    private static long lastUpdateId = 0;

    private static HashMap<String, String> authorVoices = new HashMap<>();

    public static void check() {
        try {
            HashMap<String, String> params = new HashMap<>();
            params.put("offset", "" + lastUpdateId);
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
                lastUpdateId = item.getAsJsonObject().get("update_id").getAsLong() + 1;
                JsonObject message = item.getAsJsonObject().getAsJsonObject("message");

                String userName = message.getAsJsonObject("from").get("username").getAsString();

                JsonObject voice = message.getAsJsonObject("voice");
                if (voice != null) {
                    Logger.get().info("voice: " + voice);
                    int duration = voice.get("duration").getAsInt();
                    if (duration > 15) {
                        continue;
                    }
                    String fileId = voice.get("file_id").getAsString();
                    authorVoices.put(userName, fileId);
                }
                JsonElement text = message.get("text");
                if (text != null) {
                    String textString = text.getAsString().trim();
                    if (textString.length() > 5 && textString.startsWith("play ")) {
                        String alias = textString.substring(5);
                        QueueManager.putToQueue(alias);
                        continue;
                    }
                    if (!authorVoices.containsKey(userName)) {
                        continue;
                    }
                    String msg = textString.toLowerCase();
                    int index = msg.indexOf("alias");
                    if (index == -1) {
                        continue;
                    }
                    int equalIndex = msg.indexOf("=", index + 4);
                    if (equalIndex == -1) {
                        continue;
                    }
                    if (equalIndex >= msg.length() - 1) {
                        continue;
                    }
                    String alias = msg.substring(equalIndex + 1).trim();
                    loadAndSaveVoice(authorVoices.get(userName), alias, userName);
                    authorVoices.remove(userName);
                }
                Logger.get().info(userName + ": " + message);
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

    private static void loadAndSaveVoice(String fileId, String alias, String author) {
        String fileJson = Bot.getResponse("getfile", new HashMap<String, String>() {{
            put("file_id", fileId);
        }});
        JsonParser parser = new JsonParser();
        JsonElement jsonResponse = parser.parse(fileJson);
        JsonElement ok = jsonResponse.getAsJsonObject().get("ok");
        if (ok == null || !ok.getAsString().equals("true")) {
            return;
        }
        JsonObject result = jsonResponse.getAsJsonObject().getAsJsonObject("result");
        if (result == null) {
            return;
        }
        JsonElement pathElement = result.get("file_path");
        if (pathElement == null) {
            return;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss");
        String fileName = LocalDateTime.now().format(formatter) + author + ".ogg";
        String fullPath = Bot.API_URL + "/file/bot" + Main.getBotToken() + "/" + pathElement.getAsString();
        if (Net.loadFile(fullPath, Main.FILES_PATH, fileName)) {
            new GavFile(author, alias, fileName).save();
        }
    }
}
