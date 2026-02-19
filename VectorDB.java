import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class VectorDB {

    private Dataset trainingsObjekte = new Dataset();
    private int k;
    private Modus modus;
    private DataObject input;
    private ArrayList<Integer> letzteKNaechsteIndizes;

    private VectorDBVisualizer visualizerPanel;
    private JFrame frame;

    public enum Modus {
        EUKLIDISCH,
        KOSINUS
    }

    public VectorDB(int k, int modus) {
        if (k % 2 == 0) {
            throw new IllegalArgumentException("k muss ungerade sein!");
        }
        this.k = k;
        setModus(modus);

        letzteKNaechsteIndizes = new ArrayList<>();
        input = null;

        // Visualisierung starten
        visualizerPanel = new VectorDBVisualizer(
            trainingsObjekte.getAll(),
            input,
            letzteKNaechsteIndizes,
            this.modus
        );

        frame = new JFrame("kNN Visualisierung");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(visualizerPanel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void setModus(int modus) {
        switch (modus) {
            case 0:
                this.modus = Modus.EUKLIDISCH;
                break;
            case 1:
                this.modus = Modus.KOSINUS;
                break;
            default:
                throw new IllegalArgumentException("Ungültiger Modus!");
        }
    }

    /**
     * Fügt einen neuen Punkt hinzu (interaktiv).
     * @param sprachInteresse x-Koordinate
     * @param logikInteresse y-Koordinate
     */
    public void addData(int sprachInteresse, int logikInteresse) {
        // Temporären Punkt anlegen
        input = new DataObject(sprachInteresse, logikInteresse, null);

        // kNN berechnen und Klasse ermitteln
        String vorhersage = kNN();

        // Visualisierung aktualisieren
        visualizerPanel.setInput(input);
        visualizerPanel.setKNachbarn(letzteKNaechsteIndizes);
        visualizerPanel.repaint();

        // Benutzer fragen
        int antwort = JOptionPane.showConfirmDialog(frame,
                "Vorhersage: " + vorhersage + "\nPunkt (" + sprachInteresse + ", " + logikInteresse + ") zum Datensatz hinzufügen?",
                "Bestätigung",
                JOptionPane.YES_NO_OPTION);

        if (antwort == JOptionPane.YES_OPTION) {
            // Punkt mit vorhergesagter Klasse dauerhaft speichern
            DataObject neuerPunkt = new DataObject(sprachInteresse, logikInteresse, vorhersage);
            trainingsObjekte.getAll().add(neuerPunkt);
        }
        // Temporären Punkt entfernen
        input = null;
        letzteKNaechsteIndizes.clear();

        // Visualisierung auf den aktuellen Stand bringen
        visualizerPanel.setInput(input);
        visualizerPanel.setKNachbarn(letzteKNaechsteIndizes);
        visualizerPanel.repaint();
    }

    private String kNN() {
        List<DataObject> daten = trainingsObjekte.getAll();
        ArrayList<Double> werte = new ArrayList<>();

        // Distanzen / Ähnlichkeiten berechnen
        for (DataObject obj : daten) {
            if (modus == Modus.EUKLIDISCH)
                werte.add(euklidDistanz(input, obj));
            else
                werte.add(kosinusAehnlichkeit(input, obj));
        }

        // k nächste Nachbarn ermitteln
        letzteKNaechsteIndizes.clear();
        for (int i = 0; i < k; i++) {
            double bestValue = (modus == Modus.EUKLIDISCH) ? Double.MAX_VALUE : -Double.MAX_VALUE;
            int bestIndex = -1;

            for (int j = 0; j < werte.size(); j++) {
                boolean besser = (modus == Modus.EUKLIDISCH)
                        ? werte.get(j) < bestValue
                        : werte.get(j) > bestValue;

                if (besser && !letzteKNaechsteIndizes.contains(j)) {
                    bestValue = werte.get(j);
                    bestIndex = j;
                }
            }
            letzteKNaechsteIndizes.add(bestIndex);
        }

        // Klassen auszählen
        int interessiert = 0;
        int nichtInteressiert = 0;
        for (int index : letzteKNaechsteIndizes) {
            if (daten.get(index).getKlasse().equals("Interessiert"))
                interessiert++;
            else
                nichtInteressiert++;
        }

        return (interessiert >= nichtInteressiert) ? "Interessiert" : "Nicht interessiert";
    }

    private double euklidDistanz(DataObject a, DataObject b) {
        return Math.pow(a.getSprachInteresse() - b.getSprachInteresse(), 2)
                + Math.pow(a.getLogikInteresse() - b.getLogikInteresse(), 2);
    }

    private double kosinusAehnlichkeit(DataObject a, DataObject b) {
        double dot = a.getSprachInteresse() * b.getSprachInteresse()
                + a.getLogikInteresse() * b.getLogikInteresse();
        double normA = Math.sqrt(a.getSprachInteresse() * a.getSprachInteresse()
                + a.getLogikInteresse() * a.getLogikInteresse());
        double normB = Math.sqrt(b.getSprachInteresse() * b.getSprachInteresse()
                + b.getLogikInteresse() * b.getLogikInteresse());
        return dot / (normA * normB);
    }
}