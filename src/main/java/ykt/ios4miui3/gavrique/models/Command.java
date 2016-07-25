package ykt.ios4miui3.gavrique.models;

/**
 * Created by satisfaction on 17.07.16.
 */
public class Command {
    private long chatId;
    private String userName;
    private String alias;
    private boolean remove;

    public Command(long chatId, String userName, String alias) {
        this.userName = userName;
        this.chatId = chatId;
        this.alias = alias;
        this.remove = false;
    }

    public static Command createRemoveCommand(long chatId, String userName, String alias) {
        Command c = new Command(chatId, userName, alias);
        c.remove = true;
        return c;
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

    public boolean isRemove() {
        return remove;
    }
}
