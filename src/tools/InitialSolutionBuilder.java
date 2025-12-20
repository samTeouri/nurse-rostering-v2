package tools;

import java.util.ArrayList;
import models.Assignment;
import models.Cover;
import models.DataModel;
import models.Schedule;
import models.Shift;
import models.Staff;

public class InitialSolutionBuilder {
    
    public static Schedule build(DataModel data) {

        Schedule schedule = new Schedule();

        HardConstraintChecker hcChecker = new HardConstraintChecker(data);

        for (int day = 1; day < data.getHorizon() + 1; day++) {
            for (Shift shift : data.getShifts()) {
                int staffAssigned = 0;

                Cover cover = data.getCover(day, shift.getId());
                
                while (staffAssigned < cover.getRequirement()) { 
                    for (Staff staff : data.getStaffs()) {
                        Assignment assignment = new Assignment(day, shift, staff);

                        if (hcChecker.isAssignmentFeasible(assignment, schedule)) {
                            schedule.addAssignment(assignment);
                            staffAssigned++;

                            break;
                        }
                    }
                }
            }
        }

        return schedule;
    }
}
