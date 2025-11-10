package tools;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import models.Shift;
import models.ShiftOnRequests;
import models.Staff;

public class DataLoader {
    
    public void load(String filePath) throws FileNotFoundException, IOException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath));
        while (true) {
            String line = bufferedReader.readLine();
            if (line == null) break;
            if (line.startsWith("#") || line.trim().isEmpty()) continue;

            if (line.startsWith("SECTION_HORIZON")) {
                System.out.println("Loading horizon...");

                bufferedReader.readLine();
                bufferedReader.readLine();
                line = bufferedReader.readLine();
                int horizon = Integer.parseInt(line.trim());
            }

            if (line.startsWith("SECTION_SHIFTS")) {
                System.out.println("Loading shifts...");

                ArrayList<Shift> shifts = new ArrayList<>();
                while (true) {
                    line = bufferedReader.readLine();
                    
                    if (line == null || line.trim().isEmpty()) break;
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

                    shifts.add(shift);
                }
            }

            if (line.startsWith("SECTION_STAFF")) {
                System.out.println("Loading staffs...");

                ArrayList<Staff> staffs = new ArrayList<>();
                while (true) {
                    line = bufferedReader.readLine();
                    
                    if (line == null || line.trim().isEmpty()) break;
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

                    for (String s : maxShiftsStringFormat) {
                        String[] pair = s.split("=");
                        staff.addMaxShift(pair[0], pair[1]);
                    }

                    staffs.add(staff);
                }
            }

            if (line.startsWith("SECTION_SHIFT_ON_REQUESTS")) {
                System.out.println("Loading shifts on requests...");

                ArrayList<ShiftOnRequests> shiftOnRequestsArray = new ArrayList<>();
                while (true) {
                    line = bufferedReader.readLine();
                    
                    if (line == null || line.trim().isEmpty()) break;
                    if (line.startsWith("#")) continue;
                    
                    String[] shiftAttributes = line.split(",");

                    ShiftOnRequests shiftOnRequests = new ShiftOnRequests(
                        shiftAttributes[0],
                        Integer.parseInt(shiftAttributes[1]),
                        shiftAttributes[2],
                        Integer.parseInt(shiftAttributes[3])
                    );

                    shiftOnRequestsArray.add(shiftOnRequests);
                    System.out.println(shiftOnRequests);
                }
            }

        }

        bufferedReader.close();
    }
}
