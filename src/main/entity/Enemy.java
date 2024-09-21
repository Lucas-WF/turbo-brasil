package main.entity;

import main.utils.Utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

public class Enemy extends Entity {
    public Enemy() throws IOException {
        this.x = 750;
        this.y = 0;
        this.pos = 0;
        this.speed = 0;
        this.bufferedImage = ImageIO.read(new FileInputStream("res/fusca/monke.png"));
    }

    public void start() {
        this.speed = 5;
    }

    public void update() {
        if (this.willCollideVertically) {
            this.y -= Utils.AVOID_STEP;
            this.speed = 0;
        }

        if (this.willCollideHorizontally) {
            this.x -= Utils.AVOID_STEP;
            this.speed /= 2;
            return;
        }

        this.speed += this.speed == Utils.MAX_SPEED ? 0 : 5;
        this.pos += speed;
    }

    public void draw(Graphics2D g2) {
        //g2.drawImage(bufferedImage, x, 570, 240, 240, null);
    }

    public int getSpeed() {
        return speed;
    }

    private void moveVerticallyToAvoid(float verticalDistance) {
        if (verticalDistance > 0) {
            this.y -= Utils.AVOID_STEP;
        } else {
            this.y += Utils.AVOID_STEP;
        }
    }

    private void moveHorizontallyToAvoid(float horizontalDistance) {
        if (horizontalDistance > 0) {
            this.x -= Utils.AVOID_STEP;
        } else {
            this.x += Utils.AVOID_STEP;
        }
    }

    public void tryToAvoid(ArrayList<Entity> entities) {
        float verticalDistance;
        float horizontalDistance;

        for (Entity entity : entities) {
            verticalDistance = entity.y + ((float) entity.bufferedImage.getHeight() / 2) -
                    this.y +  ((float) this.bufferedImage.getHeight() / 2);
            horizontalDistance = entity.x + ((float) entity.bufferedImage.getWidth() / 2) -
                    this.x +  ((float) this.bufferedImage.getWidth() / 2);

            if (isHorizontallyAlignedTo(entity) && isVerticallyAlignedTo(entity)) {
                if (verticalDistance <= 0) {
                    this.willCollideVertically = true;
                }

                if (horizontalDistance <= 0) {
                    this.willCollideHorizontally = true;
                }

                continue;
            }

            if (isHorizontallyAlignedTo(entity)) {
                if (Math.abs(verticalDistance) <= Utils.AVOID_DISTANCE) {
                    moveVerticallyToAvoid(verticalDistance);
                }
            }

            if (isVerticallyAlignedTo(entity)) {
                if (Math.abs(horizontalDistance) <= Utils.AVOID_DISTANCE) {
                    moveHorizontallyToAvoid(horizontalDistance);
                }
            }
        }
    }
}