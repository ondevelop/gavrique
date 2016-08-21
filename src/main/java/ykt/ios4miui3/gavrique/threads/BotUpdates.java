package ykt.ios4miui3.gavrique.threads;

import ykt.ios4miui3.gavrique.Core.Bot;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by satisfaction on 15.07.16.
 */
public class BotUpdates implements Runnable{
    public static AtomicLong lastUpdateId = new AtomicLong(0L);

    private static HashMap<String, String> authorVoices = new HashMap<>();

    @Override
    public void run() {
        while(true) {
            request();
        }
    }

    public static void request() {
        HashMap<String, String> params = new HashMap<>();
        params.put("offset", "" + lastUpdateId);
        params.put("timeout","" + 20);
        String response = Bot.getResponse("getupdates", params);

        QueueManager.putMessageToMessageQueue(response);
    }
}
