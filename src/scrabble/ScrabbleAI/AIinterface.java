/*
 * AIinterface.java
 *
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package scrabble.ScrabbleAI;

import InetGameControler.ServerSinglePlayerThread;
import LetterPackage.LetterSet;
import LetterPackage.PlacedLetter;
import java.util.ArrayList;
import scrabble.Playerspackage.Player;

/**
 *
 * @author Jirka
 */
public interface AIinterface {

    public ArrayList<PlacedLetter> hrej(Player hraje, ArrayList<ServerSinglePlayerThread> distibuted);

    public void addDistributedresult(String[] c, LetterSet letterSet);

    public String giveMePermutation(String[] c, LetterSet l);

    public String getName();

}
