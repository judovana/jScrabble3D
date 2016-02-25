/*
 * ConsoleItem.java
 *
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package MyOutputConsole;

/**
 *
 * @author Jirka
 */
public class ConsoleItem {

    private long time;
    private String string;

    @Override
    public String toString() {
        return string;
    }

    public String getString() {
        return string;
    }

    public void sertString(String s) {
        string = s;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    /**
     * Creates a new instance of ConsoleItem
     */
    public ConsoleItem(long time, String string) {
        this.time = time;
        this.string = string;
    }

}
