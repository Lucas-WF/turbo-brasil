package main.entity;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.FileInputStream;
import java.io.IOException;

public class Enemy extends Entity {

    public Enemy() throws IOException {
        this.x = 750;
        this.y = 0;
        this.speed = 0;
        this.bufferedImage = ImageIO.read(new FileInputStream("res/fusca/monke.png"));
    }

    public void update() {
    }

    public void draw(Graphics2D g2) {
        //g2.drawImage(bufferedImage, x, 570, 240, 240, null);
    }

    public int getSpeed() {
        return speed;
    }

    public boolean tryToAvoid(Entity entity) {
        return true;
    }

}