/*
 * AI2.java
 *
 * Created on 14. duben 2007, 21:24
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package scrabble.ScrabbleAI;

import scrabble.DesksPackage.Desks;
import scrabble.GameDesktopPackage.GameDesktop;
import scrabble.GameLogicPackage.GameLogic;
import LetterPackage.LetterSet;
import scrabble.Settings.Settings;

/**
 *
 * @author Jirka
 */
public class AI2 extends BasicAI {

    /* Creates a new instance of AI1 */
    public AI2(LetterSet LS, GameDesktop GD, Desks d, GameLogic gl, Settings s, MyOutputConsole.Console console) {
        super(LS, GD, d, gl, s, console);

    }
}
