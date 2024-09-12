package main;

import java.awt.*;

public class Main {
    public static void main(String[] args) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        GamePanel gamePanel = new GamePanel((int) screenSize.getWidth(), (int) screenSize.getHeight(), "Turbo Brasil");
        gamePanel.start();
    }
}