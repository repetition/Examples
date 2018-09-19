package MainTest.server;

import org.apache.log4j.PropertyConfigurator;
import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.filter.keepalive.KeepAliveFilter;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;

public class MinaServer {
    private static final Logger log = LoggerFactory.getLogger(MinaServer.class);
    /** 30秒后超时 */
    private static final int IDELTIMEOUT = 15;
    /** 15秒发送一次心跳包 */
    private static final int HEARTBEATRATE = 15;
    /** 心跳包内容 */
    private static final String HEARTBEATREQUEST = "HEARTBEATREQUEST";
    private static final String HEARTBEATRESPONSE = "HEARTBEATRESPONSE";

    static {
        System.out.println(MinaServer.class.getResource("log4j.properties"));
        PropertyConfigurator.configure(MinaServer.class.getResource("log4j.properties"));
    }

    public static void main(String[] args) {



        log.info("start...");
        IoAcceptor ioAcceptor = new NioSocketAcceptor();
        ioAcceptor.getSessionConfig().setReadBufferSize(2048*2);
        ioAcceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, IDELTIMEOUT);
        DefaultIoFilterChainBuilder filterChain = ioAcceptor.getFilterChain();
        TextLineCodecFactory factory = new TextLineCodecFactory(Charset.forName("UTF-8"));
        factory.setDecoderMaxLineLength(Integer.MAX_VALUE);
        factory.setEncoderMaxLineLength(Integer.MAX_VALUE);
        filterChain.addLast("executor",new ExecutorFilter());
        filterChain.addLast("codec", new ProtocolCodecFilter(factory));
        ioAcceptor.setHandler(new MinServerHandler());
        // filterChain.addLast("logging", new LoggingFilter());

        KeepAliveMessageFactoryImpl keepAliveMessageFactory = new KeepAliveMessageFactoryImpl();
        KeepAliveRequestTimeoutHandlerImpl keepAliveRequestTimeoutHandler = new KeepAliveRequestTimeoutHandlerImpl();
        KeepAliveFilter keepAliveFilter = new KeepAliveFilter(keepAliveMessageFactory,IdleStatus.READER_IDLE,keepAliveRequestTimeoutHandler);
        keepAliveFilter.setRequestTimeout(HEARTBEATRATE);
        keepAliveFilter.setForwardEvent(true);

        ioAcceptor.getFilterChain().addLast("heartbeat", keepAliveFilter);
        try {
            ioAcceptor.bind(new InetSocketAddress(7007));
            log.info("start success");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
