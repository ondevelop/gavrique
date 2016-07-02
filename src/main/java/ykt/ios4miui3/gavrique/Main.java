package ykt.ios4miui3.gavrique;

import ykt.ios4miui3.gavrique.Core.Logger;
import ykt.ios4miui3.gavrique.db.Db;

/**
 * Created by sergeystepanov on 01.07.16.
 */
public class Main {

    public static void main(String[] args) {

        System.out.println("server starting");

        Logger.init();
        Logger.get().info("Logger initialized");

        try {
            Logger.get().info("Db initialization started");
            Db.init();
            Logger.get().info("Db initialized");


        } catch (Exception e) {
            Logger.get().error("server error", e);
        }
        Logger.get().info("server is down");
    }
}