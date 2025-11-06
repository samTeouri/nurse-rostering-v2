package tools;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import models.Shift;

public class DataLoader {
    
    public void load(String filePath) throws FileNotFoundException, IOException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath));
        while (true) {
            String line = bufferedReader.readLine();
            if (line == null) break;
            if (line.startsWith("#") || line.equals("\n")) continue;

            if (line.startsWith("SECTION_HORIZON")) {
                bufferedReader.readLine();
                bufferedReader.readLine();
                line = bufferedReader.readLine();
                int horizon = Integer.parseInt(line.trim());
                System.out.println("Horizon: " + horizon);
            }

            if (line.startsWith("SECTION_SHIFTS")) {
                ArrayList<Shift> shifts = new ArrayList<>();
                while (true) {
                    line = bufferedReader.readLine();
                    System.out.println(line);
                    
                    if (line == null || line.equals("\n")) break;
                    if (line.startsWith("#")) continue;
                    
                    String[] shiftAttributes = line.split(",");

                    Shift shift = new Shift(
                        shiftAttributes[0],
                        Integer.parseInt(shiftAttributes[1])
                    );

                    if (shiftAttributes.length > 2) {
                        String[] shiftsThatcannotFollow = shiftAttributes[2].split("\\|");
                        for (String s : shiftsThatcannotFollow) {
                            shift.addShiftThatCannotFollow(s);
                        }
                    }

                    System.out.println(shift);

                    shifts.add(shift);
                }
            }

        }
        bufferedReader.close();
    }
}
