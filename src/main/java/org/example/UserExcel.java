package org.example;
import java.awt.Window;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.FileOutputStream;


public class UserExcel extends JFrame {
    private JLabel usernameLabel, passwordLabel, messageLabel;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton, registerButton;

    private Workbook workbook;
    private Sheet sheet;
    private String filename = "credentials.xlsx";

    public UserExcel() {
        super("User Credentials");
        createUI();
        readExcelFile();
    }

    private void createUI() {
        Container pane = getContentPane();
        pane.setLayout(new GridLayout(4, 2));

        usernameLabel = new JLabel("Username: ");
        usernameField = new JTextField(20);

        passwordLabel = new JLabel("Password: ");
        passwordField = new JPasswordField(20);

        messageLabel = new JLabel("");

        loginButton = new JButton("Login");
        loginButton.addActionListener(new LoginButtonListener());

        registerButton = new JButton("Register");
        registerButton.addActionListener(new RegisterButtonListener());

        pane.add(usernameLabel);
        pane.add(usernameField);
        pane.add(passwordLabel);
        pane.add(passwordField);
        pane.add(loginButton);
        pane.add(registerButton);
        pane.add(messageLabel);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 200);
        setVisible(true);
    }

    private void readExcelFile() {
        try {
            FileInputStream file = new FileInputStream(new File(filename));
            workbook = new XSSFWorkbook(file);
            sheet = workbook.getSheetAt(0);
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeExcelFile() {
        try {
            FileOutputStream outputStream = new FileOutputStream(filename);
            workbook.write(outputStream);
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean checkCredentials(String username, String password) {
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            Cell usernameCell = row.getCell(0);
            Cell passwordCell = row.getCell(1);

            String savedUsername = usernameCell.getStringCellValue();
            String savedPassword = passwordCell.getStringCellValue();

            if (username.equals(savedUsername) && password.equals(savedPassword)) {
                return true;
            }
        }

        return false;
    }
    public int getScore(String username) {
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            Cell usernameCell = row.getCell(0);
            Cell scoreCell = row.getCell(2);

            String savedUsername = usernameCell.getStringCellValue();
            int score = (int) scoreCell.getNumericCellValue();

            if (username.equals(savedUsername)) {
                return score;
            }
        }

        return 0; // User not found, return 0 score
    }
    public void updateScore(String username, int score) {
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            Cell usernameCell = row.getCell(0);
            Cell scoreCell = row.getCell(2);

            String savedUsername = usernameCell.getStringCellValue();
            int savedScore = (int) scoreCell.getNumericCellValue();

            if (username.equals(savedUsername)) {
                // Update the score if it's higher than the saved score
                if (score > savedScore) {
                    scoreCell.setCellValue(score);
                    writeExcelFile();
                }
                dispose();
                return;
            }
        }
    }

    private boolean registerUser(String username, String password) {
        if (checkCredentials(username, password)) {
            messageLabel.setText("User already exists");
            return false;
        }

        Row newRow = sheet.createRow(sheet.getLastRowNum() + 1);
        Cell usernameCell = newRow.createCell(0);
        Cell passwordCell = newRow.createCell(1);
        Cell scoreCell = newRow.createCell(2);
        usernameCell.setCellValue(username);
        passwordCell.setCellValue(password);
        scoreCell.setCellValue(0);

        writeExcelFile();
        messageLabel.setText("Registration successful");
        return true;
    }

    private class LoginButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String username = usernameField.getText();
            String password = String.valueOf(passwordField.getPassword());

            if (checkCredentials(username, password)) {
                messageLabel.setText("Login successful");
                int score =getScore(username);
                GuessingGame game=new GuessingGame(username,score);
                dispose();
            } else {
                messageLabel.setText("Incorrect username or password");
            }
        }
    }

    //
    private class RegisterButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String username = usernameField.getText();
            String password = String.valueOf(passwordField.getPassword());

            registerUser(username, password);
        }
    }

    public static void main(String[] args) {
        UserExcel ui = new UserExcel();
//        ui.show();
    }
}