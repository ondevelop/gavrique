package ykt.ios4miui3.gavrique.threads;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.commons.io.FilenameUtils;
import ykt.ios4miui3.gavrique.Core.Bot;
import ykt.ios4miui3.gavrique.Core.Logger;
import ykt.ios4miui3.gavrique.Main;
import ykt.ios4miui3.gavrique.models.BotMsg;
import ykt.ios4miui3.gavrique.models.GavFile;
import ykt.ios4miui3.gavrique.models.Command;
import ykt.ios4miui3.gavrique.utils.Net;

import java.io.File;
import java.sql.Date;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;

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
            if (result != null && result.size() > 0) {
                Logger.get().info("response: " + result.toString());
            }

            for (JsonElement item : result) {
                lastUpdateId = item.getAsJsonObject().get("update_id").getAsLong() + 1;
                JsonObject message = item.getAsJsonObject().getAsJsonObject("message");

                if (message == null) {
                    Logger.get().error("There is no `message`: " + item.toString());
                    continue;
                }

                JsonElement fromEl = message.get("from");
                if (fromEl == null) {
                    Logger.get().error("There is no `from`: " + message.toString());
                    continue;
                }

                JsonElement chatEl = message.getAsJsonObject().get("chat");
                if (chatEl == null) {
                    Logger.get().error("There is no `chat`: " + item.toString());
                    continue;
                }
                long chatId = chatEl.getAsJsonObject().get("id").getAsLong();

                BotMsg responseOfBot = new BotMsg();
                responseOfBot.setChatId(chatId);

                JsonElement usernameEl = fromEl.getAsJsonObject().get("username");
                if (usernameEl == null) {
                    Logger.get().error("empty `username`: " + message.toString());
                    responseOfBot.setText("Username is a required");
                    QueueManager.putBotMsgToQueue(responseOfBot);
                    continue;
                }

                String userName = usernameEl.getAsString();
                responseOfBot.setUserName(userName);

                JsonObject audioFile = message.getAsJsonObject("voice");
                String audioType = "voice";
                if (audioFile == null) {
                    audioFile = message.getAsJsonObject("audio");
                    audioType = "audio";
                }
                if (audioFile != null) {
                    Logger.get().info(audioType + ": " + audioFile);
                    int duration = audioFile.get("duration").getAsInt();
                    if (duration > 15) {
                        responseOfBot.setText("To long duration: " + duration);
                        QueueManager.putBotMsgToQueue(responseOfBot);
                        continue;
                    }
                    String fileId = audioFile.get("file_id").getAsString();
                    authorVoices.put(userName, fileId);
                    responseOfBot.setText("Send me alias of the voice");
                    QueueManager.putBotMsgToQueue(responseOfBot);
                    continue;
                }
                JsonElement text = message.get("text");
                if (text != null) {
                    String textString = text.getAsString().trim().toLowerCase();
                    // help
                    if (textString.startsWith("help") || textString.startsWith("/help")) {
                        responseOfBot.setText(Bot.HELP_TEXT);
                        QueueManager.putBotMsgToQueue(responseOfBot);
                        continue;
                    }
                    // alias list
                    if (textString.startsWith("list") || textString.startsWith("/list")) {
                        List<GavFile> list = GavFile.list();
                        String aliasList = "commands: ";
                        for (GavFile gavFile : list) {
                            aliasList += "/play" + gavFile.getAlias() + ", ";
                        }
                        if (aliasList.length() > 2) {
                            aliasList = aliasList.substring(0, aliasList.length() - 2);
                        }
                        responseOfBot.setText(aliasList);
                        QueueManager.putBotMsgToQueue(responseOfBot);
                        continue;
                    }
                    // remove command
                    if (textString.length() > 8 && textString.startsWith("/remove ")) {
                        String alias = textString.substring(8, textString.length()).trim();
                        QueueManager.putAliasToQueue(Command.createRemoveCommand(chatId, userName, alias));
                        continue;
                    }
                    // play command
                    if ((textString.startsWith("play") && textString.length() > 4) || (textString.length() > 5 && textString.startsWith("/play"))) {
                        String alias = textString.startsWith("play") ? textString.substring(4).trim() : textString.substring(5).trim();
                        int index = alias.indexOf("@");
                        if (index != -1) {
                            alias = alias.substring(0, index);
                        }
                        QueueManager.putAliasToQueue(new Command(chatId, userName, alias));
                        continue;
                    }
                    if (textString.startsWith("/") || textString.equals("play")) {
                        responseOfBot.setText("Incorrect command, /help");
                        QueueManager.putBotMsgToQueue(responseOfBot);
                        continue;
                    }
                    // alias, after voice msg
                    if (!authorVoices.containsKey(userName)) {
                        responseOfBot.setText("Send me voice before to send alias, /help");
                        QueueManager.putBotMsgToQueue(responseOfBot);
                        continue;
                    }
                    String alias = textString.toLowerCase();
                    if (alias.contains(" ") || alias.contains("\\") || alias.contains("@")) {
                        responseOfBot.setText("Bad alias value, whitespace, @ or `\\` in the alias");
                        QueueManager.putBotMsgToQueue(responseOfBot);
                        continue;
                    }
                    if (loadAndSaveVoice(authorVoices.get(userName), alias, userName)) {
                        responseOfBot.setText("Voice have been saved with the alias `" + alias + "`, you can play with command `/play" + alias + "` or `/play " + alias + "`");
                    } else {
                        responseOfBot.setText("Can not load the voice, resend the voice");
                    }
                    authorVoices.remove(userName);
                    QueueManager.putBotMsgToQueue(responseOfBot);
                }
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

    private static boolean loadAndSaveVoice(String fileId, String alias, String author) {
        String fileJson = Bot.getResponse("getfile", new HashMap<String, String>() {{
            put("file_id", fileId);
        }});
        JsonParser parser = new JsonParser();
        JsonElement jsonResponse = parser.parse(fileJson);
        JsonElement ok = jsonResponse.getAsJsonObject().get("ok");
        if (ok == null || !ok.getAsString().equals("true")) {
            return false;
        }
        JsonObject result = jsonResponse.getAsJsonObject().getAsJsonObject("result");
        if (result == null) {
            return false;
        }
        JsonElement pathElement = result.get("file_path");
        if (pathElement == null) {
            return false;
        }
        String fileExtension = FilenameUtils.getExtension(pathElement.getAsString());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss");
        String fileName = LocalDateTime.now().format(formatter) + author + (!fileExtension.isEmpty() ? "." + fileExtension : "");
        String fullPath = Bot.API_URL + "/file/bot" + Main.getBotToken() + "/" + pathElement.getAsString();
        if (Net.saveUrl(fullPath, Main.FILES_PATH + Main.PATH_SEPARATOR + fileName)) {
            GavFile gavFile = GavFile.getByAlias(alias);
            if (gavFile == null) {
                new GavFile(author, alias, fileName).save();
            } else {
                //remove old file
                try {
                    File oldFile = new File(gavFile.getFullPath());
                    oldFile.delete();
                } catch (SecurityException e) {
                    Logger.get().error("Can not delete file", e);
                }

                gavFile.setAuthor(author);
                gavFile.setCreated(new Date(new java.util.Date().getTime()));
                gavFile.setPath(fileName);
                gavFile.save();
            }
            return true;
        }
        return false;
    }
}
