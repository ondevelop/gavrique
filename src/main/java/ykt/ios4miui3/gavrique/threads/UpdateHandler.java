package ykt.ios4miui3.gavrique.threads;

import com.google.gson.*;
import org.apache.commons.io.FilenameUtils;
import ykt.ios4miui3.gavrique.Core.Bot;
import ykt.ios4miui3.gavrique.Core.Logger;
import ykt.ios4miui3.gavrique.Main;
import ykt.ios4miui3.gavrique.models.Command;
import ykt.ios4miui3.gavrique.models.GavFile;
import ykt.ios4miui3.gavrique.models.Telegram.Inline.AnswerInlineQuery;
import ykt.ios4miui3.gavrique.models.Telegram.Inline.InlineQueryResult;
import ykt.ios4miui3.gavrique.models.Telegram.Inline.InlineQueryResultArticle;
import ykt.ios4miui3.gavrique.models.Telegram.Inline.InputTextMessageContent;
import ykt.ios4miui3.gavrique.models.Telegram.InlineQuery;
import ykt.ios4miui3.gavrique.models.Telegram.Message;
import ykt.ios4miui3.gavrique.models.Telegram.SendMessage;
import ykt.ios4miui3.gavrique.models.Telegram.Update;
import ykt.ios4miui3.gavrique.utils.Net;

import java.io.File;
import java.sql.Date;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.BlockingQueue;

/**
 * Created by Silent on 21.08.2016.
 */
public class UpdateHandler implements Runnable {
    private BlockingQueue<String> updatesQueue;

    private static HashMap<String, String> authorVoices = new HashMap<>();

    public UpdateHandler(BlockingQueue<String> updatesQueue) {
        this.updatesQueue = updatesQueue;
    }

    @Override
    public void run() {
        try {
            while(true) {
                List<Update> updates = parse(updatesQueue.take());
                handle(updates);
            }
        }
        catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public List<Update> parse(String response) {
        List<Update> updates = new ArrayList<>();

        if (response == null || response.isEmpty()) {
            Logger.get().error("null response");
            return updates;
        }

        JsonParser parser = new JsonParser();
        JsonElement jsonResponse;
        JsonObject json;

        try {
            jsonResponse = parser.parse(response);
            json = jsonResponse.getAsJsonObject();
        }
        catch (IllegalStateException | JsonParseException e) {
            Logger.get().error("not ok json response: " + response);
            return updates;
        }

        if (!json.get("ok").getAsString().equals("true")) {
            Logger.get().error("not ok json response: " + response);
            return updates;
        }
        JsonArray result = json.getAsJsonArray("result");

        if (result == null) {
            Logger.get().error("no result in json: " + response);
            return updates;
        }

        Logger.get().info("response: " + result.toString());

        for (JsonElement item : result) {
            JsonObject object;
            try {
                object = item.getAsJsonObject();
                Gson gson = new Gson();

                if (object.get("message") != null) {
                    Update<Message> update = new Update<>();
                    update.setUpdate_id(object.get("update_id").getAsLong());

                    Message message = gson.fromJson(object.get("message"), Message.class);

                    update.setOptional(message);

                    if (update.getUpdate_id() >= BotUpdates.lastUpdateId.get()) {
                        updates.add(update);
                    }
                    continue;
                }

                if (object.get("inline_query") != null) {
                    Update<InlineQuery> update = new Update<>();
                    update.setUpdate_id(object.get("update_id").getAsLong());

                    InlineQuery inlineQuery = gson.fromJson(object.get("inline_query"), InlineQuery.class);

                    update.setOptional(inlineQuery);

                    if (update.getUpdate_id() >= BotUpdates.lastUpdateId.get()) {
                        updates.add(update);
                    }
                    continue;
                }
            }
            catch (IllegalStateException | ClassCastException e) {
                Logger.get().error("result item is not object: " + item.getAsString());
            }
        }

        return updates;
    }

    public void handle(List<Update> updates) {
        for (Update update : updates) {
            BotUpdates.lastUpdateId.set(update.getUpdate_id() + 1);

            // message
            if (update.getOptional() instanceof Message) {
                SendMessage responseOfBot = new SendMessage();

                Message message = (Message) update.getOptional();

                responseOfBot.setChatId(message.getChat().getId());
                responseOfBot.setUserName(message.getFrom().getUsername());

                switch (message.getType()) {
                    case AUDIO:
                        Logger.get().info("audio : " + message.getAudio().toString());
                        if (message.getAudio().getDuration() > 15) {
                            responseOfBot.setText("To long duration: " + message.getAudio().getDuration());
                            QueueManager.putBotMsgToResponseQueue(responseOfBot);
                            break;
                        }

                        authorVoices.put(message.getFrom().getUsername(), message.getVoice().getFile_id());
                        responseOfBot.setText("Send me alias of the voice");
                        QueueManager.putBotMsgToResponseQueue(responseOfBot);
                        break;
                    case VOICE:
                        Logger.get().info("voice : " + message.getVoice().toString());
                        if (message.getVoice().getDuration() > 15) {
                            responseOfBot.setText("To long duration: " + message.getVoice().getDuration());
                            QueueManager.putBotMsgToResponseQueue(responseOfBot);
                            break;
                        }

                        authorVoices.put(message.getFrom().getUsername(), message.getVoice().getFile_id());
                        responseOfBot.setText("Send me alias of the voice");
                        QueueManager.putBotMsgToResponseQueue(responseOfBot);
                        break;
                    case TEXT:
                        String text = message.getText();

                        // help
                        if (text.startsWith("help") || text.startsWith("/help")) {
                            responseOfBot.setText(Bot.HELP_TEXT);
                            QueueManager.putBotMsgToResponseQueue(responseOfBot);
                            break;
                        }

                        // alias list
                        if (text.startsWith("list") || text.startsWith("/list")) {
                            List<GavFile> list = GavFile.list();
                            String aliasList = "commands: ";
                            for (GavFile gavFile : list) {
                                aliasList += "/play" + gavFile.getAlias() + ", ";
                            }
                            if (aliasList.length() > 2) {
                                aliasList = aliasList.substring(0, aliasList.length() - 2);
                            }
                            responseOfBot.setText(aliasList);
                            QueueManager.putBotMsgToResponseQueue(responseOfBot);
                            break;
                        }

                        // random
                        if (text.startsWith("random") || text.startsWith("/random")) {
                            List<GavFile> list = GavFile.list();

                            GavFile randomGavFile = list.get(new Random().nextInt(list.size()));

                            QueueManager.putMessageToQueue(new Command(message.getChat().getId(), message.getChat().getUsername(), randomGavFile.getAlias()));
                            break;
                        }

                        // play command
                        if ((text.startsWith("play") && text.length() > 4) || (text.length() > 5 && text.startsWith("/play"))) {
                            String alias = message.getText().startsWith("play") ? text.substring(4).trim() : text.substring(5).trim();
                            int index = alias.indexOf("@");
                            if (index != -1) {
                                alias = alias.substring(0, index);
                            }
                            QueueManager.putMessageToQueue(new Command(message.getChat().getId(), message.getChat().getUsername(), alias));
                            break;
                        }
                        if (text.startsWith("/") || message.getText().equals("play")) {
                            responseOfBot.setText("Incorrect command, /help");
                            QueueManager.putBotMsgToResponseQueue(responseOfBot);
                            break;
                        }

                        // alias, after voice msg
                        if (!authorVoices.containsKey(message.getFrom().getUsername())) {
                            responseOfBot.setText("Send me voice before to send alias, /help");
                            QueueManager.putBotMsgToResponseQueue(responseOfBot);
                            break;
                        }
                        String alias = message.getText().toLowerCase();
                        if (alias.contains(" ") || alias.contains("\\") || alias.contains("@")) {
                            responseOfBot.setText("Bad alias value, whitespace, @ or `\\` in the alias");
                            QueueManager.putBotMsgToResponseQueue(responseOfBot);
                            break;
                        }
                        if (loadAndSaveVoice(authorVoices.get(message.getFrom().getUsername()), alias, message.getFrom().getUsername())) {
                            responseOfBot.setText("Voice have been saved with the alias `" + alias + "`, you can play with command `/play" + alias + "` or `/play " + alias + "`");
                        } else {
                            responseOfBot.setText("Can not load the voice, resend the voice");
                        }
                        authorVoices.remove(message.getFrom().getUsername());
                        QueueManager.putBotMsgToResponseQueue(responseOfBot);
                }
                // inline query
            }else if (update.getOptional() instanceof InlineQuery){
                InlineQuery inlineQuery = (InlineQuery) update.getOptional();

                AnswerInlineQuery answerInlineQuery = new AnswerInlineQuery();

                List<GavFile> gavFiles = GavFile.searchByAlias(inlineQuery.getQuery());

                List<InlineQueryResult> results = new ArrayList<>();

                for (GavFile gavFile : gavFiles) {
                    InlineQueryResultArticle inlineQueryResult = new InlineQueryResultArticle<InputTextMessageContent>();
                    inlineQueryResult.setId(String.valueOf(gavFile.getId()));
                    inlineQueryResult.setTitle("/play " + gavFile.getAlias());
                    inlineQueryResult.setInput_message_content(new InputTextMessageContent("/play " + gavFile.getAlias()));

                    results.add(inlineQueryResult);
                }

                answerInlineQuery.setInline_query_id(inlineQuery.getId());
                answerInlineQuery.setResults(results);

                QueueManager.putBotMsgToResponseQueue(answerInlineQuery);
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
