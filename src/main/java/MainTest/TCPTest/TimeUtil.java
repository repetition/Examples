package MainTest.TCPTest;

import MainTest.utlis.EncryUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeUtil {
    private static final Logger log = LoggerFactory.getLogger(TimeUtil.class);

    //两个日期相减得到的毫秒数
    public static long dateDiff(Date beginDate, Date endDate) {
        long date1ms = beginDate.getTime();
        long date2ms = endDate.getTime();
        return date2ms - date1ms;
    }


    /**
     * 给时间加上几个小时
     *
     * @param day  当前时间 格式：yyyy-MM-dd HH:mm:ss
     * @param hour 需要加的时间
     * @return
     */
    public static String addDateMinut(String day, int hour) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = format.parse(day);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (date == null)
            return "";
        System.out.println("front:" + format.format(date)); //显示输入的日期
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.HOUR, hour);// 24小时制
        date = cal.getTime();
        System.out.println("after:" + format.format(date));  //显示更新后的日期
        cal = null;
        return format.format(date);

    }

    /**
     * 给时间加上几个小时
     *
     * @param hour 当前时间 格式：yyyy-MM-dd HH:mm:ss
     * @param hour 需要加的时间
     * @return
     */
    public static Date addDateMinutForDate(Date date, int hour) {
/*        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = format.parse(day);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (date == null)
            return null;
        System.out.println("front:" + format.format(date)); //显示输入的日期*/
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.HOUR, hour);// 24小时制
        date = cal.getTime();
        //   System.out.println("after:" + format.format(date));  //显示更新后的日期
        cal = null;
        return date;

    }


    /**
     * 给时间加上几分钟
     *
     * @param date   当前时间 格式：yyyy-MM-dd HH:mm:ss
     * @param second 需要加的时间
     * @return
     */
    public static Date addDateSecondForDate(Date date, int second) {
/*        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = format.parse(day);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (date == null)
            return null;
        System.out.println("front:" + format.format(date)); //显示输入的日期*/
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.SECOND, second);// 24小时制
        date = cal.getTime();
        //   System.out.println("after:" + format.format(date));  //显示更新后的日期
        cal = null;
        return date;

    }

    public static void main(String[] args) throws ParseException {
/*        String timeStr = "2018-08-13 17:10:00";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // String currentTime = format.format(new Date());
        Date date = new Date();
        date.setTime(1534410080446L);
        String format1 = format.format(date);

        long time1 = format.parse(timeStr).getTime();

        System.out.println(format1+"----"+time1);

        long time = System.currentTimeMillis();
*//*
        Date date = addDateMinutForDate(new Date(time), 2);
        String add = format.format(date);
        log.info("currentTime:"+currentTime+" - add:"+add);*//*

        Date deDate = addDateSecondForDate(new Date(time), 10);
        long current = System.currentTimeMillis();


        log.info(format.format(current) + " - " + format.format(deDate));

        log.info(current + " - " + deDate.getTime());
        log.info((current - deDate.getTime()) + "");

  *//*      while ((current - deDate.getTime()) < 0) {
            current = System.currentTimeMillis();
            log.info("------------");
            try {
                Thread.sleep(500L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }*/

      /*  SecureRandom random = new SecureRandom();


        int i = random.nextInt(10);

        System.out.println(i);*/


        //     fileWrite(null,"D:\\Desktop\\Tracker.txt");

/*        String serial = "5c6W0Fb2JrHz16yiRVrl4LweEY+A5cHt";
        serial = serial.replaceAll("\r|\n", "");

        System.out.println(serial);*/

        System.out.println(getLocalMac());
        System.out.println(getLocalMac().replace("-","2"));
        String encrypt = EncryUtil.encrypt(getLocalMac().replace("-","2"));
        System.out.println(encrypt);
    }

    /**
     * 文件写入流
     *
     * @param os       流
     * @param filePath 文件路径
     */
    public static void fileWrite(OutputStream os, String filePath) {
        try {
            FileInputStream fis = new FileInputStream(filePath);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader reader = new BufferedReader(isr);
            String line;
            StringBuilder builder = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                if (line.length() == 0) {
                    continue;
                }
                builder.append(line + ",");
            }
            System.out.println(builder.toString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * <p>功能描述：获取本机mac地址 </p>
     *
     * @return
     */
    public static String getLocalMac() {
        StringBuffer sb = new StringBuffer();
        try {
            InetAddress inetAddress = InetAddress.getLocalHost();
            byte[] mac = NetworkInterface.getByInetAddress(inetAddress).getHardwareAddress();
            for (int i = 0; i < mac.length; i++) {
                if (i != 0) {
                    sb.append("-");
                }
                //字节转换为整数
                int temp = mac[i] & 0xff;
                String str = Integer.toHexString(temp);
                if (str.length() == 1) {
                    sb.append("0" + str);
                } else {
                    sb.append(str);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return sb.toString().toUpperCase();
    }
}

