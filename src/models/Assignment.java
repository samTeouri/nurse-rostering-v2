package models;

public class Assignment {
    private int day;
    private String staffId;
    private String shiftId;

    public Assignment(int day, String shiftId, String staffId) {
        this.day = day;
        this.shiftId = shiftId;
        this.staffId = staffId;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    public String getShiftId() {
        return shiftId;
    }

    public void setShiftId(String shiftId) {
        this.shiftId = shiftId;
    }
}
