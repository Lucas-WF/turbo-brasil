package main.road;

import main.road.Point;
import main.road.Road;
import main.road.Segment;

import java.awt.*;
import java.util.ArrayList;

public class RoadUI {
    private int width, height;
    private List<GamePanel.Line> lines;
    private int roadW;
    private int segL;
    private double camD;

    public RoadUI(int width, int height, List<GamePanel.Line> lines, int roadW, int segL, double camD) {
        this.width = width;
        this.height = height;
        this.lines = lines;
        this.roadW = roadW;
        this.segL = segL;
        this.camD = camD;
    }

    public void drawRoad(Graphics2D g, int pos, int playerX, int N) {
        double x = 0, dx = 0;
        double maxY = height;
        int startPos = pos / segL;
        int camH = 1500 + (int) lines.get(startPos).y;

        for (int n = startPos; n < startPos + 300; n++) {
            GamePanel.Line l = lines.get(n % N);
            l.project(playerX - (int) x, camH, pos);
            x += dx;
            dx += l.curve;

            if (l.Y > 0 && l.Y < maxY) {
                maxY = l.Y;
                Color grass = ((n / 2) % 2) == 0 ? new Color(16, 200, 16) : new Color(0, 154, 0);
                Color rumble = ((n / 2) % 2) == 0 ? new Color(255, 255, 255) : new Color(255, 0, 0);
                Color road = Color.black;
                Color midel = ((n / 2) % 2) == 0 ? new Color(255, 255, 255) : new Color(0, 0, 0);

                GamePanel.Line p = (n == 0) ? l : lines.get((n - 1) % N);

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

    public void showFinishScreen(Graphics2D g, BufferedImage finishImage) {
        g.drawImage(finishImage, 0, 0, width, height, null);
    }
}
