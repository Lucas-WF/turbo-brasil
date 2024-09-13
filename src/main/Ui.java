package main;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;

public class Ui {
    private Graphics2D g2;

    private KeyHandler keyHandler;

    private BufferedImage startButton, soloButton, multiplayerButton,exitButton,leftButton,rightButton,selectButton; // buttons
    private BufferedImage backgroundImage; // backgrounds
    private BufferedImage title1,title2,title3,title4;
    private BufferedImage car1,car2,car3,car4,car5;
    private BufferedImage m1,m2,m3;

    private int currentCarIndex = 0; // Índice para o carro atual
    private BufferedImage[] cars;

    private int currentPlayerIndex = 0;
    private BufferedImage p1,p2;
    private BufferedImage[] players;

    //title 1: TURBO BRASIL
    //title 2 : MODO`
    //title 3 : ESCOLHA SEU CARRO
    //title 4: Escolha o mapa

    Dimension screenSize;

    private int startButtonX, startButtonY = 500;
    private int soloButtonX, soloButtonY;
    private int multiplayerButtonX, multiplayerButtonY;
    private int exitButtonX,exitButtonY;
    private int leftButtonX, leftButtonY;
    private int rightButtonX, rightButtonY;
    private int selectButtonX, selectButtonY;
    private int carX, carY;
    private int m1X, m2X, m3X,mH;

    private ImageIcon backgroundGif;

    public Ui(KeyHandler keyHandler) {

        this.keyHandler = keyHandler;
        this.screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        try {
            //home
            title1 = ImageIO.read(new FileInputStream("res/titles/title1.png"));
            startButton = ImageIO.read(new FileInputStream("res/buttons/startButton.png"));
            backgroundGif = new ImageIcon("res/background/background.gif");


            //modo
            title2 = ImageIO.read(new FileInputStream("res/titles/title2.png"));
            soloButton= ImageIO.read(new FileInputStream("res/buttons/soloButton.png"));
            multiplayerButton = ImageIO.read(new FileInputStream("res/buttons/multiplayerButton.png"));
            exitButton = ImageIO.read(new FileInputStream("res/buttons/exitButton.png"));

            //solo mode
            title3 = ImageIO.read(new FileInputStream("res/titles/title3.png"));
            leftButton = ImageIO.read(new FileInputStream("res/buttons/leftButton.png"));
            rightButton = ImageIO.read(new FileInputStream("res/buttons/rightButton.png"));
            selectButton = ImageIO.read(new FileInputStream("res/buttons/selectButton.png"));

            //cars placeholder
            car1 = ImageIO.read(new FileInputStream("res/cars/car1.png"));
            car2 = ImageIO.read(new FileInputStream("res/cars/car2.png"));
            car3 = ImageIO.read(new FileInputStream("res/cars/car3.png"));
            car4 = ImageIO.read(new FileInputStream("res/cars/car4.png"));
            car5 = ImageIO.read(new FileInputStream("res/cars/car5.png"));

            //players name
            p1 = ImageIO.read(new FileInputStream("res/titles/p1.png"));
            p2 = ImageIO.read(new FileInputStream("res/titles/p2.png"));

            cars = new BufferedImage[] {car1, car2, car3, car4, car5};
            players = new BufferedImage[] {p1, p2};

            //maps selection
            title4 = ImageIO.read(new FileInputStream("res/titles/title4.png"));
            m1 = ImageIO.read(new FileInputStream("res/maps/rio.png"));
            m2 = ImageIO.read(new FileInputStream("res/maps/caatinga.png"));
            m3 = ImageIO.read(new FileInputStream("res/maps/amazonia.png"));


            //background normal
            backgroundImage = ImageIO.read(new FileInputStream("res/background/homeBackground.png"));
        } catch (IOException e) {
            throw new RuntimeException("Erro ao carregar imagem dos botões", e);
        }

    }

    public void drawMenu(Graphics2D g2, GamePanel gp) {
        this.g2 = g2;

        startButtonX = screenSize.width/2 - startButton.getWidth()/2;

        g2.drawImage(backgroundGif.getImage(), 0, 0, gp.getWidth(), gp.getHeight(), null);
        g2.drawImage(title1, screenSize.width/2 - title1.getWidth()/2 + 20, 320, title1.getWidth(), title1.getHeight(), null);
        g2.drawImage(startButton, startButtonX, startButtonY, startButton.getWidth(), startButton.getHeight(), null);
    }

    public void drawChooseGameMode(Graphics2D g2, GamePanel gp) {
        this.g2 = g2;

        soloButtonX = screenSize.width/2 + startButton.getWidth()/6 + 6;
        soloButtonY = 280 + title2.getHeight();

        multiplayerButtonX = screenSize.width/2 + multiplayerButton.getWidth()/6;
        multiplayerButtonY = 280 + title2.getHeight() + soloButton.getHeight();

        exitButtonX = screenSize.width/2 + exitButton.getWidth()/6;
        exitButtonY = 280 + title2.getHeight() + soloButton.getHeight() + multiplayerButton.getHeight();

        g2.drawImage(backgroundGif.getImage(), 0, 0, gp.getWidth(), gp.getHeight(), null);
        g2.drawImage(title2, screenSize.width/2 + 10, 260, title2.getWidth(), title2.getHeight(), null);
        g2.drawImage(soloButton, soloButtonX,soloButtonY, soloButton.getWidth(), soloButton.getHeight(), null);
        g2.drawImage(multiplayerButton,multiplayerButtonX,multiplayerButtonY, multiplayerButton.getWidth(), multiplayerButton.getHeight(), null);
        g2.drawImage(exitButton, exitButtonX, exitButtonY, exitButton.getWidth(), exitButton.getHeight(), null);
    }

    public void drawSoloMode(Graphics2D g2, GamePanel gp) {
        this.g2 = g2;

        leftButtonX = screenSize.width/2 - leftButton.getWidth()/2;

        carX = screenSize.width/2 - car1.getWidth()/2;
        carY = screenSize.height/2 - car1.getHeight()/2;

        leftButtonX = screenSize.width/2 - leftButton.getWidth() - car1.getWidth()/2;
        leftButtonY = screenSize.height/2 - leftButton.getHeight()/2;

        rightButtonX = screenSize.width/2 + car1.getWidth()/2;
        rightButtonY = screenSize.height/2 - rightButton.getHeight()/2;

        selectButtonX = screenSize.width/2 - selectButton.getWidth()/2;
        selectButtonY = screenSize.height/2 + car1.getHeight()/2;

        g2.drawImage(backgroundGif.getImage(), 0, 0, gp.getWidth(), gp.getHeight(), null);
        g2.drawImage(title3, screenSize.width/2 - title3.getWidth()/2, screenSize.height/8, title3.getWidth(), title3.getHeight(), null);
        g2.drawImage(cars[currentCarIndex], carX, carY, car1.getWidth(), car1.getHeight(), null);
        g2.drawImage(leftButton, leftButtonX, leftButtonY, leftButton.getWidth(), leftButton.getHeight(), null);
        g2.drawImage(rightButton, rightButtonX, rightButtonY, rightButton.getWidth(), rightButton.getHeight(), null);
        g2.drawImage(selectButton, selectButtonX, selectButtonY, selectButton.getWidth(), selectButton.getHeight(), null);

    }
    public void drawMultiplayerMode(Graphics2D g2, GamePanel gp) {
        this.g2 = g2;

        leftButtonX = screenSize.width/2 - leftButton.getWidth()/2;

        carX = screenSize.width/2 - car1.getWidth()/2;
        carY = screenSize.height/2 - car1.getHeight()/2;

        leftButtonX = screenSize.width/2 - leftButton.getWidth() - car1.getWidth()/2;
        leftButtonY = screenSize.height/2 - leftButton.getHeight()/2;

        rightButtonX = screenSize.width/2 + car1.getWidth()/2;
        rightButtonY = screenSize.height/2 - rightButton.getHeight()/2;

        selectButtonX = screenSize.width/2 - selectButton.getWidth()/2;
        selectButtonY = screenSize.height/2 + car1.getHeight()/2;

        g2.drawImage(backgroundGif.getImage(), 0, 0, gp.getWidth(), gp.getHeight(), null);
        g2.drawImage(players[currentPlayerIndex],screenSize.width/2 - players[currentPlayerIndex].getWidth()/2,screenSize.height/8 - title3.getHeight()/2,players[currentPlayerIndex].getWidth(),players[currentPlayerIndex].getHeight(), null);
        g2.drawImage(title3, screenSize.width/2 - title3.getWidth()/2, screenSize.height/8, title3.getWidth(), title3.getHeight(), null);
        g2.drawImage(cars[currentCarIndex], carX, carY, car1.getWidth(), car1.getHeight(), null);
        g2.drawImage(leftButton, leftButtonX, leftButtonY, leftButton.getWidth(), leftButton.getHeight(), null);
        g2.drawImage(rightButton, rightButtonX, rightButtonY, rightButton.getWidth(), rightButton.getHeight(), null);
        g2.drawImage(selectButton, selectButtonX, selectButtonY, selectButton.getWidth(), selectButton.getHeight(), null);

    }

    public void drawSelectMapMode(Graphics2D g2, GamePanel gp) {
            this.g2 = g2;

            mH = screenSize.height/8 + title4.getHeight() + 20;
            m1X = screenSize.width/6;
            m2X = screenSize.width/6 + m1.getWidth();
            m3X = screenSize.width/6 + m1.getWidth()*2;

            g2.drawImage(backgroundGif.getImage(), 0, 0, gp.getWidth(), gp.getHeight(), null);
            g2.drawImage(title4, screenSize.width/2 - title4.getWidth()/2, screenSize.height/8, title4.getWidth(), title4.getHeight(), null);
            g2.drawImage(m1,m1X,mH,m1.getWidth(),m1.getHeight(), null);
            g2.drawImage(m2,m2X,mH,m2.getWidth(),m2.getHeight(), null);
            g2.drawImage(m3,m3X,mH,m2.getWidth(),m2.getHeight(), null);
    }

    public boolean isButtonClicked(int mouseX, int mouseY, int buttonX, int buttonY, int buttonWidth, int buttonHeight) {
        return mouseX >= buttonX && mouseX <= buttonX + buttonWidth &&
                mouseY >= buttonY && mouseY <= buttonY + buttonHeight;
    }
    public boolean isStartButtonClicked(int mouseX, int mouseY) {
        return isButtonClicked(mouseX, mouseY, startButtonX, startButtonY, startButton.getWidth(), startButton.getHeight());
    }
    public boolean isSoloButtonClicked(int mouseX, int mouseY) {
        return isButtonClicked(mouseX, mouseY, soloButtonX, soloButtonY, soloButton.getWidth(), soloButton.getHeight());
    }

    public boolean isMultiplayerButtonClicked(int mouseX, int mouseY) {
        return isButtonClicked(mouseX, mouseY, multiplayerButtonX, multiplayerButtonY, multiplayerButton.getWidth(), multiplayerButton.getHeight());
    }
    public boolean isExitButtonClicked(int mouseX, int mouseY) {
        return isButtonClicked(mouseX, mouseY, exitButtonX, exitButtonY, exitButton.getWidth(), exitButton.getHeight());
    }

    public boolean isLeftButtonClicked(int mouseX, int mouseY) {
        return isButtonClicked(mouseX, mouseY, leftButtonX, leftButtonY, leftButton.getWidth(), leftButton.getHeight());
    }
    public boolean isRightButtonClicked(int mouseX, int mouseY) {
        return isButtonClicked(mouseX, mouseY, rightButtonX, rightButtonY, rightButton.getWidth(), rightButton.getHeight());
    }
    public boolean isSelectButtonClicked(int mouseX, int mouseY) {
        return isButtonClicked(mouseX, mouseY, selectButtonX, selectButtonY, selectButton.getWidth(), selectButton.getHeight());
    }

    public void handleCarSelectionClick(int mouseX, int mouseY) {
        if (isLeftButtonClicked(mouseX, mouseY)) {
            // Decrementa o índice do carro, certificando-se de que não saia dos limites
            currentCarIndex = (currentCarIndex - 1 + cars.length) % cars.length;
            System.out.println("Carro anterior selecionado: " + currentCarIndex);
        }

        if (isRightButtonClicked(mouseX, mouseY)) {
            // Incrementa o índice do carro, certificando-se de que não saia dos limites
            currentCarIndex = (currentCarIndex + 1) % cars.length;
            System.out.println("Próximo carro selecionado: " + currentCarIndex);
        }
    }
    public int handleMapSelectionClick(int mouseX, int mouseY) {
        // Verifica se clicou no mapa 1
        if (mouseX >= m1X && mouseX <= m1X + m1.getWidth() &&
                mouseY >= mH && mouseY <= mH + m1.getHeight()) {
            return 1;
        }

        // Verifica se clicou no mapa 2
        if (mouseX >= m2X && mouseX <= m2X + m2.getWidth() &&
                mouseY >= mH && mouseY <= mH + m2.getHeight()) {
            return 2;
        }

        // Verifica se clicou no mapa 3
        if (mouseX >= m3X && mouseX <= m3X + m3.getWidth() &&
                mouseY >= mH && mouseY <= mH + m3.getHeight()) {
            return 3;
        }

        // Se não clicou em nenhum mapa
        return -1;
    }
    public void confirmPlayerSelection() {
        currentPlayerIndex += 1;
    }

    public int getPlayerIndex() {
        return currentPlayerIndex;
    }

}
