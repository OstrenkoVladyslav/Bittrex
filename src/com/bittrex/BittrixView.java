package com.bittrex;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class BittrixView extends JFrame {
//    private final String HOST = "localhost";
//    private final int PORT = 8381;

//    private Socket socket;
//    private Scanner in;
//    private DataOutputStream out;

    public BittrixView() {

//        this.out = out;

        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        screenSize.setSize(screenSize.getWidth() / 2, screenSize.getHeight() / 2);
        setSize(screenSize);
        setTitle("Chat");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(true);
        setLocationByPlatform(true);
        setLayout(new BorderLayout());

        JTextArea textArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(textArea);
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        add(bottomPanel, BorderLayout.SOUTH);

        JTextField textField = new JTextField();
        bottomPanel.add(textField, BorderLayout.CENTER);

        textField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String text = textField.getText();
                textArea.append(text + "\n");
                textField.setText("");
            }
        });

        JButton buttonOK = new JButton("Send");
        bottomPanel.add(buttonOK, BorderLayout.EAST);

        buttonOK.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String text = textField.getText();
                textArea.append(text + "\n");
                textField.setText("");
                textField.requestFocusInWindow();
            }
        });

        JMenuBar jMenuBar = new JMenuBar();
        setJMenuBar(jMenuBar);
        JMenu fileMenu = new JMenu("File");
        jMenuBar.add(fileMenu);
        JMenuItem exitItem = new JMenuItem("Exit");
        fileMenu.add(exitItem);
        exitItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        JMenu editMenu = new JMenu("Edit");
        jMenuBar.add(editMenu);
        JMenuItem clearItem = new JMenuItem("Clear text");
        editMenu.add(clearItem);
        clearItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textArea.setText("");
            }
        });
        JMenuItem fillItem = new JMenuItem("Fill with Lorem Ipsum");
        editMenu.add(fillItem);
        fillItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (int i = 0; i < 5; i++) {
                    textArea.append("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum. \n");
                }
            }
        });

        setVisible(true);
        textField.requestFocusInWindow();

    }
}
