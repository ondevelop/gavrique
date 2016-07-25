package ykt.ios4miui3.gavrique.threads;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.apache.commons.io.FileUtils;
import uk.co.caprica.vlcj.component.AudioMediaPlayerComponent;
import ykt.ios4miui3.gavrique.Core.Bot;
import ykt.ios4miui3.gavrique.Core.Logger;
import ykt.ios4miui3.gavrique.models.BotMsg;
import ykt.ios4miui3.gavrique.models.GavFile;
import ykt.ios4miui3.gavrique.models.Command;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by sergeystepanov on 09.07.16.
 */
public class QueueManager {

    private static ConcurrentLinkedQueue<Command> aliasQueue = new ConcurrentLinkedQueue<>();
    private static ConcurrentLinkedQueue<BotMsg> botMsgs = new ConcurrentLinkedQueue<>();

    public static boolean putAliasToQueue(Command alias) {
        return aliasQueue.offer(alias);
    }

    public static boolean putBotMsgToQueue(BotMsg botMsg) {
        return botMsgs.offer(botMsg);
    }

    public static void sendFromQueue() {
        BotMsg botMsg = botMsgs.poll();
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

    public static void executeCommandFromQueue() {
        Command command = aliasQueue.poll();
        if (command == null) {
            return;
        }
        BotMsg botMsg = new BotMsg(command.getChatId(), null, "");
        GavFile gavFile = GavFile.getByAlias(command.getAlias());
        if (gavFile == null) {
            botMsg.setText("Not existing alias");
            QueueManager.putBotMsgToQueue(botMsg);
            return;
        }
        if (command.isRemove()) {
            try {
                FileUtils.forceDelete(new File(gavFile.getFullPath()));
            } catch (IOException e) {
                Logger.get().error("File of alias '" + command.getAlias() + "' not found: " + gavFile.getFullPath());
            } finally {
                gavFile.remove();
            }
            botMsg.setText("[" + command.getAlias() + "] have been removed");
            QueueManager.putBotMsgToQueue(botMsg);
            return;
        }
        try {
            AudioMediaPlayerComponent playerComponent = new AudioMediaPlayerComponent();
            if (playerComponent.getMediaPlayer().playMedia(gavFile.getFullPath())) {
                botMsg.setText("[" + command.getAlias() + "] is being played");
            } else {
                throw new Exception("can not play source");
            }
        } catch (Exception e) {
            Logger.get().error("Player error", e);
            botMsg.setText("[" + command.getAlias() + "], error have been happened when it is playing");
        }
        QueueManager.putBotMsgToQueue(botMsg);
    }
}
