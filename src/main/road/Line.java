package main.road;

private class Line {
    double x, y, z;
    double X, Y, W;
    double scale, curve, spriteX, clip;
    boolean drawTree = false;

    public Line() {
        curve = x = y = z = 0;
    }

    void project(int camX, int camY, int camZ) {
        scale = camD / (z - camZ);
        X = (1 + scale * (x - camX)) * width / 2;
        Y = (1 - scale * (y - camY)) * height / 2;
        W = scale * roadW * width / 2;
    }
}
