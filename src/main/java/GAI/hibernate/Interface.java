package GAI.hibernate;

import org.apache.fop.apps.*;
import org.w3c.dom.*;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.event.*;
import java.io.*;
import org.slf4j.*;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.File;

public class Interface {
    private int mode;
    // Объявления графических компонентов
    private JFrame FrameGAI;
    private DefaultTableModel Model;
    private JButton Add;
    private JButton Remove;
    private JToolBar ToolBar;
    private JScrollPane Scroll;
    private JTable Posts;
    private JComboBox Mode;
    private JTextField Searcher;
    private JButton Filter;
    private JButton Save;
    private JButton Upload;

    private static final Logger log = LoggerFactory.getLogger("Interface");

    public void convertToPDF() throws IOException, FOPException, TransformerException {
        // the XSL FO file
        File xsltFile = new File("ReportTemp.xsl");
        // the XML file which provides the input
        StreamSource xmlSource = new StreamSource(new File("DriverReport.xml"));
        // create an instance of fop factory
        FopFactory fopFactory = FopFactory.newInstance(new File(".").toURI());
        // a user agent is needed for transformation
        FOUserAgent foUserAgent = fopFactory.newFOUserAgent();
        // Setup output
        OutputStream out;
        out = new java.io.FileOutputStream("Report.pdf");

        try {
            // Construct fop with desired output format
            Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, foUserAgent, out);

            // Setup XSLT
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer(new StreamSource(xsltFile));

            // Resulting SAX events (the generated FO) must be piped through to FOP
            Result res = new SAXResult(fop.getDefaultHandler());

            // Start XSLT transformation and FOP processing
            // That's where the XML is first transformed to XSL-FO and then
            // PDF is created
            transformer.transform(xmlSource, res);
        } finally {
            out.close();
        }
    }

    class MyException extends Exception {
        public MyException() {
            super ("Данные отсуствуют");
        }
    }
    private void checkName(JTextField Searcher) throws MyException,NullPointerException
    {
        String sName = Searcher.getText();
        if (sName.contains("Тест")) throw new MyException();
        if (sName.length() == 0) throw new NullPointerException();
    }
    private void Removebutton(JTable Posts) throws NullPointerException{
        if (Posts.getSelectedRow() == -1) throw new NullPointerException();
    }
    public void show() {
// Создание окна
        FrameGAI = new JFrame("Нарушения");
        FrameGAI.setSize(1024, 680);
        FrameGAI.setLocation(100, 100);
        FrameGAI.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
// Создание кнопок и прикрепление иконок
        Add = new JButton(new ImageIcon("./img/add.png"));
        Save = new JButton(new ImageIcon("./img/save.png"));
        Remove = new JButton(new ImageIcon("./img/remove.png"));
        Upload = new JButton(new ImageIcon("./img/upload.png"));

// Настройка подсказок для кнопок
        Add.setToolTipText("Добавить");
        Save.setToolTipText("Сохранить");
        Remove.setToolTipText("Удалить");
        Upload.setToolTipText("Загрузить");
// Добавление кнопок на панель инструментов
        ToolBar = new JToolBar("Панель инструментов");
        ToolBar.add(Add);
        ToolBar.add(Remove);
        ToolBar.add(Save);
        ToolBar.add(Upload);

// Размещение панели инструментов
        FrameGAI.setLayout(new BorderLayout());
        FrameGAI.add(ToolBar, BorderLayout.NORTH);
// Создание таблицы с данными
        String [] columns = {"Номер нарушения", "Тип", "Штраф", "Дата"};
        String [][] data = {{"1", "Speeding", "500 rub.", "13-03-2019 15:04:21,985673"},
                {"2", "Intersection of a double solid", "Deprivation of a driver's license", "19-09-2019 21:05:56,123456"}};
        Model= new DefaultTableModel(data, columns);
        Posts = new JTable(Model);
        Scroll = new JScrollPane(Posts);
// Размещение таблицы с данными
        FrameGAI.add(Scroll, BorderLayout.CENTER);
// Подготовка компонентов поиска
        Mode = new JComboBox(new String[]{"Нарушения", "Автомобили", "Водители"});
        Searcher = new JTextField("Поиск");
        Filter = new JButton("Тест");
// Добавление компонентов на панель
        JPanel filterPanel = new JPanel();
        filterPanel.add(Mode);
        filterPanel.add(Searcher);
        filterPanel.add(Filter);
// Размещение панели поиска внизу окна
        FrameGAI.add(filterPanel, BorderLayout.EAST);
// Визуализация экранной формы
        FrameGAI.setVisible(true);

        Add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                log.debug("Добавление строки");
                JOptionPane.showMessageDialog(Add, "Добавить новый штраф?");
                Model.addRow(new Object[]{"3", "Вождение ТС в нетрезвом виде", "30000 руб. + лишение ВУ", "04-11-2019 06:07:38,748326"} );
                log.info("Строка добавлена");
            }
        });

        Save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    log.debug("Сохранение файла");
// Создание парсера документа
                    DocumentBuilder builder =
                            DocumentBuilderFactory.newInstance().newDocumentBuilder();
// Создание пустого документа
                    Document doc = builder.newDocument();
                    Node OrderReport = doc.createElement("DriverReport");
                    doc.appendChild(OrderReport);

                    for (int i = 0; i < Model.getRowCount(); i++){
                        Node Driver = doc.createElement("Driver");
                        OrderReport.appendChild(Driver);
                        Element ID = doc.createElement("ID");
                        ID.setTextContent((String)Model.getValueAt(i, 0));
                        Driver.appendChild(ID);
                        Element Name = doc.createElement("Name");
                        Name.setTextContent((String)Model.getValueAt(i, 1));
                        Driver.appendChild(Name);
                        Element Violation = doc.createElement("Violation");
                        Violation.setTextContent((String)Model.getValueAt(i, 2));
                        Driver.appendChild(Violation);
                    }
// Создание дочерних элементов Driver и присвоение значений атрибутам

                    try {
// Создание преобразователя документа
                        Transformer trans = TransformerFactory.newInstance().newTransformer();
// Создание файла с именем Orders.xml для записи документа
                        java.io.FileWriter fw = new FileWriter("DriverReport.xml");
// Запись документа в файл
                        trans.transform(new DOMSource(doc), new StreamResult(fw));

                        convertToPDF();
                        log.info("Файл сохранен");



                    }
                    catch (TransformerConfigurationException ex) { ex.printStackTrace(); log.warn("Файл не сохранился " + ex); }
                    catch (TransformerException ex) { ex.printStackTrace(); log.warn("Файл не сохранился " + ex);}
                    catch (IOException ex) {
                        log.warn("Файл не сохранился" + ex); throw new RuntimeException(ex);
                    }
                    catch (FOPException ex) {
                        log.warn("Файл не сохранился" + ex); throw new RuntimeException(ex);
                    }
                } catch (ParserConfigurationException ex) { ex.printStackTrace(); log.warn("Файл не сохранился " + ex); }

            }

        });
        Upload.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try {
                    log.debug("Вывод информации из файла");
                    File file = new File("src\\main\\saves\\save.txt");
                    //создаем объект FileReader для объекта File
                    FileReader fr = new FileReader(file);
                    //создаем BufferedReader с существующего FileReader для построчного считывания
                    BufferedReader reader = new BufferedReader(fr);
                    int rows = Model.getRowCount();
                    for (int i = 0; i < rows; i++) Model.removeRow(0); // Очистка таблицы
                    String OrderID;
                    do {
                        OrderID = reader.readLine();
                        if(OrderID != null)
                        {
                            String WI = reader.readLine();
                            String CI = reader.readLine();
                            String CrI = reader.readLine();
                            Model.addRow(new String[]{OrderID, WI, CI, CrI}); // Запись строки в таблицу

                        }

                    } while(OrderID != null);
                    reader.close(); log.info("Файл выгружен");
                } catch (FileNotFoundException ex) {
                    ex.printStackTrace(); log.warn("Файл не выгружен " + ex);
                } catch (IOException ex) {
                    ex.printStackTrace(); log.warn("Файл не выгружен " + ex);
                }


            }
        });

        Filter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try { log.debug("Поиск по заданным данным"); checkName(Searcher);
                }
                catch(NullPointerException ex) {
                    JOptionPane.showMessageDialog(FrameGAI, ex.toString()); log.warn("Пустой поисковой запрос " + ex);
                }
                catch(MyException myEx) {
                    JOptionPane.showMessageDialog(null, myEx.getMessage()); log.warn("Поиск удался " + myEx);
                }
            }});
        Remove.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int flag = 0;
                try {
                    log.debug("Удаление строки");
                    Removebutton(Posts);
                }
                catch (NullPointerException ex){
                    JOptionPane.showMessageDialog(FrameGAI, ex.toString());
                    flag = 1; log.warn("Строка не удалена " + ex);
                }
                if (flag == 0){
                    Model.removeRow(Posts.getSelectedRow());
                    JOptionPane.showMessageDialog(Remove, "Строка удалена");
                    log.info("Строка удалена");
                }
            }
        });
    }



    public static void main(String[] args) {

// Создание и отображение экранной формы
        new Interface().show();
    }
}
