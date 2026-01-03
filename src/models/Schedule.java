package models;

import java.util.ArrayList;
import java.util.List;

public class Schedule {
    private final ArrayList<Assignment> assignments;

    public Schedule() {
        this.assignments = new ArrayList<>();
    }

    public Schedule(List<Assignment> assignments) {
        this.assignments = new ArrayList<>(assignments);
    }

    public Schedule deepCopy() {
        ArrayList<Assignment> copiedAssignments = new ArrayList<>();
        for (Assignment a : this.assignments) {
            copiedAssignments.add(a.deepCopy());
        }
        return new Schedule(copiedAssignments);
    }

    public List<Assignment> getAssignments() {
        return assignments;
    }

    public void addAssignment(Assignment a) {
        assignments.add(a);
    }

    public int countAssignments(int day, Shift shift) {
        int count = 0;
        for (Assignment a : assignments) {
            if (a.getDay() == day && a.getShift().getId().equals(shift.getId())) {
                count++;
            }
        }
        return count;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Assignment a : assignments) {
            sb.append("Day ").append(a.getDay())
              .append(": Staff ").append(a.getStaff().getId())
              .append(" -> Shift ").append(a.getShift().getId())
              .append("\n");
        }
        return sb.toString();
    }
}

