package models;

import java.util.ArrayList;

/**
 *
 * @author samrou
 */
public class Shift {
    private String id;
    private int length;
    private ArrayList<String> shiftsThatCannotFollow;

    public Shift(String id, int length) {
        this.id = id;
        this.length = length;
        this.shiftsThatCannotFollow = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public int getLength() {
        return length;
    }

    public ArrayList<String> getShiftsThatCannotFollow() {
        return shiftsThatCannotFollow;
    }

    public void addShiftThatCannotFollow(String shiftId) {
        this.shiftsThatCannotFollow.add(shiftId);
    }

    @Override
    public String toString() {
        return "Shift{" + "id=" + id + ", length=" + length + ", shiftsThatCannotFollow=" + shiftsThatCannotFollow + '}';
    }
}
