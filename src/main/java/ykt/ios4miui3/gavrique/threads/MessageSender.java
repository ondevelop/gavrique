package ykt.ios4miui3.gavrique.threads;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import ykt.ios4miui3.gavrique.Core.Bot;
import ykt.ios4miui3.gavrique.Core.Logger;
import ykt.ios4miui3.gavrique.models.BotMessage;

import java.util.HashMap;
import java.util.concurrent.BlockingQueue;

/**
 * Created by Silent on 20.08.2016.
 */
public class MessageSender implements Runnable {
    private final BlockingQueue<BotMessage> messagesQueue;

    public MessageSender(BlockingQueue<BotMessage> messagesQueue) {
        this.messagesQueue = messagesQueue;
    }

    @Override
    public void run() {
        try {
            while(true) {
                handle(messagesQueue.take());
            }
        }
        catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void handle(BotMessage botMessage) {
        if (botMessage == null) {
            return;
        }

        String response = Bot.getResponse(botMessage.getMethodName(), botMessage.getParams());
        if (response == null) {
            Logger.get().error("null response for method botMessage, params:" + botMessage.getParams().toString());
            return;
        }
        JsonParser parser = new JsonParser();
        JsonElement responseJson = parser.parse(response);
        if (responseJson.getAsJsonObject().get("ok") == null) {
            Logger.get().error("There is no ok field in the response for method botMessage, params:" + botMessage.getParams().toString());
            return;
        }
        if (!responseJson.getAsJsonObject().get("ok").getAsString().equals("true")) {
            Logger.get().error("Not ok response for method botMessage, params:" + botMessage.getParams().toString());
            return;
        }
//        Logger.get().info("Msg send to chat_id:" + botMessage.getChatId() + ", msg:" + botMessage.getText());
    }
}
