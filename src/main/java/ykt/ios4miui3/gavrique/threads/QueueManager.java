package ykt.ios4miui3.gavrique.threads;

import ykt.ios4miui3.gavrique.Core.Logger;
import ykt.ios4miui3.gavrique.Main;
import ykt.ios4miui3.gavrique.models.GavFile;
import ykt.ios4miui3.gavrique.utils.AudioFilePlayer;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by sergeystepanov on 09.07.16.
 */
public class QueueManager {

    private static ConcurrentLinkedQueue<String> aliasQueue = new ConcurrentLinkedQueue<>();

    public static boolean putToQueue(String alias) {
        return aliasQueue.offer(alias);
    }

    public static void playFromQueue() {
        String alias = aliasQueue.poll();
        if (alias == null) {
            return;
        }
        GavFile gavFile = GavFile.getByAlias(alias);
        if (gavFile == null) {
            return;
        }
        try {
            AudioFilePlayer player = new AudioFilePlayer();
            player.play(Main.FILES_PATH + Main.PATH_SEPARATOR + gavFile.getPath());
        } catch (IllegalStateException e) {
            Logger.get().error("Player error", e);
        }
    }
}
