package es.upm.dit.adsw.correlator;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Dibujos en pantalla.
 *
 */
public class Screen {
    private static final String TITLE = "Screen (9.3.2015)";

    private final int width;
    private final int height;

    private final JFrame frame;
    private final ArrayList<Thing> thingList = new ArrayList<Thing>();

    public Screen(int width, int height) {
        this(width, height, TITLE);
    }

    public Screen(int width, int height, String title) {
        this.width = width;
        this.height = height;
        frame = new JFrame(title);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JPanel panel = new MyJPanel();
        frame.setSize(10 + width, 30 + height);
        frame.getContentPane().add(panel, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    public void add(Thing thing) {
        synchronized (thingList) {
            thingList.add(thing);
        }
    }

    public void remove(Thing thing) {
        synchronized (thingList) {
            thingList.remove(thing);
        }
    }

    public void reset() {
        synchronized (thingList) {
            thingList.clear();
        }
    }

    public void setThingList(ArrayList<Thing> thingList) {
        synchronized (thingList) {
            this.thingList.clear();
            this.thingList.addAll(thingList);
        }
    }

    public void paint() {
        frame.repaint();
    }

    public void setDefaultCloseOperation(int op) {
        frame.setDefaultCloseOperation(op);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    private class MyJPanel
            extends JPanel {
        @Override
        public void paint(Graphics g) {
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, getWidth(), getHeight());
            synchronized (thingList) {
                for (Thing thing : thingList)
                    thing.paint((Graphics2D) g);
            }
        }
    }
}
