package scrabble.GameLogicPackage;

import InetGameControler.IC;
import InetGameControler.ServerSinglePlayerThread;
import LetterPackage.LetterSet;
import LetterPackage.PlacedLetter;
import MyOutputConsole.Console;
import OGLaddons.OGLdialog;
import scrabble.Playerspackage.Player;
import scrabble.Settings.Settings;
import dictionaries.DictionaryWord;
import dictionaries.MyDictionary;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Random;
import requesty.VocabularyServer;
import requesty.wordTestResult;
import scrabble.*;
import scrabble.DesksPackage.Desks;
import scrabble.GameDesktopPackage.GameDesktop;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;
/*
 * GameLogic.java
 *
 * Created on 12. b�ezen 2007, 16:44
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

/**
 *
 * @author Jirka
 */
public class GameLogic {

    volatile private int voteok;
    volatile private int votecancel;
    volatile private int voteno;

    volatile public boolean killui;
    protected ArrayList<Player> players;
    protected Console console;
    protected Main api;
    protected dictionaries.MyDictionary dyctionary;
    protected requesty.Servers mcs;
    protected BufferedReader read = null;
    protected ArrayList<String> zadani = null;
    public int ukazatel = 0;

    public GameLogic() {

    }

    public boolean isKillui() {
        return killui;
    }

    public void setKillui(boolean killui) {
        this.killui = killui;
    }

    /**
     * Creates a new instance of GameLogic
     *
     * @param api
     */
    public GameLogic(Main api) {
        console = api.getConsole();
        this.api = api;

        if (!api.getSettings().strings[Settings.words].trim().equals("")) {
            zadani = new ArrayList();
            File f = new File(api.getSettings().strings[Settings.words]);
            try {
                read = new BufferedReader(new FileReader(f));
                String a;

                a = read.readLine();
                while (a != null) {
                    zadani.add(a);
                    a = read.readLine();
                }

                read.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            ;
        }

    }

    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
        this.players = (ArrayList<Player>) players.clone();
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    protected boolean areAllMarked(ArrayList<PlacedLetter> vector) {
        for (PlacedLetter vector1 : vector) {
            if (vector1.mark == false) {
                return false;
            }
        }
        return true;
    }

    public int countMarked(ArrayList<PlacedLetter> vector) {
        int vysledek = 0;
        for (PlacedLetter vector1 : vector) {
            if (vector1.mark == true) {
                vysledek++;
            }
        }
        return vysledek;
    }

    public boolean isMultypleWorConnected(ArrayList<PlacedLetter> vector) {
        for (PlacedLetter vector1 : vector) {
            vector1.mark = false;
        }
        vector.get(0).mark = true;
        int oldMarked = -1;
        for (int y = 0; y < vector.size() * 2; y++) {
            oldMarked = countMarked(vector);
            for (int x = 0; x < vector.size(); x++) {
                if (oneLetterNearToOneOneMarked(vector.get(x), vector)) {
                    vector.get(x).mark = true;
                }

            }
        }
        if (areAllMarked(vector)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean wattersheadtest(ArrayList<PlacedLetter> vector, GameDesktop turn) {
        for (PlacedLetter vector1 : vector) {
            vector1.mark = false;
        }
        int oldMarked = -1;
        for (int y = 0; y < vector.size() * 2; y++) {
            oldMarked = countMarked(vector);
            for (PlacedLetter vector1 : vector) {
                if (oneLetterNearToOneOneBoard(vector1, turn) || oneLetterNearToOneOneMarked(vector1, vector)) {
                    vector1.mark = true;
                }
            }
        }
        return areAllMarked(vector);
    }

    protected boolean areHolesInSortedBuffer(ArrayList<PlacedLetter> vector, GameDesktop turn) {
        if (vector.get(0).getUsedCompare() == PlacedLetter.COMPARE_BY_X) {
            int x = vector.get(0).getX() + 1;
            int y = vector.get(0).getY();
            while (turn.getLetterOnBoardIndependentOnBuffer(x, y) != null) {
                x++;
            }
            for (PlacedLetter vector1 : vector) {
                if (vector1.getX() > x) {
                    return true;
                }
            }

        }
        if (vector.get(0).getUsedCompare() == PlacedLetter.COMPARE_BY_Y) {
            int x = vector.get(0).getX();
            int y = vector.get(0).getY() - 1;
            while (turn.getLetterOnBoardIndependentOnBuffer(x, y) != null) {
                y--;
            }
            for (PlacedLetter vector1 : vector) {
                if (vector1.getY() < y) {
                    return true;
                }
            }

        }

        return false;

    }

    protected boolean areHolesInSortedBufferOfFirstTurn(ArrayList<PlacedLetter> vector) {
        if (vector.get(0).getUsedCompare() == PlacedLetter.COMPARE_BY_X) {
            for (int x = 0; x < vector.size() - 1; x++) {
                if (vector.get(x).getX() + 1 != vector.get(x + 1).getX()) {
                    return true;
                }
            }
        }

        if (vector.get(0).getUsedCompare() == PlacedLetter.COMPARE_BY_Y) {
            for (int x = 0; x < vector.size() - 1; x++) {
                if (vector.get(x).getY() - 1 != vector.get(x + 1).getY()) {
                    return true;
                }
            }

        }

        return false;

    }

    protected boolean letterOnStart(ArrayList<PlacedLetter> vector, Desks desk) {
        for (PlacedLetter vector1 : vector) {
            if ( //je nejake policko na startu?
                    desk.getPole()[vector1.getX()][vector1.getY()] == 1) {
                return true;
            }
        }
        return false;

    }

    protected boolean allLettersInOneVector(ArrayList<PlacedLetter> vector) {
        boolean ok = true;//jsou vsecky pismenka v jednom vektoru?
        for (int x = 0; x < vector.size() - 1; x++) {
            if (vector.get(x).getX() != vector.get(x + 1).getX()) {
                for (int y = 0; y < vector.size() - 1; y++) {
                    if (vector.get(y).getY() != vector.get(y + 1).getY()) {
                        ok = false;
                        return false;
                    }
                }
                break;
            }
        }
        return ok;
    }

    protected int isXvector(ArrayList<PlacedLetter> vector) {
        //jsou pismenka x vector? pocita uz s tim ze jsou ve vectoru v jedny rade
        for (int x = 0; x < vector.size() - 1; x++) {
            if (vector.get(x).getX() != vector.get(x + 1).getX() || vector.get(x).getZ() != vector.get(x + 1).getZ()) {
                for (int y = 0; y < vector.size() - 1; y++) {
                    if (vector.get(y).getY() != vector.get(y + 1).getY() || vector.get(y).getZ() != vector.get(y + 1).getZ()) {
                        return 2;
                    }
                }
                return 1;
            }
        }
        return 0;

        //1==x
        //0==y
        //z==2
    }

    protected boolean letterNearToOneOneBoard(ArrayList<PlacedLetter> vector, GameDesktop turn) {
        boolean ok = false;//dotyka se alsepon jedno z pismenek nejakeho jiz umisteneho?
        for (PlacedLetter vector1 : vector) {
            if (turn.getPolePrvek(vector1.getX() + 1, vector1.getY()) != null || turn.getPolePrvek(vector1.getX(), vector1.getY() + 1) != null || turn.getPolePrvek(vector1.getX(), vector1.getY() - 1) != null || turn.getPolePrvek(vector1.getX() - 1, vector1.getY()) != null) {
                ok = true;
                break;
            }
        }
        return ok != false;
    }

    protected boolean oneLetterNearToOneOneBoard(PlacedLetter v, GameDesktop turn) {

        return turn.getPolePrvek(v.getX() + 1, v.getY()) != null
                || turn.getPolePrvek(v.getX(), v.getY() + 1) != null
                || turn.getPolePrvek(v.getX(), v.getY() - 1) != null
                || turn.getPolePrvek(v.getX() - 1, v.getY()) != null;
    }

    protected boolean oneLetterNearToOneOneMarked(PlacedLetter v, ArrayList<PlacedLetter> vector) {

        return ((GameDesktop.getAnyNewhereInVector(vector, v.getX() + 1, v.getY()) != null) && (GameDesktop.getAnyNewhereInVector(vector, v.getX() + 1, v.getY()).mark == true))
                || ((GameDesktop.getAnyNewhereInVector(vector, v.getX(), v.getY() + 1) != null) && (GameDesktop.getAnyNewhereInVector(vector, v.getX(), v.getY() + 1).mark == true))
                || ((GameDesktop.getAnyNewhereInVector(vector, v.getX(), v.getY() - 1) != null) && (GameDesktop.getAnyNewhereInVector(vector, v.getX(), v.getY() - 1).mark == true))
                || ((GameDesktop.getAnyNewhereInVector(vector, v.getX() - 1, v.getY()) != null) && (GameDesktop.getAnyNewhereInVector(vector, v.getX() - 1, v.getY()).mark == true));
    }

    protected boolean isStartAtMiddle(ArrayList<PlacedLetter> vector, Desks desk) {
        if (vector.size() % 2 == 1) {
            int index = (vector.size() - 1) / 2;
            if (isXvector(vector) == 1) {
                return desk.getPole()[vector.get(index).getX()][vector.get(0).getY()] == 1;
            } else {
                return desk.getPole()[vector.get(0).getX()][vector.get(index).getY()] == 1;

            }
        } else {

            int index = (vector.size()) / 2;
            if (isXvector(vector) == 1) {
                return desk.getPole()[vector.get(index).getX()][vector.get(0).getY()] == 1
                        || desk.getPole()[vector.get(index - 1).getX()][vector.get(0).getY()] == 1;
            } else {
                return desk.getPole()[vector.get(0).getX()][vector.get(index).getY()] == 1
                        || desk.getPole()[vector.get(0).getX()][vector.get(index - 1).getY()] == 1;

            }

        }
    }

    protected int verify(GameDesktop turn, Desks desk, Settings settings) {
        if (turn.getSelected() != null) {
            return -8;
        }
        if (turn.getPlacedLetters().isEmpty()) {
            return -5;
        }
        if (isDeskClear(turn)) {
            //je nejake policko na startu?
            if (!letterOnStart(turn.getPlacedLetters(), desk)) {
                return -1;
            }
            if (turn.getPlacedLetters().size() == 1) {
                return -6;
            }
            if (!settings.bools[Settings.multipleword]) {
                if (!allLettersInOneVector(turn.getPlacedLetters())) {
                    return -2;
                }
                ///dalsi oerace vyzaduji setrideni
                sortLetters(turn.getPlacedLetters());
                if (!isStartAtMiddle(turn.getPlacedLetters(), desk)) {
                    return -4;
                }
                if (areHolesInSortedBufferOfFirstTurn(turn.getPlacedLetters())) {
                    return -7;
                }
            } else {
                //pouze test s marks
                if (!isMultypleWorConnected(turn.getPlacedLetters())) {
                    return -12;
                }

            }
        } else {

            if (settings.bools[Settings.multipleword]) {
                if (!wattersheadtest(turn.getPlacedLetters(), turn)) {
                    return -11;
                }
                ///kazdy pismenko vedle nejakyhojinyho (bud na place nebo jinyho z palcedletters) 
                ///se "marks" protoze aspon jedno se pak  musi dotykat vysledku..watershed:)
                //testy 
                //ArrayList<String> a;
                //a=getMultypleWords(turn.getPlacedLetters(),turn);
                //for (int x=0;x<a.size();x++) console.addItem(a.get(x));
            } else {
                //jsou vsecky pismenka v jednom vektoru?       
                if (!allLettersInOneVector(turn.getPlacedLetters())) {
                    return -2;
                }

                //dotyka se alsepon jedno z pismenek nejakeho jiz umisteneho?
                if (!letterNearToOneOneBoard(turn.getPlacedLetters(), turn)) {
                    return -3;
                }

                ///kazdy pismenko vedle nejakyhojinyho (bud na place nebo jinyho z placedletetrs) 
                ///dalsi oerace vyzaduji setrideni
                sortLetters(turn.getPlacedLetters());

                if (areHolesInSortedBuffer(turn.getPlacedLetters(), turn)) {
                    return -7;
                }
            }

        }
        return 0;
    }

    public ArrayList<String> vytvorDialogKontroly(String hraje, String vidi, ArrayList<String> slova, GameDesktop turn) {
        ArrayList<String> dl = new ArrayList<String>();
        dl.add(vidi + ",");
        dl.add("player " + hraje + " have created these word(s)");
        dl.addAll(slova);
        dl.add("with these letters:");
        String s = "";
        for (int y = 0; y < turn.getPlacedLetters().size(); y++) {
            s = s + odhackujZnak(turn.getPlacedLetters().get(y).getLetter().getType()) + ", ";
        }
        s = s.substring(0, s.length() - 2);
        dl.add("(" + s + ")");
        dl.add("do you agree with this turn?");
        return dl;
    }

    protected int checks(Desks desk, GameDesktop turn, Settings settings, Player hraje, boolean simulation) {
        if (settings.bools[Settings.playercheck]) {
            int agreed = 0;
            int[] btns = new int[2];
            btns[0] = OGLdialog.MB_OK;
            btns[1] = OGLdialog.MB_NO;
            ArrayList<String> r = getMultypleWords(turn.getPlacedLetters(), turn);
            if (!settings.isInet()) {
                for (Player player : players) {
                    if (player.getAI() == 0) {
                        ArrayList<String> dl = vytvorDialogKontroly(hraje.getJmeno(), player.getJmeno(), r, turn);
                        api.getOglDialog().execute(btns, dl, Main.DM.getWidth() / 2, Main.DM.getHeight() / 2);
                        if (api.globalReaction.reaction == OGLdialog.MR_OK) {
                            console.addItem(player.getJmeno() + " aggred");
                            agreed++;
                        } else if (api.globalReaction.reaction == OGLdialog.MR_NO) {
                            console.addItem(player.getJmeno() + " disaggre");
                        } else {
                            console.addItem(player.getJmeno() + " forced error");
                        }
                    } //for
                }
            } else {
                nulujVote();
                for (ServerSinglePlayerThread elem : api.getServerThreads()) {
                    ArrayList<String> dl = vytvorDialogKontroly(hraje.getJmeno(), elem.getPlayer().getJmeno(), r, turn);
                    elem.messageToPlayer(IC.commands[22] + "-" + dialogToStr(dl));
                }
                for (Player player : players) {
                    if (player.getAI() == 0 && player.isLocal()) {
                        ArrayList<String> dl = vytvorDialogKontroly(hraje.getJmeno(), player.getJmeno(), r, turn);
                        api.ade = new AsynchronousDialogExecuter(btns, dl, Main.DM.getWidth() / 2, Main.DM.getHeight() / 2);
                        api.globalReaction.reaction = OGLdialog.MR_NOTHING;
                        try {
                            Thread.sleep(100);

                            if (api.getThread() == Thread.currentThread()) {
                                while (OGLdialog.MR_NOTHING == api.globalReaction.reaction) {
                                    api.redirectedLoopKore();
                                }
                            } else {
                                while (OGLdialog.MR_NOTHING == api.globalReaction.reaction) {
                                    Thread.sleep(200);
                                }
                            }

                        } catch (InterruptedException ex) {
                            ex.printStackTrace();
                        }
                        if (api.globalReaction.reaction == OGLdialog.MR_OK) {
                            console.addItem(player.getJmeno() + " aggred");
                            agreed++;
                        } else if (api.globalReaction.reaction == OGLdialog.MR_NO) {
                            console.addItem(player.getJmeno() + " disaggre");
                        } else {
                            console.addItem(player.getJmeno() + " forced error");
                        }
                    }
                }
                int foreignhumans = getForeignHumans();
                Thread wait = new Thread() {
                    @Override
                    public void run() {
                        for (ServerSinglePlayerThread elem : api.getServerThreads()) {
                            elem.SynchornousReadln();
                        }
                    }
                };
                wait.start();
                waitForVotes(0, foreignhumans, false);
                agreed += voteok;
            }
            int a = getHumans();
            if (a == 2) {
                a = 3;
            }
            a++;

            if (agreed < a / 2) {
                return -9;
            }
        }

        if (settings.bools[Settings.inetcheck]) {
            int fuj = testWordsOnWeb(getMultypleWords(turn.getPlacedLetters(), turn), settings, simulation);
            if (fuj > 0) {
                return -13;
            }
        }

        if (settings.bools[Settings.wordcheck]) {
            int fuj = testWordsInDictionary(getMultypleWords(turn.getPlacedLetters(), turn), settings, simulation);
            if (fuj > 0) {
                return -10;
            }
        }
        return 0;
    }

    protected int compute(GameDesktop turn, Desks desk, Player hraje) {

        WordsWithScore wws = getWordsWithScore(turn.getPlacedLetters(), turn, desk);
        for (String line : wws.lines) {
            console.addItem(line);
        }

        int suma = wws.score;
        if (hraje.nullletters() == 7 && hraje.nullletters == 0) {
            suma = suma + 50;
            console.addItem("All letters used! amazing => 50*");
        }
        return suma;
    }

    public int evaulateTurn(Desks desk, GameDesktop turn, Settings settings, Player hraje, boolean simulation) {
        int c = verify(turn, desk, settings);
        if (c < 0) {
            return c;
        }

        c = checks(desk, turn, settings, hraje, simulation);
        if (c < 0) {
            return c;
        }

        return compute(turn, desk, hraje);
    }

    public int getHumans() {
        int vysledek = 0;
        for (Player player : players) {
            if (player.getAI() == 0) {
                vysledek++;
            }
        }
        return vysledek;
    }

    public boolean isDeskClear(GameDesktop turn) {
        for (int x = 0; x < turn.getWidth(); x++) {
            for (int y = 0; y < turn.getHeight(); y++) {
                if (turn.getPole()[x][y] != null) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean fastIsDeskClear(GameDesktop turn, Desks desk) {
        for (PlacedLetter elem : desk.startFields) {
            if (turn.getPolePrvek(elem.getX(), elem.getY()) != null) {
                return false;
            }
        }

        return true;
    }

    protected int nextPlayerIndex(Player hraje) {
        if (hraje == null) {
            return 0;
        }
        int index = 0;
        for (int x = 0; x < players.size(); x++) {
            if (players.get(x) == hraje) {
                index = x;
                break;
            }
        }
        index++;
        if (index >= players.size()) {
            index = 0;
        }
        return index;
    }

    protected int previousPlayerIndex(Player hraje) {
        if (hraje == null) {
            return 0;
        }
        int index = 0;
        for (int x = 0; x < players.size(); x++) {
            if (players.get(x) == hraje) {
                index = x;
                break;
            }
        }
        index--;
        if (index < 0) {
            index = players.size() - 1;
        }
        return index;
    }

    public int thisPlayerIndex(Player hraje) {
        if (hraje == null) {
            return 0;
        }
        int index = 0;
        for (int x = 0; x < players.size(); x++) {
            if (players.get(x) == hraje) {
                index = x;
                break;
            }
        }
        return index;
    }

    public Player nextPlayer(Player hraje) {

        if (players.size() <= 0) {
            return null;
        }
        if (hraje == null) {
            players.get(0).jokers = players.get(0).jokerCount();
            players.get(0).nullletters = players.get(0).nullletters();
            // AreadLine(players.get(0));
            return players.get(0);
        }
        int index;
        index = nextPlayerIndex(hraje);
        players.get(index).jokers = players.get(index).jokerCount();
        players.get(index).nullletters = players.get(index).nullletters();
//areAreadLine(hraje);

        return players.get(index);
    }

    public Player vidimForPlayer(Player hraje) {
        if (players.size() <= 0) {
            return null;
        }
        Player vidim = hraje;
        int iterace = 0;

        while (!((vidim.isLocal() && vidim.getAI() == 0) || (vidim == hraje && iterace > 0))) {
            vidim = players.get(nextPlayerIndex(vidim));
            iterace++;
        }

        return vidim;

    }

    public boolean randomizePlayers(int seriosity) {
        if (players == null) {
            return false;
        }
        Random r = new Random();
        for (int x = 0; x <= seriosity; x++) {
            int odkud = r.nextInt(players.size());
            int kam = r.nextInt(players.size());
            Player p = players.get(kam);
            players.set(kam, players.get(odkud));
            players.set(odkud, p);
        }
        return true;

    }

    protected void setVectorOrientation(ArrayList<PlacedLetter> unsorted, int orientation) {
        for (PlacedLetter unsorted1 : unsorted) {
            unsorted1.setUsedCompare(orientation);
        }
    }

    protected void sortOrientidVector(ArrayList<PlacedLetter> sorted) {
        if (isXvector(sorted) == 1) {
            Collections.sort(sorted);
        } else {
            Collections.sort(sorted);
            Collections.reverse(sorted);
        }
    }

    protected ArrayList<PlacedLetter> sortLetters(ArrayList<PlacedLetter> placedLetters) {

        ArrayList<PlacedLetter> unsorted = placedLetters;
        if (isXvector(unsorted) == 1) {

            setVectorOrientation(unsorted, PlacedLetter.COMPARE_BY_X);
            sortOrientidVector(unsorted);
        } else {
            setVectorOrientation(unsorted, PlacedLetter.COMPARE_BY_Y);
            sortOrientidVector(unsorted);
        }
        return placedLetters;
    }


    /*protected  ArrayList<String> getWords(ArrayList<PlacedLetter> vector,GameDesktop turn){ stare na hovno, neumi multiple  a pritom se multyple lisi jen v podminakch, ne v analyze
     ArrayList<String> lines= new ArrayList<String>();
     String w;
    
     if (vector.size()==1)   {
       
     w=getHorizontalWordFrom(vector,0,turn,false);
     if (!w.trim().equalsIgnoreCase("")) lines.add(w);
     w=getVerticalWordFrom(vector,0,turn,false);
     if (!w.trim().equalsIgnoreCase("")) lines.add(w);

     }else{
    
     if (api.getSettings().bools[Settings.multipleword])    {
     //multiple words
     boolean [] marks=new boolean[vector.size()];
     for(int x=0;x<marks.length;x++)marks[x]=false;
     marks[0]=true;
     w=getHorizontalWordFrom(vector,0,turn,true);
     if (!w.trim().equalsIgnoreCase("")) lines.add(w);

     for (int s=0;s<vector.size();s++){
     w=getVerticalWordFrom(vector,s,turn,false);
     if (!w.trim().equalsIgnoreCase("")) lines.add(w);
     }
     //k0onc multiple words
     }else{
     boolean xVector=isXvector(vector);

    
     if (xVector){
     w=getHorizontalWordFrom(vector,0,turn,true);
     if (!w.trim().equalsIgnoreCase("")) lines.add(w);

     for (int s=0;s<vector.size();s++){
     w=getVerticalWordFrom(vector,s,turn,false);
     if (!w.trim().equalsIgnoreCase("")) lines.add(w);
     }

     }else{
    
     w=getVerticalWordFrom(vector,0,turn,true);
     if (!w.trim().equalsIgnoreCase("")) lines.add(w);

     for (int s=0;s<vector.size();s++)    
     w=getHorizontalWordFrom(vector,s,turn,false);
     if (!w.trim().equalsIgnoreCase("")) lines.add(w);
        
     }
     }    
     }
     return lines;    
    
     }*/
    protected ArrayList<PlacedLetter> getHorizontalWordFrom(ArrayList<PlacedLetter> vector, PlacedLetter from, GameDesktop turn, boolean alwaysfirst) {

        ArrayList<PlacedLetter> word = new ArrayList<PlacedLetter>();
        int x = from.getX();
        int y = from.getY();
        x = x + 1;
        while (turn.getLetterOnBoardIndependentOnBuffer(x, y) != null) {
            if (turn.getPolePrvek(x, y) != null) {
                word.add(new PlacedLetter(x, y, turn.getLetterOnBoardIndependentOnBuffer(x, y)));
            } else {
                word.add(turn.getAnyNewhere(x, y));
            }
            GameDesktop.getAnyNewhereInVector(word, x, y).mark = true;
            x++;
        }

        x = from.getX();
        y = from.getY();
        x = x - 1;
        while (turn.getLetterOnBoardIndependentOnBuffer(x, y) != null) {
            if (turn.getPolePrvek(x, y) != null) {
                word.add(new PlacedLetter(x, y, turn.getLetterOnBoardIndependentOnBuffer(x, y)));
            } else {
                word.add(turn.getAnyNewhere(x, y));
            }
            GameDesktop.getAnyNewhereInVector(word, x, y).mark = true;
            x--;
        }

        if (alwaysfirst) {
            word.add(from);
        } else if (word.size() > 0) {
            word.add(from);
        }

//return readLetters(word);
        return word;
    }

    protected ArrayList<PlacedLetter> getVerticalWordFrom(ArrayList<PlacedLetter> vector, PlacedLetter from, GameDesktop turn, boolean alwaysfirst) {

        ArrayList<PlacedLetter> word = new ArrayList<PlacedLetter>();
        int x = from.getX();
        int y = from.getY();
        y = y + 1;
        while (turn.getLetterOnBoardIndependentOnBuffer(x, y) != null) {
            if (turn.getPolePrvek(x, y) != null) {
                word.add(new PlacedLetter(x, y, turn.getLetterOnBoardIndependentOnBuffer(x, y)));
            } else {
                word.add(turn.getAnyNewhere(x, y));
            }
            GameDesktop.getAnyNewhereInVector(word, x, y).mark = true;
            y++;
        }

        x = from.getX();
        y = from.getY();
        y = y - 1;
        while (turn.getLetterOnBoardIndependentOnBuffer(x, y) != null) {
            if (turn.getPolePrvek(x, y) != null) {
                word.add(new PlacedLetter(x, y, turn.getLetterOnBoardIndependentOnBuffer(x, y)));
            } else {
                word.add(turn.getAnyNewhere(x, y));
            }
            GameDesktop.getAnyNewhereInVector(word, x, y).mark = true;
            y--;
        }
        if (alwaysfirst) {
            word.add(from);
        } else if (word.size() > 0) {
            word.add(from);
        }

//return readLetters(word);
        return word;
    }

    public String readLetters(ArrayList<PlacedLetter> word) {
        sortLetters(word);
        String s = "";
        for (PlacedLetter word1 : word) {
            s = s + odhackujZnak(word1.getLetter().getType());
        }

        return s;

    }

    protected String odhackujZnak(String s) {
        /*if (s.substring(0,1).equalsIgnoreCase("á")) return  "A";else
         if (s.substring(0,1).equalsIgnoreCase("č")) return  "C";else
         if (s.substring(0,1).equalsIgnoreCase("ď")) return  "D";else
         if (s.substring(0,1).equalsIgnoreCase("ě")) return  "E";else
         if (s.substring(0,1).equalsIgnoreCase("é")) return  "E";else
         if (s.substring(0,1).equalsIgnoreCase("í")) return  "I";else
         if (s.substring(0,1).equalsIgnoreCase("N")) return  "N";else
         if (s.substring(0,1).equalsIgnoreCase("ó")) return  "O";else
         if (s.substring(0,1).equalsIgnoreCase("ř")) return  "R";else
         if (s.substring(0,1).equalsIgnoreCase("š")) return  "S";else
         if (s.substring(0,1).equalsIgnoreCase("ť")) return  "T";else
         if (s.substring(0,1).equalsIgnoreCase("ů")) return  "U";else
         if (s.substring(0,1).equalsIgnoreCase("ú")) return  "U";else
         if (s.substring(0,1).equalsIgnoreCase("ý")) return  "Y";else
         if (s.substring(0,1).equalsIgnoreCase("ž")) return  "Z";else
         */

        return s;
    }

    /*protected int getWordsValue(ArrayList<PlacedLetter> vector,GameDesktop turn,Desks desk){
     int suma=0;
     int add; 
     int k=1;
   
     if (vector.size()==1)   {
       
     add=getHorizontalWordValueFrom(vector,vector.get(0),turn,false,desk);
     k=desk.getWordKoeficientAt(
     vector.get(0).getX(),
     vector.get(0).getY()
     );
     suma=k*add;

     add=getVerticalWordValueFrom(vector,vector.get(0),turn,false,desk);
     k=desk.getWordKoeficientAt(
     vector.get(0).getX(),
     vector.get(0).getY()
     );
     suma=k*add;

     }else{   
   
   
     boolean xVector=isXvector(vector);

    
     if (xVector){
     add=getHorizontalWordValueFrom(vector,vector.get(0),turn,true,desk);
     k=1;
     for (int x=0;x<vector.size();x++)
     k = k * desk.getWordKoeficientAt(
     vector.get(x).getX(),
     vector.get(x).getY()
     );
     suma+=add*k;
     for (int s=0;s<vector.size();s++){
     add=getVerticalWordValueFrom(vector,vector.get(s),turn,false,desk);
    
     if (add>0){
     k =  desk.getWordKoeficientAt(
     vector.get(s).getX(),
     vector.get(s).getY()
     );
     }else k=1;
    
     suma+=add*k;
    
     }

     }else{
    
     add=getVerticalWordValueFrom(vector,vector.get(0),turn,true,desk);
     k=1;
     for (int x=0;x<vector.size();x++)
     k = k * desk.getWordKoeficientAt(
     vector.get(x).getX(),
     vector.get(x).getY()
     );
     suma+=add*k;

     for (int s=0;s<vector.size();s++){    
     add=getHorizontalWordValueFrom(vector,vector.get(s),turn,false,desk);

     if (add>0){
     k =  desk.getWordKoeficientAt(
     vector.get(s).getX(),
     vector.get(s).getY()
     );
     }else k=1;
    
     suma+=add*k;
     }        
     }
      
     }
     return suma   ; 
    
     }
     */
    protected int getHorizontalWordValueFrom(ArrayList<PlacedLetter> vector, PlacedLetter from, GameDesktop turn, boolean alwaysfirst, Desks desk) {

        int suma = 0;
        int add;
        int letters = 0;

        int x = from.getX();
        int y = from.getY();
        x = x + 1;
        while (turn.getLetterOnBoardIndependentOnBuffer(x, y) != null) {
            add = turn.getLetterOnBoardIndependentOnBuffer(x, y).getValue();
            if (turn.getPole()[x][y] == null) {
                add = add * desk.getLetterKoeficientAt(x, y);
            }
            if (turn.getPole()[x][y] == null) {
                GameDesktop.getAnyNewhereInVector(vector, x, y).mark = true;//spravne?
            }
            suma += add;
            letters++;
            x++;
        }

        x = from.getX();
        y = from.getY();
        x = x - 1;
        while (turn.getLetterOnBoardIndependentOnBuffer(x, y) != null) {
            add = turn.getLetterOnBoardIndependentOnBuffer(x, y).getValue();
            if (turn.getPole()[x][y] == null) {
                add = add * desk.getLetterKoeficientAt(x, y);
            }
            if (turn.getPole()[x][y] == null) {
                GameDesktop.getAnyNewhereInVector(vector, x, y).mark = true;//spravne?  
            }
            suma += add;
            letters++;
            x--;
        }

        if (alwaysfirst) {
            letters++;
            add = from.getLetter().getValue();
            x = from.getX();
            y = from.getY();
            add = add * desk.getLetterKoeficientAt(x, y);
            suma += add;
        } else if (letters > 0) {
            letters++;
            add = from.getLetter().getValue();
            x = from.getX();
            y = from.getY();
            add = add * desk.getLetterKoeficientAt(x, y);
            suma += add;
        }

        return suma;
    }

    protected int getVerticalWordValueFrom(ArrayList<PlacedLetter> vector, PlacedLetter from, GameDesktop turn, boolean alwaysfirst, Desks desk) {

        int suma = 0;
        int add;
        int letters = 0;

        int x = from.getX();
        int y = from.getY();
        y = y + 1;
        while (turn.getLetterOnBoardIndependentOnBuffer(x, y) != null) {
            add = turn.getLetterOnBoardIndependentOnBuffer(x, y).getValue();
            if (turn.getPole()[x][y] == null) {
                add = add * desk.getLetterKoeficientAt(x, y);
            }
            if (turn.getPole()[x][y] == null) {
                GameDesktop.getAnyNewhereInVector(vector, x, y).mark = true;//spravne?
            }
            suma += add;
            letters++;
            y++;
        }

        x = from.getX();
        y = from.getY();
        y = y - 1;
        while (turn.getLetterOnBoardIndependentOnBuffer(x, y) != null) {
            add = turn.getLetterOnBoardIndependentOnBuffer(x, y).getValue();
            if (turn.getPole()[x][y] == null) {
                add = add * desk.getLetterKoeficientAt(x, y);
            }
            if (turn.getPole()[x][y] == null) {
                GameDesktop.getAnyNewhereInVector(vector, x, y).mark = true;//spravne?
            }
            suma += add;
            letters++;
            y--;
        }

        if (alwaysfirst) {
            letters++;
            add = from.getLetter().getValue();
            x = from.getX();
            y = from.getY();
            add = add * desk.getLetterKoeficientAt(x, y);
            suma += add;
        } else if (letters > 0) {
            letters++;
            add = from.getLetter().getValue();
            x = from.getX();
            y = from.getY();
            add = add * desk.getLetterKoeficientAt(x, y);
            suma += add;
        }

        return suma;

    }

    public WordsWithScore getWordsWithScore(ArrayList<PlacedLetter> vector, GameDesktop turn, Desks desk) {

        WordsWithScore vysledek = new WordsWithScore();
        if (vector.isEmpty()) {
            return vysledek;
        }
        int add;
        int k = 1;
        String w;
        ArrayList<PlacedLetter> word;

        if (vector.size() == 1) {

            word = getHorizontalWordFrom(vector, vector.get(0), turn, false);
            w = readLetters(word);
            add = getHorizontalWordValueFrom(vector, vector.get(0), turn, false, desk);
            k = desk.getWordKoeficientAt(
                    vector.get(0).getX(),
                    vector.get(0).getY(),
                    turn
            );
            vysledek.score += k * add;
            if (!w.trim().equalsIgnoreCase("")) {
                vysledek.poorWords.add(w.trim());
                vysledek.lines.add(w + " (" + String.valueOf(add) + "*" + String.valueOf(k) + "=" + String.valueOf(add * k) + ")");
            }

            word = getVerticalWordFrom(vector, vector.get(0), turn, false);
            w = readLetters(word);
            add = getVerticalWordValueFrom(vector, vector.get(0), turn, false, desk);
            k = desk.getWordKoeficientAt(
                    vector.get(0).getX(),
                    vector.get(0).getY(),
                    turn
            );
            vysledek.score += k * add;
            if (!w.trim().equalsIgnoreCase("")) {
                vysledek.poorWords.add(w.trim());
                vysledek.lines.add(w + " (" + String.valueOf(add) + "*" + String.valueOf(k) + "=" + String.valueOf(add * k) + ")");
            }

        } else {

            for (PlacedLetter vector1 : vector) {
                vector1.mark = false;
            }
            int stmark = 0;
            while (stmark >= 0) {
                vector.get(stmark).mark = true;
                word = getHorizontalWordFrom(vector, vector.get(stmark), turn, true);
                if (word.size() > 1) {
                    w = readLetters(word).trim();
                    add = getHorizontalWordValueFrom(vector, vector.get(stmark), turn, true, desk);
                    k = 1;
                    for (PlacedLetter word1 : word) {
                        if (turn.getPole()[word1.getX()][word1.getY()] == null) {
                            k = k * desk.getWordKoeficientAt(word1.getX(), word1.getY(), turn);
                        }
                    }
                    vysledek.score += add * k;
                    if (!w.trim().equalsIgnoreCase("")) {
                        vysledek.poorWords.add(w.trim());
                        vysledek.lines.add(w + " (" + String.valueOf(add) + "*" + String.valueOf(k) + "=" + String.valueOf(add * k) + ")");
                    }
                }
                stmark = -1;
                for (int x = 0; x < vector.size(); x++) {
                    if (!vector.get(x).mark) {
                        stmark = x;
                        break;
                    }
                }

            }

            stmark = 0;
            for (PlacedLetter vector1 : vector) {
                vector1.mark = false;
            }
            while (stmark >= 0) {
                vector.get(stmark).mark = true;
                word = getVerticalWordFrom(vector, vector.get(stmark), turn, true);
                if (word.size() > 1) {
                    w = readLetters(word).trim();
                    add = getVerticalWordValueFrom(vector, vector.get(stmark), turn, true, desk);
                    k = 1;
                    for (PlacedLetter word1 : word) {
                        if (turn.getPole()[word1.getX()][word1.getY()] == null) {
                            k = k * desk.getWordKoeficientAt(word1.getX(), word1.getY(), turn);
                        }
                    }
                    vysledek.score += add * k;
                    if (!w.trim().equalsIgnoreCase("")) {
                        vysledek.poorWords.add(w.trim());
                        vysledek.lines.add(w + " (" + String.valueOf(add) + "*" + String.valueOf(k) + "=" + String.valueOf(add * k) + ")");
                    }
                }
                stmark = -1;
                for (int x = 0; x < vector.size(); x++) {
                    if (!vector.get(x).mark) {
                        stmark = x;
                        break;
                    }
                };

            }

            /*boolean xVector=isXvector(vector);

    
             if (xVector){
             word=getHorizontalWordFrom(vector,vector.get(0),turn,true);
             w=readLetters(word);
             add=getHorizontalWordValueFrom(vector,vector.get(0),turn,true,desk);
             k=1;
             for (int x=0;x<vector.size();x++)
             k = k * desk.getWordKoeficientAt(
             vector.get(x).getX(),
             vector.get(x).getY()
             );
             vysledek.score+=add*k;
             if (!w.trim().equalsIgnoreCase("")) vysledek.lines.add(w+" ("+String.valueOf(add)+"*"+String.valueOf(k)+"="+String.valueOf(add*k)+")");

             for (int s=0;s<vector.size();s++){
             add=getVerticalWordValueFrom(vector,vector.get(s),turn,false,desk);
             word=getVerticalWordFrom(vector,vector.get(s),turn,false);
             w=readLetters(word);
             if (add>0){
             k =  desk.getWordKoeficientAt(
             vector.get(s).getX(),
             vector.get(s).getY()
             );
             }else k=1;
    
             vysledek.score+=add*k;
             if (!w.trim().equalsIgnoreCase("")) vysledek.lines.add(w+" ("+String.valueOf(add)+"*"+String.valueOf(k)+"="+String.valueOf(add*k)+")");
    
             }

             }else{
    
             add=getVerticalWordValueFrom(vector,vector.get(0),turn,true,desk);
             word=getVerticalWordFrom(vector,vector.get(0),turn,true);
             w=readLetters(word);
             k=1;
             for (int x=0;x<vector.size();x++)
             k = k * desk.getWordKoeficientAt(
             vector.get(x).getX(),
             vector.get(x).getY()
             );
             vysledek.score+=add*k;
             if (!w.trim().equalsIgnoreCase("")) vysledek.lines.add(w+" ("+String.valueOf(add)+"*"+String.valueOf(k)+"="+String.valueOf(add*k)+")");

             for (int s=0;s<vector.size();s++){    
             add=getHorizontalWordValueFrom(vector,vector.get(0),turn,false,desk);
             word=getHorizontalWordFrom(vector,vector.get(0),turn,false);
             w=readLetters(word);
             if (add>0){
             k =  desk.getWordKoeficientAt(
             vector.get(s).getX(),
             vector.get(s).getY()
             );
             }else k=1;
    
             vysledek.score+=add*k;
             if (!w.trim().equalsIgnoreCase("")) vysledek.lines.add(w+" ("+String.valueOf(add)+"*"+String.valueOf(k)+"="+String.valueOf(add*k)+")");
             }        
             }
             */
        }

        return vysledek;

    }

    public int testJoker(GameDesktop gameDesktop, int xx, int yy, Settings settings, Player hraje) {
        ArrayList<PlacedLetter> jokerReplacement = new ArrayList<PlacedLetter>();

        jokerReplacement.add(new PlacedLetter(xx, yy, gameDesktop.getSelected()));

        ArrayList<String> s = getMultypleWords(jokerReplacement, gameDesktop);
        for (String item : s) {
            console.addItem(item + "[j]");
        }

        if (settings.bools[Settings.playercheck]) {
            int agreed = 0;
            int[] btns = new int[2];
            btns[0] = OGLdialog.MB_OK;
            btns[1] = OGLdialog.MB_NO;

            for (Player player : players) {
                if (player.getAI() == 0) {
                    ArrayList<String> dl = new ArrayList<String>();
                    dl.add(player.getJmeno() + ",");
                    dl.add("player " + hraje.getJmeno() + " REPLACED JOKER and have created these word(s)");
                    dl.addAll(s);
                    dl.add("do you agree with this turn?");
                    api.getOglDialog().execute(btns, dl, Main.DM.getWidth() / 2, Main.DM.getHeight() / 2);
                    if (api.globalReaction.reaction == OGLdialog.MR_OK) {
                        console.addItem(player.getJmeno() + " aggred");
                        agreed++;
                    } else if (api.globalReaction.reaction == OGLdialog.MR_NO) {
                        console.addItem(player.getJmeno() + " disaggre");
                    } else {
                        console.addItem(player.getJmeno() + " forced error");
                    }
                } //for
            }

            int a = getHumans();
            if (a == 2) {
                a = 3;
            }
            a++;

            if (agreed < a / 2) {
                return -9;
            }
        }

        if (settings.bools[Settings.inetcheck]) {
            int fuj = testWordsOnWeb(getMultypleWords(jokerReplacement, gameDesktop), settings, false);
            if (fuj > 0) {
                return -10;
            }
        }

        if (settings.bools[Settings.wordcheck]) {
            int fuj = testWordsInDictionary(getMultypleWords(jokerReplacement, gameDesktop), settings, false);
            if (fuj > 0) {
                return -10;
            }
        }
        return 0;
    }

    public MyDictionary getDyctionary() {
        return dyctionary;
    }

    public void setDyctionary(MyDictionary dyctionary) {
        this.dyctionary = dyctionary;
    }

    public void setMyControlServers(requesty.Servers mcs) {
        this.mcs = mcs;
    }

    public int testWordsInDictionary(ArrayList<String> r, Settings settings, boolean simulation) {

        int fuj = 0;
        for (String r1 : r) {
            if (!r1.contains("%") && r1.length() <= settings.ints[Settings.maxcheckedlength]) {
                DictionaryWord dw = dyctionary.getWordFromDictionary(r1);
                if (dw != null) {
                    String s = dw.getValue();
                    if (!dw.getHint().trim().equals("")) {
                        s = s + "(" + dw.getHint() + ")";
                    }
                    console.addItem(s + " - computer agree");
                } else {
                    console.addItem(r1 + " - computer DISAGREE");
                    fuj++;
                }
            } else {
                if (r1.contains("%")) {
                    console.addItem(r1 + " - computer do not check jokers");
                }
                if (r1.length() > settings.ints[Settings.maxcheckedlength]) {
                    console.addItem(r1 + " - computer do not check so long words");
                }
            }
        }

        if (settings.bools[Settings.addtodic] && !simulation && fuj > 0) {
            fuj = 0;
            console.addItem("Computer disaggre, but most of the players agree so the unknown word were added to  user.txt");

            for (String r1 : r) {
                if (!r1.contains("%")) {
                    DictionaryWord dw = dyctionary.getWordFromDictionary(r1);
                    if (dw == null) {
                        console.addItem(r1);
                        this.dyctionary.addWord(r1);
                        this.dyctionary.saveUserWord(r1);
                    }
                    console.addItem("dont forget to write their meaning:)");
                }
            }
        }

        return fuj;
    }

    public boolean AreadLine(Player hraje) {
        if (zadani != null) {
            String a = null;
            if (ukazatel < zadani.size()) {
                a = zadani.get(ukazatel);
            }

            if (a != null && a.length() == 7) {
                a = a.trim();
                for (int x = 0; x < 7; x++) {
                    if (hraje.getPismeno(x) != null) {
                        api.getLetterSet().addLetterToPocket(hraje.getPismeno(x));
                    }
                }
                for (int x = 0; x < 7; x++) {
                    if ("-".equalsIgnoreCase(a.substring(x, x + 1))) {
                        hraje.setLetterDirect(x, null);
                    } else /* for(int y=0;y<api.getLetterSet().getLettersInPacket().size();y++)*/ {
                        /*if (api.getLetterSet().getLettersInPacket().get(y).getType().equalsIgnoreCase(a.substring(x,x+1))) 
                         hraje.setLetterDirect(x,api.getLetterSet().getLettersInPacket().get(y));        */
                        hraje.setLetterDirect(x, api.getLetterSet().createLetter(a.substring(x, x + 1)));
                    }

                }
                ukazatel++;
                return true;
            } else {
                console.addItem("File with letters empty! or line is not 7chars long");
                ukazatel++;
            }

        }
        return false;
    }

    @Override
    public void finalize() throws Throwable {

    }

    public ArrayList<String> getMultypleWords(ArrayList<PlacedLetter> vector, GameDesktop turn) {

        ArrayList<String> lines = new ArrayList<String>();
        if (vector.isEmpty()) {
            return lines;
        }
        String w;
        ArrayList<PlacedLetter> word;

        if (vector.size() == 1) {
            String o;
            sortLetters(vector);
            word = getHorizontalWordFrom(vector, vector.get(0), turn, false);
            o = readLetters(word);
            if (!o.trim().equalsIgnoreCase("")) {
                lines.add(o);
            }
            word = getVerticalWordFrom(vector, vector.get(0), turn, false);
            o = readLetters(word);
            if (!o.trim().equalsIgnoreCase("")) {
                lines.add(o);
            }

        } else {

            for (PlacedLetter vector1 : vector) {
                vector1.mark = false;
            }
            int stmark = 0;
            while (stmark >= 0) {
                vector.get(stmark).mark = true;
                word = getHorizontalWordFrom(vector, vector.get(stmark), turn, true);
                w = readLetters(word).trim();
                if (w.length() > 1) {
                    lines.add(w);
                }
                stmark = -1;
                for (int x = 0; x < vector.size(); x++) {
                    if (!vector.get(x).mark) {
                        stmark = x;
                        break;
                    }
                }

            }
            stmark = 0;
            for (PlacedLetter vector1 : vector) {
                vector1.mark = false;
            }
            while (stmark >= 0) {
                vector.get(stmark).mark = true;
                word = getVerticalWordFrom(vector, vector.get(stmark), turn, true);
                w = readLetters(word).trim();
                if (w.length() > 1) {
                    lines.add(w);
                }
                stmark = -1;
                for (int x = 0; x < vector.size(); x++) {
                    if (!vector.get(x).mark) {
                        stmark = x;
                        break;
                    }
                }

            }
        }

        return lines;
    }

    /*protected String getHorizontalWordFrom2(PlacedLetter from,GameDesktop turn,boolean alwaysfirst){

     ArrayList<PlacedLetter> word= new ArrayList<PlacedLetter>();
     int x=from.getX();
     int y=from.getY();
     x=x+1;
     while (turn.getLetterOnBoardIndependentOnBuffer(x,y)!=null){
     if (turn.getPolePrvek(x,y)!=null)word.add(new PlacedLetter(x,y,turn.getLetterOnBoardIndependentOnBuffer(x,y)));
     else word.add(turn.getAnyNewhere(x,y));
     GameDesktop.getAnyNewhereInVector(word,x,y).mark=true;
     x++;
     }

     x=from.getX();
     y=from.getY();
     x=x-1;
     while (turn.getLetterOnBoardIndependentOnBuffer(x,y)!=null){
     if (turn.getPolePrvek(x,y)!=null)word.add(new PlacedLetter(x,y,turn.getLetterOnBoardIndependentOnBuffer(x,y)));
     else word.add(turn.getAnyNewhere(x,y));
     GameDesktop.getAnyNewhereInVector(word,x,y).mark=true;
     x--;
     }

     if (alwaysfirst){
     word.add(from);
     }else
     if (word.size()>0) {
     word.add(from);
     }

     sortLetters(word);


    
     return readLetters(word);
     }
    
     protected String getVerticalWordFrom2(PlacedLetter from,GameDesktop turn,boolean alwaysfirst){

     ArrayList<PlacedLetter> word= new ArrayList<PlacedLetter>();
     int x=from.getX();
     int y=from.getY();
     y=y+1;
     while (turn.getLetterOnBoardIndependentOnBuffer(x,y)!=null){
     if (turn.getPolePrvek(x,y)!=null)word.add(new PlacedLetter(x,y,turn.getLetterOnBoardIndependentOnBuffer(x,y)));
     else word.add(turn.getAnyNewhere(x,y));
     GameDesktop.getAnyNewhereInVector(word,x,y).mark=true;
     y++;
     }

     x=from.getX();
     y=from.getY();
     y=y-1;
     while (turn.getLetterOnBoardIndependentOnBuffer(x,y)!=null){
     if (turn.getPolePrvek(x,y)!=null)word.add(new PlacedLetter(x,y,turn.getLetterOnBoardIndependentOnBuffer(x,y)));
     else word.add(turn.getAnyNewhere(x,y));
     GameDesktop.getAnyNewhereInVector(word,x,y).mark=true;
     y--;
     }
     if (alwaysfirst) {
     word.add(from);
     }else{
     if (word.size()>0) word.add(from);
     }

     sortLetters(word);




     return readLetters(word);
     }*/
    protected boolean testWordOnNet(String w) {
        w = w.toLowerCase();
        ArrayList<wordTestResult> r = mcs.testWord(w);
        for (wordTestResult r1 : r) {
            console.addItem(r1.toString());
        }
        boolean vysledek = false;
        for (wordTestResult r1 : r) {
            if (r1.getStav() == VocabularyServer.VS_FOUND) {
                vysledek = true;
            }
        }
        if (vysledek == false) {
            vysledek = true;
            for (wordTestResult r1 : r) {
                if (r1.getStav() != VocabularyServer.VS_TOLONG) {
                    vysledek = false;
                }
            }
        }
        return vysledek;
    }

    public int testWordsOnWeb(ArrayList<String> words, Settings settings, boolean simulation) {
        int vysledek = 0;
        for (String word : words) {
            if (!word.contains("%")) {
                boolean a = testWordOnNet(word);
                if (!a) {
                    vysledek++;
                }
                if (a && !simulation && settings.bools[Settings.addServerAgreedWordToUsers]) {
                    if (!dyctionary.isWordInDictionary(word)) {
                        console.addItem(word + "have been added to user.txt");
                        this.dyctionary.addWord(word);
                        this.dyctionary.saveUserWord(word);
                    }
                }
            } else {
                if (word.contains("%")) {
                    console.addItem(word + " - computer do not check jokers");
                }
            }
        }
        return vysledek;
    }

    public int testJokersDirect(GameDesktop gameDesktop, ArrayList<PlacedLetter> jokerReplacement, Settings settings) {

        ArrayList<String> s = getMultypleWords(jokerReplacement, gameDesktop);
        int score = testWordsInDictionarySilent(s, settings, false);
        return score;
    }

    public int testWordsInDictionarySilent(ArrayList<String> r, Settings settings, boolean simulation) {

        int score = 0;
        for (String r1 : r) {
            if (!r1.contains("%") && r1.length() <= settings.ints[Settings.maxcheckedlength]) {
                DictionaryWord dw = dyctionary.getWordFromDictionary(r1);
                if (dw != null) {
                    score += dw.getCost();
                } else {
                    return -1;
                }
            } else {
                if (r1.contains("%")) {
                    System.out.println(r1 + " - computer do not check jokers!! (SHOULD BE ERROR)");
                }
                //  if (r.get(x).length()>settings.ints[settings.maxcheckedlength]) System.out.println(r.get(x)+" - computer do not check so long words");
                return -1;
            }
        }

        return score;
    }

    public boolean isFinished() {
        if (api.getSettings().bools[Settings.onlyOnePasedddEnough]) {
            return atleastPassed(api.getSettings().ints[Settings.passestowin]);
        } else {
            return allPassed(api.getSettings().ints[Settings.passestowin]);
        }

    }

    protected boolean allPassed(int n) {
        for (Player elem : players) {
            if (elem.getPasses() < n) {
                return false;
            }
        }
        return true;
    }

    protected boolean atleastPassed(int n) {
        for (Player elem : players) {
            if (elem.getPasses() >= n) {
                return true;
            }
        }
        return false;
    }

    public ArrayList<Integer> getScores() {
        ArrayList<Integer> vysledek = new ArrayList();
        for (Player elem : players) {
            vysledek.add(elem.getScore());
        }
        return vysledek;
    }

    public ArrayList<String> getNames() {
        ArrayList<String> vysledek = new ArrayList();
        for (Player elem : players) {
            vysledek.add(elem.getJmeno());
        }
        return vysledek;
    }

    public ArrayList<String> getResults() {
        ArrayList<String> vysledek = new ArrayList();
        for (Player elem : players) {
            vysledek.add(Stringuj(4, elem.getScore()) + "  " + elem.getJmeno() + "(" + elem.getIdentity() + ")");
        }
        return vysledek;
    }

    protected String Stringuj(int mist, int number) {
        String vysledek = String.valueOf(number);
        while (vysledek.length() < mist) {
            vysledek = "0" + vysledek;
        }
        return vysledek;
    }

    public void savePlayers(File f, Player hraje) {
        BufferedWriter write = null;
        try {

            write = new BufferedWriter(new FileWriter(f));
            write.write("<players total='" + players.size() + "' current='" + this.thisPlayerIndex(hraje) + "' pointer='" + this.ukazatel + "'>\r\n");
            for (Player elem : players) {
                elem.writePlayer(write, thisPlayerIndex(elem));
            }
            write.write("</players>\r\n");

        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {

                write.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

    }

    public Player UpdatePlayers(File f, LetterSet l) {
        XmlPlayerUpdater handler = null;
        XMLReader xr;
        try {

            xr = XMLReaderFactory.createXMLReader();
            handler = new XmlPlayerUpdater(l);
            xr.setContentHandler(handler);
            xr.setErrorHandler(handler);

            FileReader r = new FileReader(f);
            System.out.println(f);
            handler.setLoadedRD(players);
            xr.parse(new InputSource(r));

            while (!handler.finished) {

            }
            xr = null;
            r = null;

        } catch (SAXException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        ukazatel = handler.getPoiner();
        return players.get(handler.getCurrent());
    }

    public Player LoadPlayers(File f, LetterSet l) {
        XmlPlayerUpdater handler = null;
        XMLReader xr;
        try {

            xr = XMLReaderFactory.createXMLReader();
            handler = new XmlPlayerUpdater(l);
            xr.setContentHandler(handler);
            xr.setErrorHandler(handler);

            FileReader r = new FileReader(f);
            System.out.println(f);
            handler.setLoadedRD(null);
            xr.parse(new InputSource(r));

            while (!handler.finished) {

            }
            xr = null;
            r = null;

        } catch (SAXException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        players = handler.getLoadedData();

        for (Iterator it = players.iterator(); it.hasNext();) {
            Player elem = (Player) it.next();
            elem.setAiEngine(api.getAIengine(elem.getAI()));
            elem.setLetterSet(api.getLetterSet());
            elem.setSettings(api.getSettings());
        }

        ukazatel = handler.getPoiner();
        return players.get(handler.getCurrent());
    }

    //return handler.getLoadedData();
    public Player previousPlayer(Player hraje) {
        return players.get(previousPlayerIndex(hraje));

    }

    public int testJoker(GameDesktop gameDesktop, int x, int y, int z, Settings settings, Player hraje) {
        return testJoker(gameDesktop, x, y, settings, hraje);
    }

    public static final boolean isDesk3D(File df) {
        BufferedReader read = null;
        try {
            read = new BufferedReader(new FileReader(df));
            String a;

            a = read.readLine().trim();
            String[] c = a.split("x");
            return c.length == 3;

        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                read.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return false;
    }

    public void nulujVote() {
        console.addItem("voting! please wait to its end!");
        votecancel = 0;
        voteno = 0;
        voteok = 0;
    }

    public int voteSum() {
        return votecancel + voteno + voteok;
    }

    public boolean voteResult() {
        return voteResult(0);
    }

    public boolean voteResult(int tolerance) {
        return (voteok - voteno) > tolerance;
    }

    public boolean waitForVotes(int tolerance, int naKolikSeceka, boolean render) {
        try {
            while (voteSum() < naKolikSeceka) {

                /* if (render )   api.redirectedLoopKore();
                 else     api.redirectedLoopNoRenderKore();
                 */
                if (Thread.currentThread() == api.getThread()) {
                    api.redirectedLoopKore();
                } else {
                    Thread.sleep(200);
                }

            }
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        console.addItem("voting finished!");
        if (api.getSettings().bools[Settings.sounds]) {
            api.vf.play();
        }
        return (voteResult(tolerance));
    }

    public void voteOK() {
        voteok++;
    }

    public void voteNo() {
        voteno++;
    }

    public void voteCancel() {
        votecancel++;
    }

    public int getLocalHumansCount() {
        int vysledek = 0;
        for (Player elem : players) {
            if (elem.isLocal() && elem.getAI() == 0) {
                vysledek++;
            }
        }
        return vysledek;
    }

    public int getLocalPlayersCount() {
        int vysledek = 0;
        for (Player elem : players) {
            if (elem.isLocal()) {
                vysledek++;
            }
        }
        return vysledek;
    }

    public int getAiCount() {
        int vysledek = 0;
        for (Player elem : players) {
            if (elem.getAI() == 0) {
                vysledek++;
            }
        }
        return vysledek;
    }

    public int getForeignHumans() {
        int vysledek = 0;
        for (Player elem : players) {
            if (elem.getAI() == 0 && !elem.isLocal()) {
                vysledek++;
            }
        }
        return vysledek;
    }

    private String dialogToStr(ArrayList dl) {
        String vysledek = "";
        for (Iterator it = dl.iterator(); it.hasNext();) {
            String elem = (String) it.next();
            vysledek = vysledek + elem + "-";

        }
        return vysledek;
    }

}//class

//predelat vsecky co operuji s placed turn.placedleters tak aby pracovaly s aaraylistem..hotovo..no potez
//predelani bylo uplne na hovno, protoze procedury typu getletter independentonbuffer...


///2658
