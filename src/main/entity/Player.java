package main.entity;

import main.engine.KeyHandler;
import javax.imageio.ImageIO;
import java.awt.*;
import java.io.FileInputStream;
import java.io.IOException;

public class Player extends Entity {
    final private KeyHandler keyHandler;

    public Player(KeyHandler keyHandler) throws IOException {
        this.keyHandler = keyHandler;
        this.x = 750;
        this.y = 0;
        this.speed = 0;
        this.bufferedImage = ImageIO.read(new FileInputStream("res/fusca/monke.png"));
    }

    public void update() {
        if (keyHandler.leftPressed) {
            x -= speed;
        }

        if (keyHandler.rightPressed) {
            x += speed;
        }

        if (keyHandler.downPressed) {
            y += speed;
        }

        if (keyHandler.upPressed) {
            y -= speed;
        }
    }

    public void draw(Graphics2D g2) {
        //g2.drawImage(bufferedImage, x, 570, 240, 240, null);
    }

    public int getSpeed() {
        return speed;
    }

}
