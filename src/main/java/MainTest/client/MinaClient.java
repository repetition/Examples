package MainTest.client;

import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.filter.keepalive.KeepAliveFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

public class MinaClient {

    private static final Logger log = LoggerFactory.getLogger(MinaClient.class);

    /** 15秒发送一次心跳包 */
    private static final int HEARTBEATRATE = 15;

    public static void main(String[] args) {
        NioSocketConnector connector = new NioSocketConnector();
        connector.setHandler(new MinaClientHandler());
        connector.setConnectTimeoutMillis(5000L);
        connector.getSessionConfig().setUseReadOperation(true);

        DefaultIoFilterChainBuilder filterChain = connector.getFilterChain();
        TextLineCodecFactory factory = new TextLineCodecFactory(Charset.forName("UTF-8"));
        factory.setDecoderMaxLineLength(Integer.MAX_VALUE);
        factory.setEncoderMaxLineLength(Integer.MAX_VALUE);
        filterChain.addLast("executor", new ExecutorFilter());
        filterChain.addLast("codec", new ProtocolCodecFilter(factory));

        KeepAliveMessageFactoryImpl keepAliveMessageFactory = new KeepAliveMessageFactoryImpl();
        KeepAliveRequestTimeoutHandlerImpl keepAliveRequestTimeoutHandler = new KeepAliveRequestTimeoutHandlerImpl();
        KeepAliveFilter keepAliveFilter = new KeepAliveFilter(keepAliveMessageFactory,IdleStatus.WRITER_IDLE,keepAliveRequestTimeoutHandler);
/*        *//** 是否回发 *//*
        keepAliveFilter.setForwardEvent(true);*/
        /** 发送频率 */
        keepAliveFilter.setRequestInterval(HEARTBEATRATE);
        filterChain.addLast("heartbeat", keepAliveFilter);

        // filterChain.addLast("logging", new LoggingFilter());
        try {
            ConnectFuture future = connector.connect(new InetSocketAddress("10.10.9.241", 7007));
            future.awaitUninterruptibly();
            IoSession session = future.getSession();
          //  session.write("[{\"command\":\"END_MEETING\",\"order\":0}]");
            log.info("connect success");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
