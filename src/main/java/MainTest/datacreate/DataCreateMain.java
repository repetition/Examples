package MainTest.datacreate;

import java.util.List;

public class DataCreateMain {

    public static void main(String[] args) {
       // create500();
     // create700();
      //   create100();
     //   create400();
        create1000();

        DataCreator.deletePersonRole("4028496864ad8ba80164ada38560000f"); //org100
        //DataCreator.deletePersonRole("8a8a8971645de458016463ed68df124e");
    }

    private static void create100() {
        //创建机构
        List<String> list = DataCreator.createOrg("org100");
        //创建人员
        DataCreator.createPerson(0,100, list.get(0), list.get(1));
        //创建用户
        DataCreator.createUser(list.get(0));
        //创建授权
        DataCreator.createPersonRole(list.get(0));

    }

    private static void create400() {
        //创建机构
        List<String> list = DataCreator.createOrg("org400");
        //创建人员
        DataCreator.createPerson(1301,1701, list.get(0), list.get(1));
        //创建用户
        DataCreator.createUser(list.get(0));
        //创建授权
        DataCreator.createPersonRole(list.get(0));

    }
    private static void create500() {
        //创建机构
        List<String> list = DataCreator.createOrg("org500");
        //创建人员
        DataCreator.createPerson(100,601, list.get(0), list.get(1));
        //创建用户
        DataCreator.createUser(list.get(0));
        //创建授权
        DataCreator.createPersonRole(list.get(0));

    }

    private static void create700() {
        //创建机构
        List<String> list = DataCreator.createOrg("org700");
        //创建人员
        DataCreator.createPerson(601,1301, list.get(0), list.get(1));
        //创建用户
        DataCreator.createUser(list.get(0));
        //创建授权
        DataCreator.createPersonRole(list.get(0));

    }


    private static void create1000() {
        //创建机构
        List<String> list = DataCreator.createOrg("org1000");
        //创建人员
        DataCreator.createPerson(1301,2301, list.get(0), list.get(1));
        //创建用户
        DataCreator.createUser(list.get(0));
        //创建授权
        DataCreator.createPersonRole(list.get(0));

    }
}
