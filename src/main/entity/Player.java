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
        this.x = 750;
        this.y = 0;
        this.speed = 0;
        this.bufferedImage = ImageIO.read(new FileInputStream("res/fusca/monke.png"));
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

        if (keyHandler.leftPressed) {
            this.x -= (int) Math.ceil(speed * (Math.sqrt(2)/2));
        }

        if (keyHandler.rightPressed) {
            this.x += (int) Math.ceil(speed * (Math.sqrt(2)/2));
        }

        if (keyHandler.downPressed) {
            this.speed += this.speed == Utils.MAX_SPEED ? 0 : 5;
        }

        if (keyHandler.upPressed) {
            this.speed -= this.speed == 0 ? 0 : 5;
        }
    }

    public void draw(Graphics2D g2) {
        //g2.drawImage(bufferedImage, x, 570, 240, 240, null);
    }

    public int getSpeed() {
        return speed;
    }

    public void collision (ArrayList<Entity> entities) {
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
