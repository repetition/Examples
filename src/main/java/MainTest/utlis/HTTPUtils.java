package MainTest.utlis;

import MainTest.TCPTest.TCPConnectTest;
import com.sun.istack.internal.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HTTPUtils {
    private static final Logger log = LoggerFactory.getLogger(HTTPUtils.class);


    private static Proxy getProxy() {

        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 8888));
        return proxy;
    }

    public static String PostUrlAsString(String url, String post) {

        StringBuilder stringBuilder = new StringBuilder();
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setConnectTimeout(1200);
            connection.setRequestProperty("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.181 Safari/537.36");
           connection.setRequestProperty("Connection","keep-alive");
            connection.setDoInput(true);
            if (null == post) {
                connection.setRequestMethod("GET");
            }
            if (post != null) {
                connection.setDoOutput(true);
                connection.setRequestMethod("POST");
                OutputStream os = connection.getOutputStream();
                os.write(post.replace(",", "%2C").getBytes());
                //osr.write(post.getBytes());
                os.flush();
                os.close();
            }
            // printHeader(connection);
            if (connection.getResponseCode() == HttpURLConnection.HTTP_CREATED||connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
/*                //获取cookie
                String headerField = connection.getHeaderField("Set-Cookie");
                String cookie = headerField.substring(0, headerField.indexOf(";"));*/
                InputStream is = connection.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader buffer = new BufferedReader(isr);
                String line;
                while ((line = buffer.readLine()) != null) {
                    stringBuilder.append(line);
                }
                return stringBuilder.toString();
            } else {
                return "fail";
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "fail";
        }
    }

    public static String Post(String url, String post, String cookie) {

        StringBuilder stringBuilder = new StringBuilder();
        HttpURLConnection connection = null;
        InputStream is = null;
        InputStreamReader isr = null;
        BufferedReader buffer = null;
        try {
            connection = (HttpURLConnection) new URL(url).openConnection(getProxy());
            connection.setRequestMethod("POST");
            connection.setConnectTimeout(11200);
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            connection.setRequestProperty("Cookie", cookie);
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.181 Safari/537.36");

            OutputStream osr = connection.getOutputStream();
            osr.write(post.getBytes(Charset.forName("utf-8")));
            osr.flush();
            osr.close();

            if (connection.getResponseCode() == 200) {
                is = connection.getInputStream();
                isr = new InputStreamReader(is);
                buffer = new BufferedReader(isr);
                String line;
                while ((line = buffer.readLine()) != null) {
                    stringBuilder.append(line);
                }
                return stringBuilder.toString();
            }
            return stringBuilder.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        } finally {
            close(buffer);
            close(isr);
            close(is);
        }
    }

    public static Map<String, Object> PostUrlAsMap(String url, String post) {

        StringBuilder stringBuilder = new StringBuilder();
        HttpURLConnection connection = null;
        InputStream is = null;
        InputStreamReader isr = null;
        BufferedReader buffer = null;
        Map<String, Object> resMap = new HashMap<>();
        try {
            connection = (HttpURLConnection) new URL(url).openConnection(getProxy());
            connection.setConnectTimeout(1200);
            connection.setDoInput(true);
            if (null == post) {
                connection.setRequestMethod("GET");
            }
            if (post != null) {
                connection.setDoOutput(true);
                connection.setRequestMethod("POST");
                OutputStream os = connection.getOutputStream();
                os.write(post.replace(",", "%2C").getBytes());
                //  os.write(post.getBytes());
                os.flush();
                os.close();
            }
            if (connection.getResponseCode() == 200) {
                printHeader(connection);
                //获取cookie
                String headerField = connection.getHeaderField("Set-Cookie");
                String cookie = headerField.substring(0, headerField.indexOf(";"));
                log.info(headerField);
                resMap.put("cookie", cookie);
                is = connection.getInputStream();
                isr = new InputStreamReader(is);
                buffer = new BufferedReader(isr);
                String line;
                while ((line = buffer.readLine()) != null) {
                    stringBuilder.append(line);
                }
                resMap.put("result", stringBuilder.toString());
            }
            return resMap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            close(buffer);
            close(isr);
            close(is);
        }
    }

    private static void printHeader(HttpURLConnection connection) {
        Map<String, List<String>> headerFields = connection.getHeaderFields();
        for (Map.Entry<String, List<String>> entry : headerFields.entrySet()) {
            log.info(entry.getKey() + " : " + entry.getValue());
        }
    }

    public static void close(Closeable io) {
        if (null != io) {
            try {
                io.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
