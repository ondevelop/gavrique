package ykt.ios4miui3.gavrique.threads;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import ykt.ios4miui3.gavrique.Core.Bot;
import ykt.ios4miui3.gavrique.Core.Logger;
import ykt.ios4miui3.gavrique.models.BotMsg;

import java.util.HashMap;
import java.util.concurrent.BlockingQueue;

/**
 * Created by Silent on 20.08.2016.
 */
public class MessageSender implements Runnable {
    private final BlockingQueue<BotMsg> messagesQueue;

    public MessageSender(BlockingQueue<BotMsg> messagesQueue) {
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

    public void handle(BotMsg botMsg) {
        if (botMsg == null || botMsg.getText() == null) {
            return;
        }
        HashMap<String, String> params = new HashMap<>();
        params.put("chat_id", String.valueOf(botMsg.getChatId()));
        params.put("text", botMsg.getText());
        String response = Bot.getResponse("sendMessage", params);
        if (response == null) {
            Logger.get().error("null response for method sendMessage, params:" + params.toString());
            return;
        }
        JsonParser parser = new JsonParser();
        JsonElement responseJson = parser.parse(response);
        if (responseJson.getAsJsonObject().get("ok") == null) {
            Logger.get().error("There is no ok field in the response for method sendMessage, params:" + params.toString());
            return;
        }
        if (!responseJson.getAsJsonObject().get("ok").getAsString().equals("true")) {
            Logger.get().error("Not ok response for method sendMessage, params:" + params.toString());
            return;
        }
        Logger.get().info("Msg send to chat_id:" + botMsg.getChatId() + ", msg:" + botMsg.getText());
    }
}
