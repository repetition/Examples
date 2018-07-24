package MainTest.terminalTest;

import MainTest.utlis.HTTPUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

public class TerminalTest {
    private static final Logger log = LoggerFactory.getLogger(TerminalTest.class);
    public static final ThreadPoolExecutor executor = new ThreadPoolExecutor(5, 1000, 1L, TimeUnit.SECONDS, new LinkedBlockingDeque<Runnable>());
    public static final ExecutorService executorService = Executors.newCachedThreadPool();
    public static String login_url = "http://10.10.9.241/login/auth";
    public static String startTerminalTask_url = "http://10.10.9.241/terminalTask/startTerminalTask";
    public static String stopTerminalTask_url = "http://10.10.9.241/terminalTask/stopTerminalTask";
    public static String cookie;

    public static void main(String[] args) {
        CountDownLatch countDownLatch = new CountDownLatch(100);
        String login_param = "userName=admin&password=admin&iscookie=0&baseSessionUserId=sys04";
        TerminalTest terminalTest = new TerminalTest();
        Map<String, Object> postUrlAsMap = terminalTest.PostUrlAsMap(login_url, login_param);
        cookie = (String) postUrlAsMap.get("cookie");

/*        for (int i = 0; i < 100; i++) {

            StartTask startTask = new StartTask(i);
            StopTask stopTask = new StopTask(i);
            executor.execute(startTask);
            executor.execute(stopTask);
            new Thread(startTask).start();
            new Thread(stopTask).start();
        }*/


        long start = System.currentTimeMillis();
        for (int i = 0; i < 15; i++) {
            try {
                Thread.sleep(500L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            String startTerminalTask_param = "id=8a8a8971647d101201647d71c5630009&baseSessionUserId=sys04";
            String startResult = terminalTest.Post(startTerminalTask_url, startTerminalTask_param, cookie);
            log.info("start:" + startResult);
            try {
                Thread.sleep(500L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            String stopTerminalTask_param = "id=8a8a8971647d101201647d71c5630009&baseSessionUserId=sys04";
            String stopResult = terminalTest.Post(stopTerminalTask_url, stopTerminalTask_param, cookie);
            log.info("stop:" + stopResult);

        }


/*        for (int i = 0; i < 100; i++) {
            executorService.execute(new RestartTask(countDownLatch));
        }
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
        log.info((System.currentTimeMillis() - start) / 1000 + "s");
        log.info("start");
        String startTerminalTask_param = "id=8a8a8971647d101201647d71c5630009&baseSessionUserId=sys04";
        String startResult = terminalTest.Post(startTerminalTask_url, startTerminalTask_param, cookie);
        log.info("start:" + startResult);
    }

    static class StartTask implements Runnable {
        int id;

        public StartTask(int id) {
            this.id = id;
        }

        @Override
        public void run() {
            log.info("StartTask -- " + id);
            String startTerminalTask_param = "id=8a8a8971647d101201647d71c5630009&baseSessionUserId=sys04";
            TerminalTest terminalTest = new TerminalTest();
            String startResult = terminalTest.Post(startTerminalTask_url, startTerminalTask_param, cookie);
            log.info("start:" + startResult);
        }
    }

    static class StopTask implements Runnable {
        int id;

        public StopTask(int id) {
            this.id = id;
        }

        @Override
        public void run() {
            log.info("StopTask -- " + id);
            String stopTerminalTask_param = "id=8a8a8971647d101201647d71c5630009&baseSessionUserId=sys04";
            TerminalTest terminalTest = new TerminalTest();
            String stopResult = terminalTest.Post(stopTerminalTask_url, stopTerminalTask_param, cookie);
            log.info("stop:" + stopResult);
        }
    }

    static class RestartTask implements Runnable {
        CountDownLatch countDownLatch;

        public RestartTask(CountDownLatch countDownLatch) {
            this.countDownLatch = countDownLatch;
        }

        @Override
        public void run() {
            String startTerminalTask_param = "id=8a8a8971647d101201647d71c5630009&baseSessionUserId=sys04";
            TerminalTest terminalTest = new TerminalTest();
            String startResult = terminalTest.Post(startTerminalTask_url, startTerminalTask_param, cookie);
            log.info("start:" + startResult);
            String stopTerminalTask_param = "id=8a8a8971647d101201647d71c5630009&baseSessionUserId=sys04";
            String stopResult = terminalTest.Post(stopTerminalTask_url, stopTerminalTask_param, cookie);
            log.info("stop:" + stopResult);
            countDownLatch.countDown();
        }
    }

    public Map<String, Object> PostUrlAsMap(String url, String post) {

        StringBuilder stringBuilder = new StringBuilder();
        HttpURLConnection connection = null;
        InputStream is = null;
        InputStreamReader isr = null;
        BufferedReader buffer = null;
        Map<String, Object> resMap = new HashMap<>();
        try {
            connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setConnectTimeout(1200);
            connection.setDoInput(true);
            if (null == post) {
                connection.setRequestMethod("GET");
            }
            if (post != null) {
                connection.setDoOutput(true);
                connection.setRequestMethod("POST");
                OutputStream os = connection.getOutputStream();
                os.write(post.replace(",", "%2C").getBytes());
                //  os.write(post.getBytes());
                os.flush();
                os.close();
            }
            if (connection.getResponseCode() == 200) {
                //printHeader(connection);
                //获取cookie
                String headerField = connection.getHeaderField("Set-Cookie");
                String cookie = headerField.substring(0, headerField.indexOf(";"));
                log.info(headerField);
                resMap.put("cookie", cookie);
                is = connection.getInputStream();
                isr = new InputStreamReader(is);
                buffer = new BufferedReader(isr);
                String line;
                while ((line = buffer.readLine()) != null) {
                    stringBuilder.append(line);
                }
                resMap.put("result", stringBuilder.toString());
            }
            return resMap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            close(buffer);
            close(isr);
            close(is);
        }
    }

    public String Post(String url, String post, String cookie) {

        StringBuilder stringBuilder = new StringBuilder();
        HttpURLConnection connection = null;
        InputStream is = null;
        InputStreamReader isr = null;
        BufferedReader buffer = null;
        try {
            connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setConnectTimeout(11200);
            connection.setDoInput(true);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            connection.setRequestProperty("Cookie", cookie);
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

    public static void close(Closeable io) {
        if (null != io) {
            try {
                io.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
