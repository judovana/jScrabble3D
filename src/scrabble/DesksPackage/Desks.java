/*
 * desks.java
 *
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package scrabble.DesksPackage;

import LetterPackage.PlacedLetter;
import OGLbasic.GLApp;
import OGLbasic.GLImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import scrabble.GameDesktopPackage.GameDesktop;

/**
 *
 * @author Jirka
 */
public class Desks {

    private int pole[][];
    protected int width = 0;
    protected int height = 0;
    protected float tilesize;
    public ArrayList<PlacedLetter> startFields = new ArrayList();

    // Handles for textures
    protected int fieldsTextures[] = new int[6];
    protected int anotherTextures[] = new int[1];

    private int DESKA;

    private int stx;
    private int sty;
    private int stz;

    public int getStartz() {
        return stz;
    }

    public int getStartx() {
        return stx;
    }

    public int getStarty() {
        return sty;
    }

    public float getTilesize() {
        return tilesize;
    }

    public int[][] getPole() {
        return pole;
    }

    public int getTile(int x, int y) {
        return pole[x][y];
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Desks() {

    }

    /**
     * Creates a new instance of desks
     */

    protected void nactiTextury() {
        for (int x = 0; x < 6; x++) {
            GLImage textureImg = GLApp.loadImage("Data" + File.separator + "textures" + File.separator + String.valueOf(x) + ".png");
            fieldsTextures[x] = GLApp.makeTexture(textureImg);
            GLApp.makeTextureMipMap(textureImg);
        }
        GLImage textureImg = GLApp.loadImage("Data" + File.separator + "textures" + File.separator + "w1.png");
        anotherTextures[0] = GLApp.makeTexture(textureImg);
        GLApp.makeTextureMipMap(textureImg);
    }

    public Desks(File src, float tilesize) {
        this.tilesize = tilesize;

        //loading textures   
        nactiTextury();

        try {

            BufferedReader read = new BufferedReader(new FileReader(src));
            // read from the file.
            String s = read.readLine();
            width = Integer.parseInt(s.substring(0, s.indexOf("x")));
            height = Integer.parseInt(s.substring(s.indexOf("x") + 1, s.length()));
            pole = new int[width][height];
            nactiDesku(pole, read, 0);
            read.close();
        } catch (IOException iox) {
            // I/O error: could not open file, or lost connection, or...
            iox.printStackTrace();
        }

        //creating list
        DESKA = makeList(pole);
    }

    public void draw() {
        GL11.glCallList(DESKA);
    }

    protected int makeList(int[][] pole) {
        int DESKA = GL11.glGenLists(1);
        final int WIDTH = this.getWidth();
        final int HEIGHT = this.getHeight();
        final float TILE = this.getTilesize();

        GL11.glNewList(DESKA, GL11.GL_COMPILE);
        GL11.glDisable(GL11.GL_CULL_FACE);
        float left = (-(float) WIDTH / 2f) * TILE;
        float top = (-(float) HEIGHT / 2f) * TILE;
        float z = 0;
        for (int y = 0; y < HEIGHT; y++) {

            for (int x = 0; x < WIDTH; x++) {
                GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
                GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
                GL11.glBindTexture(GL11.GL_TEXTURE_2D, fieldsTextures[/*this.getTile(x,y)*/pole[x][y]]);
                GL11.glBegin(GL11.GL_QUADS);

                GL11.glTexCoord2d(0.0, 0.0);
                GL11.glVertex3d(left + x * TILE, top + y * TILE, z);
                GL11.glTexCoord2d(0.0, 1.0);
                GL11.glVertex3d(left + x * TILE, top + y * TILE + TILE, z);
                GL11.glTexCoord2d(1.0, 1.0);
                GL11.glVertex3d(left + x * TILE + TILE, top + y * TILE + TILE, z);
                GL11.glTexCoord2d(1.0, 0.0);
                GL11.glVertex3d(left + x * TILE + TILE, top + y * TILE, z);

                GL11.glEnd();
            }

        }
        GL11.glEnable(GL11.GL_CULL_FACE);

//okraje
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, anotherTextures[0]);

        for (int x = 0; x < WIDTH; x++) {
            GL11.glPushMatrix();
            GL11.glTranslatef(left + TILE / 2 + (float) x, top, 0);
            GL11.glScalef(0.5f, 0.1f, 0.5f);
            GLApp.renderSameCube();
            GL11.glPopMatrix();
        }

        for (int x = 0; x < WIDTH; x++) {
            GL11.glPushMatrix();
            GL11.glTranslatef(left + TILE / 2 + (float) x, -top, 0);
            GL11.glScalef(0.5f, 0.1f, 0.5f);
            GLApp.renderSameCube();
            GL11.glPopMatrix();
        }

        for (int x = 0; x < HEIGHT; x++) {
            GL11.glPushMatrix();
            GL11.glTranslatef(left, top + TILE / 2 + (float) x, 0);
            GL11.glScalef(0.1f, 0.5f, 0.5f);
            GLApp.renderSameCube();
            GL11.glPopMatrix();
        }

        for (int x = 0; x < HEIGHT; x++) {
            GL11.glPushMatrix();
            GL11.glTranslatef(-left, top + TILE / 2 + (float) x, 0);
            GL11.glScalef(0.1f, 0.5f, 0.5f);
            GLApp.renderSameCube();
            GL11.glPopMatrix();
        }
        GL11.glEndList();

        return DESKA;
    }

    public int getLetterKoeficientAt(int x, int y) {
        switch (pole[x][y]) {

            case (0):
                return 1;

            case (1):
                return 1;

            case (2):
                return 2;

            case (3):
                return 3;

            case (4):
                return 1;

            case (5):
                return 1;

            default:
                return 1;

        }
    }

    public int getWordKoeficientAt(int x, int y, GameDesktop turn) {
        switch (pole[x][y]) {

            case (0):
                return 1;

            case (1):
                if (turn.getPolePrvek(x, y) == null) {
                    return 2;
                } else {
                    return 1;
                }

            case (2):
                return 1;

            case (3):
                return 1;

            case (4):
                return 2;

            case (5):
                return 3;

            default:
                return 1;

        }
    }

    protected void nactiDesku(int[][] pole, BufferedReader read, int z) throws IOException {
        for (int x = 0; x < width; x++) {
            String s = read.readLine();
            for (int y = 0; y < height; y++) {
                if (s.charAt(y) == ' ') {
                    pole[x][y] = 0; //grass
                }
                if (s.charAt(y) == 's') {
                    pole[x][y] = 1;
                    stx = x;
                    sty = y;
                    stz = z;
                    startFields.add(new PlacedLetter(x, y, z, null));
                };  //start
                if (s.charAt(y) == 'x') {
                    pole[x][y] = 2; //double pismenko
                }
                if (s.charAt(y) == 'y') {
                    pole[x][y] = 3;  //triple pismenko
                }
                if (s.charAt(y) == 'd') {
                    pole[x][y] = 4;  //double word
                }
                if (s.charAt(y) == 't') {
                    pole[x][y] = 5;  //triple word;
                }
            }
        }
    }

}
