package main;

import entity.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;

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

    public GamePanel(int width, int height, String title) {
        this.width = width;
        this.height = height;
        this.title = title;
    }

    private void init() {
        keyHandler = new KeyHandler();
        display = new Display(width, height, title);
        display.getFrame().addKeyListener(keyHandler); // Attach KeyListener to frame
        player = new Player(keyHandler);
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
            player.draw(graphics);
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
