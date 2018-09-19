package MainTest.client;

import MainTest.server.MinServerHandler;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.service.IoService;
import org.apache.mina.core.service.IoServiceListener;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MinaClientHandler extends IoHandlerAdapter implements IoServiceListener {
    private static final Logger log = LoggerFactory.getLogger(MinaClientHandler.class);

    @Override
    public void serviceActivated(IoService service) throws Exception {

    }

    @Override
    public void serviceIdle(IoService service, IdleStatus idleStatus) throws Exception {

    }

    @Override
    public void serviceDeactivated(IoService service) throws Exception {

    }

    @Override
    public void sessionCreated(IoSession session) throws Exception {
        super.sessionCreated(session);
        log.info(session.getRemoteAddress() + " - sessionCreated");
    }

    @Override
    public void sessionOpened(IoSession session) throws Exception {
        super.sessionOpened(session);
        log.info(session.getRemoteAddress() + " - sessionOpened");
    }

    @Override
    public void sessionClosed(IoSession session) throws Exception {
        super.sessionClosed(session);
        log.info(session.getRemoteAddress() + " - sessionClosed");
    }

    @Override
    public void sessionDestroyed(IoSession session) throws Exception {

    }

    @Override
    public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
        super.sessionIdle(session, status);
        log.info(session.getRemoteAddress() + " - sessionIdle");
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
        log.info(session.getRemoteAddress() + " - messageSent");
    }
}
