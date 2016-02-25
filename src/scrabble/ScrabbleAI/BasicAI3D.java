package scrabble.ScrabbleAI;

import InetGameControler.ServerSinglePlayerThread;
import LetterPackage.*;
import java.util.Random;
import scrabble.Playerspackage.Player;
import scrabble.Settings.Settings;
import dictionaries.DictionaryWord;
import java.util.ArrayList;
import java.util.Iterator;
import scrabble.DesksPackage.*;
import scrabble.GameDesktopPackage.*;
import scrabble.GameLogicPackage.*;
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
public class BasicAI3D extends Iterations implements AIinterface {

    protected String name = "Student";
    protected LetterSet LS;
    protected GameDesktop3D GD;
    protected Desks3D desk;
    protected GameLogic3D GL;
    protected int total;
    MyOutputConsole.Console console;
    protected Object[] salphabet;
    protected ArrayList<SimpleLetter> lalphabet;

    protected Player hraje;

    //private int pocetuspechu;   //STATISTIKA
    //private int pocetpermutaci; //STATISTIKA
    /* Creates a new instance of BasicAI */
    public BasicAI3D(LetterSet LS, GameDesktop GD, Desks d, GameLogic gl, Settings s, MyOutputConsole.Console console) {
        super(s);
        this.GD = (GameDesktop3D) GD;
        this.LS = LS;
        this.desk = (Desks3D) d;
        this.GL = (GameLogic3D) gl;
        this.console = console;
        salphabet = LS.getAlphabetWithoutJokers();
        lalphabet = LS.getSimpleLetters(salphabet);
    }

    //****

    void bodyPermutation(int xx, int yy, int zz, Object[] pole, boolean first) {
        if (xx < 0 || xx >= GD.getWidth() || yy < 0 || yy >= GD.getHeight() || zz < 0 || zz >= GD.getLevels()) {
            return;
        }
        ArrayList<PlacedLetter> pl = new ArrayList();
        ArrayList<PlacedLetter> jokers = new ArrayList();

        ///3d
        {
            int x = xx;
            int y = yy;
            int z = zz;
            int m/*inus*/ = 0;
            while (z - zz - m < pole.length && z < desk.getLevels()) {

                if (GD.getPole3D().get(z)[x][y] == null) {
                    PlacedLetter npl = new PlacedLetter(x, y, z, (SimpleLetter) pole[z - zz - m]);
                    pl.add(npl);
                    if (npl.getLetter().getType().equalsIgnoreCase("%")) {
                        jokers.add(new PlacedLetter(x, y, z, null));
                    }
                } else {
                    if (GD.getPole3D().get(z)[x][y].getType().equalsIgnoreCase("%")) {
                        jokers.add(new PlacedLetter(x, y, z, null));
                    }
                    m++;
                }
                z++;
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
                            vysledek.z = zz;
                            vysledek.value = c;
                            vysledek.horiz = 2;
                        }
                    } else {
                        for (SimpleLetter elem : lalphabet) {
                            for (int i = 0; i < jokers.size(); i++) {
                                PlacedLetter npl = new PlacedLetter(jokers.get(i).getX(), jokers.get(i).getY(), jokers.get(i).getZ(), elem);
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
                                    vysledek.y = zz;
                                    vysledek.value = Math.max(c, cc);
                                    vysledek.horiz = 2;
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

        ///horizontal
        {
            int x = xx;
            int y = yy;
            int z = zz;
            int m/*inus*/ = 0;
            while (x - xx - m < pole.length && x < desk.getWidth()) {

                if (GD.getPole3D().get(z)[x][y] == null) {
                    PlacedLetter npl = new PlacedLetter(x, y, z, (SimpleLetter) pole[x - xx - m]);
                    pl.add(npl);
                    if (npl.getLetter().getType().equalsIgnoreCase("%")) {
                        jokers.add(new PlacedLetter(x, y, z, null));
                    }
                } else {
                    if (GD.getPole3D().get(z)[x][y].getType().equalsIgnoreCase("%")) {
                        jokers.add(new PlacedLetter(x, y, z, null));
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
                            vysledek.z = zz;
                            vysledek.value = c;
                            vysledek.horiz = 1;
                        }
                    } else {
                        for (SimpleLetter elem : lalphabet) {
                            for (int i = 0; i < jokers.size(); i++) {
                                PlacedLetter npl = new PlacedLetter(jokers.get(i).getX(), jokers.get(i).getY(), jokers.get(i).getZ(), elem);
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
                                    vysledek.y = zz;
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
            int z = zz;
            int m/*inus*/ = 0;
            while (y - yy - m < pole.length && y < desk.getHeight()) {

                if (GD.getPole3D().get(z)[x][y] == null) {
                    PlacedLetter npl = new PlacedLetter(x, y, z, (SimpleLetter) pole[y - yy - m]);
                    pl.add(npl);
                    if (npl.getLetter().getType().equalsIgnoreCase("%")) {
                        jokers.add(new PlacedLetter(x, y, z, null));
                    }
                } else {
                    if (GD.getPole3D().get(z)[x][y].getType().equalsIgnoreCase("%")) {
                        jokers.add(new PlacedLetter(x, y, z, null));
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
                            vysledek.z = zz;
                            vysledek.value = c;
                            vysledek.horiz = 0;
                        }
                    } else {
                        for (SimpleLetter elem : lalphabet) {
                            for (int i = 0; i < jokers.size(); i++) {
                                PlacedLetter npl = new PlacedLetter(jokers.get(i).getX(), jokers.get(i).getY(), jokers.get(i).getZ(), elem);
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
                                    vysledek.z = zz;
                                    vysledek.value = Math.max(c, cc);
                                    vysledek.horiz = 0;
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
            switch (new Random().nextInt(3)) {
                case (0):
                    bodyPermutation(desk.startFields.get(0).getX() - (pole.length / 2), desk.startFields.get(0).getY(), desk.startFields.get(0).getZ(), pole, true);
                    break;
                case (1):
                    bodyPermutation(desk.startFields.get(0).getX(), desk.startFields.get(0).getY() - (pole.length / 2), desk.startFields.get(0).getZ(), pole, true);
                    break;
                case (2):

                    bodyPermutation(desk.startFields.get(0).getX(), desk.startFields.get(0).getY(), desk.startFields.get(0).getZ() - (pole.length / 2), pole, true);
                    break;

            }
        } else {
            for (int zz = 0; zz < desk.getLevels(); zz++)//projde plochy
            {
                for (int yy = 0; yy < desk.getHeight(); yy++)//projde plochu
                {
                    for (int xx = 0; xx < desk.getWidth(); xx++) {
                        if (GL.isKillui()) {
                            return;
                        }
                        bodyPermutation(xx, yy, zz, pole, false);
                    }
                }
            }
        }
    }

    private int proceedJokers(ArrayList<String> a) {
        int vysledek = 0;
        for (String elem : a) {
            if (elem.length() > settings.ints[settings.maxcheckedlength]) {
                return 0;
            }
            if (elem.length() > settings.ints[settings.maxcheckedlength]) {
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

        ArrayList<SimpleLetter[][]> puvodne = GD.getClonedPole3D();
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
            for (int l = 0; l < GD.getLevels(); l++) {
                for (int w = 0; w < GD.getWidth(); w++) {
                    for (int h = 0; h < GD.getHeight(); h++) {
                        if (puvodne.get(l)[w][h] == null && GD.getPolePrvek(w, h, l) != null) {
                            GD.addLeterToBuffer(w, h, l, GD.getPolePrvek(w, h, l));
                        }
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
            // GD.moveLettersFromBufferToDesk();
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

    public String getName() {
        return name;
    }

}
