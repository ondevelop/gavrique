package ykt.ios4miui3.gavrique.threads;

import org.apache.commons.io.FileUtils;
import uk.co.caprica.vlcj.medialist.MediaList;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.list.MediaListPlayer;
import uk.co.caprica.vlcj.player.list.MediaListPlayerMode;
import ykt.ios4miui3.gavrique.Core.Logger;
import ykt.ios4miui3.gavrique.models.BotMsg;
import ykt.ios4miui3.gavrique.models.Command;
import ykt.ios4miui3.gavrique.models.GavFile;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;

/**
 * Created by Silent on 20.08.2016.
 */
public class Player implements Runnable {
    private final BlockingQueue<Command> playQueue;

    private MediaListPlayer mediaListPlayer;
    private MediaList mediaList;

    public Player(BlockingQueue<Command> playQueue) {
        this.playQueue = playQueue;

        MediaPlayerFactory mediaPlayerFactory = new MediaPlayerFactory();
        this.mediaListPlayer = mediaPlayerFactory.newMediaListPlayer();

        this.mediaList = mediaPlayerFactory.newMediaList();

        this.mediaListPlayer.setMediaList(mediaList);
        this.mediaListPlayer.setMode(MediaListPlayerMode.DEFAULT);
    }

    @Override
    public void run() {
        try {
            while(true) {
                handle(playQueue.take());
            }
        }
        catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    void handle(Command command) {
        if (command == null) {
            return;
        }

        BotMsg botMsg = new BotMsg(command.getChatId(), null, "");
        GavFile gavFile = GavFile.getByAlias(command.getAlias());
        if (gavFile == null) {
            botMsg.setText("Not existing alias");
            QueueManager.putBotMsgToResponseQueue(botMsg);
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
            QueueManager.putBotMsgToResponseQueue(botMsg);
            return;
        }
        try {
            /*MediaListPlayerEventAdapter listPlayerEventAdapter = new MediaListPlayerEventAdapter(){
                @Override
                public void nextItem(MediaListPlayer mediaListPlayer, libvlc_media_t item, String itemMrl) {
                    botMsg.setText("[" + command.getAlias() + "] is being played");
                    QueueManager.putBotMsgToResponseQueue(botMsg);
                }
            };

            this.mediaListPlayer.addMediaListPlayerEventListener(listPlayerEventAdapter);*/

            if (!this.mediaListPlayer.isPlaying()) {
                this.mediaList.clear();
                this.mediaList.release();
            }

            this.mediaList.addMedia(gavFile.getFullPath());

            if (!this.mediaListPlayer.isPlaying()) {
                this.mediaListPlayer.play();
            }

            botMsg.setText("[" + command.getAlias() + "] is being played");
            QueueManager.putBotMsgToResponseQueue(botMsg);
        } catch (Exception e) {
            Logger.get().error("Player error", e);
            botMsg.setText("[" + command.getAlias() + "], error have been happened when it is playing");
            QueueManager.putBotMsgToResponseQueue(botMsg);
        }
    }
}
