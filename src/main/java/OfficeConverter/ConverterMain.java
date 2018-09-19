package OfficeConverter;

import com.google.common.io.Resources;
import fr.opensagres.xdocreport.converter.*;
import fr.opensagres.xdocreport.core.document.DocumentKind;

import java.io.*;
import java.lang.reflect.Field;
import java.util.Date;

public class ConverterMain {
    static final int wdDoNotSaveChanges = 0;// 不保存待定的更改。
    static final int wdFormatPDF = 17;// word转PDF 格式

   // private static final int wdFormatPDF = 17;
    private static final int xlTypePDF = 0;
    private static final int ppSaveAsPDF = 32;

/*    public static void main(String[] args) {
*//*        String dir = "E:\\ThinkWin\\";
// load the file to be converted
        Document document = null;
        try {
            document = new Document(dir + "test.docx");
// save in different formats
            document.save(dir + "output3.pdf", SaveFormat.PDF);
            document.save(dir + "output3.html", SaveFormat.HTML);
        } catch (Exception e) {
            e.printStackTrace();
        }*//*

        initApp();
        String source = "E:\\b85be994528b6f4c4ef83c3b5ef00e2a.pptx";
        String target = "E:\\b85be994528b6f4c4ef83c3b5ef00e2a.pdf";
       // word2pdf(source,target);
       // ppt2pdf(source,target);
        ppt2PDF(source,target);
      //  Ex2PDF("E:\\无纸化测试附件\\xls\\信息发布屏组件需求说明.xlsx","E:\\无纸化测试附件\\xls\\信息发布屏组件需求说明.pdf");

    }
    public static boolean word2pdf(String source, String target) {
        System.out.println("Word转PDF开始启动...");
        long start = System.currentTimeMillis();
        ActiveXComponent app = null;
        try {
            app = new ActiveXComponent("Word.Application");
            app.setProperty("Visible", false);
            Dispatch docs = app.getProperty("Documents").toDispatch();
            System.out.println("打开文档：" + source);
            Dispatch doc = Dispatch.call(docs, "Open", source, false, true,1).toDispatch();
            System.out.println("转换文档到PDF：" + target);
            File tofile = new File(target);
            if (tofile.exists()) {
                tofile.delete();
            }
            Dispatch.call(doc, "SaveAs", target, wdFormatPDF);
            Dispatch.call(doc, "Close", false);
            long end = System.currentTimeMillis();
            System.out.println("转换完成，用时：" + (end - start) + "ms");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Word转PDF出错：" + e.getMessage());
            return false;
        } finally {
            if (app != null) {
                app.invoke("Quit", wdDoNotSaveChanges);
            }
        }
    }

    public static boolean ppt2pdf(String source, String target) {
        System.out.println("Word转PDF开始启动...");
        long start = System.currentTimeMillis();
        ActiveXComponent app = null;
        try {
            app = new ActiveXComponent("Powerpoint.Application");
           // app.setProperty("Visible", false);
            Dispatch ppts  = app.getProperty("Presentations").toDispatch();
            System.out.println("打开文档：" + source);
            Dispatch ppt = Dispatch.call(ppts, "Open", source, false, true,1).toDispatch();
            System.out.println("转换文档到PDF：" + target);
            File tofile = new File(target);
            if (tofile.exists()) {
                tofile.delete();
            }
            Dispatch.call(ppt, "SaveAs", target, 32);
            Dispatch.call(ppt, "Close");
            long end = System.currentTimeMillis();
            System.out.println("转换完成，用时：" + (end - start) + "ms");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("PPT转PDF出错：" + e.getMessage());
            return false;
        } finally {
            if (app != null) {
                app.invoke("Quit", new Variant[] {});
            }
        }
    }
    *//**
     * PPT文档转换
     *
     * @param inputFile
     * @param pdfFile
     * @author SHANHY
     *//*
    private static boolean ppt2PDF(String inputFile, String pdfFile) {
        ComThread.InitSTA();

        long start = System.currentTimeMillis();
        ActiveXComponent app =null ;
        Dispatch ppt = null;
        try {
            app = new ActiveXComponent("PowerPoint.Application");// 创建一个PPT对象
            // app.setProperty("Visible", new Variant(false)); // 不可见打开（PPT转换不运行隐藏，所以这里要注释掉）
            // app.setProperty("AutomationSecurity", new Variant(3)); // 禁用宏
            Dispatch ppts = app.getProperty("Presentations").toDispatch();// 获取文挡属性

            System.out.println("打开文档 >>> " + inputFile);
            // 调用Documents对象中Open方法打开文档，并返回打开的文档对象Document
            ppt = Dispatch.call(ppts, "Open", inputFile,
                    true,// ReadOnly
                    true,// Untitled指定文件是否有标题
                    false// WithWindow指定文件是否可见
            ).toDispatch();

            System.out.println("转换文档 [" + inputFile + "] >>> [" + pdfFile + "]");
            Dispatch.call(ppt, "SaveAs", pdfFile, 32);

            long end = System.currentTimeMillis();

            System.out.println("用时：" + (end - start) + "ms.");

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("========Error:文档转换失败：" + e.getMessage());
        } finally {
            Dispatch.call(ppt, "Close");
            System.out.println("关闭文档");
            if (app != null)
                app.invoke("Quit", new Variant[] {});
        }
        ComThread.Release();
        ComThread.quitMainSTA();
        return false;
    }

    *//***
     *
     * Excel转化成PDF
     *
     * @param inputFile
     * @param pdfFile
     * @return
     *//*
    private static int Ex2PDF(String inputFile, String pdfFile) {
        try {

            ComThread.InitSTA(true);
            ActiveXComponent ax = new ActiveXComponent("Excel.Application");
            System.out.println("开始转化Excel为PDF...");
            long date = new Date().getTime();
            ax.setProperty("Visible", false);
            ax.setProperty("AutomationSecurity", new Variant(3)); // 禁用宏
            Dispatch excels = ax.getProperty("Workbooks").toDispatch();

            Dispatch excel = Dispatch
                    .invoke(excels, "Open", Dispatch.Method,
                            new Object[] { inputFile, new Variant(false), new Variant(false) }, new int[9])
                    .toDispatch();
            File tofile = new File(pdfFile);
            if (tofile.exists()) {
                tofile.delete();
            }
            // 转换格式
            Dispatch.invoke(excel, "ExportAsFixedFormat", Dispatch.Method, new Object[] { new Variant(0), // PDF格式=0
                    pdfFile, new Variant(xlTypePDF) // 0=标准 (生成的PDF图片不会变模糊) 1=最小文件
                    // (生成的PDF图片糊的一塌糊涂)
            }, new int[1]);

            // 这里放弃使用SaveAs
            *//*
             * Dispatch.invoke(excel,"SaveAs",Dispatch.Method,new Object[]{
             * outFile, new Variant(57), new Variant(false), new Variant(57),
             * new Variant(57), new Variant(false), new Variant(true), new
             * Variant(57), new Variant(true), new Variant(true), new
             * Variant(true) },new int[1]);
             *//*
            long date2 = new Date().getTime();
            int time = (int) ((date2 - date) / 1000);
            Dispatch.call(excel, "Close", new Variant(false));

            if (ax != null) {
                ax.invoke("Quit", new Variant[] {});
                ax = null;
            }
            ComThread.Release();
            return time;
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            return -1;
        }
    }


    // public final static Sigar sigar = initSigar();

    private static ActiveXComponent initApp() {
        try {
            String file = Resources.getResource("jacob/jacob-1.18-M3-x64.dll").getFile();
            File classPath = new File(file).getParentFile();
            File dllPath = new File(file);

            String path = System.getProperty("java.library.path");
            if (isOSWin()) {
                path += ";" + classPath.getCanonicalPath();
            } else {
                path += ":" + classPath.getCanonicalPath();
            }
            //System.setProperty("java.library.path", path);
            addLibraryDir(path);
            System.out.println(System.getProperty("java.library.path"));
            System.load ("E:\\IdeaProjects\\MainTest\\target\\classes\\jacob\\jacob-1.18-M3-x64.dll");
            System.load ("E:\\IdeaProjects\\MainTest\\target\\classes\\sigar\\sigar-amd64-winnt.dll");
            ActiveXComponent  app = new ActiveXComponent("Powerpoint.Application");
            return app;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    private static void addLibraryDir(String libraryPath) throws Exception {
        Field userPathsField = ClassLoader.class.getDeclaredField("usr_paths");
        userPathsField.setAccessible(true);
        String[] paths = (String[]) userPathsField.get(null);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < paths.length; i++) {
            if (libraryPath.equals(paths[i])) {
                continue;
            }
            sb.append(paths[i]).append(';');
        }
        sb.append(libraryPath);
        System.setProperty("java.library.path", sb.toString());
        final Field sysPathsField = ClassLoader.class.getDeclaredField("sys_paths");
        sysPathsField.setAccessible(true);
        sysPathsField.set(null, null);
    }

    public static boolean isOSWin() {//OS 版本判断
        String OS = System.getProperty("os.name").toLowerCase();
        if (OS.indexOf("win") >= 0) {
            return true;
        } else return false;
    }


    private static void demo1() {
        // 1) Create options ODT 2 PDF to select well converter form the registry
        Options options = Options.getFrom(DocumentKind.DOCX).to(ConverterTypeTo.PDF);

// 2) Get the converter from the registry
        IConverter converter = ConverterRegistry.getRegistry().getConverter(options);

// 3) Convert ODT 2 PDF
        InputStream in = null;
        try {
            in = new FileInputStream(new File("E:\\ThinkWin\\威海office转码异常修改\\20180326 党委会WORD版\\议程00：20180326党委会议议程 修改_word.docx"));
            OutputStream out = new FileOutputStream(new File("E:\\ThinkWin\\威海office转码异常修改\\20180326 党委会WORD版\\11ODTHelloWord2PDF1.pdf"));
            converter.convert(in, out, options);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (XDocConverterException e) {
            e.printStackTrace();
        }
    }*/
}
