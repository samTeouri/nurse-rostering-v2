import java.io.IOException;

import models.Assignment;
import models.DataModel;
import models.Schedule;
import tools.DataLoader;
import tools.HardConstraintChecker;
import tools.InitialSolutionBuilder;

public class Main {
    private static int horizon;

    public static void main(String[] args) {
        try {
            DataLoader dataLoader = new DataLoader();
            DataModel dataModel = dataLoader.load("src/data/instances/Instance1.txt");
            System.out.println("Building initial solution...\n");
            Schedule schedule = InitialSolutionBuilder.build(dataModel);
            System.out.println("Initial solution built:\n");
            System.out.println(schedule);
            HardConstraintChecker hcChecker = new HardConstraintChecker(dataModel);
            if (hcChecker.isScheduleFeasible(schedule)) {
                System.out.println("The schedule satisfies all hard constraints.");
            } else {
                System.out.println("Hard-constraint report: " + hcChecker.hardConstraintReport(schedule));
                System.out.println("\n");
                for (Assignment a : schedule.getAssignments()) {
                    if (!hcChecker.checkC1(a, schedule.getAssignments())) {
                        System.out.println("\nAssignment violating Constraint 1: " + a);
                    }
                    if (!hcChecker.checkC2(a, schedule.getAssignments())) {
                        System.out.println("\nAssignment violating Constraint 2: " + a);
                    }
                    if (!hcChecker.checkC3(a, schedule.getAssignments())) {
                        System.out.println("\nAssignment violating Constraint 3: " + a);
                    }
                    if (!hcChecker.checkC4(a, schedule.getAssignments())) {
                        System.out.println("\nAssignment violating Constraint 4: " + a);
                    }
                    if (!hcChecker.checkC5(a, schedule.getAssignments())) {
                        System.out.println("\nAssignment violating Constraint 5: " + a);
                    }
                    if (!hcChecker.checkC8(a, schedule.getAssignments())) {
                        System.out.println("\nAssignment violating Constraint 8: " + a);
                    }
                    if (!hcChecker.checkC9(a)) {
                        System.out.println("\nAssignment violating Constraint 9: " + a);
                    }
                }
                System.out.println("The schedule does NOT satisfy all hard constraints.");
            }
        } catch (IOException e) {
            System.err.println("An error occurred : " + e.getMessage());
        }
    }
}