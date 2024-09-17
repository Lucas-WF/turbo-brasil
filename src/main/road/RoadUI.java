package main.road;

import main.road.Point;
import main.road.Road;
import main.road.Segment;

import java.awt.*;
import java.util.ArrayList;

public class RoadUI {
    private Road road;

    public RoadUI(Road road) {
        this.road = road;
    }

    public void render(Graphics2D g, int width, int height) {
        ArrayList<Point> roadPoints = road.generateRoadPoints();

        for (int i = 0; i < roadPoints.size() - 1; i++) {
            Point p1 = roadPoints.get(i);
            Point p2 = roadPoints.get(i + 1);

            g.setColor(Color.BLACK);
            g.setStroke(new BasicStroke(10));
            g.drawLine(p1.getX(), p1.getY(), p2.getX(), p2.getY());

            g.setColor(Color.WHITE);
            g.setStroke(new BasicStroke(2));
            g.drawLine((p1.getX() + p2.getX()) / 2, (p1.getY() + p2.getY()) / 2,
                    (p1.getX() + p2.getX()) / 2, (p1.getY() + p2.getY()) / 2);
        }
    }
}
