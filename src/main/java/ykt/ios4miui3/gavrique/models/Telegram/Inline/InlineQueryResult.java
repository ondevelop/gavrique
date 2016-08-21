package ykt.ios4miui3.gavrique.models.Telegram.Inline;

/**
 * Created by Silent on 21.08.2016.
 */
public abstract class InlineQueryResult {
    protected String id;
    protected String type;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
