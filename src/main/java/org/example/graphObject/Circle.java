package org.example.graphObject;

import java.awt.*;
import java.awt.geom.Ellipse2D;

public class Circle {
    final int id;
    final double x;
    final double y;
    final int size;

    final String[] row;

    public int getId() {
        return id;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public int getSize() {
        return size;
    }

    public String[] getRow() {
        return row;
    }

    public Circle(double x, double y, int size, String[] row, int id) {
        this.x = x;
        this.y = y;
        this.size = size;
        this.row = row;
        this.id = id;
    }

    public boolean isInCircle() {
        return true;
    }

    public Graphics2D createCircle(Graphics graphics) {
        Graphics2D graphics2D = (Graphics2D) graphics;
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.setColor(Color.WHITE);
        graphics2D.draw(new Ellipse2D.Double(x, y, size, size));

        return graphics2D;
    }

}