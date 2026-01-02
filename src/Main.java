import java.io.IOException;

import models.Assignment;
import models.DataModel;
import models.Schedule;
import tools.DataLoader;
import tools.HardConstraintChecker;
import tools.InitialSolutionBuilder;
import tools.ObjectiveFunction;
import tools.VNS;

/**
 * Classe principale du projet Nurse Rostering.
 * Elle charge les données, construit une solution initiale, applique la VNS pour améliorer la solution,
 * affiche le score, le temps d'exécution et vérifie la faisabilité de la solution finale.
 */
public class Main {
    /**
     * Point d'entrée du programme.
     * Charge les données, construit une solution initiale, lance la VNS, affiche le résultat et les contraintes.
     */
    public static void main(String[] args) {
        try {
            // Chargement des données depuis le fichier d'instance
            DataLoader dataLoader = new DataLoader();
            DataModel dataModel = dataLoader.load("src/data/instances/Instance1.txt");
            System.out.println("Building initial solution...\n");
            // Construction d'une solution initiale faisable
            Schedule schedule = InitialSolutionBuilder.build(dataModel);
            System.out.println("Initial solution built:\n");
            // Application de la métaheuristique VNS pour améliorer la solution
            VNS vns = new VNS(dataModel);
            long startTime = System.currentTimeMillis();
            Schedule improvedSchedule = vns.solve(schedule, 10000);
            long endTime = System.currentTimeMillis();

            // Trier les assignments par jour
            improvedSchedule.getAssignments().sort((a1, a2) -> Integer.compare(a1.getDay(), a2.getDay()));

            // Calcul du score de la solution améliorée
            ObjectiveFunction obj = new ObjectiveFunction(dataModel);
            int score = obj.evaluate(improvedSchedule);

            System.out.println("Improved solution after VNS:\n\n" + improvedSchedule);
            System.out.println("Score de la solution : " + score);
            System.out.println("Temps d'exécution (ms) : " + (endTime - startTime));


            // Vérification de la faisabilité de la solution finale
            HardConstraintChecker hcChecker = new HardConstraintChecker(dataModel);
            if (hcChecker.isScheduleFeasible(improvedSchedule)) {
                System.out.println("The schedule satisfies all hard constraints.");
            } else {
                System.out.println("Hard-constraint report: " + hcChecker.hardConstraintReport(improvedSchedule));
                System.out.println("\n");
                for (Assignment a : improvedSchedule.getAssignments()) {
                    if (!hcChecker.checkC1(a, improvedSchedule.getAssignments())) {
                        System.out.println("\nAssignment violating Constraint 1: " + a);
                    }
                    if (!hcChecker.checkC2(a, improvedSchedule.getAssignments())) {
                        System.out.println("\nAssignment violating Constraint 2: " + a);
                    }
                    if (!hcChecker.checkC3(a, improvedSchedule.getAssignments())) {
                        System.out.println("\nAssignment violating Constraint 3: " + a);
                    }
                    if (!hcChecker.checkC4(a, improvedSchedule.getAssignments())) {
                        System.out.println("\nAssignment violating Constraint 4: " + a);
                    }
                    if (!hcChecker.checkC5(a, improvedSchedule.getAssignments())) {
                        System.out.println("\nAssignment violating Constraint 5: " + a);
                    }
                    if (!hcChecker.checkC8(a, improvedSchedule.getAssignments())) {
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