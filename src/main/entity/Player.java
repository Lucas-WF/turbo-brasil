package main.entity;

import main.engine.KeyHandler;
import main.utils.Utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

public class Player extends Entity {
    final private KeyHandler keyHandler;

    public Player(KeyHandler keyHandler) throws IOException {
        this.keyHandler = keyHandler;
        this.x = 650;
        this.y = 0;
        this.pos = 0;
        this.speed = 0;
        this.bufferedImage = ImageIO.read(new FileInputStream("res/fusca/fuscaSplashArt.png"));
    }

    public void update() {

        if (this.willCollideVertically) {
            this.speed = 0;
            this.y -= 5;
        }

        if (this.willCollideHorizontally) {
            this.speed = this.speed / 2;
            this.x -= 5;
        }

        if (keyHandler.upPressed) {
            if (this.speed < Utils.MAX_SPEED) {
                this.speed += (speed == 0) ? 1 : speed * 0.02;
            }
        } else {
            if (this.speed > 20) {
                this.speed -= speed * 1/100;
            } else {
                this.speed -= 5;
            }
        }

        if (keyHandler.leftPressed) {
            this.x -= (int) Math.ceil(speed * (Math.sqrt(2) / 40));
        }

        if (keyHandler.rightPressed) {
            this.x += (int) Math.ceil(speed * (Math.sqrt(2) / 40));
        }
        if (this.speed < 0) {
            this.speed = 0;
        }
    }

    public void draw(Graphics2D g2) {
        g2.drawImage(bufferedImage, x, 570, 240, 240, null);
    }

    public double getSpeed() {
        return speed;
    }

    public void collision(ArrayList<Entity> entities) {
        float verticalDistance;
        float horizontalDistance;

        for (Entity entity : entities) {
            verticalDistance = entity.y + ((float) entity.bufferedImage.getHeight() / 2) -
                    this.y + ((float) this.bufferedImage.getHeight() / 2);
            horizontalDistance = entity.x + ((float) entity.bufferedImage.getWidth() / 2) -
                    this.x + ((float) this.bufferedImage.getWidth() / 2);

            if (isHorizontallyAlignedTo(entity) && isVerticallyAlignedTo(entity)) {
                if (verticalDistance <= 0) {
                    this.willCollideVertically = true;
                }

                if (horizontalDistance <= 0) {
                    this.willCollideHorizontally = true;
                }
            }
        }
    }
}
