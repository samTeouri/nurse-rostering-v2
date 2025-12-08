package tools;

import java.util.ArrayList;
import models.Assignment;
import models.Cover;
import models.DataModel;
import models.Shift;
import models.Staff;

public class InitialSolutionBuilder {
    
    public static ArrayList<Assignment> build(DataModel data) {
        for (int day = 1; day < data.getHorizon() + 1; day++) {
            for (Shift shift : data.getShifts()) {
                int staffAssigned = 0;

                Cover cover = data.getCover(day, shift.getId());
                
                while (staffAssigned < cover.getRequirement()) { 
                    for (Staff staff : data.getStaffs()) {
                        
                    }
                }
            }
        }

        return new ArrayList<>();
    }
}
