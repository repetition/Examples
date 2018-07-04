package JNative;

import com.google.common.io.Resources;
import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Platform;
import com.sun.jna.ptr.IntByReference;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.util.Calendar;
import java.util.Date;

public class NativeMain {
    private static final Logger log = LoggerFactory.getLogger(NativeMain.class);
/*    static {
        addLibrary();
    }*/
// This is the standard, stable way of mapping, which supports extensive
    // customization and mapping of Java to native types.

    public interface CLibrary extends Library {
        CLibrary INSTANCE = (CLibrary) Native.loadLibrary((Platform.isWindows() ? "msvcrt" : "c"),
                CLibrary.class);

        void printf(String format, Object... args);
    }

    public interface Plcommpro extends Library {
        Plcommpro Plcommpro = (Plcommpro) Native.loadLibrary((Platform.isWindows() ? "plcommpro" : "c"),
                Plcommpro.class);

        /**
         * 连接设备，连接成功后返回连接句柄。
         *
         * @param format 通过Parameter参数指定连接选项，如下列例子：
         *               "protocol=RS485,port=COM2,baudrate=38400bps,deviceid=1,timeout=50000, passwd=”;
         *               “protocol=TCP,ipaddress=192.168.12.154,port=4370,timeout=4000,passwd=”;
         *               需要向该函数传递与设备相关的连接参数方可实现连接功能。
         *               protocol是通讯使用的协议，目前有RS485和TCP两种方式；
         *               port：设备通讯端口。例如，以RS485方式连接，可设置port为COM1；以TCP通讯的端口，如果不特别强调，port默认为4370；
         *               deviceid：串口使用的设备RS485通讯地址；
         *               baudrate：串口通讯使用的波特率；
         *               ipaddress：TCP/IP通讯相关设备的IP地址；
         *               timeout：连接超时时间，单位为毫秒。遇到网络连接质量不好时，应加大timeout的值。一般的，“timeout=5000”（5秒）可以满足基本网络使用；当查询数据经常出现-2错误码时，应加大timeout的值，可以设置：“timeout=20000”（20秒）。
         *               passwd：设置通讯的连接密码，可以为空表示不使用密码。
         *               （注：Parameters连接字符串大小写敏感）
         * @return 与设备连接成功后返回该连接句柄，否则连接失败返回0。
         */
        IntByReference Connect(String format);

        /**
         * 断开与设备的连接
         *
         * @param intByReference 由Connect()成功返回的句柄
         */
        void Disconnect(IntByReference intByReference);

        /**
         * 从设备读取数据,用于读取刷卡记录、时间段、用户信息、假日设置等数据，数据可以是一条记录，也可以是多条记录。
         *
         * @param intByReference  连接成功的句柄
         * @param buff            用于接收返回数据的缓冲区，返回的数据是文本格式的，可能是多条记录，各条记录之间用\r\n分隔。
         * @param buffSize        用于接收返回数据的缓冲区大小
         * @param query_table     数据表名
         * @param query_fieldname 字段名列表，多个字段之间用分号分开，“*”表示全部字段，此时返回数据字段的第一行是字段名。
         * @param query_filter    读取数据的条件，当单独一个“字段名 操作符 值”构成的字符串时，可以支持多个条件，使用逗号分隔，按如下方式：
         *                        <字段名>=<值>（“=”符号两边不可以有空格）
         * @param query_options   当前仅在下载门禁事件记录表的数据时有效，值为“New Record”时下载新记录，当为空时下载全部记录。下载其他表数据时，该字段置为空字符串即可。
         * @return
         */
        int GetDeviceData(IntByReference intByReference, byte[] buff, int buffSize, String query_table, String query_fieldname, String query_filter, String query_options);

        /**
         * 控制 控制器 的一些操作  如 开门 关门 常开
         *
         * @param intByReference 连接成功的句柄
         * @param operation_id   1表示锁输出
         * @param door_id        门编号  1 2 4 8
         * @param address_type   此参数表示设备输出点地址类型（1：锁输出，2：辅助输出）
         * @param door_action    此参数表示开门时间（0表示关，255表示常开，取值范围为1~60（秒）），默认值为0
         * @param param4         预留之用，默认为0
         * @param option         默认为空，扩展之用
         * @return 返回为0或者正数时，表示成功；返回负数时表示失败
         */
        int ControlDevice(IntByReference intByReference, long operation_id, long door_id, long address_type, long door_action, long param4, String option);


        int DeleteDeviceData(IntByReference intByReference, String table_name, String data, String option);

        int SetDeviceParam(IntByReference intByReference, String items);

        int SetDeviceData(IntByReference intByReference, String table_name, String data, String option);
    }

    public interface Test {
        void printf(String str);
    }


    public static void main(String[] args) {


       /* CLibrary.INSTANCE.printf("Hello, World\n");
        for (int i=0;i < args.length;i++) {
            CLibrary.INSTANCE.printf("Argument %d: %s\n", i, args[i]);
        }*/


        IntByReference plcommpro = connect();
        //   String records = getRecords(plcommpro, 1);
        log.info("================getUserData=====================");
        //getUserData(plcommpro);
        log.info("================getAuthData=====================");
        getAuthData(plcommpro);
        log.info("================getTimezoneData=====================");
        // getTimezoneData(plcommpro);
        log.info("================getAccessReocrdsAndClear=====================");
        // getAccessReocrdsAndClear(plcommpro, 1);
        //删除user表信息 一个
        //  deleteData(plcommpro, "user,CardNo=2165994");
        //  deleteData(plcommpro, "user,CardNo=2165994#CardNo=2167676");
        //删除多个授权
        //  deleteData(plcommpro, "timezone,TimezoneId=696001#TimezoneId=696002#TimezoneId=696003#TimezoneId=696004#TimezoneId=69300#TimezoneId=69500");
        //删除一个授权
        //  deleteData(plcommpro, "timezone,TimezoneId=696001");

        //  deleteData(plcommpro, "userauthorize,AuthorizeTimezoneId=696001");

        //deleteAuthByTimeZoneAndDoorId(plcommpro, "2167676,2165994", "696001", "1");
     //   deleteAuthByCardPinAndDoorId(plcommpro, "2167676,2165994", "1");

        //添加授权 多个
/*        insertData(plcommpro, "timezone,,TimezoneId=69300,SunTime1=2359,MonTime1=2359,TueTime1=2359,WedTim" +
                "e1=2359,ThuTime1=2359,FriTime1=2359,SatTime1=2359#TimezoneId=69500,SunTime1=2359,MonTime1=2359,TueTime1=2359,WedTime1=2359,ThuTime1=2359,FriTime1=2359,SatTime1=2359#TimezoneId=696001,SunTime1=2359,Mon" +
                "Time1=2359,TueTime1=2359,WedTime1=2359,ThuTime1=2359,FriTime1=2359,SatTime1=2359#TimezoneId=696002,SunTime1=2359,MonTime1=2359,TueTime1=2359,WedTime1=2359,ThuTime1=2359,FriTime1=2359,SatTime1=2359#Tim" +
                "ezoneId=696003,SunTime1=2359,MonTime1=2359,TueTime1=2359,WedTime1=2359,ThuTime1=2359,FriTime1=2359,SatTime1=2359#TimezoneId=696004,SunTime1=2359,MonTime1=2359,TueTime1=2359,WedTime1=2359,ThuTime1=2359" +
                ",FriTime1=2359,SatTime1=2359");*/

        //   insertData(plcommpro, "userauthorize,,Pin=2165994,AuthorizeTimezoneId=69500,AuthorizeDoorId=1#Pin=2167676,AuthorizeTimezoneId=69500,AuthorizeDoorId=1");

       // insertData(plcommpro, "user,,CardNo=2167676,Pin=2167676,StartTime=20180620,EndTime=20500620#CardNo=2165994,Pin=2165994,StartTime=20180620,EndTime=20500620#");

        deleteUser(plcommpro,"2167676,2165994");
        getTimezoneData(plcommpro);
        getUserData(plcommpro);
        openDoor(plcommpro, 1, 30);
        disConnect(plcommpro);
    }

    /**
     * @param plcommpro   连接成功的句柄
     * @param door_id     门编号
     * @param door_action 锁打开时间，默认5s，0为关闭，255为常开
     */
    private static void openDoor(IntByReference plcommpro, long door_id, long door_action) {
        long operation_id = 1;//固定锁输出
        //   door_id = 1;//4代表3门，8代表4门
        long address_type = 1;//门锁输出,默认1即可
        // door_action = 30;//锁打开时间，默认5s，0为关闭，255为常开
        int ControlDevice_Ret = Plcommpro.Plcommpro.ControlDevice(plcommpro, operation_id, door_id, address_type, door_action, 0, "");
    }

    /**
     * @param plcommpro 连接成功的句柄
     * @return 返回user列表
     */
    private static String getUserData(IntByReference plcommpro) {
        String table = "user";
        String query_fieldName = "CardNo\tPin\tStartTime\tEndTime";// 下载ardNo;Pin 两个字段
        String query_filter = "*";
        String buff_str = GetDeviceData(plcommpro, table, query_fieldName, query_filter);
        log.info("\n" + buff_str);
        return buff_str;
    }


    /**
     * @param plcommpro 连接成功的句柄
     * @return 返回user列表
     */
    private static String getAuthData(IntByReference plcommpro) {
        String table = "userauthorize";
        String query_fieldName = "*";// 下载ardNo;Pin 两个字段
        String query_filter = "*";
        String buff_str = GetDeviceData(plcommpro, table, query_fieldName, query_filter);
        log.info("\n" + buff_str);
        return buff_str;
    }

    /**
     * @param plcommpro 连接成功的句柄
     * @return 返回user列表
     */
    private static String getTimezoneData(IntByReference plcommpro) {
        String table = "timezone";
        String query_fieldName = "TimezoneId\tSunTime1\tMonTime1\tTueTime1\tWedTime1\tThuTime1\tFriTime1\tSatTime1";// 下载ardNo;Pin 两个字段
        String query_filter = "*";
        String buff_str = GetDeviceData(plcommpro, table, query_fieldName, query_filter);
        log.info("\n" + buff_str);
        return buff_str;
    }

    /**
     * @param plcommpro 连接成功的句柄
     * @param doorId    门编号
     * @return 返回日志
     */
    private static String getRecords(IntByReference plcommpro, int doorId) {
        String table = "transaction";
        String query_filter = "DoorID=" + doorId;
        String query_fieldname = "*";//默认*
        String buff_str = GetDeviceData(plcommpro, table, query_fieldname, query_filter);
        log.info("\n" + buff_str);
        return buff_str;
    }

    public static int deleteData(IntByReference plcommpro, String data) {
        String option = "";
        String[] strings = data.split(",");
        String tableName = strings[0];
        String deleteData = strings[1];
        String[] deleteDatas = deleteData.split("#");
/*        for (int i = 0; i < deleteData.split("#").length; i++) {
            String carNo = deleteDatas[i] + "\t";
            deleteData += carNo;
        }*/
        if (deleteDatas.length > 1) {
            deleteData = deleteData.replace("#", "\r\n");
        }
        int ret = Plcommpro.Plcommpro.DeleteDeviceData(plcommpro, tableName, deleteData, option);
        log.info("state:" + ret);
        return ret;
    }

    /**
     * 根据 授权di和门编号来删除 pin码  多个pin用逗号分割
     *
     * @param plcommpro       句柄
     * @param pinData         pin码
     * @param timeZoneId      时间段id
     * @param authorizeDoorId 门编号
     */
    public static void deleteAuthByTimeZoneAndDoorId(IntByReference plcommpro, String pinData, String timeZoneId, String authorizeDoorId) {
        String option = "";
        String tableName = "userauthorize";
        String[] pins = pinData.split(",");
        StringBuilder items = new StringBuilder();
        for (String pin : pins) {
            items.append("Pin=" + pin + "\tAuthorizeTimezoneId=" + timeZoneId + "\tAuthorizeDoorId=" + authorizeDoorId + "\r\n");
        }
        int ret = Plcommpro.Plcommpro.DeleteDeviceData(plcommpro, tableName, items.toString(), option);
    }

    /**
     * 根据 门编号来删除授权 pin码  多个pin用逗号分割
     *
     * @param plcommpro       句柄
     * @param pinData         pin码 多个用逗号分割
     * @param authorizeDoorId 门编号
     */
    public static void deleteAuthByCardPinAndDoorId(IntByReference plcommpro, String pinData, String authorizeDoorId) {
        String option = "";
        String tableName = "userauthorize";
        String[] pins = pinData.split(",");
        StringBuilder items = new StringBuilder();

        for (String pin : pins) {
            items.append("Pin=" + pin + "\tAuthorizeDoorId=" + authorizeDoorId + "\r\n");
        }
        int ret = Plcommpro.Plcommpro.DeleteDeviceData(plcommpro, tableName, items.toString(), option);
    }


    /**
     * 根据卡号删除用户
     *
     * @param plcommpro 句柄
     * @param carNo     卡号 多个用逗号分割
     */
    public static void deleteUser(IntByReference plcommpro, String carNo) {
        String option = "";
        String tableName = "user";
        String[] pins = carNo.split(",");
        StringBuilder items = new StringBuilder();
        for (String pin : pins) {
            items.append("Pin=" + pin + "\r\n");
        }
        int ret = Plcommpro.Plcommpro.DeleteDeviceData(plcommpro, tableName, items.toString(), option);
    }


    public static int insertData(IntByReference plcommpro, String data) {
        String option = "";
        String[] strings = data.split(",,");
        String tableName = strings[0];
        String insertData = strings[1];
        String[] deleteDatas = insertData.split("#");
/*        for (int i = 0; i < deleteData.split("#").length; i++) {
            String carNo = deleteDatas[i] + "\t";
            deleteData += carNo;
        }*/
        if (insertData.length() > 1) {
            insertData = insertData.replace("#", "\r\n");
            insertData = insertData.replace(",", "\t");
        }


        int ret = Plcommpro.Plcommpro.SetDeviceData(plcommpro, tableName, insertData, option);
        log.info("state:" + ret);
        return ret;
    }

    /**
     * 根据卡号添加用户
     * @param plcommpro 句柄
     * @param carNo 卡号
     * @return 响应吗
     */
    public static int insertUser(IntByReference plcommpro, String carNo) {
        String option = "";
        String tableName = "user";
        String[] strings = carNo.split(",");
        StringBuilder builder = new StringBuilder();
        if (strings.length > 1) {
            for (String str : strings) {
                builder.append("CardNo=" + str + "\tPin=" + str + "\tStartTime=20180620\tEndTime=20500620\r\n");
            }
        }
        int ret = Plcommpro.Plcommpro.SetDeviceData(plcommpro, tableName, builder.toString(), option);
        log.info("state:" + ret);
        return ret;
    }

    /**
     * 获取门禁数据记录
     *
     * @param plcommpro       句柄
     * @param table           表名
     * @param query_filter    字段名条件 读取数据的条件，当单独一个“字段名 操作符 值”构成的字符串时，可以支持多个条件，使用逗号分隔，按如下方式：
     *                        <字段名>=<值>（“=”符号两边不可以有空格）
     * @param query_fieldName 字段名列表，多个字段之间用分号分开，“*”表示全部字段，此时返回数据字段的第一行是字段名
     * @return 返回获取的数据
     */
    private static String GetDeviceData(IntByReference plcommpro, String table, String query_fieldName, String query_filter) {
        //例子
/*        String table = "transaction";
        String query_filter = "DoorID=" + doorId;
        String query_options = ""; //默认为空
        String query_fieldname = "*";//默认*
        int BUFFERSIZE = 4 * 1024 * 1024;
        byte[] buff = new byte[BUFFERSIZE];*/

        String query_options = ""; //默认为空
        //  String query_fieldname = "*";//默认*

        int BUFFERSIZE = 4 * 1024 * 1024;
        byte[] buff = new byte[BUFFERSIZE];

        int ret = Plcommpro.Plcommpro.GetDeviceData(plcommpro, buff, BUFFERSIZE, table, query_fieldName, query_filter, query_options);
//        log.info("getRecords_State:" + ret);
        if (ret < 0) {
            return null;
        } else {
            //将为0的字符去除
            String buff_str = getString(buff);
            //    log.info("Records \n" + buff_str);
            saveToFile(buff_str);
            return buff_str;
        }
    }

    /**
     * 获取刷卡记录并删除   如 getAccessReocrdsAndClear(IntByReference plcommpro, 1)
     *
     * @param plcommpro 句柄
     * @param doorId    门编号
     */
    public static String getAccessReocrdsAndClear(IntByReference plcommpro, int doorId) {
        String tableName = "transaction";
        String records = getRecords(plcommpro, doorId);
        String data = "DoorID=" + doorId;
        clearTabData(plcommpro, tableName, data);
        //  log.info(records);
        return records;
    }

    public static int clearTabData(IntByReference plcommpro, String tableName, String data) {
        String option = "";
        int ret = Plcommpro.Plcommpro.DeleteDeviceData(plcommpro, tableName, data, option);

        return ret;
    }


    public static int syncTime(IntByReference plcommpro, Date date) {
        String items = "DateTime=" + formatServerTimeWithPullSDK(date);
        int ret = Plcommpro.Plcommpro.SetDeviceParam(plcommpro, items);
        return ret;
    }


    private static void saveToFile(String buff_str) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream("d:\\Records1.txt", false);
            fileOutputStream.write(buff_str.getBytes());
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 将byte[] 数组 为0 即(\u0000)的过滤掉
     *
     * @param sb 缓存数组
     * @return 返回过滤掉的缓存数组，null为异常
     */
    public static String getString(byte[] sb) {
        // trim nulls from end
        int strLen = sb.length;
        //循环遍历是否为0 直到不为0跳出
        for (; strLen > 0; strLen--)
            if (sb[strLen - 1] != '\u0000') {
                break;
            }

        try {
            //将不为0的数据写入
            return new String(sb, 0, strLen, "UTF8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static IntByReference connect() {
        addLibrary();
        String params = "protocol=TCP,ipaddress=10.10.10.121,port=4370,timeout=2000,passwd=";
        return Plcommpro.Plcommpro.Connect(params);
    }


    private static void disConnect(IntByReference intByReference) {
        Plcommpro.Plcommpro.Disconnect(intByReference);
    }


    /**
     * 功能描述：根据pullsdk提供内容格式化服务器时间
     *
     * @param serverDate
     * @return
     */
    private static long formatServerTimeWithPullSDK(Date serverDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(serverDate);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int date = calendar.get(Calendar.DATE);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        return calculatetTime(year, month, date, hour, minute, second);
    }

    /**
     * 功能描述：根据pullsdk格式要求，计算出对应格式的时间
     *
     * @param year
     * @param month
     * @param date
     * @param hour
     * @param minute
     * @param second
     * @return
     */
    private static long calculatetTime(int year, int month, int date, int hour, int minute, int second) {
        return ((year - 2000) * 12 * 31 + (month - 1) * 31 + (date - 1)) * 24
                * 60 * 60 + hour * 60 * 60 + minute * 60 + second;
    }


    public static void addLibrary() {

        String file = Resources.getResource("JNative/plcommpro.dll").getFile();
        File classPath = new File(file).getParentFile();
        File dllPath = new File(file);
        String path = System.getProperty("java.library.path");
        //  System.out.println(path);
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
        // System.out.println(System.getProperty("java.library.path"));
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