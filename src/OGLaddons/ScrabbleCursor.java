/*
 * ScrabbleCursor.java
 *
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package OGLaddons;

import OGLbasic.GLApp;
import OGLbasic.GLImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.IntBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

/**
 *
 * @author Jirka
 */
public class ScrabbleCursor {

    private int cursorcounter = 0;
    int cinc = 1;
    int maxcr = 40;
    int maxcr2 = 40;
    int cursorused = 0;
    int crshiftx = 0, crshifty = 0;

    public void setCinc(int cinc) {
        this.cinc = cinc;
    }
    String dir;

    private int[] cursorhandle;

    public int getClclick() {
        return clclick;
    }

    public int getCrclick() {
        return crclick;
    }

    private int clclick;

    private int crclick;

    private int cursorsize;

    private GLApp api;

    /*
     * Creates a new instance of ScrabbleCursor
     */
    public ScrabbleCursor(GLApp api, String dir) {
        this.api = api;
        this.dir = dir;
        try {
            loadcursor(false);
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void Draw() {
        GLApp.setOrthoOn();
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        int cX = GLApp.cursorX - crshiftx;//36;
        int cY = GLApp.cursorY + crshifty - 128;//26;
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, cursorhandle[cursorcounter]);
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glTexCoord2d(0.0, 0.0);
        GL11.glVertex3d(cX, cY, 0);
        GL11.glTexCoord2d(0.0, 1.0);
        GL11.glVertex3d(cX, cY + cursorsize, 0);
        GL11.glTexCoord2d(1.0, 1.0);
        GL11.glVertex3d(cX + cursorsize, cY + cursorsize, 0);
        GL11.glTexCoord2d(1.0, 0.0);
        GL11.glVertex3d(cX + cursorsize, cY, 0);
        GL11.glEnd();
        GLApp.setOrthoOff();
    }

    public void countcursor() {
        int usedmaxcr;
        cursorcounter += cinc;
        if (cursorcounter < 0) {
            cursorcounter = 0;
        }
        if (cinc == 0) {
            usedmaxcr = maxcr2;
        } else {
            usedmaxcr = maxcr;
        }
        if (cursorcounter > usedmaxcr) {
            cursorcounter = usedmaxcr;
        }
        if (cursorcounter == 0) {
            cinc = 1;
        }
        if (cursorcounter == usedmaxcr) {
            cinc = -1;
        }

    }

    private void loadcursor(boolean delete) throws FileNotFoundException, IOException {
        cursorcounter = 0;
        if (delete) {
            for (int x = 0; x <= maxcr; x++) {
                IntBuffer textureIdBuffer = BufferUtils.createIntBuffer(1);
                textureIdBuffer.put(cursorhandle[x]).flip();
                GL11.glDeleteTextures(textureIdBuffer);
            }
        }

        FileReader r = new FileReader(dir + File.separator + "cr.txt");
        char[] ch = new char[100];
        r.read(ch);
        String s = new String();
        s = String.copyValueOf(ch);
        int i = 0;
        s = s.trim();
        /*
         a [name]
         b[lastfile number]
         d[pointx]
         e[pointy]
         f[max animation index]
         g[right click image]
         h[leftclick image]
         s(final)[number of 0 chars in countin]
         */
        String a = s.substring(0, s.indexOf(' '));
        s = s.substring(s.indexOf(' ') + 1);
        String b = s.substring(0, s.indexOf(' '));
        s = s.substring(s.indexOf(' ') + 1);
        String d = s.substring(0, s.indexOf(' '));
        s = s.substring(s.indexOf(' ') + 1);
        String e = s.substring(0, s.indexOf(' '));
        s = s.substring(s.indexOf(' ') + 1);
        String f = s.substring(0, s.indexOf(' '));
        s = s.substring(s.indexOf(' ') + 1);
        String g = s.substring(0, s.indexOf(' '));
        s = s.substring(s.indexOf(' ') + 1);
        String h = s.substring(0, s.indexOf(' '));
        s = s.substring(s.indexOf(' ') + 1);

        maxcr = Integer.parseInt(b);
        int nul = Integer.parseInt(s);
        cursorhandle = new int[maxcr + 1];
        for (int x = 0; x <= maxcr; x++) {
            String n = String.valueOf(x);
            while (n.length() < nul) {
                n = "0" + n;
            }
            GLImage textureImg = GLApp.loadImage(dir + File.separator + a + n + ".png");
            cursorsize = textureImg.getWidth();
            cursorhandle[x] = GLApp.makeTexture(textureImg);
        }
        maxcr2 = maxcr;
        maxcr = Integer.parseInt(f);
        clclick = Integer.parseInt(h);
        crclick = Integer.parseInt(g);
        crshiftx = Integer.parseInt(d);
        crshifty = Integer.parseInt(e);

    }

    public void setCursorcounter(int cursorcounter) {
        this.cursorcounter = cursorcounter;
    }

}
