package MainTest.Netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DefaultHeaders;
import io.netty.handler.codec.http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

public class Netty {
    private static final Logger log = LoggerFactory.getLogger(Netty.class);

    public static void main(String[] args) {

        HttpRequest httpRequest = initRequest();
        NioEventLoopGroup workGroup = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(workGroup);
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.handler(new ChannelInitializer() {
            @Override
            protected void initChannel(Channel channel) throws Exception {
                channel.pipeline().addLast("HttpClientCodec", new HttpClientCodec());
                channel.pipeline().addLast(new ClientChannelInboundHandlerAdapter());
            }
        });

        ChannelFuture channelFuture = bootstrap.connect("10.10.9.234", 80);

        channelFuture.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                if (channelFuture.isSuccess()) {
                    httpRequest.headers().set(HttpHeaderNames.RANGE, "bytes=0-0");
                    channelFuture.channel().writeAndFlush(httpRequest);
                }
            }
        });
    }

    private static HttpRequest initRequest() {
        String uri = "http://10.10.9.234/res/AnyDesk.exe";
        try {
            URL url = new URL(uri);
            DefaultFullHttpRequest httpRequest = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, uri);
            HttpHeaders httpHeaders = httpRequest.headers();
            httpHeaders.add("Host", url.getHost());
            httpHeaders.add("Connection", "keep-alive");
            httpHeaders.add("Upgrade-Insecure-Requests", "1");
            httpHeaders.add("User-Agent",
                    "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.75 Safari/537.36");
            httpHeaders.add("Accept",
                    "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
            httpHeaders.add("Referer", url.getHost());
            httpHeaders.add("Accept-Encoding", "gzip, deflate, br");
            httpHeaders.add("Accept-Language", "zh-CN,zh;q=0.9");

            return httpRequest;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }

    }


    static class ClientChannelInboundHandlerAdapter extends ChannelInboundHandlerAdapter {
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            super.channelRead(ctx, msg);
            if (msg instanceof HttpResponse) {
                DefaultHttpResponse defaultHttpResponse = (DefaultHttpResponse) msg;
                log.info("HttpResponse:" + defaultHttpResponse.toString());
            }

        }
    }

}
