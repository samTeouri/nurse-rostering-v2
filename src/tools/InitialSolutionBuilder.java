package tools;

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

        for (int day = 0; day < data.getHorizon(); day++) {
            for (Shift shift : data.getShifts()) {
                int staffAssigned = 0;

                Cover cover = data.getCover(day, shift.getId());
                for (Staff staff : data.getStaffs()) {
                    Assignment assignment = new Assignment(day, shift, staff);

                    if (hcChecker.isAssignmentFeasible(assignment, schedule)) {
                        schedule.addAssignment(assignment);
                        staffAssigned++;
                        if (staffAssigned >= cover.getRequirement()) {
                            break;
                        }
                    }
                }
                if (!hcChecker.isScheduleFeasible(schedule)) {
                    for (Assignment a : schedule.getAssignments()) {
                        if (!hcChecker.checkC1(a, schedule.getAssignments())
                            || !hcChecker.checkC2(a, schedule.getAssignments())
                            || !hcChecker.checkC3(a, schedule.getAssignments())
                            || !hcChecker.checkC4(a, schedule.getAssignments())
                            || !hcChecker.checkC5(a, schedule.getAssignments())
                            || !hcChecker.checkC8(a, schedule.getAssignments())
                            || !hcChecker.checkC9(a)) {
                            schedule.getAssignments().remove(a);
                        }
                    }
                }
            }
        }

        return schedule;
    }
}
