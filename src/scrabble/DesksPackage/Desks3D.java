/*
 * Desks3D.java
 *
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package scrabble.DesksPackage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import org.lwjgl.opengl.GL11;
import scrabble.GameDesktopPackage.GameDesktop;
import scrabble.GameDesktopPackage.GameDesktop3D;

/**
 *
 * @author Jirka
 */
public class Desks3D extends Desks {

    /**
     * Creates a new instance of Desks3D
     */
    private float movement = 2;
    private ArrayList<int[][]> pole;
    private int levels;
    private ArrayList<Integer> DESKY;

    public void setMovement(float movement) {
        this.movement = movement;
    }

    public void addMovement(float part) {
        this.movement += part;
    }

    public float getMovement() {
        return movement;
    }

    public int getLevels() {
        return levels;
    }

    /**
     * @deprecated
     */
    public int[][] getPole() {
        return null;
    }

    public ArrayList<int[][]> getPole3D() {
        return pole;
    }

    public Desks3D(File src, float tilesize) {
        this.tilesize = tilesize;
        pole = new ArrayList();
        DESKY = new ArrayList();
        //loading textures   
        nactiTextury();

        try {

            BufferedReader read = new BufferedReader(new FileReader(src));
            // read from the file.
            String s = read.readLine();
            String[] c = s.split("x");
            width = Integer.parseInt(c[0]);
            height = Integer.parseInt(c[1]);
            levels = Integer.parseInt(c[2]);
            for (int x = 0; x < levels; x++) {
                int[][] Lpole = new int[width][height];
                nactiDesku(Lpole, read, x);
                pole.add(Lpole);
                //creating list
                int LDESKA = makeList(Lpole);
                DESKY.add(LDESKA);
                read.readLine();
            }
            read.close();
        } catch (IOException iox) {
            // I/O error: could not open file, or lost connection, or...
            iox.printStackTrace();
        }

    }

    @Override
    public void draw() {
        GL11.glPushMatrix();
        int xlevels = levels / 2;

        GL11.glTranslatef(0, 0, -(float) xlevels * movement);
        for (Integer elem : DESKY) {
            GL11.glTranslatef(0, 0, movement);
            GL11.glCallList(elem);
        }

        GL11.glPopMatrix();
    }

    /*
     * @deprecated
     */
    @Override
    public int getLetterKoeficientAt(int x, int y) {
        return -1;
    }

    public int getLetterKoeficientAt(int x, int y, int z) {
        switch (pole.get(z)[x][y]) {

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

    /*
     * @deprecated
     */
    public int getWordKoeficientAt(int x, int y) {
        return -1;
    }

    public int getWordKoeficientAt(int x, int y, int z, GameDesktop turnek) {
        GameDesktop3D turn = (GameDesktop3D) turnek;
        switch (pole.get(z)[x][y]) {

            case (0):
                return 1;

            case (1):
                if (turn.getPolePrvek(x, y, z) == null) {
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

}
