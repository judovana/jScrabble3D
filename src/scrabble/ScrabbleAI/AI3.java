/*
 * AI3.java
 *
 * Created on 14. duben 2007, 21:17
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package scrabble.ScrabbleAI;

import InetGameControler.ServerSinglePlayerThread;
import java.util.ArrayList;
import scrabble.DesksPackage.Desks;
import scrabble.GameDesktopPackage.GameDesktop;
import scrabble.GameLogicPackage.GameLogic;
import LetterPackage.LetterSet;
import scrabble.Settings.Settings;

/**
 *
 * @author Jirka
 */
public class AI3 extends BasicAI {

    /* Creates a new instance of AI1 */
    public AI3(LetterSet LS, GameDesktop GD, Desks d, GameLogic gl, Settings s, MyOutputConsole.Console console) {
        super(LS, GD, d, gl, s, console);
        name = "Master";

    }

    @Override
    protected int permutuj() {
        int a;

        if (settings.ints[Settings.maxcheckedlength] > 6) {
            permutujSEVEN(denull(hraje.getletters().toArray()));
        }
        if (GL.isKillui()) {
            return 0;
        }
        if (settings.ints[Settings.maxcheckedlength] > 5) {
            permutujSIX(denull(hraje.getletters().toArray()));
        }
        if (GL.isKillui()) {
            return 0;
        }
        if (settings.ints[Settings.maxcheckedlength] > 4) {
            permutujFIVE(denull(hraje.getletters().toArray()));
        }
        if (GL.isKillui()) {
            return 0;
        }
        if (settings.ints[Settings.maxcheckedlength] > 3) {
            permutujFOUR(denull(hraje.getletters().toArray()));
        }
        if (GL.isKillui()) {
            return 0;
        }
        if (settings.ints[Settings.maxcheckedlength] > 2) {
            permutujTHRE(denull(hraje.getletters().toArray()));
        }
        if (GL.isKillui()) {
            return 0;
        }
        if (settings.ints[Settings.maxcheckedlength] > 1) {
            permutujTWO(denull(hraje.getletters().toArray()));
        }
        if (GL.isKillui()) {
            return 0;
        }
        if (settings.ints[Settings.maxcheckedlength] > 0) {
            permutujONE(denull(hraje.getletters().toArray()));
        }
        if (GL.isKillui()) {
            return 0;
        }
        a = placeWord();
        return a;

    }

    @Override
    protected int permutujDistributed(ArrayList<ServerSinglePlayerThread> distibuted) {
        int a;

        if (settings.ints[Settings.maxcheckedlength] > 6) {
            permutujDistributed(7, hraje.getletters(), distibuted);
        }
        if (GL.isKillui()) {
            return 0;
        }
        if (settings.ints[Settings.maxcheckedlength] > 5) {
            permutujDistributed(6, hraje.getletters(), distibuted);
        }
        if (GL.isKillui()) {
            return 0;
        }
        if (settings.ints[Settings.maxcheckedlength] > 4) {
            permutujDistributed(5, hraje.getletters(), distibuted);
        }
        if (GL.isKillui()) {
            return 0;
        }
        if (settings.ints[Settings.maxcheckedlength] > 3) {
            permutujDistributed(4, hraje.getletters(), distibuted);
        }
        if (GL.isKillui()) {
            return 0;
        }
        if (settings.ints[Settings.maxcheckedlength] > 2) {
            permutujDistributed(3, hraje.getletters(), distibuted);
        }
        if (GL.isKillui()) {
            return 0;
        }
        if (settings.ints[Settings.maxcheckedlength] > 1) {
            permutujDistributed(2, hraje.getletters(), distibuted);
        }
        if (GL.isKillui()) {
            return 0;
        }
        if (settings.ints[Settings.maxcheckedlength] > 0) {
            permutujDistributed(1, hraje.getletters(), distibuted);
        }
        if (GL.isKillui()) {
            return 0;
        }
        a = placeWord();
        return a;

    }

}
