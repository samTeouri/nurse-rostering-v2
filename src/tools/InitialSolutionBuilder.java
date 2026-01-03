package tools;

import models.Assignment;
import models.Cover;
import models.DataModel;
import models.Schedule;
import models.Shift;
import models.Staff;

/**
 * Classe utilitaire pour construire une solution initiale faisable au problème de nurse rostering.
 */
public class InitialSolutionBuilder {
    
    /**
     * Construit une solution initiale en respectant les contraintes du problème.
     * @param data Le modèle de données du problème
     * @return Un planning initial faisable
     */
    public static Schedule build(DataModel data) {

        Schedule schedule = new Schedule();

        HardConstraintChecker hcChecker = new HardConstraintChecker(data);

        for (int day = 0; day < data.getHorizon(); day++) {
            for (Shift shift : data.getShifts()) {

                Cover cover = data.getCover(day, shift.getId());
                int staffAssigned = 0;

                for (Staff staff : data.getStaffs()) {

                    if (staffAssigned >= cover.getRequirement()) {
                        break;
                    }

                    Assignment assignment = new Assignment(day, shift, staff);

                    if (hcChecker.isAssignmentFeasible(assignment, schedule)) {
                        schedule.addAssignment(assignment);
                        staffAssigned++;
                    }
                }
            }
        }


        return schedule;
    }
}
