package tools;

import java.util.*;

import models.*;

/**
 * Classe implémentant la métaheuristique VNS (Variable Neighborhood Search)
 * pour améliorer une solution de nurse rostering.
 * Elle propose plusieurs types de voisinages et utilise une mémoire tabou pour éviter les cycles.
 */
public class VNS {

    private final DataModel data;
    private final HardConstraintChecker hc;
    private final ObjectiveFunction objectiveFunction;

    /**
     * Constructeur de la classe VNS.
     * Initialise les outils nécessaires à l'évaluation et la vérification des solutions.
     * @param data Le modèle de données du problème
     */
    public VNS(DataModel data) {
        this.data = data;
        this.hc = new HardConstraintChecker(data);
        this.objectiveFunction = new ObjectiveFunction(data);
    }

    /**
     * Applique la recherche VNS pour améliorer une solution initiale.
     * Explore différents voisinages et utilise une mémoire tabou pour éviter les cycles.
     * @param initial La solution initiale
     * @param maxIterations Le nombre maximal d'itérations
     * @return La meilleure solution trouvée
     */
    public Schedule solve(Schedule initial, int maxIterations) {
        // On part d'une copie profonde de la solution initiale
        Schedule best = initial.deepCopy();
        // Score de la meilleure solution courante
        int bestScore = objectiveFunction.evaluate(best);
        // Mémoire tabou pour éviter de revisiter les mêmes solutions (utilise le hash du planning)
        Set<Integer> tabuMemory = new HashSet<>();
        tabuMemory.add(best.hashCode());

        int nbNeighborhoods = 4; // Nombre de voisinages différents

        // Boucle principale de la VNS
        for (int iter = 0; iter < maxIterations; iter++) {
            for (int k = 1; k <= nbNeighborhoods; k++) {
                // Génère un voisin selon le type de voisinage k
                Schedule neighbor = generateNeighbor(best, k);
                // Vérifie la faisabilité du voisin
                if (neighbor != null && hc.isScheduleFeasible(neighbor)) {
                    int hash = neighbor.hashCode(); // Hash pour la mémoire tabou
                    if (!tabuMemory.contains(hash)) {
                        int score = objectiveFunction.evaluate(neighbor);
                        // Si le voisin est strictement meilleur, on l'accepte
                        if (score < bestScore) {
                            best = neighbor.deepCopy();
                            bestScore = score;
                            tabuMemory.add(hash); // On ajoute à la mémoire tabou
                            break; // On repart du nouveau meilleur voisin
                        }
                    }
                }
            }
        }
        return best;
    }

    /**
     * Génère un voisin de la solution courante selon le type de voisinage choisi.
     * @param s La solution courante
     * @param k Le type de voisinage (1: swap staff, 2: move, 3: swap shift, 4: inversion bloc)
     * @return Un voisin de la solution ou null si aucun voisin valide
     */
    private Schedule generateNeighbor(Schedule s, int k) {
        if (k == 1) return swapTwoAssignments(s);
        if (k == 2) return moveAssignment(s);
        if (k == 3) return swapTwoShifts(s);
        if (k == 4) return invertBlock(s);
        return null;
    }

    /**
     * Voisinage : échange les staffs de deux assignments aléatoires.
     * @param s La solution courante
     * @return Une nouvelle solution ou null si l'échange n'est pas faisable
     */
    private Schedule swapTwoAssignments(Schedule s) {
        Schedule copy = s.deepCopy();
        Random rand = new Random();
        int size = copy.getAssignments().size();
        if (size < 2) return null;
        // On choisit deux indices différents au hasard
        int idx1 = rand.nextInt(size);
        int idx2 = rand.nextInt(size);
        if (idx1 == idx2) return null;
        Assignment a1 = copy.getAssignments().get(idx1);
        Assignment a2 = copy.getAssignments().get(idx2);
        // On échange les staffs entre les deux assignments
        Staff temp = a1.getStaff();
        a1.setStaff(a2.getStaff());
        a2.setStaff(temp);
        // On vérifie la faisabilité après l'échange
        if (!hc.isAssignmentFeasible(a1, copy) || !hc.isAssignmentFeasible(a2, copy)) {
            return null;
        }
        return copy;
    }

    /**
     * Voisinage : déplace un assignment vers un autre staff.
     * @param s La solution courante
     * @return Une nouvelle solution ou null si le déplacement n'est pas faisable
     */
    private Schedule moveAssignment(Schedule s) {
        Schedule copy = s.deepCopy();
        Random rand = new Random();
        int size = copy.getAssignments().size();
        if (size == 0) return null;
        // On choisit un assignment au hasard à déplacer
        Assignment a = copy.getAssignments().get(rand.nextInt(size));
        copy.getAssignments().remove(a);
        // On tente de le réaffecter à un autre staff
        for (Staff staff : data.getStaffs()) {
            Assignment newA = new Assignment(a.getDay(), a.getShift(), staff);
            if (hc.isAssignmentFeasible(newA, copy)) {
                copy.addAssignment(newA);
                return copy;
            }
        }
        return null;
    }

    // Nouveau voisinage : échange de shifts entre deux assignments
    /**
     * Voisinage : échange les shifts de deux assignments aléatoires.
     * @param s La solution courante
     * @return Une nouvelle solution ou null si l'échange n'est pas faisable
     */
    private Schedule swapTwoShifts(Schedule s) {
        Schedule copy = s.deepCopy();
        Random rand = new Random();
        int size = copy.getAssignments().size();
        if (size < 2) return null;
        // On choisit deux assignments différents au hasard
        int idx1 = rand.nextInt(size);
        int idx2 = rand.nextInt(size);
        if (idx1 == idx2) return null;
        Assignment a1 = copy.getAssignments().get(idx1);
        Assignment a2 = copy.getAssignments().get(idx2);
        // On échange les shifts entre les deux assignments
        Shift temp = a1.getShift();
        a1.setShift(a2.getShift());
        a2.setShift(temp);
        // On vérifie la faisabilité après l'échange
        if (!hc.isAssignmentFeasible(a1, copy) || !hc.isAssignmentFeasible(a2, copy)) {
            return null;
        }
        return copy;
    }

    // Nouveau voisinage : inversion d'un bloc de jours pour un staff
    /**
     * Voisinage : inverse l'ordre des jours d'un bloc d'assignments pour un staff donné.
     * @param s La solution courante
     * @return Une nouvelle solution ou null si l'inversion n'est pas faisable
     */
    private Schedule invertBlock(Schedule s) {
        Schedule copy = s.deepCopy();
        Random rand = new Random();
        List<Staff> staffList = data.getStaffs();
        if (staffList.isEmpty()) return null;
        // On choisit un staff au hasard
        Staff staff = staffList.get(rand.nextInt(staffList.size()));
        int horizon = data.getHorizon();
        if (horizon < 2) return null;
        // On choisit un intervalle de jours aléatoire [start, end]
        int start = rand.nextInt(horizon - 1);
        int end = start + 1 + rand.nextInt(horizon - start - 1);
        // On récupère les assignments de ce staff sur l'intervalle
        List<Assignment> block = new ArrayList<>();
        for (Assignment a : copy.getAssignments()) {
            if (a.getStaff().equals(staff) && a.getDay() >= start && a.getDay() <= end) {
                block.add(a);
            }
        }
        if (block.size() < 2) return null;
        // On inverse l'ordre des jours dans le bloc
        block.sort(Comparator.comparingInt(Assignment::getDay));
        for (int i = 0; i < block.size() / 2; i++) {
            int tmpDay = block.get(i).getDay();
            block.get(i).setDay(block.get(block.size() - 1 - i).getDay());
            block.get(block.size() - 1 - i).setDay(tmpDay);
        }
        // On vérifie la faisabilité de toutes les assignments modifiées
        for (Assignment a : block) {
            if (!hc.isAssignmentFeasible(a, copy)) return null;
        }
        return copy;
    }



}
