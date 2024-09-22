package main;

import main.engine.GamePanel;
import main.utils.Utils;

import javax.swing.JFrame;

public class Main {
    public static void main(String[] args) {
        int width = (int) Utils.SCREEN_WIDTH;
        int height = (int) Utils.SCREEN_HEIGHT;
        GamePanel gamePanel = new GamePanel((int) width, (int) height, "Turbo Brasil");
        gamePanel.start();
    }
}
