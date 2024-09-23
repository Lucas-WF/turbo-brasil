    package main.road;

    import main.entity.Enemy;
    import main.entity.Entity;
    import main.entity.Player;

    import java.awt.*;
    import java.util.ArrayList;
    import java.util.Collections;
    import java.util.concurrent.ExecutorService;
    import java.util.concurrent.Executors;

    public class GameRace {
        private final int width, height;
        private final Player player;
        private final Enemy[] enemies;
        private final ExecutorService threadPool;
        private final Road road;

        private int pos = 0;
        private final int totalLaps = 3;
        private int completedLaps = 1;
        private boolean gameFinished = false;
        private boolean gameStarted = false;
        private int countdown = 4;
        private long lastTime;

        public GameRace(int width, int height, Player player, Enemy[] enemies) {
            this.width = width;
            this.height = height;
            this.player = player;
            this.enemies = enemies;
            this.threadPool = Executors.newFixedThreadPool(enemies.length);
            road = new Road(768, 768, 0.84);
        }

        public ArrayList<Entity> getEntities() {
            ArrayList<Entity> entities = new ArrayList<>();
            entities.add(this.player);
            Collections.addAll(entities, this.enemies);
            return entities;
        }

        public void init() {
            lastTime = System.nanoTime();
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
                    if (completedLaps > totalLaps) {
                        completedLaps = totalLaps;
                        gameFinished = true;
                    }
                }
            }
        }

        public void render(Graphics2D graphics) {
            showCounter(graphics, String.valueOf(countdown));

            for (Enemy enemy : enemies) {
                threadPool.submit(() -> {
                    enemy.draw(graphics);
                });
            }

            if (gameFinished) {
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

                if (!gameStarted) {
                    graphics.setColor(Color.BLACK);
                    graphics.setFont(new Font("Arial", Font.BOLD, 48));
                    String countdownText = String.valueOf(countdown);
                    graphics.drawString(countdownText, width / 2 - 20,height / 4);
                }
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
    }
