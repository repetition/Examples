package MainTest.client;

import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

public class MinaClient {

    private static final Logger log = LoggerFactory.getLogger(MinaClient.class);

    public static void main(String[] args) {
        NioSocketConnector connector = new NioSocketConnector();
        connector.setHandler(new MinaClientHandler());
        connector.setConnectTimeoutMillis(5000L);
        connector.getSessionConfig().setUseReadOperation(true);

        DefaultIoFilterChainBuilder filterChain = connector.getFilterChain();
        TextLineCodecFactory factory = new TextLineCodecFactory(Charset.forName("UTF-8"));
        factory.setDecoderMaxLineLength(Integer.MAX_VALUE);
        factory.setEncoderMaxLineLength(Integer.MAX_VALUE);
        filterChain.addLast("executor",new ExecutorFilter());
        filterChain.addLast("codec", new ProtocolCodecFilter(factory));
       // filterChain.addLast("logging", new LoggingFilter());

        ConnectFuture future = connector.connect(new InetSocketAddress("10.10.9.234", 7007));
        future.awaitUninterruptibly();
        IoSession session = future.getSession();
 /*       try {
            Thread.sleep(2000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
        log.info("connect success");

        session.write("测试包活时间！");
    }
}
