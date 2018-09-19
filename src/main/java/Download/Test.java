package Download;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Test {

    public static void main(String[] args) {


      //  String json = "{returnStr={code=1, msg=完成指定工作项失败！, result=null}, currentState=2.0, responseCode=200.0}";
        String json = "{\"code\":\"1\", \"msg\":\"完成指定工作项失败！\",\" result\":\"null\"}";


        JsonParser jsonParser = new JsonParser();

        JsonObject asJsonObject = jsonParser.parse(json).getAsJsonObject();


        String returnStrObj = asJsonObject.get("code").getAsString();


        System.out.println(returnStrObj);


    }
}
