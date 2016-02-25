/*
 * AIresult.java
 *
 * Created on 9. duben 2007, 12:12
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package scrabble.ScrabbleAI;

import java.util.ArrayList;
import LetterPackage.PlacedLetter;

/**
 *
 * @author Jirka
 */
public class AIresult implements Comparable<AIresult> {

    public int value = 0;
    public int x;
    public int y;
    public int z;
    public int horiz; //horiz=1(x),vert(y)=0,3d(z)=2
    public ArrayList<PlacedLetter> slovo = new ArrayList();

    /**
     * Creates a new instance of AIresult
     */
    public AIresult() {
        horiz = 0;
    }

    public int compareTo(AIresult anotherAiresult) {
        int thisVal = this.value;
        int anotherVal = anotherAiresult.value;
        return (thisVal < anotherVal ? -1 : (thisVal == anotherVal ? 0 : 1));
    }

}
