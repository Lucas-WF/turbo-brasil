package main;

import main.engine.GamePanel;
import main.utils.Utils;

import javax.swing.JFrame;

public class Main {
    public static void main(String[] args) {
        // Definindo largura, altura e título da janela do jogo
        int width = (int) Utils.SCREEN_WIDTH;
        int height = (int) Utils.SCREEN_HEIGHT;
        String title = "Pseudo 3D Racing Game";

        // Criando o GamePanel
        GamePanel gamePanel = new GamePanel(width, height, title);

        // Configurando a janela principal (JFrame)
        JFrame frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.add(gamePanel);
        frame.pack();
        frame.setLocationRelativeTo(null); // Centraliza a janela
        frame.setVisible(true);

        // Iniciar o jogo
        gamePanel.start();
    }
}
