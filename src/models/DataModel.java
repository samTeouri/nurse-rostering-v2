package models;

import java.util.ArrayList;

public class DataModel {
    private int horizon;
    private ArrayList<DayOff> daysOff;
    private ArrayList<Shift> shifts;
    private ArrayList<Staff> staffs;
    private ArrayList<Cover> covers;
    private ArrayList<ShiftOnRequests> shiftOnRequests;
    private ArrayList<ShiftOffRequests> shiftOffRequests;

    public DataModel() {}

    public DataModel(int horizon, ArrayList<DayOff> daysOff, ArrayList<Shift> shifts, ArrayList<Staff> staffs,
                     ArrayList<Cover> covers, ArrayList<ShiftOnRequests> shiftOnRequests,
                     ArrayList<ShiftOffRequests> shiftOffRequests) {
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

    public ArrayList<DayOff> getDaysOff() {
        return daysOff;
    }

    public void setDaysOff(ArrayList<DayOff> daysOff) {
        this.daysOff = daysOff;
    }

    public ArrayList<Shift> getShifts() {
        return shifts;
    }

    public void setShifts(ArrayList<Shift> shifts) {
        this.shifts = shifts;
    }

    public ArrayList<Staff> getStaffs() {
        return staffs;
    }

    public void setStaffs(ArrayList<Staff> staffs) {
        this.staffs = staffs;
    }

    public ArrayList<Cover> getCovers() {
        return covers;
    }

    public void setCovers(ArrayList<Cover> covers) {
        this.covers = covers;
    }

    public Cover getCover(int day, String shiftId) {
        for (Cover cover : covers) {
            if (cover.getDay() == day && cover.getShiftId().equals(shiftId)) {
                return cover;
            }
        }
        return null;
    }

    public ArrayList<ShiftOnRequests> getShiftOnRequests() {
        return shiftOnRequests;
    }

    public void setShiftOnRequests(ArrayList<ShiftOnRequests> shiftOnRequests) {
        this.shiftOnRequests = shiftOnRequests;
    }

    public ArrayList<ShiftOffRequests> getShiftOffRequests() {
        return shiftOffRequests;
    }

    public void setShiftOffRequests(ArrayList<ShiftOffRequests> shiftOffRequests) {
        this.shiftOffRequests = shiftOffRequests;
    }

    @Override
    public String toString() {
        return "DataModel{" + "horizon=" + horizon + ", daysOff=" + daysOff + ", shifts=" + shifts + ", staffs=" + staffs + ", covers=" + covers + ", shiftOnRequests=" + shiftOnRequests + ", shiftOffRequests=" + shiftOffRequests + '}';
    }
}
