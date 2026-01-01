package models;

public class Assignment {
    private int day;
    private Staff staff;
    private Shift shift;

    public Assignment(int day, Shift shift, Staff staff) {
        this.day = day;
        this.shift = shift;
        this.staff = staff;
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

    public String toString() {
        return "Day " + day + ": Staff " + staff.getId() + " -> Shift " + shift.getId();
    }
}
