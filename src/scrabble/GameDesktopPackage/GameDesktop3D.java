/*
 * GameDesktop3D.java
 *
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package scrabble.GameDesktopPackage;

import LetterPackage.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import org.lwjgl.opengl.GL11;
import scrabble.Main;
import scrabble.Playerspackage.Player;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

/**
 *
 * @author Jirka
 */
public class GameDesktop3D extends GameDesktop {

    private ArrayList<SimpleLetter[][]> pole;
    private int cz;
    private int levels;
    private float movement = 2;

    private float compTranslate(int patro) {
        int xlevels = levels / 2;

        return (-(float) xlevels * movement) + (float) (patro + 1) * movement + 0.12f;
    }

    /**
     * Creates a new instance of GameDesktop3D
     */
    public GameDesktop3D() {
    }

    public int getCz() {
        return cz;
    }

    public void setCz(int cz) {
        if (cz < 0) {
            return;
        }
        if (cz >= levels) {
            return;
        }

        this.cz = cz;
    }

    public void setMovement(float movement) {
        this.movement = movement;
    }

    public int getLevels() {
        return levels;
    }

    public void setLevels(int levels) {
        this.levels = levels;
    }

    public void moveCzUp() {
        setCz(cz + 1);

    }

    public void moveCzDown() {
        setCz(cz - 1);
    }

    @Override
    public void delOnBuffer(Player hraje) {
        for (int x = 0; x < placedLetters.size(); x++) {
            if (placedLetters.get(x).getX() == cx && placedLetters.get(x).getY() == cy & placedLetters.get(x).getZ() == cz) {
                SimpleLetter a = placedLetters.get(x).getLetter();
                placedLetters.remove(x);
                hraje.returnToLetters(a);
                return;
            }
        }
    }

    /*
     * @deprecated  
     */
    @Override
    public void addLeterToBuffer(int x, int y, SimpleLetter alfa) {

    }

    public void addLeterToBuffer(int x, int y, int z, SimpleLetter alfa) {
        placedLetters.add(new PlacedLetter(x, y, z, alfa));
    }

    /*
     * @deprecated  
     */
    @Override
    public void setPole(SimpleLetter[][] pole) {

    }

    public void setPole(ArrayList<SimpleLetter[][]> pole) {
        this.pole = pole;

    }

    /*
     * @deprecated
     */
    public SimpleLetter[][] getClonedPole() {
        return null;
    }

    public ArrayList<SimpleLetter[][]> getClonedPole3D() {
        ArrayList<SimpleLetter[][]> vysledek = new ArrayList();

        for (SimpleLetter[][] elem : pole) {
            SimpleLetter[][] nwpole = new SimpleLetter[width][height];
            for (int w = 0; w < getWidth(); w++) {
                System.arraycopy(elem[w], 0, nwpole[w], 0, getHeight());
            }
            vysledek.add(nwpole);
        }
        return vysledek;
    }

    /*
     * @deprecated
     */
    @Override
    public SimpleLetter[][] getPole() {
        return null;
    }

    public ArrayList<SimpleLetter[][]> getPole3D() {
        return pole;
    }

    @Override
    public GameDesktop Clone() {
        return new GameDesktop(width, height);
    }

    @Override
    public int addLetterToBuffer() {
        int a = setLetter(selected, true);

        if (a == LETTER_OK) {
            addLeterToBuffer(cx, cy, cz, selected);
            setSelected(null);
        }
        return a;
    }

    @Override
    protected int setLetter(SimpleLetter alfa, boolean test) {
        return setLetter(alfa, cx, cy, cz, test);
    }

    /*
     * @deprecated
     */
    @Override
    public int setLetter(SimpleLetter alfa, int x, int y, boolean test) {
        return -1;
    }

    public int setLetter(SimpleLetter alfa, int x, int y, int z, boolean test) {
        if (alfa == null) {
            return NO_LETTER;
        }
        if (x < 0 || x >= width) {
            return OUT_OF_RANGE;
        }
        if (y < 0 || y >= height) {
            return OUT_OF_RANGE;
        }
        if (z < 0 || z >= levels) {
            return OUT_OF_RANGE;
        }
        if (pole.get(z)[x][y] != null) {
            return PLACE_FULL;
        }
        if (isAnyNewhere(x, y, z)) {
            return PLACE_FULL;
        }
        if (test) {
            return LETTER_OK;
        }
        pole.get(z)[x][y] = alfa;

        return LETTER_OK;
    }

    /*
     * @deprecated
     */
    @Override
    public int setLetterDirect(SimpleLetter alfa, int x, int y) {
        return -1;
    }

    @Override
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
        if (z < 0 || z >= levels) {
            return OUT_OF_RANGE;
        }

        pole.get(z)[x][y] = alfa;

        return LETTER_OK;
    }

    /*
     * @deprecated
     */
    @Override
    public int setLetterSuperDirect(SimpleLetter alfa, int x, int y) {
        return -1;
    }

    @Override
    public int setLetterSuperDirect(SimpleLetter alfa, int x, int y, int z) {
        if (x < 0 || x >= width) {
            return OUT_OF_RANGE;
        }
        if (y < 0 || y >= height) {
            return OUT_OF_RANGE;
        }
        if (z < 0 || z >= levels) {
            return OUT_OF_RANGE;
        }

        pole.get(z)[x][y] = alfa;

        return LETTER_OK;
    }

    private void prepareLetter(int x, int y, float z, int w, int h) {
        if (Main.style == 1) {
            GL11.glTranslatef(x - w, y - h, z);
            GL11.glScalef(0.5f, 0.5f, 0.1f);
        }
        if (Main.style == 2) {
            GL11.glTranslatef(x - w, y - h, z + 0.40f);
            GL11.glScalef(0.5f, 0.5f, 0.5f);
        }
    }

    @Override
    public void draw() {
        int w = width / 2;
        int h = height / 2;
        int xlevels = levels / 2;

        for (int l = 0; l < pole.size(); l++) {
            SimpleLetter[][] elem = (SimpleLetter[][]) pole.get(l);
            float z = compTranslate(l);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    if (elem[x][y] != null) {
                        GL11.glPushMatrix();
                        //GL11.glTranslatef(x-w,y-h,z);

                        prepareLetter(x, y, z, w, h);
                        GL11.glCallList(elem[x][y].getList());
                        GL11.glPopMatrix();
                    }
                }
            }
        }
        if (isKreslitBuffer()) {
            for (PlacedLetter placedLetter : placedLetters) {
                if (placedLetter != null) {
                    float z = compTranslate(placedLetter.getZ());
                    GL11.glPushMatrix();
                    /*GL11.glTranslatef(placedLetters.get(s).getX()-w,placedLetters.get(s).getY()-h,z);
                     GL11.glScalef(0.5f,0.5f,0.1f);
                     */
                    prepareLetter(placedLetter.getX(), placedLetter.getY(), z, w, h);
                    GL11.glCallList(placedLetter.getLetter().getList());
                    GL11.glPopMatrix();
                }
            }
        }
        if (selected != null) {
            float z = compTranslate(cz);
            GL11.glPushMatrix();
            /*GL11.glTranslatef(cx-w,cy-h,z);
             GL11.glScalef(0.5f,0.5f,0.1f);*/
            prepareLetter(cx, cy, z, w, h);
            GL11.glCallList(selected.getList());
            GL11.glPopMatrix();
        }

    }

    @Override
    public void drawCursor() {
        int w = width / 2;
        int h = height / 2;
        int xlevels = levels / 2;
        //if (levels%2==0)xlevels++;
        float z = compTranslate(cz);

        GL11.glPushMatrix();
        if (selected == null) {
            /*GL11.glTranslatef(cx-w,cy-h,z);
             GL11.glScalef(0.6f,0.6f,0.15f);*/
            if (Main.style == 1) {
                GL11.glTranslatef(cx - w, cy - h, z);
                GL11.glScalef(0.6f, 0.6f, 0.15f);
            }
            if (Main.style == 2) {
                GL11.glTranslatef(cx - w, cy - h, z + 0.38f);
                GL11.glScalef(0.6f, 0.6f, 0.6f);
            }
        } else {

            GL11.glTranslatef(cx - w, cy - h, -compTranslate(0) + 0.15f);
            //cube je 2x2*2
            //GL11.glScalef(0.6f,0.6f,0.15f);
            GL11.glScalef(0.6f, 0.6f, compTranslate(levels - 1) + 0.15f);
        }

        if (selected != null) {
            if (pole.get(cz)[cx][cy] == null && !isAnyNewhere(cx, cy, cz)) {
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

    public GameDesktop3D(int width, int height, int levels) {
        this.width = width;
        this.height = height;
        this.levels = levels;
        this.pole = new ArrayList();
        for (int l = 0; l < levels; l++) {
            SimpleLetter[][] npole = new SimpleLetter[width][height];
            placedLetters = new ArrayList<PlacedLetter>();
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    npole[x][y] = null;
                }
            }
            pole.add(npole);
        }
    }

    @Override
    public void moveLettersFromBufferToDesk() {
        for (PlacedLetter placedLetter : placedLetters) {
            pole.get(placedLetter.getZ())[placedLetter.getX()][placedLetter.getY()] = placedLetter.getLetter();
        }
        placedLetters.clear();
    }

    /*
     * @deprecated
     */
    @Override
    public SimpleLetter getPolePrvek(int x, int y) {
        return null;
    }

    @Override
    public SimpleLetter getPolePrvek(int x, int y, int z) {
        if (x < 0 || x >= width) {
            return null;
        }
        if (y < 0 || y >= height) {
            return null;
        }
        if (z < 0 || z >= levels) {
            return null;
        }
        return pole.get(z)[x][y];

    }

    /*
     * @deprecated
     */
    @Override
    public boolean isAnyNewhere(int x, int y) {
        return false;
    }

    public boolean isAnyNewhere(int x, int y, int z) {
        for (PlacedLetter placedLetter : placedLetters) {
            if (placedLetter.getX() == x && placedLetter.getY() == y && placedLetter.getZ() == z) {
                return true;
            }
        }
        return false;
    }

    /*
     * @deprecated
     */
    @Override
    public SimpleLetter getLetterOnBoardIndependentOnBuffer(int x, int y) {
        return null;
    }

    public SimpleLetter getLetterOnBoardIndependentOnBuffer(int x, int y, int z) {
        if (getPolePrvek(x, y, z) != null) {
            return getPolePrvek(x, y, z);
        }
        if (isAnyNewhere(x, y, z)) {
            for (PlacedLetter placedLetter : placedLetters) {
                if (placedLetter.getX() == x && placedLetter.getY() == y && placedLetter.getZ() == z) {
                    return placedLetter.getLetter();
                }
            }

        }
        return null;
    }

    /*
     * @deprecated
     */
    @Override
    public PlacedLetter getAnyNewhere(int x, int y) {
        return null;
    }

    public PlacedLetter getAnyNewhere(int x, int y, int z) {
        for (PlacedLetter placedLetter : placedLetters) {
            if (placedLetter.getX() == x && placedLetter.getY() == y && placedLetter.getZ() == z) {
                return placedLetter;
            }
        }
        return null;
    }

    public static final PlacedLetter getAnyNewhereInVector(ArrayList<PlacedLetter> vector, int x, int y, int z) {
        for (PlacedLetter vector1 : vector) {
            if (vector1.getX() == x && vector1.getY() == y && vector1.getZ() == z) {
                return vector1;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        String vysledek = "\n";
        for (int z = 0; z < levels; z++) {

            for (int j = height - 1; j >= 0; j--) {
                String s = "";

                for (int i = 0; i < width; i++) {
                    if (getPolePrvek(i, j, z) == null) {
                        s += " @ ";
                    } else {
                        s += " " + getPolePrvek(i, j, z).getType() + " ";
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
            vysledek = vysledek + "\n*****************************\n";
        }
        return vysledek;
    }

    @Override
    public void writeDesktop(BufferedWriter write) throws IOException {

        write.write("<gamedesktop>\r\n");
        write.write("<field w='" + width + "' h='" + height + "' l='" + levels + "' >\r\n");
        for (int z = 0; z < levels; z++) {
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    if (pole.get(z)[i][j] != null) {
                        write.write("<letter x='" + i + "' y='" + j + "' z='" + z + "'>");
                        write.write(pole.get(z)[i][j].getType());
                        write.write("</letter>\r\n");
                    }
                }
            }
        }
        write.write("</field>\r\n");

        write.write("<placedletters>\r\n");
        for (PlacedLetter elem : placedLetters) {
            write.write("<placedletter x='" + elem.getX() + "' y='" + elem.getY() + "' z='" + elem.getZ() + "'>");
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
        write.write("<cz>\r\n");
        write.write(String.valueOf(cz));
        write.write("</cz>\r\n");
        write.write("</cursor>\r\n");
        write.write("<levels>\r\n");
        write.write(String.valueOf(levels));
        write.write("</levels>\r\n");
        write.write("<movement>\r\n");
        write.write(String.valueOf(movement));
        write.write("</movement>\r\n");

        write.write("</gamedesktop>\r\n");
    }

    @Override
    public void Update(File f, LetterSet l) {
        XmlDesktopUpdater3D handler = null;;
        XMLReader xr;
        try {

            xr = XMLReaderFactory.createXMLReader();
            handler = new XmlDesktopUpdater3D(l);
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

    @Deprecated
    @Override
    public void setPolePrvek(int x, int y, SimpleLetter object) {

    }

    public void setPolePrvek(int x, int y, int z, SimpleLetter object) {
        pole.get(z)[x][y] = object;

    }

    @Override
    public void clean() {

        for (SimpleLetter[][] elem : pole) {
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    elem[x][y] = null;

                }
            }
        }
    }

}
