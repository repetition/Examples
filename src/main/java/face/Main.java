package face;

import MainTest.utlis.HTTPUtils;
import com.google.gson.*;
import sun.rmi.runtime.Log;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static MainTest.utlis.HTTPUtils.close;

public class Main {

    private static final String kuangshi_Cookie = "session=35ae2f4f-e3aa-457e-8f2f-eb112c410dea";
    private static final String authorization = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX2lkIjoiYWRtaW4iLCJ1c2VyX3JvbGUiOjEsImlhdCI6MTUyOTM4Nzk2MSwiZXhwIjoxNTI5MzkxNTYxfQ.mkbtXo-a3gOzbO6gvQHcS6l_e-VFSXFRr7pcDB1Wg6o";

    public static void main(String[] args) {


      //  kuangshiDelete();


       dipingxianDelete();

    }

    private static void dipingxianDelete() {
        String list = Post("http://10.10.11.87:8098/api/user/query?page_num=1&page_size=10&user_name=", null, authorization);
        System.out.println(list);

        Gson gson = new Gson();
        DiPingXianListBean diPingXianListBean = gson.fromJson(list, DiPingXianListBean.class);


        for (DiPingXianListBean.DataBean.ResultBean resultBean : diPingXianListBean.getData().getResult()) {
            String user_id = resultBean.getUser_id();

            List<String> userList = new ArrayList<>();
            userList.add(user_id);

            DeleteBean deleteBean = new DeleteBean();
            deleteBean.setUser_id(userList);
            String json = gson.toJson(deleteBean);
            System.out.println(json);

            String result = Post("http://10.10.11.87:8098/api/user/delete", json, authorization);
            System.out.println(result);
        }
    }


    private static void kuangshiDelete() {
        String list = HTTPUtils.Post("http://10.10.9.119/subject/list?category=employee&size=10&_=" + new Date().getTime(), null, kuangshi_Cookie);
        System.out.println(list);

        Gson gson = new Gson();
        listBean listBean = gson.fromJson(list, listBean.class);
        for (face.listBean.DataBean dataBean : listBean.getData()) {
            int id = dataBean.getId();
            String result = HTTPUtils.Delete("http://10.10.9.119/subject/" + id, null, kuangshi_Cookie);
            System.out.println(result);
        }
    }


    public static String Post(String url, String post, String authorization) {

        StringBuilder stringBuilder = new StringBuilder();
        HttpURLConnection connection = null;
        InputStream is = null;
        InputStreamReader isr = null;
        BufferedReader buffer = null;
        try {
            connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setConnectTimeout(11200);
            connection.setDoInput(true);
            connection.setRequestProperty("Accept", "application/json, text/plain, */*");
            connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            connection.setRequestProperty("Authorization", authorization);
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.181 Safari/537.36");
            if (null != post) {
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
                OutputStream osr = connection.getOutputStream();
                osr.write(post.getBytes(Charset.forName("utf-8")));
                osr.flush();
                osr.close();
            } else {
                connection.setRequestMethod("GET");
            }

            if (connection.getResponseCode() == 200) {
                is = connection.getInputStream();
                isr = new InputStreamReader(is);
                buffer = new BufferedReader(isr);
                String line;
                while ((line = buffer.readLine()) != null) {
                    stringBuilder.append(line);
                }
                return stringBuilder.toString();
            }
            return stringBuilder.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        } finally {
            close(buffer);
            close(isr);
            close(is);
        }
    }
}
