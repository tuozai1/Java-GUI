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

public class MessageCompany extends JFrame implements ActionListener {
    JPanel jp1, jp2, jp3, jp4, jp5, jp6;
    JLabel companyIdLabel, companyIdLabel1, companyNameLabel, nameLabel, companyNameLabel1, addressLabel, keyLabel;
    JButton editButton, backButton;
    JTextField nameTextField, addressTextField, keyTextField;

    static Connection connection;
    static ResultSet rs;
    static PreparedStatement ps;

    String address;
    String key;
    String name;
    int company_id;

    public MessageCompany(int company_id) {
        this.company_id = company_id;

        try {
            Class.forName(driverName);

            connection = DriverManager.getConnection(url, username, password);

            if (connection == null) {
                System.out.println("连接失败");
            } else {
                System.out.println("连接成功");
            }

            ps = connection.prepareStatement("select company_name, company_address, company_key from companies where company_id = ?");
            ps.setInt(1, company_id);
            rs = ps.executeQuery();
            if (rs.next()) {
                name = rs.getString(1);
                address = rs.getString(2);
                key = rs.getString(3);
            }

            if (name == null) {
                name = "暂无";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        jp1 = new JPanel();
        jp2 = new JPanel();
        jp3 = new JPanel();
        jp4 = new JPanel();
        jp5 = new JPanel();
        jp6 = new JPanel();

        companyIdLabel = new JLabel("企业代号:");
        companyIdLabel1 = new JLabel(String.valueOf(company_id));
        companyNameLabel = new JLabel("企业名称:");
        nameLabel = new JLabel(name);
        companyNameLabel1 = new JLabel("企业名称");
        addressLabel = new JLabel("  地   址   ");
        keyLabel = new JLabel("  密   钥   ");

        // 设置字体、加粗和大小
        Font font = new Font("SansSerif", Font.BOLD, 24);
        companyIdLabel.setFont(font);
        companyIdLabel1.setFont(font);
        companyNameLabel.setFont(font);
        nameLabel.setFont(font);

        nameTextField = new JTextField(20);
        addressTextField = new JTextField(20);
        keyTextField = new JTextField(20);

        nameTextField.setText(name.equals("暂无") ? null : name);
        addressTextField.setText(address);
        keyTextField.setText(key);

        editButton = new JButton("修改");
        backButton = new JButton("返回");

        editButton.addActionListener(this);
        backButton.addActionListener(this);

        jp1.add(companyIdLabel);
        jp1.add(companyIdLabel1);

        jp2.add(companyNameLabel);
        jp2.add(nameLabel);

        jp3.add(companyNameLabel1);
        jp3.add(nameTextField);

        jp4.add(addressLabel);
        jp4.add(addressTextField);

        jp5.add(keyLabel);
        jp5.add(keyTextField);

        jp6.add(editButton);
        jp6.add(backButton);

        add(jp1);
        add(jp2);
        add(jp3);
        add(jp4);
        add(jp5);
        add(jp6);

        setLayout(new GridLayout(6, 1));
        setTitle("项目进度查询管理系统-企业信息修改");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("修改")) {
            try {
                name = nameTextField.getText();
                address = addressTextField.getText();
                key = keyTextField.getText();

                ps = connection.prepareStatement("update companies set company_name = ?, company_address = ?, company_key = ?" +
                        "where company_id = ?");
                ps.setString(1, name);
                ps.setString(2, address);
                ps.setString(3, key);
                ps.setInt(4, company_id);

                ps.executeUpdate();

                // 回显名称
                ps = connection.prepareStatement("select company_name from companies where company_id = ?;");
                ps.setInt(1, company_id);
                rs = ps.executeQuery();

                if (rs.next()) {
                    nameLabel.setText(name);
                }

                JOptionPane.showMessageDialog(null, "修改成功！", "提示消息", JOptionPane.WARNING_MESSAGE);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        } else {
            dispose();
            new CompanyEmployee(company_id);
        }
    }

    public static void main(String[] args) {
        new MessageCompany(1000);

    }
}