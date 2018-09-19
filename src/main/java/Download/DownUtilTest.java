package Download;


import com.google.gson.Gson;
import org.hyperic.sigar.cmd.Ls;

import javax.naming.Name;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by amosli on 14-7-2.
 */
public class DownUtilTest {

    public static void main(String[] args) {

/*        int flag = clazzForClass(Clazz1.class, Clazz2.class);
        System.out.println(flag);*/

/*        Clazz1 clazz1 = new Clazz1();
        Clazz2 clazz2 = new Clazz2();
        Clazz3 clazz3 = new Clazz3();

        List<Clazz> list = new ArrayList<>();
        list.add(clazz1);
        list.add(clazz2);
        list.add(clazz3);
        System.out.println("排序前：");
        System.out.println(list);
        Collections.sort(list,new compare());
        System.out.println("排序后：");
        System.out.println(list);

        Gson gson = new Gson();
        String s = gson.toJson(list);
        System.out.println(s);*/

        List<demo> list = new ArrayList<>();
        demo demo1 = new demo();
        demo1.setAge(20);
        demo1.setName("张三");

        demo demo2 = new demo();
        demo2.setAge(30);
        demo2.setName("李四");
        list.add(demo1);
        list.add(demo2);

        Collections.sort(list,new compare());

    }



    interface Clazz{
        Date getOperationTime();
        void setOperationTime(Date date);

    }
    static class Clazz1 implements Clazz{

        public Date date = new Date();

        @Override
        public Date getOperationTime(){
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:S");
            Date date = new Date();
            System.out.println("Clazz1:"+format.format(date));
            return date ;
        }

        @Override
        public void setOperationTime(Date date) {

        }
    }
    static class Clazz2 implements Clazz {
        public Date date = new Date();

       @Override
       public Date getOperationTime(){
           try {
               SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:S");
               String old_str = "2018-07-17 5:30:30:10";
               Date parse = format.parse(old_str);
              // System.out.println("Clazz2:"+old_str);
               return parse;
           } catch (ParseException e) {
               e.printStackTrace();
           }
           return null;
       }

        @Override
        public void setOperationTime(Date date) {
        }

    }
    static class Clazz3 implements Clazz {
        public Date date = new Date();

        @Override
        public Date getOperationTime(){
            try {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:S");
                String old_str = "2018-08-06 5:30:30:10";
                Date parse = format.parse(old_str);
             //   System.out.println("Clazz3:"+old_str);

                return parse;
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public void setOperationTime(Date date) {

        }

    }

    static class compare implements Comparator<Object>{
        @Override
        public int compare(Object o1, Object o2) {
          //  System.out.println("o1:"+o1.getClass().getName());
          //  System.out.println("o2:"+o2.getClass().getName());

            int flag = clazzForClass2( o1,  o2);
          //  System.out.println("compare:"+flag);
            return flag;
        }

        public static int clazzForClass(Class clzz1, Class clzz2){
            int flag = 0;
            try {
                Field date = clzz1.getDeclaredField("date");
                Method getOperationTime_Clzz1Method = clzz1.getMethod("getOperationTime");
                Date clzz1_date = (Date) getOperationTime_Clzz1Method.invoke(clzz1.newInstance());
                Method getOperationTime_Clzz2Method = clzz2.getMethod("getOperationTime");
                Date clzz2_date = (Date) getOperationTime_Clzz2Method.invoke(clzz2.newInstance());
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

                if (null == clzz1_date) {
                    flag = -1;
                    return flag;
                }
                if (null == clzz2_date) {
                    flag = -1;
                    return flag;
                }
                flag = clzz1_date.compareTo(clzz2_date);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
            return flag;
        }
        public static int clazzForClass2(Object o1, Object o2){
            int flag = 0;
            try {
                Class clzz1 = o1.getClass();
                Class clzz2 = o2.getClass();
                Method getOperationTime_Clzz1Method = clzz1.getMethod("getAge");
                int clzz1_date = (int) getOperationTime_Clzz1Method.invoke(o1);
                Method getOperationTime_Clzz2Method = clzz2.getMethod("getAge");
                int clzz2_date = (int) getOperationTime_Clzz2Method.invoke(o2);
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                System.out.println(clzz1_date+"----"+clzz2_date);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
            return flag;
        }
    }



    static class demo {

        public String name;
        public int age;


        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }


    }
}
