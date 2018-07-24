package MainTest.terminalTest;

import MainTest.utlis.HTTPUtils;
import MainTest.utlis.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TerminalQueueTest {
    private static final Logger log = LoggerFactory.getLogger(TerminalQueueTest.class);

    public static String host = "http://10.10.9.241/";
    public static String login_url = host +"login/auth";
    public static String spotMsg_url = host+"spotMessage/saveOrUpdate";
    private static String cookie;

    public static void main(String[] args) {

        String login_param = "userName=admin&password=admin&iscookie=0&baseSessionUserId=sys04";
        Map<String, Object> login_map = HTTPUtils.PostUrlAsMap(login_url, login_param);
        cookie = (String) login_map.get("cookie");

        spotMsg();
    }

    private static void spotMsg() {
        List<String> stringList = new ArrayList<>();
        stringList.add("contentStyle="+Utils.encoderEN("height:80px;line-height:80px;font-family:宋体;color:#FFFFFF;background-color:#1C2842;font-size:28px"));
        stringList.add("content="+Utils.encoderEN("队列测试"));
        stringList.add("timeType="+"second");
        stringList.add("timeLength="+"20");
        stringList.add("playSpeed="+"normal");
        stringList.add(Utils.encoderEN("terminalMessages[0].terminal.id")+"="+"8a8a89716458942e016458c510090073");
        stringList.add("baseSessionUserId="+"sys04");

        String parameter = createParameter(stringList);
        String spotMsgResult = HTTPUtils.Post(spotMsg_url, parameter,cookie);
        log.info(spotMsgResult);
    }

    private static String createParameter(List<String> stringList) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < stringList.size(); i++) {
            String str = stringList.get(i);
            if (i==stringList.size()-1){
                builder.append(str);
                continue;
            }
            builder.append(str+"&");
        }
        return builder.toString();
    }
}
