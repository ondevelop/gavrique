package ykt.ios4miui3.gavrique.models;

/**
 * Created by satisfaction on 17.07.16.
 */
public class PlayCommand {
    private long chatId;
    private String userName;
    private String alias;

    public PlayCommand(long chatId, String userName, String alias) {
        this.userName = userName;
        this.chatId = chatId;
        this.alias = alias;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public long getChatId() {
        return chatId;
    }

    public void setChatId(long chatId) {
        this.chatId = chatId;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }
}
