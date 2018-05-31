package MainTest.Jenkins;

public class Api {

    //Jenkins地址
    private static final String Jenkins_Host = "http://10.10.11.58:8080/job/";
    //分支项目名
    private static final String PROJECT_NAME = "失败重试机制";
    //模块名字
    private static final String PROJECT_MODULE_NAME = "-thinkwin-cr";
    //获取模块详情
    private static final String PROJECT_MODULE_DETAIL = "/api/json?pretty=true";
    //触发构建
    private static final String PROJECT_MODULE_BUILD = "/build?delay=0sec";
    //获取构建进度
    private static final String PROJECT_MODULE_BUILD_PROGRESS = "/buildHistory/ajax";

    private static final String download_url = "http://10.10.11.58:8080/job/%E5%A4%B1%E8%B4%A5%E9%87%8D%E8%AF%95%E6%9C%BA%E5%88%B6-thinkwin-cr/lastSuccessfulBuild/artifact/target/ROOT.war";

    /**
     * 获取Jenkins地址
     *
     * @return
     */
    public static String getBaseUrl() {
        return Jenkins_Host + PROJECT_NAME + PROJECT_MODULE_NAME;
    }

    /**
     * 获取模块构建详情
     *
     * @return
     */
    public static String getModuleDetailUrl() {
        return getBaseUrl() + PROJECT_MODULE_DETAIL;
    }

    /**
     * 触发构建地址
     *
     * @return
     */
    public static String moduleBuildUrl() {
        return getBaseUrl() + PROJECT_MODULE_BUILD;
    }

    /**
     * 获取构建进度
     *
     * @return
     */
    public static String getProjectModuleBuildPregressUrl() {
        return getBaseUrl() + PROJECT_MODULE_BUILD_PROGRESS;
    }


    /**
     * 获取构建进度
     *
     * @return
     */
    public static String getLastSuccessfulBuildDonwloadUrl() {
        return getBaseUrl() + PROJECT_MODULE_BUILD_PROGRESS + "/lastSuccessfulBuild/artifact/target/ROOT.war";
    }
}
