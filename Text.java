package es.upm.dit.adsw.correlator;

import java.awt.*;

public class Text
        implements Thing {
    private final int cx;
    private final int cy;
    private final String text;

    public Text(int cx, int cy, String text) {
        this.cx = cx;
        this.cy = cy;
        this.text = text;
    }

    @Override
    public void paint(Graphics2D g) {
        FontMetrics fm = g.getFontMetrics();
        int x = cx - fm.stringWidth(text) / 2;
        int y = cy + (fm.getAscent() - fm.getDescent()) / 2;
        g.setColor(Color.BLACK);
        g.drawString(text, x, y);
    }
}
