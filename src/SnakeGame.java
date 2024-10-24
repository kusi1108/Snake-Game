import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;// to store the snake body
import java.util.Random;// to place the food randomly 
import javax.swing.*;

public class SnakeGame extends JPanel implements ActionListener, KeyListener {
    // we are creating a private class so that snakegame can only access the class
    // here we are initializing a tile width and height
    // when a snake moves it will be n times of 25 for example is snake is 5 tiles
    // away from above and 5 tiles from left then it is 5 times 25 pixel away
    private class Tile {
        int x, y;

        Tile(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    int boardWidth, boardHeight;
    int tileSize = 25;

    // Snake
    Tile snakeHead;
    ArrayList<Tile> snakeBody;

    // Food
    Tile food;
    Random random; // to random place food

    // game logic
    Timer gameLoop;
    int velocityX, velocityY;
    boolean gameOver = false;

    // The reason for using this is to differentiate between the instance variables
    // (class-level fields) and the parameters passed to the constructor.
    SnakeGame(int boardWidth, int boardHeight) {
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        setPreferredSize(new Dimension(this.boardWidth, this.boardHeight));
        setBackground(Color.black);
        // we have to create instance of snakegame in App.java
        // snake game is 600x600 pixels ande we need to find a tile size. So in this we
        // are going to define it as 25x25. Basically we are partitioning 600x600 into
        // 24 rows and 24 columns so that snake will move one tile not in a pixel
        addKeyListener(this);
        setFocusable(true);

        // we are setting a default start as 5, 5
        snakeHead = new Tile(5, 5);
        snakeBody = new ArrayList<Tile>();

        food = new Tile(10, 10);

        // create a random object
        random = new Random();
        placeFood(); // will create a placeFood function

        velocityX = 0;
        velocityY = 0;// its going downwards

        gameLoop = new Timer(100, this);
        gameLoop.start();
    }

    // we have to create a paint function
    public void paintComponent(Graphics g) {
        super.paintComponent(g); // This line calls the paintComponent method of the superclass (JPanel or
                                 // whichever class SnakeGame extends).It ensures that the component is properly
                                 // rendered before any custom drawing happens.
        draw(g); // we have to define the draw function. It will contain the logic for rendering
                 // your game elements
    }

    public void draw(Graphics g) {
        // Grid
        // drawing a grid
        // for (int i = 0; i < boardWidth / tileSize; i++) {
        // g.drawLine(i * tileSize, 0, i * tileSize, boardHeight); // drawing vertical
        // lines
        // g.drawLine(0, i * tileSize, boardWidth, i * tileSize); // drawing horizontal
        // lines
        // }

        // Food
        g.setColor(Color.red);
        // g.fillRect(food.x * tileSize, food.y * tileSize, tileSize, tileSize);
        g.fill3DRect(food.x * tileSize, food.y * tileSize, tileSize, tileSize, true);

        // Snake head
        g.setColor(Color.green);
        // The fillRect method in the Graphics class is used to draw a filled rectangle
        // on the screen.
        // to get the 5, 5 tiles away we have to multiply with tilesize
        // g.fillRect(snakeHead.x * tileSize, snakeHead.y * tileSize, tileSize,
        // tileSize);
        // x: The x-coordinate of the upper-left corner of the rectangle.
        // y: The y-coordinate of the upper-left corner of the rectangle.
        // width: The width of the rectangle.
        // height: The height of the rectangle.
        g.fill3DRect(snakeHead.x * tileSize, snakeHead.y * tileSize, tileSize, tileSize, true);

        // snake body
        for (int i = 0; i < snakeBody.size(); i++) {
            Tile snakePart = snakeBody.get(i);
            // g.fillRect(snakePart.x * tileSize, snakePart.y * tileSize, tileSize,
            // tileSize);
            g.fill3DRect(snakePart.x * tileSize, snakePart.y * tileSize, tileSize, tileSize, true);
        }

        // Score
        g.setFont(new Font("Arial", Font.PLAIN, 16));
        if (gameOver) {
            g.setColor(Color.red);
            g.drawString("Game Over: " + String.valueOf(snakeBody.size()), tileSize - 16, tileSize);
        } else {
            g.drawString("Score: " + String.valueOf(snakeBody.size()), tileSize - 16, tileSize);
        }
    }

    public void placeFood() {
        // This function set the x and y coordinates of the food
        food.x = random.nextInt(boardWidth / tileSize);// 600/25 = 24 so the x will be randome number from 0 to 24 it is
                                                       // same for y
        food.y = random.nextInt(boardHeight / tileSize);
    }

    // Collison
    public boolean collision(Tile t1, Tile t2) {
        return t1.x == t2.x && t1.y == t2.y;
    }

    public void move() {
        // eat food
        if (collision(snakeHead, food)) {
            snakeBody.add(new Tile(food.x, food.y));
            placeFood();
        }

        // snake body
        for (int i = snakeBody.size() - 1; i >= 0; i--) {
            Tile snakePart = snakeBody.get(i);
            if (i == 0) {
                snakePart.x = snakeHead.x;
                snakePart.y = snakeHead.y;
            } else {
                Tile prevSnakePart = snakeBody.get(i - 1);
                snakePart.x = prevSnakePart.x;
                snakePart.y = prevSnakePart.y;
            }
        }

        // snake head
        snakeHead.x += velocityX;
        snakeHead.y += velocityY;

        // game over conditions
        for (int i = 0; i < snakeBody.size(); i++) {
            Tile snakePart = snakeBody.get(i);
            // collide with the snake head
            if (collision(snakeHead, snakePart)) {
                gameOver = true;
            }
            if (snakeHead.x * tileSize < 0 || snakeHead.x * tileSize > boardWidth || snakeHead.y * tileSize < 0
                    || snakeHead.y * tileSize > boardHeight) {
                gameOver = true;
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
        if (gameOver) {
            gameLoop.stop();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP && velocityY != 1) {
            velocityX = 0;
            velocityY = -1;
        }

        if (e.getKeyCode() == KeyEvent.VK_DOWN && velocityY != -1) {
            velocityX = 0;
            velocityY = 1;
        }

        if (e.getKeyCode() == KeyEvent.VK_LEFT && velocityY != 1) {
            velocityX = -1;
            velocityY = 0;
        }

        if (e.getKeyCode() == KeyEvent.VK_RIGHT && velocityY != -1) {
            velocityX = 1;
            velocityY = 0;
        }
    }

    // We don't need these
    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
