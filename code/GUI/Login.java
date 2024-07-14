package code.GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Login extends JFrame implements ActionListener {

    JPanel jp1, jp2, jp3;
    JLabel titleLabel;
    JButton teacherButton, studentButton;

    public Login() {
        jp1 = new JPanel();
        jp2 = new JPanel();
        jp3 = new JPanel();

        titleLabel = new JLabel("请选择登录端");
        Font font = new Font("SansSerif", Font.BOLD, 24);
        titleLabel.setFont(font);

        teacherButton = new JButton("企业端");
        studentButton = new JButton("员工端");

        teacherButton.addActionListener(this);
        studentButton.addActionListener(this);

        jp1.add(titleLabel);

        jp2.add(teacherButton);
        jp3.add(studentButton);

        add(jp1);
        add(jp2);
        add(jp3);

        setLayout(new GridLayout(3, 1));
        setTitle("项目进度查询管理系统-登录页面");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("企业端")) {
            dispose();
            new LoginCompany();
        } else {
            dispose();
            new LoginEmployee();
        }
    }

    public static void main(String[] args) {
        new Login();
    }
}
