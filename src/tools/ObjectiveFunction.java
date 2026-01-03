package tools;

import java.util.*;
import models.*;

/**
 * Classe utilitaire pour évaluer la qualité d'un planning (fonction objectif).
 * Calcule le coût total d'une solution selon différents critères de pénalité.
 */
public class ObjectiveFunction {
    
    private final DataModel data;

    // Niveau 1 — Couverture (très élevé)
    private static final int V_MIN = 1000;   // sous-couverture
    private static final int V_MAX = 1000;    // sur-couverture

    // Niveau 2 — Demandes OFF
    private static final int P_OFF = 20;

    // Niveau 3 — Demandes ON
    private static final int Q_ON = 10;

    // Niveau 4 — Équité / confort
    private static final int W_MIN_WORKLOAD = 5;
    private static final int W_MIN_CONSEC_WORK = 5;
    private static final int W_MIN_CONSEC_OFF = 5;


    /**
     * Constructeur de la fonction objectif.
     * @param data Le modèle de données du problème
     */
    public ObjectiveFunction(DataModel data) {
        this.data = data;
    }

    /**
     * Évalue le coût total d'un planning selon les pénalités définies.
     * @param schedule Le planning à évaluer
     * @return Le score total (plus il est bas, meilleure est la solution)
     */
    public int evaluate(Schedule schedule) {
        int cost = 0;

        cost += penaltyShiftOnRequests(schedule);
        cost += penaltyShiftOffRequests(schedule);
        cost += penaltyUnderCover(schedule);
        cost += penaltyOverCover(schedule);
        cost += W_MIN_WORKLOAD * penaltyMinWorkload(schedule);
        cost += W_MIN_CONSEC_WORK * penaltyMinConsecutiveWork(schedule);
        cost += W_MIN_CONSEC_OFF * penaltyMinConsecutiveOff(schedule);

        return cost;
    }

    /**
     * Calcule la pénalité pour les demandes de shifts ON non satisfaites.
     */
    private int penaltyShiftOnRequests(Schedule schedule) {
        int cost = 0;

        for (Staff s : data.getStaffs()) {
            ArrayList<String> onReq = data.getStaffShiftOnRequestsIds(s.getId());
            for (String shiftId : onReq) {
                boolean assigned = schedule.getAssignments().stream()
                    .anyMatch(a ->
                        a.getStaff().equals(s)
                        && a.getShift().getId().equals(shiftId)
                    );
                if (!assigned) cost += Q_ON;
            }
        }
        return cost;
    }
    
    /**
     * Calcule la pénalité pour les demandes de shifts OFF non respectées.
     */
    private int penaltyShiftOffRequests(Schedule schedule) {
        int cost = 0;

        for (Assignment a : schedule.getAssignments()) {
            if (data
                .getStaffShiftOffRequestsIds(a.getStaff().getId())
                .contains(a.getShift().getId())) {
                cost += P_OFF;
            }
        }
        return cost;
    }

    /**
     * Calcule la pénalité pour sous-couverture des shifts.
     */
    private int penaltyUnderCover(Schedule schedule) {
        int cost = 0;

        for (int d = 0; d < data.getHorizon(); d++) {
            final int day = d;
            for (Shift sh : data.getShifts()) {

                Cover cover = data.getCover(day, sh.getId());
                if (cover == null) continue;

                long assigned = schedule.getAssignments().stream()
                    .filter(a ->
                        a.getDay() == day &&
                        a.getShift().getId().equals(sh.getId()))
                    .count();

                if (assigned < cover.getRequirement()) {
                    cost += (cover.getRequirement() - assigned) * V_MIN;
                }
            }
        }
        return cost;
    }

    /**
     * Calcule la pénalité pour sur-couverture des shifts.
     */
    private int penaltyOverCover(Schedule schedule) {
        int cost = 0;

        for (int d = 0; d < data.getHorizon(); d++) {
            final int day = d;
            for (Shift sh : data.getShifts()) {

                Cover cover = data.getCover(day, sh.getId());
                if (cover == null) continue;

                long assigned = schedule.getAssignments().stream()
                    .filter(a ->
                        a.getDay() == day &&
                        a.getShift().getId().equals(sh.getId()))
                    .count();

                if (assigned > cover.getRequirement()) {
                    cost += (assigned - cover.getRequirement()) * V_MAX;
                }
            }
        }
        return cost;
    }

    private int penaltyMinWorkload(Schedule schedule) {
        Map<Staff, Integer> worked = new HashMap<>();

        for (Assignment a : schedule.getAssignments()) {
            worked.merge(
                a.getStaff(),
                a.getShift().getLength(),
                Integer::sum
            );
        }

        int penalty = 0;

        for (Staff s : data.getStaffs()) {
            int total = worked.getOrDefault(s, 0);
            if (total < s.getMinTotalMinutes()) {
                penalty += (s.getMinTotalMinutes() - total);
            }
        }
        return penalty;
    }

    private int penaltyMinConsecutiveWork(Schedule schedule) {
        Map<Staff, List<Integer>> daysByStaff = new HashMap<>();

        for (Assignment a : schedule.getAssignments()) {
            daysByStaff
                .computeIfAbsent(a.getStaff(), k -> new ArrayList<>())
                .add(a.getDay());
        }

        int penalty = 0;

        for (Staff s : data.getStaffs()) {
            int min = s.getMinConsecutiveShifts();
            if (min <= 1) continue;

            List<Integer> days = daysByStaff.getOrDefault(s, List.of());
            Collections.sort(days);

            int streak = 1;
            for (int i = 1; i < days.size(); i++) {
                if (days.get(i) == days.get(i - 1) + 1) {
                    streak++;
                } else {
                    if (streak < min) {
                        penalty += (min - streak);
                    }
                    streak = 1;
                }
            }
            if (streak < min) penalty += (min - streak);
        }
        return penalty;
    }

    private int penaltyMinConsecutiveOff(Schedule schedule) {
        int H = data.getHorizon();
        Map<Staff, Set<Integer>> workedDays = new HashMap<>();

        for (Assignment a : schedule.getAssignments()) {
            workedDays
                .computeIfAbsent(a.getStaff(), k -> new HashSet<>())
                .add(a.getDay());
        }

        int penalty = 0;

        for (Staff s : data.getStaffs()) {
            int minOff = s.getMinConsecutiveDaysOff();
            if (minOff <= 1) continue;

            Set<Integer> work = workedDays.getOrDefault(s, Set.of());

            int offStreak = 0;
            for (int d = 0; d < H; d++) {
                if (!work.contains(d)) {
                    offStreak++;
                } else {
                    if (offStreak > 0 && offStreak < minOff) {
                        penalty += (minOff - offStreak);
                    }
                    offStreak = 0;
                }
            }
            if (offStreak > 0 && offStreak < minOff) {
                penalty += (minOff - offStreak);
            }
        }
        return penalty;
    }


}
