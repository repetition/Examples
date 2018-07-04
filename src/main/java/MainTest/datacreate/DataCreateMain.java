package MainTest.datacreate;

import java.util.List;

public class DataCreateMain {

    public static void main(String[] args) {
        //create500();
       // create700();
         create100();

       // DataCreator.deletePersonRole("8a8a8971645de458016463e219470488"); //org100
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

    private static void create500() {
        //创建机构
        List<String> list = DataCreator.createOrg("org500");
        //创建人员
        DataCreator.createPerson(0,500, list.get(0), list.get(1));
        //创建用户
        DataCreator.createUser(list.get(0));
        //创建授权
        DataCreator.createPersonRole(list.get(0));

    }

    private static void create700() {
        //创建机构
        List<String> list = DataCreator.createOrg("org700");
        //创建人员
        DataCreator.createPerson(501,1201, list.get(0), list.get(1));
        //创建用户
        DataCreator.createUser(list.get(0));
        //创建授权
        DataCreator.createPersonRole(list.get(0));

    }
}
