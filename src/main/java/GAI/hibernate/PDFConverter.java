package GAI.hibernate;

import org.apache.fop.apps.*;

import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Формирование отчета в PDF
 */
public class PDFConverter {
    /**
     *
     * @param pdfname Путь к PDF
     */
    public static void ShowPDF(String pdfname) {

        try {

            File pdfFile = new File(pdfname);
            if (pdfFile.exists()) {
                if (Desktop.isDesktopSupported()) {
                    Desktop.getDesktop().open(pdfFile);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    /**
     *
     * @param XSL Шаблон
     * @param XML Данные
     * @param PDF Куда формировать
     * @throws IOException
     * @throws FOPException
     * @throws TransformerException
     */
    public static void convertToPDF(String XSL, String XML, String PDF)  throws IOException, FOPException, TransformerException {
        //Using Apache FOP to build PDF from XML, using XSL-FO, styled file

        File xsltFile = new File(XSL);
        StreamSource xmlSource = new StreamSource(new File(XML));
        FopFactory fopFactory = FopFactory.newInstance(new File(".").toURI());
        FOUserAgent foUserAgent = fopFactory.newFOUserAgent();
        OutputStream out;
        out = new java.io.FileOutputStream(PDF);

        try {
            Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, foUserAgent, out);

            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer(new StreamSource(xsltFile));

            Result res = new SAXResult(fop.getDefaultHandler());
            System.out.println(xmlSource);
            System.out.println(res);
            transformer.transform(xmlSource, res);
        } finally {
            out.close();
        }
    }
}
