package com.lwp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

/**
 * @author lanwp
 * @Date 2019/10/11 20:25
 */
public class Notepad {
    public static void main(String[] args) {

        // 窗体 JFrame
        // 文本域 JTextArea
        // 菜单条 JMenuBar
        // 菜单   JMenu
        // 菜单项  JMenuItem

        // 创建一个窗体对象
        JFrame jFrame = new JFrame("记事本");
        // 创建一个菜单条
        JMenuBar jMenuBar = new JMenuBar();
        // 创建菜单
        JMenu fileMenu = new JMenu("文件");
        JMenu editMenu = new JMenu("编辑");
        JMenu layoutMenu = new JMenu("格式");
        JMenu viewMenu = new JMenu("查看");
        JMenu helpMenu = new JMenu("帮助");

        // 创建菜单项-子菜单
        JMenuItem newMenuItem = new JMenuItem("新建(N)");
        JMenuItem openMenuItem = new JMenuItem("打开(O)");
        JMenuItem saveMenuItem = new JMenuItem("保存(S)");
        JMenuItem saveAsMenuItem = new JMenuItem("另存为(A)");
        JMenuItem printMenuItem = new JMenuItem("打印(P)");
        JMenuItem exitMenuItem = new JMenuItem("退出(X)");
        fileMenu.add(openMenuItem);
        fileMenu.add(newMenuItem);
        fileMenu.add(saveMenuItem);
        fileMenu.add(saveAsMenuItem);
        // 增加分隔线
        fileMenu.addSeparator();
        fileMenu.add(printMenuItem);
        fileMenu.add(exitMenuItem);

        // 创建文本域
        JTextArea area = new JTextArea();
        // 增加滚动条，不加超出窗口不显示
        JScrollPane scrollPane = new JScrollPane(area); // 包着area

        area.setFont(new Font("微软雅黑", Font.ITALIC, 26));

        // 按钮功能
        newMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int value = JOptionPane.showConfirmDialog(jFrame, "确定新建文件?");
                if (value == 0) {
                    area.setText("");
                }
            }
        });

        openMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 文件选择
                JFileChooser jFileChooser = new JFileChooser();
                int value = jFileChooser.showOpenDialog(jFrame);
                // 选中了某一个文件  读取文件的内容 战士在我们自己记事本的文本域中
                File selectedFile = jFileChooser.getSelectedFile();
                if (selectedFile.isFile() && value == 0) {
                    System.out.println("读取文件");
                    if (selectedFile.getName().endsWith(".txt")) {
                        StringBuilder sb = new StringBuilder();
                        try {
                            FileInputStream fis = new FileInputStream(selectedFile);
                            BufferedInputStream bis = new BufferedInputStream(fis);
                            byte[] bytes = new byte[1024];
                            int read = bis.read(bytes);
                            while (read != -1) {
                                String result = new String(bytes);
                                sb.append(result);
                                read = bis.read(bytes);
                            }
                            area.setText(sb.toString());
//                            area.append();

                        } catch (FileNotFoundException e1) {
                            e1.printStackTrace();
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }

                    }
                }
            }
        });

        // 保存功能
        saveMenuItem.addActionListener(e -> {
            //弹出文件选择器
            JFileChooser jFileChooser = new JFileChooser();
            //弹出保存对话框
            int value = jFileChooser.showOpenDialog(jFrame);
            if (value == 0) {
                File selectedFile = jFileChooser.getSelectedFile();
                //获取现在记事本中的文字
                String result = area.getText();
                // 创建文件
                try {
                    boolean newFile = selectedFile.createNewFile();
                    // 输入
                    BufferedWriter bw = new BufferedWriter(new FileWriter(selectedFile));
                    bw.write(result);
                    bw.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });

        // 将菜单添加在菜单条中
        jMenuBar.add(fileMenu);
        jMenuBar.add(editMenu);
        jMenuBar.add(layoutMenu);
        jMenuBar.add(viewMenu);
        jMenuBar.add(helpMenu);
        // 将菜单条和文本域添加在窗体中
        jFrame.setJMenuBar(jMenuBar);
//        jFrame.add(area);
        // 需要滚动条就添加滚动条,滚动条包含area
        jFrame.add(scrollPane);

        jFrame.setBounds(300, 400, 500, 500);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setVisible(true);
    }
}
