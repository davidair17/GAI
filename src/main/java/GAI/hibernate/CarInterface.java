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


public class CarInterface extends JFrame{
    protected static final Logger logger = LogManager.getLogger(CarInterface.class);
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

    public void deleteSQLCar(int id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Query query = session.createQuery("delete from Car where id = :id");
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
            super ("Автомобиль, номерной знак и владелец не указаны");
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

        JLabel nameLabel = new JLabel("Maintenance");
        JTextField nameField = new JTextField();
        formPanel.add(nameLabel);
        formPanel.add(nameField);

        JLabel lastLabel = new JLabel("Car_plate");
        JTextField lastField = new JTextField();
        formPanel.add(lastLabel);
        formPanel.add(lastField);

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
            String maintenance = nameField.getText();
            String carplate = lastField.getText();
            String str = String.valueOf(comboBox.getSelectedItem());
            int index = str.indexOf(" ");
            String id = str.substring(0, index);



            try{


                Session session1 = HibernateUtil.getSessionFactory().openSession();
                Transaction transaction = session1.beginTransaction();

                Car driver = new Car();

                driver.setCar_plate(carplate);
                driver.setMaintenance(LocalDate.parse(maintenance));
                Driver test = session1.get(Driver.class, Integer.valueOf(id));
                driver.setDriver(test);


                session1.save(driver);

                transaction.commit();
                session1.close();

                dialog.dispose();
            }
            catch (DateTimeParseException ex){
                JOptionPane.showMessageDialog(null, "Дату надо указать!");
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
        Car Car = session.get(Car.class, id);
        session.close();
        JDialog dialog = new JDialog(this, "Re-Edit", true);
        dialog.setLayout(new BorderLayout());

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(4, 2));

        JLabel nameLabel = new JLabel("Car plate:");
        JTextField nameField = new JTextField(Car.getCar_plate());
        formPanel.add(nameLabel);
        formPanel.add(nameField);

        JLabel dataLabel = new JLabel("Maintenance:");
        JTextField dataField = new JTextField(String.valueOf(Car.getMaintenance()));
        formPanel.add(dataLabel);
        formPanel.add(dataField);

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

            try {

                isalnum(nameField.getText());


                LocalDate.parse(dataField.getText());

                String name = nameField.getText();

                String data = dataField.getText();

                String str = String.valueOf(comboBox.getSelectedItem());
                int index = str.indexOf(" ");
                String dr = str.substring(0, index);


                Session session1 = HibernateUtil.getSessionFactory().openSession();
                Transaction transaction = session1.beginTransaction();

                Car Car1 = session1.get(Car.class, id);
                Car1.setCar_plate(name);
                Car1.setMaintenance(LocalDate.parse(data));
                Driver test = session1.get(Driver.class, Integer.valueOf(dr));
                Car1.setDriver(test);


                session1.save(Car1);

                session1.update(Car1);

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
            super ("Такого автомобиля не существует");
        }}




    public void toUpdate(){
        try {
            File file = new File("Car.txt");
            //создаем объект FileReader для объекта File
            FileReader fr = new FileReader(file);
            //создаем BufferedReader с существующего FileReader для построчного считывания
            BufferedReader reader = new BufferedReader(fr);
            int rows = model.getRowCount();
            for (int i = 0; i < rows; i++) model.removeRow(0); // Очистка таблицы
            String CarID;
            do {
                CarID = reader.readLine();
                if (CarID != null) {
                    String WI = reader.readLine();
                    String CI = reader.readLine();
                    String DR = reader.readLine();

                    model.addRow(new String[]{CarID, WI, CI, DR}); // Запись строки в таблицу
                }
            } while (CarID != null);
            reader.close();

        } catch (IOException ex) {
            ex.printStackTrace();
            logger.warn("Ошибка", ex);
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
    public void DataGet(){

        List<Car> sop = getCar();
        try (FileWriter writer = new FileWriter("Car.txt")) {
            for (int i = 0; i < sop.size(); i++) {
                writer.write(String.valueOf(sop.get(i).getIdCar()));
                writer.write("\n");
                writer.write(sop.get(i).getCar_plate());
                writer.write("\n");
                writer.write(String.valueOf(sop.get(i).getMaintenance()));
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

        logger.info("Открыта экранная форма Заказы");
// Создание окна
        FrameOrder = new JFrame("Cars");
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
        String[] columns = {"Car ID", "Car plate", "Maintenance", "Driver"};
        String[][] data = {{}};
        model = new EditableTableModel(data, columns);
        Posts = new JTable(model);
        scroll = new JScrollPane(Posts);

        DataGet();


// Размещение таблицы с данными
        FrameOrder.add(scroll, BorderLayout.CENTER);
// Подготовка компонентов поиска
        Mode = new JComboBox(new String[]{"Cars", "Drivers",
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
            JOptionPane.showMessageDialog(adding, "Add new Car?");
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
                    deleteSQLCar(por);
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
                    case ("Cars"):
                        break;
                    case ("Drivers"):
                        FrameOrder.dispose();
                        new DriverInterface().show();
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

    }
    public static void main(String[] args) {

// Создание и отображение экранной формы
        new CarInterface().show();
    }



}