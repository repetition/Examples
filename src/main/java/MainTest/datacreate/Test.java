package MainTest.datacreate;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;

import java.io.File;
import java.text.DecimalFormat;
import java.util.Timer;
import java.util.TimerTask;

public class Test extends Application {

    @Override
    public void start(Stage stage) {
        stage.setTitle("测试统计图");

        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis(0,100,10);
        yAxis.setSide(Side.LEFT);
      //  yAxis.setMinorTickLength(0);
       // yAxis.tickUnitProperty().setValue(100);
     //   yAxis.upperBoundProperty().setValue(100);
        yAxis.setMinorTickCount(11);
        xAxis.setLabel("                                              "
                + "                                                                  X 轴：时间");
        yAxis.setLabel("                                                       Y 轴：测试值");
        final LineChart<Number, Number> lineChart =
                new LineChart<Number, Number>(xAxis, yAxis);

        //lineChart.setTitle("测试统计图");
        XYChart.Series series = new XYChart.Series();
        series.setName("测试统计图");
        //获取的数据填写，从后台获取。用C#写个DLL貌似能获取
/*        series.getData().add(new XYChart.Data(1, 23));
       series.getData().add(new XYChart.Data(2, 14));*/
/*        series.getData().add(new XYChart.Data(2, 14));
        series.getData().add(new XYChart.Data(3, 15));
        series.getData().add(new XYChart.Data(4, 24));
        series.getData().add(new XYChart.Data(5, 34));
        series.getData().add(new XYChart.Data(6, 36));
        series.getData().add(new XYChart.Data(7, 22));
        series.getData().add(new XYChart.Data(8, 45));
        series.getData().add(new XYChart.Data(9, 43));
        series.getData().add(new XYChart.Data(10, 17));
        series.getData().add(new XYChart.Data(11, 29));
        series.getData().add(new XYChart.Data(12, 25));*/

        Scene scene = new Scene(lineChart, 600, 400);
            lineChart.getData().add(series);
        lineChart.setCreateSymbols(false);
        lineChart.setAxisSortingPolicy(LineChart.SortingPolicy.X_AXIS);
        stage.setScene(scene);
        stage.show();

        final int[] count = {1};
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        count[0]++;
                        double combined = 0;
                        try {
                            combined = sigar.getCpuPerc().getCombined();
                        } catch (SigarException e) {
                            e.printStackTrace();
                        }
                        int anInt = DoubleFormatInt(combined * 100);
                        series.getData().add(new XYChart.Data(count[0], anInt));
                    }
                });
            }
        }, 1000L, 1000L);

    }

    public static void main(String[] args) {
        launch(args);
    }


    public final static Sigar sigar = initSigar();

    private static Sigar initSigar() {
        try {
            String file = Test.class.getResource("sigar/sigar-amd64-winnt.dll").getFile();
            System.out.println(file);
            File classPath = new File(file).getParentFile();

            String path = System.getProperty("java.library.path");
            if (isOSWin()) {
                path += ";" + classPath.getCanonicalPath();
            } else {
                path += ":" + classPath.getCanonicalPath();
            }
            System.setProperty("java.library.path", path);
            return new Sigar();
        } catch (Exception e) {
            return null;
        }
    }

    public static boolean isOSWin() {//OS 版本判断
        String OS = System.getProperty("os.name").toLowerCase();
        if (OS.indexOf("win") >= 0) {
            return true;
        } else return false;
    }

    //四舍五入把double转化为int类型整数,0.5也舍去,0.51进一
    public static int DoubleFormatInt(Double dou){
        DecimalFormat df = new DecimalFormat("######0"); //四色五入转换成整数
        return Integer.parseInt(df.format(dou));
    }
}
