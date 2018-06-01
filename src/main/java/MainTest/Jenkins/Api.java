package MainTest.Jenkins;

import MainTest.Jenkins.bean.ModuleInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Api {
    private static final Logger log = LoggerFactory.getLogger(Api.class);

    //Jenkins地址
    private static final String Jenkins_Host = "http://10.10.11.58:8080/job/";
    //获取模块详情
    private static final String PROJECT_MODULE_DETAIL = "/api/json?pretty=true";
    //触发构建
    private static final String PROJECT_MODULE_BUILD = "/build?delay=0sec";
    //获取构建进度
    private static final String PROJECT_MODULE_BUILD_PROGRESS = "/buildHistory/ajax";

    //获取构建的模块详情
    private static final String projectModuleBuildDetail = "/api/json?pretty=true";

    private static ModuleInfo mModuleInfo = null;

    /**
     * 获取Jenkins地址
     *
     * @return
     */
    public static void config(ModuleInfo moduleInfo) {
        mModuleInfo = moduleInfo;
    }


    /**
     * 获取Jenkins地址
     *
     * @return
     */
    public static String getBaseUrl() {

        return Jenkins_Host + mModuleInfo.getProjectName() + mModuleInfo.getProjectModuleName();
    }

    public static String getModuleLastBuildNumDetail() {

        return getBaseUrl() + "/" + mModuleInfo.getLastBuildNum() + projectModuleBuildDetail;
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
     * @return 返回构建的进度地址
     */
    public static String getProjectModuleBuildProgressUrl() {
        return getBaseUrl() + PROJECT_MODULE_BUILD_PROGRESS;
    }


    /**
     * 获取ROOT.war下载地址
     *
     * @return 返回ROOT.war下载地址
     */
    public static String getLastSuccessfulBuildDonwloadUrl() {
        return getBaseUrl() + PROJECT_MODULE_BUILD_PROGRESS + "/lastSuccessfulBuild/artifact/target/ROOT.war";
    }


    public static ModuleInfo getModuleInfo() {
        return mModuleInfo;
    }
}
