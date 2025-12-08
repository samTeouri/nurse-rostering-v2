package models;

public class DataModel {
    private int horizon;
    private DayOff[] daysOff;
    private Shift[] shifts;
    private Staff[] staffs;
    private Cover[] covers;
    private ShiftOnRequests[] shiftOnRequests;
    private ShiftOffRequests[] shiftOffRequests;

    public DataModel() {}

    public DataModel(int horizon, DayOff[] daysOff, Shift[] shifts, Staff[] staffs,
                     Cover[] covers, ShiftOnRequests[] shiftOnRequests,
                     ShiftOffRequests[] shiftOffRequests) {
        this.horizon = horizon;
        this.daysOff = daysOff;
        this.shifts = shifts;
        this.staffs = staffs;
        this.covers = covers;
        this.shiftOnRequests = shiftOnRequests;
        this.shiftOffRequests = shiftOffRequests;
    }

    public int getHorizon() {
        return horizon;
    }

    public void setHorizon(int horizon) {
        this.horizon = horizon;
    }

    public DayOff[] getDaysOff() {
        return daysOff;
    }

    public void setDaysOff(DayOff[] daysOff) {
        this.daysOff = daysOff;
    }

    public Shift[] getShifts() {
        return shifts;
    }

    public void setShifts(Shift[] shifts) {
        this.shifts = shifts;
    }

    public Staff[] getStaffs() {
        return staffs;
    }

    public void setStaffs(Staff[] staffs) {
        this.staffs = staffs;
    }

    public Cover[] getCovers() {
        return covers;
    }

    public void setCovers(Cover[] covers) {
        this.covers = covers;
    }

    public ShiftOnRequests[] getShiftOnRequests() {
        return shiftOnRequests;
    }

    public void setShiftOnRequests(ShiftOnRequests[] shiftOnRequests) {
        this.shiftOnRequests = shiftOnRequests;
    }

    public ShiftOffRequests[] getShiftOffRequests() {
        return shiftOffRequests;
    }

    public void setShiftOffRequests(ShiftOffRequests[] shiftOffRequests) {
        this.shiftOffRequests = shiftOffRequests;
    }

    @Override
    public String toString() {
        return "DataModel [horizon=" + horizon + ", daysOff=" + java.util.Arrays.toString(daysOff) + ", shifts=" + java.util.Arrays.toString(shifts)
                + ", staffs=" + java.util.Arrays.toString(staffs) + ", covers=" + java.util.Arrays.toString(covers)
                + ", shiftOnRequests=" + java.util.Arrays.toString(shiftOnRequests) + ", shiftOffRequests=" + java.util.Arrays.toString(shiftOffRequests) + "]";
    }
}
