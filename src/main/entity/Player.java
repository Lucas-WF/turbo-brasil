package main.entity;

import main.engine.KeyHandler;
import main.engine.Ui;
import main.utils.Utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

public class Player extends Entity {
    final private KeyHandler keyHandler;

    private int carImageTurnBack= 0,carImageTurnRight = 0, carImageTurnLeft = 0;
    private static int carW = 240;
    private static int carH = 200;

    public Player(KeyHandler keyHandler) throws IOException {
        this.keyHandler = keyHandler;
        this.x = 650;
        this.y = 0;
        this.pos = 0;
        this.speed = 0;
    }



    public void update() {
        if(Ui.getCarNameSelected() != null) {
            try {
                if((carImageTurnBack % 12) == 0 && speed > 0) {
                    super.setBufferedImage(ImageIO.read(new FileInputStream("res/gameCars/" + Ui.getCarNameSelected() + "Costas1.png")));
                }else {
                    super.setBufferedImage(ImageIO.read(new FileInputStream("res/gameCars/" + Ui.getCarNameSelected() + "Costas2.png")));

                }

                carImageTurnBack = (carImageTurnBack > 1_000_000) ? 0 : carImageTurnBack + 1; // Para não estourar o int

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
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
                int newX = this.x - (int) Math.ceil(speed * (Math.sqrt(2) / 100));
                this.x = Math.max(newX, 0);
                try {
                    super.setBufferedImage(ImageIO.read(new FileInputStream("res/gameCars/" + Ui.getCarNameSelected() + "Esquerda.png")));

                    carImageTurnBack = (carImageTurnBack > 1_000_000) ? 0 : carImageTurnBack + 1; // Para não estourar o int

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            }

            if (keyHandler.rightPressed) {
                int newX = this.x + (int) Math.ceil(speed * (Math.sqrt(2) / 100));
                this.x = Math.min(newX, (int) Utils.SCREEN_WIDTH - carW);

                try {
                    super.setBufferedImage(ImageIO.read(new FileInputStream("res/gameCars/" + Ui.getCarNameSelected() + "Direita.png")));
                    carImageTurnBack = (carImageTurnBack > 1_000_000) ? 0 : carImageTurnBack + 1; // Para não estourar o int

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            if (this.speed < 0) {
                this.speed = 0;
            }

            if (this.x < Utils.SCREEN_WIDTH/2 - 384 - 90 || this.x > 384 + Utils.SCREEN_WIDTH/2 - carW + 90) {
                this.speed  -= speed * 3/100;
            }
        }
    }

    public void draw(Graphics2D g2) {
        g2.drawImage(bufferedImage, x, (int) Utils.SCREEN_HEIGHT - carH - 40, carW, carH, null);
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
