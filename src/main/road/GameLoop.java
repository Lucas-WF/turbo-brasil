package main.road;

import main.engine.GamePanel;

public class GameLoop implements Runnable {
    private boolean running = false;
    private final int FPS = 60;
    private final GamePanel gamePanel;

    public GameLoop(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    @Override
    public void run() {
        long lastTime = System.nanoTime();
        double nsPerTick = 1e9 / FPS;
        double delta = 0;

        while (running) {
            long now = System.nanoTime();
            delta += (now - lastTime) / nsPerTick;
            lastTime = now;

            if (delta >= 1) {
                gamePanel.tick();
                gamePanel.render();
                delta--;
            }
        }
    }

    public void start() {
        running = true;
        new Thread(this).start();
    }

    public void stop() {
        running = false;
    }
}
