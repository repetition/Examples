package WebMagic;

import org.hyperic.sigar.RPC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Selectable;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * http://www.caam.org.cn/newslist/a95-1.html  公告信息
 */
public class WebMagicMain {
    private static final Logger log = LoggerFactory.getLogger(WebMagicMain.class);

    private static int count = 0;

    public static void main(String[] args) {

        // Spider.create(new VehicleAnnouncementPageProcessor()).addUrl("http://data.miit.gov.cn/resultSearch?categoryTreeId=1128").run();
          Spider.create(new NoticePageProcessor()).addUrl("http://www.caam.org.cn/newslist/a95-1.html").run();
       // log.info(getLocalMacAddressFromIp());
        //log.info(getMac());
    }

    static class NoticePageProcessor implements PageProcessor {
        private Site site = Site.me().setUserAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.26 Safari/537.36 Core/1.63.6726.400 QQBrowser/10.2.2265.400");

        @Override
        public void process(Page page) {
            Html html = page.getHtml();

            Selectable xpath = html.xpath("//div[@class=\"xwzxlist xwzxlist_noline\"]//ul//li");

            List<Selectable> li_Nodes = xpath.nodes();

            for (Selectable node : li_Nodes) {
                Selectable a_node = node.xpath("//li//a");
                //列表信息
                String notice_Title = a_node.regex(">(.+?)<").get();
                String links = a_node.links().get();
                if (notice_Title.contains("车辆新产品公示")) {
                    log.info(notice_Title);
                    log.info(links);
                    Spider.create(new NoticePageInfoProcessor()).addUrl(links).run();
                }
            }
        }

        @Override
        public Site getSite() {
            site.setTimeOut(10000);
            site.setRetryTimes(10);
            return site;
        }


    }

    /**
     * 获取每个批次的公告列表
     */
    static class NoticePageInfoProcessor implements PageProcessor{
        private Site site = Site.me().setUserAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.26 Safari/537.36 Core/1.63.6726.400 QQBrowser/10.2.2265.400");
        @Override
        public void process(Page page) {
            Html html = page.getHtml();
            Selectable xpath = html.xpath("//div[@class=\"newstext\"]");
            //获取描述
            Selectable noticePageInfo = xpath.xpath("//p");
            String noticePageInfoStr = noticePageInfo.regex(">(.+?)<").get();
            //获取公告列表链接
            String links = xpath.xpath("//p//a").links().get();
            log.info(noticePageInfoStr+"");
            log.info(links+"");

            Pattern pattern = Pattern.compile("\\（(.+?)\\）");
            Matcher matcher = pattern.matcher(noticePageInfoStr);
            if (matcher.find()) {
                //获取批次
                String batch = matcher.group(1);
            }





        }

        @Override
        public Site getSite() {
            site.setTimeOut(10000);
            site.setRetryTimes(10);
            return site;
        }
    }


    /**
     * 车辆公告处理
     */
    static class VehicleAnnouncementPageProcessor implements PageProcessor {

        private Site site = Site.me().setUserAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.26 Safari/537.36 Core/1.63.6726.400 QQBrowser/10.2.2265.400");

        @Override
        public void process(Page page) {

            Html html = page.getHtml();

            Selectable selectable = html.xpath("//div[@id='page-wrapper']//div[@class=\"row\"]//div");

            List<Selectable> nodes = selectable.xpath("//ul[@class=\"pagination\"]//li").nodes();
            // 获取总的页数 <a href="javascript:void(0);" onclick="goPage('24')">24</a>
            String totalCount = nodes.get(nodes.size() - 1).xpath("//a").regex("goPage\\(\\'(.+?)'\\)\"").get();

            int totalPage = Integer.valueOf(totalCount);
            for (int i = 1; i <= totalPage; i++) {
                String pageUri = "http://data.miit.gov.cn/resultSearch?searchType=advancedSearch&categoryTreeId=1128&scol_Cpmc=&scol_Cpxh=&scol_Cpsb=&scol_Qymc=&pagenow=" + i;
                Spider.create(new VehicleAnnouncementPageInfoProcessor()).addUrl(pageUri).run();
            }

            log.info(count + "");
            //  log.info(html+"");

        }

        @Override
        public Site getSite() {
            return site;
        }
    }

    /**
     * 提取每行信息
     */
    static class VehicleAnnouncementPageInfoProcessor implements PageProcessor {
        private Site site = Site.me().setUserAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.26 Safari/537.36 Core/1.63.6726.400 QQBrowser/10.2.2265.400");

        @Override
        public void process(Page page) {
            getListInfo(page.getHtml());
        }

        @Override
        public Site getSite() {
            return site;
        }
    }

    private static void getListInfo(Html html) {
        Selectable tbody = html.xpath("//table[@class='table table-bordered table-responsive']");
        //获取当前页数据集合对象
        Selectable tbody_tr = tbody.xpath("//tbody//tr");
        //获取集合对象
        List<Selectable> tr_nodes = tbody_tr.nodes();
        count += tr_nodes.size();
        for (Selectable node : tr_nodes) {
            //获取编号    <td scope="row">1</td>
            String row = node.xpath("//td[@scope='row']").regex(">(.+?)<").get();
            log.info(row + "");
            //获取详细信息集合
            List<Selectable> td_nodes = node.xpath("//td[@style='CURSOR: pointer']").nodes();
       /*     for (Selectable td_node : td_nodes) {
                //企业名称
                String title = td_node.regex("title=\"(.+?)\">").get();
                log.info(title);
            }*/
            //企业名称
            String enterpriseName = td_nodes.get(0).regex("title=\"(.+?)\">").get();
            String href = td_nodes.get(0).xpath("//a").regex("href=\"(.+?)\"").get();
            log.info("http://data.miit.gov.cn" + href);
            href = "http://data.miit.gov.cn" + href;
            //产品商标
            String productTrademark = td_nodes.get(1).regex("title=\"(.+?)\">").get();
            //产品名称
            String productName = td_nodes.get(2).regex("title=\"(.+?)\">").get();
            //产品型号
            String productModel = td_nodes.get(3).regex("title=\"(.+?)\">").get();

            log.info(enterpriseName);
            log.info(productTrademark);
            log.info(productName);
            log.info(productModel);
            if (productName.contains("两轮摩托车")) {
                saveToFile(enterpriseName + " \r\n " + productTrademark + " | " + productName + " | " + productModel + " \r\n " + href + "\r\n\r\n");
            }
        }
    }

    private static void saveToFile(String buff_str) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream("d:\\carList.txt", true);
            fileOutputStream.write(buff_str.getBytes());
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据IP地址获取MAC地址
     *
     * @return
     */
    private static String getLocalMacAddressFromIp() {
        String strMacAddr = null;
        try {
            //获得IpD地址
            InetAddress ip = getLocalInetAddress();
            byte[] b = NetworkInterface.getByInetAddress(ip).getHardwareAddress();
            StringBuffer buffer = new StringBuffer();
            for (int i = 0; i < b.length; i++) {
                if (i != 0) {
                    buffer.append(':');
                }
                String str = Integer.toHexString(b[i] & 0xFF);
                buffer.append(str.length() == 1 ? 0 + str : str);
            }
            strMacAddr = buffer.toString().toUpperCase();
        } catch (Exception e) {

        }

        return strMacAddr;
    }

    /**
     * 获取移动设备本地IP
     *
     * @return
     */
    private static InetAddress getLocalInetAddress() {
        InetAddress ip = null;
        try {
            //列举
            Enumeration<NetworkInterface> en_netInterface = NetworkInterface.getNetworkInterfaces();
            while (en_netInterface.hasMoreElements()) {//是否还有元素
                NetworkInterface ni = (NetworkInterface) en_netInterface.nextElement();//得到下一个元素
                Enumeration<InetAddress> en_ip = ni.getInetAddresses();//得到一个ip地址的列举
                while (en_ip.hasMoreElements()) {
                    ip = en_ip.nextElement();
                    if (!ip.isLoopbackAddress() && ip.getHostAddress().indexOf(":") == -1) {
                        break;
                    } else {
                        ip = null;
                    }
                }

                if (ip != null) {
                    break;
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return ip;
    }

    //获取当前连接网络的网卡的mac地址
    private static String parseByte(byte b) {
        String s = "00" + Integer.toHexString(b)+":";
        return s.substring(s.length() - 3);
    }

    /**
     * 获取当前系统连接网络的网卡的mac地址
     * @return
     */
    public static final String getMac() {
        byte[] mac = null;
        StringBuffer sb = new StringBuffer();
        try {
            Enumeration<NetworkInterface> netInterfaces = NetworkInterface.getNetworkInterfaces();
            while (netInterfaces.hasMoreElements()) {
                NetworkInterface ni = netInterfaces.nextElement();
                Enumeration<InetAddress> address = ni.getInetAddresses();
                while (address.hasMoreElements()) {
                    InetAddress ip = address.nextElement();
                    if (ip.isAnyLocalAddress() || !(ip instanceof Inet4Address) || ip.isLoopbackAddress())
                        continue;
                    if (ip.isSiteLocalAddress()) {
                        mac = ni.getHardwareAddress();
                    } else if (!ip.isLinkLocalAddress()) {
                        mac = ni.getHardwareAddress();
                        break;
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }

        if(mac != null){
            for(int i=0 ;i<mac.length ;i++){
                sb.append(parseByte(mac[i]));
            }
            return sb.substring(0, sb.length()-1);
        }else {
            return null;
        }
    }





}
