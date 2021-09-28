package es.upm.dit.adsw.correlator;

import javax.swing.*;
import java.awt.*;

public class Plotter {
    private static final int WIDTH = 400;
    private static final int HEIGHT = 400;
    private static final int MARGIN = 20;

    private final Screen screen;

    private final double[][] data;
    private Linear linear;

    public Plotter(double[][] data, String title) {
        this.data = data;
        screen = new Screen(2 * WIDTH, HEIGHT, title);
        screen.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        // calcula la recta de regresion lineal
        linear = new Linear();
        for (double[] point : data)
            linear.registra(point[0], point[1]);
    }


    public void plotData() {
        screen.add(new Text(WIDTH / 4, MARGIN, "datos"));
        Transform t = new Transform(data);

        // eje X ... si se ve
        int px0 = t.px(0);
        if (MARGIN < px0 && px0 < WIDTH - MARGIN)
            screen.add(new Line(px0, MARGIN, px0, HEIGHT - MARGIN, Color.BLACK));
        else
            screen.add(new Line(WIDTH / 2, MARGIN, WIDTH / 2, HEIGHT - MARGIN, Color.BLACK));

        // eje Y ... si se ve
        int py0 = t.py(0);
        if (MARGIN < py0 && py0 < HEIGHT - MARGIN)
            screen.add(new Line(MARGIN, py0, WIDTH - MARGIN, py0, Color.BLACK));
        else
            screen.add(new Line(MARGIN, HEIGHT / 2, WIDTH - MARGIN, HEIGHT / 2, Color.BLACK));

        // recta de regresion lineal
        double a = linear.getA();
        double b = linear.getB();
        double y_min_reg = a * t.x_min + b;
        double y_max_reg = a * t.x_max + b;
        screen.add(new Line(t.px(t.x_min), t.py(y_min_reg),
                t.px(t.x_max), t.py(y_max_reg),
                Color.ORANGE));

        for (double[] point : data) {
            int px = t.px(point[0]);
            int py = t.py(point[1]);
            screen.add(new Circle(px, py, 3, Color.RED));
        }

        screen.paint();
    }

    public void plotError() {
        screen.add(new Text(WIDTH + WIDTH / 4, MARGIN, "errores"));
        screen.add(new Line(WIDTH + WIDTH / 2, MARGIN, WIDTH + WIDTH / 2, HEIGHT - MARGIN, Color.BLUE));
        screen.add(new Line(WIDTH + MARGIN, HEIGHT / 2, 2 * WIDTH - MARGIN, HEIGHT / 2, Color.BLUE));

        double datum_max = 0;
        double error_max = 0;
        double a = linear.getA();
        double b = linear.getB();
        double[][] error = new double[data.length][2];
        for (int i = 0; i < data.length; i++) {
            error[i][0] = data[i][0];
            error[i][1] = data[i][1] - (a * data[i][0] + b);
//            double err = error[i][1] / data[i][1];
            if (Math.abs(error[i][1]) > Math.abs(error_max)) {
                datum_max = data[i][1];
                error_max = error[i][1];
            }
        }
        // dump(error);
        screen.add(new Text(WIDTH + WIDTH * 3 / 4, MARGIN,
                String.format("max: %4.2f%%", 100 * error_max / datum_max)));

        Transform t = new Transform(error);
        t.setY0();
        for (double[] point : error) {
            int px = WIDTH + t.px(point[0]);
            int py = t.py(point[1] / 2);    // escala 1:2 para que se vea mejor
            screen.add(new Crosshair(px, py, 4, Color.BLUE));
        }

        screen.paint();
    }

    private void dump(double[][] data) {
        for (double[] point : data)
            System.out.printf("%10.2g %10.2g%n", point[0], point[1]);
    }

    private class Transform {
        private double x_min;
        private double x_max;
        private double y_min;
        private double y_max;

        private double escalaX;
        private double escalaY;

        public Transform(double[][] data) {
            x_min = data[0][0];
            y_min = data[0][1];
            x_max = x_min;
            y_max = y_min;
            for (double[] point : data) {
                x_min = Math.min(x_min, point[0]);
                y_min = Math.min(y_min, point[1]);
                x_max = Math.max(x_max, point[0]);
                y_max = Math.max(y_max, point[1]);
            }
            setParams();
        }

        public void setParams() {
            double ancho = WIDTH - 2 * MARGIN;
            double alto = HEIGHT - 2 * MARGIN;
            escalaX = ancho / (x_max - x_min);
            escalaY = alto / (y_max - y_min);
        }

        /**
         * Centra el eje X para que los valores de Y queden alrededor de 0.
         */
        public void setY0() {
            if (y_max > -y_min) {
                y_min = -y_max;
                setParams();
            } else if (-y_min > y_max) {
                y_max = -y_min;
                setParams();
            }
        }

        /**
         * Conversion de coordenadas en el eje X.
         *
         * @param x valor del usuario.
         * @return posicion en pixels.
         */
        private int px(double x) {
//        return MARGIN + (int) (x0 + (escalaX * x));
            return MARGIN + (int) (escalaX * (x - x_min));
        }

        /**
         * Conversion de coordenadas en el eje Y.
         *
         * @param y valor del usuario.
         * @return posicion en pixels.
         */
        private int py(double y) {
//        return MARGIN + (int) (y0 - (escalaY * y));
            return HEIGHT - MARGIN - (int) (escalaY * (y - y_min));
        }
    }
}
