package models;

import java.util.ArrayList;

/**
 *
 * @author samrou
 */
public class Shift {
    private String id;
    private int length;
    private ArrayList<String> shiftsThatcannotFollow;

    public Shift(String id, int length) {
        this.id = id;
        this.length = length;
        this.shiftsThatcannotFollow = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public int getLength() {
        return length;
    }

    public ArrayList<String> getShiftsThatcannotFollow() {
        return shiftsThatcannotFollow;
    }

    public void addShiftThatCannotFollow(String shiftId) {
        this.shiftsThatcannotFollow.add(shiftId);
    }

    @Override
    public String toString() {
        return "Shift{" + "id=" + id + ", length=" + length + ", shiftsThatcannotFollow=" + shiftsThatcannotFollow + '}';
    }
}
