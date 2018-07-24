package MainTest.cmd;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.*;

public class ProcessConverter {

    public static void main(String[] args) {

        final String name = args[0];
        System.out.println(name);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    searchProcess(name);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }, 1000L, 1000L);

    }

    private static void searchProcess(String processName) throws IOException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = format.format(new Date());
        System.out.println(time);


        ProcessBuilder builder = new ProcessBuilder();
        builder.redirectErrorStream(true);
        List<String> cmdList = new ArrayList<>();
        cmdList.add("cmd.exe");
        cmdList.add("/c");
        cmdList.add("tasklist");
        cmdList.add("/FI");
       // cmdList.add("IMAGENAME eq EXCEL*");
        cmdList.add("IMAGENAME eq "+processName+"*");
        builder.command(cmdList);

        Process process = builder.start();
      //  System.out.println(getProcessID(process));
        InputStream inputStream = process.getInputStream();
        InputStreamReader isr = new InputStreamReader(inputStream, Charset.forName("gbk"));
        BufferedReader reader = new BufferedReader(isr);
        String line;
        StringBuilder stringBuilder = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            stringBuilder.append(line + "\r\n");
        }
        System.out.println(stringBuilder.toString());
        System.out.println(getPid(process));
    }
/*
    public static long getProcessID(Process p)
    {
        long result = -1;
        try
        {
            //for windows
            if (p.getClass().getName().equals("java.lang.Win32Process") ||
                    p.getClass().getName().equals("java.lang.ProcessImpl"))
            {
                Field f = p.getClass().getDeclaredField("handle");
                f.setAccessible(true);
                long handl = f.getLong(p);
                Kernel32 kernel = Kernel32.INSTANCE;
                WinNT.HANDLE hand = new WinNT.HANDLE();
                hand.setPointer(Pointer.createConstant(handl));
                result = kernel.GetProcessId(hand);
                f.setAccessible(false);
            }
            //for unix based operating systems
            else if (p.getClass().getName().equals("java.lang.UNIXProcess"))
            {
                Field f = p.getClass().getDeclaredField("pid");
                f.setAccessible(true);
                result = f.getLong(p);
                f.setAccessible(false);
            }
        }
        catch(Exception ex)
        {
            result = -1;
        }
        return result;
    }*/

    public static int getPid(Process process) {
        try {
            Class<?> cProcessImpl = process.getClass();
            Field fPid = cProcessImpl.getDeclaredField("pid");
            if (!fPid.isAccessible()) {
                fPid.setAccessible(true);
            }
            return fPid.getInt(process);
        } catch (Exception e) {
            return -1;
        }
    }
}
