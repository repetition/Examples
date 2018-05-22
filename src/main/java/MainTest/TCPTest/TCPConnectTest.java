package MainTest.TCPTest;

import MainTest.utlis.HTTPUtils;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 请求终端的获取终端信息的测试请求api
 */
public class TCPConnectTest {
    private static String host = "http://10.10.9.234";
    private static String url_Login = host + "/login/auth";
    private static String url_findListPageByPower = host + "/terminal/findListPageByPower";
    private static String url_getDeviceInfo = host + "/terminal/getDeviceInfo";
    private static final Logger log = LoggerFactory.getLogger(TCPConnectTest.class);

    private static long currentTime = System.currentTimeMillis();

    private static long delayedTime;

    public static void main(String[] args) {


        Gson gson = new Gson();
        //登录
        String login_Parameter = "userName=admin&password=admin&iscookie=0&baseSessionUserId=sys04";
        Map<String, Object> map = HTTPUtils.PostUrlAsMap(url_Login, login_Parameter);
        String login_Result = (String) map.get("result");
        String cookie = (String) map.get("cookie");
        UserInfoBean userInfoBean = gson.fromJson(login_Result, UserInfoBean.class);
        log.info(login_Result);

        //获取终端列表
        String findTerminals_Parameter = "limit=10&baseSessionUserId=sys04";
        String terminalsStr = HTTPUtils.Post(url_findListPageByPower, findTerminals_Parameter, cookie);
        log.info(terminalsStr);
        TerminalsBean terminalsBean = gson.fromJson(terminalsStr, TerminalsBean.class);
        log.info(terminalsBean.toString() + "");


        List<TerminalsBean.ResultBean> resultBeans = terminalsBean.getResult();

        long start = System.currentTimeMillis();
        int success = 0;
        int fail = 0;
        Map<String, Object> failMap = new HashMap<>();

        delayedTime = TimeUtil.addDateMinutForDate(new Date(), 5).getTime();

        while ((currentTime - delayedTime) < 0) {
            currentTime = System.currentTimeMillis();
            for (TerminalsBean.ResultBean resultBean : resultBeans) {
                log.info("--------------------------------------------------");
                String id = resultBean.getId();
                String ip = resultBean.getIp();
                String name = resultBean.getName();
                log.info("TCP监测：" + ip + " - " + name);

                String terminal_Parameter = "terminalId=" + id + "&baseSessionUserId=sys04";
                String result = HTTPUtils.Post(url_getDeviceInfo, terminal_Parameter, cookie);

                DevicesInfoBean devicesInfoBean = gson.fromJson(result, DevicesInfoBean.class);

                if (null != devicesInfoBean.getResponseObject() && devicesInfoBean.getResponseObject().length() > 10) {
                    log.info("TCP监测：" + ip + " - " + name + "|成功");
                    log.info(devicesInfoBean.getResponseObject());
                    success++;
                } else {
                    log.info("TCP监测：" + ip + " - " + name + "|失败");
                    failMap.put("fails", "TCP监测：" + ip + " - " + name + "|失败");
                    fail++;
                }
                log.info("--------------------------------------------------");
            }
        }
        log.info("success:" + success + " - fail:" + fail);
        for (Map.Entry<String, Object> entry : failMap.entrySet()) {
            log.info(entry.getKey());
        }
        long end = System.currentTimeMillis();

        log.info("耗时：" + (end - start) / 1000 + "s");
        // String terminals = HTTPUtils.PostUrlAsString(url_findListPageByPower, findTerminals_Parameter);

    }

}
