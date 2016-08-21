package ykt.ios4miui3.gavrique.models.Telegram;

import ykt.ios4miui3.gavrique.models.BotMessage;

import java.util.HashMap;

/**
 * Created by Silent on 21.08.2016.
 */
public class SendMessage implements BotMessage {
    private long chatId;
    private String userName;
    private String text;

    public SendMessage() {
    }

    public SendMessage(long chatId, String userName, String text) {
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

    @Override
    public HashMap<String, String> getParams() {
        HashMap<String, String> params = new HashMap<>();
        params.put("chat_id", String.valueOf(this.getChatId()));
        params.put("text", this.getText());

        return params;
    }

    @Override
    public String getMethodName() {
        return "sendMessage";
    }
}
