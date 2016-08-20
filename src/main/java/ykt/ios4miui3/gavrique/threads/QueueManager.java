package ykt.ios4miui3.gavrique.threads;

import ykt.ios4miui3.gavrique.models.BotMsg;
import ykt.ios4miui3.gavrique.models.Command;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by sergeystepanov on 09.07.16.
 */
public class QueueManager {

    private static BlockingQueue<Command> playQueue;
    private static BlockingQueue<BotMsg> messagesQueue;

    private static ConcurrentLinkedQueue<Command> aliasQueue = new ConcurrentLinkedQueue<>();
    private static ConcurrentLinkedQueue<BotMsg> botMsgs = new ConcurrentLinkedQueue<>();

    public static void start() {
        playQueue = new LinkedBlockingQueue<>();
        messagesQueue = new LinkedBlockingQueue<>();

        Player player = new Player(playQueue);
        MessageSender messageSender = new MessageSender(messagesQueue);

        new Thread(player).start();
        new Thread(messageSender).start();
        new Thread(new BotUpdates()).start();
    }

    public static void putMessageToQueue(Command alias) {
        try {
            playQueue.put(alias);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void putBotMsgToQueue(BotMsg botMsg) {
        try {
            messagesQueue.put(botMsg);
        }
        catch (InterruptedException e) {
        }
    }
}
