package ykt.ios4miui3.gavrique.models;

import java.util.HashMap;

/**
 * Created by satisfaction on 17.07.16.
 */
public interface BotMessage {
    HashMap<String, String> getParams();

    String getMethodName();
}
