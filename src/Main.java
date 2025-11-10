import java.io.IOException;
import tools.DataLoader;

public class Main {
    private static int horizon;

    public static void main(String[] args) {
        try {
            DataLoader dataLoader = new DataLoader();
            dataLoader.load("src/data/instances/Instance15.txt");
        } catch (IOException e) {
            System.err.println("An error occurred : " + e.getMessage());
        }
    }
}