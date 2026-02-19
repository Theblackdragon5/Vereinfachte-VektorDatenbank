import java.util.ArrayList;
import java.util.List;

public class Dataset {

    private List<DataObject> data;

    public Dataset() {
        data = new ArrayList<>();
        loadPresetData();   // automatically load training data
    }

    private void loadPresetData() {

        data.add(new DataObject(7, 7, "Interessiert"));
        data.add(new DataObject(8, 7, "Interessiert"));
        data.add(new DataObject(11, 2, "Interessiert"));
        data.add(new DataObject(11, 3, "Interessiert"));
        data.add(new DataObject(2, 11, "Interessiert"));
        data.add(new DataObject(3, 11, "Interessiert"));
        data.add(new DataObject(9, 10, "Interessiert"));
        data.add(new DataObject(11, 11, "Interessiert"));

        data.add(new DataObject(1, 10, "Nicht interessiert"));
        data.add(new DataObject(10, 1, "Nicht interessiert"));
        data.add(new DataObject(2, 8, "Nicht interessiert"));
        data.add(new DataObject(8, 2, "Nicht interessiert"));
        data.add(new DataObject(3, 4, "Nicht interessiert"));
        data.add(new DataObject(2, 3, "Nicht interessiert"));
        data.add(new DataObject(3, 2, "Nicht interessiert"));
    }

    public List<DataObject> getAll() {
        return data;
    }

    public int size() {
        return data.size();
    }

    public void print() {
        for (DataObject obj : data) {
            System.out.println(obj);
        }
    }
}
