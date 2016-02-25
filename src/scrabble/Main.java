/*
 * Main.java
 *
 * Created on 1. srpen 2006, 13:14
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package scrabble;

import Cammons.Cammons;
import Cammons.MP3;
import InetGameControler.IC;
import InetGameControler.JoinedPlayer;
import InetGameControler.PlayerThread;
import InetGameControler.ServerSinglePlayerThread;
import LetterPackage.LetterSet;
import LetterPackage.PlacedLetter;
import LetterPackage.SimpleLetter;
import MyInpuyConsole.SimpleInputConsole;
import OGLaddons.DialogReactioner;
import OGLaddons.OGLdialog;
import OGLaddons.ScrabbleCursor;
import OGLbasic.GLApp;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import scrabble.DesksPackage.Desks3D;
import scrabble.GameDesktopPackage.GameDesktop3D;
import scrabble.GameLogicPackage.GameLogic3D;
import scrabble.Playerspackage.Player;
import scrabble.ScrabbleAI.*;
import scrabble.Settings.Settings;
import dictionaries.MyDictionary;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.Random;
import org.lwjgl.*;
import org.lwjgl.opengl.*;
import org.lwjgl.input.*;
import org.lwjgl.opengl.glu.*;
import MyOutputConsole.Console;
import scrabble.DesksPackage.Desks;
import scrabble.GameDesktopPackage.GameDesktop;
import scrabble.GameLogicPackage.GameLogic;

//import org.lwjgl.util.Timer;
/**
 *
 * @author Jirka
 */
public class Main extends GLApp {

    /**
     * Creates a new instance of Main
     */
    public Main() {

    }
    //private static final int[] alphy={GL11.GL_ZERO,GL11.GL_ONE,GL11.GL_DST_COLOR,GL11.GL_SRC_COLOR,GL11.GL_ONE_MINUS_DST_COLOR,GL11.GL_ONE_MINUS_SRC_COLOR,GL11.GL_SRC_ALPHA,GL11.GL_ONE_MINUS_SRC_ALPHA,GL11.GL_DST_ALPHA,GL11.GL_ONE_MINUS_DST_ALPHA};
    //private static final String[] alphys={"GL11.GL_ZERO","GL11.GL_ONE","GL11.GL_DST_COLOR","GL11.GL_SRC_COLOR","GL11.GL_ONE_MINUS_DST_COLOR","GL11.GL_ONE_MINUS_SRC_COLOR","GL11.GL_SRC_ALPHA","GL11.GL_ONE_MINUS_SRC_ALPHA","GL11.GL_DST_ALPHA","GL11.GL_ONE_MINUS_DST_ALPHA"};
    //private int alphyukazatel1=0,alphyukazatel2=0;
    public int LSIZE = 100;
    public static final int MTC_NOTHING = 0;
    public static final int MTC_RUNAI = 1;

    private Thread ui;
    volatile public AsynchronousDialogExecuter ade = null;
    public MP3 pv, vf, yt;
    //public org.lwjgl.util.Timer odpocet=new org.lwjgl.util.Timer();
    public javax.swing.Timer odpocet2;
    public int ss, mm;
    boolean aifinished = true;
    private int threadcounter = 0;
    // Lighting colors
    float faWhite[] = {1.0f, 1.0f, 1.0f, 1.0f};
    float faLightBlue[] = {0.8f, 0.8f, .9f, 1f};
    // Light direction: if last value is 0, then this describes light direction.  If 1, then light position.
    float lightDirection[] = {-2f, 2f, 2f, 0f};
    // Camera position
    static float[] cameraPos = {0f, 0f, 35f};
    float cameraRotation = 0f;
    // A constant used in navigation
    final float piover180 = 0.0174532925f;

    //***non ogl
    private OGLdialog oglDialog;

    public DialogReactioner globalReaction = new DialogReactioner();

    private boolean alt;

    private Desks desk;

    private LetterSet letterSet;

    private boolean todolshift = false, todorshift = false;
    float savexuhel, xuhel = 0;
    float saveyuhel, yuhel = 0;
    float savezuhel, zuhel = 0;
    private int savemx, savemy;
    private boolean mousedown;
    private Player hraje;//vidi se v netoe hre != hraje??

    private ArrayList<ServerSinglePlayerThread> serverThreads;
    private PlayerThread playerThread;
    private PlayerThread consoleSimulator;

    private scrabble.Playerspackage.Player vidim;

    private ScrabbleCursor cursor;
    private GameDesktop gameDesktop;
    private GameLogic gameLogic;
    private SimpleInputConsole inputConsole;
    private requesty.Servers servers;

    private boolean tododeskalpha;
    private float deskalpha = 1f;

// private BasicAI  ai;
    /*private AIinterface ai1;
     private AIinterface ai2;
     private AIinterface ai3;
     private AIinterface ai4;*/
    private AIinterface[] ai = new AIinterface[6];

    private Thread thread;

    public static int style = 1;

    private int md;

    private boolean muze;

    private boolean redirectedFinished = true;

    private MyOutputConsole.Console console;
    private Settings settings;

    private boolean todoalpha = false;
    private float alpha = 1f;

    public Thread getThread() {
        return thread;
    }

    public ArrayList<ServerSinglePlayerThread> getServerThreads() {
        return serverThreads;
    }

    public AIinterface getAIengine(int ai) {
        switch (ai) {
            case 1:
                return this.ai[0];
            case 2:
                return this.ai[1];
            case 3:
                return this.ai[2];
            case 4:
                return this.ai[3];
            case 5:
                return this.ai[4];
            case 6:
                return this.ai[(new Random().nextInt(this.ai.length))];
            default:
                return null;
        }

    }

    public Player getVidim() {
        return vidim;
    }

    public Player getHraje() {
        return hraje;
    }

    public OGLdialog getOglDialog() {
        return oglDialog;
    }

    public LetterSet getLetterSet() {
        return letterSet;
    }

    public Settings getSettings() {
        return settings;
    }

    public void setSettings(Settings settings) {
        this.settings = settings;
    }

    public Console getConsole() {
        return console;
    }

    /**
     * Initialize the scene. Called by GLApp.run()
     */
    @Override
    public void init() {
        Main.inititalDisplayMode = settings.strings[Settings.dysplaymode];

        int k = 100 / 15;
        if (settings.ints[Settings.turntime] > 0) {
            ss = 59;
            mm = settings.ints[Settings.turntime] - 1;
            odpocet2 = new javax.swing.Timer(1000, new ActionListener() {

                public void actionPerformed(ActionEvent evt) {

                    ss--;
                    if (ss == -1) {
                        mm -= 1;
                        ss = 59;
                    }
                    if (mm == -1 && hraje == vidim) {
                        if (!settings.isInet() || settings.IsServer()) {
                            gameLogic.setKillui(true);
                            finished = true;
                            hraje.allLettersBack(gameDesktop);
                            Player hral = hraje;
                            Player budehrat = gameLogic.nextPlayer(hraje);
                            console.addItem(hral.getJmeno() + ", time is up, NOW is " + budehrat.getJmeno() + " turn");
                            DALSIHRAC();
                        } else {
                            playerThread.messageToServer(IC.commands[12]);
                        }
                    }

                }
            });
            odpocet2.start();
        }

        StartUpScrabble.jScrabelFrame.jProgressBar1.setValue(k * 1);
        StartUpScrabble.jScrabelFrame.repaint();
        // initialize Window, Keyboard, Mouse, OpenGL environment
        initDisplay();
        initInput();
        initOpenGL();

        // setup and enable perspective
        setPerspective();
        LSIZE = compLsize(DM);
        // Create a light (diffuse light, ambient light, position)
        setLight(GL11.GL_LIGHT1, faWhite, faLightBlue, lightDirection);

        // enable lighting and texture rendering
        //GL11.glEnable(GL11.GL_LIGHTING);//??nutne
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glPolygonMode(GL11.GL_BACK, GL11.GL_FILL);
        GL11.glDisable(GL11.GL_CULL_FACE);
        //GL11.glEnable(GL11.GL_POINT_SMOOTH);
        //GL11.glHint(GL11.GL_POINT_SMOOTH_HINT,GL11.GL_NICEST);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_ALPHA_TEST);//??
        GL11.glEnable(GL11.GL_DEPTH_TEST);//??
        // GL11.glAlphaFunc ( GL11.GL_GREATER, 0.21f ) ;//??
        GL11.glEnable(GL11.GL_ALPHA_TEST);//??
//create game primitives
        StartUpScrabble.jScrabelFrame.jProgressBar1.setValue(k * 2);
        StartUpScrabble.jScrabelFrame.repaint();

        buildFont("Data" + File.separator + "font.png", 16);

        StartUpScrabble.jScrabelFrame.jProgressBar1.setValue(k * 3);
        StartUpScrabble.jScrabelFrame.repaint();
        String dp = "Data" + File.separator + "textures" + File.separator + "basic_dialog" + File.separator;
        oglDialog = new OGLdialog(dp + "button.png", dp + "bg.png", this, "Data" + File.separator + "dialogFont.png", dp + "sb.png", dp + "border.png");

        StartUpScrabble.jScrabelFrame.jProgressBar1.setValue(k * 4);
        StartUpScrabble.jScrabelFrame.repaint();

        File df = new File("Data" + File.separator + "desks" + File.separator + settings.strings[Settings.desk]);
        settings.bools[Settings.desk3D] = GameLogic.isDesk3D(df);
        if (settings.bools[Settings.desk3D]) {
            deskalpha = 0f;
        }
        if (settings.bools[Settings.desk3D]) {
            desk = new Desks3D(df, 1);
            yuhel = +60;

        } else {
            desk = new Desks(new File("Data" + File.separator + "desks" + File.separator + settings.strings[Settings.desk]), 1);
        }

        StartUpScrabble.jScrabelFrame.jProgressBar1.setValue(k * 5);
        StartUpScrabble.jScrabelFrame.repaint();

        letterSet = new LetterSet(new File("Data" + File.separator + "alfabets" + File.separator + settings.strings[Settings.letterset]));

        StartUpScrabble.jScrabelFrame.jProgressBar1.setValue(k * 6);
        StartUpScrabble.jScrabelFrame.repaint();

        cursor = new ScrabbleCursor(this, "Data" + File.separator + "cursors" + File.separator + settings.strings[Settings.cursor]);
        StartUpScrabble.jScrabelFrame.jProgressBar1.setValue(k * 7);
        StartUpScrabble.jScrabelFrame.repaint();
        if (settings.bools[Settings.desk3D]) {
            gameDesktop = new GameDesktop3D(desk.getWidth(), desk.getHeight(), ((Desks3D) desk).getLevels());
        } else {
            gameDesktop = new GameDesktop(desk.getWidth(), desk.getHeight());
        }

        StartUpScrabble.jScrabelFrame.jProgressBar1.setValue(k * 8);
        StartUpScrabble.jScrabelFrame.repaint();

        gameDesktop.setRedbox(letterSet.getRedboxletter());
        gameDesktop.setBluebox(letterSet.getBlueboxletter());
        gameDesktop.setWhiteebox(letterSet.getWhiteboxletter());
        gameDesktop.setCx(desk.getStartx());
        gameDesktop.setCy(desk.getStarty());
        if (gameDesktop instanceof GameDesktop3D) {
            ((GameDesktop3D) gameDesktop).setCz((desk).getStartz());
        }

        console = new Console(settings.ints[Settings.minconosletime], settings.ints[settings.maxcconsoletime]);
        if (settings.bools[Settings.desk3D]) {
            gameLogic = new GameLogic3D(this);
        } else {
            gameLogic = new GameLogic(this);
        }

        //for(int x=0;x<=20;x++) gameDesktop.setLetter(letterSet.getRandomLetterFromPocket(),new Random().nextInt(15),new Random().nextInt(15));
        StartUpScrabble.jScrabelFrame.jProgressBar1.setValue(k * 9);
        StartUpScrabble.jScrabelFrame.repaint();

        inputConsole = new SimpleInputConsole(this, "Data" + File.separator + "dialogFont.png", dp + "bg.png") {

            public void finish() {
                String s = inputConsole.getZadano();
                inputConsole.setZadano("");
                console.addItem(hraje.getJmeno() + ": " + s);
                String c[] = s.split(" ");
                if (c == null) {
                    return;
                }
                if (c.length == 0) {
                    return;
                }

                if (c[0].trim().equalsIgnoreCase("TABhelp")) {
                    if (c.length == 3 && c[2].length() == 1) {

                        console.addItem(hraje.getJmeno() + " needs help with word " + c[1] + " please wait:)");

                        int i = 0;
                        if (c[2].toUpperCase().equals("B")) {
                            i = MyDictionary.MODEL_BEGIN;
                        }
                        if (c[2].toUpperCase().equals("E")) {
                            i = MyDictionary.MODEL_END;
                        }
                        if (c[2].toUpperCase().equals("W")) {
                            i = MyDictionary.MODEL_EVERYWHERE;
                        }
                        if (c[2].toUpperCase().equals("R")) {
                            i = MyDictionary.MODEL_REGEX;
                        }

                        ArrayList<String> dl = gameLogic.getDyctionary().getAll(c[1], i);

                        Collections.sort(dl);
                        dl.add(0, "total: " + dl.size());
                        int btns[] = new int[1];
                        btns[0] = OGLdialog.MR_CANCEL;
                        oglDialog.execute(btns, dl, 250, DM.getHeight() * 2 / 3);

                    } else {
                        console.addItem("comand help have two arguments, one is part of the word and the second is a letter (b,e,w,r)=begin,end,ewerywhere,regex");
                    }
                }

                if (c[0].trim().equalsIgnoreCase("TABinetcheck")) {
                    console.addItem(hraje.getJmeno() + "wants to werify some words on web:");
                    ArrayList<String> a = new ArrayList<String>();
                    for (int i = 1; i < c.length; i++) {
                        a.add(c[i]);
                    }
                    int b = gameLogic.testWordsOnWeb(a, settings, true);
                    if (b == 0) {
                        console.addItem("all ok");
                    } else {
                        console.addItem("not ok");
                    }

                }

                if (c[0].trim().equalsIgnoreCase("TABdictcheck")) {
                    console.addItem(hraje.getJmeno() + "wants to werify some words in dictionary:");
                    ArrayList<String> a = new ArrayList<String>();
                    for (int i = 1; i < c.length; i++) {
                        a.add(c[i]);
                    }
                    int b = gameLogic.testWordsInDictionary(a, settings, true);
                    if (b == 0) {
                        console.addItem("all ok");
                    } else {
                        console.addItem("not ok");
                    }

                }

                if (c[0].trim().equalsIgnoreCase("TABadd")) {
                    if (c.length >= 2) {
                        String cx = "";
                        for (int i = 2; i < c.length; i++) {
                            cx = cx + " " + c[i];
                        }
                        gameLogic.getDyctionary().addWordWithHint(c[1], cx.trim());
                        console.addItem(hraje.getJmeno() + " adds word to dictionary(" + c[1] + ")");
                    } else {
                        console.addItem("TABadd adds an word to dictionary (first argument is an word, the others arguments 'is' its hint");
                    }
                }

                if (c[0].trim().equalsIgnoreCase("TABsetall")) {
                    if (c.length == 2 && c[1].length() == 7) {

                        hraje.allLettersBack(gameDesktop);

                        for (int x = 0; x < 7; x++) {
                            if (hraje.getPismeno(x) != null) {
                                letterSet.addLetterToPocket(hraje.getPismeno(x));
                            }
                        }

                        for (int x = 0; x < 7; x++) {
                            if ("-".equalsIgnoreCase(c[1].substring(x, x + 1))) {
                                hraje.setLetterDirect(x, null);
                            } else /*for(int y=0;y<letterSet.getLettersInPacket().size();y++)*/ {
                                /*if (letterSet.getLettersInPacket().get(y).getType().equalsIgnoreCase(c[1].substring(x,x+1))) 
                                 hraje.setLetterDirect(x,letterSet.getLettersInPacket().get(y));        */
                                hraje.setLetterDirect(x, letterSet.createLetter(c[1].substring(x, x + 1)));
                            }

                        }
                        console.addItem(hraje.getJmeno() + " cloned these letters into his packet: " + c[1]);
                    } else {
                        console.addItem("comand setall have one string parameter which is seven letters long row, with - as noletter");
                    }
                }

                if (c[0].trim().equalsIgnoreCase("TABjoker")) {
                    if (c.length == 4 && (gameDesktop instanceof GameDesktop)) {
                        int x = Integer.parseInt(c[1]);
                        int y = Integer.parseInt(c[2]);
                        int f = 7 - Integer.parseInt(c[3]);
                        if (gameDesktop.getPole()[x][y] != null) {
                            if (hraje.getPismeno(f) != null && gameDesktop.getPole()[x][y].getType().equals("%")) {
                                SimpleLetter swap = gameDesktop.getPole()[x][y];
                                gameDesktop.setLetterDirect(hraje.getPismeno(f), x, y);
                                hraje.getletters().set(f, swap);
                                console.addItem("joker was changed");
                                hraje.jokers++;

                            }
                        }
                    } else {
                        console.addItem("Comand 'joeker' have three integer arguments (x coordinate,y coorrdinate and letter you want to place to)");
                    }

                    if (c.length == 5 && (gameDesktop instanceof GameDesktop3D)) {
                        GameDesktop3D g3d = (GameDesktop3D) gameDesktop;
                        int x = Integer.parseInt(c[1]);
                        int y = Integer.parseInt(c[2]);
                        int z = Integer.parseInt(c[3]);
                        int f = 7 - Integer.parseInt(c[4]);
                        if (g3d.getPole3D().get(z)[x][y] != null) {
                            if (hraje.getPismeno(f) != null && g3d.getPole3D().get(z)[x][y].getType().equals("%")) {
                                SimpleLetter swap = g3d.getPole3D().get(z)[x][y];
                                g3d.setLetterDirect(hraje.getPismeno(f), x, y, z);
                                hraje.getletters().set(f, swap);
                                console.addItem("joker was changed");
                                hraje.jokers++;

                            }
                        }
                    } else {
                        console.addItem("Comand 'joeker' have four integer arguments (x coordinate,y coorrdinate, z coordinateand letter you want to place to)");
                    }

                }
                if (c[0].trim().equalsIgnoreCase("TABletterpack")) {
                    ArrayList<String> dl = new ArrayList<String>();
                    for (int x = 0; x < letterSet.getLettersInPacketSize(); x++) {
                        dl.add(letterSet.getLettersInPacket().get(x).getType() + "(" + letterSet.getLettersInPacket().get(x).getValue() + ")");
                    }
                    Collections.sort(dl);
                    int btns[] = new int[1];
                    btns[0] = OGLdialog.MR_CANCEL;
                    oglDialog.execute(btns, dl, 200, DM.getHeight() * 2 / 3);

                }

                if (c[0].trim().equalsIgnoreCase("TABDELoncursor")) {
                    if (gameDesktop instanceof GameDesktop3D) {
                        GameDesktop3D g3d = (GameDesktop3D) gameDesktop;
                        g3d.setLetterSuperDirect(null, g3d.getCx(), g3d.getCy(), g3d.getCz());
                        console.addItem(hraje.getJmeno() + " deleted letter on " + g3d.getCx() + "," + g3d.getCy() + "," + g3d.getCz());
                    } else {
                        gameDesktop.setLetterSuperDirect(null, gameDesktop.getCx(), gameDesktop.getCy());
                        console.addItem(hraje.getJmeno() + " deleted letter on " + gameDesktop.getCx() + "," + gameDesktop.getCy());
                    }
                }
                if (c[0].trim().equalsIgnoreCase("TAB")) {
                    ArrayList<String> dl = new ArrayList<String>();
                    dl.add("TABHELP");
                    dl.add("TABLETTERPACK");
                    dl.add("TABJOKER");
                    dl.add("TABSETALL");
                    dl.add("TABADD");
                    dl.add("TABDICTCHECK");
                    dl.add("TABINETCHECK");
                    dl.add("TABDELoncursor");
                    dl.add("TABFINISH");
                    Collections.sort(dl);
                    int btns[] = new int[1];
                    btns[0] = OGLdialog.MR_CANCEL;
                    oglDialog.execute(btns, dl, 400, DM.getHeight() * 2 / 3);
                }

                if (c[0].trim().equalsIgnoreCase("TABFINISH")) {
                    if (c.length != 2) {
                        console.addItem("tab finish needs parameter wits ai (1,2,3,4,5,6");
                    } else {
                        hraje.setAiEngine(null);
                        hraje.setAiEngine(getAIengine(Integer.parseInt(c[1].trim())));

                        if (hraje.getAiEngine() == null) {
                            console.addItem("unknown ai engine");
                        } else {
                            aiturn();
                            hraje.setAiEngine(null);
                            DALSIHRAC();
                        }
                    }
                }

            }

        };

        StartUpScrabble.jScrabelFrame.jProgressBar1.setValue(k * 10);
        StartUpScrabble.jScrabelFrame.repaint();

        MyDictionary d = new MyDictionary(settings.dictionaries, this.letterSet);
        gameLogic.setDyctionary(d);

        StartUpScrabble.jScrabelFrame.jProgressBar1.setValue(k * 11);
        StartUpScrabble.jScrabelFrame.repaint();

        servers = new requesty.Servers(settings.servers);
        gameLogic.setMyControlServers(servers);

        StartUpScrabble.jScrabelFrame.jProgressBar1.setValue(k * 12);
        StartUpScrabble.jScrabelFrame.repaint();
//if (settings.bools[Settings.sounds]) {
        vf = new MP3("Data" + File.separator + "sounds" + File.separator + "vf.mp3");
        pv = new MP3("Data" + File.separator + "sounds" + File.separator + "pv.mp3");
        yt = new MP3("Data" + File.separator + "sounds" + File.separator + "yt.mp3");
//}
        if (settings.bools[Settings.desk3D]) {
            ai[0] = new D3AI1(letterSet, gameDesktop, desk, gameLogic, settings, console);
            ai[1] = new D3AI2(letterSet, gameDesktop, desk, gameLogic, settings, console);
            ai[2] = new D3AI3(letterSet, gameDesktop, desk, gameLogic, settings, console);
            ai[3] = new D3AI4(letterSet, gameDesktop, desk, gameLogic, settings, console);
            ai[4] = new D3AI5(letterSet, gameDesktop, desk, gameLogic, settings, console);
            ai[ai.length - 1] = new BasicAI3D(letterSet, gameDesktop, desk, gameLogic, settings, console);
        } else {
            ai[0] = new AI1(letterSet, gameDesktop, desk, gameLogic, settings, console);
            ai[1] = new AI2(letterSet, gameDesktop, desk, gameLogic, settings, console);
            ai[2] = new AI3(letterSet, gameDesktop, desk, gameLogic, settings, console);
            ai[3] = new AI4(letterSet, gameDesktop, desk, gameLogic, settings, console);
            ai[4] = new AI5(letterSet, gameDesktop, desk, gameLogic, settings, console);
            ai[ai.length - 1] = new BasicAI(letterSet, gameDesktop, desk, gameLogic, settings, console);
        }

        //playerThread=null;
        // serverThreads=null;
        if (settings.loadGame == null) {
            ArrayList<Player> hraci = new ArrayList<Player>();
            for (int x = 0; x < settings.playersNames.size(); x++) {
                Player p = new Player(settings.playersNames.get(x), letterSet, settings.playersValues.get(x), settings);
                p.setLocal(true);
                if (p.getAI() == 1) {
                    p.setAiEngine(ai[0]);
                }
                if (p.getAI() == 2) {
                    p.setAiEngine(ai[1]);
                }
                if (p.getAI() == 3) {
                    p.setAiEngine(ai[2]);
                }
                if (p.getAI() == 4) {
                    p.setAiEngine(ai[3]);
                }
                if (p.getAI() == 5) {
                    p.setAiEngine(ai[4]);
                }
                hraci.add(p);
            }

            if (settings.IsServer() && settings.isInet()) {
                serverThreads = new ArrayList();
                for (JoinedPlayer elem : settings.joinedPalyers) {
                    Player p = new Player(elem.getName(), letterSet, 0, settings);
                    p.setNetwork(elem);
                    p.setLocal(false);
                    hraci.add(p);
                    serverThreads.add(new ServerSinglePlayerThread(p, this) {

                        public void proceedMessage(String[] c) {
                            proceedServerMessage(c, this);

                        }
                    });
                }
                console.setMultyple(serverThreads);
            } else if (settings.isInet()) { /*=is not server*/

                JoinedPlayer elem = (JoinedPlayer) settings.joinedPalyers.get(0);
                Player p = new Player(elem.getName(), letterSet, 0, settings);
                p.setNetwork(elem);
                p.setLocal(false);
                playerThread = new PlayerThread(p, this) {

                    public void proceedMessage(String[] c) {
                        proceedClientMessage(c, this);
                    }
                };
                console.setOut(playerThread);

            }
            gameLogic.setPlayers(hraci);

            //hraje=gameLogic.nextPlayer(null);
            if (settings.bools[Settings.randomizeplayers] && hraci.size() > 0) {
                gameLogic.randomizePlayers(100);
            }
        } else {
            hraje = SaveLoad.LoadGame(settings.loadGame, console, gameDesktop, gameLogic, letterSet);
            if (hraje == null) {
                hraje = gameLogic.nextPlayer(null);
            }
            vidim = gameLogic.vidimForPlayer(hraje);
            if (vidim == hraje && hraje.getAI() == 0 && settings.bools[Settings.sounds]) {
                yt.play();
            }
            if (hraje.getAI() > 0) {
                hraje = gameLogic.previousPlayer(hraje);
                DALSIHRAC();
            }
        }

        StartUpScrabble.jScrabelFrame.jProgressBar1.setValue(k * 13);
        StartUpScrabble.jScrabelFrame.repaint();

        if (playerThread != null) {
            playerThread.start();
            playerThread.messageToServer(IC.commands[0]);

            while (playerThread.apiCantContinue) {
                try {
                    //waiting for server said everybody loaded  
                    Thread.sleep(200);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
            gameLogic.setPlayers(playerThread.getPlayers());

        }
        if (serverThreads != null) {
            for (ServerSinglePlayerThread elem : serverThreads) {
                elem.start();
            }
            boolean priznak = true;
            while (priznak) {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                priznak = false;
                for (Iterator it = serverThreads.iterator(); it.hasNext();) {
                    ServerSinglePlayerThread elem = (ServerSinglePlayerThread) it.next();
                    if (elem.myPlyerIn == false) {
                        priznak = true;
                    }
                }
            }
            nahlasHrace();
            nahlasVsemPismenka();
            zpravaVsem(IC.commands[1]);

        }

        StartUpScrabble.jScrabelFrame.jProgressBar1.setValue(k * 14);
        StartUpScrabble.jScrabelFrame.repaint();

        StartUpScrabble.jScrabelFrame.setVisible(false);

        if ((settings.loadGame == null) && (!settings.isInet() || settings.IsServer())) {
            DALSIHRAC();
        }
        //   if (settings.IsServer()) nahlaskdoHraje();

    }

    /**
     * Render one frame. Called by GLApp.run().
     */
    public void render() {
        if (ade != null) {
            oglDialog.prepare(ade.getBtns(), ade.getContext(), ade.getWidth(), ade.getHeight());
            ade = null;
            oglDialog.show();
        }
        if (frameCount % 2 == 0) {
            cursor.countcursor();
        }
        // select model view and reset

        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadIdentity();

        // clear depth buffer and color
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

        // adjust camera position according to arrow key events
        setCameraPosition();

        // shift and rotate entire scene (opposite to  "camera" position)
        GL11.glRotatef((360.0f - cameraRotation), 0, 1f, 0);   // first rotate around y axis
        GL11.glTranslatef(-cameraPos[0], -cameraPos[1], -cameraPos[2]);  // then move forward on x,z axis (staying on ground, so no Y motion)

        // Don't 'loadIdentiy' to clear the model view matrix below.
        // Model translations are added onto the translate we did above that
        // shifted and rotated the entire scene.
        GL11.glPushMatrix();
        GL11.glRotatef(-yuhel, 1, 0, 0);
        GL11.glRotatef(xuhel, 0, 0, 1);
        GL11.glRotatef(zuhel, 0, 1, 0);

//Z-Buffer writing - 
        //GL11.glDepthMask(false);
        GL11.glEnable(GL11.GL_BLEND);
        if (settings.bools[Settings.desk3D]) {
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            if (alpha > 0.5) {
                GL11.glColor4f(1f, 1f, 1f, alpha); //jen s glligting; najit alternativu.
                gameDesktop.draw();
            }
            GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_SRC_ALPHA);
            if (deskalpha < 0.89f) {
                GL11.glColor4f(1f, 1f, 1f, deskalpha); //jen s glligting; najit alternativu.
                desk.draw();
            }
            GL11.glColor4f(1f, 1f, 1f, alpha); //jen s glligting; najit alternativu.
            gameDesktop.drawCursor();
        } else {
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glColor4f(1f, 1f, 1f, deskalpha); //jen s glligting; najit alternativu.
            desk.draw();
            GL11.glColor4f(1f, 1f, 1f, alpha); //jen s glligting; najit alternativu.
            gameDesktop.draw();
            GL11.glColor4f(1f, 1f, 1f, alpha); //jen s glligting; najit alternativu.
            gameDesktop.drawCursor();
        }

        //GL11.glDepthMask(true);       
        GL11.glDisable(GL11.GL_BLEND);

        GL11.glPopMatrix();

        GL11.glColor4f(1f, 1f, 1f, 1f);  //jen s gl lighting

        setOrthoOn();
        if (vidim != null) {
            for (int x = 0; x < vidim.getletters().size(); x++) {
                if (vidim.getletters().get(x) != null) {//hracovy pismena

                    GL11.glBindTexture(GL11.GL_TEXTURE_2D, vidim.getletters().get(x).getTexture());
                    GL11.glBegin(GL11.GL_QUADS);
                    GL11.glNormal3f(0.0f, 0.0f, 1.0f);
                    GL11.glTexCoord2f(0.0f, 0.0f);
                    GL11.glVertex2i(0, x * (LSIZE + 10) + 0);	// Bottom Left Of The Texture and Quad
                    GL11.glTexCoord2f(1.0f, 0.0f);
                    GL11.glVertex2i(LSIZE, x * (LSIZE + 10) + 0);	// Bottom Right Of The Texture and Quad
                    GL11.glTexCoord2f(1.0f, 1.0f);
                    GL11.glVertex2i(LSIZE, x * (LSIZE + 10) + LSIZE);	// Top Right Of The Texture and Quad
                    GL11.glTexCoord2f(0.0f, 1.0f);
                    GL11.glVertex2i(0, x * (LSIZE + 10) + LSIZE);	// Top Left Of The Texture and Quad
                    GL11.glEnd();

                }
                glPrint(0, (x + 1) * (LSIZE + 10) - 16, 0, String.valueOf(7 - x));
            }
        }
        if (vidim != null) {
            if (vidim.getSelected() > 0 && vidim.getSelected() < vidim.getletters().size()) {
                if (vidim.getletters().get(vidim.getSelected()) != null) {//vybrane pismenko
                    GL11.glBindTexture(GL11.GL_TEXTURE_2D, vidim.getletters().get(vidim.getSelected()).getTexture());
                    GL11.glBegin(GL11.GL_QUADS);
                    GL11.glNormal3f(0.0f, 0.0f, 1.0f);
                    GL11.glTexCoord2f(0.0f, 0.0f);
                    GL11.glVertex2i(DM.getWidth() - 100, 0);	// Bottom Left Of The Texture and Quad
                    GL11.glTexCoord2f(1.0f, 0.0f);
                    GL11.glVertex2i(DM.getWidth(), 0);	// Bottom Right Of The Texture and Quad
                    GL11.glTexCoord2f(1.0f, 1.0f);
                    GL11.glVertex2i(DM.getWidth(), 100);	// Top Right Of The Texture and Quad
                    GL11.glTexCoord2f(0.0f, 1.0f);
                    GL11.glVertex2i(DM.getWidth() - 100, 100);	// Top Left Of The Texture and Quad
                    GL11.glEnd();
                }
            }
        }

        setOrthoOff();
        /*for(int x=0;x<hraje.getletters().size();x++){
         GL11.glPushMatrix();        
         GL11.glTranslatef(x*3,2,2);
         GL11.glCallList(hraje.getletters().get(x).getList());
         GL11.glPopMatrix();        
         }*/

        if (hraje != null) {
            glPrint(0, DM.getHeight() - 32, 0, "Playng: " + hraje.toString());
        }
        if (vidim != null) {
            glPrint(0, DM.getHeight() - 48, 0, "Before computer: " + vidim.toString());
        }
        /*
         glPrint(0,DM.getHeight()-64,0,("1("+alphyukazatel1+"): "+alphys[alphyukazatel1])  );
         *
         glPrint(0,DM.getHeight()-80,0,("2("+alphyukazatel2+"): "+alphys[alphyukazatel2])  );
         */

        if (odpocet2 != null) {
            glPrint(0, DM.getHeight() - 64, 0, mm + ":" + ss);
        }
        console.checkDeletions();
        for (int x = 0; x < console.getConsoleItems().size(); x++) {
            glPrint(DM.getWidth() - console.getConsoleItems().get(x).getString().length() * 16, DM.getHeight() - 16 * x - 32, 0, console.getConsoleItemText(x));
        }
        int c = 116;
        if (vidim != null) {
            if (vidim.getSelected() == 0) {
                c = 16;
            }
        }
        for (int x = 0; x < gameLogic.getPlayers().size(); x++) {
            glPrint(DM.getWidth() - gameLogic.getPlayers().get(x).toString().length() * 16, gameLogic.getPlayers().size() * 16 - 16 * x + c, 0, gameLogic.getPlayers().get(x).toString());
        }
        GL11.glColor4f(1f, 1f, 1f, alpha); //jen s glligting; najit alternativu.
        oglDialog.draw();
        GL11.glColor4f(1f, 1f, 1f, 1f); //jen s glligting; najit alternativu.
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_CULL_FACE);
        cursor.Draw();
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glDisable(GL11.GL_BLEND);

        inputConsole.draw(0, 0, DM.getWidth(), 32);

        // Place the light.  Light will move with the rest of the scene
        GL11.glLight(GL11.GL_LIGHT1, GL11.GL_POSITION, allocFloats(lightDirection));

    }

    /**
     * set the field of view and view depth. Don't use gluLookAt() since this
     * will conflict with our own camera position translations that we do in
     * render().
     */
    public static void setPerspective() {
        // select projection matrix (controls view on screen)
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        /* 		fovy,aspect,zNear,zFar  */
        GLU.gluPerspective(30f, // zoom in or out of view
                aspectRatio, // shape of viewport rectangle
                .01f, // Min Z: how far from eye position does view start
                500f);       // max Z: how far from eye position does view extend
        /* don't use gluLookAt if moving camera position manually */

    }

    /**
     * Adjust the Camera position based on keyboard arrow key input.
     */
    public void setCameraPosition() {
        // Turn left
        //if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
        //   cameraRotation += 1.0f;
        // }
        // Turn right
        // if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
        //     cameraRotation -= 1.0f;
        // }
        // move forward in current direction
        if (!todoalpha && !tododeskalpha) {
            if (Keyboard.isKeyDown(Keyboard.KEY_UP) && !alt && (todolshift || todorshift)) {
                cameraPos[1] += .3f;
            }
            // move backward in current direction
            if (Keyboard.isKeyDown(Keyboard.KEY_DOWN) && !alt && (todolshift || todorshift)) {
                cameraPos[1] -= .3f;
            }

            if (Keyboard.isKeyDown(Keyboard.KEY_LEFT) && !alt && (todolshift || todorshift)) {
                cameraPos[0] -= (float) Math.cos(cameraRotation * piover180) * .3f;
                cameraPos[2] -= (float) Math.sin(cameraRotation * piover180) * .3f;
            }
            // move backward in current direction
            if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT) && !alt && (todolshift || todorshift)) {
                cameraPos[0] += (float) Math.cos(cameraRotation * piover180) * .3f;
                cameraPos[2] += (float) Math.sin(cameraRotation * piover180) * .3f;
            }
        }
        // move camera down
        if (Keyboard.isKeyDown(Keyboard.KEY_PRIOR) && (todolshift || todorshift)) {

            cameraPos[0] -= (float) Math.sin(cameraRotation * piover180) * .3f;
            cameraPos[2] -= (float) Math.cos(cameraRotation * piover180) * .3f;
        }
        // move camera up
        if (Keyboard.isKeyDown(Keyboard.KEY_NEXT) && (todolshift || todorshift)) {
            cameraPos[0] += (float) Math.sin(cameraRotation * piover180) * .3f;
            cameraPos[2] += (float) Math.cos(cameraRotation * piover180) * .3f;
        }

    }

    public void mouseMove(int x, int y) {
        if (alt) {
            if (Mouse.getEventDWheel() == 0) {
                muze = true;
            }
            if (muze) {
                resizeWindow();
            }
        }
        if (todolshift && mousedown) {
            xuhel = savexuhel + ((float) (savemx - x)) / 2f;
            if (!todorshift) {
                yuhel = saveyuhel + ((float) (savemy - y)) / 2f;
            }
        }
        if (todorshift && mousedown) {
            zuhel = savezuhel + ((float) (savemx - x)) / 2f;
            yuhel = saveyuhel + ((float) (savemy - y)) / 2f;
        }
    }

    public void mouseDown(int x, int y) {

        oglDialog.mouseDown(globalReaction);

        if (x < 100 && y < 7 * (LSIZE + 10) - 10 && x > 0 && y > 0) {
            int yy = y;
            int selected = yy / (LSIZE + 10);
            if (vidim.getSelected() == 0) {
                vidim.setSelected(selected);
            } else {
                SimpleLetter swap = vidim.getletters().get(selected);
                vidim.getletters().set(selected, vidim.getletters().get(vidim.getSelected()));
                vidim.getletters().set(vidim.getSelected(), swap);
                vidim.setSelected(0);
            }

        }
        if (Mouse.getEventButton() == 0) {
            cursor.setCursorcounter(cursor.getClclick());//20;
            cursor.setCinc(0);
        }
        if (Mouse.getEventButton() == 1) {
            cursor.setCursorcounter(cursor.getCrclick());//38;
            cursor.setCinc(0);
        }
        mousedown = true;
        savemx = x;
        savemy = y;
        savexuhel = xuhel;
        saveyuhel = yuhel;
        savezuhel = zuhel;
    }

    public void mouseUp(int x, int y) {

        mousedown = false;
        if ((new Random()).nextBoolean()) {
            cursor.setCinc(1);
        } else {
            cursor.setCinc(-1);
        }

    }

    public void keyDown(int keycode) {

        oglDialog.keyDown(globalReaction, keycode);

        if (keycode == Keyboard.KEY_F9) {
            STATISTIKAscreenshot();
        }

        if (keycode == Keyboard.KEY_F8) {
            style++;
            if (style == 3) {
                style = 1;
            }
            letterSet.replaceLettersList(style);
        }

        if (keycode == Keyboard.KEY_LMENU || keycode == Keyboard.KEY_RMENU) {
            alt = true;
        }

        if (keycode == Keyboard.KEY_ADD && alt && desk instanceof Desks3D) {
            ((Desks3D) desk).addMovement(0.5f);
            ((GameDesktop3D) gameDesktop).setMovement(((Desks3D) desk).getMovement());

        }

        if (keycode == Keyboard.KEY_SUBTRACT && alt && desk instanceof Desks3D) {
            ((Desks3D) desk).addMovement(-0.5f);
            ((GameDesktop3D) gameDesktop).setMovement(((Desks3D) desk).getMovement());
        }

        if (keycode == Keyboard.KEY_ADD && !alt) {
            if (todoalpha) {
                if (alpha < 1) {

                    float tmpalpha = alpha;
                    alpha += 0.1;
                    if (!console.updateItem("[s] Alpha " + String.valueOf((int) (tmpalpha * 100f)), "[s] Alpha " + String.valueOf((int) (alpha * 100f)))) {
                        console.updateItem("Chenging alpha of letters", "[s] Alpha " + String.valueOf((int) (alpha * 100f)));
                    }
                }
            }
            if (tododeskalpha) {
                if (deskalpha < 1) {
                    float tmpalpha = deskalpha;
                    deskalpha += 0.1;
                    if (!console.updateItem("[s] Alpha " + String.valueOf((int) (tmpalpha * 100f)), "[s] Alpha " + String.valueOf((int) (deskalpha * 100f)))) {
                        console.updateItem("Chenging alpha of desk", "[s] Alpha " + String.valueOf((int) (deskalpha * 100f)));
                    }
                }
            }
        }

        if (keycode == Keyboard.KEY_SUBTRACT && !alt) {
            if (todoalpha) {
                if (alpha > 0) {
                    float tmpalpha = alpha;
                    alpha -= 0.1;
                    if (!console.updateItem("[s] Alpha " + String.valueOf((int) (tmpalpha * 100f)), "[s] Alpha " + String.valueOf((int) (alpha * 100f)))) {
                        console.updateItem("Chenging alpha of letters", "[s] Alpha " + String.valueOf((int) (alpha * 100f)));
                    }
                }
            }

            if (tododeskalpha) {
                if (deskalpha > 0) {
                    float tmpdeskalpha = deskalpha;
                    deskalpha -= 0.1;
                    if (!console.updateItem("[s] Alpha " + String.valueOf((int) (tmpdeskalpha * 100f)), "[s] Alpha " + String.valueOf((int) (deskalpha * 100f)))) {
                        console.updateItem("Chenging alpha of desk", "[s] Alpha " + String.valueOf((int) (deskalpha * 100f)));
                    }

                }
            }

        }

        if (keycode == Keyboard.KEY_A) {
            console.addItemSilent("Chenging alpha of letters");
            todoalpha = true;

        }

        if (keycode == Keyboard.KEY_D) {
            console.addItemSilent("Chenging alpha of desk");
            tododeskalpha = true;

        }

        if (oglDialog.isVisible()) {
            return;
        }
        if (keycode == Keyboard.KEY_BACK && alt && !settings.isInet()) {
            if (settings.ints[settings.turn] > 1) {
                settings.ints[settings.turn]--;
                String jmenoh = hraje.getJmeno();
                hraje = SaveLoad.AutooLoadGame(settings.ints[settings.turn], console, gameDesktop, gameLogic, letterSet);
                vidim = gameLogic.vidimForPlayer(hraje);
                console.addItem("turn  was turned back! by " + jmenoh);
            } else {
                console.addItem("turn  stack is on the bottom");
            }

        }

        if (keycode == Keyboard.KEY_F10) {
            Calendar dnes = new GregorianCalendar();
            String s = ("Saves" + File.separator + dnes.getTime().toString().replace(':', '_').replace(' ', '_') + ".zip");
            console.addItem("saving: " + s);
            BufferedImage ss = null;
//if enough memorry
            //if (Runtime.getRuntime().freeMemory()>360*(int)Math.pow(2,20)) //nejak nejede:(
            ss = GLApp.screenShot(DM.getWidth(), DM.getHeight());
//}                
            SaveLoad.SaveGame(s, console, settings, gameDesktop, gameLogic, letterSet, hraje, ss);
            console.addItem("saved: " + s);
        }

        if (keycode == Keyboard.KEY_ESCAPE) {
            int[] btns = new int[2];
            btns[0] = OGLdialog.MB_OK;
            btns[1] = OGLdialog.MB_NO;
            ArrayList<String> dl = new ArrayList<String>();
            dl.add("are you shure to close?");
            oglDialog.execute(btns, dl, 450, 200);

            if (globalReaction.reaction == OGLdialog.MR_OK) {

                Calendar dnes = new GregorianCalendar();
                File f = new File("logs" + File.separator + dnes.getTime().toString().replace(':', '_').replace(' ', '_') + ".txt");
                console.savehystory(f, true);
                gameLogic.setKillui(true);
                finished = true;
            }

        }

        if (keycode == Keyboard.KEY_TAB && settings.bools[settings.inputkonzole] && !alt) {
            inputConsole.setZadano("");
            inputConsole.setVisible(!inputConsole.isVisible());

        }
        if (inputConsole.isVisible()) {
            inputConsole.input(keycode, alt);
        } else {

            if (keycode == Keyboard.KEY_R && !alt) {//restart
                int[] btns = new int[2];
                btns[0] = OGLdialog.MB_OK;
                btns[1] = OGLdialog.MB_NO;
                ArrayList<String> dl = new ArrayList<String>();
                dl.add("are you shure to restart?");
                oglDialog.execute(btns, dl, 450, 200);

                if (globalReaction.reaction == OGLdialog.MR_NO) {
                    return;
                }

                if (!settings.isInet()) {
                    gameLogic.setKillui(true);
                    restartGame();
                } else {
                    if (settings.IsServer()) {
                        gameLogic.nulujVote();
                        gameLogic.voteOK();
                        zpravaVsem(IC.commands[17] + "-" + vidim.getJmeno());

                        for (Iterator it = gameLogic.getPlayers().iterator(); it.hasNext();) {
                            Player elem = (Player) it.next();

                            if (elem.isLocal() && elem.getAI() == 0 && elem != vidim) {
                                //dialog na loalni humas
                                ArrayList<String> l = new ArrayList();
                                l.add(vidim.getJmeno() + " wants to restart game.");
                                l.add(elem.getJmeno() + " do you want to?");
                                if (settings.bools[Settings.sounds]) {
                                    pv.play();
                                }
                                btns = new int[3];
                                btns[0] = OGLdialog.MB_OK;
                                btns[1] = OGLdialog.MB_NO;
                                btns[2] = OGLdialog.MB_CANCEL;
                                oglDialog.execute(btns, l, 500, 300);
                                switch (globalReaction.reaction) {
                                    case OGLdialog.MR_NO:
                                        gameLogic.voteNo();
                                        console.addItem(elem.getJmeno() + " disagree");
                                        break;
                                    case OGLdialog.MR_OK:
                                        gameLogic.voteOK();
                                        console.addItem(elem.getJmeno() + " agree");
                                        break;
                                    case OGLdialog.MR_CANCEL:
                                        gameLogic.voteCancel();
                                        console.addItem(elem.getJmeno() + " no idea");
                                        break;

                                }
                            }

                        }

                        int humans = gameLogic.getHumans();
                        boolean a = gameLogic.waitForVotes(0, humans, false);//true pokud budu chtit rreaktivitu

                        if (!a) {
                            return;
                        }

                        //zpravaVsem(IC.commands[18]);
                        gameLogic.setKillui(true);
                        restartGame();

                    } else {
                        playerThread.messageToServer(IC.commands[17] + "-" + hraje.getJmeno());

                    }
                }

            }
            if (keycode == Keyboard.KEY_P && vidim == hraje && !alt) {//pass
                if (!settings.isInet()) {
                    if (passEngine()) {
                        DALSIHRAC();
                    }
                } else {
                    if (settings.IsServer()) {
                        if (passEngine()) {
                            DALSIHRAC();//??? co sem? zmenit dalsi hrac na posilani pro clienty? asi jo:(
                        }
                    } else {
                        playerThread.messageToServer(IC.commands[6]);
                    }
                }

            }

            if (keycode == Keyboard.KEY_C && vidim == hraje && !alt) {//change leeters , return from budffer to pocket 
                if (!settings.isInet()) {
                    if (changeLetterEngine() == 0) {
                        DALSIHRAC();
                    }
                } else {
                    if (settings.IsServer()) {
                        if (changeLetterEngine() == 0) {
                            DALSIHRAC();//??? co sem? zmenit dalsi hrac na posilani pro clienty? asi jo:(
                        }
                    } else {
                        ArrayList<PlacedLetter> a = (ArrayList) gameDesktop.getPlacedLetters().clone();
                        String sell = "null";
                        if (gameDesktop.getSelected() != null) {
                            sell = gameDesktop.getSelected().toString();
                        }
                        playerThread.messageToServer(IC.commands[13] + "-" + sell + Cammons.vytvorZpravuOTahu(a));

                    }
                }

            }

            if (keycode == Keyboard.KEY_J && settings.bools[settings.jokerchanging] && vidim == hraje && !alt) {
                int x = gameDesktop.getCx();
                int y = gameDesktop.getCy();
                int z = 0;
                String c = gameDesktop.getSelected().getType();
                if (gameDesktop instanceof GameDesktop3D) {
                    z = ((GameDesktop3D) gameDesktop).getCz();
                }
                //replace engine  neneni hotovyyy!!!!
                if (!settings.isInet()) {
                    int rje = replaceJokerEngine(x, y, z);
                    if (Math.abs(rje) == 1) {
                        DALSIHRAC();
                    }
                } else {
                    if (settings.IsServer()) {
                        int rje = replaceJokerEngine(x, y, z);
                        if (rje == 0 || rje == -1) {
                            zpravaVsem(IC.commands[15] + "-" + x + "-" + y + "-" + z + "-" + c);
                        }
                        if (Math.abs(rje) == 1) {
                            hraje.allLettersBack(gameDesktop);
                            DALSIHRAC();//??? co sem? zmenit dalsi hrac na posilani pro clienty? asi jo:(
                        }
                    } else {

                        String sell = "null";
                        if (gameDesktop.getSelected() != null) {
                            sell = gameDesktop.getSelected().toString();
                        }
                        playerThread.messageToServer(IC.commands[14] + "-" + x + "-" + y + "-" + z + "-" + sell + Cammons.vytvorZpravuOTahu((ArrayList<PlacedLetter>) gameDesktop.getPlacedLetters().clone()));

                    }
                }

            }

            if (keycode == Keyboard.KEY_DELETE && !alt && vidim == hraje) {
                if (gameDesktop.getSelected() != null) {
                    hraje.returnToLetters(gameDesktop.getSelected());
                    gameDesktop.setSelected(null);

                }
            }

            if (keycode == Keyboard.KEY_DELETE && alt && vidim == hraje) {
                gameDesktop.delOnBuffer(hraje);

            }
            if (alt) {
                for (int i = 0; i < hraje.getletters().size(); i++) {
                    if (hraje.getletters().get(i) != null) {
                        String znak = new String(new char[]{Keyboard.getEventCharacter()});
                        if (znak.equalsIgnoreCase(hraje.getletters().get(i).getType())) {
                            keycode = 8 - i;
                            break;
                        }

                    }
                }
            }

            if (vidim == hraje) {
                if (keycode == Keyboard.KEY_1) {
                    int qc = 6;
                    if (hraje.getPismeno(qc) != null) {
                        hraje.setSelected(0);
                        SimpleLetter byvaly = gameDesktop.getSelected();
                        gameDesktop.setSelected(hraje.getPismeno(qc));
                        hraje.getletters().set(qc, null);
                        hraje.returnToLetters(byvaly);
                    }
                }

                if (keycode == Keyboard.KEY_2) {
                    int qc = 5;
                    if (hraje.getPismeno(qc) != null) {
                        hraje.setSelected(0);
                        SimpleLetter byvaly = gameDesktop.getSelected();
                        gameDesktop.setSelected(hraje.getPismeno(qc));
                        hraje.getletters().set(qc, null);
                        hraje.returnToLetters(byvaly);
                    }
                }
                if (keycode == Keyboard.KEY_3) {
                    int qc = 4;
                    if (hraje.getPismeno(qc) != null) {
                        hraje.setSelected(0);
                        SimpleLetter byvaly = gameDesktop.getSelected();
                        gameDesktop.setSelected(hraje.getPismeno(qc));
                        hraje.getletters().set(qc, null);
                        hraje.returnToLetters(byvaly);
                    }
                }
                if (keycode == Keyboard.KEY_4) {
                    int qc = 3;
                    if (hraje.getPismeno(qc) != null) {
                        hraje.setSelected(0);
                        SimpleLetter byvaly = gameDesktop.getSelected();
                        gameDesktop.setSelected(hraje.getPismeno(qc));
                        hraje.getletters().set(qc, null);
                        hraje.returnToLetters(byvaly);
                    }
                }
                if (keycode == Keyboard.KEY_5) {
                    int qc = 2;
                    if (hraje.getPismeno(qc) != null) {
                        hraje.setSelected(0);
                        SimpleLetter byvaly = gameDesktop.getSelected();
                        gameDesktop.setSelected(hraje.getPismeno(qc));
                        hraje.getletters().set(qc, null);
                        hraje.returnToLetters(byvaly);
                    }
                }
                if (keycode == Keyboard.KEY_6) {
                    int qc = 1;
                    if (hraje.getPismeno(qc) != null) {
                        hraje.setSelected(0);
                        SimpleLetter byvaly = gameDesktop.getSelected();
                        gameDesktop.setSelected(hraje.getPismeno(qc));
                        hraje.getletters().set(qc, null);
                        hraje.returnToLetters(byvaly);
                    }
                }
                if (keycode == Keyboard.KEY_7) {
                    int qc = 0;
                    if (hraje.getPismeno(qc) != null) {
                        hraje.setSelected(0);
                        SimpleLetter byvaly = gameDesktop.getSelected();
                        gameDesktop.setSelected(hraje.getPismeno(qc));
                        hraje.getletters().set(qc, null);
                        hraje.returnToLetters(byvaly);
                    }
                }

                if (keycode == Keyboard.KEY_SPACE || keycode == Keyboard.KEY_RETURN) {
                    gameDesktop.addLetterToBuffer();

                }

                if (keycode == Keyboard.KEY_BACK && !alt) {
                    SimpleLetter alfa = gameDesktop.PopLetter();
                    hraje.returnToLetters(alfa);

                }

                if (keycode == Keyboard.KEY_F12) {//next player?
                    if (!settings.isInet()) {
                        int npe = nextPlayerEngine();
                        if (npe == 1 || npe == 3) {
                            DALSIHRAC();
                        }
                    } else {
                        ArrayList<PlacedLetter> a = (ArrayList) gameDesktop.getPlacedLetters().clone();
                        if (settings.IsServer()) {
                            int npe = nextPlayerEngine();
                            if (npe == 1) {
                                //posli vsem co si maji pridat
                                zpravaVsem(IC.commands[11] + Cammons.vytvorZpravuOTahu(a));
                                //score vsem
                                zpravaVsem(IC.commands[10] + "-" + vytvorZpravuOScore());

                            }
                            if (npe == 1 || npe == 3) {
                                DALSIHRAC();//??? co sem? zmenit dalsi hrac na posilani pro clienty? asi jo:(
                            }
                        } else {
                            String sell = "null";
                            if (gameDesktop.getSelected() != null) {
                                sell = gameDesktop.getSelected().toString();
                            }
                            playerThread.messageToServer(IC.commands[9] + "-" + sell + Cammons.vytvorZpravuOTahu(a));
                        }
                    }

                }

                if (keycode == Keyboard.KEY_F11 && settings.bools[settings.turncheck]) {//only local??? vypada toze jo...
                    try {
                        console.shutUp(true);
                        console.addItem("****start simulating turn****");
                        boolean playerscheck = settings.bools[settings.playercheck];
                        settings.bools[settings.playercheck] = false;
                        if (gameDesktop.getPlacedLetters().size() > 0) {
                            int r = gameLogic.evaulateTurn(desk, gameDesktop, settings, hraje, true);//kontrola tahu

                            if (r < 0) {
                                errortoconsole(r);

                            } else {
                                console.addItem(hraje.getJmeno() + " recived " + String.valueOf(r) + " => " + String.valueOf(r + hraje.getScore()));

                            }
                        } else {
                            console.addItem("You placed no letter!! (try pass)");
                        }
                        settings.bools[settings.playercheck] = playerscheck;
                        console.addItem("****end simulating turn****");
                    } finally {
                        console.shutUp(false);
                    }
                }
            }

            if (keycode == Keyboard.KEY_F1) {//help:)
                ArrayList<String> dl = new ArrayList<String>();

                dl.add("pgm by Jiri Vanek - judovana@email.cz");
                dl.add("pgm homepage: jScrabble.wz.cz");
                dl.add("version 1.1 release 1.0");
                dl.add("");

                dl.add("alt+up,down/right left - move desk ");
                dl.add("Lshift or Rshift+ mouse left button rotate");
                dl.add("shift +pgup/pgdown zoom");
                dl.add("alt + [+-] 3D movement");
                dl.add("1-7 select letter");
                dl.add("P pass");
                dl.add("R reset game");
                dl.add("C change leters");
                dl.add("P pass");
                dl.add("F12 TURN!!!");
                dl.add("SPACE or ENTER  palce letter");
                dl.add("del unselect letter");
                dl.add("alt+del move placed letter UNDER cursor back to letters");
                dl.add("backspace delete placed letter(s)");
                dl.add("clicks on your letters to Thing loudly;)");
                dl.add("F2 clear console");
                dl.add("F3 pause console");
                dl.add("F4 fullscreen/window");
                dl.add("F11 simulate turn");
                dl.add("F5 recycle console item");
                dl.add("F6 cursor position");
                dl.add("J swap joker");
                dl.add("ESC end game");
                dl.add("A(+-) transparenci of letters");
                dl.add("D(+-) transparenci of desk");
                dl.add("TAB console(if enabled)");
                dl.add("Alt+letter direct selection");
                dl.add("Alt+backspace return turn<warrning-now working only in hotseat of n humans, and bad:(");
                dl.add("f9 screenshot");
                dl.add("f8 change style of letters");
                dl.add("Alt+mousewheel-change resolution/windowsize");

                int[] btns = new int[1];
                btns[0] = OGLdialog.MB_CANCEL;
                oglDialog.execute(btns, dl, DM.getWidth() * 2 / 3, DM.getHeight() * 2 / 3);

            }

            if (keycode == Keyboard.KEY_F2) {//help:)
                console.allToHistory();
                //console.getConsoleItems().clear(); uz je to v hystory
            }

            if (keycode == Keyboard.KEY_F3) {//help:)
                if (console.isPaused()) {
                    console.unPause();
                } else {
                    console.pause();
                    console.addItemSilent("console paused");
                }

            }

            if (keycode == Keyboard.KEY_F4) {//help:)
                fullScreen = !fullScreen;
                try {
                    Display.setFullscreen(fullScreen);
                } catch (LWJGLException ex) {
                    ex.printStackTrace();
                }
            }

            if (keycode == Keyboard.KEY_F5) {
                console.recycleItem();
            }

            if (keycode == Keyboard.KEY_F6) {
                int z = 0;
                if (gameDesktop instanceof GameDesktop3D) {
                    z = ((GameDesktop3D) gameDesktop).getCz();
                }
                console.addItemSilent("ShadowCursor at: [" + gameDesktop.getCx() + "," + gameDesktop.getCy() + "z" + z + "]");
            }

            if (keycode == Keyboard.KEY_LEFT) {
                gameDesktop.moveCLeft();
            }

            if (keycode == Keyboard.KEY_RIGHT) {
                gameDesktop.moveCright();
            }

            if (keycode == Keyboard.KEY_UP && !alt) {
                gameDesktop.moveCup();
            }

            if (keycode == Keyboard.KEY_DOWN && !alt) {
                gameDesktop.moveCdown();
            }

            if (gameDesktop instanceof GameDesktop3D) {
                if (keycode == Keyboard.KEY_UP && alt) {
                    ((GameDesktop3D) gameDesktop).moveCzUp();
                }

                if (keycode == Keyboard.KEY_DOWN && alt) {
                    ((GameDesktop3D) gameDesktop).moveCzDown();
                }

            }

            if (keycode == Keyboard.KEY_HOME) {
                cameraPos[0] = 0f;
                cameraPos[1] = 0f;
                cameraPos[2] = 35f;
                cameraRotation = 0f;
            }
            if (keycode == Keyboard.KEY_END) {
                xuhel = 0f;
                yuhel = 0f;
                zuhel = 0f;
            }
        }
        if (keycode == Keyboard.KEY_LSHIFT) {
            todolshift = true;
        }
        if (keycode == Keyboard.KEY_RSHIFT) {
            todorshift = true;
        }
    }

    public void keyUp(int keycode) {
        /* if (keycode==Keyboard.KEY_PRIOR ){
         if (alt){
         alphyukazatel2++;
         if (alphyukazatel2>=alphy.length)alphyukazatel2=0;
            
         }else{
         alphyukazatel1++;
         if (alphyukazatel1>=alphy.length)alphyukazatel1=0;
            
         }
         }
        
         if (keycode==Keyboard.KEY_NEXT){
         if (alt){
         alphyukazatel2--;
         if (alphyukazatel2<0)alphyukazatel2=alphy.length-1;
            
         }else{
         alphyukazatel1--;
         if (alphyukazatel1<0)alphyukazatel1=alphy.length-1;;
            
         }
         }*/

        if (keycode == Keyboard.KEY_LMENU || keycode == Keyboard.KEY_RMENU) {
            alt = false;
        }

        if (!inputConsole.isVisible()) {
            if (keycode == Keyboard.KEY_A) {
                todoalpha = false;
                console.addItemSilent("finished - alpha of letters ");

            }

            if (keycode == Keyboard.KEY_D) {
                tododeskalpha = false;
                console.addItemSilent("finished - alpha of desk");

            }

        }

        if (keycode == Keyboard.KEY_LSHIFT) {
            todolshift = false;
        }

        if (keycode == Keyboard.KEY_RSHIFT) {
            todorshift = false;
        }

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Main demo = new Main();
        demo.run();  // will call init(), render(), mouse functions

    }

    private void errortoconsole(int r) {
        console.addItem(evaulateerror(r));
    }

    private String evaulateerror(int r) {
        switch (r) {
            case -1:
                return ("First turn must be placed on start field!!");

            case -2:
                return ("All letters must be in one row/column!!");

            case -3:
                return ("At least one of your letters must be near of of one on te board!!");

            case -4:
                return ("Middle of the starting seqence must be on start field!!");

            case -5:
                return ("You placed no letter!! (try pass)");

            case -6:
                return ("You cant start with one letter");

            case -7:
                return ("Holes in word!!");

            case -8:
                return ("letter still holded in cursor!!");

            case -9:
                return ("most of players disagree with your turn");

            case -10:
                return ("computer disagree with your turn");
            case -11:
                return ("one ore more word is separated");
            case -12:
                return ("your words are not conected");
            case -13:
                return ("Web servers disaggre with your turn");
            default:
                return ("incorrect turn");

        }
    }

    private void aiturn() {
        long time = System.currentTimeMillis();

        console.addItem("Ai started (" + hraje.getJmeno() + ")");
        aifinished = false;
        ui = new Thread(new Runnable() {
            public void run() {
                // gameLogic.killui=false;
                threadcounter++;
                System.out.println("AIthread - " + threadcounter);
                try {
                    ArrayList<ServerSinglePlayerThread> tmpServerThreads = serverThreads;
                    if (!settings.bools[Settings.distributedAi]) {
                        tmpServerThreads = null;
                    }
                    if (hraje.getAI() == ai.length) {
                        hraje.setAiEngine(getAIengine(ai.length));
                    }
                    ArrayList<PlacedLetter> a = hraje.getAiEngine().hrej(hraje, tmpServerThreads);
                    if (a.size() == 0) {

                    } else {
                        //posli vsem co si maji pridat
                        zpravaVsem(IC.commands[11] + Cammons.vytvorZpravuOTahu(a));
                        //score vsem
                        zpravaVsem(IC.commands[10] + "-" + vytvorZpravuOScore());
                    }
                    gameDesktop.setPlacedLetters(a);
                    gameDesktop.moveLettersFromBufferToDesk();
                    hraje.refulLetters(gameLogic);
                } finally {
                    aifinished = true;
                }
            }
        }, "AIthread - " + threadcounter);
        gameDesktop.setKreslitBuffer(false);
        try {
            ui.start();

            try {

                // Main loop
                if (Thread.currentThread() == thread) {
                    while (!aifinished) {

                        redirectedLoopKore();

                    }
                } else {
                    while (!aifinished) {

                        Thread.sleep(200);

                    }
                }

            } catch (Exception e) {
                System.out.println("GLApp.run(): " + e);
                e.printStackTrace(System.out);
            }
        } finally {
            gameDesktop.setKreslitBuffer(true);
            console.addItem("ai finished : " + (((double) System.currentTimeMillis() - (double) time) / 1000d) + "s");
        }
    }

    public void DALSIHRAC() {

        restartTimer();

        hraje = gameLogic.nextPlayer(hraje);
        vidim = gameLogic.vidimForPlayer(hraje);
        if (vidim == hraje && hraje.getAI() == 0 && settings.bools[Settings.sounds]) {
            yt.play();
        }
        if (serverThreads != null) {
            nahlaskdoHraje();
        }
        settings.ints[settings.turn]++;;
        SaveLoad.AutoSaveGame(settings.ints[settings.turn], console, settings, gameDesktop, gameLogic, letterSet, hraje);

        if (gameLogic.isFinished()) {
            zpravaVsem(IC.commands[23]);
            int btns[] = new int[2];
            btns[0] = OGLdialog.MR_OK;
            btns[1] = OGLdialog.MR_NO;
            ArrayList<String> dl = finishGame();
            oglDialog.execute(btns, dl, DM.getWidth() / 2, DM.getHeight() * 2 / 3);
            if (globalReaction.reaction == OGLdialog.MR_OK) {
                restartGame();
                return;
            }
            if (globalReaction.reaction == OGLdialog.MR_NO) {
                Calendar dnes = new GregorianCalendar();
                File f = new File("logs" + File.separator + dnes.getTime().toString().replace(':', '_').replace(' ', '_') + ".txt");
                console.savehystory(f, true);
                finished = true;
                return;
            }
        }
        while (hraje.getAI() > 0) {

            aiturn();
            if (gameLogic.isFinished()) {
                zpravaVsem(IC.commands[23]);
                int btns[] = new int[2];
                btns[0] = OGLdialog.MR_OK;
                btns[1] = OGLdialog.MR_NO;
                ArrayList<String> dl = finishGame();
                /* //STATISTIKA*/ oglDialog.execute(btns, dl, DM.getWidth() / 2, DM.getHeight() * 2 / 3);
                //globalReaction.reaction=OGLdialog.MR_OK; //STATISTIKA
                //STATISTIKAscreenshot();
                if (globalReaction.reaction == OGLdialog.MR_OK) {
                    restartGame();
                    return;
                }
                if (globalReaction.reaction == OGLdialog.MR_NO) {
                    Calendar dnes = new GregorianCalendar();
                    File f = new File("logs" + File.separator + dnes.getTime().toString().replace(':', '_').replace(' ', '_') + ".txt");
                    console.savehystory(f, true);
                    finished = true;
                    return;
                }
            }
            hraje = gameLogic.nextPlayer(hraje);
            vidim = gameLogic.vidimForPlayer(hraje);
            if (vidim == hraje && hraje.getAI() == 0 && settings.bools[Settings.sounds]) {
                yt.play();
            }
            nahlaskdoHraje();
        }
    }

    private void restartGame() {
        gameLogic.setKillui(false);
        gameLogic.ukazatel = 0;
        console.addItem("Game restarted");
        System.out.print("Game restarted\n");
        if (gameDesktop.getSelected() != null) {
            letterSet.addLetterToPocket(gameDesktop.getSelected());
        }
        gameDesktop.setSelected(null);
        int lvls = 1;
        if (gameDesktop instanceof GameDesktop3D) {
            lvls = ((GameDesktop3D) gameDesktop).getLevels();
        }
        for (int l = 0; l < lvls; l++) {
            for (int x = 0; x < desk.getWidth(); x++) {
                for (int y = 0; y < desk.getHeight(); y++) {
                    if (!(gameDesktop instanceof GameDesktop3D)) {
                        if (gameDesktop.getPolePrvek(x, y) != null) {
                            letterSet.addLetterToPocket(gameDesktop.getPolePrvek(x, y));
                            gameDesktop.setPolePrvek(x, y, null);
                        }
                    } else {
                        GameDesktop3D g3d = (GameDesktop3D) gameDesktop;
                        if (g3d.getPolePrvek(x, y, l) != null) {
                            letterSet.addLetterToPocket(g3d.getPolePrvek(x, y, l));
                            g3d.setPolePrvek(x, y, l, null);
                        }
                    }
                }
            }
        }
        for (int x = 0; x < gameDesktop.getPlacedLetters().size(); x++) {
            letterSet.addLetterToPocket(gameDesktop.getPlacedLetters().get(x).getLetter());
        }
        gameDesktop.getPlacedLetters().clear();

        for (int x = 0; x < gameLogic.getPlayers().size(); x++) {
            hraje = gameLogic.getPlayers().get(x);
            for (int z = 0; z < hraje.getletters().size(); z++) {
                if (hraje.getletters().get(z) != null) {
                    letterSet.addLetterToPocket(hraje.getletters().get(z));
                }
            }
            hraje.getletters().clear();
            hraje.setScore(0);
            hraje.setPasses(0);
            for (int y = 0; y <= 6; y++) {
                hraje.getletters().add(letterSet.getRandomLetterFromPocket());
            }

        }
        hraje = null;
        if (settings.bools[settings.randomizeplayers]) {
            gameLogic.randomizePlayers(100);
        }
        //hraje=gameLogic.nextPlayer(hraje);
        if (serverThreads != null) {
            //cisteni
            zpravaVsem(IC.commands[19]);
            //poradi
            nahlasHrace();
            //pismenka
            nahlasVsemPismenka();
        }

        DALSIHRAC();
    }

    private ArrayList<String> finishGame() {
        ArrayList<String> dl = gameLogic.getResults();
        Collections.sort(dl);
        Collections.reverse(dl);
        for (Iterator it = dl.iterator(); it.hasNext();) {
            String elem = (String) it.next();
            console.addItem(elem);

        }
        dl.add(0, "Game finished, restart? ");
        return dl;
    }

    public void zpravaVsem(String string) {
        if (serverThreads == null) {
            return;
        }
        for (ServerSinglePlayerThread elem : serverThreads) {
            elem.messageToPlayer(string);
        }
    }

    private void nahlasVsemPismenka() {
        for (ServerSinglePlayerThread elem : serverThreads) {
            String s = vytvorZpravuOPismenkach(elem.getPlayer());
            elem.messageToPlayer(IC.commands[4] + "-" + s);
        }
    }

    private String vytvorZpravuOPismenkach(Player player) {
        String s = "";
        for (SimpleLetter elem : player.getletters()) {
            s += elem.toString();
        }
        return s;
    }

    private void nahlasHrace() {
        for (ServerSinglePlayerThread elem : serverThreads) {
            String s = vytvorZpravuOHracich(elem.getPlayer());
            elem.messageToPlayer(IC.commands[2] + "-" + s);
        }
    }

    private String vytvorZpravuOHracich(Player player) {
        String s = "";
        for (Player elem : gameLogic.getPlayers()) {
            int local = 0;
            if (elem == player) {
                local = 1;
            }
            s += elem.getJmeno() + ";" + local + ";" + elem.getAI() + "/";
        }
        return s;
    }

    private String vytvorZpravuOScore() {
        String s = "";
        for (Player elem : gameLogic.getPlayers()) {
            s += elem.getScore() + "/";
        }
        return s;
    }

    private void zpravaVsemKrome(Player p, String zprava) {
        for (ServerSinglePlayerThread elem : serverThreads) {
            if (elem.getPlayer() != p) {
                elem.messageToPlayer(zprava);
            }
        }

    }

    private void nahlaskdoHraje() {
        zpravaVsem(IC.commands[5] + "-" + String.valueOf(gameLogic.thisPlayerIndex(hraje)));
    }

    public void proceedServerMessage(String[] c, ServerSinglePlayerThread st) {
        if (c[0].equalsIgnoreCase(IC.commands[3])) {
            System.out.println("Server-command accepted: console used");
            String s = "";
            for (int i = 1; i < c.length; i++) {
                s += c[i];
                if (i < c.length - 1) {
                    s += "-";
                }
            }
            console.addItemSilent(s);
            zpravaVsemKrome(st.getPlayer(), s);
        }
        if (c[0].equalsIgnoreCase(IC.commands[6])) {
            System.out.println("Server-command accepted: pass(" + st.getPlayer().getJmeno() + ")");
            ArrayList l = (ArrayList) hraje.getletters().clone();
            if (passEngine()) {
                hraje.addPass();

                //vymazat mu desk;
                st.messageToPlayer(IC.commands[7]);
                //nahlasit mu jeho  pismenka
                hraje.setLetters(l);
                st.messageToPlayer(IC.commands[4] + "-" + vytvorZpravuOPismenkach(hraje));
                DALSIHRAC();
            }
        }

        if (c[0].equalsIgnoreCase(IC.commands[9])) {
            System.out.println("Server-command accepted: TURN(" + st.getPlayer().getJmeno() + ")");
            ArrayList<PlacedLetter> a = new ArrayList();
            try {
                if (c[1].trim().equalsIgnoreCase("null")) {
                    gameDesktop.setSelected(null);
                } else {
                    gameDesktop.setSelected(letterSet.parsePalcedLetter(c[1]).getLetter());
                }
                for (int i = 2; i < c.length; i++) {
                    //A[5, 4, 3]

                    a.add(letterSet.parsePalcedLetter(c[i]));

                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            gameDesktop.setPlacedLetters((ArrayList<PlacedLetter>) a.clone());
            int npe = nextPlayerEngine();

            if (npe == 1 || npe == 3) {
                //tomuhle ze ma vymazzat buffer
                st.messageToPlayer(IC.commands[7]);
                hraje.setPasses(0);
                if (npe == 1) {
                    //buffer vsem
                    zpravaVsem(IC.commands[11] + Cammons.vytvorZpravuOTahu(a));
                    //score vsem
                    zpravaVsem(IC.commands[10] + "-" + vytvorZpravuOScore());
                    //tomuhle na serveru zmenit obsah kamenu v zasobniku!
                    odeberPismenka(hraje, a);
                    hraje.refulLetters(gameLogic);
                    //tomuhle jaky dostal pismenka
                    st.messageToPlayer(IC.commands[4] + "-" + vytvorZpravuOPismenkach(hraje));
                }
                DALSIHRAC();
            } else {
                //tomuhle ze je debil
                if (settings.bools[Settings.skipplayer]) {
                    st.messageToPlayer(IC.commands[7]);
                    DALSIHRAC();
                }
            }
        }
        if (c[0].equalsIgnoreCase(IC.commands[12])) {
            System.out.println("Server command accepted: time up");
            st.messageToPlayer(IC.commands[7]);
            DALSIHRAC();
        }

        if (c[0].equalsIgnoreCase(IC.commands[13])) {
            System.out.println("Server-command accepted: cahnging letters(" + st.getPlayer().getJmeno() + ")");
            ArrayList<PlacedLetter> a = new ArrayList();
            try {
                if (c[1].trim().equalsIgnoreCase("null")) {
                    gameDesktop.setSelected(null);
                } else {
                    gameDesktop.setSelected(letterSet.parsePalcedLetter(c[1]).getLetter());
                }
                for (int i = 2; i < c.length; i++) {
                    //A[5, 4, 3]

                    a.add(letterSet.parsePalcedLetter(c[i]));

                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            gameDesktop.setPlacedLetters((ArrayList<PlacedLetter>) a.clone());
            int npe = changeLetterEngine();

            if (npe == 0) {
                hraje.setPasses(0);
                //tomuhle ze ma vymazzat buffer
                st.messageToPlayer(IC.commands[7]);
                if (npe == 0/*??*/) {

                    //tomuhle na serveru zmenit obsah kamenu v zasobniku!
                    odeberPismenka(hraje, a);
                    hraje.refulLetters(gameLogic);
                    //tomuhle jaky dostal pismenka
                    st.messageToPlayer(IC.commands[4] + "-" + vytvorZpravuOPismenkach(hraje));
                }
                DALSIHRAC();
            }
        }

        if (c[0].equalsIgnoreCase(IC.commands[14])) {
            System.out.println("Server-command accepted: cahnging joker(" + st.getPlayer().getJmeno() + ")");
            int x = Integer.parseInt(c[1]);
            int y = Integer.parseInt(c[2]);
            int z = Integer.parseInt(c[3]);
            String sell = c[4];
            /*String buff=c[5]-[c6]...;*/
            if (sell.equalsIgnoreCase("null")) {
                gameDesktop.setSelected(null);
            } else {
                gameDesktop.setSelected(letterSet.createLetter(sell));
            }
            ArrayList<PlacedLetter> a = new ArrayList();
            for (int i = 5; i < c.length; i++) {
                try {
                    //A[5, 4, 3]

                    a.add(letterSet.parsePalcedLetter(c[i]));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            }
            gameDesktop.setPlacedLetters(a);
            int jce = replaceJokerEngine(x, y, z);
            gameDesktop.setPlacedLetters(new ArrayList());
            gameDesktop.setSelected(null);
            switch (jce) {
                case -1://ok skipping 
                    //vsem co je  ted na onom policku
                    zpravaVsem(IC.commands[15] + "-" + x + "-" + y + "-" + z + "-" + sell);
                    //tomuhle ze ma na kurzoru jolyka
                    st.messageToPlayer(IC.commands[16] + "-%");
                    //tomuhle at si vymaze buffer
                    st.messageToPlayer(IC.commands[7]);
                    DALSIHRAC();
                    break;
                case 0://ok not sciping 
                    //vsem jco se nachazi na  danem policku
                    zpravaVsem(IC.commands[15] + "-" + x + "-" + y + "-" + z + "-" + sell);
                    //tomuhle ze ma ted na kurzoru jolyka
                    st.messageToPlayer(IC.commands[16] + "-%");
                    break;
                case 1: //faild skipping
                    //tomuhle at si vymaze buffer
                    st.messageToPlayer(IC.commands[7]);
                    DALSIHRAC();
                    break;
                default://faild playying
                    //nic? :D
                    break;

            }

        }

        if (c[0].equalsIgnoreCase(IC.commands[18])) {
            System.out.println("Server-command accepted: voting (" + st.getPlayer().getJmeno() + ")");
            if (c.length == 3) {
                c[1] = "-" + c[2];
            }
            if (Integer.parseInt(c[1]) == OGLdialog.MR_OK) {
                gameLogic.voteOK();
                console.addItem(st.getPlayer().getJmeno() + " agree");
            }
            if (Integer.parseInt(c[1]) == OGLdialog.MR_NO) {
                gameLogic.voteNo();
                console.addItem(st.getPlayer().getJmeno() + " disagree");
            }
            if (Integer.parseInt(c[1]) == OGLdialog.MR_CANCEL) {
                gameLogic.voteCancel();
                console.addItem(st.getPlayer().getJmeno() + " no idea");
            }
        }

        if (c[0].equalsIgnoreCase(IC.commands[17])) {
            {
                System.out.println("Server-command accepted: do you want restart?");
                gameLogic.nulujVote();
                gameLogic.voteOK();//za toho co to vyvolal
                zpravaVsemKrome(st.getPlayer(), IC.commands[17] + "-" + st.getPlayer().getJmeno());

                for (Player elem : gameLogic.getPlayers()) {
                    if (elem.isLocal() && elem.getAI() == 0) {
                        //dialog na loalni humas
                        ArrayList<String> l = new ArrayList();
                        l.add(st.getPlayer().getJmeno() + " wants to restart game.");
                        l.add(elem.getJmeno() + " do you want to?");
                        if (settings.bools[Settings.sounds]) {
                            pv.play();
                        }
                        int[] btns = new int[3];
                        btns[0] = OGLdialog.MB_OK;
                        btns[1] = OGLdialog.MB_NO;
                        btns[2] = OGLdialog.MB_CANCEL;
                        globalReaction.reaction = OGLdialog.MR_NOTHING;
                        ade = new AsynchronousDialogExecuter(btns, l, 500, 300);
                        try {
                            Thread.sleep(200);
                        } catch (InterruptedException ex) {
                            ex.printStackTrace();
                        }
                        while (OGLdialog.MR_NOTHING == globalReaction.reaction) {
                            redirectedLoopNoRenderKore();
                        }
                        switch (globalReaction.reaction) {
                            case OGLdialog.MR_NO:
                                gameLogic.voteNo();
                                console.addItem(elem.getJmeno() + " disagree");
                                break;
                            case OGLdialog.MR_OK:
                                gameLogic.voteOK();
                                console.addItem(elem.getJmeno() + " agree");
                                break;
                            case OGLdialog.MR_CANCEL:
                                gameLogic.voteCancel();
                                console.addItem(elem.getJmeno() + " no idea");
                                break;

                        }
                    }
                }

                int humans = gameLogic.getHumans();
                boolean a = gameLogic.waitForVotes(0, humans, false);
                if (!a) {
                    return;
                }

                //zpravaVsem(IC.commands[18]);
                gameLogic.setKillui(true);
                restartGame();

            }

        }

        if (c[0].equalsIgnoreCase(IC.commands[21])) {
            System.out.println("Server-command accepted: ai result");
            if (hraje.getAiEngine() != null && hraje.getAI() > 0) {
                hraje.getAiEngine().addDistributedresult(c, letterSet);
            }

        }

    }

    public void proceedClientMessage(String[] c, PlayerThread pt) {
        if (c[0].equalsIgnoreCase(IC.commands[3])) {
            System.out.println("Client-command accepted: console used");
            String s = "";
            for (int i = 1; i < c.length; i++) {
                s += c[i];
                if (i < c.length - 1) {
                    s += "-";
                }
            }
            console.addItemSilent(s);
        }
        if (c[0].equalsIgnoreCase(IC.commands[5])) {
            System.out.println("Client-command accepted: wohos turn");
            hraje = gameLogic.getPlayers().get(Integer.parseInt(c[1]));
            vidim = gameLogic.vidimForPlayer(hraje);
            if (vidim == hraje && hraje.getAI() == 0 && settings.bools[Settings.sounds]) {
                yt.play();
            }
            restartTimer();
        }
        if (c[0].equalsIgnoreCase(IC.commands[7])) {
            System.out.println("Client-command accepted: clear buffer");
            //zazalohovat
            while (gameDesktop.getPlacedLetters().size() > 0) {
                SimpleLetter alfa = gameDesktop.PopLetter();
                vidim.returnToLetters(alfa);
            }
            //samzat? :)
            gameDesktop.setPlacedLetters(new ArrayList());
            //selected
            if (gameDesktop.getSelected() != null) {
                vidim.returnToLetters(gameDesktop.getSelected());
            }
            gameDesktop.setSelected(null);

        }
        if (c[0].equalsIgnoreCase(IC.commands[2])) {
            //dodatek k nahlaeni hracu
            //na chuja chuj
            gameLogic.setPlayers(pt.getPlayers());

        }

        if (c[0].equalsIgnoreCase(IC.commands[11])) {
            System.out.println("Client-command accepted: new placed letters");
            ArrayList<PlacedLetter> a = new ArrayList();
            for (int i = 1; i < c.length; i++) {

                try {
                    a.add(letterSet.parsePalcedLetter(c[i]));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            gameDesktop.setPlacedLetters(a);
            gameDesktop.moveLettersFromBufferToDesk();
        }

        if (c[0].equalsIgnoreCase(IC.commands[10])) {
            System.out.println("client-command accepted: scores");

            String[] cc = c[1].split("/");
            for (int i = 0; i < cc.length; i++) {
                gameLogic.getPlayers().get(i).setScore(Integer.parseInt(cc[i]));
            }
        }

        if (c[0].equalsIgnoreCase(IC.commands[4])) {
            Player v = getLocalPlayer();
            System.out.println("client-command accepted: settingable letters");

            ArrayList<SimpleLetter> a = new ArrayList();

            for (int i = 0; i < c[1].length(); i++) {
                a.add(letterSet.createLetter(c[1].substring(i, i + 1)));
            }
            for (int i = c[1].length(); i < 7; i++) {
                a.add(null);
            }
            if (v != null) {
                v.setLetters(a);
            } else {
                System.out.println("strange error, no players?");
            }
        }
        if (c[0].equalsIgnoreCase(IC.commands[15])) {
            System.out.println("client-command accepted: sett letter direct");
            int x = Integer.parseInt(c[1]);
            int y = Integer.parseInt(c[2]);
            int z = Integer.parseInt(c[3]);
            String sell = c[4];
            if (sell.equalsIgnoreCase("null")) {
                gameDesktop.setLetterSuperDirect(null, x, y, z);
            } else {
                gameDesktop.setLetterSuperDirect(letterSet.createLetter(sell.trim()), x, y, z);
            }
        }

        if (c[0].equalsIgnoreCase(IC.commands[16])) {
            System.out.println("client-command accepted: set sellected direct");
            String sell = c[1];
            if (sell.equalsIgnoreCase("null")) {
                gameDesktop.setSelected(null);
            } else {
                gameDesktop.setSelected(letterSet.createLetter(sell.trim()));
            }
        }
        if (c[0].equalsIgnoreCase(IC.commands[17])) {
            System.out.println("client-command accepted: do you want restart?");
            int[] btns = {OGLdialog.MB_OK, OGLdialog.MB_NO, OGLdialog.MB_CANCEL};
            ArrayList<String> l = new ArrayList();
            l.add(c[1] + " wants to restart game.");
            l.add("do you want to?");
            if (settings.bools[Settings.sounds]) {
                pv.play();
            }
            globalReaction.reaction = OGLdialog.MR_NOTHING;
            ade = new AsynchronousDialogExecuter(btns, l, 450, 300);

            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            while (OGLdialog.MR_NOTHING == globalReaction.reaction) {
                redirectedLoopNoRenderKore();
            }
            //nemuzu volat render z jineho vlakna
            //spani asi neni doreseny

            playerThread.messageToServer(IC.commands[18] + "-" + globalReaction.reaction);
        }

        if (c[0].equalsIgnoreCase(IC.commands[19])) {
            System.out.println("client-command accepted: clearing desk");
            gameDesktop.setSelected(null);
            gameDesktop.setPlacedLetters(new ArrayList());
            gameDesktop.clean();
        }

        if (c[0].equalsIgnoreCase(IC.commands[20])) {
            final String[] q = c;
            String s;
            System.out.println("client-command accepted: distributed  ai");
            //console.addItemSilent("distributed ai started"); 
            long time = System.currentTimeMillis();
            gameDesktop.setKreslitBuffer(false);
            try {
                s = ai[0].giveMePermutation(q, letterSet);

            } finally {
                gameDesktop.setKreslitBuffer(true);
            }

            // console.addItemSilent("distributed ai finished : "+(((double)System.currentTimeMillis()-(double)time)/1000d)+"s");             
            pt.messageToServer(s);
        }

        if (c[0].equalsIgnoreCase(IC.commands[22])) {
            System.out.println("client-command accepted: accept turn");
            int[] btns = {OGLdialog.MB_OK, OGLdialog.MB_NO};
            ArrayList<String> l = arrayListFromMessage(c);

            if (settings.bools[Settings.sounds]) {
                pv.play();
            }
            globalReaction.reaction = OGLdialog.MR_NOTHING;
            ade = new AsynchronousDialogExecuter(btns, l, DM.getWidth() / 2, DM.getHeight() / 2);

            try {
                Thread.sleep(100);

                while (OGLdialog.MR_NOTHING == globalReaction.reaction) {
                    // redirectedLoopNoRenderKore();    
                    Thread.sleep(200);
                }

            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            playerThread.messageToServer(IC.commands[18] + "-" + globalReaction.reaction);
        }

        if (c[0].equalsIgnoreCase(IC.commands[23])) {
            System.out.println("client-command accepted: game Finished");

            int btns[] = new int[1];
            btns[0] = OGLdialog.MR_CANCEL;

            ArrayList<String> dl = finishGame();

            ade = new AsynchronousDialogExecuter(btns, dl, DM.getWidth() / 2, DM.getHeight() * 2 / 3);
            try {
                Thread.sleep(200);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            while (OGLdialog.MR_NOTHING == globalReaction.reaction) {
                redirectedLoopNoRenderKore();
            }
            console.addItemSilent("server will maybe restart the game. ask him. or tell him you are ending;)");
            hraje = null;

        }

    }

    public void STATISTIKAscreenshot() {
        Calendar dnes = new GregorianCalendar();
        String s = ("Saves" + File.separator + dnes.getTime().toString().replace(':', '_').replace(' ', '_') + ".jpg");
        console.addItem("saving screenshot: " + s);
        BufferedImage ss = null;

        ss = GLApp.screenShot(DM.getWidth(), DM.getHeight());
        try {
//}                
            ImageIO.write(ss, "jpg", new File(s));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private boolean passEngine() {
        if (hraje.jokers < hraje.jokerCount() && !settings.bools[Settings.lostturnafterjoker]) {
            console.addItem("You didnt used replaced joker)");
            return false;
        } else {
            hraje.addPass();
            console.addItem(hraje.getJmeno() + " make pass");
            hraje.allLettersBack(gameDesktop);

            return true;
        }
    }

    private int nextPlayerEngine() {//true=1;false=2;hybrid=3
        if (hraje.jokers < hraje.jokerCount()) {
            console.addItem("You didnt used replaced joker)");
            return 2;
        } else {
            if (gameDesktop.getPlacedLetters().size() > 0) {
                int r = gameLogic.evaulateTurn(desk, gameDesktop, settings, hraje, false);//kontrola tahu

                if (r < 0) {
                    errortoconsole(r);
                    if (settings.bools[Settings.skipplayer]) {
                        hraje.allLettersBack(gameDesktop);
                        Player hral = hraje;
                        //hraje=gameLogic.nextPlayer(hraje); 
                        Player budehrat = gameLogic.nextPlayer(hraje);
                        console.addItem("so " + hral.getJmeno() + " is skipped, now its  " + budehrat.getJmeno() + "turn");
                        hraje.setPasses(0);
                        //poslat signal at si uklidi...:(
                        return 3;
                    }
                    return 2;
                } else {
                    console.addItem(hraje.getJmeno() + " recived " + String.valueOf(r) + " => " + String.valueOf(r + hraje.getScore()));
                    hraje.addToScore(r);
                    gameDesktop.moveLettersFromBufferToDesk();

                    hraje.refulLetters(gameLogic);
                    //hraje=gameLogic.nextPlayer(hraje);
                    hraje.setPasses(0);

                    return 1;

                }
            } else {
                console.addItem("You placed no letter!! (try pass)");
                return 2;
            }
        }

    }

    private void odeberPismenka(Player h, ArrayList<PlacedLetter> a) {
        for (Iterator it = a.iterator(); it.hasNext();) {
            String elem = ((PlacedLetter) it.next()).getLetter().getType();
            for (int i = 0; i < 7; i++) {
                if (h.getletters().get(i) != null) {
                    if (h.getletters().get(i).getType().equalsIgnoreCase(elem)) {
                        h.getletters().set(i, null);
                    }
                }
            }
        }
    }

    private Player getLocalPlayer() {
        for (Iterator it = gameLogic.getPlayers().iterator(); it.hasNext();) {
            Player elem = (Player) it.next();
            if (elem.isLocal()) {
                return elem;
            }

        }
        return null;
    }

    private void restartTimer() {
        ss = 59;
        mm = getSettings().ints[Settings.turntime] - 1;
    }

    private int changeLetterEngine() {
        if (hraje.jokers < hraje.jokerCount() && !settings.bools[Settings.lostturnafterjoker]) {
            console.addItem("You didnt used replaced joker)");
            return 1;
        } else {
            if (letterSet.getLettersInPacketSize() < 7) {
                console.addItem("You cant change when pocket include less then 7 letters");
                return 2;
            } else {
                if (gameDesktop.getSelected() != null) {
                    console.addItem("unplaced letter in cursor");
                    return 3;
                } else {
                    int kolik = gameDesktop.getPlacedLetters().size();
                    if (kolik == 0) {
                        console.addItem("No letter(s) to change-try pass");
                        return 4;
                    } else {
                        console.addItem(hraje.getJmeno() + " changed " + String.valueOf(kolik) + " letters");
                        for (int x = 0; x < gameDesktop.getPlacedLetters().size(); x++) {
                            letterSet.addLetterToPocket(gameDesktop.getPlacedLetters().get(x).getLetter());
                        }

                        //hraje.allLettersBack(gameDesktop);
                        gameDesktop.getPlacedLetters().clear();
                        hraje.refulLetters(gameLogic);
                        hraje.setPasses(0);
                        //hraje=gameLogic.nextPlayer(hraje);
                        //DALSIHRAC();
                        return 0;
                    }
                }
            }
        }
    }

    private int replaceJokerEngine(int x, int y, int z) {
        if (gameDesktop.getSelected() != null) {
            if (gameDesktop.getPolePrvek(x, y, z) != null) {
                if (gameDesktop.getPolePrvek(x, y, z).getType().equals("%")) {
                    int r = gameLogic.testJoker(gameDesktop, x, y, z, settings, hraje);
                    if (r < 0) {
                        errortoconsole(r);
                        if (settings.bools[Settings.skipplayer]) {
                            hraje.allLettersBack(gameDesktop);
                            Player hral = hraje;
                            //hraje=gameLogic.nextPlayer(hraje); 
                            Player budehrat = gameLogic.nextPlayer(hraje);
                            console.addItem("so " + hral.getJmeno() + " is skipped, now its NOW " + budehrat.getJmeno() + "turn");
                            //DALSIHRAC();
                            return 1;
                        }
                        return 5;
                    } else {
                        SimpleLetter swap = gameDesktop.getPolePrvek(x, y, z);
                        SimpleLetter newone = gameDesktop.getSelected();
                        gameDesktop.setLetterDirect(gameDesktop.getSelected(), x, y, z);
                        gameDesktop.setSelected(swap);
                        console.addItem(hraje.getJmeno() + " replaced joker was on [" + gameDesktop.getCx() + "," + gameDesktop.getCy() + "," + z + "] with " + newone.getType());
                        if (settings.bools[Settings.lostturnafterjoker]) {
                            hraje.allLettersBack(gameDesktop);
                            hraje.setPasses(0);
                            //hraje=gameLogic.nextPlayer(hraje);
                            //DALSIHRAC();
                            console.addItem("next player");
                            return -1;
                        } else {
                            return 0;
                        }
                    }
                } else {
                    console.addItem("you cant repalce not-joker letter;)");
                    return 2;
                }
            } else {
                console.addItem("there is nothing to replace;)");
                return 3;
            }
        } else {
            console.addItem("Joker cant be stolen;)");
            return 4;
        }
    }/*
     *dalsihrac 1(faild,skiping),-1(ok skiping)
     *0=ok not skiping
     *else faild,playing
     */


    void setThread(Thread thread) {
        this.thread = thread;
    }

    public void redirectedLoopKore() {

        try {

            // Main loop
            if (!Display.isVisible()) {  //!!!
                Thread.sleep(200L);
            } else if (Display.isCloseRequested()) {  //!!!
                redirectedFinished = true;
            }
            mainLoop();
            Display.update();  //!!!!

        } catch (Exception e) {
            System.out.println("GLApp.run(): " + e);
            e.printStackTrace(System.out);
        }
    }

    public void redirectedLoopNoRenderKore() {

        try {

            // Main loop
            if (!Display.isVisible()) {  //!!!
                Thread.sleep(200L);
            } else if (Display.isCloseRequested()) {  //!!!
                redirectedFinished = true;
            }
            mainLoopNoRender();
            //    Display.update();  //!!!!

        } catch (Exception e) {
            System.out.println("GLApp.run(): " + e);
            e.printStackTrace(System.out);
        }
    }

    private ArrayList arrayListFromMessage(String[] c) {
        ArrayList vysledek = new ArrayList();
        for (int i = 1; i < c.length; i++) {
            vysledek.add(c[i]);
        }
        return vysledek;
    }

    private void resizeWindow() {

        md = Mouse.getEventDWheel();
        if (md != 0) {
            muze = false;
        }
        if (md != 0 && Math.abs(md) != 1) {
            DisplayMode[] l = null;

            try {
                l = Display.getAvailableDisplayModes();
            } catch (LWJGLException ex) {
                ex.printStackTrace();
                md = -1;

            }
            DisplayMode s = null;
            if (md < 0 && l != null) {
                for (DisplayMode l1 : l) {
                    if (l1.getHeight() * l1.getWidth() < DM.getHeight() * DM.getWidth()) {
                        s = l1;
                        break;
                    }
                }
                if (s != null) {
                    for (DisplayMode l1 : l) {
                        if (l1.getHeight() * l1.getWidth() < DM.getHeight() * DM.getWidth() && l1.getHeight() * l1.getWidth() > s.getHeight() * s.getWidth()) {
                            s = l1;
                        }
                    }
                }
            }
            if (md > 0 && l != null) {
                for (DisplayMode l1 : l) {
                    if (l1.getHeight() * l1.getWidth() > DM.getHeight() * DM.getWidth()) {
                        s = l1;
                        break;
                    }
                }
                if (s != null) {
                    for (DisplayMode l1 : l) {
                        if (l1.getHeight() * l1.getWidth() > DM.getHeight() * DM.getWidth() && l1.getHeight() * l1.getWidth() < s.getHeight() * s.getWidth()) {
                            s = l1;
                        }
                    }
                }
            }
            if (s != null) {
                try {
                    Display.setDisplayMode(s);
                } catch (LWJGLException ex) {
                    ex.printStackTrace();
                    md = -1;
                    s = null;
                }
                if (s != null) {
                    DM = s;
                    System.out.println(DM);
                    setViewPort();
                    LSIZE = compLsize(DM);
                    GL11.glViewport(viewportX, viewportY, viewportW, viewportH);
                }
            }
            md = 1;
        }
    }

    private int compLsize(DisplayMode DM) {
        return (DM.getHeight() - 50) / 10;
    }

}
