/*
 * SimpleInputConsole.java
 *
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package MyInpuyConsole;

import java.util.ArrayList;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import OGLbasic.GLApp;
import OGLbasic.GLImage;

/**
 *
 * @author Jirka
 */
public abstract class SimpleInputConsole {

    private boolean visible;
    private String zadano = "";
    private static GLApp api;
    private final int bg;
    private final ArrayList<String> history = new ArrayList<String>();
    private int historyIndex = 0;

    /*
     * Creates a new instance of SimpleInputConsole
     */
    public SimpleInputConsole(GLApp api, String font, String bg) {
        history.add("top of history");
        SimpleInputConsole.api = api;
        GLImage textureImg = GLApp.loadImage(bg);
        this.bg = GLApp.makeTexture(textureImg);

        buildFont(font, 16);

    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public String getZadano() {
        return zadano;
    }

    public void setZadano(String zadano) {
        this.zadano = zadano;
    }

    public void input(int znak, boolean alt) {
        switch (znak) {
            case Keyboard.KEY_LSHIFT:
                return;
            case Keyboard.KEY_RSHIFT:
                return;
            case Keyboard.KEY_LCONTROL:
                return;
            case Keyboard.KEY_RCONTROL:
                return;

//hacek carka?
        }

        if ((znak == Keyboard.KEY_BACK || znak == Keyboard.KEY_DELETE) && zadano.length() > 0) {
            zadano = zadano.substring(0, zadano.length() - 1);
        } else if (znak == Keyboard.KEY_RETURN) {
            history.add(zadano);
            historyIndex = history.size() - 1;
            finish();
        } else if (znak == Keyboard.KEY_UP) {
            if (historyIndex + 1 < history.size()) {
                historyIndex++;
                zadano = history.get(historyIndex);
            }
        } else if (znak == Keyboard.KEY_DOWN) {
            if (historyIndex > 0) {
                historyIndex--;
                zadano = history.get(historyIndex);
            }
        } /*}else if (znak==Keyboard.KEY_SPACE) {
         zadano+=" ";
         }else if (znak==Keyboard.KEY_MINUS) {
         zadano+="-";
         }else if (znak==Keyboard.KEY_MULTIPLY) {
         zadano+="*";
         }*/ else {

            zadano += Keyboard.getEventCharacter();
            zadano = zadano.toUpperCase();
        }

    }

    public void draw(int left, int top, int width, int height) {
        if (!visible) {
            return;
        }
        GLApp.setOrthoOn();

        GL11.glBindTexture(GL11.GL_TEXTURE_2D, bg);
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glNormal3f(0.0f, 0.0f, 1.0f);
        GL11.glTexCoord2f(0.0f, 0.0f);
        GL11.glVertex2i(left, top);	// Bottom Left Of The Texture and Quad
        GL11.glTexCoord2f(1.0f, 0.0f);
        GL11.glVertex2i(left + width, top);	// Bottom Right Of The Texture and Quad
        GL11.glTexCoord2f(1.0f, 1.0f);
        GL11.glVertex2i(left + width, top + height);	// Top Right Of The Texture and Quad
        GL11.glTexCoord2f(0.0f, 1.0f);
        GL11.glVertex2i(left, top + height);	// Top Left Of The Texture and Quad
        GL11.glEnd();

        glPrint(left, top + 14, 0, zadano);
        GLApp.setOrthoOff();

    }

    public abstract void finish();

    //========================================================================
    // Functions to build a character set and draw text strings.
    //
    // Example:
    //           buildFont("Font_tahoma.png");
    //           ...
    //           glPrint(100, 100, 0, "Here's some text");
    //           ...
    //           destroyFont();   // cleanup
    //========================================================================
    static int fontListBase = -1;           // Base Display List For The character set
    static int fontTextureHandle = -1;      // Texture handle for character set image

    /**
     * Build a character set from the given texture image.
     *
     * @param charSetImage texture image containing 256 characters in a 16x16
     * grid
     * @param fontWidth how many pixels to allow per character on screen
     *
     * @see destroyFont()
     */
    public static void buildFont(String charSetImage, int fontWidth) {
        // make texture from image
        GLImage textureImg = GLApp.loadImage(charSetImage);
        fontTextureHandle = GLApp.makeTexture(textureImg);
        // build character set as call list of 256 textured quads
        buildFont(fontTextureHandle, fontWidth);
    }

    /**
     * Build the character set display list from the given texture. Creates one
     * quad for each character, with one letter textured onto each quad. Assumes
     * the texture is a 256x256 image containing every character of the charset
     * arranged in a 16x16 grid. Each character is 16x16 pixels. Call
     * destroyFont() to release the display list memory.
     *
     * Should be in ORTHO (2D) mode to render text (see setOrtho()).
     *
     * Special thanks to NeHe and Giuseppe D'Agata for the "2D Texture Font"
     * tutorial (http://nehe.gamedev.net). grid
     *
     * @param fontTxtrHandle
     * @param fontWidth how many pixels to allow per character on screen
     *
     * @see destroyFont()
     */
    public static void buildFont(int fontTxtrHandle, int fontWidth) {
        float factor = 1f / 16f;
        float cx, cy;
        fontListBase = GL11.glGenLists(256); // Creating 256 Display Lists
        for (int i = 0; i < 256; i++) {
            cx = (float) (i % 16) / 16f;              // X Texture Coord Of Character (0 - 1.0)
            cy = (float) (i / 16) / 16f;              // Y Texture Coord Of Character (0 - 1.0)
            GL11.glNewList(fontListBase + i, GL11.GL_COMPILE); // Start Building A List
            GL11.glBegin(GL11.GL_QUADS);              // Use A 16x16 pixel Quad For Each Character
            GL11.glTexCoord2f(cx, 1 - cy - factor);  // Texture Coord (Bottom Left)
            GL11.glVertex2i(0, 0);
            GL11.glTexCoord2f(cx + factor, 1 - cy - factor); // Texture Coord (Bottom Right)
            GL11.glVertex2i(16, 0);
            GL11.glTexCoord2f(cx + factor, 1 - cy);   // Texture Coord (Top Right)
            GL11.glVertex2i(16, 16);
            GL11.glTexCoord2f(cx, 1 - cy);             // Texture Coord (Top Left)
            GL11.glVertex2i(0, 16);
            GL11.glEnd();                              // Done Building Our Quad (Character)
            GL11.glTranslatef(fontWidth, 0, 0);        // Move To The Right Of The Character
            GL11.glEndList();                          // Done Building The Display List
        } // Loop Until All 256 Are Built
    }

    /**
     * Clean up the allocated display lists for the character set.
     */
    public static void destroyFont() {
        if (fontListBase != -1) {
            GL11.glDeleteLists(fontListBase, 256);
            fontListBase = -1;
        }
    }

    @Override
    public void finalize() throws Throwable {
        super.finalize();
        destroyFont();
    }

    /*
     * Render a text string onto the screen, using the character set created by
     * buildCharSet().
     */
    public static void glPrint(int x, int y, int set, String msg) {
        int offset = fontListBase - 32 + (128 * set);
        if (fontListBase == -1 || fontTextureHandle == -1) {
            System.out.println("GLApp.glPrint(): character set has not been created -- run buildCharSet() first");
            return;
        }
        if (msg != null) {
            // enable the charset texture
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, fontTextureHandle);
            // prepare to render in 2D
            // setOrthoOn();
            // draw the text
            GL11.glTranslatef(x, y, 0);        // Position The Text (in pixels coords)
            for (int i = 0; i < msg.length(); i++) {
                GL11.glCallList(offset + msg.charAt(i));
            }
            // restore the original positions and views
            //setOrthoOff();
        }
    }

}
