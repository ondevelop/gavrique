package ykt.ios4miui3.gavrique;

import ykt.ios4miui3.gavrique.Core.Logger;
import ykt.ios4miui3.gavrique.Core.SparkServer;
import ykt.ios4miui3.gavrique.db.Db;
import ykt.ios4miui3.gavrique.threads.GavThreadScheduler;

import java.io.File;

/**
 * Created by sergeystepanov on 01.07.16.
 */
public class Main {

    public static final String PATH_SEPARATOR = System.getProperties().getProperty("file.separator");
    public static final String RESOURCES_DIR_NAME = "resources";
    public static final String FILES_PATH = RESOURCES_DIR_NAME + PATH_SEPARATOR + "audiofiles";

    public static void main(String[] args) {

        System.out.println("server starting");

        Logger.init();
        Logger.get().info("Logger initialized");

        try {
            createDirs();
            Logger.get().info("Db initialization started");
            Db.init();
            Logger.get().info("Db initialized");

            Logger.get().info("Scheduler starting");
            GavThreadScheduler.start();

            Logger.get().info("Spark starting");
            SparkServer.run();

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
}
