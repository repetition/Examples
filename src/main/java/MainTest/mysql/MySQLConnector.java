package MainTest.mysql;


import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;
import sun.rmi.runtime.Log;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Timer;
import java.util.TimerTask;

public class MySQLConnector {
    // JDBC 驱动名及数据库 URL
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost:3306/cr_zyrf1";
   // static final String DB_URL = "jdbc:mysql://10.10.10.214:3306/cr_4_2_2";

    // 数据库的用户名与密码，需要根据自己的设置
    static final String USER = "root";
    static final String PASS = "root";

    public static void main(String[] args) {

        Connection connection = getConn();
        try {
            PreparedStatement statement = connection.prepareStatement("select * from prem_mission where mission_name like '%只在web端%' ");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {

                String id = resultSet.getString("id");

                System.out.println(id);

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    private static void test() {
        Connection conn = getConn();
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    String sql = "show status like '%Threads_connected%'";
                    PreparedStatement statement = conn.prepareStatement(sql);
                    ResultSet resultSet = statement.executeQuery();
                    while (resultSet.next()) {
                        int value = resultSet.getInt("Value");
                        String name = resultSet.getString("Variable_name");
                        System.out.println(name + "  " + value);
                        if (value > 100) {
                            FileOutputStream fos = new FileOutputStream("E:\\connect.txt", true);
                            fos.write((name + value+"\r\n").getBytes(Charset.forName("utf-8")));
                            fos.flush();
                            fos.close();
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }, 500L, 1000L);
    }

    public static Connection getConn() {
        // 注册 JDBC 驱动
        try {
            Class.forName(JDBC_DRIVER);
            // 打开链接
            System.out.println("连接数据库...");
            Connection conn = (Connection) DriverManager.getConnection(DB_URL, USER, PASS);
            return conn;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
