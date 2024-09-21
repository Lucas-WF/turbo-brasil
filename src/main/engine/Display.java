package main.engine;

import javax.swing.*;
import java.awt.*;

public class Display {
    private JFrame frame;
    public Canvas canvas;
    public int width, height;

    public Display(int width, int height, String title) {
        this.width = width;
        this.height = height;

        frame = new JFrame();
        frame.setTitle(title);
        frame.setSize(width, height);
        frame.setExtendedState(frame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(true);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        canvas = new Canvas();
        canvas.setPreferredSize(new Dimension(width, height));
        canvas.setMinimumSize(new Dimension(width, height));
        canvas.setMaximumSize(new Dimension(width, height));
        canvas.setFocusable(true);

        frame.add(canvas);
        frame.pack();

        canvas.requestFocus();
    }

    public JFrame getFrame() {
        return frame;
    }
}
