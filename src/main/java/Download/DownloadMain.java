package Download;

import MainTest.Netty.ChunkInfo;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class DownloadMain {
    public static int HTREAD_COUNT = 10;
    private static final Logger log = LoggerFactory.getLogger(DownloadMain.class);
    public static final ThreadPoolExecutor executor = new ThreadPoolExecutor(20, 30, 1L, TimeUnit.SECONDS, new LinkedBlockingDeque<Runnable>());

    private static String uri = "https://gsf-fl.softonic.com/9fd/943/5a1b676abb3b325a0edbc0a54790d3d0ba/file?Expires=1530826135&Signature=3b4315be6523713c0e1cc2fcb20d7b0c5092dff7&SD_used=&channel=WEB&fdh=no&id_file=6cd2a998-96d7-11e6-ab0c-00163ed833e7&instance=softonic_en&type=PROGRAM&url=https://netspeedmonitor64.en.softonic.com&Filename=netspeedmonitor_2_5_4_0_x64_setup.msi";

    private static String filePath = "G:\\netspeedmonitor_2_5_4_0_x64_setup.msi";

    public static void main(String[] args) {


        long totalSize = getFileTotalSize(uri);
        log.info("文件总大小：" + totalSize);
        createTempFile(totalSize);

        /**
         * 多线程下载
         */
        List<ChunkInfo> chunkInfos = buildChunkInfos(totalSize);
        for (ChunkInfo chunkInfo : chunkInfos) {
            DownloadThread downloadThread = new DownloadThread(chunkInfo);
          //  executor.execute(downloadThread);
            new Thread(downloadThread).start();
            return;
        }

    }

    /**
     * 计算区块大小
     *
     * @param totalSize
     * @return
     */
    private static List<ChunkInfo> buildChunkInfos(long totalSize) {
        //计算每个线程的区块
        long blockSize = totalSize / HTREAD_COUNT;//计算每个线程理论上下载的数量.
        List<ChunkInfo> chunkInfos = new ArrayList<>();
        for (int i = 0; i < HTREAD_COUNT; i++) {
            ChunkInfo chunkInfo = new ChunkInfo();
            long startIndex = i * blockSize; //线程开始下载的位置

            long endIndex = (i + 1) * blockSize - 1; //线程结束下载的位置
            if (i == (HTREAD_COUNT - 1)) {  //如果是最后一个线程,将剩下的文件全部交给这个线程完成
                endIndex = totalSize - 1;
            }
            //设置理论区块大小
            chunkInfo.setBlockSize(blockSize);
            chunkInfo.setId(i);//设置id
            chunkInfo.setStartIndex(startIndex);//设置起始位置
            chunkInfo.setEndIndex(endIndex);//设置结束位置
            chunkInfo.setTotalSize(totalSize); //设置文件总大小
            chunkInfo.setUri(uri); //设置文件总大小
            chunkInfo.setFilePath(filePath);
            chunkInfos.add(chunkInfo);
            log.info("线程" + i + "startIndex：" + startIndex + "| endIndex:" + endIndex);
        }
        return chunkInfos;
    }

    private static void createTempFile(long totalSize) {
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        } else {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        RandomAccessFile randomAccessFile = null;
        try {
            randomAccessFile = new RandomAccessFile(file, "rw");
            randomAccessFile.setLength(totalSize);
            randomAccessFile.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static long getFileTotalSize(String uri) {
        long fileSize = 0;
        try {
            URL url = new URL(uri);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5 * 1000);
            conn.setRequestMethod("GET");
            conn.setRequestProperty(
                    "Accept",
                    "image/gif, image/jpeg, image/pjpeg, image/pjpeg, "
                            + "application/x-shockwave-flash, application/xaml+xml, "
                            + "application/vnd.ms-xpsdocument, application/x-ms-xbap, "
                            + "application/x-ms-application, application/vnd.ms-excel, "
                            + "application/vnd.ms-powerpoint, application/msword, */*");
            conn.setRequestProperty("Accept-Language", "zh-CN");
            conn.setRequestProperty("Charset", "UTF-8");
            conn.setRequestProperty("Connection", "Keep-Alive");
            // 得到文件大小
            fileSize = conn.getContentLength();
            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileSize;
    }
}