package JNative;

import org.xvolks.jnative.JNative;
import org.xvolks.jnative.Type;
import org.xvolks.jnative.exceptions.NativeException;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;

public class JNativeMain {

    public static void main(String[] args) {


   /*     //创建对象
        JNative nGetSystemTime = new JNative("kernel32", "GetSystemTime");//GetSystemTime是dll中的方法
        Kernel32.SystemTime systemTime = new Kernel32.SystemTime();
        //设置指针的参数如上面类所述
        nGetSystemTime.setParameter(0, systemTime.getPointer());
        //执行方法
        nGetSystemTime.invoke();
        //解析结构指针的内容
*/
        addLibrary();
        try {
            System.loadLibrary("JNativeCpp");
            String params = "protocol=TCP,ipaddress=10.10.10.121,port=4370,timeout=2000,passwd=";
            JNative jNative = new JNative("plcommpro.dll", "Connect");
            jNative.setRetVal(Type.INT);
            jNative.setParameter(0,Type.STRING,params);
            jNative.invoke();
            int pointer = jNative.getFunctionPointer();
            System.out.println(pointer);

        } catch (NativeException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }


    public static void addLibrary() {

        String file = JNativeMain.class.getResource("JNative/JNativeCpp.dll").getFile();
        File classPath = new File(file).getParentFile();
        File dllPath = new File(file);
        String path = System.getProperty("java.library.path");
        System.out.println(path);
        try {
            if (isOSWin()) {
                path += ";" + classPath.getCanonicalPath();
            } else {
                path += ":" + classPath.getCanonicalPath();
            }
            addLibraryDir(path);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void addLibraryDir(String libraryPath) throws Exception {
        Field userPathsField = ClassLoader.class.getDeclaredField("usr_paths");
        userPathsField.setAccessible(true);
        String[] paths = (String[]) userPathsField.get(null);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < paths.length; i++) {
            if (libraryPath.equals(paths[i])) {
                continue;
            }
            sb.append(paths[i]).append(';');
        }
        sb.append(libraryPath);
        System.setProperty("java.library.path", sb.toString());
        final Field sysPathsField = ClassLoader.class.getDeclaredField("sys_paths");
        sysPathsField.setAccessible(true);
        sysPathsField.set(null, null);
    }

    public static boolean isOSWin() {//OS 版本判断
        String OS = System.getProperty("os.name").toLowerCase();
        if (OS.indexOf("win") >= 0) {
            return true;
        } else return false;
    }
}
