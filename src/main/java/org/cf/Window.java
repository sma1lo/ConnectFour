package org.cf;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Window extends JFrame {

    private Board board;
    private MiniMax bot;

    private BoardPanel boardPanel;
    private JLabel status;

    private boolean gameOver;
    private boolean botThinking;

    public Window() {
        setTitle("Connect Four");
        setSize(650, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        showMenu();

        setVisible(true);
    }

    private void showMenu() {
        JPanel menu = new JPanel();
        menu.setLayout(new BoxLayout(menu, BoxLayout.Y_AXIS));
        menu.setBorder(BorderFactory.createEmptyBorder(170, 100, 170, 100));

        JLabel title = new JLabel("CONNECT FOUR");
        title.setFont(new Font("Arial", Font.BOLD, 32));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("Choose who goes first");
        subtitle.setFont(new Font("Arial", Font.PLAIN, 16));
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton playerButton = new JButton("Player starts");
        JButton botButton = new JButton("Bot starts");

        playerButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        botButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        playerButton.setMaximumSize(new Dimension(200, 35));
        botButton.setMaximumSize(new Dimension(200, 35));

        playerButton.addActionListener(e -> startGame(false));
        botButton.addActionListener(e -> startGame(true));

        menu.add(title);
        menu.add(Box.createVerticalStrut(15));
        menu.add(subtitle);
        menu.add(Box.createVerticalStrut(35));
        menu.add(playerButton);
        menu.add(Box.createVerticalStrut(10));
        menu.add(botButton);

        getContentPane().removeAll();
        add(menu);

        revalidate();
        repaint();
    }

    private void startGame(boolean botFirst) {
        board = new Board();
        bot = new MiniMax(board);

        gameOver = false;
        botThinking = false;

        status = new JLabel("Player's turn", SwingConstants.CENTER);
        status.setFont(new Font("Arial", Font.BOLD, 18));

        JButton newGame = new JButton("New Game");
        newGame.addActionListener(e -> showMenu());

        JPanel header = new JPanel(new BorderLayout());
        header.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        header.add(status, BorderLayout.CENTER);
        header.add(newGame, BorderLayout.EAST);

        boardPanel = new BoardPanel();

        getContentPane().removeAll();

        add(header, BorderLayout.NORTH);
        add(boardPanel, BorderLayout.CENTER);

        revalidate();
        repaint();

        if (botFirst) {
            botMove();
        }
    }

    private void playerMove(int col) {
        if (gameOver || botThinking) {
            return;
        }

        int row = board.getAvailableRow(col);

        if (row == -1) {
            return;
        }

        board.makeMove(new Cell(row, col, BoardState.TEAM_A));

        boardPanel.repaint();

        if (checkGameEnd(row, col, BoardState.TEAM_A)) {
            return;
        }

        botMove();
    }

    private boolean checkGameEnd(int row, int col, BoardState state) {
        if (board.checkWin(row, col)) {
            gameOver = true;

            String message = state == BoardState.TEAM_A
                    ? "You win!"
                    : "Bot wins!";

            status.setText(message);
            JOptionPane.showMessageDialog(this, message);

            return true;
        }

        if (board.getMoves() == Board.ROWS * Board.COLS) {
            gameOver = true;

            status.setText("Draw!");
            JOptionPane.showMessageDialog(this, "Draw!");

            return true;
        }

        return false;
    }

    private Board copyBoard() {
        Board copy = new Board();

        for (int col = 0; col < Board.COLS; col++) {
            for (int row = Board.ROWS - 1; row >= 0; row--) {

                BoardState state = board.getCell(row, col).getState();

                if (state != BoardState.NONE) {
                    copy.makeMove(new Cell(row, col, state));
                }
            }
        }

        return copy;
    }

    private void botMove() {
        if (gameOver) {
            return;
        }

        botThinking = true;
        status.setText("Bot is thinking...");

        SwingWorker<Integer, Void> worker =
                new SwingWorker<Integer, Void>() {

                    @Override
                    protected Integer doInBackground() {
                        Board searchBoard = copyBoard();
                        MiniMax searchBot = new MiniMax(searchBoard);

                        return searchBot.findBestMove(BoardState.TEAM_B);
                    }

                    @Override
                    protected void done() {
                        try {
                            int col = get();
                            int row = board.getAvailableRow(col);

                            if (row != -1) {
                                board.makeMove(
                                        new Cell(row, col, BoardState.TEAM_B)
                                );

                                boardPanel.repaint();

                                if (checkGameEnd(row, col, BoardState.TEAM_B)) {
                                    botThinking = false;
                                    return;
                                }
                            }

                            botThinking = false;
                            status.setText("Player's turn");

                        } catch (Exception ex) {
                            botThinking = false;
                            status.setText("Bot error");
                            ex.printStackTrace();
                        }
                    }
                };

        worker.execute();
    }

    private class BoardPanel extends JPanel {

        public BoardPanel() {
            setBackground(Color.WHITE);

            addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    int cellSize = Math.min(getWidth() / Board.COLS, getHeight() / Board.ROWS);

                    int boardWidth = cellSize * Board.COLS;
                    int boardHeight = cellSize * Board.ROWS;

                    int offsetX = (getWidth() - boardWidth) / 2;
                    int offsetY = (getHeight() - boardHeight) / 2;

                    int x = e.getX() - offsetX;
                    int y = e.getY() - offsetY;

                    if (x < 0 || x >= boardWidth || y < 0 || y >= boardHeight) {
                        return;
                    }

                    int col = x / cellSize;
                    playerMove(col);
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int cellSize = Math.min(getWidth() / Board.COLS, getHeight() / Board.ROWS);

            int boardWidth = cellSize * Board.COLS;
            int boardHeight = cellSize * Board.ROWS;

            int offsetX = (getWidth() - boardWidth) / 2;
            int offsetY = (getHeight() - boardHeight) / 2;

            g2d.setColor(new Color(35, 85, 190));
            g2d.fillRect(offsetX, offsetY, boardWidth, boardHeight);

            int diameter = cellSize - 10;
            int padding = 5;

            for (int row = 0; row < Board.ROWS; row++) {
                for (int col = 0; col < Board.COLS; col++) {
                    BoardState state = board.getCell(row, col).getState();

                    if (state == BoardState.TEAM_A) {
                        g2d.setColor(Color.RED);
                    } else if (state == BoardState.TEAM_B) {
                        g2d.setColor(Color.YELLOW);
                    } else {
                        g2d.setColor(Color.WHITE);
                    }

                    int circleX = offsetX + col * cellSize + padding;
                    int circleY = offsetY + row * cellSize + padding;

                    g2d.fillOval(circleX, circleY, diameter, diameter);
                }
            }
        }
    }
}
