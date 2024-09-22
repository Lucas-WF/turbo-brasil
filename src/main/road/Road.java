package main.road;

import main.engine.GamePanel;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Road {
    private List<Line> lines = new ArrayList<>();
    private final int roadWidth;
    private final int segmentLength;
    private final int numSegments;
    private final double camD; //CamDepth

    Color grass, rumble, road, midline;

    public Road(int roadWidth, int segmentLength, int numSegments, double camD) {
        this.roadWidth = roadWidth;
        this.segmentLength = segmentLength;
        this.numSegments = numSegments;
        this.camD = camD;
        generateRoad();
    }

    private void generateRoad() {
        for (int i = 0; i < numSegments; i++) {
            Line line = new Line();
            line.setZ( i * segmentLength);
            if (i > 200 && i < 700) {
                line.setCurve(4);
            }
            if (i > 1000) {
                line.setY(Math.sin(Math.toRadians(i / 30)) * 1500);
                if (line.getY() < 0) {
                    line.setY(0);
                }
            }
            lines.add(line);
        }
    }

    public List<Line> getLines() {
        return lines;
    }

    public void renderRoad(Graphics2D g, int width, int height, int camX, int camY, int pos, int playerX) {
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
                System.out.println(GamePanel.getMapSelected());
                switch(GamePanel.getMapSelected()) {
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
