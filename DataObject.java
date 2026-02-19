public class DataObject {
    private int sprachInteresse;
    private int logikInteresse;
    private String klasse;

    public DataObject(int sprachInteresse, int logikInteresse, String klasse) {
        this.sprachInteresse = sprachInteresse;
        this.logikInteresse = logikInteresse;
        this.klasse = klasse;
    }
    public int getSprachInteresse() {
        return sprachInteresse;
    }
    public int getLogikInteresse() {
        return logikInteresse;
    }
    public String getKlasse() {
        return klasse;
    }
}