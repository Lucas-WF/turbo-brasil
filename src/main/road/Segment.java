package main.road;
import java.util.ArrayList;

public class Segment {
    final private SegmentType type;
    final private ArrayList<Point> points;
    final public ArrayList<Point> generatedPoints = new ArrayList<>();

    public Segment(SegmentType type, ArrayList<Point> points) {
        this.type = type;
        this.points = points;
    }

    public SegmentType getType() {
        return type;
    }

    private Point bezier_curve(double t, Point b0, Point b1) {
        int x = (int) ((1 - t) * b0.getX() + t * b1.getX());
        int y = (int) ((1 - t) * b0.getY() + t * b1.getY());

        return new Point(x, y);
    }

    private Point bezier_curve(double t, Point b0, Point b1, Point b2, Point b3) {
        // B(t) = (1-t)³*B0 + 3t(1-t)²*B1 + 3t²(1-t)*B2 + t³B3
        double x = Math.pow((1 - t), 3) * b0.getX()
                + 3 * t * Math.pow((1 - t), 2) * b1.getX()
                + 3 * Math.pow(t, 2) * (1 - t) * b2.getX()
                + Math.pow(t, 3) * b3.getX();

        double y = Math.pow((1 - t), 3) * b0.getY()
                + 3 * t * Math.pow((1 - t), 2) * b1.getY()
                + 3 * Math.pow(t, 2) * (1 - t) * b2.getY()
                + Math.pow(t, 3) * b3.getY();

        return new Point((int) x, (int) y);
    }

    public void generatePoints() throws Exception {
        if (type == SegmentType.STRAIGHT && points.size() == 2) {
            Point start = points.get(0);
            Point end = points.get(1);

            for (int i = 0; i <= 100; ++i) {
                double t = i / 100.0;
                Point p = bezier_curve(t, start, end);
                generatedPoints.add(p);
            }
        }

        if (type == SegmentType.CURVE && points.size() == 4) {
            Point b0 = points.get(0);
            Point b1 = points.get(1);
            Point b2 = points.get(2);
            Point b3 = points.get(3);

            for (int i = 0; i <= 100; ++i) {
                double t = i / 100.0;
                Point p = bezier_curve(t, b0, b1, b2, b3);
                generatedPoints.add(p);
            }
        }

        if (points.size() < 2 || points.size() > 4 || points.size() == 3) {
            throw new Exception("Unimplemented cases");
        }
    }
}
