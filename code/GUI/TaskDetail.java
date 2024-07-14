package code.GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

import static code.jdbc.Common.*;

public class TaskDetail extends JFrame implements ActionListener {
    JPanel jp1, jp2, jp3, jp4;
    JLabel taskNameLabel, nameLabel, processLabel, jl4, descLabel;
    JButton editButton, backButton;
    JTextField processTextField;
    JTextArea jta;
    JScrollPane jsp;
    static Connection connection;
    static ResultSet rs;
    static PreparedStatement ps;
    String task_name;
    String progress;
    String description;
    String employee_name;
    int identity;

    public TaskDetail(String task_name, String employee_name, int identity) {
        this.task_name = task_name;
        this.employee_name = employee_name;
        this.identity = identity;

        if (identity == 0) {
            setTitle("项目进度查询管理系统-员工项目信息（员工端）");
        } else {
            setTitle("项目进度查询管理系统-员工项目信息（企业端）");
        }
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);

        jp1 = new JPanel();
        jp2 = new JPanel();
        jp3 = new JPanel();
        jp4 = new JPanel();

        taskNameLabel = new JLabel("项目名称：");
        nameLabel = new JLabel(task_name);
        processLabel = new JLabel("项目进度：");
        jl4 = new JLabel("%");
        descLabel = new JLabel("描述：");

        Font font = new Font("SansSerif", Font.BOLD, 15);
        taskNameLabel.setFont(font);
        nameLabel.setFont(font);

        editButton = new JButton("修改");
        backButton = new JButton("返回");

        editButton.addActionListener(this);
        backButton.addActionListener(this);

        processTextField = new JTextField(5);

        jta = new JTextArea(10, 30);
        jsp = new JScrollPane(jta);

        jta.setLineWrap(true);  // 设置自动换行
        jsp.setBounds(20, 70, 160, 100);

        if (identity == 1) {
            processTextField.setEditable(false);
            jta.setEditable(false);
            editButton.setEnabled(false);
        }

        JPanel topPanel = new JPanel(new GridLayout(1, 2));
        topPanel.add(jp1);
        topPanel.add(jp2);

        jp1.add(taskNameLabel);
        jp1.add(nameLabel);

        jp2.add(processLabel);
        jp2.add(processTextField);
        jp2.add(jl4);

        jp3.add(descLabel);
        jp3.add(jsp);

        jp4.add(editButton);
        jp4.add(backButton);

        Box box = Box.createVerticalBox();
        box.add(topPanel);
        box.add(jp3);

        add(box, BorderLayout.CENTER);
        add(jp4, BorderLayout.SOUTH);

        setVisible(true);

        linkDB();

        try {
            ps = connection.prepareStatement("select task_progress, description from tasks where task_name = ?;");
            ps.setString(1, task_name);
            rs = ps.executeQuery();
            if (rs.next()) {
                progress = rs.getString(1);
                description = rs.getString(2);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        processTextField.setText(progress);
        jta.setText(description);
    }

    private static void linkDB() {
        try {
            connection = DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("修改")) {
            progress = processTextField.getText();
            description = jta.getText();
            try {
                ps = connection.prepareStatement("update tasks set task_progress = ?, description = ? " +
                        "where task_name = ?");
                ps.setString(1, progress);
                ps.setString(2, description);
                ps.setString(3, task_name);
                ps.executeUpdate();
                JOptionPane.showMessageDialog(null, "修改成功！", "提示消息", JOptionPane.WARNING_MESSAGE);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }

            processTextField.setText(progress);
            jta.setText(description);
        } else {
            dispose();
            new EmployeeTasks(employee_name, identity);
        }
    }

    public static void main(String[] args) {
        new TaskDetail("延迟队列发布文章功能", "zhang", 0);
    }
}
