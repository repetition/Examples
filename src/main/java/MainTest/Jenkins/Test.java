package MainTest.Jenkins;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Test {

    public static void main(String[] args) {
        String json = "{\"errorMessage\":\"org.springframework.web.util.NestedServletException: Request processing failed; nested exception is java.lang.Exception: 1002; nested exception is org.springframework.web.util.NestedServletException: Request processing failed; nested exception is java.lang.Exception: 1002\",\"message\":\"操作失败.\",\"success\":false}";

        JsonParser jsonParser = new JsonParser();

        JsonObject jsonObj = (JsonObject) jsonParser.parse(json);

        JsonElement success = jsonObj.get("success");

        System.out.println(success.isJsonNull());


    }
}
