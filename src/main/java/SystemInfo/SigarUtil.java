package SystemInfo;

import com.google.common.io.Resources;
import org.hyperic.sigar.CpuInfo;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;

import java.io.File;

public class SigarUtil {
    public final static Sigar sigar = initSigar();

    private static Sigar initSigar() {
        try {
            String file = Resources.getResource("sigar/sigar-amd64-winnt.dll").getFile();
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

        CpuInfo[] cpuInfoList = null;
        try {
            cpuInfoList = sigar.getCpuInfoList();
            for (CpuInfo cpuInfo : cpuInfoList) {
                System.out.println(cpuInfo.getTotalCores());
            }
        } catch (SigarException e) {
            e.printStackTrace();
        }

    }

}
