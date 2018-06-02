package MainTest.Jenkins;

import MainTest.Jenkins.bean.LastBuildBean;
import MainTest.Jenkins.bean.ModuleInfo;
import MainTest.Jenkins.bean.ProjectDetailBean;
import MainTest.utlis.HTTPUtils;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProjectBuild {
    private static final Logger log = LoggerFactory.getLogger(ProjectBuild.class);

    private static ModuleInfo moduleInfo = null;

    /**
     * 配置模块信息
     *
     * @param moduleInfo 模块信息
     */
    public static void config(ModuleInfo moduleInfo) {
        Api.config(moduleInfo);
    }

    /**
     * 获取模块构建详情
     *
     * @return 返回构建实体对象
     */
    public static ProjectDetailBean getModuleDetails() {
        log.info(Api.getModuleDetailUrl());
        String module_build_detail = HTTPUtils.PostUrlAsString(Api.getModuleDetailUrl(), null);
        ProjectDetailBean projectDetailBean = buildGson().fromJson(module_build_detail, ProjectDetailBean.class);
        int lastSuccessfulBuildNum = projectDetailBean.getLastSuccessfulBuild().getNumber();
        int lastBuildNum = projectDetailBean.getLastBuild().getNumber();

        //判断是否已经构建
        if (lastBuildNum == lastSuccessfulBuildNum) {
            log.info("没有构建！");
        } else {
            //设置构建
/*            ModuleInfo moduleInfo = Api.getModuleInfo();
            moduleInfo.setLastBuildNum(lastBuildNum);
            moduleInfo.setLastSuccessfulBuildNum(lastSuccessfulBuildNum);
            Api.updataModileInfo(moduleInfo);*/
            Api.getModuleInfo().setLastBuildNum(lastBuildNum);
            Api.getModuleInfo().setLastSuccessfulBuildNum(lastSuccessfulBuildNum);
        }
        return projectDetailBean;
    }


    /**
     * 获取最后一次构建的详情
     *
     * @return 构建的进度
     */
    public static LastBuildBean getProjectModuleLastBuild() {
        log.info(Api.getModuleLastBuildNumDetail());
        String module_last_build = HTTPUtils.PostUrlAsString(Api.getModuleLastBuildNumDetail(), null);
        LastBuildBean lastBuildBean = buildGson().fromJson(module_last_build, LastBuildBean.class);
        return lastBuildBean;
    }


    /**
     * 触发构建
     *
     * @return true 构建成功 false  构建失败
     */
    public static boolean moduleBuild() {
        log.info(Api.moduleBuildUrl());
        //触发构建
        String module_building = HTTPUtils.PostUrlAsString(Api.moduleBuildUrl(), null);

        if (module_building.equals("")) {
            return true;
        }
        if (module_building.equals("fail")) {
            return false;
        }
        return false;
    }

    /**
     * 获取构建进度
     *
     * @return 构建的进度
     */
    public static String getProjectModuleBuildProgress() {
        log.info("getProjectModuleBuildProgress:" + Api.getProjectModuleBuildProgressUrl());
        //构建Jenkins项目
        String module_build_progress = HTTPUtils.PostUrlAsString(Api.getProjectModuleBuildProgressUrl(), null);
        String progress = getBuildProgress(module_build_progress);
        return progress;
    }

    /**
     * 获取模块构建详情
     *
     * @return true 下载成功  false 下载失败
     */
    public static String downloadLastSuccessfulROOT() {
        // TODO: 2018/5/31 root包现在待实现
        return null;
    }

    private static String getBuildProgress(String progressStr) {
        //通过正则获取进度信息
        Pattern pattern = Pattern.compile("<td style=\"width:(.+?);\" class");
        Matcher matcher = pattern.matcher(progressStr);

        if (!matcher.find()) {
            log.info("没有找到内容");
            return "";
        }
        String progress_find = matcher.group(1);
        log.info(progress_find);
        return progress_find;
    }

    private static Gson buildGson() {
        Gson gson = new Gson();
        return gson;
    }
}
