/*
 * LetterSet.java
 *
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package LetterPackage;

import OGLbasic.GLApp;
import OGLbasic.GLImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import org.lwjgl.opengl.GL11;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

/**
 *
 * @author Jirka
 */
public class LetterSet {

    private int bgtexture;
    private ArrayList<Integer> lettersTextures;
    private ArrayList<Letter> letters;
    private ArrayList<Letter> lettersBackup;
    private ArrayList<SimpleLetter> lettersInPacket;
    private final int redboxtexture;
    private final int redboxlist;
    private final SimpleLetter redboxletter;

    private final int blueboxtexture;
    private final int blueboxlist;
    private final SimpleLetter blueboxletter;

    private final int whiteboxtexture;
    private final int whiteboxlist;
    private final SimpleLetter whiteboxletter;

    public int getLettersInPacketSize() {
        return lettersInPacket.size();
    }

    public ArrayList<SimpleLetter> getLettersInPacket() {
        return lettersInPacket;
    }

    public void setLettersInPacket(ArrayList<SimpleLetter> lettersInPacket) {
        this.lettersInPacket = lettersInPacket;
    }

    public int getLetterCost(String a) {
        for (Letter letter : letters) {
            if (letter.getType().equalsIgnoreCase(a)) {
                return letter.getValue();
            }
        }
        return 0;
    }

    public SimpleLetter getRedboxletter() {
        return redboxletter;
    }

    public SimpleLetter getWhiteboxletter() {
        return whiteboxletter;
    }

    public int getBgtexture() {
        return bgtexture;
    }

    public Letter getLetter(int index) {
        if (index < 0 || index >= letters.size()) {
            return null;
        }
        return letters.get(index);
    }

    /**
     * Creates a new instance of LetterSet
     * @return 
     */
    public SimpleLetter getRandomLetterFromPocket() {
        if (lettersInPacket.isEmpty()) {
            return null;
        }
        int r = new Random().nextInt(lettersInPacket.size());
        SimpleLetter navrat = lettersInPacket.get(r);
        lettersInPacket.remove(r);
        return navrat;
    }

    public SimpleLetter getBlueboxletter() {
        return blueboxletter;
    }

    public LetterSet(File src) {
        lettersTextures = new ArrayList<Integer>();
        try {

            BufferedReader read = new BufferedReader(new FileReader(src));
            // read from the file.
            String s = read.readLine();
            s = s.replace((CharSequence) "*", (CharSequence) File.separator);
            GLImage textureImg = GLApp.loadImage("Data" + File.separator + "textures" + File.separator + "letters" + File.separator + s);
            int texturepointer = GLApp.makeTexture(textureImg);
            GLApp.makeTextureMipMap(textureImg);
            this.bgtexture = texturepointer;

            letters = new ArrayList<Letter>();
            lettersBackup = new ArrayList<Letter>();
            s = read.readLine();
            while (s != null) {
                s = s.replace((CharSequence) "*", (CharSequence) File.separator);
                String[] ss = s.split(" ");

                textureImg = GLApp.loadImage("Data" + File.separator + "textures" + File.separator + "letters" + File.separator + ss[3]);
                texturepointer = GLApp.makeTexture(textureImg);
                GLApp.makeTextureMipMap(textureImg);
                lettersTextures.add(texturepointer);
                int listBase = GL11.glGenLists(1);
                GL11.glNewList(listBase, GL11.GL_COMPILE);
                GLApp.renderDiffCube(texturepointer, bgtexture, bgtexture, bgtexture, bgtexture, bgtexture);
                GL11.glEndList();
                Letter l = new Letter(ss[0], Integer.parseInt(ss[1]), ss[2], listBase, texturepointer);
                letters.add(l);
                lettersBackup.add(l);

                s = read.readLine();
            }
        } catch (IOException iox) {
            // I/O error: could not open file, or lost connection, or...
            iox.printStackTrace();
        }
        lettersInPacket = new ArrayList<SimpleLetter>();
        for (Letter letter : letters) {
            for (int y = 1; y <= Integer.parseInt(letter.getQuantity()); y++) {
                lettersInPacket.add(new SimpleLetter(letter.getType(), letter.getValue(), letter.getList(), letter.getTexture()));
            }
        }

        ///red box
        GLImage textureImg = GLApp.loadImage("Data" + File.separator + "textures" + File.separator + "redglass.png");
        int texturepointer = GLApp.makeTexture(textureImg);
        GLApp.makeTextureMipMap(textureImg);
        lettersTextures.add(texturepointer);
        int listBase = GL11.glGenLists(1);
        GL11.glNewList(listBase, GL11.GL_COMPILE);
        GLApp.renderDiffCube(texturepointer, texturepointer, texturepointer, texturepointer, texturepointer, texturepointer);
        GL11.glEndList();
        this.redboxlist = listBase;
        this.redboxtexture = texturepointer;
        this.redboxletter = new SimpleLetter("redbox", 0, listBase, texturepointer);

        ///blue box
        textureImg = GLApp.loadImage("Data" + File.separator + "textures" + File.separator + "blueglass.png");
        texturepointer = GLApp.makeTexture(textureImg);
        GLApp.makeTextureMipMap(textureImg);
        lettersTextures.add(texturepointer);
        listBase = GL11.glGenLists(1);
        GL11.glNewList(listBase, GL11.GL_COMPILE);
        GLApp.renderDiffCube(texturepointer, texturepointer, texturepointer, texturepointer, texturepointer, texturepointer);
        GL11.glEndList();
        this.blueboxlist = listBase;
        this.blueboxtexture = texturepointer;
        this.blueboxletter = new SimpleLetter("bluebox", 0, listBase, texturepointer);

        ///white box
        textureImg = GLApp.loadImage("Data" + File.separator + "textures" + File.separator + "whiteglass.png");
        texturepointer = GLApp.makeTexture(textureImg);
        GLApp.makeTextureMipMap(textureImg);
        lettersTextures.add(texturepointer);
        listBase = GL11.glGenLists(1);
        GL11.glNewList(listBase, GL11.GL_COMPILE);
        GLApp.renderDiffCube(texturepointer, texturepointer, texturepointer, texturepointer, texturepointer, texturepointer);
        GL11.glEndList();
        this.whiteboxlist = listBase;
        this.whiteboxtexture = texturepointer;
        this.whiteboxletter = new SimpleLetter("bluebox", 0, listBase, texturepointer);

    }

    public void addLetterToPocket(SimpleLetter alfa) {
        if (alfa == null) {
            return;
        }
        lettersInPacket.add(alfa);
    }

    public boolean haveLetter(String letter) {
        for (Letter letter1 : letters) {
            if (letter1.getType().equalsIgnoreCase(letter)) {
                return true;
            }
        }
        return false;
    }

    public Object[] getAlphabetWithoutJokers() {
        Object[] a = null;
        int m = 0;
        if (haveLetter("%")) {
            a = new Object[letters.size() - 1];
        } else {
            a = new Object[letters.size()];
        }

        for (int i = 0; i < letters.size(); i++) {
            if (!letters.get(i).getType().equalsIgnoreCase("%")) {
                a[i - m] = letters.get(i).getType();

            } else {
                m++;
            }
        }

        return a;
    }

    public SimpleLetter createLetter(String type) {
        for (Letter letter : letters) {
            if (letter.getType().equalsIgnoreCase(type)) {
                return new SimpleLetter(letter.getType(), letter.getValue(), letter.getList(), letter.getTexture());
            }
        }
        return null;
    }

    public ArrayList<SimpleLetter> getSimpleLetters(Object[] alphabetStrings) {
        ArrayList<SimpleLetter> vysledek = new ArrayList();
        for (Object alphabetString : alphabetStrings) {
            SimpleLetter a = createLetter((String) alphabetString);
            if (a != null) {
                vysledek.add(a);
            }
        }
        return vysledek;
    }

    public void savePocket(File f) {
        BufferedWriter write = null;
        try {

            write = new BufferedWriter(new FileWriter(f));
            write.write("<packet>\r\n");
            for (SimpleLetter elem : lettersInPacket) {
                write.write("<letter>");
                write.write(elem.getType());
                write.write("</letter>");
            }
            write.write("</packet>\r\n");
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

    public void Update(File f) {
        XmlPacketUpdater handler = null;;
        XMLReader xr;
        try {

            xr = XMLReaderFactory.createXMLReader();
            handler = new XmlPacketUpdater();
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

    public PlacedLetter parsePalcedLetter(String c) throws Exception {
        //A[5, 4, 3]
        c = c.replaceAll("[(]", ",");
        c = c.replaceAll("[)]", ",");
        String[] cc = c.split(",");
        return new PlacedLetter(
                Integer.parseInt(cc[1].trim()),
                Integer.parseInt(cc[2].trim()),
                Integer.parseInt(cc[3].trim()),
                createLetter(cc[0].trim())
        );

    }

    public void replaceLettersList(int style) {
        for (Letter elem : lettersBackup) {

            switch (style) {
                case 1:
                    GL11.glDeleteLists(elem.getList(), 1);
                    int listBase = GL11.glGenLists(1);
                    GL11.glNewList(listBase, GL11.GL_COMPILE);
                    GLApp.renderDiffCube(elem.getTexture(), bgtexture, bgtexture, bgtexture, bgtexture, bgtexture);
                    GL11.glEndList();
                    elem.setList(listBase);
                    break;
                case 2:
                    GL11.glDeleteLists(elem.getList(), 1);
                    listBase = GL11.glGenLists(1);
                    GL11.glNewList(listBase, GL11.GL_COMPILE);
                    GLApp.renderDiffCube(elem.getTexture());
                    GL11.glEndList();
                    elem.setList(listBase);
                    break;
            }
        }
    }
}
