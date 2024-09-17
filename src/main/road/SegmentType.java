package main.road;

public enum SegmentType {
    STRAIGHT("straight"),
    LEFT("hill"),
    RIGHT("right");

    public String type;
    SegmentType(String type) {
        type = type;
    }
}
