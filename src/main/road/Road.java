package main.road;

import java.util.ArrayList;

public class Road {
    final private Point b0;
    final private Point bE;
    final private ArrayList<Segment> segments = new ArrayList<>();

    public Road(Point b0, Point bE) {
        this.b0 = b0;
        this.bE = bE;
    }

    public void generateSegments(ArrayList<SegmentType> types) {
        ArrayList<Point> initPoints = new ArrayList<>();
        ArrayList<Point> endPoints = new ArrayList<>();

        initPoints.add(b0);
        initPoints.add(new Point(100, 200)); // RANDOM IMPLEMENT

        endPoints.add(new Point(300, 400)); // RANDOM IMPLEMENT
        endPoints.add(bE);

        segments.add(new Segment(SegmentType.STRAIGHT, initPoints));

        for (SegmentType type : types) {
            segments.add(new Segment(type, initPoints)); // PLEASE, CHANGE THE initPoints FOR THE REAL ONES
        }

        segments.add(new Segment(SegmentType.STRAIGHT, endPoints));
    }

    public ArrayList<Segment> getSegments() {
        return segments;
    }

    public ArrayList<Point> generateRoadPoints() {
        ArrayList<Point> roadPoints = new ArrayList<>();
        for (Segment segment : segments) {
            // TODO: CALL THE GENERATE POINTS FUNC
            roadPoints.addAll(segment.generatedPoints);
        }
        return roadPoints;
    }
}
