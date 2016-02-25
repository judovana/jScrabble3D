/*
 * SimpleLetter.java
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
public class SimpleLetter implements Comparable {

    private String type;
    private int value;
    private int list;
    private int texture;

    public int getTexture() {
        return texture;
    }

    public String getType() {
        return type;
    }

    public int getValue() {
        return value;
    }

    public int getList() {
        return list;
    }

    public void setList(int nwList) {
        list = nwList;
    }

    /**
     * Creates a new instance of SimpleLetter
     */
    public SimpleLetter() {

    }

    public SimpleLetter(String type, int value, int list, int texture) {
        this.type = type;
        this.value = value;
        this.list = list;
        this.texture = texture;
    }

    @Override
    public String toString() {
        return type;
    }

    public int compareTo(Object o) {
        return type.compareTo((String) o);
    }

}
