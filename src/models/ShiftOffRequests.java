package models;

public class ShiftOffRequests {
    private String staffId;
    private int day;
    private String shiftId;
    private int weight;

    public ShiftOffRequests(String staffId, int day, String shiftId, int weight) {
        this.staffId = staffId;
        this.day = day;
        this.shiftId = shiftId;
        this.weight = weight;
    }

    public String getStaffId() {
        return staffId;
    }

    public int getDay() {
        return day;
    }

    public String getShiftId() {
        return shiftId;
    }

    public int getWeight() {
        return weight;
    }

    @Override
    public String toString() {
        return "ShiftOffRequests [staffId=" + staffId + ", day=" + day + ", shiftId=" + shiftId + ", weight=" + weight + "]";
    }
}
