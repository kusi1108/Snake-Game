import javax.swing.*;

public class App {
    public static void main(String[] args) throws Exception {
        // System.out.println("Hello, World!");
        // Set the frame
        int boardWidth = 600;
        int boardHeight = boardWidth;

        // Create the Window
        JFrame frame = new JFrame("Snake Game");
        frame.setVisible(true);
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // To draw the game we need a JPanel for it we create a new class
        // Creating the instance of snake game
        SnakeGame snakeGame = new SnakeGame(boardWidth, boardHeight);
        frame.add(snakeGame);
        // to get the frame in full dimensions
        frame.pack();
        snakeGame.requestFocus();

    }
}
