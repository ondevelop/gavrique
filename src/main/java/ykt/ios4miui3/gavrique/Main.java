package ykt.ios4miui3.gavrique;

import ykt.ios4miui3.gavrique.Core.Logger;

/**
 * Created by sergeystepanov on 01.07.16.
 */
public class Main {

    public static void main(String[] args) {

        System.out.println("server starting");

        Logger.init();

        Logger.get().info("Logger initialized");

        Logger.get().info("server is down");

    }
}
