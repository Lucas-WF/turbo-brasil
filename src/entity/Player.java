package entity;

import main.KeyHandler;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;

public class Player extends Entity {
    private KeyHandler keyHandler;
    private BufferedImage image;
    private int speed;

    public Player(KeyHandler keyHandler) {
        this.keyHandler = keyHandler;
        this.x = 0;
        this.speed = 5; // Set the movement speed

        try {
            this.image = ImageIO.read(new FileInputStream("res/fusca/fuscaSplashArt.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void update() {
        if (keyHandler.leftPressed) {
            x -= speed;
        }

        if (keyHandler.rightPressed) {
            x += speed;
        }
    }

    public void draw(Graphics2D g2) {
        g2.drawImage(image, x, 0, 120, 120, null);
    }
}
