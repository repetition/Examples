package formdata;

import com.jacob.com.STA;

import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Map;

public class FormDataMain {

    static String url = "http://10.10.9.245:8083/fileUpload4";

    public static void main(String[] args) {
        formData();
        getFileName("E:\\无纸化测试附件\\图片\\Desert.jpg");
        getFileName("E:/无纸化测试附件/图片/Desert.jpg");
        getFileName("E:\\Desert.jpg");
        getFileName("E:/Desert.jpg");
    }

    private static void formData() {
        String boundary = "----WebKitFormBoundary9689DW4A1DR0cHSz";
        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection(new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 8888)));
            conn.setRequestMethod("POST");
            conn.setConnectTimeout(120 * 1000);
            conn.setReadTimeout(120 * 1000);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setRequestProperty("Connection", "keep-alive");
            // 设置字符编码
            conn.setRequestProperty("Charset", "UTF-8");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.75 Safari/537.36");
            conn.setRequestProperty("Charset", "UTF-8");
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);

            OutputStream os = conn.getOutputStream();
            //参数
            Map<String, String> parameterMaps = new HashMap<>();
            parameterMaps.put("imageNameIndex", "3");
            parameterMaps.put("url", "www.baidu.com");
            //文件
            Map<String, String> fileMaps = new HashMap<>();
            fileMaps.put("1", "E:\\无纸化测试附件\\图片\\Desert.jpg");
            fileMaps.put("2", "E:\\无纸化测试附件\\txt\\文本文本.txt");
            formDataParameters(os, parameterMaps, fileMaps);
            os.close();

            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream is = conn.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader reader = new BufferedReader(isr);
                String line;
                StringBuilder builder = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }
                System.out.println(builder.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * formData参数拼接
     *
     * @param os         httpUrlConnect输出流
     * @param parameters 参数列表
     * @param files      文件列表  key 编号 value 文件路径
     * @throws IOException
     */
    public static void formDataParameters(OutputStream os, Map<String, String> parameters, Map<String, String> files) throws IOException {
        String BOUNDARY = "----WebKitFormBoundary9689DW4A1DR0cHSz";
        String twoHyphens = "--";
        String end = "\r\n";
        StringBuffer sb = new StringBuffer();
        //参数拼接
        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            // 发送每个字段:
            sb = sb.append(twoHyphens + BOUNDARY + end);
            sb = sb.append("Content-Disposition: form-data; name=\"" + entry.getKey() + "\"");
            sb = sb.append(end);
            sb = sb.append(end);
            sb = sb.append(entry.getValue());
            sb = sb.append(end);
        }
        os.write(sb.toString().getBytes());
        //文件拼接
        for (Map.Entry<String, String> entry : files.entrySet()) {
            // 发送每个文件
            os.write((twoHyphens + BOUNDARY + end).getBytes());
            os.write(("Content-Disposition: form-data; name=\"file\"; filename=\"" + getFileName(entry.getValue()) + "\"").getBytes());
            os.write(end.getBytes());
            //contentType
            os.write(("Content-Type:" + switchType(getFileName(entry.getValue()))).getBytes());
            os.write(end.getBytes());
            os.write(end.getBytes());
            //文件写入
            fileWrite(os, entry.getValue());
            os.write(end.getBytes());
        }
        os.write((twoHyphens + BOUNDARY + twoHyphens).getBytes());
        os.flush();
    }

    /**
     * 根据路径获取 文件名字
     *
     * @param filePath 文件路径
     * @return
     */
    public static String getFileName(String filePath) {
        String fileName = "";
        if (filePath.lastIndexOf("\\") != -1) {
            fileName = filePath.substring(filePath.lastIndexOf("\\") + 1, filePath.length());
        }
        if (filePath.lastIndexOf("/") != -1) {
            fileName = filePath.substring(filePath.lastIndexOf("/") + 1, filePath.length());
        }
        System.out.println(fileName);
        return fileName;
    }

    /**
     * 文件写入流
     *
     * @param os       流
     * @param filePath 文件路径
     */
    public static void fileWrite(OutputStream os, String filePath) {
        try {
            FileInputStream fis = new FileInputStream(filePath);
            int len;
            byte[] bytes = new byte[1024 * 1024];
            while ((len = fis.read(bytes)) != -1) {
                os.write(bytes, 0, len);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据文件后缀名获取相应的contentType
     *
     * @param filePath 文件名字
     * @return
     */
    public static String switchType(String filePath) {
        String type = filePath.substring(filePath.lastIndexOf(".") + 1, filePath.length());
        String contentType = "";
        if ("txt".equals(type)) {
            contentType = "text/plain";
        } else if ("htm".equals(type) || "html".equals(type)) {
            contentType = "text/html";
        } else if ("jpg".equals(type)) {
            contentType = "image/jpeg";
        } else if ("gif".equals(type)) {
            contentType = "image/jpeg";
        } else if ("wav".equals(type)) {
            contentType = "audio/x-wave";
        } else if ("mp3".equals(type)) {
            contentType = "audio/mpeg";
        } else if ("mpg".equals(type)) {
            contentType = "video/mpeg";
        } else if ("zip".equals(type)) {
            contentType = "application/zip";
        } else if ("mp4".equals(type)) {
            contentType = "video/mpeg4";
        } else {
            contentType = "application/octet-stream";
        }
        return contentType;
    }
}
