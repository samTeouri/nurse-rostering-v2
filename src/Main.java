import java.io.IOException;
import models.DataModel;
import tools.DataLoader;

public class Main {
    private static int horizon;

    public static void main(String[] args) {
        try {
            DataLoader dataLoader = new DataLoader();
            DataModel dataModel = dataLoader.load("src/data/instances/Instance1.txt");
            System.out.println(dataModel);
        } catch (IOException e) {
            System.err.println("An error occurred : " + e.getMessage());
        }
    }
}