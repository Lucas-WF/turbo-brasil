package main;

import entity.Player;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel implements Runnable{
    final int orginalTitleSize = 16; // titulo fica com o tamanho 16 x 16
    final int scale = 3;

    public final int titleSize =  orginalTitleSize * scale;  // define o tamanho real do titulo
    final int maxScreenColumn = 16;
    final int maxScreenRow = 12;
    final int screenWidth = titleSize * maxScreenColumn;
    final int screenHeight = titleSize * maxScreenRow;

    int FPS = 60;

    KeyHandler keyH = new KeyHandler();
    Thread gameThread;  // chama automaticamente a função run ao inicial o game
    Player player = new Player(this, keyH);

    //set player's default position
    int playerX = 100;
    int playerY = 100;
    int playerSpeed = 4;

    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);
    }

    public void startGameThread() {

        gameThread = new Thread(this);
        gameThread.start();  // cai chamar a funcao run
    }

    @Override
    public void run() {

        while(gameThread != null) {

            double drawInterval = 1_000_000_000/FPS;  // 0.01666 seconds
            double nextDrawTime = System.nanoTime() + drawInterval;


            // 1) Update:   update information such as character position
            update();

            //2) Draw: draw on the screen with the updates data
            repaint();


            try {

                double remaningTime = nextDrawTime - System.nanoTime();
                remaningTime = remaningTime/1_000_000;

                if(remaningTime < 0) {
                    remaningTime = 0;
                }

                Thread.sleep((long) remaningTime);

                nextDrawTime += drawInterval;
            }
            catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }
    }

    public void update() {
        player.update();
    }

    public void paintComponent(Graphics g) {

            super.paintComponent(g);

            Graphics2D g2 = (Graphics2D) g;

            player.draw(g2);

            g2.dispose();
    }
}
