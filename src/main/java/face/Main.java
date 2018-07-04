package face;

import MainTest.utlis.HTTPUtils;
import com.google.gson.*;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static MainTest.utlis.HTTPUtils.close;

public class Main {

    private static final String kuangshi_Cookie = "session=c8217fa6-2f52-43c0-912b-b3d511273d9c";
    private static final String authorization = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX2lkIjoiYWRtaW4iLCJ1c2VyX3JvbGUiOjEsImlhdCI6MTUyOTM4Nzk2MSwiZXhwIjoxNTI5MzkxNTYxfQ.mkbtXo-a3gOzbO6gvQHcS6l_e-VFSXFRr7pcDB1Wg6o";

    public static void main(String[] args) {

        System.out.println("start");
      //  kuangshiDelete();
        kuangshiDeleteNew();
        System.out.println("end");

        // dipingxianDelete();

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

    private static void kuangshiDeleteNew() {
        String list = HTTPUtils.Post("http://172.16.22.221/subject/list?category=employee&size=10&_=" + new Date().getTime(), null, kuangshi_Cookie);
        System.out.println(list);

        Gson gson = new Gson();
        ListBean listBean = gson.fromJson(list, ListBean.class);

     //   String url = "http://172.16.22.221/subject/list?category=employee&page=2&size=10&_=1530509023040";

        int total = listBean.getPage().getTotal();

        for (int i = 0; i < total; i++) {
            int index = i + 1;
            System.out.println("第"+i+"页");
            String listPage = HTTPUtils.Post("http://172.16.22.221/subject/list?category=employee&page=" + index + "&size=10&_=" + new Date().getTime(), null, kuangshi_Cookie);
            Gson gsonPage = new Gson();
            ListBean pageList = gsonPage.fromJson(listPage, ListBean.class);
            for (face.ListBean.DataBean dataBean : pageList.getData()) {
                int id = dataBean.getId();
                String result = HTTPUtils.Delete("http://172.16.22.221/subject/" + id, null, kuangshi_Cookie);
                System.out.println(result);
            }
        }


    }

    private static void kuangshiDelete() {
        String list = HTTPUtils.Post("http://10.10.9.119/subject/list?category=employee&size=10&_=" + new Date().getTime(), null, kuangshi_Cookie);
        System.out.println(list);

        Gson gson = new Gson();
        ListBean ListBean = gson.fromJson(list, ListBean.class);
        for (ListBean.DataBean dataBean : ListBean.getData()) {
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
