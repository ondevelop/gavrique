package ykt.ios4miui3.gavrique.Core;

import com.google.gson.Gson;
import spark.ResponseTransformer;

/**
 * Created by satisfaction on 06.07.16.
 */
public class JsonTransformer  implements ResponseTransformer {

    private Gson gson = new Gson();

    @Override
    public String render(Object model) {
        return gson.toJson(model);
    }

}