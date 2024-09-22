package main.utils;

import java.awt.*;

public class Utils {
    final static private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

    public static double SCREEN_WIDTH = screenSize.getWidth();

    public static double SCREEN_HEIGHT = screenSize.getHeight();

    public static int AVOID_DISTANCE = 50;

    public static int AVOID_STEP = 5;

    public static int MAX_SPEED = 500;
}
