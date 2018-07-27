package JAVAFX;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JavaScriptCall {

    private static final Logger log = LoggerFactory.getLogger(JavaScriptCall.class);

    public void javaScriptCall(){
        log.info("Js Call Java");
    }
    public void javaScriptCall1(String str){
        log.info("Js Call Java :" +str);
    }
}
