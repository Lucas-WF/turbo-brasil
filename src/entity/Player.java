package entity;

import main.GamePanel;
import main.KeyHandler;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;

public class Player extends Entity {
    GamePanel gp;
    KeyHandler keyH;

    public Player(GamePanel gp, KeyHandler keyH) {

        this.gp = gp;
        this.keyH = keyH;

        setDefaultValues();
        getPlayerImage();
    }

    public void setDefaultValues() {

        x = 305;
        y = 400;
        speed = 4;
        direction = "none";
    }

    public void update() {
        if(keyH.upPressed) {
            y -= speed;
        }
        else if(keyH.downPressed) {
            y += speed;
        }
        else if(keyH.leftPressed) {
            direction = "left";
            x -= speed;
        }
        else if(keyH.rightPressed) {
            direction = "right";
            x += speed;
        }
        else if (!keyH.rightPressed) {
            direction = "none";
        }
        else if (!keyH.leftPressed) {
            direction = "none";
        }
    }

    public void getPlayerImage() {
        try{
            this.right = ImageIO.read(new FileInputStream("/home/matheus/Java Projects/RacingGame/res/fusca/fuscaDireita.png"));
            this.none = ImageIO.read(new FileInputStream("/home/matheus/Java Projects/RacingGame/res/fusca/fuscaSplashArt.png"));
            this.left = ImageIO.read(new FileInputStream("/home/matheus/Java Projects/RacingGame/res/fusca/fuscEsquerda.png"));
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void draw(Graphics2D g2) {
        BufferedImage image = switch (direction) {
            case "right" -> right;
            case "left" -> left;
            default -> none;
        };

        int splashHeight = 120;
        int splashWidth = 120;

        g2.drawImage(image, x, y, splashHeight, splashWidth, null);

    }

}
