package GAI.hibernate;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SecondFrame {
    private JFrame frame = new JFrame("Тест");
    private JButton test = new JButton("Тест");
    private JLabel text = new JLabel("       ");
    private JLabel text1 = new JLabel("       ");
    private JLabel text2 = new JLabel("       ");
    private JLabel text3 = new JLabel("       ");

    public SecondFrame() {
        JPanel filterPanel = new JPanel();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(512,256);
        frame.setVisible(true);
        filterPanel.add(test);
        filterPanel.add(text);
        frame.add(text, BorderLayout.WEST);
        frame.add(text1, BorderLayout.EAST);
        frame.add(text2, BorderLayout.SOUTH);
        frame.add(text3, BorderLayout.NORTH);
        frame.add(test, BorderLayout.CENTER);

        test.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog (test, "Проверка нажатия на кнопку");

            }
        });
    }
}