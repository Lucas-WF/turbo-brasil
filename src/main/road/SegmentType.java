package main.road;

public enum SegmentType {
    STRAIGHT("straight"),
    HILL("hill"),
    CURVE("curve");

    public String type;
    SegmentType(String type) {
        type = type;
    }
}
