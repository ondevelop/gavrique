package ykt.ios4miui3.gavrique.models.Telegram.Inline;

import com.google.gson.Gson;
import ykt.ios4miui3.gavrique.models.BotMessage;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Silent on 21.08.2016.
 */
public class AnswerInlineQuery implements BotMessage{
    private String inline_query_id;

    private List<InlineQueryResult> results;

    private int cache_time;
    private boolean is_personal;
    private String next_offset;
    private String switch_pm_text;
    private String switch_pm_parameter;


    @Override
    public HashMap<String, String> getParams() {
        HashMap<String, String> params = new HashMap<>();
        params.put("inline_query_id", this.inline_query_id);
        params.put("results", new Gson().toJson(this.results));
        params.put("cache_time", String.valueOf(cache_time));
        params.put("is_personal", String.valueOf(is_personal));
        params.put("next_offset", next_offset);
        params.put("switch_pm_text", switch_pm_text);
        params.put("switch_pm_parameter", switch_pm_parameter);

        return params;
    }

    @Override
    public String getMethodName() {
        return "answerInlineQuery";
    }

    public String getInline_query_id() {
        return inline_query_id;
    }

    public void setInline_query_id(String inline_query_id) {
        this.inline_query_id = inline_query_id;
    }

    public List<InlineQueryResult> getResults() {
        return results;
    }

    public void setResults(List<InlineQueryResult> results) {
        this.results = results;
    }

    public int getCache_time() {
        return cache_time;
    }

    public void setCache_time(int cache_time) {
        this.cache_time = cache_time;
    }

    public boolean is_personal() {
        return is_personal;
    }

    public void setIs_personal(boolean is_personal) {
        this.is_personal = is_personal;
    }

    public String getNext_offset() {
        return next_offset;
    }

    public void setNext_offset(String next_offset) {
        this.next_offset = next_offset;
    }

    public String getSwitch_pm_text() {
        return switch_pm_text;
    }

    public void setSwitch_pm_text(String switch_pm_text) {
        this.switch_pm_text = switch_pm_text;
    }

    public String getSwitch_pm_parameter() {
        return switch_pm_parameter;
    }

    public void setSwitch_pm_parameter(String switch_pm_parameter) {
        this.switch_pm_parameter = switch_pm_parameter;
    }
}
