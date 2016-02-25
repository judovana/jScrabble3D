/*
 * PlacedLetter.java
 *
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package LetterPackage;

/**
 *
 * @author Jirka
 */
public class PlacedLetter implements Comparable {

    private final SimpleLetter letter;
    private final int x;
    private final int y;
    private final int z;
    private int usedCompare;
    public static final int COMPARE_BY_X = 1;
    public static final int COMPARE_BY_Y = 2;
    public static final int COMPARE_BY_Z = 3;
    //public static final int COMPARE_BY_W=4;??
    public boolean mark;

    public int getY() {
        return y;
    }

    public int getX() {
        return x;
    }

    public int getZ() {
        return z;
    }

    public SimpleLetter getLetter() {
        return letter;
    }

    public int getUsedCompare() {
        return usedCompare;
    }

    /*
     * Creates a new instance of PlacedLetter
     */
    public PlacedLetter(int x, int y, SimpleLetter alfa) {
        this.x = x;
        this.y = y;
        this.z = 0;
        this.letter = alfa;
    }

    public PlacedLetter(int x, int y, int z, SimpleLetter alfa) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.letter = alfa;
    }

    public void setUsedCompare(int usedCompare) {
        this.usedCompare = usedCompare;
    }

    public int compareTo(Object o) {

        switch (usedCompare) {
            case COMPARE_BY_X:
                return x - ((PlacedLetter) o).getX();
            case COMPARE_BY_Y:
                return y - ((PlacedLetter) o).getY();
            case COMPARE_BY_Z:
                return z - ((PlacedLetter) o).getZ();
            default:
                return letter.compareTo(o);
        }

    }

    @Override
    public String toString() {
        return getLetter().toString() + "(" + x + ", " + y + ", " + z + ")";
    }
}
