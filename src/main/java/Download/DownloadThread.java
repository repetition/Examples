package Download;

import MainTest.Netty.ChunkInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class DownloadThread implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(DownloadThread.class);
    private static long downloadFileSize = 0;
    private ChunkInfo chunkInfo;

    public DownloadThread(ChunkInfo chunkInfo) {
        this.chunkInfo = chunkInfo;

    }

    @Override
    public void run() {
        long start = System.currentTimeMillis();
        log.info("线程：" + chunkInfo.getId() + "正在下载....");
        try {
            URL url = new URL(chunkInfo.getUri());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(10000);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.181 Safari/537.36");
            connection.setRequestProperty("Connection", "Keep-Alive");
            //设置分段下载的头信息。  Range:做分段数据请求用的。格式: Range bytes=0-1024  或者 bytes:0-1024
          //  connection.setRequestProperty("Range", "bytes=" + chunkInfo.getStartIndex() + "-" + chunkInfo.getEndIndex());
            connection.setReadTimeout(10000);
            if (connection.getResponseCode() != HttpURLConnection.HTTP_PARTIAL) {
                log.info("code:"+connection.getResponseCode());
               // return;
            }

            File file = new File(chunkInfo.getFilePath());
            RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
            //  randomAccessFile.setLength(chunkInfo.getTotalSize());
         //   randomAccessFile.seek(chunkInfo.getStartIndex());
            InputStream is = connection.getInputStream();
            byte[] bytes = new byte[1024 * 1024 * 10];
            int line;
            long length = 0;

            while ((line = is.read(bytes)) != -1) {
                randomAccessFile.write(bytes, 0, line);
                length += line;
                // log.info("线程"+chunkInfo.getId()+"下载的区块L:"+(chunkInfo.getEndIndex()-chunkInfo.getStartIndex())+": 已经下载："+length);
                //  downloadFileSize += line;
                //  log.info("下载进度：" + ((downloadFileSize * 1.0) / (chunkInfo.getTotalSize() - 1) * 1.0) * 100.0 + "%");
                fileSizeSum(line);
            }
            is.close();
            randomAccessFile.close();
            log.info("thread:"+chunkInfo.getId()+"|"+chunkInfo.getTotalSize() + "| " + downloadFileSize);

        } catch (IOException e) {
            e.printStackTrace();
        }

        if (downloadFileSize == chunkInfo.getTotalSize()) {
            long end = System.currentTimeMillis();
            log.info("downloadSuccess | time:" + (end - start) / 1000 + "s");

        }
    }

    /**
     * 计算文件总的下载进度
     *
     * @param line
     */
    public synchronized void fileSizeSum(int line) {
        downloadFileSize += line;
        log.info("下载进度：" + ((downloadFileSize * 1.0) / (chunkInfo.getTotalSize() - 1) * 1.0) * 100.0 + "%");
    }
}
