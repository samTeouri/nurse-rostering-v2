package tools;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import models.Cover;
import models.DataModel;
import models.DayOff;
import models.Shift;
import models.ShiftOffRequests;
import models.ShiftOnRequests;
import models.Staff;

public class DataLoader {
    
    /**
     * @param filePath
     * @return
     * @throws FileNotFoundException
     * @throws IOException
     */
    public DataModel load(String filePath) throws FileNotFoundException, IOException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath));

        DataModel dataModel = new DataModel();

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

                dataModel.setHorizon(horizon);
            }

            if (line.startsWith("SECTION_SHIFTS")) {
                System.out.println("Loading shifts...");

                ArrayList<Shift> shifts = new ArrayList<>();
                while (true) {
                    line = bufferedReader.readLine();
                    
                    if (line == null || line.trim().isEmpty()) {
                        dataModel.setShifts(shifts.toArray(new Shift[0]));
                        break;
                    };
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
                    
                    if (line == null || line.trim().isEmpty()) {
                        dataModel.setStaffs(staffs.toArray(new Staff[0]));
                        break;
                    }
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

            if (line.startsWith("SECTION_DAYS_OFF")) {
                System.out.println("Loading days off...");

                ArrayList<DayOff> daysOffArray = new ArrayList<>();
                while (true) {
                    line = bufferedReader.readLine();
                    
                    if (line == null || line.trim().isEmpty()) {
                        dataModel.setDaysOff(daysOffArray.toArray(new DayOff[0]));
                        break;
                    }
                    if (line.startsWith("#")) continue;
                    
                    String[] daysOffAttributes = line.split(",");

                    DayOff dayOff = new DayOff(
                        daysOffAttributes[0],
                        Integer.parseInt(daysOffAttributes[1])
                    );

                    daysOffArray.add(dayOff);
                }
            }

            if (line.startsWith("SECTION_SHIFT_ON_REQUESTS")) {
                System.out.println("Loading shifts on requests...");

                ArrayList<ShiftOnRequests> shiftOnRequestsArray = new ArrayList<>();
                while (true) {
                    line = bufferedReader.readLine();
                    
                    if (line == null || line.trim().isEmpty()) {
                        dataModel.setShiftOnRequests(shiftOnRequestsArray.toArray(new ShiftOnRequests[0]));
                        break;
                    }
                    if (line.startsWith("#")) continue;
                    
                    String[] shiftAttributes = line.split(",");

                    ShiftOnRequests shiftOnRequests = new ShiftOnRequests(
                        shiftAttributes[0],
                        Integer.parseInt(shiftAttributes[1]),
                        shiftAttributes[2],
                        Integer.parseInt(shiftAttributes[3])
                    );

                    shiftOnRequestsArray.add(shiftOnRequests);
                }
            }

            if (line.startsWith("SECTION_SHIFT_OFF_REQUESTS")) {
                System.out.println("Loading shifts off requests...");

                ArrayList<ShiftOffRequests> shiftOffRequestsArray = new ArrayList<>();
                while (true) {
                    line = bufferedReader.readLine();
                    
                    if (line == null || line.trim().isEmpty()) {
                        dataModel.setShiftOffRequests(shiftOffRequestsArray.toArray(new ShiftOffRequests[0]));
                        break;
                    }
                    if (line.startsWith("#")) continue;
                    
                    String[] shiftAttributes = line.split(",");

                    ShiftOffRequests shiftOffRequests = new ShiftOffRequests(
                        shiftAttributes[0],
                        Integer.parseInt(shiftAttributes[1]),
                        shiftAttributes[2],
                        Integer.parseInt(shiftAttributes[3])
                    );

                    shiftOffRequestsArray.add(shiftOffRequests);
                }
            }

            if (line.startsWith("SECTION_COVER")) {
                System.out.println("Loading covers...");

                ArrayList<Cover> covers = new ArrayList<>();
                while (true) {
                    line = bufferedReader.readLine();
                    
                    if (line == null || line.trim().isEmpty()) {
                        dataModel.setCovers(covers.toArray(new Cover[0]));
                        break;
                    }
                    if (line.startsWith("#")) continue;
                    
                    String[] coverAttributes = line.split(",");

                    Cover cover = new Cover(
                        Integer.parseInt(coverAttributes[0]),
                        coverAttributes[1],
                        Integer.parseInt(coverAttributes[2]),
                        Integer.parseInt(coverAttributes[3]),
                        Integer.parseInt(coverAttributes[4])
                    );

                    covers.add(cover);
                }
            }

        }

        bufferedReader.close();

        System.out.println("Data loading completed.");

        return dataModel;

    }
}
