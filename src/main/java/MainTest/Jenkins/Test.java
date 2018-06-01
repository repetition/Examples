package MainTest.Jenkins;

import MainTest.utlis.HTTPUtils;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Test {

    public static void main(String[] args) {


        String string = HTTPUtils.PostUrlAsString("http://y.qq.com/n/yqq/song/001J5QJL1pRQYB.html", null);

        System.out.println(string);


    }
}
