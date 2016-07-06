package ykt.ios4miui3.gavrique.Core;

/**
 * Created by satisfaction on 06.07.16.
 */
public class JsonResult {
    private String result;
    private String msg;

    public JsonResult(String result, String msg) {
        this.result = result;
        this.msg = msg;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
