package main.road;

import java.util.ArrayList;

public class Road {
    private final Point b0;
    private final Point bE;
    private ArrayList<Segment> segments;

    private final ArrayList<SegmentType> sTypes;

    public Road(Point b0, Point bE, ArrayList<SegmentType> sE) {
        this.b0 = b0;
        this.bE = bE;
        this.sTypes = sE;
        this.segments = new ArrayList<>();
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
            Segment segment = new Segment(type, segmentPoints);
            try {
                segment.generatePoints();
            } catch (Exception e) {
                e.printStackTrace();
            }

            segments.add(segment);
            previousPoint = nextPoint;
        }
        if (!previousPoint.equals(bE)) {
            ArrayList<Point> finalSegmentPoints = new ArrayList<>();
            finalSegmentPoints.add(previousPoint);
            finalSegmentPoints.add(bE);

            Segment finalSegment = new Segment(SegmentType.STRAIGHT, finalSegmentPoints);
            try {
                finalSegment.generatePoints();
            } catch (Exception e) {
                e.printStackTrace();
            }
            segments.add(finalSegment);
        }
    }



    public ArrayList<Segment> getSegments() {
        return segments;
    }

    public ArrayList<Point> generateRoadPoints() {
        generateSegments(sTypes);
        ArrayList<Point> roadPoints = new ArrayList<>();
        for (Segment segment : segments) {
            roadPoints.addAll(segment.generatedPoints);
        }
        return roadPoints;
    }
}
