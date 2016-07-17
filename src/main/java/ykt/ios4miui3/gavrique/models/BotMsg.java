package ykt.ios4miui3.gavrique.models;

/**
 * Created by satisfaction on 17.07.16.
 */
public class BotMsg {

    private long chatId;
    private String userName;
    private String text;

    public BotMsg() {
    }

    public BotMsg(long chatId, String userName, String text) {
        this.chatId = chatId;
        this.userName = userName;
        this.text = text;
    }

    public long getChatId() {
        return chatId;
    }

    public void setChatId(long chatId) {
        this.chatId = chatId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
