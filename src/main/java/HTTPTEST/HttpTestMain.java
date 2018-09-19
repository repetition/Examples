package HTTPTEST;

import MainTest.utlis.HTTPUtils;
import MainTest.utlis.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sound.midi.Soundbank;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class HttpTestMain {
    private static final Logger log = LoggerFactory.getLogger(HttpTestMain.class);
    private static final String host= "http://repetition.free.idcfengye.com/";
   // private static final String host= "http://10.10.9.95:8080/";
    private static final String api_queryAllMissionListInfo= host+"mission/queryAllMissionListInfo";

    private static final String api_login = host+ "login/auth";


    private static final String key = "od3J6Efns2ZNQvN0dskwKFe6T6l8FKisFbj9THZ%2Bh%2BpnwoxKvkhC6bHW%2F6kMpHr%2BBCfaKW3%2FaTNu%0D%0A%2FLQhSaHkBC4iJH3Dj%2BZUALvJ5P%2BPEMyFCpmWa0TA%2BIFubyZ0oXOr5ZG7j4zsEwf867k0xd5CpX8o%0D%0AcjC%2FSwE0F10FroQpfVc%3D";

    public static void main(String[] args) {


     //   login();
        try {
            queryAllMissionListInfo();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        //   String md5 = Utils.getMD5("admin");

      //  log.info(md5);
    }


    private static void queryAllMissionListInfo() throws UnsupportedEncodingException {

        Map<String,String> parameterMap = new HashMap<>();
        //页码
        parameterMap.put("pageNo","");
        //每页条数
        parameterMap.put("pageSize","");
        //负责人名称
        String encode = URLEncoder.encode("系统管理员", "utf-8");
       // parameterMap.put("manager",encode);
        parameterMap.put("manager","");
        //负责部门名称
        parameterMap.put("dept","");
        //任务名称
        parameterMap.put("missionName","");
        //任务内容
        parameterMap.put("missionContent","");
        //会议时间 精确到天
        parameterMap.put("meetingTime","");
        //任务开始时间 精确到天
        parameterMap.put("beginTime","");
        //任务结束时间 精确到天
        parameterMap.put("endTime","");
        //任务状态 草稿 draft 未开始 before 执行中 start 已取消 cancel 完成 finish  交付 delivery
        parameterMap.put("status","");
        //排序： 0.倒序 1.正序     默认倒序
        parameterMap.put("sort","");
        //排序类型 0.创建时间 1.会议时间 2.负责人 3.负责部门 4.任务开始时间 5.任务结束时间
        parameterMap.put("sortType","");
        //任务类型  0.全部 1.会议任务 2.其他任务     不传默认查全部
        parameterMap.put("missionType","0");
        //任务类型   不传默认查全部0.全部 1.未完成（未开始、执行中） 2.已完成（取消、交付、完成）
        parameterMap.put("missionState","");
        //是否包含草稿 0.不包含 1.包含
        parameterMap.put("hasDratf","");
        //任务发起人姓名
        parameterMap.put("sponsor","");
        //预留字段
        parameterMap.put("content","");
        //加密key
        parameterMap.put("key",key);

        String parameters = HTTPUtils.formatPostParameters(parameterMap);
        //post
        ///String result = HTTPUtils.PostUrlAsString(api_queryAllMissionListInfo, parameters);
        //get
         String result = HTTPUtils.PostUrlAsString(api_queryAllMissionListInfo+"?"+parameters, null);
        System.out.println(result);
    }


    private static void login() {
        Map<String,String> loginMap = new HashMap<>();
        loginMap.put("userName","admin");
        loginMap.put("password","admin");
        loginMap.put("iscookie","0");
        String parameters = HTTPUtils.formatPostParameters(loginMap);
        String string = HTTPUtils.PostUrlAsString(api_login, parameters);

        log.info(string);
    }
}
