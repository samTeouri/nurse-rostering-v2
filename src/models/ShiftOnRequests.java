package models;

public class ShiftOnRequests {
    private String staffId;
    private int day;
    private String shiftId;
    private int weight;

    public ShiftOnRequests(String staffId, int day, String shiftId, int weight) {
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
        return "ShiftOnRequests [staffId=" + staffId + ", day=" + day + ", shiftId=" + shiftId + ", weight=" + weight + "]";
    }
}
