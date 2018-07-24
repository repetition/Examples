package MainTest.datacreate;

import MainTest.utlis.HTTPUtils;
import MainTest.utlis.Utils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

import static MainTest.datacreate.Api.getAttendeesMobilURL;
import static MainTest.datacreate.Api.login_MobilURL;
import static MainTest.datacreate.Api.meeting_Sign_URL;

public class PressureTest {
    private static final Logger log = LoggerFactory.getLogger(PressureTest.class);
    public static final ThreadPoolExecutor executor = new ThreadPoolExecutor(20, 30, 1L, TimeUnit.SECONDS, new LinkedBlockingDeque<Runnable>());
    //当前会议id
   // public static final String meetingId = "4028496864b2ad860164b2b4a095001d"; //500
   // public static final String meetingId = "4028496864b2ad860164b2c73ec02b1e"; //400
     public static final String meetingId = "4028496864b2ad860164b307372e40e4"; //1000

    //签到成功
    public static int failCount = 0;
    //签到失败
    public static int successCount = 0;
    //登录成功
    public static int loginCount = 0;
    //登录失败
    public static int loginFailCount = 0;


    public static int downSuccess = 0;
    public static int downFail = 0;

    public static void main(String[] args) {
        //下载资料
       // resourceDown();
        //签到
          signMeeting();
    }

    public static void resourceDown() {
        File[] files = new File("F:\\ResourceDown\\").listFiles();
        for (File file : files) {
            file.delete();
        }
        long start = System.currentTimeMillis();
        //登录
       // Map<String, String> stringStringMap = loginForMobile("admin", "admin");
        Map<String, String> stringStringMap = loginForMobile("hgcs01", "hgcs01");
        //获取所有参会人
        JsonObject jsonObject = getAttendeesByMeeting(stringStringMap);
        JsonObject responseObject = jsonObject.getAsJsonObject("responseObject");
        JsonArray innerAttendee = responseObject.get("innerAttendee").getAsJsonArray();
        String meetingRoomId = responseObject.get("meetingRoom").getAsJsonObject().get("id").getAsString();

        // 建立ExecutorService线程池
        ExecutorService exec = Executors.newFixedThreadPool(innerAttendee.size()-1);
        // 计数器
        CountDownLatch countDownLatch = new CountDownLatch(innerAttendee.size());
        ConcurrentHashMap<Integer, ThreadRecord> threadMap = new ConcurrentHashMap<Integer, ThreadRecord>();
        //登录时间集合
        ConcurrentHashMap<Integer, Long> loginTimeMap = new ConcurrentHashMap<Integer, Long>();
        //下载时间集合
        ConcurrentHashMap<Integer, Long> downTimeMap = new ConcurrentHashMap<>();

        ConcurrentHashMap<Integer, Long> listTimeMap = new ConcurrentHashMap<>();

        for (int i = 0; i < innerAttendee.size(); i++) {
            JsonObject asJsonObject = innerAttendee.get(i).getAsJsonObject();
            String id = asJsonObject.get("id").getAsString();
            String name = asJsonObject.get("name").getAsString();
            String personId = asJsonObject.get("personId").getAsString();
            log.info("参会人：" + "id:" + id + "- name:" + name + " - personId:" + personId);
            if (name.equals("系统管理员")) {
                continue;
            }
            ResourceDownRunnable runnable = new ResourceDownRunnable(i, countDownLatch, threadMap, loginTimeMap, asJsonObject, downTimeMap);
            exec.submit(runnable);
            // new Thread(runnable).start();
        }

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long end = System.currentTimeMillis();
        log.info("登录成功：" + loginCount + " - 失败：" + loginFailCount);
        log.info("总耗时 ：" + (end - start) / 1000 + "s");
        log.info("fail:" + failCount + " success:" + successCount);

        long loginTotalTime = 0;
        for (Map.Entry<Integer, Long> longEntry : loginTimeMap.entrySet()) {
            Long value = longEntry.getValue();
            loginTotalTime += value;
        }
        log.info(loginTimeMap.size() + "");
       // log.info("登录平均耗时：" + (loginTotalTime / loginTimeMap.size()) / 1000 + "s");
        log.info("登录平均耗时：" + (loginTotalTime / loginTimeMap.size())  + "ms");

        long downTotalTime = 0;
        for (Map.Entry<Integer, Long> longEntry : downTimeMap.entrySet()) {
            Long value = longEntry.getValue();
            downTotalTime += value;
        }
        log.info("下载成功：" + downSuccess + " - 失败：" + downFail);

        log.info(loginTimeMap.size() + "");
        int fileLength = new File("F:\\ResourceDown\\").listFiles().length;
      //  log.info("下载平均耗时：" + (downTotalTime / downTimeMap.size()) / 1000 + "s" + " - 下载总数：" + fileLength);
        log.info("下载平均耗时：" + (downTotalTime / downTimeMap.size())  + "ms" + " - 下载总数：" + fileLength);

        exec.shutdown();

    }

    private static void signMeeting() {
        long start = System.currentTimeMillis();
        //登录
        Map<String, String> stringStringMap = loginForMobile("admin", "admin");
        //获取所有参会人
        JsonObject jsonObject = getAttendeesByMeeting(stringStringMap);
        JsonObject responseObject = jsonObject.getAsJsonObject("responseObject");
        JsonArray innerAttendee = responseObject.get("innerAttendee").getAsJsonArray();
        String meetingRoomId = responseObject.get("meetingRoom").getAsJsonObject().get("id").getAsString();

        // 建立ExecutorService线程池
        ExecutorService exec = Executors.newFixedThreadPool(innerAttendee.size());
        // 计数器
        CountDownLatch countDownLatch = new CountDownLatch(innerAttendee.size() - 1);
        ConcurrentHashMap<Integer, ThreadRecord> threadMap = new ConcurrentHashMap<Integer, ThreadRecord>();
        ConcurrentHashMap<Integer, Long> loginTimeMap = new ConcurrentHashMap<Integer, Long>();
        ConcurrentHashMap<Integer, Long> signTimeMap = new ConcurrentHashMap<>();

        ConcurrentHashMap<Integer, Map<String,Long>> listTimeMap = new ConcurrentHashMap<>();

        for (int i = 0; i < innerAttendee.size(); i++) {
            JsonObject asJsonObject = innerAttendee.get(i).getAsJsonObject();
            String id = asJsonObject.get("id").getAsString();
            String name = asJsonObject.get("name").getAsString();
            String personId = asJsonObject.get("personId").getAsString();
            log.info("参会人：" + "id:" + id + "- name:" + name + " - personId:" + personId);
            if (name.equals("系统管理员")) {
                continue;
            }
            SignRunnable runnable = new SignRunnable(i, countDownLatch, threadMap, loginTimeMap, asJsonObject, meetingRoomId, signTimeMap,listTimeMap);
            exec.submit(runnable);
            // new Thread(runnable).start();
        }
/*
        new Thread(){
            @Override
            public void run() {
                while (true){
                    try {
                        Thread.sleep(1000L);
                        log.info(countDownLatch.getCount()+"");

                        if (countDownLatch.getCount()==0){
                            break;
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();*/

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long end = System.currentTimeMillis();
        log.info("登录成功：" + loginCount + " - 失败：" + loginFailCount);
        log.info("总耗时 ：" + (end - start) / 1000 + "s");
            log.info("fail:" + failCount + " success:" + successCount);
        long totalTime = 0;
        for (Map.Entry<Integer, Long> longEntry : loginTimeMap.entrySet()) {
            Long value = longEntry.getValue();
            totalTime += value;
        }
        log.info(loginTimeMap.size() + "");
        log.info("登录成功：" + loginTimeMap.size() + " - 失败：" + (innerAttendee.size()-1-loginTimeMap.size()));
        log.info("登录平均耗时：" + (totalTime / loginTimeMap.size()) / 1000 + "s");

        long signTotalTime = 0;
        for (Map.Entry<Integer, Long> longEntry : signTimeMap.entrySet()) {
            Long value = longEntry.getValue();
            signTotalTime += value;
        }

        log.info("签到成功："+signTimeMap.size() + " - 失败："+(loginTimeMap.size()-signTimeMap.size()));
        log.info("签到平均耗时：" + (signTotalTime / signTimeMap.size()) / 1000 + "s");
    }


    public synchronized static Map<String, String> loginForMobile(String user, String pass) {
        String login_Param = "password=" + user + "&equipmentCode=cc8325677f6945a9&lang=zh_CN&equipmentType=Android&username=" + pass + "&equipmentOsVersion=8.0.0&token=d6d4f6c27f5ddadf2d4bd18ffb1d3977&sid=sys041530688064415";
        String login_Result = HTTPUtils.PostUrlAsString(login_MobilURL, login_Param);
//        log.info(login_Result);
        if (login_Result.equals("fail")) {
            return null;
        }
        JsonParser parser = new JsonParser();
        JsonObject asJsonObject = parser.parse(login_Result).getAsJsonObject();
        String orgaId = asJsonObject.get("orgaId").getAsString();
        String orgaName = asJsonObject.get("orgaName").getAsString();
        String sid = asJsonObject.get("sid").getAsString();
        String token = asJsonObject.get("token").getAsString();
        String tokenMD5 = Utils.getMD5(token + "810ThinkWin811");

        Map<String, String> map = new HashMap<>();
        map.put("orgaId", orgaId);
        map.put("orgaName", orgaName);
        map.put("sid", sid);
        map.put("tokenMD5", tokenMD5);
        return map;
    }


    public synchronized static JsonObject getAttendeesByMeeting(Map<String, String> map) {
        String tokenMD5 = map.get("tokenMD5");
        String sid = map.get("sid");
        String getAttendees_Param = "meetingId=" + meetingId + "&token=" + tokenMD5 + "&sid=" + sid;
        String attendees_Result = HTTPUtils.PostUrlAsString(getAttendeesMobilURL, getAttendees_Param);
        JsonParser parser = new JsonParser();
        JsonObject asJsonObject = parser.parse(attendees_Result).getAsJsonObject();
/*        JsonObject responseObject = asJsonObject.getAsJsonObject("responseObject");
        JsonArray innerAttendee = responseObject.get("innerAttendee").getAsJsonArray();*/
        return asJsonObject;
    }

    public synchronized static boolean meetingSign(Map<String, String> map) {
        String tokenMD5 = map.get("tokenMD5");
        String sid = map.get("sid");
        String sign_Param = "meetingId=" + meetingId + "&token=" + tokenMD5 + "&sid=" + sid;
        String sign_Result = HTTPUtils.PostUrlAsString(meeting_Sign_URL, sign_Param);
        log.info(sign_Result);
        JsonParser parser = new JsonParser();
        JsonObject asJsonObject = parser.parse(sign_Result).getAsJsonObject();
        boolean success = asJsonObject.get("success").getAsBoolean();
        return success;
    }

    static class ThreadRecord {
        long start;
        long end;

        public ThreadRecord(long start, long end) {
            this.start = start;
            this.end = end;
        }
    }


}
