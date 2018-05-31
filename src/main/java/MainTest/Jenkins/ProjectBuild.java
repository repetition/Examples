package MainTest.Jenkins;

import MainTest.utlis.HTTPUtils;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProjectBuild {
    private static final Logger log = LoggerFactory.getLogger(ProjectBuild.class);

    /**
     * 获取模块构建详情
     *
     * @return 返回构建实体对象
     */
    public static LastBuildBean getModuleDetails() {
        String module_build_detail = HTTPUtils.PostUrlAsString(Api.getModuleDetailUrl(), null);

        return buildGson().fromJson(module_build_detail, LastBuildBean.class);
    }

    /**
     * 触发构建
     *
     * @return true 构建成功 false  构建失败
     */
    public static boolean moduleBuild() {
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
    public static String getProjectModuleBuildPregress() {
        //构建Jenkins项目
        String module_build_progress = HTTPUtils.PostUrlAsString(Api.getProjectModuleBuildPregressUrl(), null);
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

    /*    for (int i = 0; i <= matcher.groupCount(); i++) {
            System.out.println(i+""+matcher.group(i));
        }*/
        String progress_find = matcher.group(1);
        log.info(progress_find);
        return progress_find;
    }

    private static Gson buildGson() {
        Gson gson = new Gson();
        return gson;
    }
}
