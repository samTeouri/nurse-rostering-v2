package models;

import java.util.HashMap;

public class Staff {
    private String id;
    private int MaxTotalMinutes;
    private int MinTotalMinutes;
    private int MaxConsecutiveShifts;
    private int MinConsecutiveShifts;
    private int MinConsecutiveDaysOff;
    private int MaxWeekends;
    private HashMap<String, String> maxShifts;

    public Staff(String id,
        int MaxTotalMinutes,
        int MinTotalMinutes,
        int MaxConsecutiveShifts,
        int MinConsecutiveShifts,
        int MinConsecutiveDaysOff,
        int MaxWeekends
    ) {
        this.id = id;
        this.MaxTotalMinutes = MaxTotalMinutes;
        this.MinTotalMinutes = MinTotalMinutes;
        this.MaxConsecutiveShifts = MaxConsecutiveShifts;
        this.MinConsecutiveShifts = MinConsecutiveShifts;
        this.MinConsecutiveDaysOff = MinConsecutiveDaysOff;
        this.MaxWeekends = MaxWeekends;
        this.maxShifts = new HashMap<>();
    }

    public String getId() {
        return id;
    }

    public int getMaxTotalMinutes() {
        return MaxTotalMinutes;
    }

    public int getMinTotalMinutes() {
        return MinTotalMinutes;
    }

    public int getMaxConsecutiveShifts() {
        return MaxConsecutiveShifts;
    }

    public int getMinConsecutiveShifts() {
        return MinConsecutiveShifts;
    }

    public int getMinConsecutiveDaysOff() {
        return MinConsecutiveDaysOff;
    }

    public int getMaxWeekends() {
        return MaxWeekends;
    }

    public void addMaxShift(String shift, String max) {
        this.maxShifts.put(shift, max);
    }

    @Override
    public String toString() {
        return "Staff{" + "id=" + id + ", MaxTotalMinutes=" + MaxTotalMinutes + ", MinTotalMinutes=" + MinTotalMinutes + ", MaxConsecutiveShifts=" + MaxConsecutiveShifts + ", MinConsecutiveShifts=" + MinConsecutiveShifts + ", MinConsecutiveDaysOff=" + MinConsecutiveDaysOff + ", MaxWeekends=" + MaxWeekends + ", maxShifts=" + maxShifts + '}';
    }

}
