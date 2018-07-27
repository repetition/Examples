package MainTest.datacreate;

import MainTest.utlis.HTTPUtils;
import MainTest.utlis.Utils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class DataCreator {
    private static final Logger log = LoggerFactory.getLogger(DataCreator.class);
    private static final String HOST = "http://10.10.8.103:10011/thinkwin-cr/";

    private static final String login_URL = HOST + "login/auth";

    private static final String createOrg_URL = HOST + "orga/saveOrUpdateOrga";

    private static final String getOrgCode_URL = HOST + "orga/findOrgaCode";
    /**
     * 创建人员接口
     */
    private static final String createPerson_URL = HOST + "person/saveOrUpdatePerson";
    /**
     * 根据分组id获取所有此分组下的人员
     */
    private static final String getPersonByOrg_URL = HOST + "person/findPersonByNameAndOrga";
    /**
     * 创建用户接口，人员和用户关联
     */
    private static final String createUser_URL = HOST + "user/saveOrUpdateUser";
    /**
     * 人员授权 接口
     */
    private static final String createPersonRole_URL = HOST + "personRole/batchSavePersonRole";



    private static final String deletePersonRole_URL =HOST+"personRole/deletePersonRoleByPersonIdAndRoleIds";


    private static String currentOrgId = null;

    public static List<String> createOrg(String orgName) {
        log.info("正在创建机构");
        String en = Utils.encoderEN(orgName);
        String getOrgCode_Param = "orgName=" + en + "&baseSessionUserId=sys04";
        String cookie = getCookie();

        String orgCdoe = HTTPUtils.Post(getOrgCode_URL, getOrgCode_Param, cookie);
        JsonParser parser = new JsonParser();
        String responseObject = parser.parse(orgCdoe).getAsJsonObject().get("responseObject").getAsString();

        String createOrg_Param = "parentOrga.id=1001&parentOrga.orgName=" + Utils.encoderEN("Utils.encoderEN(orgName)") + "&orgType=1&orgName=" + en + "&orgNickname=" + en + "&orgCode=" + responseObject + "&toggle=function(b%2Ca)%7Breturn%20this%3D%3Db%3Fa%3Ab%7D&colorRgb=function()%7B%7D&status=1&baseSessionUserId=sys04";
        String createOrg_Result = HTTPUtils.Post(createOrg_URL, createOrg_Param, cookie);
        log.info(createOrg_Result);
        String orgId = parser.parse(createOrg_Result).getAsJsonObject().get("responseObject").getAsJsonObject().get("id").getAsString();
        String orgNames = parser.parse(createOrg_Result).getAsJsonObject().get("responseObject").getAsJsonObject().get("orgName").getAsString();
        currentOrgId = orgId;
        List<String> list = new ArrayList<>();
        list.add(orgId);
        list.add(orgName);
        return list;
    }

    public static void createPerson(int start,int end, String orgId, String orgName) {
        String cookie = getCookie();
        log.info("创建人员");
        for (int i = start; i < end; i++) {
            String personName = "TEST_" + i;
            String createPerson_Param = "personName=" + personName + "&sex=0&orga.id=" + orgId + "&orga.orgName=" + Utils.encoderEN(orgName) + "&manager_status=0&tel=15321254" + i + "&email=1115746521%40qq.com&baseSessionUserId=sys04";
            String create_Result = HTTPUtils.Post(createPerson_URL, createPerson_Param, cookie);
            log.info(create_Result);
        }
    }

    public static void createUser(String orgId) {

        log.info("创建用户");
        String cookie = getCookie();

        String findPersonByOrg_Param = "orgaId=" + orgId + "&name=&baseSessionUserId=sys04";
        String persons_Result = HTTPUtils.Post(getPersonByOrg_URL, findPersonByOrg_Param, cookie);

        JsonParser parser = new JsonParser();
        JsonArray asJsonArray = parser.parse(persons_Result).getAsJsonArray();
        for (JsonElement jsonElement : asJsonArray) {
            JsonObject asJsonObject = jsonElement.getAsJsonObject();
            String id = asJsonObject.get("id").getAsString();
            String personName = asJsonObject.get("personName").getAsString();
            String createUser_Param = "userName=" + personName + "&person.id=" + id + "&person.personName=" + personName + "&baseSessionUserId=sys04";
            String createUser_Result = HTTPUtils.Post(createUser_URL, createUser_Param, cookie);
            log.info(createUser_Result);

         /*   try {
                Thread.sleep(100L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/
        }

    }

    public static void createPersonRole(String orgId) {
        log.info("创建授权");
        String cookie = getCookie();

        String findPersonByOrg_Param = "orgaId=" + orgId + "&name=&baseSessionUserId=sys04";
        String persons_Result = HTTPUtils.Post(getPersonByOrg_URL, findPersonByOrg_Param, cookie);


        JsonParser parser = new JsonParser();
        JsonArray asJsonArray = parser.parse(persons_Result).getAsJsonArray();
        for (JsonElement jsonElement : asJsonArray) {
            JsonObject asJsonObject = jsonElement.getAsJsonObject();
            String id = asJsonObject.get("id").getAsString();
            String personName = asJsonObject.get("personName").getAsString();

            String createPersonRole_Param = "person.id=" + id + "&roles%5B0%5D.id=sys05&baseSessionUserId=sys04";
            String createPersonRole_Result = HTTPUtils.Post(createPersonRole_URL, createPersonRole_Param, cookie);
            log.info(createPersonRole_Result);

         /*   try {
                Thread.sleep(100L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/
        }
    }

    public static void deletePersonRole(String orgId) {
        String cookie = getCookie();
        String findPersonByOrg_Param = "orgaId=" + orgId + "&name=&baseSessionUserId=sys04";
        String persons_Result = HTTPUtils.Post(getPersonByOrg_URL, findPersonByOrg_Param, cookie);


        JsonParser parser = new JsonParser();
        JsonArray asJsonArray = parser.parse(persons_Result).getAsJsonArray();
        for (JsonElement jsonElement : asJsonArray) {
            JsonObject asJsonObject = jsonElement.getAsJsonObject();
            String id = asJsonObject.get("id").getAsString();
            String personName = asJsonObject.get("personName").getAsString();

            String deletePersonRole_Param = "personId=" + id + "&roleIds=sys05&baseSessionUserId=sys04";

            String createPersonRole_Result = HTTPUtils.Post(deletePersonRole_URL, deletePersonRole_Param, cookie);
            log.info(createPersonRole_Result);

         /*   try {
                Thread.sleep(100L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/
        }

    }


    public static Map<String, Object> login() {
        String login_Param = "userName=admin&password=admin&iscookie=0";
        Map<String, Object> map = HTTPUtils.PostUrlAsMap(login_URL, login_Param);
        log.info("登录成功");
        return map;
    }

    public static String getCookie() {
        Map<String, Object> map = login();
        String cookie = (String) map.get("cookie");
        String result = (String) map.get("result");
        return cookie;
    }


}
