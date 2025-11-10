package models;

public class DayOff {
    private String staffId;
    private int day;

    public DayOff(String staffId, int day) {
        this.staffId = staffId;
        this.day = day;
    }

    public String getStaffId() {
        return staffId;
    }

    public int getDay() {
        return day;
    }

    @Override
    public String toString() {
        return "DaysOff [staffId=" + staffId + ", day=" + day + "]";
    }
}
