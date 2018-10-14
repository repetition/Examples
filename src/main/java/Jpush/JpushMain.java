package Jpush;

import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.AndroidNotification;
import cn.jpush.api.push.model.notification.IosAlert;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;

import java.util.*;

public class JpushMain {
    private static final String appKey = "5757da7f0fd5ae06660670d1";
    private static final String masterSecret = "022f16324880cc2b7d086550";
    public static void main(String[] args) {

        JPushClient jPushClient = new JPushClient(masterSecret, appKey);
        try {
            List<String> list = new ArrayList<>();
            list.add("b70217198b95f1ea");
            list.add("71ee954aab68dfa1");
            list.add("1b2fef4e1a80222f");
            PushPayload pushPayload = buildPayload("222", "2333", list, null);
            // PushResult pushResult = jPushClient.sendPush(buildMessage_android());
            jPushClient.sendPush(pushPayload);
        } catch (APIConnectionException e) {
            e.printStackTrace();
        } catch (APIRequestException e) {
            e.printStackTrace();
        }


    }


    public static PushPayload buildPushObject_android_and_ios() {
        Map<String, String> extras = new HashMap<String, String>();
        extras.put("test", "https://community.jiguang.cn/push");


        PushPayload.Builder builder = PushPayload.newBuilder();
        //设置推送平台
        builder.setPlatform(Platform.android_ios());
        //设置推送设备
        builder.setAudience(Audience.alias("b70217198b95f1ea"));

        Notification.Builder notification = Notification.newBuilder();
        notification.setAlert("通知");
        notification.addPlatformNotification(AndroidNotification.newBuilder().setTitle("内容").build());
        builder.setNotification(notification.build());
        return builder.build();
    }

    public static PushPayload buildMessage_android() {
        Map<String, String> extras = new HashMap<String, String>();
        extras.put("test", "https://community.jiguang.cn/push");


        PushPayload.Builder builder = PushPayload.newBuilder();
        //设置推送平台
        builder.setPlatform(Platform.android());
        //设置推送设备
        builder.setAudience(Audience.alias("1b2fef4e1a80222f"));

        Message content = Message.content("12345");
        builder.setMessage(content);
        return builder.build();
    }

    public static PushPayload buildPayload(String title, String content, List<String> alias, Map<String, Object> extras)
    {
/*
        Map newRetMap = new HashMap();
        Set keySet = extras.keySet();
        for (String mayKey : keySet) {
            newRetMap.put(mayKey, extras.get(mayKey).toString());
        }
*/

        IosAlert iosAlert = IosAlert.newBuilder()
                .setTitleAndBody(title, "", content)
                .build();

        PushPayload payload = PushPayload.newBuilder()
                .setPlatform(Platform.android_ios())
                .setAudience(Audience.alias(alias))
                .setNotification(Notification.newBuilder()
                        .setAlert("")
                        .addPlatformNotification(
                                ((AndroidNotification.Builder)((AndroidNotification.Builder)AndroidNotification.newBuilder()
                                        .setTitle(title)
                                        //.addExtras(newRetMap))
                                        )
                                        .addExtra("content", content))
                                        .build())
                        .addPlatformNotification(
                                ((IosNotification.Builder)IosNotification.newBuilder()
                                        .setAlert(iosAlert)
                                      //  .addExtras(newRetMap))
                                        )
                                        .build())
                        .build())
                .build();

        return payload;
    }
}
