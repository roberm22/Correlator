package es.upm.dit.adsw.correlator;

import java.util.ArrayList;
import java.util.List;

public class Linear {
    private final List<double[]> values = new ArrayList<double[]>();

    public static void main(String[] args) {
        Linear calc = new Linear();
        calc.registra(2, 1);
        calc.registra(3, 3);
        calc.registra(4, 2);
        calc.registra(4, 4);
        calc.registra(5, 4);
        calc.registra(6, 4);
        calc.registra(6, 6);
        calc.registra(7, 4);
        calc.registra(7, 6);
        calc.registra(8, 7);
        calc.registra(10, 9);
        calc.registra(10, 10);

        System.out.println("xm=  " + calc.mediaX());
        System.out.println("ym=  " + calc.mediaY());
        System.out.println("sx=  " + calc.desviacionEstandarX());
        System.out.println("sy=  " + calc.desviacionEstandarY());
        System.out.println("sxy= " + calc.covarianza());
        System.out.println("r=   " + calc.correlacion());
    }

    /**
     * Anota un punto.
     *
     * @param x valor X.
     * @param y valor Y.
     */
    public void registra(double x, double y) {
        values.add(new double[]{x, y});
    }

    public double getA() {
        double sx = desviacionEstandarX();
        double sx2 = sx * sx;
        return covarianza() / sx2;
    }

    public double getB() {
        return mediaY() - getA() * mediaX();
    }

    public double getR2() {
        double r = correlacion();
        return r * r;
    }

    /**
     * Numero de puntos.
     *
     * @return numero de puntos.
     */
    public int size() {
        return values.size();
    }

    /**
     * @return valor medio de la serie.
     */
    public double mediaX() {
        double s = 0;
        for (double[] xy : values)
            s += xy[0];
        int n = values.size();
        return s / n;
    }

    /**
     * @return valor medio de la serie.
     */
    public double mediaY() {
        double s = 0;
        for (double[] xy : values)
            s += xy[1];
        int n = values.size();
        return s / n;
    }

    /**
     * @return desviacion tipica de la serie.
     */
    public double desviacionEstandarX() {
        double xm = mediaX();
        double s2 = 0;
        for (double[] xy : values) {
            double x = xy[0];
            s2 += (x - xm) * (x - xm);
        }
        int n_1 = size() - 1;
        return Math.sqrt(s2 / n_1);
    }

    /**
     * @return desviacion tipica de la serie.
     */
    public double desviacionEstandarY() {
        double ym = mediaY();
        double s2 = 0;
        for (double[] xy : values) {
            double y = xy[1];
            s2 += (y - ym) * (y - ym);
        }
        int n_1 = size() - 1;
        return Math.sqrt(s2 / n_1);
    }

    /**
     * Calcula la covarianza Sxy de los puntos registrados.
     *
     * @return covarianza.
     */
    public double covarianza() {
        double xm = mediaX();
        double ym = mediaY();

        double s = 0;
        for (double[] xy : values)
            s += (xy[0] - xm) * (xy[1] - ym);
        int n_1 = size() - 1;
        return s / n_1;
    }

    /**
     * Calcula el indice de correlacion, r, de los puntos registrados.
     *
     * @return indice de correlacion.
     */
    public double correlacion() {
        double sx = desviacionEstandarX();
        double sy = desviacionEstandarY();
        double sxy = covarianza();
        return sxy / (sx * sy);
    }

    public double getR() {
        int n = values.size();
        double sx = 0;
        double sy = 0;
        double sx2 = 0;
        double sy2 = 0;
        double sxy = 0;
        for (double[] xy : values) {
            Double xi = xy[0];
            Double yi = xy[1];
            sx += xi;
            sy += yi;
            sx2 += xi * xi;
            sy2 += yi * yi;
            sxy += xi * yi;
        }
        return (n * sxy - sx * sy) / (Math.sqrt(n * sx2 - sx * sx) * Math.sqrt(n * sy2 - sy * sy));
    }
}
