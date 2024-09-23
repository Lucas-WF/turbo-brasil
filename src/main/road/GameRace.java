    package main.road;

    import main.entity.Player;
    import main.engine.KeyHandler;

    import javax.imageio.ImageIO;
    import javax.swing.*;
    import java.awt.*;
    import java.io.FileInputStream;
    import java.io.IOException;

    public class GameRace {
        private static final int FPS = 60;
        private final int width, height;
        private KeyHandler keyHandler;
        private Player player;
        private Road road;

        private int playerX = 0;
        private int pos = 0;
        private int totalLaps = 3;
        private int completedLaps = 1;
        private boolean gameFinished = false;
        private boolean gameStarted = false;
        private int countdown = 4;
        private long lastTime;

        public GameRace(int width, int height, String title, Player player) {
            this.width = width;
            this.height = height;
            this.player = player;
            road = new Road(768, 768, 1600, 0.84);
        }

        public void init() throws IOException {
            keyHandler = new KeyHandler();
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
                player.update();
                pos += player.getSpeed();

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
            if (gameFinished) {
                showFinishScreen(graphics);
            } else {

                graphics.clearRect(0, 0, width, height);
                int camY = 1500;
                road.renderRoad(graphics, width, height, playerX, camY, pos, playerX);
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

        private void showFinishScreen(Graphics2D g) {
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, width, height);
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 48));
            g.drawString("Game Finished!", width / 2 - 150, height / 2);
        }
    }
