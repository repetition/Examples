package MainTest.BroadcastSocket;

import com.aspose.words.Run;
import jdk.internal.util.xml.impl.Input;
import org.apache.log4j.net.SocketServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class BroadcastSocket {
    public static final ThreadPoolExecutor executor = new ThreadPoolExecutor(20, 30, 1L, TimeUnit.SECONDS, new LinkedBlockingDeque<Runnable>());

    private static final Logger log = LoggerFactory.getLogger(BroadcastSocket.class);

    private static String data = "{\"port\":53515,\"name\":\"PyReceiver\",\"id\":\"1111\",\"width\":1280,\"height\":960,\"mirror\":\"h264\",\"audio\":\"pcm\",\"subtitles\":\"text/vtt\",\"proxyHeaders\":true,\"hls\":false,\"upsell\":true}";

    //接受消息
    public static DatagramSocket datagramSocket = null;
    private static OutputStream outputStream;

    public static void main(String[] args) throws IOException {
/*        executor.execute(() -> {
            while (true) {

                try {
                   // Thread.sleep(2000L);
                    sendBroadcast(data);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });*/

        executor.execute(() -> {
            try {
                log.info("socket start...");
                socketServer();
                log.info("socket started , bind 53515");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    private static void socket() throws InterruptedException {
        try {
            Socket socket = new Socket();
            socket.connect(new InetSocketAddress("192.168.31.132", 53515));
            while (true) {
                if (socket.getInputStream() != null) {
                    InputStream inputStream = socket.getInputStream();
                    log.info(inputStream.available() + "");
                } else {
                    break;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
            Thread.sleep(2000L);
            socket();
        }

    }

    private static void socketServer() throws InterruptedException {
        try {

            ServerSocket serverSocket = new ServerSocket(53515);
            while (true) {
                Socket socket = serverSocket.accept();
                executor.execute(new SocketRunnable(socket));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void sendBroadcast(String str) throws IOException {
        if (null == datagramSocket) {
            datagramSocket = new DatagramSocket(53515);
        }
        byte[] bytes = new byte[1024];
        DatagramPacket datagramPacket = new DatagramPacket(bytes, bytes.length);
        datagramSocket.receive(datagramPacket);
        log.info("receive  - " + datagramPacket.getAddress() + ":" + datagramPacket.getPort());
        String receiveStr = new String(datagramPacket.getData());
        log.info("get Data : " + receiveStr);
        sendMsg(datagramSocket, str, datagramPacket);
    }

    private static void sendMsg(DatagramSocket datagramSocket, String str, DatagramPacket datagramPacket) throws IOException {
        int port = datagramPacket.getPort();
        String localBroadCast = getLocalBroadCast();
        DatagramPacket sendDatagramPacket = new DatagramPacket(str.getBytes(), str.length(), datagramPacket.getAddress(), port);
        datagramSocket.send(sendDatagramPacket);
    }


    /**
     * 获取本机广播地址，并自动区分Windows还是Linux操作系统
     *
     * @return String
     */
    public static String getLocalBroadCast() {
        String broadCastIp = null;
        try {
            Enumeration<?> netInterfaces = (Enumeration<?>) NetworkInterface.getNetworkInterfaces();
            while (netInterfaces.hasMoreElements()) {
                NetworkInterface netInterface = (NetworkInterface) netInterfaces.nextElement();
                if (!netInterface.isLoopback() && netInterface.isUp()) {
                    List<InterfaceAddress> interfaceAddresses = netInterface.getInterfaceAddresses();
                    for (InterfaceAddress interfaceAddress : interfaceAddresses) {
                        //只有 IPv4 网络具有广播地址，因此对于 IPv6 网络将返回 null。
                        if (interfaceAddress.getBroadcast() != null) {
                            broadCastIp = interfaceAddress.getBroadcast().getHostAddress();

                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return broadCastIp;
    }

    public static class SocketRunnable implements Runnable {
        private Socket socket;

        public SocketRunnable(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            commandFfplay();
            try {
                InputStream inputStream = null;
                inputStream = socket.getInputStream();
                while (true) {
                   // log.info(inputStream.available() + "");
                    byte[] bytes = new byte[inputStream.available()];
                    inputStream.read(bytes);
                    outputStream.write(bytes);
                    // outputStream.flush();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void commandFfplay() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        List<String> command = new ArrayList<>();
        command.add("F:\\QQDownload\\ffmpeg\\bin\\ffplay.exe");
        command.add("-framerate");
        command.add("30");
        command.add("-");
        processBuilder.redirectErrorStream(true);
        processBuilder.command(command);
        try {
            Process process = processBuilder.start();
            outputStream = process.getOutputStream();
            executor.execute(new StreamReaderRunnable(process));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static class StreamReaderRunnable implements Runnable {
        private Process process;

        public StreamReaderRunnable(Process process) {
            this.process = process;
        }

        @Override
        public void run() {
            try {
                while (true) {
                    InputStream inputStream = process.getInputStream();
                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                    BufferedReader reader = new BufferedReader(inputStreamReader);
                    String line;
                    StringBuilder builder = new StringBuilder();
                    while ((line = reader.readLine()) != null) {
                        System.out.println(line);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
