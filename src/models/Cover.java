package models;

public class Cover {
    private int day;
    private String shiftId;
    private int requirement;
    private int weightForUnder;
    private int weightForOver;

    public Cover(int day, String shiftId, int requirement, int weightForUnder, int weightForOver) {
        this.day = day;
        this.shiftId = shiftId;
        this.requirement = requirement;
        this.weightForUnder = weightForUnder;
        this.weightForOver = weightForOver;
    }

    public String getShiftId() {
        return shiftId;
    }

    public int getDay() {
        return day;
    }

    public int getRequirement() {
        return requirement;
    }

    public int getWeightForUnder() {
        return weightForUnder;
    }

    public int getWeightForOver() {
        return weightForOver;
    }

    @Override
    public String toString() {
        return "Cover [day=" + day + ", shiftId=" + shiftId + ", requirement=" + requirement + ", weightForUnder=" + weightForUnder
                + ", weightForOver=" + weightForOver + "]";
    }
}
