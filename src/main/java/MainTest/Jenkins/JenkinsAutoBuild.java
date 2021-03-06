package MainTest.Jenkins;

import MainTest.Jenkins.bean.LastBuildBean;
import MainTest.Jenkins.bean.ModuleInfo;
import MainTest.Jenkins.bean.ProjectDetailBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

public class JenkinsAutoBuild {

    private static final Logger log = LoggerFactory.getLogger(JenkinsAutoBuild.class);
    private static List<ModuleInfo> moduleInfoList = new ArrayList<>();

    public static void main(String[] args) {
        String projectName = "失败重试机制";

        moduleConfig();
        Properties properties = moduleRead();
        for (String str : properties.stringPropertyNames()) {
            String moduleStr = properties.getProperty(str);
            String moduleName = moduleStr.substring(moduleStr.indexOf("-"));
            ModuleInfo moduleInfo = new ModuleInfo();
            moduleInfo.setProjectName(projectName);
            moduleInfo.setProjectModuleName(moduleName);
            moduleInfoList.add(moduleInfo);
        }

        log.info("总数：" + properties.stringPropertyNames().size());
        int building = 0;
        for (ModuleInfo moduleInfo : moduleInfoList) {
            building++;
            log.info("正在构建 ......." + moduleInfo.getProjectName() + moduleInfo.getProjectModuleName());
            log.info("总数：" + building + "/" + properties.stringPropertyNames().size());
            ProjectBuild.config(moduleInfo);

            boolean isBuild = ProjectBuild.moduleBuild();
            try {
                Thread.sleep(2000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (isBuild) {
                //获取Jenkins打包详情页
                ProjectDetailBean moduleDetails = ProjectBuild.getModuleDetails();
                //获取最新构建编号
                int lastBuildNum = moduleDetails.getLastBuild().getNumber();
                String aClass = moduleDetails.getLastSuccessfulBuild().get_class();
                String url = moduleDetails.getLastSuccessfulBuild().getUrl();
                int lastSuccessfulBuildNum = moduleDetails.getLastSuccessfulBuild().getNumber();

                log.info("---------------------------");
                log.info(url);
                log.info("lastSuccessfulBuildNum:" + lastSuccessfulBuildNum);
                log.info("lastBuildNum:" + lastBuildNum);
                log.info("---------------------------");

                while (true) {
                    try {
                        Thread.sleep(2000L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    String progress = ProjectBuild.getProjectModuleBuildProgress();
                    log.info(progress);
                    LastBuildBean moduleLastBuild = ProjectBuild.getProjectModuleLastBuild();
                    //   log.info("moduleLastBuild.isBuilding():"+moduleLastBuild.isBuilding());
                    if (!moduleLastBuild.isBuilding()) {
                        for (LastBuildBean.ArtifactsBean artifactsBean : moduleLastBuild.getArtifacts()) {
                  /*          log.info(artifactsBean.getFileName());
                            log.info(artifactsBean.getDisplayPath());*/
                            log.info(artifactsBean.getRelativePath());
                        }
                        log.info("构建成功 " + moduleInfo.getProjectName() + moduleInfo.getProjectModuleName());
                        break;
                    }
                }
            } else {
                log.info("构建失败");
                break;
            }


        }


    }

    private static Properties moduleRead() {
        try {
            File file = new File("E:\\JenkinsModuleS.properties");
            if (!file.exists()) {
                file.createNewFile();
            }
            Properties properties = new Properties();
            properties.load(new FileInputStream(file));
            return properties;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static void moduleConfig() {
        try {
            File file = new File("E:\\JenkinsModuleS.properties");
            if (!file.exists()) {
                file.createNewFile();
            }
            Properties properties = new Properties();
            properties.load(new FileInputStream(file));
            properties.setProperty("1", "失败重试机制-thinkwin-cr");
            properties.setProperty("2", "失败重试机制-thinkwin-cr-base");
            properties.setProperty("3", "失败重试机制-thinkwin-publish");
            properties.setProperty("4", "失败重试机制-thinkwin-base-old");
            properties.setProperty("5", "失败重试机制-thinkwin-extjs-ui");
            properties.setProperty("6", "失败重试机制-thinkwin-base-user");
            properties.store(new FileOutputStream("E:\\JenkinsModuleS.properties"), "comment");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
