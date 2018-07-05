package MainTest.datacreate;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

import static MainTest.datacreate.Api.HOST;
import static MainTest.datacreate.Api.login_MobilURL;
import static MainTest.datacreate.PressureTest.loginCount;
import static MainTest.datacreate.PressureTest.loginFailCount;
import static MainTest.datacreate.PressureTest.meetingId;

public class ResourceDownRunnable implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(ResourceDownRunnable.class);
    int index;
    CountDownLatch countDownLatch;
    ConcurrentHashMap<Integer, PressureTest.ThreadRecord> threadMap;
    JsonObject asJsonObject;
    public static int count = 0;
    ConcurrentHashMap<Integer, Long> loginTimeMap;
    String meetingRoomId;
    String personId;

    public ResourceDownRunnable(int index, CountDownLatch countDownLatch, ConcurrentHashMap<Integer, PressureTest.ThreadRecord> threadMap, ConcurrentHashMap<Integer, Long> loginTimeMap, JsonObject asJsonObject, String meetingRoomId) {
        this.index = index;
        this.countDownLatch = countDownLatch;
        this.threadMap = threadMap;
        this.asJsonObject = asJsonObject;
        this.loginTimeMap = loginTimeMap;
        this.meetingRoomId = meetingRoomId;
    }


    @Override
    public void run() {


    }


    public void meetingResourceDown(Map<String, String> map, String materialsPreviewAddress) {
        try {
            URL url = new URL(HOST + materialsPreviewAddress);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(120 * 1000);
            connection.setReadTimeout(120 * 1000);
            connection.setDoInput(true);
            connection.setRequestMethod("GET");
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream is = connection.getInputStream();
                byte[] bytes = new byte[10 * 1024];
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    public String meetingResourceList(Map<String, String> map) {
        String tokenMD5 = map.get("tokenMD5");
        String sid = map.get("sid");
        String meetingResourceList_Param = "meetingId=" + meetingId + "&token=" + tokenMD5 + "&sid=" + sid;
        String meetingResourceList_Result = PostUrlAsString(meetingResourceList_Param, meetingResourceList_Param);
        if (meetingResourceList_Result.equals("fail")) {
            return null;
        }
        JsonParser parser = new JsonParser();
        JsonObject asJsonObject = parser.parse(meetingResourceList_Result).getAsJsonObject();
        JsonArray asJsonArray = asJsonObject.get("responseObject").getAsJsonObject().get("materialList").getAsJsonArray();
        String materialsPreviewAddress = asJsonArray.get(0).getAsJsonObject().get("materialsPreviewAddress").getAsString();
        return materialsPreviewAddress;
    }

    public Map<String, String> loginForMobile(String user, String pass) {
        long start = System.currentTimeMillis();
        String login_Param = "password=" + user + "&equipmentCode=" + getUUID32() + "&lang=zh_CN&equipmentType=Android&username=" + pass + "&equipmentOsVersion=8.0.0&token=d6d4f6c27f5ddadf2d4bd18ffb1d3977&sid=sys041530688064415";
        String login_Result = PostUrlAsString(login_MobilURL, login_Param);
        //   log.info(login_Result);
        if (login_Result.equals("fail")) {
            loginFailCount++;
            loginTimeMap.put(index, (System.currentTimeMillis() - start));
            return null;
        }
        JsonParser parser = new JsonParser();
        JsonObject asJsonObject = parser.parse(login_Result).getAsJsonObject();
        String orgaId = asJsonObject.get("orgaId").getAsString();
        String orgaName = asJsonObject.get("orgaName").getAsString();
        String sid = asJsonObject.get("sid").getAsString();
        String token = asJsonObject.get("token").getAsString();
        String tokenMD5 = getMD5(token + "810ThinkWin811");
        String personName = asJsonObject.get("personName").getAsString();
//        log.info("tokenMD5:" + tokenMD5 + "- personName:" + personName + " - sid:" + sid);
        boolean result = asJsonObject.get("result").getAsBoolean();

        if (result) {
            loginCount++;
        } else {
            loginFailCount++;
        }

        Map<String, String> map = new HashMap<>();
        map.put("orgaId", orgaId);
        map.put("orgaName", orgaName);
        map.put("sid", sid);
        map.put("tokenMD5", tokenMD5);
        map.put("personName", personName);
        long end = System.currentTimeMillis();
//        log.info("connectTime:" + (end - start) / 1000 + "s");
        loginTimeMap.put(index, (end - start));
        return map;
    }

    public String PostUrlAsString(String url, String post) {
        StringBuilder stringBuilder = new StringBuilder();
        HttpURLConnection connection = null;
        OutputStream os = null;
        InputStream is = null;
        InputStreamReader isr = null;
        try {
            connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setConnectTimeout(120 * 1000);//120s
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Linux; Android 8.0; ONEPLUS A3010 Build/OPR1.170623.032; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/58.0.3029.124 Mobile Safari/537.36");
            //  connection.setRequestProperty("Connection", "keep-alive");
            connection.setDoInput(true);
            //connection.setDoOutput(true);
            if (null == post) {
                connection.setRequestMethod("GET");
            }
            if (post != null) {
                connection.setDoOutput(true);
                connection.setRequestMethod("POST");
                os = connection.getOutputStream();
                os.write(post.replace(",", "%2C").getBytes());
                //osr.write(post.getBytes());
                os.flush();
                os.close();
            }
            // printHeader(connection);
            if (connection.getResponseCode() == HttpURLConnection.HTTP_CREATED || connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
/*                //获取cookie
                String headerField = connection.getHeaderField("Set-Cookie");
                String cookie = headerField.substring(0, headerField.indexOf(";"));*/
                is = connection.getInputStream();
                isr = new InputStreamReader(is);
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
        } finally {
            try {
                if (null != os) {
                    os.close();
                }
                if (null != is) {
                    is.close();
                }
                if (null != isr) {
                    isr.close();
                }
                if (null != connection) {
                    connection.disconnect();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * * 获取MD5加密
     * *
     * * @param pwd
     * *            需要加密的字符串
     * * @return String字符串 加密后的字符串
     */
    public String getMD5(String str) {
        try {
            // 创建加密对象
            MessageDigest digest = MessageDigest.getInstance("md5");

            // 调用加密对象的方法，加密的动作已经完成
            byte[] bs = digest.digest(str.getBytes());
            // 接下来，我们要对加密后的结果，进行优化，按照mysql的优化思路走
            // mysql的优化思路：
            // 第一步，将数据全部转换成正数：
            String hexString = "";
            for (byte b : bs) {
                // 第一步，将数据全部转换成正数：
                // 解释：为什么采用b&255
                /*
                 * b:它本来是一个byte类型的数据(1个字节) 255：是一个int类型的数据(4个字节)
                 * byte类型的数据与int类型的数据进行运算，会自动类型提升为int类型 eg: b: 1001 1100(原始数据)
                 * 运算时： b: 0000 0000 0000 0000 0000 0000 1001 1100 255: 0000
                 * 0000 0000 0000 0000 0000 1111 1111 结果：0000 0000 0000 0000
                 * 0000 0000 1001 1100 此时的temp是一个int类型的整数
                 */
                int temp = b & 255;
                // 第二步，将所有的数据转换成16进制的形式
                // 注意：转换的时候注意if正数>=0&&<16，那么如果使用Integer.toHexString()，可能会造成缺少位数
                // 因此，需要对temp进行判断
                if (temp < 16 && temp >= 0) {
                    // 手动补上一个“0”
                    hexString = hexString + "0" + Integer.toHexString(temp);
                } else {
                    hexString = hexString + Integer.toHexString(temp);
                }
            }
            return hexString;
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "";
    }

    public String getUUID32() {
        String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase();
        return uuid;
//  return UUID.randomUUID().toString().replace("-", "").toLowerCase();
    }
}
