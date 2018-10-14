import MainTest.utlis.HTTPUtils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.commons.beanutils.BeanUtils;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class BaiDuPan {
    static String url = "https://pan.baidu.com/api/categorylist?order=size&desc=1&showempty=0&web=1&page=1&num=100&category=6&t=0.5927081984205158&channel=chunlei&web=1&app_id=250528&bdstoken=fac570f6c9059e725e7212d6ede28141&logid=MTUzOTMzNTg3NDI3MDAuMjgxODA3MDQyMzI0Mjk2NDM=&clienttype=0";
    static String post = "order=size&desc=1&showempty=0&web=1&page=1&num=100&category=6&t=0.5927081984205158&channel=chunlei&web=1&app_id=250528&bdstoken=fac570f6c9059e725e7212d6ede28141&logid=MTUzOTMzNTg3NDI3MDAuMjgxODA3MDQyMzI0Mjk2NDM=&clienttype=0";
    public static void main(String[] args) throws InvocationTargetException, IllegalAccessException {
        String cookie = readCookie();
        String post = HTTPUtils.Post(url, BaiDuPan.post, cookie);


        JsonParser parser = new JsonParser();
        JsonObject jsonObject = parser.parse(post).getAsJsonObject();

        Gson gson = new Gson();
        ResBean resBean = gson.fromJson(post, ResBean.class);
        System.out.println(resBean+"");
        List<ResBean.InfoBean> infoBeans = resBean.getInfo();
        Collections.sort(infoBeans,new MyComparator());
        List<ResBean.NewInfoBean> newInfo = new ArrayList<>();
        for (ResBean.InfoBean infoBean : infoBeans) {
            long size = infoBean.getSize();
            ResBean.NewInfoBean newInfoBean = new ResBean.NewInfoBean();
            BeanUtils.copyProperties(newInfoBean,infoBean);

            newInfoBean.setSizeStr(SizeConverter.convertBytes(size,false));
            newInfo.add(newInfoBean);
        }

        System.out.println(gson.toJson(infoBeans));
        System.out.println(gson.toJson(newInfo));

    }


    static class MyComparator implements Comparator<ResBean.InfoBean> {

        @Override
        public int compare(ResBean.InfoBean o1, ResBean.InfoBean o2) {

            if (o2.getSize()<= o1.getSize()) {
                return 1;
            }
            return 0;
        }
    }

    private static String readCookie() {
        try {
            FileInputStream fis = new FileInputStream("D:\\Desktop\\cookie.txt");
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            String line;
            StringBuilder stringBuilder = new StringBuilder();
            while ((line = br.readLine()) != null) {
                stringBuilder.append(line);
            }
            return stringBuilder.toString();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
