import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;
// JPanel  boardWidth, BoardHeight - +ActionListener Implement new Method

public class Snakegame extends JPanel implements ActionListener, KeyListener {


    //private class
    private class Tile {
        int x;
        int y;
        

        Tile(int x, int y) {
            this.x = x;
            this.y = y;
        }

    }
    int boardWidth;
    int boardHeight;
    int tileSize = 25;
//snake
    Tile snakeHead;
    ArrayList<Tile> snakeBody;

    //food
    Tile apple;
    Random random; //set randomize apple

//Game Logic
    int velocityX;
    int velocityY;
    Timer gameLoop; //game loop

    boolean gameOver = false;



    Snakegame(int boardWidth, int boardHeight) {
        this.boardWidth = boardWidth; //field & parameter
        this.boardHeight = boardHeight; //''
        setPreferredSize(new Dimension(this.boardWidth, this.boardHeight));
        setBackground(Color.black);
        addKeyListener(this);
        setFocusable(true);

        snakeHead = new Tile(5, 5);
        snakeBody = new ArrayList<>();

        apple = new Tile(10, 10);
        random = new Random();
        placeApple(); //PlaceApple function

        velocityX = 1;
        velocityY = 0; //downwards


// set new Timer: delay / listener loop
        gameLoop = new Timer(100, this);
        gameLoop.start();

    }

//draw graphics with Graphics g
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
        //Grid properties
        for (int i = 0; i < boardWidth/tileSize; i++) {
            g.drawLine(i*tileSize, 0, i*tileSize, boardHeight); //(x1, x2)
            g.drawLine(0, i*tileSize, boardWidth, i*tileSize); //(y1 y2)
        }
      //apple food
        g.setColor(Color.red);
        g.fillRect(apple.x *tileSize, apple.y *tileSize, tileSize, tileSize);
    }



//snakehead
    public void draw(Graphics g) {
        g.setColor(Color.green);
        g.fillRect(snakeHead.x * tileSize, snakeHead.y *tileSize, tileSize, tileSize); //multiplies snake head if catch

        //snakebody
        for (int i = 0; i <snakeBody.size(); i++) {
            Tile snakePart = snakeBody.get(i);
            g.fillRect(snakePart.x * tileSize, snakePart.y * tileSize, tileSize, tileSize);
        }
    }


    public void placeApple() {
        apple.x = random.nextInt(boardWidth/tileSize); // 600/25 = 24
        apple.y = random.nextInt(boardHeight/tileSize);
    }


    public void move() {
        //eat food
        if (collision(snakeHead, apple)) {
            snakeBody.add(new Tile(apple.x, apple.y));
            placeApple();
        }

        //move snake body
        for (int i = snakeBody.size()-1; i >= 0; i--) {
            Tile snakePart = snakeBody.get(i);
            if (i == 0) { //right before the head
                snakePart.x = snakeHead.x;
                snakePart.y = snakeHead.y;
            }
            else {
                Tile prevSnakePart = snakeBody.get(i-1);
                snakePart.x = prevSnakePart.x;
                snakePart.y = prevSnakePart.y;
            }
        }
        //move snake head
        snakeHead.x += velocityX;
        snakeHead.y += velocityY;

        //game over conditions
        for (int i = 0; i < snakeBody.size(); i++) {
            Tile snakePart = snakeBody.get(i);

            //collide with snake head
            if (collision(snakeHead, snakePart)) {
                gameOver = true;
            }
        }

        if (snakeHead.x*tileSize < 0 || snakeHead.x*tileSize > boardWidth || //passed left border or right border
                snakeHead.y*tileSize < 0 || snakeHead.y*tileSize > boardHeight ) { //passed top border or bottom border
            gameOver = true;
        }
    }

    public boolean collision(Tile tile1, Tile tile2) {
        return tile1.x == tile2.x && tile1.y == tile2.y;
    }

    @Override
    public void actionPerformed(ActionEvent e) { //called every x milliseconds by gameLoop timer
        move();
        repaint();
        if (gameOver) {
            gameLoop.stop();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // System.out.println("KeyEvent: " + e.getKeyCode());
        if (e.getKeyCode() == KeyEvent.VK_UP && velocityY != 1) {
            velocityX = 0;
            velocityY = -1;
        }
        else if (e.getKeyCode() == KeyEvent.VK_DOWN && velocityY != -1) {
            velocityX = 0;
            velocityY = 1;
        }
        else if (e.getKeyCode() == KeyEvent.VK_LEFT && velocityX != 1) {
            velocityX = -1;
            velocityY = 0;
        }
        else if (e.getKeyCode() == KeyEvent.VK_RIGHT && velocityX != -1) {
            velocityX = 1;
            velocityY = 0;
        }
    }

    //not needed
    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}
}
