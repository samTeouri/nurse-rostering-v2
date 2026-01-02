package tools;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import models.Assignment;
import models.Cover;
import models.DataModel;
import models.Schedule;
import models.Staff;

public class HardConstraintChecker {

    private final DataModel data;

    public HardConstraintChecker(DataModel data) {
        this.data = data;
    }

    public boolean isAssignmentFeasible(
            Assignment candidate,
            Schedule partialSchedule) {

        List<Assignment> current = partialSchedule.getAssignments();

        return checkC1(candidate, current)
            && checkC2(candidate, current)
            && checkC3(candidate, current)
            && checkC4(candidate, current)
            && checkC5(candidate, current)
            && checkC8(candidate, current)
            && checkC9(candidate);
    }

    public boolean isScheduleFeasible(Schedule schedule) {
        List<Assignment> A = schedule.getAssignments();

        return checkC1All(A)
            && checkC2All(A)
            && checkC3All(A)
            && checkC4All(A)
            && checkC5All(A)
            && checkC8All(A)
            && checkC9All(A);
    }

    public Map<String, Boolean> hardConstraintReport(Schedule schedule) {
        List<Assignment> A = schedule.getAssignments();

        Map<String, Boolean> report = new HashMap<>();
        report.put("C1", checkC1All(A));
        report.put("C2", checkC2All(A));
        report.put("C3", checkC3All(A));
        report.put("C4", checkC4All(A));
        report.put("C5", checkC5All(A));
        report.put("C6", checkC6All(A));
        report.put("C7", checkC7All(A));
        report.put("C8", checkC8All(A));
        report.put("C9", checkC9All(A));
        report.put("C10", checkC10All(A));
        return report;
    }

    public boolean checkC1(Assignment c, List<Assignment> A) {
        List<Assignment> A_copy = new ArrayList<>(A);
        A_copy.remove(c);  // To avoid checking against itself
        for (Assignment a : A_copy) {
            if (a.getStaff().getId().equals(c.getStaff().getId())
                && a.getDay() == c.getDay()) {
                return false;
            }
        }
        return true;
    }

    private boolean checkC1All(List<Assignment> A) {
        Set<String> seen = new HashSet<>();

        for (Assignment a : A) {
            String key = a.getStaff().getId() + "_" + a.getDay();
            if (!seen.add(key)) return false;
        }
        return true;
    }


    public boolean checkC2(Assignment c, List<Assignment> A) {
        for (Assignment a : A) {
            if (a.getStaff().getId().equals(c.getStaff().getId())
                && a.getDay() == c.getDay() - 1
                && a.getShift()
                    .getShiftsThatCannotFollow()
                    .contains(c.getShift().getId())) {
                return false;
            }
        }
        return true;
    }

    private boolean checkC2All(List<Assignment> A) {
        for (Assignment a1 : A) {
            for (Assignment a2 : A) {
                if (a1.getStaff().equals(a2.getStaff())
                    && a2.getDay() == a1.getDay() + 1
                    && a1.getShift()
                        .getShiftsThatCannotFollow()
                        .contains(a2.getShift().getId())) {
                    return false;
                }
            }
        }
        return true;
    }


    public boolean checkC3(Assignment c, List<Assignment> A) {
        int maxAllowed = Integer.parseInt(c.getStaff().getMaxShifts().get(c.getShift().getId()));
        int count = 0;
        for (Assignment a : A) {
            if (a.getStaff().getId().equals(c.getStaff().getId())
                && a.getShift().getId().equals(c.getShift().getId())) {
                count++;
            }
        }
        return count + 1 <= maxAllowed;
    }

    private boolean checkC3All(List<Assignment> A) {

        Map<String, Integer> count = new HashMap<>();

        for (Assignment a : A) {
            String key = a.getStaff().getId() + "_" + a.getShift().getId();
            count.put(key, count.getOrDefault(key, 0) + 1);
        }

        for (Assignment a : A) {
            int maxAllowed = Integer.parseInt(a.getStaff().getMaxShifts().get(a.getShift().getId()));

            String key = a.getStaff().getId() + "_" + a.getShift().getId();
            if (count.get(key) > maxAllowed) return false;
        }
        return true;
    }


    public boolean checkC4(Assignment c, List<Assignment> A) {
        int totalMinutes = 0;

        List<Assignment> A_copy = new ArrayList<>(A);
        A_copy.remove(c);

        for (Assignment a : A_copy) {
            if (a.getStaff().getId().equals(c.getStaff().getId())) {
                totalMinutes += a.getShift().getLength();
            }
        }
        return totalMinutes + c.getShift().getLength() <= c.getStaff().getMaxTotalMinutes();
    }

    private boolean checkC4All(List<Assignment> A) {
        Map<Staff, Integer> minutes = new HashMap<>();

        for (Assignment a : A) {
            minutes.put(
                a.getStaff(),
                minutes.getOrDefault(a.getStaff(), 0) + a.getShift().getLength()
            );
        }

        for (Staff s : minutes.keySet()) {
            int total = minutes.get(s);
            if (total > s.getMaxTotalMinutes()) return false;
        }
        return true;
    }

    public boolean checkC5(Assignment c, List<Assignment> A) {
        List<Integer> days = new ArrayList<>();
        for (Assignment a : A) {
            if (a.getStaff().getId().equals(c.getStaff().getId())) {
                days.add(a.getDay());
            }
        }

        days.add(c.getDay());
        Collections.sort(days);

        int streak = 1;
        for (int i = 1; i < days.size(); i++) {
            if (days.get(i) == days.get(i - 1) + 1) {
                streak++;
                if (streak > c.getStaff().getMaxConsecutiveShifts())
                    return false;
            } else {
                streak = 1;
            }
        }
        return true;
    }

    private boolean checkC5All(List<Assignment> A) {
        Map<Staff, List<Integer>> daysByStaff = new HashMap<>();

        for (Assignment a : A) {
            daysByStaff
                .computeIfAbsent(a.getStaff(), s -> new ArrayList<>())
                .add(a.getDay());
        }

        for (Map.Entry<Staff, List<Integer>> e : daysByStaff.entrySet()) {
            List<Integer> days = e.getValue();
            Collections.sort(days);

            int streak = 1;
            for (int i = 1; i < days.size(); i++) {
                if (days.get(i) == days.get(i - 1) + 1) {
                    streak++;
                    if (streak > e.getKey().getMaxConsecutiveShifts())
                        return false;
                } else {
                    streak = 1;
                }
            }
        }
        return true;
    }


    public boolean checkC6All(List<Assignment> A) {
        Map<Staff, List<Integer>> daysByStaff = new HashMap<>();

        for (Assignment a : A) {
            daysByStaff
                .computeIfAbsent(a.getStaff(), s -> new ArrayList<>())
                .add(a.getDay());
        }

        for (Map.Entry<Staff, List<Integer>> entry : daysByStaff.entrySet()) {
            Staff staff = entry.getKey();
            int cmin = staff.getMinConsecutiveShifts();
            if (cmin <= 1) continue;

            List<Integer> days = entry.getValue();
            Collections.sort(days);

            int streak = 1;
            for (int i = 1; i < days.size(); i++) {
                if (days.get(i) == days.get(i - 1) + 1) {
                    streak++;
                } else {
                    if (streak < cmin) return false;
                    streak = 1;
                }
            }

            if (streak < cmin) return false;
        }
        return true;
    }

    public boolean checkC7All(List<Assignment> A) {

        int H = data.getHorizon();

        Map<Staff, Set<Integer>> workedDays = new HashMap<>();

        for (Assignment a : A) {
            workedDays
                .computeIfAbsent(a.getStaff(), s -> new HashSet<>())
                .add(a.getDay());
        }

        for (Staff staff : data.getStaffs()) {
            int omin = staff.getMinConsecutiveDaysOff();
            if (omin <= 1) continue;

            Set<Integer> work = workedDays.getOrDefault(staff, Set.of());

            int offStreak = 0;
            for (int d = 0; d < H; d++) {
                if (!work.contains(d)) {
                    offStreak++;
                } else {
                    if (offStreak > 0 && offStreak < omin) return false;
                    offStreak = 0;
                }
            }
            if (offStreak > 0 && offStreak < omin) return false;
        }
        return true;
    }

    public boolean checkC8(Assignment c, List<Assignment> A) {

        Set<Integer> weekendsWorked = new HashSet<>();

        for (Assignment a : A) {
            if (a.getStaff().getId().equals(c.getStaff().getId())) {
                if (isWeekend(a.getDay())) {
                    weekendsWorked.add(a.getDay() / 7);
                }
            }
        }

        if (isWeekend(c.getDay())) {
            weekendsWorked.add(c.getDay() / 7);
        }

        return weekendsWorked.size() <= c.getStaff().getMaxWeekends();
    }

    private boolean checkC8All(List<Assignment> A) {
        Map<Staff, Set<Integer>> weekendsWorked = new HashMap<>();

        for (Assignment a : A) {
            if (isWeekend(a.getDay())) {
                weekendsWorked
                    .computeIfAbsent(a.getStaff(), s -> new HashSet<>())
                    .add(a.getDay() / 7);
            }
        }

        for (Map.Entry<Staff, Set<Integer>> e : weekendsWorked.entrySet()) {
            if (e.getValue().size() > e.getKey().getMaxWeekends())
                return false;
        }
        return true;
    }

    private boolean isWeekend(int day) {
        int dayOfWeek = day % 7;
        return dayOfWeek == 5 || dayOfWeek == 6;
    }

    public boolean checkC9(Assignment c) {
        return !data
                .getStaffDaysOff(c.getStaff().getId())
                .contains(c.getDay());
    }

    private boolean checkC9All(List<Assignment> A) {
        for (Assignment a : A) {
            if (data
                .getStaffDaysOff(a.getStaff().getId())
                .contains(a.getDay())) {
                return false;
            }
        }
        return true;
    }

    private boolean checkC10All(List<Assignment> A) {
        int shiftOffRequestsAssignedCount = countShiftOffRequestsAssigned(A);
        int staffMissingCount = countStaffMissingCover(A);
        int staffExceedCount = countStaffExceedingCover(A);
        if (shiftOffRequestsAssignedCount - staffExceedCount + staffMissingCount == 0) return false;
        return true;
    }

    private int countShiftOffRequestsAssigned(List<Assignment> A) {
        int count = 0;
        for (Assignment a : A) {
            if (data.getStaffShiftOffRequestsIds(a.getStaff().getId()).contains(a.getShift().getId())) {
                count++;
            }
        }
        return count;
    }

    private int countStaffMissingCover(List<Assignment> A) {
        int count = 0;
        for (Assignment a : A) {
            Cover cover = data.getCover(a.getDay(), a.getShift().getId());
            if (cover != null) {
                int assignedCount = (int) A.stream()
                    .filter(assignment -> assignment.getDay() == a.getDay()
                        && assignment.getShift().getId().equals(a.getShift().getId()))
                    .count();
                if (assignedCount < cover.getRequirement()) {
                    count++;
                }
            }
        }
        return count;
    }

    private int countStaffExceedingCover(List<Assignment> A) {
        int count = 0;
        for (Assignment a : A) {
            Cover cover = data.getCover(a.getDay(), a.getShift().getId());
            if (cover != null) {
                int assignedCount = (int) A.stream()
                    .filter(assignment -> assignment.getDay() == a.getDay()
                        && assignment.getShift().getId().equals(a.getShift().getId()))
                    .count();
                if (assignedCount > cover.getRequirement()) {
                    count++;
                }
            }
        }
        return count;
    }
}