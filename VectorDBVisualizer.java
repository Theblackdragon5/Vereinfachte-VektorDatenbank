import javax.swing.*;
import java.awt.*;
import java.util.List;

public class VectorDBVisualizer extends JPanel {

    private List<DataObject> trainingsObjekte;
    private DataObject input;
    private List<Integer> kNaechsteIndizes;
    private VectorDB.Modus modus;

    private final int MAX_WERT = 11;
    private final int SCALE = 50;
    private final int RAND = 80;

    public VectorDBVisualizer(List<DataObject> trainingsObjekte,DataObject input,List<Integer> kNaechsteIndizes,VectorDB.Modus modus) {
        this.trainingsObjekte = trainingsObjekte;
        this.input = input;
        this.kNaechsteIndizes = kNaechsteIndizes;
        this.modus = modus;

        setPreferredSize(new Dimension(700, 700));
        setBackground(Color.WHITE);
    }

    public void setInput(DataObject input) {
        this.input = input;
    }

    public void setKNachbarn(List<Integer> indices) {
        this.kNaechsteIndizes = indices;
    }

    public void setModus(VectorDB.Modus modus) {
        this.modus = modus;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        int breite = MAX_WERT * SCALE;
        int hoehe = MAX_WERT * SCALE;

        int nullX = RAND;
        int nullY = RAND + hoehe;

        // Gitter
        g2.setColor(Color.LIGHT_GRAY);
        for (int i = 0; i <= MAX_WERT; i++) {
            int x = nullX + i * SCALE;
            int y = nullY - i * SCALE;
            g2.drawLine(x, nullY, x, nullY - hoehe);
            g2.drawLine(nullX, y, nullX + breite, y);
        }

        // Achsen
        g2.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(2));
        g2.drawLine(nullX, nullY, nullX, nullY - hoehe);
        g2.drawLine(nullX, nullY, nullX + breite, nullY);

        
        if (input != null) {
            int xInput = nullX + input.getSprachInteresse() * SCALE;
            int yInput = nullY - input.getLogikInteresse() * SCALE;

            // Euklid
            if (modus == VectorDB.Modus.EUKLIDISCH && kNaechsteIndizes != null) {
                g2.setColor(Color.BLACK);
                g2.setStroke(new BasicStroke(2));
                for (int index : kNaechsteIndizes) {
                    DataObject nachbar = trainingsObjekte.get(index);
                    int x = nullX + nachbar.getSprachInteresse() * SCALE;
                    int y = nullY - nachbar.getLogikInteresse() * SCALE;
                    g2.drawLine(xInput, yInput, x, y);
                }
            }

            // Kosinus
            if (modus == VectorDB.Modus.KOSINUS) {
                // Roter Vektor für den Input
                g2.setColor(Color.RED);
                g2.setStroke(new BasicStroke(3));
                g2.drawLine(nullX, nullY, xInput, yInput);

                // Schwarze Vektoren für die Nachbarn
                if (kNaechsteIndizes != null) {
                    g2.setColor(Color.BLACK);
                    g2.setStroke(new BasicStroke(2));
                    for (int index : kNaechsteIndizes) {
                        DataObject nachbar = trainingsObjekte.get(index);
                        int x = nullX + nachbar.getSprachInteresse() * SCALE;
                        int y = nullY - nachbar.getLogikInteresse() * SCALE;
                        g2.drawLine(nullX, nullY, x, y);
                    }
                }
            }

            // Input-Punkt zeichnen
            g2.setColor(Color.RED);
            g2.fillOval(xInput - 8, yInput - 8, 16, 16);
        }

        // Trainingspunkte zeichnen
        for (DataObject obj : trainingsObjekte) {
            int x = nullX + obj.getSprachInteresse() * SCALE;
            int y = nullY - obj.getLogikInteresse() * SCALE;

            if (obj.getKlasse().equals("Interessiert")) {
                g2.setColor(Color.BLUE);
            } else {
                g2.setColor(Color.GREEN);
            }
            g2.fillOval(x - 6, y - 6, 12, 12);
        }
    }

    public static void show(List<DataObject> trainingsObjekte,
                            DataObject input,
                            List<Integer> kNaechsteIndizes,
                            VectorDB.Modus modus) {
        JFrame frame = new JFrame("kNN Visualisierung");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new VectorDBVisualizer(trainingsObjekte, input, kNaechsteIndizes, modus));
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}