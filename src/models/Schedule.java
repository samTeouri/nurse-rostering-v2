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

    public List<Assignment> getAssignments() {
        return assignments;
    }

    public void addAssignment(Assignment a) {
        assignments.add(a);
    }
}

