package MainTest.Jenkins;

import MainTest.utlis.HTTPUtils;
import com.google.gson.Gson;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JenkinsAutoBuild {

    private static final Logger log = LoggerFactory.getLogger(JenkinsAutoBuild.class);

    private static final String Jenkins_Host = "http://10.10.11.58:8080/job/";

    private static final String PROJECT_NAME = "失败重试机制";

    private static final String PROJECT_MODULE_ROOT_NAME = "-thinkwin-cr";
    //模块构建详情页
    private static final String PROJECT_MODULE_ROOT_URL = Jenkins_Host + PROJECT_NAME + PROJECT_MODULE_ROOT_NAME + "/api/json?pretty=true";
    //模块触发构建
    private static final String PROJECT_MODULE_ROOT_BUILD_URL = Jenkins_Host + PROJECT_NAME + PROJECT_MODULE_ROOT_NAME + "/build?delay=0sec";
    //获取模块构建进度
    private static final String PROJECT_MODULE_ROOT_BUILD_PROGRESS_URL = Jenkins_Host + PROJECT_NAME + PROJECT_MODULE_ROOT_NAME + "/buildHistory/ajax";

    public static void main(String[] args) {

        //构建Jenkins项目
        String project_module_root_build_result = HTTPUtils.PostUrlAsString(PROJECT_MODULE_ROOT_BUILD_URL, null);

        log.info("Build");
        log.info(project_module_root_build_result);

        //获取Jenkins打包详情页
        String project_root_detail = HTTPUtils.PostUrlAsString(PROJECT_MODULE_ROOT_URL, null);
        ProjectDetailBean projectDetailBean = buildGson().fromJson(project_root_detail, ProjectDetailBean.class);

        try {
            Thread.sleep(2000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //获取最新构建编号
        int lastBuildNum = projectDetailBean.getLastBuild().getNumber();
        String aClass = projectDetailBean.getLastSuccessfulBuild().get_class();
        String url = projectDetailBean.getLastSuccessfulBuild().getUrl();
        int lastSuccessfulBuildNum = projectDetailBean.getLastSuccessfulBuild().getNumber();

        log.info("LastSuccessfulBuild");
        log.info(aClass);
        log.info(url);
        log.info(lastSuccessfulBuildNum + "");
        log.info("LastBuild");
        log.info(lastBuildNum + "");


        if (lastBuildNum == lastSuccessfulBuildNum) {
            log.info("没有构建！");
            return;
        }

        Timer progressTimer = new Timer();
        progressTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                String progress = getBuildProgress();

                if (progress.equals("")) {
                    progressTimer.cancel();
                }
            }
        }, 1000L, 2000L);
        //获取构建信息
        String PROJECT_MODULE_ROOT_LAST_BUILD_URL = Jenkins_Host + PROJECT_NAME + PROJECT_MODULE_ROOT_NAME + "/" + lastBuildNum + "/api/json?pretty=true";
        Timer buildTimer = new Timer();
        buildTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                String project_build_detail = HTTPUtils.PostUrlAsString(PROJECT_MODULE_ROOT_LAST_BUILD_URL, null);
                LastBuildBean lastBuildBean = buildGson().fromJson(project_build_detail, LastBuildBean.class);

                boolean building = lastBuildBean.isBuilding();
                log.info("是否构建：" + building);
                if (!building) {
                    String result = lastBuildBean.getResult();
                    log.info("state:" + result);
                    List<LastBuildBean.ArtifactsBean> artifacts = lastBuildBean.getArtifacts();
                    for (LastBuildBean.ArtifactsBean artifact : artifacts) {
                        String fileName = artifact.getFileName();
                        String displayPath = artifact.getDisplayPath();
                        String relativePath = artifact.getRelativePath();
                        log.info(fileName);
                        log.info(displayPath);
                        log.info(relativePath);
                    }
                    String fullDisplayName = lastBuildBean.getFullDisplayName();
                    log.info(fullDisplayName);
                    buildTimer.cancel();
                }
            }
        }, 1000L, 2000L);


    }

    private static String getBuildProgress() {
        //构建Jenkins项目
        String project_module_root_build_progress_result = HTTPUtils.PostUrlAsString(PROJECT_MODULE_ROOT_BUILD_PROGRESS_URL, null);

        //通过正则获取进度信息
        Pattern pattern = Pattern.compile("<td style=\"width:(.+?);\" class");
        Matcher matcher = pattern.matcher(project_module_root_build_progress_result);

        if (!matcher.find()) {
            log.info("没有找到内容");
            return "";
        }

    /*    for (int i = 0; i <= matcher.groupCount(); i++) {
            System.out.println(i+""+matcher.group(i));
        }*/
        String progressStr = matcher.group(1);
        log.info(progressStr);
        return progressStr;
    }


    private static Gson buildGson() {
        Gson gson = new Gson();
        return gson;
    }
}
