package ykt.ios4miui3.gavrique.utils;

import org.eclipse.jetty.server.Response;
import ykt.ios4miui3.gavrique.Core.Logger;
import ykt.ios4miui3.gavrique.Main;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by satisfaction on 15.07.16.
 */
public class Net {

    public static String getContentFromHttpsRequest(String urlString) {
        String result = null;
        URL url = null;
        try {
            url = new URL(urlString);
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            if (connection.getResponseCode() == Response.SC_OK) {
                result = getContent(connection);
            }
        } catch (MalformedURLException e) {
            Logger.get().error("Url error", e);
        } catch (IOException e) {
            Logger.get().error("Request error", e);
        }

        return result;
    }


    private static String getContent(HttpsURLConnection con) {
        String content = null;
        if (con != null) {
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                StringBuilder builder = new StringBuilder();
                String input;
                while ((input = br.readLine()) != null) {
                    builder.append(input + Main.LINE_SEPARATOR);
                }
                br.close();
                if (builder.length() > 0) {
                    content = builder.toString();
                    int sepLen = Main.LINE_SEPARATOR.length();
                    content = content.substring(0, content.length() > sepLen ? content.length() - sepLen : content.length());
                }
            } catch (IOException e) {
                Logger.get().error("Response read error", e);
            }
        }
        return content;
    }
}
