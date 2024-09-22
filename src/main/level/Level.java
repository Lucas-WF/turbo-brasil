package main.level;


import java.util.ArrayList;
import java.util.List;
import java.util.OptionalInt;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Level {
//    final private Road road;
//
//    public Level(Road road) {
//        this.road = road;
//    }
//
//    public List<Point> generateMiniMap() {
//        List<Segment> segments = road.getSegments();
//        OptionalInt index =  IntStream.range(0, segments.size()).filter(i -> segments.get(i).getType() == SegmentType.RIGHT).findFirst();
//
//        if(index.isEmpty()) {
//            return segments.stream().flatMap(segment -> segment.generatedPoints.stream()).collect(Collectors.toList());
//        }
//
//        List<Point> pointList = new ArrayList<>(segments.subList(0, index.getAsInt()).stream().flatMap(segment ->
//                segment.generatedPoints.stream()).toList());
//        for (Segment segment : segments.subList(index.getAsInt(), segments.size() - 1)) {
//            if (segment.generatedPoints.isEmpty()) {
//                continue;
//            }
//
//            int xCenter = segment.generatedPoints.get(0).getX();
//            int yCenter = segment.generatedPoints.get(0).getY();
//            double theta = -Math.PI / 2;
//
//            double cosTheta = Math.cos(theta);
//            double sinTheta = Math.sin(theta);
//            double[][] R = {
//                    {cosTheta, -sinTheta},
//                    {sinTheta, cosTheta}
//            };
//
//            for (Point point: segment.generatedPoints) {
//                int dx = point.getX() - xCenter;
//                int dy = point.getY() - yCenter;
//                int rotatedX = (int) (R[0][0] * dx + R[0][1] * dy + xCenter);
//                int rotatedY = (int) (R[1][0] * dx + R[1][1] * dy + yCenter);
//                pointList.add(new Point(rotatedX, rotatedY));
//            }
//        }
//
//        return pointList;
//    }
}
