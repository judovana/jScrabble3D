package scrabble.ScrabbleAI;

import InetGameControler.ServerSinglePlayerThread;
import LetterPackage.*;
import java.util.Random;
import scrabble.Playerspackage.Player;
import scrabble.Settings.Settings;
import dictionaries.DictionaryWord;
import java.util.ArrayList;
import java.util.Iterator;
import scrabble.DesksPackage.Desks;
import scrabble.GameDesktopPackage.GameDesktop;
import scrabble.GameLogicPackage.GameLogic;
import scrabble.GameLogicPackage.WordsWithScore;
/*
 * BasicAI.java
 *
 * Created on 6. duben 2007, 20:02
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

/**
 *
 * @author Jirka
 */
//debil=vezme prvni permutaci ktera sedne
//student chodi asi jako tohle basic
//master rpochazinejdriv vsecky iterace
public class BasicAI extends Iterations implements AIinterface {

    protected String name = "Student";
    protected LetterSet LS;
    protected GameDesktop GD;
    protected Desks desk;
    protected GameLogic GL;
    protected int total;
    MyOutputConsole.Console console;
    protected Object[] salphabet;
    protected ArrayList<SimpleLetter> lalphabet;

    protected Player hraje;

    //private int pocetuspechu;   //STATISTIKA
    //private int pocetpermutaci; //STATISTIKA
    /*
     * Creates a new instance of BasicAI
     */
    public BasicAI(LetterSet LS, GameDesktop GD, Desks d, GameLogic gl, Settings s, MyOutputConsole.Console console) {
        super(s);
        this.GD = GD;
        this.LS = LS;
        this.desk = d;
        this.GL = gl;
        this.console = console;
        salphabet = LS.getAlphabetWithoutJokers();
        lalphabet = LS.getSimpleLetters(salphabet);
    }

    void bodyPermutation(int xx, int yy, Object[] pole, boolean first) {

        if (xx < 0 || xx >= GD.getWidth() || yy < 0 || yy >= GD.getHeight()) {
            return;
        }
        ArrayList<PlacedLetter> pl = new ArrayList();
        ArrayList<PlacedLetter> jokers = new ArrayList();
        ///horizontal
        {
            int x = xx;
            int y = yy;
            int m/*inus*/ = 0;
            while (x - xx - m < pole.length && x < desk.getWidth()) {

                if (GD.getPole()[x][y] == null) {
                    PlacedLetter npl = new PlacedLetter(x, y, (SimpleLetter) pole[x - xx - m]);
                    pl.add(npl);
                    if (npl.getLetter().getType().equalsIgnoreCase("%")) {
                        jokers.add(new PlacedLetter(x, y, null));
                    }
                } else {
                    if (GD.getPole()[x][y].getType().equalsIgnoreCase("%")) {
                        jokers.add(new PlacedLetter(x, y, null));
                    }
                    m++;
                }
                x++;
            }

            if (m + pl.size() <= settings.ints[Settings.maxcheckedlength] && m + pl.size() + x < desk.getWidth()) {
                if (GL.wattersheadtest(pl, GD) || (first && GL.isMultypleWorConnected(pl))) {
                    GD.setPlacedLetters(pl);

                    if (settings.bools[Settings.aiIgnoreJokers]) {
                        jokers.clear();
                    }
                    if (jokers.isEmpty()) {

                        int c = -1;
                        if (settings.bools[Settings.aiIgnoreDesk]) {
                            c = testInDictionary(GL.getMultypleWords(pl, GD));
                        } else {
                            WordsWithScore wws = GL.getWordsWithScore(pl, GD, desk);
                            c = testInDictionary(wws.poorWords);
                            if (c > 0) {
                                c = wws.score;
                            }
                        }

                        if (c > vysledek.value) {
                            //pocetuspechu++;//STATISTIKA
                            vysledek.slovo = pl;
                            vysledek.x = xx;
                            vysledek.y = yy;
                            vysledek.value = c;
                            vysledek.horiz = 1;
                        }
                    } else {
                        for (SimpleLetter elem : lalphabet) {
                            for (int i = 0; i < jokers.size(); i++) {
                                PlacedLetter npl = new PlacedLetter(jokers.get(i).getX(), jokers.get(i).getY(), elem);
                                jokers.set(i, npl);
                            }
                            int cc = GL.testJokersDirect(GD, jokers, settings);
                            if (cc >= 0) {
                                int c = -1;
                                if (settings.bools[settings.aiIgnoreDesk]) {
                                    c = testInDictionary(GL.getMultypleWords(pl, GD));
                                } else {
                                    WordsWithScore wws = GL.getWordsWithScore(pl, GD, desk);
                                    c = testInDictionary(wws.poorWords);
                                    if (c > 0) {
                                        c = wws.score;
                                    }
                                }
                                if (Math.max(c, cc) > vysledek.value) {
                                    vysledek.slovo = pl;
                                    vysledek.x = xx;
                                    vysledek.y = yy;
                                    vysledek.value = Math.max(c, cc);
                                    vysledek.horiz = 1;
                                }
                            }
                        }

                    }
                    GD.setPlacedLetters(new ArrayList());
                }
            }

            jokers = new ArrayList();
            pl = new ArrayList();
        }
        //verticals 
        {
            int x = xx;
            int y = yy;
            int m/*inus*/ = 0;
            while (y - yy - m < pole.length && y < desk.getHeight()) {

                if (GD.getPole()[x][y] == null) {
                    PlacedLetter npl = new PlacedLetter(x, y, (SimpleLetter) pole[y - yy - m]);
                    pl.add(npl);
                    if (npl.getLetter().getType().equalsIgnoreCase("%")) {
                        jokers.add(new PlacedLetter(x, y, null));
                    }
                } else {
                    if (GD.getPole()[x][y].getType().equalsIgnoreCase("%")) {
                        jokers.add(new PlacedLetter(x, y, null));
                    }
                    m++;
                }
                y++;
            }

            if (m + pl.size() <= settings.ints[Settings.maxcheckedlength] && m + pl.size() + y < desk.getHeight()) {
                if (GL.wattersheadtest(pl, GD) || (first && GL.isMultypleWorConnected(pl))) {
                    GD.setPlacedLetters(pl);
                    /*   
                     int c=testInDictionary(GL.getMultypleWords(pl,GD));
                     if (c>vysledek.value) {
                     vysledek.slovo=pl;
                     vysledek.x=xx;
                     vysledek.y=yy;     
                     vysledek.value=c;
                     vysledek.horiz=false;
                
                     }*/
                    if (settings.bools[Settings.aiIgnoreJokers]) {
                        jokers.clear();
                    }
                    if (jokers.isEmpty()) {
                        int c = -1;
                        if (settings.bools[Settings.aiIgnoreDesk]) {
                            c = testInDictionary(GL.getMultypleWords(pl, GD));
                        } else {
                            WordsWithScore wws = GL.getWordsWithScore(pl, GD, desk);
                            c = testInDictionary(wws.poorWords);
                            if (c > 0) {
                                c = wws.score;
                            }
                        }

                        if (c > vysledek.value) {
                            //pocetuspechu++;//STATISTIKA
                            vysledek.slovo = pl;
                            vysledek.x = xx;
                            vysledek.y = yy;
                            vysledek.value = c;
                            vysledek.horiz = 1;
                        }
                    } else {
                        for (SimpleLetter elem : lalphabet) {
                            for (int i = 0; i < jokers.size(); i++) {
                                PlacedLetter npl = new PlacedLetter(jokers.get(i).getX(), jokers.get(i).getY(), elem);
                                jokers.set(i, npl);
                            }
                            int cc = GL.testJokersDirect(GD, jokers, settings);
                            if (cc >= 0) {
                                int c = -1;
                                if (settings.bools[Settings.aiIgnoreDesk]) {
                                    c = testInDictionary(GL.getMultypleWords(pl, GD));
                                } else {
                                    WordsWithScore wws = GL.getWordsWithScore(pl, GD, desk);
                                    c = testInDictionary(wws.poorWords);
                                    if (c > 0) {
                                        c = wws.score;
                                    }
                                }
                                if (Math.max(c, cc) > vysledek.value) {
                                    vysledek.slovo = pl;
                                    vysledek.x = xx;
                                    vysledek.y = yy;
                                    vysledek.value = Math.max(c, cc);
                                    vysledek.horiz = 1;
                                }
                            }
                        }

                    }
                    GD.setPlacedLetters(new ArrayList());
                }
            }
            jokers = new ArrayList();
            pl = new ArrayList();
        }
    }

    protected void proceedPermutation(Object[] pole) {

        //SimpleLetter[] pole=(SimpleLetter[])p;
        //for (int i = 0; i < pole.length; i++) System.out.print(pole[i]);
        //System.out.println("");
        //pocetpermutaci++;//STATISTIKA
        if (GL.fastIsDeskClear(GD, desk)) {
            switch (new Random().nextInt(2)) {
                case (0):
                    bodyPermutation(desk.startFields.get(0).getX() - (pole.length / 2), desk.startFields.get(0).getY(), pole, true);
                    break;
                case (1):
                    bodyPermutation(desk.startFields.get(0).getX(), desk.startFields.get(0).getY() - (pole.length / 2), pole, true);
                    break;

            }
        } else {
            for (int yy = 0; yy < desk.getHeight(); yy++)//projde plochu
            {
                for (int xx = 0; xx < desk.getWidth(); xx++) {
                    if (GL.isKillui()) {
                        return;
                    }
                    bodyPermutation(xx, yy, pole, false);
                }
            }
        }
    }

    private int proceedJokers(ArrayList<String> a) {
        int vysledek = 0;
        for (String elem : a) {
            if (elem.length() > settings.ints[Settings.maxcheckedlength]) {
                return 0;
            }
            if (elem.length() > settings.ints[Settings.maxcheckedlength]) {
                return 0;
            }
            DictionaryWord dw = GL.getDyctionary().getWordFromDictionary(elem);
            if (dw != null) {
                vysledek += dw.getCost();
            } else {
                return 0;
            }
        }
        return vysledek;
    }

    protected int testInDictionary(ArrayList<String> a) {
        int vysledek = 0;
        //jokers
        //vysledek=cycleJokers(a,alphabet); //useless joker pokus 1
        vysledek = proceedJokers(a);

        return vysledek;
    }

    protected int permutuj() {
        int a;
        int max = 0;
        if (settings.ints[Settings.maxcheckedlength] > 6) {
            permutujSEVEN(denull(hraje.getletters().toArray()));
        }
        a = placeWord();
        max = Math.max(max, a);
        if (GL.isKillui()) {
            return 0;
        }
        if (a > 0 && !settings.bools[Settings.multipleword]) {
            return a;
        }
        if (settings.ints[Settings.maxcheckedlength] > 5) {
            permutujSIX(denull(hraje.getletters().toArray()));
        }
        a = placeWord();
        max = Math.max(max, a);
        if (GL.isKillui()) {
            return 0;
        }
        if (a > 0 && !settings.bools[Settings.multipleword]) {
            return a;
        }
        if (settings.ints[Settings.maxcheckedlength] > 4) {
            permutujFIVE(denull(hraje.getletters().toArray()));
        }
        a = placeWord();
        max = Math.max(max, a);
        if (GL.isKillui()) {
            return 0;
        }
        if (a > 0 && !settings.bools[Settings.multipleword]) {
            return a;
        }
        if (settings.ints[Settings.maxcheckedlength] > 3) {
            permutujFOUR(denull(hraje.getletters().toArray()));
        }
        a = placeWord();
        max = Math.max(max, a);
        if (GL.isKillui()) {
            return 0;
        }
        if (a > 0 && !settings.bools[Settings.multipleword]) {
            return a;
        }
        if (settings.ints[Settings.maxcheckedlength] > 2) {
            permutujTHRE(denull(hraje.getletters().toArray()));
        }
        a = placeWord();
        max = Math.max(max, a);
        if (GL.isKillui()) {
            return 0;
        }
        if (a > 0 && !settings.bools[Settings.multipleword]) {
            return a;
        }
        if (settings.ints[Settings.maxcheckedlength] > 1) {
            permutujTWO(denull(hraje.getletters().toArray()));
        }
        a = placeWord();
        max = Math.max(max, a);
        if (GL.isKillui()) {
            return 0;
        }
        if (a > 0 && !settings.bools[Settings.multipleword]) {
            return a;
        }
        if (settings.ints[Settings.maxcheckedlength] > 0) {
            permutujONE(denull(hraje.getletters().toArray()));
        }
        a = placeWord();
        max = Math.max(max, a);
        if (a > 0 && !settings.bools[Settings.multipleword]) {
            return a;
        }
        if (GL.isKillui()) {
            return 0;
        }
        return max;
    }

    protected int permutujDistributed(ArrayList<ServerSinglePlayerThread> distibuted) {
        int a;
        int max = 0;

        if (settings.ints[Settings.maxcheckedlength] > 6) {
            permutujDistributed(7, hraje.getletters(), distibuted);
        }

        a = placeWord();
        max = Math.max(max, a);
        if (GL.isKillui()) {
            return 0;
        }
        if (a > 0 && !settings.bools[Settings.multipleword]) {
            return a;
        }
        if (settings.ints[Settings.maxcheckedlength] > 5) {
            permutujDistributed(6, hraje.getletters(), distibuted);
        }
        a = placeWord();
        max = Math.max(max, a);
        if (GL.isKillui()) {
            return 0;
        }
        if (a > 0 && !settings.bools[Settings.multipleword]) {
            return a;
        }
        if (settings.ints[Settings.maxcheckedlength] > 4) {
            permutujDistributed(5, hraje.getletters(), distibuted);
        }
        a = placeWord();
        max = Math.max(max, a);
        if (GL.isKillui()) {
            return 0;
        }
        if (a > 0 && !settings.bools[Settings.multipleword]) {
            return a;
        }
        if (settings.ints[Settings.maxcheckedlength] > 3) {
            permutujDistributed(4, hraje.getletters(), distibuted);
        }
        a = placeWord();
        max = Math.max(max, a);
        if (GL.isKillui()) {
            return 0;
        }
        if (a > 0 && !settings.bools[Settings.multipleword]) {
            return a;
        }
        if (settings.ints[Settings.maxcheckedlength] > 2) {
            permutujDistributed(3, hraje.getletters(), distibuted);
        }
        a = placeWord();
        max = Math.max(max, a);
        if (GL.isKillui()) {
            return 0;
        }
        if (a > 0 && !settings.bools[Settings.multipleword]) {
            return a;
        }
        if (settings.ints[Settings.maxcheckedlength] > 1) {
            permutujDistributed(2, hraje.getletters(), distibuted);
        }
        a = placeWord();
        max = Math.max(max, a);
        if (GL.isKillui()) {
            return 0;
        }
        if (a > 0 && !settings.bools[Settings.multipleword]) {
            return a;
        }
        if (settings.ints[Settings.maxcheckedlength] > 0) {
            permutujDistributed(1, hraje.getletters(), distibuted);
        }
        a = placeWord();
        max = Math.max(max, a);
        if (a > 0 && !settings.bools[Settings.multipleword]) {
            return a;
        }
        if (GL.isKillui()) {
            return 0;
        }
        return max;
    }

    public static void main(String[] args) {

    }

    public ArrayList<PlacedLetter> hrej(Player hraje, ArrayList<ServerSinglePlayerThread> distibuted) {
        //pocetpermutaci=0; //STATISTIKA
        // pocetuspechu=0;   //STATISTIKA               
        vysledek = new AIresult();
        this.hraje = hraje;
        int oldnullletters = hraje.nullletters();
        total = 0;
        int predpermutaci;

        SimpleLetter[][] puvodne = GD.getClonedPole();
        console.addItem("thinking");
        do {
            predpermutaci = hraje.nullletters();
            if (distibuted == null) {
                permutuj();
            } else {
                permutujDistributed(distibuted);
            }
        } while (predpermutaci != hraje.nullletters()
                && settings.bools[Settings.multipleword]);
//        System.out.println("pocet permutaci: "+pocetpermutaci); //STATISTIKA
//        System.out.println("pocet uspechu: "+pocetuspechu);    //STATISTIKA
        if (hraje.nullletters() == oldnullletters) {
            hraje.addPass();
            console.addItem(hraje.getJmeno() + " make pass");
            return new ArrayList();
        } else {
            if (hraje.nullletters() == 7 && oldnullletters == 0) {
                total += 50;

            }
            console.addItem("finished (" + total + "):");
            GD.setPlacedLetters(new ArrayList());
            for (int w = 0; w < GD.getWidth(); w++) {
                for (int h = 0; h < GD.getHeight(); h++) {
                    if (puvodne[w][h] == null && GD.getPolePrvek(w, h) != null) {
                        GD.addLeterToBuffer(w, h, GD.getPolePrvek(w, h));
                    }
                }
            }

            GD.setPole(puvodne);
            GD.getPlacedLetters();
            WordsWithScore wws = GL.getWordsWithScore(GD.getPlacedLetters(), GD, desk);

            console.addItem(GD.writeBuffer());
            for (String elem : wws.lines) {
                console.addItem(elem);
            }
            //GD.moveLettersFromBufferToDesk();
            total = wws.score;
            if (hraje.nullletters() == 7 && oldnullletters == 0) {
                total += 50;
                console.addItem("All letters used!");
            }
            hraje.addToScore(total);
            console.addItem("total: " + total);
            hraje.setPasses(0);

            return GD.getPlacedLetters();
        }

    }

    protected int placeWord() {
        if (vysledek.value <= 0) {
            return vysledek.value;
        }

        //if (vysledek.horiz){ neninutne rozdelit
        console.addItem(hraje.getJmeno() + "(AI): " + GL.readLetters(vysledek.slovo));

        for (PlacedLetter slovo : vysledek.slovo) {
            for (int i = 0; i < hraje.getletters().size(); i++) {
                if (hraje.getletters().get(i) == slovo.getLetter()) {
                    hraje.getletters().set(i, null);
                }
            }
        }
        GD.setPlacedLetters(vysledek.slovo);
        WordsWithScore wws = GL.getWordsWithScore(vysledek.slovo, GD, desk);
        for (String line : wws.lines) {
            console.addItem(line);
        }
        GD.moveLettersFromBufferToDesk();
        total += wws.score;
        console.addItem("*" + wws.score);
            //hraje.addToScore(wws.score);

        //}else{       //vertic    }
        int vratise = vysledek.value;
        vysledek = new AIresult();
        return vratise;
    }

//usless jokers pokus 1
    protected int cycleJokers(ArrayList<String> words,/*ArrayList<PlacedLetter> pl kuli umisatenemu zoliku?,*/ Object[] alphabet) {
        int ml = maxLength(words);
        int joker = 0;
        boolean jokers[][] = new boolean[words.size()][ml];

        for (int j = 0; j < words.size(); j++) {
            for (int i = 0; i < words.get(j).length(); i++) {
                if (words.get(j).substring(i, i + 1).equalsIgnoreCase("%")) {
                    jokers[j][i] = true;
                    joker++;
                } else {
                    jokers[j][i] = false;
                }
            }
        }

        if (joker == 0) {
            return proceedJokers(words);
        } else {
            int maxc = 0;
            for (int x = 0; x < alphabet.length; x++) {
                for (int y = 0; y < words.size(); y++) {
                    for (int i = 0; i < words.get(y).length(); i++) {
                        if (jokers[y][i]) {
                            words.set(y, replaceCharAt((String) words.get(y), i, (String) alphabet[x]));
                        }
                    }

                }
                int c = proceedJokers(words);
                if (c > maxc) {
                    maxc = x;
                }
            }
            return maxc;
        }
    }

    private static String replaceCharAt(String s, int pos, String c) {
        return s.substring(0, pos) + c + s.substring(pos + 1);
    }

    private int maxLength(ArrayList<String> words) {
        int vysledek = 0;
        for (String elem : words) {
            if (elem.length() > vysledek) {
                vysledek = elem.length();
            }
        }
        return vysledek;
    }
//konec useles joker pokus 1

    public String getName() {
        return name;
    }

}

/*
 *userfriendly debil jede od 7 k 1 a u kazdeho vezme to prvni co ho napadne
 *user enemy debil od 1 k 7 a u kazdeho co ho napadne (cykli nejdriv vsechny 1? vsechny 2?... nebo cykli (1234567) ?)
 *user friendly student (tohle)
 *user enemy srudent tohle ale 1->7
 *master cykli porad vsecko.... hyper slow
 *
 *cely cyklus 1-7 do while "nezmenen pocet pismenek" ? 
 *userenemy bude asi na hovno kuli "!multiple lwords"
 *
 *co resit 1-7 randomem? nekdy u nekoho?
 *
 *
 *
 *dalsi napad:
 idiot pouzije vzdycky random iteraci
 *student je tohle
 *master procikly vzdycky vsechny
 *
 *jeste vyresit co s while"nezmenili se pismenka"
 *
 *
 *dalswi napad student co bude cyklit od nejkrstdich - rychle; dobre?
 */

/**/

/*
 *distibice
 *
 *1pc zadna
 *2pc 1-4 serv 5-7 comp
 *3pc 1-3 serv 4-5 comp 6-7 comp
 *4pc 1-2 serv 3-4 5-6 7
 *5pc 1-2 3-4 5 6 7
 *6pc 1-2 3 4 5 6 7
 *7pc 1 2 3 4 5 6 7
 */
