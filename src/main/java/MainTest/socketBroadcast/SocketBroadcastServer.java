package MainTest.socketBroadcast;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.*;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.List;

/**
 * @author zhangbo
 *  广播发送接收类
 */
public class SocketBroadcastServer {
    private static final Logger log  = LoggerFactory.getLogger(SocketBroadcastServer.class);

    private static String data = "{\"port\":53515,\"name\":\"PyReceiver @ 10.10.8.185\",\"id\":\"11\",\"width\":1280,\"height\":960,\"mirror\":\"h264\",\"audio\":\"pcm\",\"subtitles\":\"text/vtt\",\"proxyHeaders\":true,\"hls\":false,\"upsell\":true}";
    public static void main(String[] args) {

        sendSocketBroadcastServer("ssss");


    }

    private static void sendSocketBroadcastServer(String msg) {
        try {
            DatagramSocket datagramSocket =  new DatagramSocket(22222);
            //获取广播地址
            String localBroadCast = getLocalBroadCast();
           // String localBroadCast = "10.10.8.255";
            log.info(localBroadCast);
            //包装数据对象
            DatagramPacket datagramPacket = new DatagramPacket(msg.getBytes(Charset.forName("utf-8")), msg.length(), getAddress(localBroadCast),53515);
            datagramSocket.send(datagramPacket);
            //发送消息后，接收消息
            receive(datagramSocket);

        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void receive(DatagramSocket datagramSocket) throws IOException, InterruptedException {
        while (true){
            Thread.sleep(2000L);
            byte[] bytes = new byte[1024];
            DatagramPacket datagramPacket = new DatagramPacket(bytes, bytes.length);
            datagramSocket.receive(datagramPacket);
            byte[] data = datagramPacket.getData();
            java.lang.String str = new java.lang.String(data, 0, data.length);
            log.info("receive for server:"+str);
        }
    }

    /**
     * 根据ip转换成InetAddress对象
     * @param localBroadCast  ip地址
     * @return
     * @throws UnknownHostException
     */
    private static InetAddress getAddress(String localBroadCast) throws UnknownHostException {
        String[] ipStr = localBroadCast.split("\\.");
        byte[] ipBuf = new byte[4];
        for(int i = 0; i < 4; i++){
            ipBuf[i] = (byte)(Integer.parseInt(ipStr[i])&0xff);
        }
        return InetAddress.getByAddress(ipBuf);
    }


    /**
     * 获取本机广播地址，并自动区分Windows还是Linux操作系统
     * @return String
     */
    public static String getLocalBroadCast(){
        String broadCastIp = null;
        try {
            Enumeration<?> netInterfaces = (Enumeration<?>) NetworkInterface.getNetworkInterfaces();
            while (netInterfaces.hasMoreElements()) {
                NetworkInterface netInterface = (NetworkInterface) netInterfaces.nextElement();
                if (netInterface.getDisplayName().contains("VMware")) {
                    continue;
                }
                if (!netInterface.isLoopback()&& netInterface.isUp()) {
                    List<InterfaceAddress> interfaceAddresses = netInterface.getInterfaceAddresses();
                    for (InterfaceAddress interfaceAddress : interfaceAddresses) {
                        //只有 IPv4 网络具有广播地址，因此对于 IPv6 网络将返回 null。
                        if(interfaceAddress.getBroadcast()!= null){
                            broadCastIp =interfaceAddress.getBroadcast().getHostAddress();
                        }
                    }
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return broadCastIp;
    }
}
