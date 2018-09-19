package MainTest.socketBroadcast;

import javafx.stage.Screen;
import jdk.internal.util.xml.impl.Input;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.lang.String;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class SocketBroadcastClient {
    private static final Logger log = LoggerFactory.getLogger(SocketBroadcastClient.class);
    private static final ThreadPoolExecutor executor = new ThreadPoolExecutor(20, 30, 1L, TimeUnit.SECONDS, new LinkedBlockingDeque<Runnable>());
    public static String meta_data = "{\"port\":53515,\"name\":\"PyReceiver @ 10.10.8.185\",\"id\":\"11\",\"width\":1280,\"height\":960,\"mirror\":\"h264\",\"audio\":\"pcm\",\"subtitles\":\"text/vtt\",\"proxyHeaders\":true,\"hls\":false,\"upsell\":true}";
   // private static OutputStream outputStream;
    //private static Process process;


    public static void main(java.lang.String[] args) {
        log.info("start receive ....");
        //receive();
        // streamFfplay();
        startSocketServer();
        log.info("start receive success!");
    }

    private static void receive() {
        final DatagramSocket[] datagramSocket = {null};
        executor.execute(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        if (null == datagramSocket[0]) {
                            datagramSocket[0] = new DatagramSocket(53515);
                        }
                        byte[] bytes = new byte[1024];
                        DatagramPacket datagramPacket = new DatagramPacket(bytes, bytes.length);
                        datagramSocket[0].receive(datagramPacket);

                        //收到消息 发送消息
                        log.info("address:" + datagramPacket.getAddress() + "   port:" + datagramPacket.getPort());
                        byte[] data = datagramPacket.getData();
                        java.lang.String str = new java.lang.String(data, 0, data.length);
                        log.info("receive:" + str);
                        log.info("send..");
                        DatagramPacket sendDatagramPacket = new DatagramPacket(meta_data.getBytes(), meta_data.length(), datagramPacket.getAddress(), datagramPacket.getPort());
                        datagramSocket[0].send(sendDatagramPacket);
                    } catch (SocketException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }


    public static void startSocketServer() {
        final ServerSocket[] serverSocket = {null};
        executor.execute(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if (null == serverSocket[0]) {
                        try {
                            serverSocket[0] = new ServerSocket(53515);
                            Socket socket = serverSocket[0].accept();
                            // streamFfplay();
                            SocketRunnable runnable = new SocketRunnable(socket);
                            executor.execute(runnable);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
    }

    private static Process streamFfplay() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        List<String> command = new ArrayList<>();
        command.add("E:\\ffmpeg\\bin\\ffplay.exe");
        command.add("-framerate");
        // command.add("-f");
        command.add("30");
        //  command.add("-i");
        command.add("-");
        processBuilder.redirectErrorStream(true);
        processBuilder.command(command);
        try {
           Process process = processBuilder.start();
            InputStreamRunnable inputStreamRunnable = new InputStreamRunnable(process);
            executor.execute(inputStreamRunnable);
            return process;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static class SocketRunnable implements Runnable {

        private Socket socket;

        public SocketRunnable(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            Process process = streamFfplay();
            OutputStream outputStream = process.getOutputStream();
            try {
                InputStream is = socket.getInputStream();
                while (true) {
                    if (!socket.isConnected()||socket.isClosed()|| socket.isOutputShutdown()) {
                        process.destroy();
                        is.close();
                        socket = null;
                        executor.remove(this);
                        break;
                    }
                   // log.info(is.available() + "");
                    byte[] bytes = new byte[is.available()];
                    //  process.getOutputStream().write(bytes);\
                    is.read(bytes);
                    if (bytes.length<=0) {
                        continue;
                    }
                    outputStream.write(bytes);
                   // saveToFile(bytes);
                    //  outputStream.flush();
                }
            } catch (IOException e) {
                e.printStackTrace();
                log.info("异常！", e);
                process.destroy();
               executor.remove(this);
               // executor.execute(this);
            }
        }

        private void saveToFile(byte[] bytes) {

            try {
                File file = new File("E:/ffmpeg/bin/video.raw");
                if (!file.exists()) {
                    file.createNewFile();
                }
                FileOutputStream fileOutputStream = new FileOutputStream(file,true);
                fileOutputStream.write(bytes);
                fileOutputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public static class InputStreamRunnable implements Runnable {
        private Process process;

        public InputStreamRunnable(Process process) {
            this.process = process;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    InputStreamReader inputStreamReader = new InputStreamReader(process.getInputStream());
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        System.out.println(line);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
