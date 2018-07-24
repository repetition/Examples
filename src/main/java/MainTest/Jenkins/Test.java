package MainTest.Jenkins;

import MainTest.utlis.HTTPUtils;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Test {

    public static void main(String[] args) {


  /*      String string = HTTPUtils.PostUrlAsString("http://y.qq.com/n/yqq/song/001J5QJL1pRQYB.html", null);

        System.out.println(string);*/

        String old_str = "2018-07-17";
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            String str = format.format(new Date());
            long time = format.parse(str).getTime();

            long parse = format.parse(old_str).getTime();


            System.out.println(str+":"+time+"  -  "+old_str + ":" + parse);

        } catch (ParseException e) {
            e.printStackTrace();
        }


    }
}
