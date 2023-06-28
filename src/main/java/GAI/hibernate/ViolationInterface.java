package GAI.hibernate;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.awt.*;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.event.*;
import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;


public class ViolationInterface extends JFrame{
    protected static final Logger logger = LogManager.getLogger(ViolationInterface.class);
    private JFrame FrameOrder;
    private EditableTableModel model;
    private JButton adding;
    private JButton remove;
    private JToolBar toolBar;
    private JScrollPane scroll;
    private JTable Posts;
    private JComboBox Mode;
    private JTextField Searcher;



    private void filterTable() {
        String query = Searcher.getText();
        TableRowSorter<TableModel> sorter = new TableRowSorter<>(Posts.getModel());
        sorter.setRowFilter(RowFilter.regexFilter(query));
        Posts.setRowSorter(sorter);
    }

    public void deleteSQLViolation(int id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Query query = session.createQuery("delete from Violation where id = :id");
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
            super ("Тип нарушения, штраф и дата не указаны");
        }}
    private void isalnum(String str) throws IsalnumEx
    {
        if (str.equals("")) throw new IsalnumEx();
    }
    public List<Driver> getDriver() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Driver", Driver.class).list();
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ArrayList<>();
        }
    }
    private void getIdDriver(){
        List<Driver> sop = getDriver();
        try (FileWriter writer = new FileWriter("options.txt")) {
            for (int i = 0; i < sop.size(); i++) {
                writer.write(String.valueOf(sop.get(i)));
                writer.write("\n");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }





    private void showDialog() {


        JDialog dialog = new JDialog(this, "New", true);
        dialog.setLayout(new BorderLayout());

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(8, 2));

        JLabel nameLabel = new JLabel("Type");
        JTextField nameField = new JTextField();
        formPanel.add(nameLabel);
        formPanel.add(nameField);

        JLabel lastLabel = new JLabel("Penalty");
        JTextField lastField = new JTextField();
        formPanel.add(lastLabel);
        formPanel.add(lastField);

        JLabel dateLabel = new JLabel("Datetime");
        JTextField dateField = new JTextField();
        formPanel.add(dateLabel);
        formPanel.add(dateField);

        getIdDriver();

        List<String> optionsList = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("options.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                optionsList.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
            logger.warn("Ошибка", e);
        }

        String[] options = optionsList.toArray(new String[0]);
        JComboBox<String> comboBox = new JComboBox<>(options);
        comboBox.setSelectedIndex(0);



        formPanel.add(new JLabel("Select"));
        formPanel.add(comboBox);



        dialog.add(formPanel, BorderLayout.CENTER);

        JButton saveButton = new JButton("Save");

        saveButton.addActionListener(e -> {
            String type = nameField.getText();
            String penalty = lastField.getText();
            String datetime = dateField.getText();
            String str = String.valueOf(comboBox.getSelectedItem());
            int index = str.indexOf(" ");
            String id = str.substring(0, index);



            try{


                Session session1 = HibernateUtil.getSessionFactory().openSession();
                Transaction transaction = session1.beginTransaction();

                Violation driver = new Violation();

                driver.setType(type);
                driver.setPenalty(penalty);
                driver.setDate(LocalDate.parse(datetime));
                Driver test = session1.get(Driver.class, Integer.valueOf(id));
                driver.setDriver(test);


                session1.save(driver);

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
        Violation Violation = session.get(Violation.class, id);
        session.close();
        JDialog dialog = new JDialog(this, "Re-Edit", true);
        dialog.setLayout(new BorderLayout());

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(4, 2));

        JLabel nameLabel = new JLabel("Type:");
        JTextField nameField = new JTextField(Violation.getType());
        formPanel.add(nameLabel);
        formPanel.add(nameField);

        JLabel dataLabel = new JLabel("Penalty:");
        JTextField dataField = new JTextField(Violation.getPenalty());
        formPanel.add(dataLabel);
        formPanel.add(dataField);

        JLabel dateLabel = new JLabel("Date:");
        JTextField dateField = new JTextField(String.valueOf(Violation.getDate()));
        formPanel.add(dateLabel);
        formPanel.add(dateField);


        dialog.add(formPanel, BorderLayout.CENTER);

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {

            try {

                isalnum(nameField.getText());
                isalnum(dataField.getText());
                LocalDate.parse(dateField.getText());

                String type = nameField.getText();
                String datetime = dateField.getText();
                String penalty = dataField.getText();


                Session session1 = HibernateUtil.getSessionFactory().openSession();
                Transaction transaction = session1.beginTransaction();

                Violation Violation1 = session1.get(Violation.class, id);
                Violation1.setType(type);
                Violation1.setDate(LocalDate.parse(datetime));
                Violation1.setPenalty(penalty);

                session1.update(Violation1);

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
            super ("Такого нарушения нет");
        }}




    public void toUpdate(){
        try {
            File file = new File("Violation.txt");
            //создаем объект FileReader для объекта File
            FileReader fr = new FileReader(file);
            //создаем BufferedReader с существующего FileReader для построчного считывания
            BufferedReader reader = new BufferedReader(fr);
            int rows = model.getRowCount();
            for (int i = 0; i < rows; i++) model.removeRow(0); // Очистка таблицы
            String ViolationID;
            do {
                ViolationID = reader.readLine();
                if (ViolationID != null) {
                    String WI = reader.readLine();
                    String CI = reader.readLine();
                    String DR = reader.readLine();
                    String DT = reader.readLine();

                    model.addRow(new String[]{ViolationID, WI, DR, CI, DT}); // Запись строки в таблицу
                }
            } while (ViolationID != null);
            reader.close();

        } catch (IOException ex) {
            ex.printStackTrace();
            logger.warn("Ошибка", ex);
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

        List<Violation> sop = getViolation();
        try (FileWriter writer = new FileWriter("Violation.txt")) {
            for (int i = 0; i < sop.size(); i++) {
                writer.write(String.valueOf(sop.get(i).getIdViolation()));
                writer.write("\n");
                writer.write(sop.get(i).getType());
                writer.write("\n");
                writer.write(String.valueOf(sop.get(i).getDate()));
                writer.write("\n");
                writer.write(sop.get(i).getPenalty());
                writer.write("\n");
                writer.write(String.valueOf(sop.get(i).getDriver()));
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

        logger.info("Открыта экранная форма Нарушения");
// Создание окна
        FrameOrder = new JFrame("Violations");
        FrameOrder.setSize(1024, 680);
        FrameOrder.setLocation(100, 100);
        FrameOrder.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
// Создание кнопок и прикрепление иконок
        adding = new JButton(new ImageIcon("./img/add.png"));
        remove = new JButton(new ImageIcon("./img/remove.png"));
// Настройка подсказок для кнопок
        adding.setToolTipText("Add");
        remove.setToolTipText("Delete");
// Добавление кнопок на панель инструментов
        toolBar = new JToolBar("Панель инструментов");
        toolBar.add(adding);
        toolBar.add(remove);


// Размещение панели инструментов
        FrameOrder.setLayout(new BorderLayout());
        FrameOrder.add(toolBar, BorderLayout.NORTH);
// Создание таблицы с данными
        String[] columns = {"Violation ID", "Type", "Penalty", "Date", "Driver"};
        String[][] data = {{}};
        model = new EditableTableModel(data, columns);
        Posts = new JTable(model);
        scroll = new JScrollPane(Posts);

        DataGet();


// Размещение таблицы с данными
        FrameOrder.add(scroll, BorderLayout.CENTER);
// Подготовка компонентов поиска
        Mode = new JComboBox(new String[]{"Violations", "Drivers",
                "Cars"});
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
            JOptionPane.showMessageDialog(adding, "Add new Violation?");
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

            logger.debug(e);
            int flag = 0;
            try {
                Removebutton(Posts);
            } catch (NullPointerException ex) {
                JOptionPane.showMessageDialog(FrameOrder, ex.toString());
                logger.warn("Ошибка", ex);
                flag = 1;
            }
            if (flag == 0) {
                int del = Posts.getSelectedRow();
                int por = Integer.parseInt((String) Posts.getValueAt(del, 0));
                deleteSQLViolation(por);
                model.removeRow(Posts.getSelectedRow());
                JOptionPane.showMessageDialog(remove, "Row Removed!");
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
                    case ("Violations"):
                        break;
                    case ("Drivers"):
                        FrameOrder.dispose();
                        new DriverInterface().show();
                        break;
                    case ("Cars"):
                        FrameOrder.dispose();
                        new CarInterface().show();
                        break;
                    default:
                        break;
                }
            }
        });

    }
    public static void main(String[] args) {

// Создание и отображение экранной формы
        new ViolationInterface().show();
    }


}

