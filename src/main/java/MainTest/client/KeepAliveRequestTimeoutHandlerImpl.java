package MainTest.client;

import MainTest.server.KeepAliveMessageFactoryImpl;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.keepalive.KeepAliveFilter;
import org.apache.mina.filter.keepalive.KeepAliveRequestTimeoutHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KeepAliveRequestTimeoutHandlerImpl implements KeepAliveRequestTimeoutHandler {
    private static final Logger log = LoggerFactory.getLogger(KeepAliveRequestTimeoutHandlerImpl.class);
    @Override
    public void keepAliveRequestTimedOut(KeepAliveFilter filter, IoSession session) throws Exception {
        log.info("client - timeout");
    }
}
