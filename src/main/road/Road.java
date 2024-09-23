package main.road;

import main.engine.GamePanel;
import main.entity.Player;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Road {
    private List<Line> lines = new ArrayList<>();
    private final int roadWidth;
    private final int segmentLength;
    private int numSegments;
    private final double camD; //CamDepth
    private BufferedImage backgroundImage;

    Color grass, rumble, road, midline;


    public Road(int roadWidth, int segmentLength, double camD) {
        this.roadWidth = roadWidth;
        this.segmentLength = segmentLength;
        this.camD = camD;
    }

    private void generateRoad() throws IOException {
        String mapSelected = GamePanel.getMapSelected();
        if (mapSelected == null) {
            throw new IllegalStateException("The selected map cannot be null");
        }
        switch (mapSelected) {
            case "Rio":
                this.numSegments = 2000;
                this.backgroundImage = ImageIO.read(new File("res/background/rioBackground.png"));
                for (int i = 0; i < numSegments; i++) {
                    Line line = new Line();
                    line.setZ(i * segmentLength);
                    if (i > 75 && i < 375 || i > 825 && i < 925 || i > 1075 && i < 1375 || i > 1825 && i < 1925) {
                        line.setCurve(4);
                    }
                    if (i > 525 && i < 625 || i > 1525 && i < 1625) {
                        line.setCurve(-4);
                    }
                    lines.add(line);
                }
                break;
            case "Caatinga":
                this.numSegments = 2200;
                this.backgroundImage = ImageIO.read(new File("res/background/caatingaBackground.png"));
                for (int i = 0; i < numSegments; i++) {
                    Line line = new Line();
                    line.setZ(i * segmentLength);
                    if (i > 100 && i < 200 || i > 400 && i < 500 || i > 1000  && i < 1375 || i > 1825 && i < 1925) {
                        line.setCurve(4);
                    }
                    if (i > 200 && i < 300 || i > 500 && i < 600) {
                        line.setCurve(-4);
                    }
                    lines.add(line);
                }
                break;
            case "Amazonia":
                this.numSegments = 2000;
                this.backgroundImage = ImageIO.read(new File("res/background/amazoniaBackground.png"));
                for (int i = 0; i < numSegments; i++) {
                    Line line = new Line();
                    line.setZ(i * segmentLength);
                    if (i > 200 && i < 300 || i > 400 && i < 500 ||i > 1400 && i < 1600 || i > 1825 && i < 1950) {
                        line.setCurve(4);
                    }
                    if (i > 300 && i < 400 || i > 900 && i < 1200 ) {
                        line.setCurve(-4);
                    }
                    lines.add(line);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown map selected: " + mapSelected);
        }
    }

    public List<Line> getLines() {
        return lines;
    }

    public void renderRoad(Graphics2D g, int width, int height, int camX, int camY, int pos, int playerX, Player player) {
        try {
            generateRoad();
            g.drawImage(backgroundImage, 0, 0, width, height / 2 + 10, null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        int startPos = pos / segmentLength;
        double x = 0, dx = 0;
        double maxY = height;
        camY = 1500 + (int) lines.get(startPos).getWorldY();
        for (int n = startPos; n < startPos + 300; n++) {
            Line currentLine = lines.get(n % numSegments);  // Pega o segmento atual
            Line previousLine = (n == 0) ? currentLine : lines.get((n - 1) % numSegments);  // Pega o segmento anterior

            currentLine.project(camX - (int) x, camY, pos, camD, roadWidth, width, height);
            x += dx;
            dx += currentLine.getCurve();

            if (currentLine.getY() > 0 && currentLine.getY() < maxY) {
                maxY = currentLine.getY();
                switch (GamePanel.getMapSelected()) {
                    case "Rio":
                        grass = ((n / 2) % 2) == 0 ? new Color(60, 120, 220) : new Color(90, 139, 227);
                        rumble = ((n / 2) % 2) == 0 ? new Color(120, 120, 120) : new Color(70, 70, 70);
                        road = ((n / 2) % 2) == 0 ? new Color(245, 245, 220) : new Color(238, 232, 170);
                        midline = ((n / 2) % 2) == 0 ? new Color(210, 210, 200) : new Color(200, 195, 140);
                        break;
                    case "Caatinga":
                        grass = ((n / 2) % 2) == 0 ? new Color(139, 69, 19) : new Color(101, 67, 33);
                        rumble = ((n / 2) % 2) == 0 ? new Color(255, 255, 255) : new Color(255, 0, 0);
                        road = Color.black;
                        midline = ((n / 2) % 2) == 0 ? new Color(255, 255, 255) : new Color(0, 0, 0);
                        break;
                    case "Amazonia":
                        grass = ((n / 2) % 2) == 0 ? new Color(16, 200, 16) : new Color(0, 154, 0);
                        rumble = ((n / 2) % 2) == 0 ? new Color(255, 255, 255) : new Color(255, 0, 0);
                        road = Color.black;
                        midline = ((n / 2) % 2) == 0 ? new Color(255, 255, 255) : new Color(0, 0, 0);
                        break;
                    default:
                        grass = ((n / 2) % 2) == 0 ? new Color(16, 200, 16) : new Color(0, 154, 0);
                        rumble = ((n / 2) % 2) == 0 ? new Color(255, 255, 255) : new Color(255, 0, 0);
                        road = Color.black;
                        midline = ((n / 2) % 2) == 0 ? new Color(255, 255, 255) : new Color(0, 0, 0);
                }

                drawQuad(g, grass, 0, (int) previousLine.getY(), width, 0, (int) currentLine.getY(), width);
                drawQuad(g, rumble, (int) previousLine.getX(), (int) previousLine.getY(), (int) (previousLine.getW() * 1.5), (int) currentLine.getX(), (int) currentLine.getY(), (int) (currentLine.getW() * 1.5));
                drawQuad(g, road, (int) previousLine.getX(), (int) previousLine.getY(), (int) (previousLine.getW() * 1.4), (int) currentLine.getX(), (int) currentLine.getY(), (int) (currentLine.getW() * 1.4));
                drawQuad(g, midline, (int) previousLine.getX(), (int) previousLine.getY(), (int) (previousLine.getW() * 0.8), (int) currentLine.getX(), (int) currentLine.getY(), (int) (currentLine.getW() * 0.8));
                drawQuad(g, road, (int) previousLine.getX(), (int) previousLine.getY(), (int) (previousLine.getW() * 0.7), (int) currentLine.getX(), (int) currentLine.getY(), (int) (currentLine.getW() * 0.7));
            }

            String mapSelected = GamePanel.getMapSelected();
            switch (mapSelected) {
                case "Rio":
                    if (startPos > 75 && startPos < 375 || startPos > 825 && startPos < 925 || startPos > 1075 && startPos < 1375 || startPos > 1825 && startPos < 1925 || startPos > 525 && startPos < 625 || startPos > 1525 && startPos < 1625) {
                        player.setCurve(player.getSpeed() * 1 / 10000);
                    } else {
                        player.setCurve(0);
                    }
                    break;
                case "Caatinga":
                    if (startPos > 100 && startPos < 200 || startPos > 400 && startPos < 500 || startPos > 1000  && startPos < 1375 || startPos > 1825 && startPos < 1925 || startPos > 200 && startPos < 300 || startPos > 500 && startPos < 600) {
                        player.setCurve(player.getSpeed() * 1 / 10000);
                    } else {
                        player.setCurve(0);
                    }
                    break;
                case "Amazonia":
                    if (startPos > 200 && startPos < 300 || startPos > 400 && startPos < 500 || startPos > 1400 && startPos < 1600 || startPos > 1825 && startPos < 1950 || startPos > 300 && startPos < 400 || startPos > 900 && startPos < 1200 ) {
                        player.setCurve(player.getSpeed() * 1 / 10000);
                    } else {
                        player.setCurve(0);
                    }
                    break;
                default:
                    break;
            }
        }
    }


    private void drawQuad(Graphics g, Color c, int x1, int y1, int w1, int x2, int y2, int w2) {
        int[] xPoints = { x1 - w1, x2 - w2, x2 + w2, x1 + w1 };
        int[] yPoints = { y1, y2, y2, y1 };
        g.setColor(c);
        g.fillPolygon(xPoints, yPoints, 4);
    }

    public int getSegmentLength() {
        return segmentLength;
    }
}
