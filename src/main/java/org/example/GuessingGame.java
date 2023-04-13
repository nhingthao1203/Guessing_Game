package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.example.SoundPlayer;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JColorChooser;
import java.awt.Toolkit;


public class GuessingGame extends JFrame implements ActionListener {
    private JLabel promptLabel, resultLabel, numbersLabel, countdownLabel;
    private JButton[] numberButtons;
    private int computerNumber;
    private int count;
    private int timeLeft = 30; // set initial time to 30 seconds
    private Timer timer;
    private JButton playAgainButton;
    private String username;//biến user
    private UserExcel ui= new UserExcel();


    public GuessingGame(String username,int score) {
        this.username=username;
        // Set up the window
        setTitle("Guessing Game");
        setSize(900, 1200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        //Show username
        JLabel usernameLabel = new JLabel("Welcome, " + username);
        usernameLabel.setFont(new Font(Font.DIALOG, Font.BOLD, 30));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 4;
        add(usernameLabel, gbc);

        //HIển thị hộp thoại nhập lấy giá tr thời gian
        String timeInput = JOptionPane.showInputDialog("Enter countdown time (in seconds):");
        int time = 0;
        if (timeInput != null && !timeInput.isEmpty()) {
            time = Integer.parseInt(timeInput);
        }
        this.timeLeft = time;


        //Show score
        int bestScore =ui.getScore(username);
        JLabel scoreLabel = new JLabel("Best score: "+ bestScore);
        scoreLabel.setFont(new Font(Font.DIALOG, Font.BOLD, 30));
        gbc.gridx = 0;
        gbc.gridy = 9;
        gbc.gridwidth = 4;
        add(scoreLabel, gbc);

        playAgainButton = new JButton("Play Again");
        playAgainButton.setFont(new Font(Font.DIALOG, Font.BOLD, 30));
        playAgainButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Reset the game
                computerNumber = (int) (Math.random() * 100 + 1);
                count = 1;
//                timeLeft = time;
                int score = ui.getScore(username);
                if (count < score || score == 0) {
                    ui.updateScore(username, count);
                }
                // Restart the game with the updated score
                GuessingGame game = new GuessingGame(username, count);
                dispose();

                resultLabel.setText("");
                countdownLabel.setText("Time left: " + timeLeft + " seconds");
                for (int i = 0; i < 100; i++) {
                    numberButtons[i].setEnabled(true);
                    numberButtons[i].setBackground(null);
                }
                timer.restart();
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 4;
        add(playAgainButton, gbc);

        // Generate a random number
        computerNumber = (int) (Math.random() * 100 + 1);
        count = 0;

        // Add components to the window
        promptLabel = new JLabel("Click a number to guess between 1 and 100:");
        promptLabel.setFont(new Font(Font.DIALOG, Font.BOLD, 30));
        gbc.gridx =0;
        gbc.gridy = 1;
        gbc.gridwidth = 4;
        gbc.anchor =GridBagConstraints.CENTER;
        add(promptLabel, gbc);

        //time
        countdownLabel = new JLabel("Time left: " + timeLeft + " seconds");
        countdownLabel.setFont(new Font(Font.DIALOG, Font.BOLD, 30));
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 4;
        gbc.anchor =GridBagConstraints.CENTER;
        add(countdownLabel, gbc);

        JPanel numberPanel = new JPanel();
        numberPanel.setLayout(new GridLayout(10, 10));

        // Create an array of 100 numbers
        numberButtons = new JButton[100];
        for (int i = 0; i < 100; i++) {
            numberButtons[i] = new JButton(String.valueOf(i + 1));
            numberButtons[i].addActionListener(this);
            Font font = new Font(Font.DIALOG, Font.BOLD, 20);
            numberButtons[i].setFont(font);
            numberButtons[i].setPreferredSize(new Dimension(80, 80));
            numberPanel.add(numberButtons[i]);
        }

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 4;
        add(numberPanel, gbc);

        resultLabel = new JLabel("");
        resultLabel.setFont(new Font(Font.DIALOG, Font.BOLD, 30));
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 4;
        add(resultLabel, gbc);

        setVisible(true);
        // Set up the timer to update the countdown label every second
        timer = new Timer(1000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                timeLeft--;
                countdownLabel.setText("Time left: " + timeLeft + " seconds");
                if (timeLeft == 0) {
                    // Disable all the number buttons if time runs out
                    for (int i = 0; i < 100; i++) {
                        numberButtons[i].setEnabled(false);
                        if (Integer.parseInt(numberButtons[i].getText()) == computerNumber && !numberButtons[i].getBackground().equals(Color.RED)) {
                            numberButtons[i].setBackground(Color.RED);
                        }
                    }
                    resultLabel.setText("Time's up! The number was " + computerNumber);
                    SoundPlayer.play("src/main/java/Sound/Timeup.wav");
                    timer.stop();
                }

            }
        });
        timer.start();

        pack();
        setLocationRelativeTo(null);


    }

    public void actionPerformed(ActionEvent event) {
        // Get the number from the button that was clicked
        String guessString = event.getActionCommand();
        int guess = Integer.parseInt(guessString);

        // Determine if the guess is too high, too low, or correct
        if (guess < computerNumber) {
            resultLabel.setText("Your guess is too low, try again");
            resultLabel.setFont(new Font(Font.DIALOG, 10,30));
            count++;
        } else if (guess > computerNumber) {
            resultLabel.setText("Your guess is too high, try again");
            resultLabel.setFont(new Font(Font.DIALOG, 10,30));
            count++;
        } else {
            resultLabel.setText("Correct! Total guesses: " + count);
            resultLabel.setFont(new Font(Font.DIALOG, 10,30));
            //sound bingo
            SoundPlayer.play("src/main/java/Sound/Bingo.wav");
            int score = 100-count*5;
            int besScore = ui. getScore(username);
            if(score>besScore)
            {
                ui.updateScore(username,score);
//                scoreLabel.setText("Best score:"+score);
            }
            // Disable all the number buttons
            for (int i = 0; i < 100; i++) {
                numberButtons[i].setEnabled(false);
            }
            timer.stop();
        }
        // Get the button that was clicked
        JButton clickedButton = (JButton) event.getSource();

// Change the background color of the button
        if (guess == computerNumber || timeLeft == 0) {
            clickedButton.setBackground(new Color(86, 213, 86));
        } else {
            clickedButton.setBackground(new Color(239, 113, 148));
        }

    }

//    public static void main(String[] args) {
//        UserExcel user = new UserExcel();
//    }
}