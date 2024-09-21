package main;

import main.engine.GamePanel;
import main.road.Point;
import main.road.RoadUI;
import main.road.Road;
import main.road.SegmentType;
import main.utils.Utils;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        Point b0 = new Point(0, 0);
        Point bE = new Point(200, 0);

        ArrayList<SegmentType> segmentTypes = new ArrayList<>();
        segmentTypes.add(SegmentType.STRAIGHT);

        Road road = new Road(b0, bE, segmentTypes);

        RoadUI roadUI = new RoadUI(road, (int) Utils.SCREEN_WIDTH, (int) Utils.SCREEN_HEIGHT);

        GamePanel gamePanel = new GamePanel((int) Utils.SCREEN_WIDTH, (int) Utils.SCREEN_HEIGHT, "Turbo Brasil");
        gamePanel.start();
    }
}
