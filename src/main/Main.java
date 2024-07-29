package main;

import main.GamePanel;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        JFrame window = new JFrame();

        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // config para fechar a janela

        window.setResizable(false); // config para não permitir mudança de tamanho

        window.setTitle("Jogo Corrida");

        window.setLocationRelativeTo(null);  // a janela vai ser aberta no centro da tela

        GamePanel gamePanel = new GamePanel();
        window.add(gamePanel);

        window.pack(); // aplica um tamanho a janela para se adaptar as preferencias de tamanho dos seus subcomponentes

        window.setVisible(true);

        gamePanel.startGameThread();
    }
}