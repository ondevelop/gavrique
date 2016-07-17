package ykt.ios4miui3.gavrique.utils;

import org.apache.commons.io.FileUtils;
import org.eclipse.jetty.server.Response;
import ykt.ios4miui3.gavrique.Core.Logger;
import ykt.ios4miui3.gavrique.Main;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

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
            } else {
                Logger.get().error("Request error for :" + urlString);
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

    public static boolean loadFile(String path, String filePath) {
        try {
            File newFile = new File(filePath);
            newFile.createNewFile();
            FileUtils.copyURLToFile(new URL(path), newFile, 11000, 11000);
            return true;
        } catch (MalformedURLException e) {
            Logger.get().error("Url error", e);
        } catch (IOException e) {
            Logger.get().error("File saving from url error", e);
        }
        return false;
    }

    public static boolean saveUrl(String urlString, String filename) {
        BufferedInputStream in = null;
        FileOutputStream fout = null;
        boolean result = false;
        try {
            URLConnection conn = new URL(urlString).openConnection();
            conn.setConnectTimeout(12000);
            conn.setReadTimeout(12000);
            in = new BufferedInputStream(conn.getInputStream());
            fout = new FileOutputStream(filename);

            byte data[] = new byte[1024];
            int count;
            while ((count = in.read(data, 0, 1024)) != -1) {
                fout.write(data, 0, count);
            }
            fout.flush();
            fout.close();
            Logger.get().info("FileOutputStream have been flushed:" + filename);
            result = true;
        } catch (MalformedURLException e) {
            Logger.get().error("Url error", e);
        } catch (IOException e) {
            Logger.get().error("File saving from url error", e);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
                if (fout != null) {
                    fout.close();
                }
            } catch (IOException e) {
                Logger.get().error("File saving from url error", e);
            }
        }
        return result;
    }
}
