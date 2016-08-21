package ykt.ios4miui3.gavrique.models.Telegram.Inline;

/**
 * Created by Silent on 21.08.2016.
 */
public class InputTextMessageContent {
    private String message_text;
    private String parse_mode;
    private boolean disable_web_page_preview;

    public InputTextMessageContent(String message_text) {
        this.message_text = message_text;
    }

    public String getMessage_text() {
        return message_text;
    }

    public void setMessage_text(String message_text) {
        this.message_text = message_text;
    }

    public String getParse_mode() {
        return parse_mode;
    }

    public void setParse_mode(String parse_mode) {
        this.parse_mode = parse_mode;
    }

    public boolean isDisable_web_page_preview() {
        return disable_web_page_preview;
    }

    public void setDisable_web_page_preview(boolean disable_web_page_preview) {
        this.disable_web_page_preview = disable_web_page_preview;
    }
}
