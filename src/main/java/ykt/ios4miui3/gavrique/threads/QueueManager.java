package ykt.ios4miui3.gavrique.threads;

import ykt.ios4miui3.gavrique.models.BotMsg;
import ykt.ios4miui3.gavrique.models.Command;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by sergeystepanov on 09.07.16.
 */
public class QueueManager {

    private static BlockingQueue<Command> playQueue;
    private static BlockingQueue<BotMsg> responseQueue;
    private static BlockingQueue<String> updatesQueue;

    public static void start() {
        playQueue = new LinkedBlockingQueue<>();
        responseQueue = new LinkedBlockingQueue<>();
        updatesQueue = new LinkedBlockingQueue<>();

        Player player = new Player(playQueue);
        MessageSender messageSender = new MessageSender(responseQueue);
        UpdateHandler updateHandler = new UpdateHandler(updatesQueue);

        new Thread(player).start();
        new Thread(messageSender).start();
        new Thread(updateHandler).start();
        new Thread(new BotUpdates()).start();
    }

    public static void putMessageToQueue(Command alias) {
        try {
            playQueue.put(alias);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void putBotMsgToResponseQueue(BotMsg botMsg) {
        try {
            responseQueue.put(botMsg);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void putMessageToMessageQueue(String message) {
        try {
            updatesQueue.put(message);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
