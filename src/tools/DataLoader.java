package tools;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import models.Shift;
import models.Staff;

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
            }

            if (line.startsWith("SECTION_SHIFTS")) {
                ArrayList<Shift> shifts = new ArrayList<>();
                while (true) {
                    line = bufferedReader.readLine();
                    
                    if (line == null || line.equals("\n")) break;
                    if (line.startsWith("#")) continue;

                    System.out.println(line.equals("\n"));
                    
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

                    shifts.add(shift);
                }
            }

            if (line.startsWith("SECTION_STAFF")) {
                ArrayList<Staff> staffs = new ArrayList<>();
                while (true) {
                    line = bufferedReader.readLine();
                    System.out.println(line);
                    
                    if (line == null || line.equals("\n")) break;
                    if (line.startsWith("#")) continue;
                    
                    String[] staffAttributes = line.split(",");

                    Staff staff = new Staff(
                        staffAttributes[0],
                        staffAttributes.length > 7 ? Integer.parseInt(staffAttributes[2]) : 0,
                        staffAttributes.length > 3 ? Integer.parseInt(staffAttributes[3]) : 0,
                        staffAttributes.length > 4 ? Integer.parseInt(staffAttributes[4]) : 0,
                        staffAttributes.length > 5 ? Integer.parseInt(staffAttributes[5]) : 0,
                        staffAttributes.length > 6 ? Integer.parseInt(staffAttributes[6]) : 0,
                        staffAttributes.length > 7 ? Integer.parseInt(staffAttributes[7]) : 0
                    );

                    String[] maxShiftsStringFormat = staffAttributes[1].split("\\|");

                    HashMap<String, String> maxShifts = new HashMap<>();

                    for (String s : maxShiftsStringFormat) {
                        String[] pair = s.split("=");
                        staff.addMaxShift(pair[0], pair[1]);
                    }

                    System.out.println(staff);
                }
            }

        }
        bufferedReader.close();
    }
}
