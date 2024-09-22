package main.entity;

import main.utils.Utils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;

public abstract class Entity {
    public int x, y;
    public int pos;
    public double screenX = Utils.SCREEN_WIDTH / 2;
    public double screenY= Utils.SCREEN_HEIGHT;
    public double speed;

    public void setBufferedImage(BufferedImage typeOfCar) {
        this.bufferedImage = typeOfCar;
    }

    public boolean willCollideHorizontally = false;
    public boolean willCollideVertically = false;
    public BufferedImage bufferedImage;

    boolean isHorizontallyAlignedTo(Entity entity) {
        double thisLeft = screenX - (double) bufferedImage.getWidth() / 2;
        double thisRight = screenX + (double) bufferedImage.getWidth() / 2;
        double entityLeft = entity.screenX - (double) entity.bufferedImage.getWidth() / 2;
        double entityRight = entity.screenX + (double) entity.bufferedImage.getWidth() / 2;

        return thisRight >= entityLeft && thisLeft <= entityRight;
    }

    boolean isVerticallyAlignedTo(Entity entity) {
        double thisTop = screenY - (double) bufferedImage.getHeight() / 2;
        double thisBottom = screenY + (double) bufferedImage.getHeight() / 2;
        double entityTop = entity.screenY - (double) entity.bufferedImage.getHeight() / 2;
        double entityBottom = entity.screenY + (double) entity.bufferedImage.getHeight() / 2;

        return thisBottom >= entityTop && thisTop <= entityBottom;
    }

    boolean isAllignedTo(String object) {
        return true;
    } // Define a class to walls and other objects


}
