package main.entity;

import main.utils.Utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.random.RandomGenerator;

public class Enemy extends Entity {
    private static final int carW = 240;

    public Enemy() throws IOException {
        this.x = 750;
        this.y = 0;
        this.pos = 0;
        this.speed = 0;
        this.bufferedImage = ImageIO.read(new FileInputStream("res/gameCars/Amarelo1Costas1.png"));
    }

    public void update() {
        this.speed += RandomGenerator.getDefault().nextInt(0, 5);

        synchronized (this) {
            if (this.willCollideVertically) {
                this.y -= Utils.AVOID_STEP;
                this.speed = 0;
                this.willCollideVertically = false;
            }

            if (this.willCollideHorizontally) {
                this.x -= Utils.AVOID_STEP;
                this.speed /= 2;
                this.willCollideHorizontally = false;
                return;
            }

            this.speed += this.speed == Utils.MAX_SPEED ? 0 : 5;
            this.pos += (int) speed;

        }
    }

    public void draw(Graphics2D g2) {
        int carH = 200;
        g2.drawImage(bufferedImage, x, (int) Utils.SCREEN_HEIGHT - carH - 40, carW, carH, null);
    }


    public double getSpeed() {
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
        synchronized (this) {
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
}