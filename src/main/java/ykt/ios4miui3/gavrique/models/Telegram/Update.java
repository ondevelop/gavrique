package ykt.ios4miui3.gavrique.models.Telegram;

/**
 * Created by Silent on 21.08.2016.
 */
public class Update<Optional> {
    private long update_id;

    private Optional optional;

    public long getUpdate_id() {
        return update_id;
    }

    public void setUpdate_id(long update_id) {
        this.update_id = update_id;
    }

    public Optional getOptional() {
        return optional;
    }

    public void setOptional(Optional optional) {
        this.optional = optional;
    }
}
