package ykt.ios4miui3.gavrique.models.Telegram;

/**
 * Created by Silent on 21.08.2016.
 */
public class MessageEntity {
    public enum Type {mention , hashtag, bot_command, url, email, bold, italic, code, pre, text_link, text_mention }
    private Type type;
    private long offset;
    private long length;
    private String url;
    private User user;

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public long getOffset() {
        return offset;
    }

    public void setOffset(long offset) {
        this.offset = offset;
    }

    public long getLength() {
        return length;
    }

    public void setLength(long length) {
        this.length = length;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
