package models;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.stream.Collectors;

public class Assignment {
    private int day;
    private Staff staff;
    private Shift shift;

    public Assignment(int day, Shift shift, Staff staff) {
        this.day = day;
        this.shift = shift;
        this.staff = staff;
    }

    public boolean isValid(ArrayList<Assignment> currentAssignments, DataModel data) {

        // Constraint 1
        for (Assignment assignment : currentAssignments) {
            if (assignment.getStaff().getId().equals(this.staff.getId()) && assignment.getDay() == this.day) {
                return false;
            }
        }

        // Constraint 2
        for (Assignment assignment : currentAssignments) {
            ArrayList<String> shiftsThatCannotFollowIds = assignment.getShift().getShiftsThatCannotFollow();

            HashSet<String> shiftIdsSet = new HashSet<>(shiftsThatCannotFollowIds);
            ArrayList<Shift> shiftsThatCannotFollow = data.getShifts().stream()
                .filter(shift -> shiftIdsSet.contains(shift.getId()))
                .collect(Collectors.toCollection(ArrayList::new));
            
            for (Shift stcf : shiftsThatCannotFollow) {
                if (assignment.getStaff().getId().equals(this.staff.getId())
                    && assignment.getDay() == this.day - 1
                    && stcf.getId().equals(this.shift.getId())) {
                    return false;
                }
            }
        }

        return true;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public Staff getStaff() {
        return staff;
    }

    public void setStaff(Staff staff) {
        this.staff = staff;
    }

    public Shift getShift() {
        return shift;
    }

    public void setShift(Shift shift) {
        this.shift = shift;
    }
}
