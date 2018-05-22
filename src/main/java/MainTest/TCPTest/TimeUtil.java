package MainTest.TCPTest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;

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
     * @param hour  当前时间 格式：yyyy-MM-dd HH:mm:ss
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

    public static void main(String[] args) {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // String currentTime = format.format(new Date());

        long time = System.currentTimeMillis();
/*
        Date date = addDateMinutForDate(new Date(time), 2);
        String add = format.format(date);
        log.info("currentTime:"+currentTime+" - add:"+add);*/

        Date deDate = addDateSecondForDate(new Date(time), 10);
        long current = System.currentTimeMillis();


        log.info(format.format(current) + " - " + format.format(deDate));

        log.info(current + " - " + deDate.getTime());
        log.info((current - deDate.getTime()) + "");

        while ((current - deDate.getTime()) < 0) {
            current = System.currentTimeMillis();
            log.info("------------");
            try {
                Thread.sleep(500L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }


    }
}
