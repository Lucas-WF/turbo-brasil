package main.road;

import java.util.ArrayList;

public class Road {
    final private Point b0;
    final private Point bE;
    private final ArrayList<Segment> segments;

    public Road(Point b0, Point bE, ArrayList<Segment> sE) {
        this.b0 = b0;
        this.bE = bE;
        this.segments = sE;
    }

    public void generateSegments(ArrayList<SegmentType> types) {
        Point previousPoint = b0;
        ArrayList<Point> segmentPoints;

        for (SegmentType type : types) {
            segmentPoints = new ArrayList<>();
            segmentPoints.add(previousPoint);

            Point nextPoint;
            switch (type) {
                case STRAIGHT:
                    nextPoint = new Point(previousPoint.getX() + 100, previousPoint.getY());
                    break;
                case LEFT:
                    nextPoint = new Point(previousPoint.getX() + 100, previousPoint.getY() - 100);
                    break;
                case RIGHT:
                    nextPoint = new Point(previousPoint.getX() + 100, previousPoint.getY() + 100);
                    break;
                default:
                    nextPoint = new Point(previousPoint.getX(), previousPoint.getY());
                    break;
            }

            segmentPoints.add(nextPoint);
            segments.add(new Segment(type, segmentPoints));
            previousPoint = nextPoint;
        }

        ArrayList<Point> finalSegmentPoints = new ArrayList<>();
        finalSegmentPoints.add(previousPoint);
        finalSegmentPoints.add(bE);
        segments.add(new Segment(SegmentType.STRAIGHT, finalSegmentPoints));
    }


    public ArrayList<Segment> getSegments() {
        return segments;
    }

    public ArrayList<Point> generateRoadPoints() {
        ArrayList<Point> roadPoints = new ArrayList<>();
        for (Segment segment : segments) {
            roadPoints.addAll(segment.generatedPoints);
        }
        return roadPoints;
    }
}
