package main.engine;

import main.entity.Player;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.List;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import javax.imageio.ImageIO; // Adicionado import

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

    private List<Line> lines = new ArrayList<>();
    private int roadW = 768;
    private int segL = 768;
    private double camD = 0.84;
    private int N;
    private int playerX = 0;
    private int pos = 0;
    private long downPressStartTime = -1;
    private int totalLaps = 3;
    private int completedLaps = 1;
    private boolean gameFinished = false;
    private BufferedImage finishImage;

    public GamePanel(int width, int height, String title) {
        this.width = width;
        this.height = height;
        this.title = title;
        initRoad();
    }

    private void loadFinishImage() {
        try {
            finishImage = ImageIO.read(new FileInputStream("res/finish_screen.png")); // Substitua pelo caminho da sua imagem
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void init() throws IOException {
        keyHandler = new KeyHandler();
        display = new Display(width, height, title);
        display.getFrame().addKeyListener(keyHandler);
        player = new Player(keyHandler);
        loadFinishImage();
    }

    private void initRoad() {
        for (int i = 0; i < 1600; i++) {
            Line line = new Line();
            line.z = i * segL;
            if (i > 200 && i < 700) {
                line.curve = 4;
            }
            if (i > 1000) {
                line.y = Math.sin(Math.toRadians(i / 30)) * 1500;
                if (line.y < 0) {
                    line.y = 0;
                }
            }
            if (i % 2 == 0) {
                line.spriteX = -2.5;
                line.drawTree = true;
            }
            lines.add(line);
        }
        N = lines.size();
    }

    private void tick() {
        if (gameFinished) return;

        player.update();

        pos += 500;

        if (pos >= segL * N) {
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
                    pos = segL * (N - 1);
                }
            }
        } else {
            downPressStartTime = -1;
        }
    }

    private void render() {
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
                drawRoad(graphics);
                player.draw(graphics);

                graphics.setColor(Color.WHITE);
                graphics.setFont(new Font("Arial", Font.BOLD, 24));
                String lapText = completedLaps + " / " + totalLaps;
                graphics.drawString(lapText, width - 100, 30); // Ajuste a posição conforme necessário
            }
        } finally {
            graphics.dispose();
        }
        bs.show();
    }

    private void showFinishScreen(Graphics2D g) {
        g.drawImage(finishImage, 0, 0, width, height, null);
    }

    private void drawRoad(Graphics2D g) {
        int startPos = pos / segL;
        double x = 0, dx = 0;
        double maxY = height;
        int camH = 1500 + (int) lines.get(startPos).y;

        for (int n = startPos; n < startPos + 300; n++) {
            Line l = lines.get(n % N);
            l.project(playerX - (int) x, camH, pos);
            x += dx;
            dx += l.curve;

            if (l.Y > 0 && l.Y < maxY) {
                maxY = l.Y;
                Color grass = ((n / 2) % 2) == 0 ? new Color(16, 200, 16) : new Color(0, 154, 0);
                Color rumble = ((n / 2) % 2) == 0 ? new Color(255, 255, 255) : new Color(255, 0, 0);
                Color road = Color.black;
                Color midel = ((n / 2) % 2) == 0 ? new Color(255, 255, 255) : new Color(0, 0, 0);

                Line p = (n == 0) ? l : lines.get((n - 1) % N);

                drawQuad(g, grass, 0, (int) p.Y, width, 0, (int) l.Y, width);
                drawQuad(g, rumble, (int) p.X, (int) p.Y, (int) (p.W * 1.5), (int) l.X, (int) l.Y, (int) (l.W * 1.5));
                drawQuad(g, road, (int) p.X, (int) p.Y, (int) (p.W * 1.4), (int) l.X, (int) l.Y, (int) (l.W * 1.4));
                drawQuad(g, midel, (int) p.X, (int) p.Y, (int) (p.W * 0.8), (int) l.X, (int) l.Y, (int) (l.W * 0.8));
                drawQuad(g, road, (int) p.X, (int) p.Y, (int) (p.W * 0.7), (int) l.X, (int) l.Y, (int) (l.W * 0.7));
            }
        }
    }

    private void drawQuad(Graphics g, Color c, int x1, int y1, int w1, int x2, int y2, int w2) {
        int[] xPoints = { x1 - w1, x2 - w2, x2 + w2, x1 + w1 };
        int[] yPoints = { y1, y2, y2, y1 };
        g.setColor(c);
        g.fillPolygon(xPoints, yPoints, 4);
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

    private class Line {
        double x, y, z;
        double X, Y, W;
        double scale, curve, spriteX, clip;
        boolean drawTree = false;

        public Line() {
            curve = x = y = z = 0;
        }

        void project(int camX, int camY, int camZ) {
            scale = camD / (z - camZ);
            X = (1 + scale * (x - camX)) * width / 2;
            Y = (1 - scale * (y - camY)) * height / 2;
            W = scale * roadW * width / 2;
        }
    }
}