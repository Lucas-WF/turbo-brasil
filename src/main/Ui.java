package main;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;

public class Ui {
    private Graphics2D g2;

    private KeyHandler keyHandler;

    private BufferedImage startButton, soloButton, multiplayerButton,exitButton; // buttons
    private BufferedImage backgroundImage; // backgrounds
    private BufferedImage title1,title2;

    Dimension screenSize;

    private int startButtonX, startButtonY = 500;
    private int soloButtonX = 300, soloButtonY = 200;
    private int multiplayerButtonX = 300, multiplayerButtonY = 300;
    private int exitButtonX = 300,exitButtonY = 300;

    public Ui(KeyHandler keyHandler) {

        this.keyHandler = keyHandler;
        this.screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        try {
            startButton = ImageIO.read(new FileInputStream("res/buttons/startButton.png"));
            soloButton= ImageIO.read(new FileInputStream("res/buttons/soloButton.png"));
            multiplayerButton = ImageIO.read(new FileInputStream("res/buttons/multiplayerButton.png"));
            exitButton = ImageIO.read(new FileInputStream("res/buttons/exitButton.png"));
            title1 = ImageIO.read(new FileInputStream("res/titles/title1.png"));
            title2 = ImageIO.read(new FileInputStream("res/titles/title2.png"));
            backgroundImage = ImageIO.read(new FileInputStream("res/background/homeBackground.png"));
        } catch (IOException e) {
            throw new RuntimeException("Erro ao carregar imagem dos botões", e);
        }
        startButtonX = screenSize.width/2 - startButton.getWidth()/2;

    }

    public void drawMenu(Graphics2D g2, GamePanel gp) {
        this.g2 = g2;

        g2.drawImage(backgroundImage, 0, 0, gp.getWidth(), gp.getHeight(), null);
        g2.drawImage(title1, screenSize.width/2 - title1.getWidth()/2, 320, title1.getWidth(), title1.getHeight(), null);
        g2.drawImage(startButton, screenSize.width/2 - startButton.getWidth()/2, 500, startButton.getWidth(), startButton.getHeight(), null);
    }

    public void drawChooseGameMode(Graphics2D g2, GamePanel gp) {
        this.g2 = g2;

        g2.drawImage(backgroundImage, 0, 0, gp.getWidth(), gp.getHeight(), null);
        g2.drawImage(title2, screenSize.width/2, 320, title2.getWidth(), title2.getHeight(), null);
    }

    public boolean isButtonClicked(int mouseX, int mouseY, int buttonX, int buttonY, int buttonWidth, int buttonHeight) {
        return mouseX >= buttonX && mouseX <= buttonX + buttonWidth &&
                mouseY >= buttonY && mouseY <= buttonY + buttonHeight;
    }
    public boolean isStartButtonClicked(int mouseX, int mouseY) {
        return isButtonClicked(mouseX, mouseY, startButtonX, startButtonY, startButton.getWidth(), startButton.getHeight());
    }

}
