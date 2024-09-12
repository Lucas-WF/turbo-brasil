package main;

import entity.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;


public class GamePanel extends JPanel implements Runnable{
    private static final int FPS = 60;
    private static final double TIME_PER_TICK = 1e9 / FPS; // Nanoseconds per frame

    private final int width, height;
    private final String title;
    private Display display;

    private Thread thread;
    private boolean running = false;

    private BufferStrategy bs;
    private Graphics2D graphics;
    private KeyHandler keyHandler;

    private Player player;

    // Teste para a troca de telas
    private static int gameState = 0;
    private final int titleState = 0;

    private final int gameMode = 1;

    public static void setGameState(int gameState) {
        GamePanel.gameState = gameState;
    }

    public static int getGameState() {
        return gameState;
    }

    public int getGameMode() {
        return gameMode;
    }

    public int getTitleState() {
        return titleState;
    }

    private Ui ui;

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }
/// ---
///
    public GamePanel(int width, int height, String title) {
        this.width = width;
        this.height = height;
        this.title = title;
        display = new Display(width, height, title);
        display.canvas.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int mouseX = e.getX();
                int mouseY = e.getY();
                System.out.println("Mouse Position: " + mouseX + ", " + mouseY);

                if (ui.isStartButtonClicked(mouseX, mouseY) && gameState == 0) {
                    System.out.println("Botão Start clicado! Indo para a seleção de modo de jogo.");
                    gameState = gameState + 1;
                }


            }
        });
    }

    private void init() {
        keyHandler = new KeyHandler(this);
        display.getFrame().addKeyListener(keyHandler); // Attach KeyListener to frame
        player = new Player(keyHandler);
        ui = new Ui(keyHandler);
    }

    private void tick() {
        player.update();
    }

    private void render() {
        if (bs == null) {
            display.canvas.createBufferStrategy(3);
            bs = display.canvas.getBufferStrategy();
            return;
        }

        graphics = (Graphics2D) bs.getDrawGraphics();
        try {
            graphics.clearRect(0, 0, width, height);

            switch (gameState){
                case 0:
                    ui.drawMenu(graphics, this); // Tela Inicial
                    break;
                case 1:
                    ui.drawChooseGameMode(graphics, this);  // Escolha do modo de jogo
                    break;
                default:
                    ui.drawMenu(graphics, this);
                    break;
            }
        } finally {
            graphics.dispose();
        }
        bs.show();
    }

    @Override
    public void run() {
        init();

        long lastTime = System.nanoTime();
        double delta = 0;

        while (running) {
            long now = System.nanoTime();
            delta += (now - lastTime) / TIME_PER_TICK;
            lastTime = now;

            if (delta >= 1) {
                tick();
                render();
                delta--;
            }
        }

        stop();
    }

    public synchronized void start() {
        if (running) return;
        running = true;
        thread = new Thread(this);
        thread.start();
    }

    public synchronized void stop() {
        if (!running) return;
        running = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
