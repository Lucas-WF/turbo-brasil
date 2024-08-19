package main;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;

public class Ui {
    private Graphics2D g2;

    private KeyHandler keyHandler;

    public Ui(KeyHandler keyHandler) {
        this.keyHandler = keyHandler;
    }

    public void drawMenu(Graphics2D g2, GamePanel gp) {
        BufferedImage image;
        this.g2 = g2;

        try {
            image = ImageIO.read(new FileInputStream("res/titulo/Home.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        g2.drawImage(image, 0, 0, gp.getWidth(), gp.getHeight(), null);
    }

    public void drawChooseGameMode(Graphics2D g2, GamePanel gp) {
        BufferedImage image;

        try {
            image = ImageIO.read(new FileInputStream("res/titulo/ChooseGameMode.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        g2.drawImage(image, 0, 0, gp.getWidth(), gp.getHeight(), null);
    }


}
