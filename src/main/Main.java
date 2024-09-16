package main;

import main.engine.GamePanel;
import main.utils.Utils;

public class Main {
    public static void main(String[] args) {
        GamePanel gamePanel = new GamePanel((int) Utils.SCREEN_WIDTH, (int) Utils.SCREEN_HEIGHT, "Turbo Brasil");
        gamePanel.start();
    }
}