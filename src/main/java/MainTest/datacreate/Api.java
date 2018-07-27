package MainTest.datacreate;

public class Api {

  //  public static final String HOST = "http://218.26.226.125:10000/";
    public static final String HOST = "http://10.10.10.214/";
    //登录
    public static final String login_MobilURL = HOST + "mobileapp/login";
    //签到 （旧版）
    public static final String meeting_Sign_URL = HOST + "mobile/meetingSign/signMeetingByMeetingId";
    //获取会议参会人
    public static final String getAttendeesMobilURL = HOST + "mobile/meetingQuery/findMeetingInfo";
    //当前会议id
    public static final String loginLogout_MobilURL= HOST + "mobileapp/logout";


    public static final String meetingRoomHtml_URL = HOST + "conference/sign/yanqian.html";
    //css、js
    public static final String meetingRoomHtmlCSS1_URL = HOST + "conference/sign/css/reset.css";
    public static final String meetingRoomHtmlCSS2_URL = HOST + "conference/sign/css/style.css";
    public static final String meetingRoomHtmlJS1_URL = HOST + "dist/lib/zepto.min.js";
    public static final String meetingRoomHtmlJS2_URL = HOST + "conference/sign/js/js.js";
    //验证前获取当前会议室信息列表 接口
    public static final String meetingRoom_loadMeetingRoomById_URL = HOST + "meetingRoomSignUp/loadMeetingRoomById";
    //验证前获取当前会议室 会议列表息列表 接口
    public static final String meetingRoom_findMeetingByMeetingRoomId_URL = HOST + "meetingRoomSignUp/findMeetingByMeetingRoomId";

    //签到成功页面
    public static final String yanhouHtml_URL = HOST + "conference/sign/yanhou.html";
    //H5签到接口
    public static final String yanhou_sign_URL = HOST + "meetingRoomSignUp/findMeetingById";


    //资料
    public static final String meetingResourceList_URL = HOST + "mobile/resource/findMeetingMaterials";



}
