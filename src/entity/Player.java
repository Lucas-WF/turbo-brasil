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
        this.x = 750;
        this.speed = 15; // Set the movement speed

        try {
            this.image = ImageIO.read(new FileInputStream("res/fusca/monke.png"));
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
        g2.drawImage(image, x, 570, 240, 240, null);
    }

    public int getSpeed() {
        return speed;
    }

}
