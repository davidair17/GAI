package GAI.hibernate;

import org.apache.fop.apps.FOPException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.awt.*;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.awt.event.*;
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;


public class DriverInterface extends JFrame{
    protected static final Logger logger = LogManager.getLogger(DriverInterface.class);
    private JFrame FrameOrder;
    private EditableTableModel model;
    private JButton adding;
    private JButton remove;
    private JToolBar toolBar;
    private JScrollPane scroll;
    private JTable Posts;
    private JComboBox Mode;
    private JTextField Searcher;
    private JButton Report;



    private void filterTable() {
        String query = Searcher.getText();
        TableRowSorter<TableModel> sorter = new TableRowSorter<>(Posts.getModel());
        sorter.setRowFilter(RowFilter.regexFilter(query));
        Posts.setRowSorter(sorter);
    }

    public void deleteSQLDriver(int id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Query query = session.createQuery("delete from Driver where id = :id");
            query.setParameter("id", id);
            query.executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            logger.warn("Ошибка", e);
        }
    }


    static class IsalnumEx extends Exception {
        public IsalnumEx() {
            super ("ФИО и дата рождения не указаны");
        }}
    private void isalnum(String str) throws IsalnumEx
    {
        if (str.equals("")) throw new IsalnumEx();
    }





    private void showDialog() {


        JDialog dialog = new JDialog(this, "New", true);
        dialog.setLayout(new BorderLayout());

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(8, 2));

        JLabel nameLabel = new JLabel("Name");
        JTextField nameField = new JTextField();
        formPanel.add(nameLabel);
        formPanel.add(nameField);

        JLabel lastLabel = new JLabel("Dateofbirth");
        JTextField lastField = new JTextField();
        formPanel.add(lastLabel);
        formPanel.add(lastField);



        dialog.add(formPanel, BorderLayout.CENTER);

        JButton saveButton = new JButton("Save");

        saveButton.addActionListener(e -> {
            String name = nameField.getText();
            String dateofbirth = lastField.getText();



            try{


                Session session1 = HibernateUtil.getSessionFactory().openSession();
                Transaction transaction = session1.beginTransaction();

                Driver order = new Driver();

                order.setName(name);
                order.setDateofbirth(LocalDate.parse(dateofbirth));


                session1.save(order);

                transaction.commit();
                session1.close();

                dialog.dispose();
            }
            catch (DateTimeParseException ex){
                JOptionPane.showMessageDialog(null, "Укажите дату");
                logger.warn("Ошибка", ex);
            }




        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(saveButton);

        dialog.add(buttonPanel, BorderLayout.SOUTH);

        dialog.setPreferredSize(new Dimension(400, 300));
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);



    }

    private void EditDialog(int id) {

        Session session = HibernateUtil.getSessionFactory().openSession();
        Driver Driver = session.get(Driver.class, id);
        session.close();
        JDialog dialog = new JDialog(this, "Re-Edit", true);
        dialog.setLayout(new BorderLayout());

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(4, 2));

        JLabel nameLabel = new JLabel("Name:");
        JTextField nameField = new JTextField(Driver.getName());
        formPanel.add(nameLabel);
        formPanel.add(nameField);

        JLabel dataLabel = new JLabel("Dateofbirth:");
        JTextField dataField = new JTextField(String.valueOf(Driver.getDateofbirth()));
        formPanel.add(dataLabel);
        formPanel.add(dataField);


        dialog.add(formPanel, BorderLayout.CENTER);

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {

            try {

                isalnum(nameField.getText());


                LocalDate.parse(dataField.getText());

                String name = nameField.getText();

                String data = dataField.getText();


                Session session1 = HibernateUtil.getSessionFactory().openSession();
                Transaction transaction = session1.beginTransaction();

                Driver Driver1 = session1.get(Driver.class, id);
                Driver1.setName(name);
                Driver1.setDateofbirth(LocalDate.parse(data));

                session1.update(Driver1);

                transaction.commit();
                session1.close();


                dialog.dispose();
            }catch (DateTimeParseException ex1){
                JOptionPane.showMessageDialog(null, "Укажите дату");
                logger.warn("Ошибка", ex1);
            } catch (IsalnumEx ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage());
                logger.warn("Ошибка", ex);
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(saveButton);

        dialog.add(buttonPanel, BorderLayout.SOUTH);

        dialog.setPreferredSize(new Dimension(400, 150));
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }


    class MyException extends Exception {
        public MyException() {
            super ("Такого водителя не существует");
        }}




    public void toUpdate(){
        try {
            File file = new File("Driver.txt");
            //создаем объект FileReader для объекта File
            FileReader fr = new FileReader(file);
            //создаем BufferedReader с существующего FileReader для построчного считывания
            BufferedReader reader = new BufferedReader(fr);
            int rows = model.getRowCount();
            for (int i = 0; i < rows; i++) model.removeRow(0); // Очистка таблицы
            String DriverID;
            do {
                DriverID = reader.readLine();
                if (DriverID != null) {
                    String WI = reader.readLine();
                    String CI = reader.readLine();

                    model.addRow(new String[]{DriverID, WI, CI}); // Запись строки в таблицу
                }
            } while (DriverID != null);
            reader.close();

        } catch (IOException ex) {
            ex.printStackTrace();
            logger.warn("Ошибка", ex);
        }
    }

    public List<Driver> getDriver() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Driver", Driver.class).list();
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ArrayList<>();
        }
    }
    public List<Car> getCar() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Car", Car.class).list();
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ArrayList<>();
        }
    }
    public List<Violation> getViolation() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Violation", Violation.class).list();
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ArrayList<>();
        }
    }
    public void DataGet(){

        List<Driver> sop = getDriver();
        try (FileWriter writer = new FileWriter("Driver.txt")) {
            for (int i = 0; i < sop.size(); i++) {
                writer.write(String.valueOf(sop.get(i).getIdDriver()));
                writer.write("\n");
                writer.write(sop.get(i).getName());
                writer.write("\n");
                writer.write(String.valueOf(sop.get(i).getDateofbirth()));
                writer.write("\n");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        toUpdate();
    }
    private void Removebutton(JTable Posts) throws NullPointerException{
        if (Posts.getSelectedRow() == -1) throw new NullPointerException();
    }
    public void show() {

        logger.info("Открыта экранная форма Водители");
// Создание окна
        FrameOrder = new JFrame("Drivers");
        FrameOrder.setSize(1024, 680);
        FrameOrder.setLocation(100, 100);
        FrameOrder.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
// Создание кнопок и прикрепление иконок
        adding = new JButton(new ImageIcon("./img/add.png"));
        remove = new JButton(new ImageIcon("./img/remove.png"));
        Report = new JButton(new ImageIcon("./img/pdf.png"));
// Настройка подсказок для кнопок
        adding.setToolTipText("Add");
        remove.setToolTipText("Delete");
        Report.setToolTipText("Report");
// Добавление кнопок на панель инструментов
        toolBar = new JToolBar("Панель инструментов");
        toolBar.add(adding);
        toolBar.add(remove);
        toolBar.add(Report);


// Размещение панели инструментов
        FrameOrder.setLayout(new BorderLayout());
        FrameOrder.add(toolBar, BorderLayout.NORTH);
// Создание таблицы с данными
        String[] columns = {"Driver ID", "Name", "Date Of Birth"};
        String[][] data = {{}};
        model = new EditableTableModel(data, columns);
        Posts = new JTable(model);
        scroll = new JScrollPane(Posts);

        DataGet();


// Размещение таблицы с данными
        FrameOrder.add(scroll, BorderLayout.CENTER);
// Подготовка компонентов поиска
        Mode = new JComboBox(new String[]{"Drivers", "Cars",
                "Violations"});
        Searcher = new JTextField("Search", 15);
// Добавление компонентов на панель
        JPanel filterPanel = new JPanel();
        JPanel ModePanel = new JPanel();
        ModePanel.add(Mode);
        filterPanel.add(Searcher);
// Размещение панели поиска внизу окна
        FrameOrder.add(filterPanel, BorderLayout.SOUTH);
        FrameOrder.add(ModePanel, BorderLayout.EAST);
// Визуализация экранной формы
        FrameOrder.setVisible(true);


        adding.addActionListener(e -> {
            logger.debug(e);
            JOptionPane.showMessageDialog(adding, "Add new Driver?");
            showDialog();
            /*loadDataFromFile("file.txt");*/
            DataGet();
        });



        Searcher.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                filterTable();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                filterTable();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                filterTable();
            }
        });

        remove.addActionListener(e -> {
            int result = JOptionPane.showConfirmDialog(null, "Вы уверены?", "Подтверждение", JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION) {
                // Действия при нажатии на кнопку "Да"

                logger.debug(e);
                int flag = 0;
                try {
                    Removebutton(Posts);
                } catch (NullPointerException ex) {
                    JOptionPane.showMessageDialog(FrameOrder, ex.toString());
                    flag = 1;
                }
                if (flag == 0) {
                    int del = Posts.getSelectedRow();
                    int por = Integer.parseInt((String) Posts.getValueAt(del, 0));
                    deleteSQLDriver(por);
                    model.removeRow(Posts.getSelectedRow());
                    JOptionPane.showMessageDialog(remove, "Row Removed!");
                }
            }
        });

        Posts.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                logger.debug(e);
                if (e.getClickCount() == 2) {
                    int del = Posts.getSelectedRow();
                    int por = Integer.parseInt((String) Posts.getValueAt(del, 0));
                    EditDialog(por);
                    DataGet();

                }
            }
        });

        Mode.addActionListener(e -> {
            logger.debug(e);
            String s = (String) Mode.getSelectedItem();

            if (s != null) {
                switch (s) {
                    case ("Drivers"):
                        break;
                    case ("Cars"):
                        FrameOrder.dispose();
                        new CarInterface().show();
                        break;
                    case ("Violations"):
                        FrameOrder.dispose();
                        new ViolationInterface().show();
                        break;
                    default:
                        break;
                }
            }
        });
        Report.addActionListener(e -> {
            logger.debug(e);
            try {

// Создание парсера документа
                DocumentBuilder builder =
                        DocumentBuilderFactory.newInstance().newDocumentBuilder();
// Создание пустого документа
                Document doc = builder.newDocument();
                Node root = doc.createElement("DriverReport");
                doc.appendChild(root);

                List<Driver> sop = getDriver();
                List<Car> sop1 = getCar();
                List<Violation> sop2 = getViolation();
                for (int i = 0; i < sop.size(); i++) {
                    String ok = String.valueOf(sop.get(i).getIdDriver());
                    int flag = 0;
                    if (ok.equals("")){
                        flag = 1;
                    }

                    if (flag == 0) {
                        Node Driver = doc.createElement("Driver");
                        root.appendChild(Driver);

                        Element ID = doc.createElement("ID");
                        ID.setTextContent(ok);
                        Driver.appendChild(ID);

                        Element Name = doc.createElement("Name");
                        Name.setTextContent(sop.get(i).getName());
                        Driver.appendChild(Name);}

                    String ok2 = String.valueOf(sop1.get(i).getIdCar());
                    int flag2 = 0;
                    if (ok2.equals("")){
                        flag2 = 1;
                    }
                    if (flag2 == 0) {
                        Node Car = doc.createElement("Car");
                        root.appendChild(Car);

                        Element Carplate = doc.createElement("Car_plate");
                        Carplate.setTextContent(sop1.get(i).getCar_plate());
                        Car.appendChild(Carplate);
                        Element Maintenance = doc.createElement("Maintenance");
                        Maintenance.setTextContent(String.valueOf(sop1.get(i).getMaintenance()));
                        Car.appendChild(Maintenance);
                    }
                    String ok3 = String.valueOf(sop2.get(i).getIdViolation());
                    int flag3 = 0;
                    if (ok3.equals("")){
                        flag3 = 1;
                    }
                    if (flag3 == 0) {
                        Node DA = doc.createElement("Violations");
                        root.appendChild(DA);

                        Element Violation = doc.createElement("Violation");
                        Violation.setTextContent(sop2.get(i).getType());
                        DA.appendChild(Violation);
                    }

                }
// Создание дочерних элементов Order и присвоение значений атрибутам

                try {
// Создание преобразователя документа
                    Transformer trans = TransformerFactory.newInstance().newTransformer();
// Создание файла с именем Orders.xml для записи документа
                    FileWriter fw = new FileWriter("DriverReport.xml");
// Запись документа в файл
                    trans.transform(new DOMSource(doc), new StreamResult(fw));

                    FOPPdfDemo.convertToPDF("ReportTemp.xsl", "DriverReport.xml", "Report.pdf");
                    FOPPdfDemo.main("Report.pdf");


                } catch (TransformerException ex) {
                    ex.printStackTrace();
                } catch (IOException | FOPException ex) {
                    throw new RuntimeException(ex);

                }
            } catch (ParserConfigurationException ex) {
                ex.printStackTrace();
            }

        });

    }
    public static void main(String[] args) {

// Создание и отображение экранной формы
        new DriverInterface().show();
    }


}

