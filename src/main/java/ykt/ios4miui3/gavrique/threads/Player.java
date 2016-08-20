package ykt.ios4miui3.gavrique.threads;

import org.apache.commons.io.FileUtils;
import uk.co.caprica.vlcj.component.AudioMediaPlayerComponent;
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

    public Player(BlockingQueue<Command> playQueue) {
        this.playQueue = playQueue;
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
