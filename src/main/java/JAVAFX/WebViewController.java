package JAVAFX;

import com.google.gson.JsonObject;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.FontSmoothingType;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebHistory;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import netscape.javascript.JSObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.ws.Holder;
import java.net.URL;
import java.util.ResourceBundle;


public class WebViewController implements Initializable {
    private static final Logger log = LoggerFactory.getLogger(WebViewController.class);
    public WebView mWebView;
    public Stage mStage;
    public Button mButton;
    public TextField mTFInput;
    public Button mBTOpen;
    public Button mBTBack;
    private WebEngine webEngine;
    private JavaScriptCall javaScriptCall;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        log.info("www.baidu.com....");
        mWebView.setContextMenuEnabled(true);
        mWebView.setMaxHeight(1080.0);
        mWebView.setMaxWidth(1920.0);
        mWebView.setZoom(1.0);
        mWebView.setFontSmoothingType(FontSmoothingType.LCD);

        webEngine = mWebView.getEngine();
        webEngine.setJavaScriptEnabled(true);
        javaScriptCall = new JavaScriptCall();
        JSObject window = (JSObject) webEngine.executeScript("window");
        window.setMember("app", javaScriptCall);

        webEngine.load("file://E:\\IdeaProjects\\MainTest\\src\\main\\java\\JAVAFX\\javaFxWebView.html");
        mWebView.resize(1080.0, 1920.0);
        double height = mWebView.getMaxHeight();
        double width = mWebView.getMaxWidth();

        log.info("height:" + height + "-" + "width:" + width);
        webEngine.titleProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                mStage.setTitle(newValue);
            }
        });

        String userAgent = webEngine.getUserAgent();
        log.info(userAgent);
    }

    public void setStage(Stage primaryStage) {
        mStage = primaryStage;
    }

    public void onAction(ActionEvent event) {
        //   mStage.hide();
        // mStage.initStyle(StageStyle.UNDECORATED);
        mStage.setFullScreen(true);
    }

    public void onOpenAction(ActionEvent event) {
        webEngine.load(mTFInput.getText());
    }

    public void onBackAction(ActionEvent event) {
        WebHistory history = webEngine.getHistory();
        history.go(-1);
    }

    public void onRefreshAction(ActionEvent event) {
        webEngine.reload();
    }

    public void onJavaFXCallJsAction(ActionEvent event) {
        webEngine.executeScript("alert()");
    }

    public void onJavaFXCallJs1Action(ActionEvent actionEvent) {
        webEngine.executeScript("alert1('java')");
    }
}
