package ykt.ios4miui3.gavrique;

import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.discovery.NativeDiscovery;
import ykt.ios4miui3.gavrique.Core.Bot;
import ykt.ios4miui3.gavrique.Core.Logger;
import ykt.ios4miui3.gavrique.db.Db;
import ykt.ios4miui3.gavrique.threads.QueueManager;

import java.io.File;

/**
 * Created by sergeystepanov on 01.07.16.
 */
public class Main {

    public static final String PATH_SEPARATOR = System.getProperties().getProperty("file.separator");
    public static final String RESOURCES_DIR_NAME = "resources";
    public static final String FILES_PATH = RESOURCES_DIR_NAME + PATH_SEPARATOR + "audiofiles";
    public static final String LINE_SEPARATOR = System.getProperty("line.separator");

    private static String botName = "";
    private static String botToken = "";

    public static void main(String[] args) {

        System.out.println("server starting");

        Logger.init();
        Logger.get().info("Logger initialized");

        try {
            Logger.get().info("search vlc libs in os");
            if (!new NativeDiscovery().discover()) {
                Logger.get().error("There is no vlc libs in this os, install VLC player");
                return;
            }
            Logger.get().info("Libs have been find: " + LibVlc.INSTANCE.libvlc_get_version());

            Logger.get().info("Check args");
            initBot(args);
            Logger.get().info("bot props is got");

            createDirs();
            Logger.get().info("Db initialization started");
            Db.init();
            Logger.get().info("Db initialized");

            Logger.get().info("Scheduler starting");
            QueueManager.start();

            /* not need now
            Logger.get().info("Spark starting");
            SparkServer.run();
            */

            Logger.get().info(Bot.getResponse("getme"));
        } catch (Exception e) {
            Logger.get().error("server error", e);
        }
    }

    private static void createDirs() throws SecurityException {
        File dirResources = new File(RESOURCES_DIR_NAME);
        if (!dirResources.exists()) {
            dirResources.mkdir();
        }
        dirResources = new File(FILES_PATH);
        if (!dirResources.exists()) {
            dirResources.mkdir();
        }
        dirResources = new File(RESOURCES_DIR_NAME + PATH_SEPARATOR + "db");
        if (!dirResources.exists()) {
            dirResources.mkdir();
        }
    }

    private static void initBot(String[] args) throws Exception {
        if (args.length < 2) {
            throw new Exception("args not found");
        }
        botName = args[0];
        botToken = args[1];
        if (botName.isEmpty() || botToken.isEmpty()) {
            throw new Exception("args is empty");
        }
    }

    public static String getBotName() {
        return botName;
    }

    public static String getBotToken() {
        return botToken;
    }
}
