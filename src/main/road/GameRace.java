package main.road;

import main.entity.Enemy;
import main.entity.Entity;
import main.entity.Player;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class GameRace {
    private final int width, height;
    private final Player player;
    private final Enemy[] enemies;
    private final ExecutorService threadPool;
    private final Road road;
    private final Object leaderboardLock = new Object(); 

    private boolean gameFinished = false;
    private boolean isThreadPoolActive = true; 

    private final Thread leaderboardThread = new Thread(() -> {
        synchronized (leaderboardLock) {
            while (!gameFinished) {
                Entity[] carsArray = getEntities().toArray(new Entity[0]);
                Entity[] sortedCars = sortLeaderboard(carsArray, carsArray.length);
                for (int i = 0; i < sortedCars.length; ++i) {
                    sortedCars[i].leaderboardPos = i + 1;
                }
                try {
                    leaderboardLock.wait(1000); 
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    });

    private int pos = 0;
    private final int totalLaps = 3;
    private int completedLaps = 1;
    private boolean gameStarted = false;
    private int countdown = 4;
    private long lastTime;

    public GameRace(int width, int height, Player player, Enemy[] enemies) {
        this.width = width;
        this.height = height;
        this.player = player;
        this.enemies = enemies;
        this.threadPool = Executors.newFixedThreadPool(enemies.length);
        this.road = new Road(768, 768, 0.84);
    }

    protected ArrayList<Entity> getEntities() {
        ArrayList<Entity> entities = new ArrayList<>();
        entities.add(this.player);
        Collections.addAll(entities, this.enemies);
        return entities;
    }

    public void init() {
        lastTime = System.nanoTime();
        leaderboardThread.start();
    }

    public void update() {
        if (gameFinished) return;

        if (!gameStarted) {
            long currentTime = System.nanoTime();
            if ((currentTime - lastTime) / 1_000_000_000 >= 1) {
                countdown--;
                lastTime = currentTime;
            }

            if (countdown <= 0) {
                gameStarted = true;
            }
        } else {
            for (Enemy enemy : enemies) {
                threadPool.submit(() -> {
                    enemy.update();
                    enemy.tryToAvoid(getEntities());
                });
            }

            player.update();
            player.collision(this.enemies);
            pos += (int) player.getSpeed();

            if (pos >= road.getLines().size() * 768) {
                pos = 0;
                completedLaps++;
                System.out.println(completedLaps);
                if (completedLaps == totalLaps) {
                    gameFinished = true;
                }
            }
        }
    }

    public void render(Graphics2D graphics) throws InterruptedException {
        showCounter(graphics, String.valueOf(countdown));

        if (!gameFinished) {
            for (Enemy enemy : enemies) {
                if (isThreadPoolActive) {
                    threadPool.submit(() -> {
                        enemy.draw(graphics);
                    });
                }
            }
        }

        if (gameFinished) {
            leaderboardThread.join(); 
            shutdownThreadPool();
            showFinishScreen(graphics);
        } else {
            graphics.clearRect(0, 0, width, height);
            int camY = 1500;
            int playerX = 0;
            road.renderRoad(graphics, width, height, playerX, camY, pos, playerX, player);
            player.draw(graphics);

            graphics.setColor(Color.WHITE);
            graphics.setFont(new Font("Arial", Font.BOLD, 24));
            String lapText = completedLaps + " / " + totalLaps;
            graphics.drawString(lapText, width - 100, 30);

            graphics.setColor(Color.WHITE);
            graphics.setFont(new Font("Arial", Font.BOLD, 24));
            String position = player.leaderboardPos + "st";
            graphics.drawString(position, width - 100, 50);

            if (!gameStarted) {
                graphics.setColor(Color.BLACK);
                graphics.setFont(new Font("Arial", Font.BOLD, 48));
                String countdownText = String.valueOf(countdown);
                graphics.drawString(countdownText, width / 2 - 20, height / 4);
            }
        }
    }

    private void shutdownThreadPool() throws InterruptedException {
        isThreadPoolActive = false; 
        threadPool.shutdown();
        try {
            if (!threadPool.awaitTermination(2, TimeUnit.SECONDS)) {
                List<Runnable> droppedTasks = threadPool.shutdownNow();
                System.out.println("Dropped tasks: " + droppedTasks.size());
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void showCounter(Graphics2D g, String counter) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, width, height);
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 48));
        g.drawString(counter, width / 2 - 150, height / 2);
    }

    private void showFinishScreen(Graphics2D g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, width, height);
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 48));
        g.drawString("Game Finished!", width / 2 - 150, height / 2);
    }

    private Entity[] sortLeaderboard(Entity[] cars, int n) {
        if (n < 2) {
            return cars;
        }

        int mid = n / 2;
        Entity[] left = new Entity[mid];
        Entity[] right = new Entity[n - mid];

        System.arraycopy(cars, 0, left, 0, mid);
        System.arraycopy(cars, mid, right, 0, n - mid);

        left = sortLeaderboard(left, mid);
        right = sortLeaderboard(right, n - mid);

        return merge(left, right);
    }

    private Entity[] merge(Entity[] left, Entity[] right) {
        int leftLength = left.length;
        int rightLength = right.length;
        Entity[] merged = new Entity[leftLength + rightLength];

        int i = 0, j = 0, k = 0;

        while (i < leftLength && j < rightLength) {
            if (left[i].pos <= right[j].pos) {
                merged[k++] = left[i++];
            } else {
                merged[k++] = right[j++];
            }
        }

        while (i < leftLength) {
            merged[k++] = left[i++];
        }
        while (j < rightLength) {
            merged[k++] = right[j++];
        }

        return merged;
    }
}
