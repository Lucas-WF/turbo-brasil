package main;

import java.awt.*;

public class Main {
    public static void main(String[] args) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        System.out.println(screenSize.getWidth() + " " + screenSize.getHeight());

        GamePanel gamePanel = new GamePanel((int) screenSize.getWidth(), (int) screenSize.getHeight(), "Turbo Brasil");
        gamePanel.start();
    }
}