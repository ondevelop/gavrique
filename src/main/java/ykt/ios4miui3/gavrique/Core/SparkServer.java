package ykt.ios4miui3.gavrique.Core;

import spark.Spark;

/**
 * Created by satisfaction on 06.07.16.
 */
public class SparkServer {

    public static void run() {
        Spark.port(8081);
        Spark.get("/", (request, response) -> "ok");
        Spark.get("/api/play/:alias", "application/json", new PlayerController(), new JsonTransformer());
    }
}
