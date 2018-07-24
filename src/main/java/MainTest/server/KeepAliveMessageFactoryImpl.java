package MainTest.server;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.keepalive.KeepAliveMessageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KeepAliveMessageFactoryImpl implements KeepAliveMessageFactory {
    private static final Logger log = LoggerFactory.getLogger(KeepAliveMessageFactoryImpl.class);
    /** 心跳包内容 */
    private static final String HEARTBEATREQUEST = "HEARTBEATREQUEST";
    private static final String HEARTBEATRESPONSE = "HEARTBEATRESPONSE";
    @Override
    public boolean isRequest(IoSession session, Object message) {
        return false;
    }

    @Override
    public boolean isResponse(IoSession session, Object message) {
        if(message.equals(HEARTBEATREQUEST)){
            log.info("接收到客户端心数据包引发心跳事件跳数据包是》》" + message);
            return true;
        }
        return false;
    }

    @Override
    public Object getRequest(IoSession session) {
        return null;
    }

    @Override
    public Object getResponse(IoSession session, Object request) {

        return request;
    }
}
