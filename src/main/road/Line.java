package main.road;

public class Line {
    private double x, y, z;  // Coordenadas do mundo (não transformadas)
    private double X, Y, W;  // Coordenadas de projeção na tela (transformadas)
    private double curve;

    public void project(int camX, int camY, int camZ, double camD, int roadW, int width, int height) {
        double scale = camD / (z - camZ);
        X = (1 + scale * (x - camX)) * width / 2;
        Y = (1 - scale * (y - camY)) * height / 2;
        W = scale * roadW * width / 2;
    }

    public double getX() {
        return X;  // X projetado
    }

    public double getY() {
        return Y;  // Y projetado
    }

    public double getW() {
        return W;  // Largura projetada
    }

    public double getCurve() {
        return curve;
    }

    public double getWorldY(){
        return y;
    }
    public void setCurve(double curve) {
        this.curve = curve;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setW(double w) {
        this.W = w;
    }
}
