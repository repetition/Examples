package JAVAFX;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import sun.rmi.runtime.Log;

public class JavaFXWebView extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("webView.fxml"));
        Parent  parent = fxmlLoader.load();
        primaryStage.setScene(new Scene(parent, 800, 500,Color.web("#666970")));
        primaryStage.setTitle("Tools");
        primaryStage.setWidth(800);
        primaryStage.setHeight(500);
        primaryStage.setFullScreen(true);
        //  primaryStage.initModality(Modality.WINDOW_MODAL);
        primaryStage.initStyle(StageStyle.DECORATED);
        WebViewController controller = fxmlLoader.getController();
        controller.setStage(primaryStage);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
