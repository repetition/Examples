package JAVAFX;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.event.EventDispatchChain;
import javafx.event.EventDispatcher;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import sun.rmi.runtime.Log;

import java.awt.*;
import java.awt.event.AWTEventListener;

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

  /*      primaryStage.setEventDispatcher(new EventDispatcher() {
            @Override
            public Event dispatchEvent(Event event, EventDispatchChain tail) {
                System.out.println(event );
                return null;
            }
        });*/

        Toolkit.getDefaultToolkit().addAWTEventListener(new AWTEventListener() {
            @Override
            public void eventDispatched(AWTEvent event) {
                System.out.println(event);
            }
        },10L);
    }


    public static void main(String[] args) {
        launch(args);
    }
}
