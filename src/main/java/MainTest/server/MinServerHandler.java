package MainTest.server;

import MainTest.utlis.HTTPUtils;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class MinServerHandler extends IoHandlerAdapter {
    private static final Logger log = LoggerFactory.getLogger(MinServerHandler.class);
    private static Map<String, IoSession> ioSessionMap = new HashMap<>();

    public boolean isAdd = false;

    @Override
    public void sessionCreated(IoSession session) throws Exception {
        super.sessionCreated(session);
        log.info(session.getRemoteAddress().toString() + " - sessionCreated");
    }

    @Override
    public void sessionOpened(IoSession session) throws Exception {
        super.sessionOpened(session);
        log.info(session.getRemoteAddress().toString() + " - sessionOpened");

    }

    @Override
    public void sessionClosed(IoSession session) throws Exception {
        super.sessionClosed(session);
        log.info(session.getRemoteAddress() + " - sessionClosed");
    }

    @Override
    public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
        super.sessionIdle(session, status);
        log.info(session.getRemoteAddress().toString() + " - sessionIdle");
    }

    @Override
    public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
        super.exceptionCaught(session, cause);
        log.info(session.getRemoteAddress() + " - exceptionCaught");
    }


    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
        super.messageReceived(session, message);
        log.info(session.getRemoteAddress() + " - messageReceived");
    }

    @Override
    public void messageSent(IoSession session, Object message) throws Exception {
        super.messageSent(session, message);
        log.info(session.getRemoteAddress().toString() + " - messageSent");
    }
}
