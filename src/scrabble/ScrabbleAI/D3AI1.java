/*
 * AI1.java
 *
 * Created on 14. duben 2007, 21:17
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package scrabble.ScrabbleAI;

import InetGameControler.ServerSinglePlayerThread;
import java.util.ArrayList;
import java.util.Random;
import scrabble.DesksPackage.Desks;
import scrabble.GameDesktopPackage.GameDesktop;
import scrabble.GameLogicPackage.GameLogic;
import LetterPackage.LetterSet;
import scrabble.Settings.Settings;

/**
 *
 * @author Jirka
 */
public class D3AI1 extends BasicAI3D {

    /* Creates a new instance of AI1 */
    public D3AI1(LetterSet LS, GameDesktop GD, Desks d, GameLogic gl, Settings s, MyOutputConsole.Console console) {
        super(LS, GD, d, gl, s, console);
        name = "Idiot";
    }

    @Override
    protected int permutuj() {
        int a;
        int max = 0;
        int base = settings.ints[Settings.maxcheckedlength];
        if (base > 7) {
            base = 7;
        }
        Random r = new Random();
        int tries = 0;
        while (tries < 3) {
            tries++;
            switch (r.nextInt(base)) {
                case (0):
                    if (settings.ints[Settings.maxcheckedlength] > 6) {
                        permutujSEVEN(denull(hraje.getletters().toArray()));
                    }
                    a = placeWord();
                    max = Math.max(max, a);
                    if (a > 0 && !settings.bools[Settings.multipleword]) {
                        return a;
                    }
                    if (GL.isKillui()) {
                        return 0;
                    }
                    break;
                case (1):
                    if (settings.ints[Settings.maxcheckedlength] > 5) {
                        permutujSIX(denull(hraje.getletters().toArray()));
                    }
                    a = placeWord();
                    max = Math.max(max, a);
                    if (a > 0 && !settings.bools[Settings.multipleword]) {
                        return a;
                    }
                    if (GL.isKillui()) {
                        return 0;
                    }
                    break;
                case (2):
                    if (settings.ints[Settings.maxcheckedlength] > 4) {
                        permutujFIVE(denull(hraje.getletters().toArray()));
                    }
                    a = placeWord();
                    max = Math.max(max, a);
                    if (a > 0 && !settings.bools[Settings.multipleword]) {
                        return a;
                    }
                    if (GL.isKillui()) {
                        return 0;
                    }
                    break;
                case (3):
                    if (settings.ints[Settings.maxcheckedlength] > 3) {
                        permutujFOUR(denull(hraje.getletters().toArray()));
                    }
                    a = placeWord();
                    max = Math.max(max, a);
                    if (a > 0 && !settings.bools[Settings.multipleword]) {
                        return a;
                    }
                    if (GL.isKillui()) {
                        return 0;
                    }
                    break;
                case (4):
                    if (settings.ints[Settings.maxcheckedlength] > 2) {
                        permutujTHRE(denull(hraje.getletters().toArray()));
                    }
                    a = placeWord();
                    max = Math.max(max, a);
                    if (a > 0 && !settings.bools[Settings.multipleword]) {
                        return a;
                    }
                    if (GL.isKillui()) {
                        return 0;
                    }
                    break;
                case (5):
                    if (settings.ints[Settings.maxcheckedlength] > 1) {
                        permutujTWO(denull(hraje.getletters().toArray()));
                    }
                    a = placeWord();
                    max = Math.max(max, a);
                    if (a > 0 && !settings.bools[Settings.multipleword]) {
                        return a;
                    }
                    if (GL.isKillui()) {
                        return 0;
                    }
                    break;
                case (6):
                    if (settings.ints[Settings.maxcheckedlength] > 0) {
                        permutujONE(denull(hraje.getletters().toArray()));
                    }
                    a = placeWord();
                    max = Math.max(max, a);
                    if (a > 0 && !settings.bools[Settings.multipleword]) {
                        return a;
                    }
                    break;
            }
        }

        return max;
    }

    @Override
    protected int permutujDistributed(ArrayList<ServerSinglePlayerThread> distibuted) {
        int a;
        int max = 0;
        int base = settings.ints[Settings.maxcheckedlength];
        if (base > 7) {
            base = 7;
        }
        Random r = new Random();
        int tries = 0;
        while (tries < 3) {
            tries++;
            switch (r.nextInt(base)) {
                case (0):
                    if (settings.ints[Settings.maxcheckedlength] > 6) {
                        permutujDistributed(7, hraje.getletters(), distibuted);
                    }
                    a = placeWord();
                    max = Math.max(max, a);
                    if (a > 0 && !settings.bools[Settings.multipleword]) {
                        return a;
                    }
                    if (GL.isKillui()) {
                        return 0;
                    }
                    break;
                case (1):
                    if (settings.ints[Settings.maxcheckedlength] > 5) {
                        permutujDistributed(6, hraje.getletters(), distibuted);
                    }
                    a = placeWord();
                    max = Math.max(max, a);
                    if (a > 0 && !settings.bools[Settings.multipleword]) {
                        return a;
                    }
                    if (GL.isKillui()) {
                        return 0;
                    }
                    break;
                case (2):
                    if (settings.ints[Settings.maxcheckedlength] > 4) {
                        permutujDistributed(5, hraje.getletters(), distibuted);
                    }
                    a = placeWord();
                    max = Math.max(max, a);
                    if (a > 0 && !settings.bools[Settings.multipleword]) {
                        return a;
                    }
                    if (GL.isKillui()) {
                        return 0;
                    }
                    break;
                case (3):
                    if (settings.ints[Settings.maxcheckedlength] > 3) {
                        permutujDistributed(4, hraje.getletters(), distibuted);
                    }
                    a = placeWord();
                    max = Math.max(max, a);
                    if (a > 0 && !settings.bools[Settings.multipleword]) {
                        return a;
                    }
                    if (GL.isKillui()) {
                        return 0;
                    }
                    break;
                case (4):
                    if (settings.ints[Settings.maxcheckedlength] > 2) {
                        permutujDistributed(3, hraje.getletters(), distibuted);
                    }
                    a = placeWord();
                    max = Math.max(max, a);
                    if (a > 0 && !settings.bools[Settings.multipleword]) {
                        return a;
                    }
                    if (GL.isKillui()) {
                        return 0;
                    }
                    break;
                case (5):
                    if (settings.ints[Settings.maxcheckedlength] > 1) {
                        permutujDistributed(2, hraje.getletters(), distibuted);
                    }
                    a = placeWord();
                    max = Math.max(max, a);
                    if (a > 0 && !settings.bools[Settings.multipleword]) {
                        return a;
                    }
                    if (GL.isKillui()) {
                        return 0;
                    }
                    break;
                case (6):
                    if (settings.ints[Settings.maxcheckedlength] > 0) {
                        permutujDistributed(1, hraje.getletters(), distibuted);
                    }
                    a = placeWord();
                    max = Math.max(max, a);
                    if (a > 0 && !settings.bools[Settings.multipleword]) {
                        return a;
                    }
                    break;
            }
        }

        return max;
    }

}
