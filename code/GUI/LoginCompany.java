package code.GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static code.jdbc.Common.*;
import static code.jdbc.Common.password;
import static code.utils.MD5.decrypt;

public class LoginCompany extends JFrame implements ActionListener {
    JPanel jp1, jp2, jp3, jp4;
    JLabel companyIdLabel, copwdLabel;
    JButton loginButton, registerButton, resetButton, quitButton;
    JTextField companyIdTextField;
    JPasswordField pwdField;

    static Connection connection;

    static ResultSet rs;
    static PreparedStatement ps;

    String pwd;

    public LoginCompany() {
        linkDB();

        jp1 = new JPanel();
        jp2 = new JPanel();
        jp3 = new JPanel();
        jp4 = new JPanel();

        companyIdLabel = new JLabel("企业代号");
        copwdLabel = new JLabel("  密   码   ");

        companyIdTextField = new JTextField(10);

        pwdField = new JPasswordField(10);

        loginButton = new JButton("登录");
        registerButton = new JButton("注册");
        resetButton = new JButton("重置");
        quitButton = new JButton("退出");

        loginButton.addActionListener(this);
        registerButton.addActionListener(this);
        resetButton.addActionListener(this);
        quitButton.addActionListener(this);

        jp1.add(companyIdLabel);
        jp1.add(companyIdTextField);

        jp2.add(copwdLabel);
        jp2.add(pwdField);

        jp3.add(loginButton);
        jp3.add(registerButton);

        jp4.add(resetButton);
        jp4.add(quitButton);

        add(jp1);
        add(jp2);
        add(jp3);
        add(jp4);

        setLayout(new GridLayout(4, 1));
        setTitle("项目进度查询管理系统-企业登录");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private static void linkDB() {
        try {
            connection = DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void clear() {
        companyIdTextField.setText("");
        pwdField.setText("");
    }

    public void checkCompany() {
        try {
            if (companyIdTextField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "请输入企业代号！", "提示消息", JOptionPane.WARNING_MESSAGE);
                return;
            }

            ps = connection.prepareStatement("select company_pwd from companies where company_id = ?");
            ps.setInt(1, Integer.parseInt(companyIdTextField.getText()));
            rs = ps.executeQuery();
            if (!rs.next()) {
                JOptionPane.showMessageDialog(null, "没有该企业！", "提示消息", JOptionPane.WARNING_MESSAGE);
                return;
            }

            rs = ps.executeQuery();

            if (rs.next()) {
                pwd = rs.getString(1);
                loginCompany(Integer.parseInt(companyIdTextField.getText()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loginCompany(int company_id) {
        if(decrypt(String.valueOf(pwdField.getPassword()), pwd)) {
            JOptionPane.showMessageDialog(null, "登录成功", "提示信息", JOptionPane.WARNING_MESSAGE);
            clear();
            dispose();

            new CompanyEmployee(company_id);

        } else if (String.valueOf(pwdField.getPassword()).isEmpty()) {
            JOptionPane.showMessageDialog(null, "请输入密码！", "提示信息", JOptionPane.WARNING_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "密码错误！\n请重新输入", "提示信息", JOptionPane.ERROR_MESSAGE);
            clear();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("重置")) {
            clear();
        } else if (e.getActionCommand().equals("退出")) {
            System.exit(0);
        } else if (e.getActionCommand().equals("登录")) {
            checkCompany();
        } else {
            dispose();
            new RegisterCompany();
        }
    }

    public static void main(String[] args) {
        new LoginCompany();
    }
}
