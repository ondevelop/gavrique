package ykt.ios4miui3.gavrique.models.Telegram.Inline;

/**
 * Created by Silent on 21.08.2016.
 */
public class InlineQueryResultArticle<T> extends InlineQueryResult {
    private String title;
    private T input_message_content;
    //todo:input_message_content
    //todo:reply_markup
    private String url;
    private boolean hide_url;
    private String description;
    private String thumb_url;
    private int thumb_width;
    private int thumb_height;

    public InlineQueryResultArticle() {
        this.type = "article";
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public T getInput_message_content() {
        return input_message_content;
    }

    public void setInput_message_content(T input_message_content) {
        this.input_message_content = input_message_content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isHide_url() {
        return hide_url;
    }

    public void setHide_url(boolean hide_url) {
        this.hide_url = hide_url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getThumb_url() {
        return thumb_url;
    }

    public void setThumb_url(String thumb_url) {
        this.thumb_url = thumb_url;
    }

    public int getThumb_width() {
        return thumb_width;
    }

    public void setThumb_width(int thumb_width) {
        this.thumb_width = thumb_width;
    }

    public int getThumb_height() {
        return thumb_height;
    }

    public void setThumb_height(int thumb_height) {
        this.thumb_height = thumb_height;
    }
}
