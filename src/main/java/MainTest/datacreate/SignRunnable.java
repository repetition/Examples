package MainTest.datacreate;

import com.google.gson.JsonObject;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

import static MainTest.datacreate.PressureTest.failCount;
import static MainTest.datacreate.PressureTest.loginForMobile;
import static MainTest.datacreate.PressureTest.meetingSign;

public class SignRunnable implements Runnable {
    int index;
    CountDownLatch countDownLatch;
    ConcurrentHashMap<Integer, PressureTest.ThreadRecord> threadMap;
    JsonObject asJsonObject;

    public SignRunnable(int index, CountDownLatch countDownLatch, ConcurrentHashMap<Integer, PressureTest.ThreadRecord> threadMap, JsonObject asJsonObject) {
        this.index = index;
        this.countDownLatch = countDownLatch;
        this.threadMap = threadMap;
        this.asJsonObject = asJsonObject;
    }

    @Override
    public void run() {
        long sart = System.currentTimeMillis();
        String name = asJsonObject.get("name").getAsString();
        //参会人登录
        Map<String, String> attendeesMap = loginForMobile(name, name);

        if (null == attendeesMap) {
            countDownLatch.countDown();//减一
            failCount++;
            return;
        }

        //参会人签到
        boolean isSuccess = meetingSign(attendeesMap);
        if (!isSuccess) {
            countDownLatch.countDown();//减一
            failCount++;
            return;
        }


        long end = System.currentTimeMillis();
        threadMap.put(index, new PressureTest.ThreadRecord(sart, end));

    }


}
