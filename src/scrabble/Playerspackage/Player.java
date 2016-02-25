/*
 * Player.java
 *
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package scrabble.Playerspackage;

import InetGameControler.JoinedPlayer;
import LetterPackage.LetterSet;
import LetterPackage.SimpleLetter;
import scrabble.ScrabbleAI.AIinterface;
import scrabble.Settings.Settings;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import scrabble.GameDesktopPackage.GameDesktop;
import scrabble.GameLogicPackage.GameLogic;

/**
 *
 * @author Jirka
 */
public class Player {

    private String jmeno;
    private LetterSet letterSet;
    ;
    private ArrayList<SimpleLetter> letters;
    private int selected = 0;
    private int Score = 0;
    private int AI;//0=hrac,1=debil,2=pokrocili...n=master?
    public int jokers;
    private AIinterface aiEngine;
    private int passes;
    boolean local = true;
    private JoinedPlayer network;

    public void setNetwork(JoinedPlayer network) {
        this.network = network;
    }

    public JoinedPlayer getNetwork() {
        return network;
    }

    private Settings settings;

    public int nullletters;

    public void setLetters(ArrayList<SimpleLetter> letters) {
        this.letters = letters;
    }

    public void setAI(int AI) {
        this.AI = AI;
    }

    public void setJmeno(String jmeno) {
        this.jmeno = jmeno;
    }

    public void setSelected(int selected) {
        this.selected = selected;
    }

    public int getSelected() {
        return selected;
    }

    public void setSettings(Settings settings) {
        this.settings = settings;
    }

    public void setLetterSet(LetterSet letterSet) {
        this.letterSet = letterSet;
    }

    public LetterSet getLetterSet() {
        return letterSet;
    }

    public Settings getSettings() {
        return settings;
    }

    public boolean isLocal() {
        return local;
    }

    public void setLocal(boolean local) {
        this.local = local;
    }

    public int getPasses() {
        return passes;
    }

    public void setPasses(int passes) {
        this.passes = passes;
    }

    public void addPass() {
        this.passes++;
    }

    public AIinterface getAiEngine() {
        return aiEngine;
    }

    public void setAiEngine(AIinterface aiEngine) {
        this.aiEngine = aiEngine;
    }

    public int getScore() {
        return Score;
    }

    public void setScore(int Score) {
        this.Score = Score;
    }

    public int getAI() {
        return AI;
    }

    public int addToScore(int howmany) {
        Score += howmany;
        return Score;
    }

    public String getJmeno() {
        return jmeno;
    }

    public ArrayList<SimpleLetter> getletters() {
        return letters;
    }

    public SimpleLetter getPismeno(int index) {
        if (index < 0 || index >= letters.size()) {
            return null;
        }
        return letters.get(index);
    }

    public int jokerCount() {
        int navrat = 0;
        for (int x = 0; x <= 6; x++) {
            if (getPismeno(x) != null) {
                if (getPismeno(x).getType().equals("%")) {
                    navrat++;
                }
            }
        }
        return navrat;
    }

    public int nullletters() {
        int navrat = 0;
        for (int x = 0; x <= 6; x++) {
            if (getPismeno(x) == null) {
                navrat++;
            }
        }
        return navrat;
    }

    /*
     * Creates a new instance of Player
     */
    public Player(String jmeno, LetterSet set, int AI, Settings settings) {
        this.jmeno = jmeno;
        this.settings = settings;
        this.AI = AI;
        this.letterSet = set;
        letters = new ArrayList<SimpleLetter>();
        Score = 0;//??
        if (letterSet != null) {
            for (int x = 0; x <= 6; x++) {
                letters.add(letterSet.getRandomLetterFromPocket());
            }
        }

    }

    @Override
    public String toString() {
        return jmeno + "[" + getIdentity() + "]" + " :  " + String.valueOf(Score);
    }

    public boolean returnToLetters(SimpleLetter byvaly) {
        if (byvaly == null) {
            return true;
        }
        for (int x = 0; x < letters.size(); x++) {
            if (letters.get(x) == null) {
                letters.set(x, byvaly);
                return true;
            }
        }
        letters.add(byvaly);
        return false;

    }

    public void refulLetters(GameLogic gl) {
        if (gl.AreadLine(this)) {
            return;
        }
        for (int x = 0; x <= 6; x++) {
            if (getPismeno(x) == null) {
                SimpleLetter a = letterSet.getRandomLetterFromPocket();
                if (this.AI > 0 && a != null && settings.bools[Settings.aiIgnoreJokers] && a.getType().equalsIgnoreCase("%")) {
                    SimpleLetter b = letterSet.getRandomLetterFromPocket();
                    letterSet.addLetterToPocket(a);
                    letters.set(x, b);
                } else {
                    letters.set(x, a);
                }
            }
        }

    }

    public void setLetterDirect(int x, SimpleLetter e) {
        this.letters.set(x, e);
    }

    public void allLettersBack(GameDesktop gameDesktop) {
        while (gameDesktop.getPlacedLetters().size() > 0) {
            SimpleLetter alfa = gameDesktop.PopLetter();
            this.returnToLetters(alfa);
        }
        if (gameDesktop.getSelected() != null) {
            this.returnToLetters(gameDesktop.getSelected());
        }
        gameDesktop.setSelected(null);
    }

    public String getIdentity() {
        /* switch (AI) {
         case(0): return "Human";
         case(1): return "Idiot";
         case(2): return "Student";
         case(3): return "Master";
         case(4): return "CommitedSucide";
         case(5): return "SuperCommited";
         case(6): return "Random";
         default: return"unknown";
           
         }*/
        if (AI == 0) {
            return "Human";
        } else if (aiEngine == null) {
            return "Random";
        } else {
            return aiEngine.getName();
        }
    }

    public void writePlayer(BufferedWriter write, int id) throws IOException {

        //write= new BufferedWriter(new FileWriter(f)); 
        write.write("<player id='" + id + "'>\r\n");
        write.write("<name>" + jmeno + "</name>\r\n");
        write.write("<selected>" + selected + "</selected>\r\n");
        write.write("<Score>" + Score + "</Score>\r\n");
        write.write("<AI>" + AI + "</AI>\r\n");
        write.write("<jokers>" + jokers + "</jokers>\r\n");
        write.write("<passes>" + passes + "</passes>\r\n");
        write.write("<local>" + Boolean.toString(local) + "</local>\r\n");
        write.write("<nullletters>" + nullletters + "</nullletters>\r\n");
        write.write("<letters>\r\n");
        for (SimpleLetter elem : letters) {
            if (elem != null) {
                write.write("<letter>");
                write.write(elem.toString());
                write.write("</letter>");
            }
        }

        write.write("</letters>\r\n");

        write.write("</player>\r\n");

    }

}
