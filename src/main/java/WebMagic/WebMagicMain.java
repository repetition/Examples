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
import java.util.List;

/**
 * http://www.caam.org.cn/newslist/a95-1.html  公告信息
 */
public class WebMagicMain {
    private static final Logger log = LoggerFactory.getLogger(WebMagicMain.class);

    private static int count = 0;

    public static void main(String[] args) {

        Spider.create(new VehicleAnnouncementPageProcessor()).addUrl("http://data.miit.gov.cn/resultSearch?categoryTreeId=1128").run();

    }

    /**
     * 车辆公告处理
     */
    static class VehicleAnnouncementPageProcessor implements PageProcessor{

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
                String pageUri = "http://data.miit.gov.cn/resultSearch?searchType=advancedSearch&categoryTreeId=1128&scol_Cpmc=&scol_Cpxh=&scol_Cpsb=&scol_Qymc=&pagenow="+i;
                Spider.create(new VehicleAnnouncementPageInfoProcessor()).addUrl(pageUri).run();
            }

            log.info(totalCount);
          //  log.info(html+"");

        }

        @Override
        public Site getSite() {
            return site;
        }
    }


    static class  VehicleAnnouncementPageInfoProcessor implements PageProcessor{
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
        count +=tr_nodes.size();
        for (Selectable node : tr_nodes) {
            //获取编号    <td scope="row">1</td>
            String row = node.xpath("//td[@scope='row']").regex(">(.+?)<").get();
            log.info(row+"");
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
            log.info("http://data.miit.gov.cn"+href);
            href = "http://data.miit.gov.cn"+href;
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

            saveToFile(enterpriseName+","+href+","+enterpriseName+","+productTrademark+","+productName+","+productModel);
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

}
