package OfficeConverter;

import com.aspose.words.Document;
import com.aspose.words.SaveFormat;
import com.yeokhengmeng.docstopdfconverter.DocToPDFConverter;
import com.yeokhengmeng.docstopdfconverter.DocxToPDFConverter;
import fr.opensagres.poi.xwpf.converter.pdf.PdfOptions;
import fr.opensagres.xdocreport.converter.*;
import fr.opensagres.xdocreport.core.document.DocumentKind;

import java.io.*;

public class ConverterMain {


    public static void main(String[] args) {

        String dir = "E:\\ThinkWin\\";
// load the file to be converted
        Document document = null;
        try {
            document = new Document(dir + "test.docx");
// save in different formats
            document.save(dir + "output1.pdf", SaveFormat.PDF);
            document.save(dir + "output1.html", SaveFormat.HTML);
        } catch (Exception e) {
            e.printStackTrace();
        }

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
