package MainTest.Netty;

import com.sun.istack.internal.Nullable;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Netty {
    private static final Logger log = LoggerFactory.getLogger(Netty.class);
    private static final long Default_Connects = 10;
    private static final String uri = "http://10.10.9.234/res/AnyDesk.exe";

    private static List<ChunkInfo> chunkInfos = new ArrayList<>();

    public static void main(String[] args) {

        HttpRequest httpRequest = initRequest(uri);
        // buildChunk();
        HttpResponse httpResponse = getResponse(httpRequest);
        String content_range = httpResponse.headers().get(HttpHeaderNames.CONTENT_RANGE);
        long fileTotalSize = 0;
        Pattern pattern = Pattern.compile("/(.+)");
        Matcher matcher = pattern.matcher(content_range);
        if (matcher.find()) {
            log.info(matcher.group(1));
            fileTotalSize = Long.valueOf(matcher.group(1));
        }


    }

    private static List<ChunkInfo> buildChunk(long fileTotalSize) {

        long blockSize = fileTotalSize / Default_Connects;//计算每个线程理论上下载的数量.
        for (int i = 0; i < Default_Connects; i++) {
            ChunkInfo chunkInfo = new ChunkInfo();
            long startIndex = i * blockSize; //线程开始下载的位置
            long endIndex = (i + 1) * blockSize - 1; //线程结束下载的位置
            if (i == (Default_Connects - 1)) {  //如果是最后一个线程,将剩下的文件全部交给这个线程完成
                endIndex = fileTotalSize - 1;
            }
            chunkInfo.setBlockSize(blockSize);//设置理论区块大小
            chunkInfo.setId(i);//设置id
            chunkInfo.setStartIndex(startIndex);//设置起始位置
            chunkInfo.setEndIndex(endIndex);//设置结束位置
            chunkInfo.setTotalSize(fileTotalSize); //设置文件总大小
            chunkInfos.add(chunkInfo);
        }
        return chunkInfos;
    }

    private static HttpResponse getResponse(HttpRequest httpRequest) {
        //线程同步锁
        CountDownLatch countDownLatch = new CountDownLatch(1);
        final ClientChannelInboundHandlerAdapter[] clientChannelInboundHandlerAdapter = {null};

        NioEventLoopGroup workGroup = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(workGroup);
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.handler(new ChannelInitializer() {
            @Override
            protected void initChannel(Channel channel) throws Exception {
                channel.pipeline().addLast("HttpClientCodec", new HttpClientCodec());
                clientChannelInboundHandlerAdapter[0] = new ClientChannelInboundHandlerAdapter();
                channel.pipeline().addLast(clientChannelInboundHandlerAdapter[0]);
            }
        });
        ChannelFuture channelFuture = bootstrap.connect("10.10.9.234", 80);
        channelFuture.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                if (channelFuture.isSuccess()) {
                    httpRequest.headers().set(HttpHeaderNames.RANGE, "bytes=0-0");
                    channelFuture.channel().writeAndFlush(httpRequest);
                    clientChannelInboundHandlerAdapter[0].setCountDownLatch(countDownLatch);
                } else {
                    countDownLatch.countDown();
                }
            }
        });
        try {
            countDownLatch.await(30L, TimeUnit.SECONDS);
            HttpResponse httpResponse = clientChannelInboundHandlerAdapter[0].getResponse();
            if (null != httpResponse) {
                return httpResponse;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    /**
     * 初始化请求对象
     *
     * @return 返回请求对象
     */
    private static HttpRequest initRequest(String uri) {
        try {
            URL url = new URL(uri);
            DefaultFullHttpRequest httpRequest = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, uri);
            //设置请求头
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


    private static long getFileTotalSize(String uri) {

        try {
            URL url = new URL(uri);
            String host = url.getHost();
            int port = url.getPort();


        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return 111;
    }


    static class ClientChannelInboundHandlerAdapter extends ChannelInboundHandlerAdapter {
        private HttpResponse httpResponse = null;
        private CountDownLatch countDownLatch;

        public HttpResponse getResponse() {
            return httpResponse;
        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            super.channelRead(ctx, msg);
            if (msg instanceof HttpResponse) {
                DefaultHttpResponse defaultHttpResponse = (DefaultHttpResponse) msg;
                log.info("HttpResponse:" + defaultHttpResponse.toString());
                String range = defaultHttpResponse.headers().get(HttpHeaderNames.CONTENT_RANGE);
                String content_length = defaultHttpResponse.headers().get(HttpHeaderNames.CONTENT_LENGTH);
                log.info("RANGE:" + range + " | content_length:" + content_length);
                httpResponse = defaultHttpResponse;
                countDownLatch.countDown();
            }
        }

        public void setCountDownLatch(CountDownLatch countDownLatch) {
            this.countDownLatch = countDownLatch;
        }
    }

}
