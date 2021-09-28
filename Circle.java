package es.upm.dit.adsw.correlator;

import java.awt.*;

public class Circle
        implements Thing {
    private int cx;
    private int cy;
    private int r;
    private final Color color;

    public Circle(int cx, int cy, int r, Color color) {
        this.cx = cx;
        this.cy = cy;
        this.r = r;
        this.color = color;
    }

    @Override
    public void paint(Graphics2D g) {
        if (color == null)
            return;
        g.setColor(color);
        g.fillOval(cx - r, cy - r, 2 * r, 2 * r);
    }
}
