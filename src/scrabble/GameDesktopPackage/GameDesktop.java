/*
 * GameDesktop.java
 *
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package scrabble.GameDesktopPackage;

import LetterPackage.LetterSet;
import LetterPackage.PlacedLetter;
import LetterPackage.SimpleLetter;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import org.lwjgl.opengl.GL11;
import scrabble.*;
import scrabble.Playerspackage.Player;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

/**
 *
 * @author Jirka
 */
public class GameDesktop {

    private SimpleLetter[][] pole;
    protected int width = -1;
    protected int height = -1;
    protected ArrayList<PlacedLetter> placedLetters;
    //protected PlacedLetter[] startfields;
    protected int cx = 0;
    protected int cy = 0;

    protected SimpleLetter selected;

    protected SimpleLetter redbox;
    protected SimpleLetter bluebox;
    protected SimpleLetter whitebox;
    private boolean kreslitBuffer = true;

    public static final int OUT_OF_RANGE = 1;
    public static final int PLACE_FULL = 2;
    public static final int LETTER_OK = 0;
    public static final int NO_LETTER = 3;

    public void setKreslitBuffer(boolean kreslitBuffer) {
        this.kreslitBuffer = kreslitBuffer;
    }

    public boolean isKreslitBuffer() {
        return kreslitBuffer;
    }

    public GameDesktop() {
    }

    public void delOnBuffer(Player hraje) {
        for (int x = 0; x < placedLetters.size(); x++) {
            if (placedLetters.get(x).getX() == cx && placedLetters.get(x).getY() == cy) {
                SimpleLetter a = placedLetters.get(x).getLetter();
                placedLetters.remove(x);
                hraje.returnToLetters(a);
                return;
            }
        }

    }

    public void setPlacedLetters(ArrayList<PlacedLetter> placedLetters) {
        this.placedLetters = placedLetters;
    }

    public ArrayList<PlacedLetter> getPlacedLetters() {
        return placedLetters;
    }

    public int getCy() {
        return cy;
    }

    public void setCx(int cx) {
        if (cx >= width) {
            return;
        }
        if (cx < 0) {
            return;
        }
        this.cx = cx;
    }

    public void setCy(int cy) {
        if (cy >= height) {
            return;
        }
        if (cy < 0) {
            return;
        }
        this.cy = cy;
    }

    public void setBluebox(SimpleLetter bluebox) {
        this.bluebox = bluebox;
    }

    public SimpleLetter getSelected() {
        return selected;
    }

    public void setSelected(SimpleLetter selected) {
        this.selected = selected;
    }

    public int getCx() {
        return cx;
    }

    public void moveCLeft() {
        setCx(this.cx - 1);
    }

    public void moveCup() {
        setCy(this.cy + 1);
    }

    public void moveCright() {
        setCx(this.cx + 1);
    }

    public void setRedbox(SimpleLetter redbox) {
        this.redbox = redbox;
    }

    public void moveCdown() {
        setCy(this.cy - 1);
    }

    public void addLeterToBuffer(int x, int y, SimpleLetter alfa) {
        placedLetters.add(new PlacedLetter(x, y, alfa));
    }

    public SimpleLetter PopLetter() {
        if (placedLetters.size() == 0) {
            return null;
        }
        PlacedLetter alfa = placedLetters.get(placedLetters.size() - 1);
        placedLetters.remove(placedLetters.size() - 1);
        //pole[alfa.getX()][alfa.getY()]=null;
        return alfa.getLetter();
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setPole(SimpleLetter[][] pole) {
        this.pole = pole;

    }

    public String writeBuffer() {
        String r = "";
        for (PlacedLetter elem : placedLetters) {
            r = r + (elem.getLetter().getType());
        }
        return r;
    }

    public SimpleLetter[][] getClonedPole() {
        SimpleLetter[][] nwpole = new SimpleLetter[width][height];
        for (int w = 0; w < getWidth(); w++) {
            System.arraycopy(pole[w], 0, nwpole[w], 0, getHeight());
        }

        return nwpole;
    }

    public SimpleLetter[][] getPole() {
        return pole;
    }

    public GameDesktop Clone() {
        return new GameDesktop(width, height);
    }

    /**
     * Creates a new instance of GameDesktop
     *
     * @return
     */
    public int addLetterToBuffer() {
        int a = setLetter(selected, true);

        if (a == LETTER_OK) {
            addLeterToBuffer(cx, cy, selected);
            setSelected(null);
        }
        return a;
    }

    protected int setLetter(SimpleLetter alfa, boolean test) {
        return setLetter(alfa, cx, cy, test);
    }

    public int setLetter(SimpleLetter alfa, int x, int y, boolean test) {
        if (alfa == null) {
            return NO_LETTER;
        }
        if (x < 0 || x >= width) {
            return OUT_OF_RANGE;
        }
        if (y < 0 || y >= height) {
            return OUT_OF_RANGE;
        }
        if (pole[x][y] != null) {
            return PLACE_FULL;
        }
        if (isAnyNewhere(x, y)) {
            return PLACE_FULL;
        }
        if (test) {
            return LETTER_OK;
        }
        pole[x][y] = alfa;

        return LETTER_OK;
    }

    public int setLetterDirect(SimpleLetter alfa, int x, int y, int z) {
        if (alfa == null) {
            return NO_LETTER;
        }
        if (x < 0 || x >= width) {
            return OUT_OF_RANGE;
        }
        if (y < 0 || y >= height) {
            return OUT_OF_RANGE;
        }

        pole[x][y] = alfa;

        return LETTER_OK;
    }

    public int setLetterDirect(SimpleLetter alfa, int x, int y) {
        if (alfa == null) {
            return NO_LETTER;
        }
        if (x < 0 || x >= width) {
            return OUT_OF_RANGE;
        }
        if (y < 0 || y >= height) {
            return OUT_OF_RANGE;
        }

        pole[x][y] = alfa;

        return LETTER_OK;
    }

    public int setLetterSuperDirect(SimpleLetter alfa, int x, int y, int z) {
        return setLetterSuperDirect(alfa, x, y);
    }

    public int setLetterSuperDirect(SimpleLetter alfa, int x, int y) {
        if (x < 0 || x >= width) {
            return OUT_OF_RANGE;
        }
        if (y < 0 || y >= height) {
            return OUT_OF_RANGE;
        }

        pole[x][y] = alfa;

        return LETTER_OK;
    }

    private void prepareLetter(int x, int y, int w, int h) {
        if (Main.style == 1) {
            GL11.glTranslatef(x - w, y - h, 0.12f);
            GL11.glScalef(0.5f, 0.5f, 0.1f);
        }
        if (Main.style == 2) {
            GL11.glTranslatef(x - w, y - h, 0.52f);
            GL11.glScalef(0.5f, 0.5f, 0.5f);
        }
    }

    public void draw() {
        int w = width / 2;
        int h = height / 2;
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (pole[x][y] != null) {
                    GL11.glPushMatrix();

                    prepareLetter(x, y, w, h);
                    GL11.glCallList(pole[x][y].getList());
                    GL11.glPopMatrix();
                }
            }
        }
        if (isKreslitBuffer()) {
            for (int s = 0; s < placedLetters.size(); s++) {
                if (placedLetters.get(s) != null) {
                    GL11.glPushMatrix();
                    /*GL11.glTranslatef(placedLetters.get(s).getX()-w,placedLetters.get(s).getY()-h,0.12f);
                     GL11.glScalef(0.5f,0.5f,0.1f);*/
                    prepareLetter(placedLetters.get(s).getX(), placedLetters.get(s).getY(), w, h);
                    GL11.glCallList(placedLetters.get(s).getLetter().getList());
                    GL11.glPopMatrix();
                }
            }
        }
        if (selected != null) {
            GL11.glPushMatrix();
            //GL11.glTranslatef(cx-w,cy-h,0.12f);
            //GL11.glScalef(0.5f,0.5f,0.1f);
            prepareLetter(cx, cy, w, h);
            GL11.glCallList(selected.getList());
            GL11.glPopMatrix();
        }

    }

    public void drawCursor() {
        int w = width / 2;
        int h = height / 2;
        GL11.glPushMatrix();
        if (Main.style == 1) {
            GL11.glTranslatef(cx - w, cy - h, 0.12f);
            GL11.glScalef(0.6f, 0.6f, 0.15f);
        }
        if (Main.style == 2) {
            GL11.glTranslatef(cx - w, cy - h, 0.50f);
            GL11.glScalef(0.6f, 0.6f, 0.6f);
        }
        if (selected != null) {
            /*GL11.glPushMatrix();
             GL11.glTranslatef(cx-w,cy-h,0.12f);
             GL11.glScalef(0.5f,0.5f,0.1f);
             GL11.glCallList(selected.getList());
             GL11.glPopMatrix();*/

            //GL11.glColor4f(1f,1f,1f,1.5f); 
            //GL11.glBlendFunc( GL11.GL_SRC_ALPHA,GL11.GL_ONE_MINUS_SRC_ALPHA);
            if (pole[cx][cy] == null && !isAnyNewhere(cx, cy)) {
                if (bluebox != null) {

                    GL11.glCallList(bluebox.getList());

                }
            } else {
                if (redbox != null) {

                    GL11.glCallList(redbox.getList());

                }
            }
        } else if (whitebox != null) {

            GL11.glCallList(whitebox.getList());

        }
        GL11.glPopMatrix();

    }

    public void setWhiteebox(SimpleLetter simpleLetter) {
        this.whitebox = simpleLetter;
    }

    public GameDesktop(int width, int height) {
        this.width = width;
        this.height = height;
        pole = new SimpleLetter[width][height];
        placedLetters = new ArrayList<PlacedLetter>();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                pole[x][y] = null;
            }
        }
    }

    public void moveLettersFromBufferToDesk() {
        for (int x = 0; x < placedLetters.size(); x++) {
            pole[placedLetters.get(x).getX()][placedLetters.get(x).getY()] = placedLetters.get(x).getLetter();
        }
        placedLetters.clear();
    }

    public SimpleLetter getPolePrvek(int x, int y) {
        if (x < 0 || x >= width) {
            return null;
        }
        if (y < 0 || y >= height) {
            return null;
        }
        return pole[x][y];

    }

    public boolean isAnyNewhere(int x, int y) {
        for (int q = 0; q < placedLetters.size(); q++) {
            if (placedLetters.get(q).getX() == x && placedLetters.get(q).getY() == y) {
                return true;
            }
        }
        return false;
    }

    public SimpleLetter getLetterOnBoardIndependentOnBuffer(int x, int y) {
        if (getPolePrvek(x, y) != null) {
            return getPolePrvek(x, y);
        }
        if (isAnyNewhere(x, y)) {
            for (int q = 0; q < placedLetters.size(); q++) {
                if (placedLetters.get(q).getX() == x && placedLetters.get(q).getY() == y) {
                    return placedLetters.get(q).getLetter();
                }
            }

        };
        return null;
    }

    public PlacedLetter getAnyNewhere(int x, int y) {
        for (int q = 0; q < placedLetters.size(); q++) {
            if (placedLetters.get(q).getX() == x && placedLetters.get(q).getY() == y) {
                return placedLetters.get(q);
            }
        }
        return null;
    }

    public static final PlacedLetter getAnyNewhereInVector(ArrayList<PlacedLetter> vector, int x, int y) {
        for (int q = 0; q < vector.size(); q++) {
            if (vector.get(q).getX() == x && vector.get(q).getY() == y) {
                return vector.get(q);
            }
        }
        return null;
    }

    public String toString() {
        String vysledek = "\n";
        for (int j = height - 1; j >= 0; j--) {
            String s = "";

            for (int i = 0; i < width; i++) {
                if (getPolePrvek(i, j) == null) {
                    s += " @ ";
                } else {
                    s += " " + getPolePrvek(i, j).getType() + " ";
                }
            }
            if (j < 10) {
                vysledek = vysledek + "0" + (j) + " " + s + "\n";
            } else {
                vysledek = vysledek + (j) + " " + s + "\n";
            }
        }
        String s = "   ";
        for (int j = 0; j < width; j++) {
            if (j < 10) {
                s = s + "  0" + j;
            } else {
                s = s + " " + j;
            }
        }
        vysledek = vysledek + s;

        return vysledek;
    }

    public void writeDesktop(BufferedWriter write) throws IOException {
        write.write("<gamedesktop>\r\n");
        write.write("<field w='" + width + "' h='" + height + "'>\r\n");
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (pole[i][j] != null) {
                    write.write("<letter x='" + i + "' y='" + j + "'>");
                    write.write(pole[i][j].getType());
                    write.write("</letter>\r\n");
                }
            }
        }
        write.write("</field>\r\n");

        write.write("<placedletters>\r\n");
        for (Iterator it = placedLetters.iterator(); it.hasNext();) {
            PlacedLetter elem = (PlacedLetter) it.next();

            write.write("<placedletter x='" + elem.getX() + "' y='" + elem.getY() + "'>");
            write.write(elem.getLetter().getType());
            write.write("</placedletter>\r\n");

        }

        write.write("</placedletters>\r\n");
        if (selected != null) {
            write.write("<selected>\r\n");
            write.write(selected.getType());
            write.write("</selected>\r\n");
        }
        write.write("<cursor>\r\n");
        write.write("<cx>\r\n");
        write.write(String.valueOf(cx));
        write.write("</cx>\r\n");
        write.write("<cy>\r\n");
        write.write(String.valueOf(cy));
        write.write("</cy>\r\n");
        write.write("</cursor>\r\n");

        write.write("</gamedesktop>\r\n");
    }

    public void saveDesktop(File f) {
        BufferedWriter write = null;
        try {
            write = new BufferedWriter(new FileWriter(f));
            writeDesktop(write);

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

    public void Update(File f, LetterSet l) {
        XmlDesktopUpdater handler = null;;
        XMLReader xr;
        try {

            xr = XMLReaderFactory.createXMLReader();
            handler = new XmlDesktopUpdater(l);
            xr.setContentHandler(handler);
            xr.setErrorHandler(handler);

            FileReader r = new FileReader(f);
            System.out.println(f);
            handler.setLoadedRD(this);
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

        //return handler.getLoadedData();
    }

    public void setPolePrvek(int x, int y, SimpleLetter object) {
        pole[x][y] = object;
    }

    public SimpleLetter getPolePrvek(int x, int y, int z) {
        return getPolePrvek(x, y);
    }

    public void clean() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                pole[x][y] = null;

            }

        }
    }

}
