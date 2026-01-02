package tools;

import java.util.*;
import models.*;

public class ObjectiveFunction {
    
    private final DataModel data;

    private static final int Q_ON = 5;
    private static final int P_OFF = 10;
    private static final int V_MIN = 20;
    private static final int V_MAX = 15;

    public ObjectiveFunction(DataModel data) {
        this.data = data;
    }

    public int evaluate(Schedule schedule) {
        int cost = 0;

        cost += penaltyShiftOnRequests(schedule);
        cost += penaltyShiftOffRequests(schedule);
        cost += penaltyUnderCover(schedule);
        cost += penaltyOverCover(schedule);

        return cost;
    }

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

}
