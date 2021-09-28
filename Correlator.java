package es.upm.dit.adsw.correlator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Calcula la correlacion en diferentes modelos.
 *
 */
public class Correlator {
    private static final String TITLE = "Correlator (2.3.2016)";
    private Locale Locale_ES = new Locale("es", "ES");
    private NumberFormat format;

    private final JTextArea dataInArea;
    private final JTextArea dataOutArea;

    private double[][] data;
    private double[][] data_log;
    private double[][] data_nlog;
    private double[][] data_pol;
    private double[][] data_exp;

    private double[][] shown_data;
    private String shown_title;

    private final JTextField a_log;
    private final JTextField b_log;
    private final JTextField r2_log;
    private final JTextField a_lin;
    private final JTextField b_lin;
    private final JTextField r2_lin;
    private final JTextField a_nlog;
    private final JTextField b_nlog;
    private final JTextField r2_nlog;
    private final JTextField a_pol;
    private final JTextField b_pol;
    private final JTextField r2_pol;
    private final JTextField a_exp;
    private final JTextField b_exp;
    private final JTextField r2_exp;
    private final JComboBox<String> formatComboBox;
    private final ArrayList<JButton> buttons;

    public static void main(String[] args) {
        JFrame frame = new JFrame(TITLE);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        new Correlator(frame);
        frame.pack();
        frame.setVisible(true);
    }

    private Correlator(JFrame frame) {
        Font font = new Font("monospaced", Font.PLAIN, 12);
        dataInArea = new JTextArea(20, 30);
        dataInArea.setFont(font);
        frame.getContentPane().add(new JScrollPane(dataInArea), BorderLayout.WEST);

        dataOutArea = new JTextArea(20, 30);
        dataOutArea.setFont(font);
        dataOutArea.setEditable(false);
        dataOutArea.setBackground(Color.CYAN);
        frame.getContentPane().add(new JScrollPane(dataOutArea), BorderLayout.EAST);

        JLabel t_complexity = new JLabel("complejidad");
        JLabel t_a = new JLabel("a", JLabel.CENTER);
        JLabel t_b = new JLabel("b", JLabel.CENTER);
        JLabel t_r2 = new JLabel("<html>r<sup>2", JLabel.CENTER);

        final JButton name_log = new JButton("O(log(n))");
        a_log = mkShower();
        b_log = mkShower();
        r2_log = mkShower();

        final JButton name_lin = new JButton("O(n)");
        a_lin = mkShower();
        b_lin = mkShower();
        r2_lin = mkShower();

        final JButton name_n_log = new JButton("O(n log(n))");
        a_nlog = mkShower();
        b_nlog = mkShower();
        r2_nlog = mkShower();

        final JButton name_pol = new JButton("<html>O(n<sup>a</sup>)");
        a_pol = mkShower();
        b_pol = mkShower();
        r2_pol = mkShower();

        final JButton name_exp = new JButton("<html>O(a<sup>n</sup>)");
        a_exp = mkShower();
        b_exp = mkShower();
        r2_exp = mkShower();

        buttons = new ArrayList<JButton>();
        buttons.add(name_log);
        buttons.add(name_lin);
        buttons.add(name_n_log);
        buttons.add(name_pol);
        buttons.add(name_exp);
        for (JButton button : buttons)
            button.setBackground(Color.WHITE);

        name_log.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (JButton button : buttons)
                    button.setBackground(Color.LIGHT_GRAY);
                name_log.setBackground(Color.CYAN);
                showData(data_log, "O(log(n))");
            }
        });
        name_lin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (JButton button : buttons)
                    button.setBackground(Color.LIGHT_GRAY);
                name_lin.setBackground(Color.CYAN);
                showData(data, "O(n)");
            }
        });
        name_n_log.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (JButton button : buttons)
                    button.setBackground(Color.LIGHT_GRAY);
                name_n_log.setBackground(Color.CYAN);
                showData(data_nlog, "O(n log(n))");
            }
        });
        name_pol.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (JButton button : buttons)
                    button.setBackground(Color.LIGHT_GRAY);
                name_pol.setBackground(Color.CYAN);
                showData(data_pol, "O(n^a)");
            }
        });
        name_exp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (JButton button : buttons)
                    button.setBackground(Color.LIGHT_GRAY);
                name_exp.setBackground(Color.CYAN);
                showData(data_exp, "O(a^n)");
            }
        });

        JPanel calc = new JPanel(new SpringLayout());
        calc.add(t_complexity);
        calc.add(t_a);
        calc.add(t_b);
        calc.add(t_r2);

        calc.add(name_log);
        calc.add(a_log);
        calc.add(b_log);
        calc.add(r2_log);

        calc.add(name_lin);
        calc.add(a_lin);
        calc.add(b_lin);
        calc.add(r2_lin);

        calc.add(name_n_log);
        calc.add(a_nlog);
        calc.add(b_nlog);
        calc.add(r2_nlog);

        calc.add(name_pol);
        calc.add(a_pol);
        calc.add(b_pol);
        calc.add(r2_pol);

        calc.add(name_exp);
        calc.add(a_exp);
        calc.add(b_exp);
        calc.add(r2_exp);

        SpringUtilities.makeCompactGrid(calc,
                6, 4, //rows, cols
                5, 5, //initialX, initialY
                5, 5);//xPad, yPad
        frame.getContentPane().add(calc, BorderLayout.CENTER);

        formatComboBox = new JComboBox<String>();
        formatComboBox.addItem("es: 1.234,56");
        formatComboBox.addItem("en: 1,234.56");
        formatComboBox.addItem("fr: 1 234,56");
        formatComboBox.setMaximumSize(formatComboBox.getPreferredSize());

        JButton resetButton = new JButton("RESET");
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                for (JButton button : buttons)
                    button.setBackground(Color.WHITE);
                reset();
            }
        });

        JButton evalButton = new JButton("EVAL");
        evalButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                for (JButton button : buttons)
                    button.setBackground(Color.WHITE);
                eval();
            }
        });

        JButton plotButton = new JButton("PLOT");
        plotButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                if (shown_data == null || shown_data.length == 0)
                    return;
                Plotter plotter = new Plotter(shown_data, shown_title);
                plotter.plotData();
                plotter.plotError();
            }
        });

        JToolBar panel = new JToolBar();
        panel.setFloatable(false);
        panel.add(resetButton);
        panel.add(Box.createHorizontalStrut(15));
        panel.add(formatComboBox);
        panel.add(Box.createGlue());
        panel.add(evalButton);
        panel.add(Box.createGlue());
        panel.add(plotButton);
        frame.getContentPane().add(panel, BorderLayout.SOUTH);
    }

    private void showData(double[][] dd, String title) {
        shown_data = dd;
        shown_title = title;
        StringBuilder builder = new StringBuilder();
        for (double[] line : dd) {
            for (double value : line)
                builder.append(value).append('\t');
            builder.append('\n');
        }
        dataOutArea.setText(builder.toString());
    }

    private JTextField mkShower() {
        JTextField field = new JTextField(10);
        field.setEditable(false);
        field.setBackground(Color.WHITE);
        field.setHorizontalAlignment(SwingConstants.CENTER);
        return field;
    }

    private void reset() {
        dataInArea.setText("");
        dataOutArea.setText("");
        a_log.setText("");
        b_log.setText("");
        r2_log.setText("");
        a_lin.setText("");
        b_lin.setText("");
        r2_lin.setText("");
        a_nlog.setText("");
        b_nlog.setText("");
        r2_nlog.setText("");
        a_pol.setText("");
        b_pol.setText("");
        r2_pol.setText("");
        a_exp.setText("");
        b_exp.setText("");
        r2_exp.setText("");
    }

    private void eval() {
        dataOutArea.setText("");

        int formatIdx = formatComboBox.getSelectedIndex();
        if (formatIdx == 0)
            format = NumberFormat.getInstance(Locale_ES);
        else if (formatIdx == 2)
            format = NumberFormat.getInstance(Locale.FRENCH);
        else
            format = NumberFormat.getInstance(Locale.ENGLISH);

        data = loadData(dataInArea.getText());
        if (!valid(data)) {
            JOptionPane.showMessageDialog(dataInArea,
                    "N filas\n2 columnas", "datos", JOptionPane.ERROR_MESSAGE);
            return;
        }

        evalLin(data);
        data_log = evalLog(data);
        data_nlog = evalNLog(data);
        data_pol = evalPol(data);
        data_exp = evalExp(data);
    }

    private void show(JTextField field, double v) {
        field.setText(String.format("%.2g", v));
    }

    private void evalLin(double[][] data) {
        Linear calculator = new Linear();
        for (double[] line : data)
            calculator.registra(line[0], line[1]);
        show(a_lin, calculator.getA());
        show(b_lin, calculator.getB());
        show(r2_lin, calculator.getR2());
    }

    private double[][] evalLog(double[][] data) {
        double[][] mData = as(data);
        Linear calculator = new Linear();
        for (int i = 0; i < data.length; i++) {
            mData[i][0] = Math.log(data[i][0]);
            mData[i][1] = data[i][1];
            calculator.registra(mData[i][0], mData[i][1]);
        }
        show(a_log, calculator.getA());
        show(b_log, calculator.getB());
        show(r2_log, calculator.getR2());
        return mData;
    }

    private double[][] evalNLog(double[][] data) {
        double[][] mData = as(data);
        Linear calculator = new Linear();
        for (int i = 0; i < data.length; i++) {
            mData[i][0] = data[i][0] * Math.log(data[i][0]);
            mData[i][1] = data[i][1];
            calculator.registra(mData[i][0], mData[i][1]);
        }
        show(a_nlog, calculator.getA());
        show(b_nlog, calculator.getB());
        show(r2_nlog, calculator.getR2());
        return mData;
    }

    private double[][] evalPol(double[][] data) {
        double[][] mData = as(data);
        Linear calculator = new Linear();
        for (int i = 0; i < data.length; i++) {
            mData[i][0] = Math.log(data[i][0]);
            mData[i][1] = Math.log(data[i][1]);
            calculator.registra(mData[i][0], mData[i][1]);
        }
        show(a_pol, calculator.getA());
        show(b_pol, Math.exp(calculator.getB()));
        show(r2_pol, calculator.getR2());
        return mData;
    }

    private double[][] evalExp(double[][] data) {
        double[][] mData = as(data);
        Linear calculator = new Linear();
        for (int i = 0; i < data.length; i++) {
            mData[i][0] = data[i][0];
            mData[i][1] = Math.log(data[i][1]);
            calculator.registra(mData[i][0], mData[i][1]);
        }
        show(a_exp, Math.exp(calculator.getA()));
        show(b_exp, Math.exp(calculator.getB()));
        show(r2_exp, calculator.getR2());
        return mData;
    }

    private boolean valid(double[][] data) {
        if (data == null)
            return false;
        if (data.length < 2)
            return false;
        for (double[] line : data) {
            if (line.length != 2)
                return false;
        }
        return true;
    }

    private double[][] as(double[][] data) {
        double[][] out = new double[data.length][];
        for (int line = 0; line < data.length; line++)
            out[line] = new double[data[line].length];
        return out;
    }

    private double[][] loadData(String text) {
        ArrayList<String> lines0 = split(text, "\n");
        ArrayList<String> lines = new ArrayList<String>();
        for (String line : lines0) {
            line = line.trim();
            if (line.length() > 0 && !line.startsWith("//"))
                lines.add(line);
        }

        double[][] data = new double[lines.size()][];
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            int sep = split(line);
            if (sep < 0)
                return null;
            data[i] = new double[2];
            data[i][0] = parse(line.substring(0, sep).trim());
            data[i][1] = parse(line.substring(sep + 1).trim());
        }
        return data;
    }

    private int split(String line) {
        for (int at = 0; at < line.length(); at++) {
            char ch = line.charAt(at);
            if (ch == ' ')
                return at;
            if (ch == '\t')
                return at;
            if (ch == ';')
                return at;
        }
        return -1;
    }

    private double parse(String s) {
        try {
            Number number = format.parse(s);
            return number.doubleValue();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(dataInArea,
                    e.getMessage(), s, JOptionPane.ERROR_MESSAGE);
        }
        return 0;
    }

    private ArrayList<String> split(String text, String seps) {
        ArrayList<String> list = new ArrayList<String>();
        int s0 = 0;
        for (int at = 0; at < text.length(); at++) {
            char ch = text.charAt(at);
            if (seps.indexOf(ch) >= 0) {
                String t1 = text.substring(s0, at).trim();
                if (t1.length() > 0)
                    list.add(t1);
                s0 = at + 1;
            }
        }
        String t1 = text.substring(s0).trim();
        if (t1.length() > 0)
            list.add(t1);
        return list;
    }
}
