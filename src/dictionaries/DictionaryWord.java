/*
 * DictionaryWord.java
 *
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package dictionaries;

/**
 *
 * @author Jirka
 */
public class DictionaryWord {

    private final String value;
    private final String hint;
    private int cost;
    private final int length;

    public String getHint() {
        return hint;
    }

    public int getCost() {
        return cost;
    }

    public int getLength() {
        return length;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }

    /*
     * Creates a new instance of DictionaryWord
     */
    public DictionaryWord(String value, String hint, LetterPackage.LetterSet ls) {
        this.value = value.toUpperCase();
        this.hint = hint;
        cost = 0;
        length = value.length();
        for (int x = 0; x < value.length(); x++) {
            cost = cost + ls.getLetterCost(value.substring(x, x + 1));
        }
    }

}
