import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import javax.sound.sampled.*;

public class TicTacToe extends JFrame {
    private JButton[][] buttons;
    private JButton restartButton;
    private char currentPlayer;
    private boolean gameEnded;
    private Clip clickSound;
    private Clip winSound;
    private Clip drawSound;

    public TicTacToe() {
        setTitle("Tic Tac Toe");
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Initialize sounds
        loadSounds();

        // Game board panel
        JPanel boardPanel = new JPanel(new GridLayout(3, 3));
        buttons = new JButton[3][3];
        currentPlayer = 'X';
        gameEnded = false;

        initializeButtons(boardPanel);

        // Restart button panel
        JPanel controlPanel = new JPanel();
        restartButton = new JButton("Restart Game");
        restartButton.addActionListener(e -> restartGame());
        controlPanel.add(restartButton);

        add(boardPanel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);
    }

    private void loadSounds() {
        try {
            // Click sound
            URL clickSoundURL = getClass().getResource("click.wav");
            if (clickSoundURL != null) {
                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(clickSoundURL);
                clickSound = AudioSystem.getClip();
                clickSound.open(audioInputStream);
            }

            // Win sound
            URL winSoundURL = getClass().getResource("win.wav");
            if (winSoundURL != null) {
                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(winSoundURL);
                winSound = AudioSystem.getClip();
                winSound.open(audioInputStream);
            }

            // Draw sound
            URL drawSoundURL = getClass().getResource("draw.wav");
            if (drawSoundURL != null) {
                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(drawSoundURL);
                drawSound = AudioSystem.getClip();
                drawSound.open(audioInputStream);
            }
        } catch (Exception e) {
            System.out.println("Error loading sounds: " + e.getMessage());
        }
    }

    private void playSound(Clip sound) {
        if (sound != null) {
            sound.setFramePosition(0);
            sound.start();
        }
    }

    private void initializeButtons(JPanel panel) {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                buttons[row][col] = new JButton();
                buttons[row][col].setFont(new Font("Arial", Font.BOLD, 60));
                buttons[row][col].addActionListener(new ButtonClickListener(row, col));
                panel.add(buttons[row][col]);
            }
        }
    }

    private void restartGame() {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                buttons[row][col].setText("");
                buttons[row][col].setEnabled(true);
            }
        }
        currentPlayer = 'X';
        gameEnded = false;
    }

    private class ButtonClickListener implements ActionListener {
        private int row;
        private int col;

        public ButtonClickListener(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (gameEnded || !buttons[row][col].getText().equals("")) {
                return;
            }

            buttons[row][col].setText(String.valueOf(currentPlayer));
            playSound(clickSound);

            if (checkWin()) {
                JOptionPane.showMessageDialog(null, "Player " + currentPlayer + " wins!");
                playSound(winSound);
                gameEnded = true;
                disableAllButtons();
            } else if (isBoardFull()) {
                JOptionPane.showMessageDialog(null, "It's a draw!");
                playSound(drawSound);
                gameEnded = true;
            } else {
                currentPlayer = (currentPlayer == 'X') ? 'O' : 'X';
            }
        }
    }

    private void disableAllButtons() {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                buttons[row][col].setEnabled(false);
            }
        }
    }

    private boolean checkWin() {
        // Check rows
        for (int row = 0; row < 3; row++) {
            if (buttons[row][0].getText().equals(String.valueOf(currentPlayer)) &&
                buttons[row][1].getText().equals(String.valueOf(currentPlayer)) &&
                buttons[row][2].getText().equals(String.valueOf(currentPlayer))) {
                return true;
            }
        }

        // Check columns
        for (int col = 0; col < 3; col++) {
            if (buttons[0][col].getText().equals(String.valueOf(currentPlayer)) &&
                buttons[1][col].getText().equals(String.valueOf(currentPlayer)) &&
                buttons[2][col].getText().equals(String.valueOf(currentPlayer))) {
                return true;
            }
        }

        // Check diagonals
        if (buttons[0][0].getText().equals(String.valueOf(currentPlayer)) &&
            buttons[1][1].getText().equals(String.valueOf(currentPlayer)) &&
            buttons[2][2].getText().equals(String.valueOf(currentPlayer))) {
            return true;
        }

        if (buttons[0][2].getText().equals(String.valueOf(currentPlayer)) &&
            buttons[1][1].getText().equals(String.valueOf(currentPlayer)) &&
            buttons[2][0].getText().equals(String.valueOf(currentPlayer))) {
            return true;
        }

        return false;
    }

    private boolean isBoardFull() {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                if (buttons[row][col].getText().equals("")) {
                    return false;
                }
            }
        }
        return true;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TicTacToe game = new TicTacToe();
            game.setVisible(true);
        });
    }
}