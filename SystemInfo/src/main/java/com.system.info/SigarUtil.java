package SystemInfo;

import com.google.common.io.Resources;
import org.hyperic.sigar.*;

import java.io.File;

public class SigarUtil {
    public final static Sigar sigar = initSigar();

    private static Sigar initSigar() {
        try {
            String file = Resources.getResource("sigar/sigar-amd64-winnt.dll").getFile();
            System.out.println(file);
            File classPath = new File(file).getParentFile();

            String path = System.getProperty("java.library.path");
            if (isOSWin()) {
                path += ";" + classPath.getCanonicalPath();
            } else {
                path += ":" + classPath.getCanonicalPath();
            }
            System.setProperty("java.library.path", path);
            return new Sigar();
        } catch (Exception e) {
            return null;
        }
    }

    public static boolean isOSWin() {//OS 版本判断
        String OS = System.getProperty("os.name").toLowerCase();
        if (OS.indexOf("win") >= 0) {
            return true;
        } else return false;
    }


    public static void main(String[] args) {

   /*     CpuInfo[] cpuInfoList = null;
        try {
          *//*  cpuInfoList = sigar.getCpuInfoList();
            for (CpuInfo cpuInfo : cpuInfoList) {
                System.out.println(cpuInfo.getTotalCores());
            }*//*

            for (int i = 0; i < 100; i++) {
                Thread.sleep(500L);
                System.out.println(CpuPerc.format(sigar.getCpuPerc().getCombined()));
            }
        } catch (SigarException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/

        try {
            net();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static void net() throws Exception {
      //  Sigar sigar = new Sigar();
        String ifNames[] = sigar.getNetInterfaceList();
        for (int i = 0; i < ifNames.length; i++) {
            String name = ifNames[i];
            NetInterfaceConfig ifconfig = sigar.getNetInterfaceConfig(name);
            System.out.println("网络设备名:    " + name);// 网络设备名
            System.out.println("IP地址:    " + ifconfig.getAddress());// IP地址
            System.out.println("子网掩码:    " + ifconfig.getNetmask());// 子网掩码
            if ((ifconfig.getFlags() & 1L) <= 0L) {
                System.out.println("!IFF_UP...skipping getNetInterfaceStat");
                continue;
            }
            NetInterfaceStat ifstat = sigar.getNetInterfaceStat(name);
            System.out.println(name + "接收的总包裹数:" + ifstat.getRxPackets());// 接收的总包裹数
            System.out.println(name + "发送的总包裹数:" + ifstat.getTxPackets());// 发送的总包裹数
            System.out.println(name + "接收到的总字节数:" + ifstat.getRxBytes());// 接收到的总字节数
            System.out.println(name + "发送的总字节数:" + ifstat.getTxBytes());// 发送的总字节数
            System.out.println(name + "接收到的错误包数:" + ifstat.getRxErrors());// 接收到的错误包数
            System.out.println(name + "发送数据包时的错误数:" + ifstat.getTxErrors());// 发送数据包时的错误数
            System.out.println(name + "接收时丢弃的包数:" + ifstat.getRxDropped());// 接收时丢弃的包数
            System.out.println(name + "发送时丢弃的包数:" + ifstat.getTxDropped());// 发送时丢弃的包数
        }
    }

}
