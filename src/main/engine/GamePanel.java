package main.engine;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.io.IOException;
import main.entity.Player;
import main.road.Road;

public class GamePanel extends JPanel implements Runnable {
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

    private Road road;
    private int playerX = 0;
    private int pos = 0;
    private long downPressStartTime = -1;
    private int totalLaps = 3;
    private int completedLaps = 1;
    private boolean gameFinished = false;

    public GamePanel(int width, int height, String title) {
        this.width = width;
        this.height = height;
        this.title = title;
        road = new Road(768, 768, 1600, 0.84); // Criação da estrada
    }

    private void init() throws IOException {
        keyHandler = new KeyHandler();
        display = new Display(width, height, title);
        display.getFrame().addKeyListener(keyHandler);
        player = new Player(keyHandler);
    }

    public void tick() {
        if (gameFinished) return;

        player.update();
        pos += 500;

        if (pos >= road.getLines().size() * 768) {
            pos = 0;
            completedLaps++;
            if (completedLaps > totalLaps) {
                completedLaps = totalLaps;
                gameFinished = true;
            }
        }

        if (keyHandler.downPressed) {
            if (downPressStartTime == -1) {
                downPressStartTime = System.currentTimeMillis();
            }
            long pressedDuration = System.currentTimeMillis() - downPressStartTime;

            if (pressedDuration >= 3000) {
                pos -= Math.min(200, (pressedDuration - 3000) / 15);
                if (pos < 0) {
                    pos = road.getLines().size() * 768 - 1;
                }
            }
        } else {
            downPressStartTime = -1;
        }
    }

    public void render() {
        if (bs == null) {
            display.canvas.createBufferStrategy(3);
            bs = display.canvas.getBufferStrategy();
            return;
        }

        graphics = (Graphics2D) bs.getDrawGraphics();
        try {
            if (gameFinished) {
                showFinishScreen(graphics);
            } else {
                graphics.clearRect(0, 0, width, height);
                int camY = 1500;
                road.renderRoad(graphics, width, height, playerX, camY, pos, playerX);

                // Renderiza o jogador
                player.draw(graphics);

                // Renderiza o texto de voltas
                graphics.setColor(Color.WHITE);
                graphics.setFont(new Font("Arial", Font.BOLD, 24));
                String lapText = completedLaps + " / " + totalLaps;
                graphics.drawString(lapText, width - 100, 30);  // Ajusta a posição do texto conforme necessário
            }
        } finally {
            graphics.dispose();
        }
        bs.show();
    }



    private void showFinishScreen(Graphics2D g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, width, height);
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 48));
        g.drawString("Game Finished!", width / 2 - 150, height / 2);
    }

    @Override
    public void run() {
        try {
            init();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

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