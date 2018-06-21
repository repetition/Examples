package OfficeConverter;

import com.aspose.words.Document;
import com.aspose.words.SaveFormat;
import com.google.common.io.Resources;
import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;
import fr.opensagres.xdocreport.converter.*;
import fr.opensagres.xdocreport.core.document.DocumentKind;
import org.hyperic.sigar.Sigar;

import java.io.*;
import java.lang.reflect.Field;

public class ConverterMain {
    static final int wdDoNotSaveChanges = 0;// 不保存待定的更改。
    static final int wdFormatPDF = 17;// word转PDF 格式

    public static void main(String[] args) {
/*        String dir = "E:\\ThinkWin\\";
// load the file to be converted
        Document document = null;
        try {
            document = new Document(dir + "test.docx");
// save in different formats
            document.save(dir + "output3.pdf", SaveFormat.PDF);
            document.save(dir + "output3.html", SaveFormat.HTML);
        } catch (Exception e) {
            e.printStackTrace();
        }*/

        initApp();
        String source = "E:\\ThinkWin\\威海office转码异常修改\\20180326 党委会WORD版\\议程00：20180326党委会议议程 修改.docx";
        String target = "E:\\ThinkWin\\威海office转码异常修改\\20180326 党委会WORD版\\议程00：20180326党委会议议程 修改1111.pdf";
        word2pdf(source,target);

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
            Dispatch doc = Dispatch.call(docs, "Open", source, false, true).toDispatch();
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
            System.out.println("Word转PDF出错：" + e.getMessage());
            return false;
        } finally {
            if (app != null) {
                app.invoke("Quit", wdDoNotSaveChanges);
            }
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
            ActiveXComponent  app = new ActiveXComponent("Word.Application");
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
    }
}
