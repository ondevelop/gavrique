package ykt.ios4miui3.gavrique.Core;

import org.slf4j.LoggerFactory;

/**
 * Created by satisfaction on 02.07.16.
 */
public class Logger {
    private static org.slf4j.Logger instance;

    public static org.slf4j.Logger get() {
        if (instance == null) {
            init();
        }
        return instance;
    }

    public static void init() {
        instance = LoggerFactory.getLogger(Logger.class);
    }

}
