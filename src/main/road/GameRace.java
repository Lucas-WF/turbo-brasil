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

        private final Thread leaderboardThread = new Thread(() -> {
            Entity[] carsArray = getEntities().toArray(new Entity[0]);

            Entity[] sortedCars = sortLeaderboard(carsArray, carsArray.length);
            for (int i = 0; i < sortedCars.length; ++i) {
                sortedCars[i].leaderboardPos = i + 1;
            }
        });

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
                    if (completedLaps > totalLaps) {
                        completedLaps = totalLaps;
                        gameFinished = true;
                    }
                }
            }
        }

        public void render(Graphics2D graphics) throws InterruptedException {
            showCounter(graphics, String.valueOf(countdown));

            for (Enemy enemy : enemies) {
                threadPool.submit(() -> {
                    enemy.draw(graphics);
                });
            }

            if (gameFinished) {
                leaderboardThread.join();
                threadPool.shutdown();
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

        private Entity[] sortLeaderboard(Entity[] cars, int n) {
            if (n < 2) {
                return cars;
            }

            int mid = n / 2;
            Entity[] l = new Entity[mid];
            Entity[] r = new Entity[n - mid];

            System.arraycopy(cars, 0, l, 0, mid);
            System.arraycopy(cars, mid, r, 0, n - mid);

            l = sortLeaderboard(l, mid);
            r = sortLeaderboard(r, n - mid);

            return merge(l, r);
        }

        private Entity[] merge(Entity[] l, Entity[] r) {
            int left = l.length;
            int right = r.length;
            Entity[] merged = new Entity[left + right];

            int i = 0, j = 0, k = 0;

            while (i < left && j < right) {
                if (l[i].pos <= r[j].pos) {
                    merged[k++] = l[i++];
                } else {
                    merged[k++] = r[j++];
                }
            }

            while (i < left) {
                merged[k++] = l[i++];
            }
            while (j < right) {
                merged[k++] = r[j++];
            }

            return merged;
        }

    }
