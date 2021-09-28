package es.upm.dit.adsw.correlator;

import java.awt.*;

public class Line
        implements Thing {
    private final int x1;
    private final int y1;
    private final int x2;
    private final int y2;
    private final Color color;

    /**
     * Constructor.
     *
     * @param x1    abscisa del punto 1.
     * @param y1    ordenada del punto 1.
     * @param x2    abscisa del punto 2.
     * @param y2    ordenada del punto 2.
     * @param color color de la linea.
     */
    public Line(int x1, int y1, int x2, int y2, Color color) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.color = color;
    }

    @Override
    public void paint(Graphics2D g) {
        if (color == null)
            return;
        g.setColor(color);
        g.drawLine(x1, y1, x2, y2);
    }
}
