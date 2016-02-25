/*
 * OGLdialog.java
 *
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package OGLaddons;

import Cammons.Cammons;
import OGLbasic.GLImage;
import java.util.ArrayList;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import scrabble.*;

/**
 *
 * @author Jirka
 */
public class OGLdialog {

    public static final int MB_OK = 0;
    public static final int MB_CANCEL = 1;
    public static final int MB_NO = 2;

    public static final int MR_OK = 0;
    public static final int MR_CANCEL = 1;
    public static final int MR_NO = 2;
    public static final int MR_RESIZEX = -3;
    public static final int MR_RESIZEY = -2;
    public static final int MR_SCROLL_UP = -4;
    public static final int MR_SCROLL_DOWN = -5;
    public static final int MR_SCROLL_LEFT = -6;
    public static final int MR_SCROLL_RIGHT = -7;
    public static final int MR_NOTHING = -1;

    private int[] buttons = new int[3];
    private final int[] buttonsTexture = new int[3];
    private final int bg, borders;
    private final int[] sb = new int[6];
    private final int SB_UP = 0;
    private final int SB_DOWN = 1;
    private final int SB_LEFT = 2;
    private final int SB_RIGHT = 3;
    private final int SB_BG = 4;
    private final int SB_RD = 5;
    private static Main api;

    private ArrayList<String> context;
    private ArrayList<String> renderedContext;

    private int width;
    private int height;
    private int left;
    private int top;
    private int buttonleft;
    private boolean visible;
    private int scrolledX;
    private int scrolledY;

    private int rowsOnScreen;
    private int charsOnScreens;

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }
    private int maxChars;

    public int getWidth() {
        return width;
    }

    public void setHeight(int height) {
        this.height = height;
        //dopocitat
    }

    public void setWidth(int width) {
        this.width = width;
        //dopocitat
    }

    public int getHeight() {
        return height;
    }

    public void prepare(int[] buttons, ArrayList<String> context, int width, int height) {
        left = (Main.DM.getWidth() - width) / 2;
        top = (Main.DM.getHeight() + height) / 2;
        scrolledX = 0;
        scrolledY = 0;
        visible = true;
        this.buttons = buttons;
        this.context = context;
        this.width = width;
        this.height = height;

        int max = 0;
        for (String context1 : context) {
            if (context1.length() > max) {
                max = context1.length();
            }
        }

        rowsOnScreen = (height - 110) / 32;//50 button 10+10+10+10 okraje 20 scroll
        charsOnScreens = (width - 40) / 16;//10+10 okraje,20 scroll

        if (buttons.length % 2 == 0) {
            buttonleft = left + width / 2 - (buttons.length / 2) * 140;
        } else {
            buttonleft = left + width / 2 - (buttons.length - 1) * 70 - 70;
        }

        maxChars = 0;
        for (String context1 : context) {
            maxChars = Math.max(maxChars, context1.length());
        }
        renderedContext = getRenderedLines();
    }

    public void execute(int[] buttons, ArrayList<String> context, int width, int height) {
        prepare(buttons, context, width, height);

        show();

    }

    public void show() {
        api.globalReaction.reaction = OGLdialog.MR_NOTHING;
        boolean finished = false;
        try {

            // Main loop
            while (!finished) {
                if (!Display.isVisible()) {  //!!!
                    Thread.sleep(200L);
                } else if (Display.isCloseRequested()) {  //!!!
                    finished = true;
                }
                api.mainLoop();
                Display.update();  //!!!!

                if (api.globalReaction.reaction >= 0) {
                    finished = true;
                }

            }

        } catch (Exception e) {
            System.out.println("GLApp.run(): " + e);
            e.printStackTrace(System.out);
        }
    }

    private ArrayList<String> getRenderedLines() {
        ArrayList<String> vysledek = new ArrayList<String>();
        if (scrolledY >= context.size()) {
            return vysledek;
        }
        for (int y = scrolledY; y < Math.min(scrolledY + rowsOnScreen, context.size()); y++) {
            int from = scrolledX;
            if (from < 0) {
                from = 0;
            }
            int to = scrolledX + charsOnScreens;
            if (to < 0) {
                to = 0;
            }
            if (to > context.get(y).length()) {
                to = context.get(y).length();
            }
            if (to - from <= 0) {
                vysledek.add("");
            } else {
                vysledek.add(context.get(y).substring(from, to));
            }

        }
        return vysledek;
    }

    /*
     * Creates a new instance of OGLdialog
     */
    public OGLdialog(String buttonsroot, String bg, Main api, String font, String sbname, String borders) {
        OGLdialog.api = api;

        String preffix = Cammons.deleteSuffix(buttonsroot);
        String suffix = buttonsroot.substring(preffix.length(), buttonsroot.length());
        GLImage textureImg = Main.loadImage(preffix + "OK" + suffix);
        buttonsTexture[MB_OK] = Main.makeTexture(textureImg);
        textureImg = Main.loadImage(preffix + "CANCEL" + suffix);
        buttonsTexture[MB_CANCEL] = Main.makeTexture(textureImg);
        textureImg = Main.loadImage(preffix + "NO" + suffix);
        buttonsTexture[MB_NO] = Main.makeTexture(textureImg);

        textureImg = Main.loadImage(bg);
        this.bg = Main.makeTexture(textureImg);

        textureImg = Main.loadImage(borders);
        this.borders = Main.makeTexture(textureImg);

        buildFont(font, 16);

        preffix = Cammons.deleteSuffix(sbname);
        suffix = sbname.substring(preffix.length(), sbname.length());
        textureImg = Main.loadImage(preffix + "UP" + suffix);
        sb[SB_UP] = Main.makeTexture(textureImg);
        textureImg = Main.loadImage(preffix + "DOWN" + suffix);
        sb[SB_DOWN] = Main.makeTexture(textureImg);
        textureImg = Main.loadImage(preffix + "LEFT" + suffix);
        sb[SB_LEFT] = Main.makeTexture(textureImg);
        textureImg = Main.loadImage(preffix + "RIGHT" + suffix);
        sb[SB_RIGHT] = Main.makeTexture(textureImg);
        textureImg = Main.loadImage(preffix + "BG" + suffix);
        sb[SB_BG] = Main.makeTexture(textureImg);
        textureImg = Main.loadImage(preffix + "RIDER" + suffix);
        sb[SB_RD] = Main.makeTexture(textureImg);

    }

    //pokud je visible tak se vykresli dle parametru
    public void draw() {
        if (!visible) {
            return;
        }
        Main.setOrthoOn();

        GL11.glBindTexture(GL11.GL_TEXTURE_2D, bg);
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glNormal3f(0.0f, 0.0f, 1.0f);
        GL11.glTexCoord2f(0.0f, 0.0f);
        GL11.glVertex2i(left, top - height);	// Bottom Left Of The Texture and Quad
        GL11.glTexCoord2f(1.0f, 0.0f);
        GL11.glVertex2i(left + width, top - height);	// Bottom Right Of The Texture and Quad
        GL11.glTexCoord2f(1.0f, 1.0f);
        GL11.glVertex2i(left + width, top);	// Top Right Of The Texture and Quad
        GL11.glTexCoord2f(0.0f, 1.0f);
        GL11.glVertex2i(left, top);	// Top Left Of The Texture and Quad
        GL11.glEnd();

        for (int x = 0; x < buttons.length; x++) {
            drawButton(buttonleft + (x * 140), top - height + 60, 128, 50, buttonsTexture[buttons[x]]);
        }
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        for (int y = 0; y < renderedContext.size(); y++) {
            glPrint(10 + left, top - 10 - (y + 1) * 32, 0, renderedContext.get(y));
        }

        //GL11.glColor4f(1f,1f,1f,0.5f);
        if (maxChars > charsOnScreens) {
            drawButton(left + 10, top - height + 90, 20, 20, sb[SB_LEFT]);
            drawButton(width + left - 2 * 20 - 10, top - height + 90, 20, 20, sb[SB_RIGHT]);
            drawScroll(left + 10 + 20, top - height + 90, width + left - 2 * 20 - 10, top - height + 90 - 20, sb[SB_BG]);
            drawButton(left + 10 + 20 + (width - 10 - 20 - 20 - 20 - 10) / (maxChars - charsOnScreens + 1) * scrolledX, top - height + 90, 20, 20, sb[SB_RD]);
        }
        if (rowsOnScreen < context.size()) {
            drawButton(width + left - 20 - 10, top - height + 90 + 20, 20, 20, sb[SB_DOWN]);
            drawButton(width + left - 20 - 10, top - 10, 20, 20, sb[SB_UP]);
            drawScroll(width + left - 20 - 10, top - 10 - 20, width + left - 10, top - height + 90 + 20, sb[SB_BG]);
            drawButton(width + left - 20 - 10, top - 10 - 20 - (height - 130) / (context.size() - rowsOnScreen + 1) * scrolledY, 20, 20, sb[SB_RD]);
        }
        //GL11.glColor4f(1f,1f,1f,1f);

        Main.setOrthoOff();
    }

    ;
    
public void mouseDown(DialogReactioner r) {
        r.reaction = mouseDown();
    }

    public void keyDown(DialogReactioner r, int keycode) {
        r.reaction = keyDown(keycode);
    }

    public int keyDown(int keycode) {
        if (!visible) {
            return MR_NOTHING;
        }
        if (maxChars > charsOnScreens) {
            if (keycode == Keyboard.KEY_RIGHT) {
                if (scrolledX == 0) {
                    return MR_NOTHING;
                }
                scrolledX--;
                renderedContext = getRenderedLines();
                return MR_SCROLL_RIGHT;
            }
            if (keycode == Keyboard.KEY_LEFT) {
                if (scrolledX > maxChars - charsOnScreens - 1) {
                    return MR_NOTHING;
                }
                scrolledX++;
                renderedContext = getRenderedLines();
                return MR_SCROLL_LEFT;
            }

            if (keycode == Keyboard.KEY_RIGHT) {
                if (scrolledX == 0) {
                    return MR_NOTHING;
                }
                scrolledX--;
                renderedContext = getRenderedLines();
                return MR_SCROLL_RIGHT;
            }
            if (keycode == Keyboard.KEY_LEFT) {
                if (scrolledX > maxChars - charsOnScreens - 1) {
                    return MR_NOTHING;
                }
                scrolledX++;
                renderedContext = getRenderedLines();
                return MR_SCROLL_LEFT;
            }
        }
        if (rowsOnScreen < context.size()) {//untested

            if (keycode == Keyboard.KEY_NEXT) {
                if (scrolledY > context.size() - rowsOnScreen - 1) {
                    return MR_NOTHING;
                }
                scrolledY += rowsOnScreen;
                {
                    if (scrolledY > context.size() - rowsOnScreen) {
                        scrolledY = context.size() - rowsOnScreen;
                    }

                }
                renderedContext = getRenderedLines();
                return MR_SCROLL_DOWN;
            }
            if (keycode == Keyboard.KEY_PRIOR) {//untested
                if (scrolledY < 0) {
                    return MR_NOTHING;
                }
                scrolledY -= rowsOnScreen;
                if (scrolledY < 0) {
                    scrolledY = 0;
                }
                renderedContext = getRenderedLines();
                return MR_SCROLL_UP;
            }
        }

        for (int x = 0; x < buttons.length; x++) {
            if (keycode == Keyboard.KEY_RETURN && buttons[x] == MR_OK) {
                visible = false;
                return buttons[x];
            }
            if (keycode == Keyboard.KEY_ESCAPE && buttons[x] == MR_CANCEL) {
                visible = false;
                return buttons[x];
            }
        }
        return MR_NOTHING;
    }

    public int mouseDown() {
        if (!visible) {
            return MR_NOTHING;
        }
        if (maxChars > charsOnScreens) {
            if (cursorIn(left + 10, top - height + 90, 20, 20)) {
                if (scrolledX == 0) {
                    return MR_NOTHING;
                }
                scrolledX--;
                renderedContext = getRenderedLines();
                return MR_SCROLL_RIGHT;
            }
            if (cursorIn(width + left - 2 * 20 - 10, top - height + 90, 20, 20)) {
                if (scrolledX > maxChars - charsOnScreens - 1) {
                    return MR_NOTHING;
                }
                scrolledX++;
                renderedContext = getRenderedLines();
                return MR_SCROLL_LEFT;
            }
        }
        if (rowsOnScreen < context.size()) {
            if (cursorIn(width + left - 20 - 10, top - height + 90 + 20, 20, 20)) {
                if (scrolledY > context.size() - rowsOnScreen - 1) {
                    return MR_NOTHING;
                }
                scrolledY++;
                renderedContext = getRenderedLines();
                return MR_SCROLL_DOWN;
            }
            if (cursorIn(width + left - 20 - 10, top - 10, 20, 20)) {
                if (scrolledY == 0) {
                    return MR_NOTHING;
                }
                scrolledY--;
                renderedContext = getRenderedLines();
                return MR_SCROLL_UP;
            }
        }

        for (int x = 0; x < buttons.length; x++) {
            if (cursorIn(buttonleft + (x * 140), top - height + 60, 128, 50)) {
                visible = false;
                return buttons[x];
            }
        }
        return MR_NOTHING;
    }

    private boolean cursorIn(int left, int top, int width, int height) {
        return Main.cursorX > left && Main.cursorX < left + width
                && Main.cursorY < top && Main.cursorY > top - height;
    }

    private void drawButton(int left, int top, int width, int height, int type) {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, type);
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glNormal3f(0.0f, 0.0f, 1.0f);
        GL11.glTexCoord2f(0.0f, 0.0f);
        GL11.glVertex2i(left, top - height);	// Bottom Left Of The Texture and Quad
        GL11.glTexCoord2f(1.0f, 0.0f);
        GL11.glVertex2i(left + width, top - height);	// Bottom Right Of The Texture and Quad
        GL11.glTexCoord2f(1.0f, 1.0f);
        GL11.glVertex2i(left + width, top);	// Top Right Of The Texture and Quad
        GL11.glTexCoord2f(0.0f, 1.0f);
        GL11.glVertex2i(left, top);	// Top Left Of The Texture and Quad
        GL11.glEnd();
    }

    private void drawScroll(int left, int top, int right, int bottom, int type) {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, type);
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glNormal3f(0.0f, 0.0f, 1.0f);
        GL11.glTexCoord2f(0.0f, 0.0f);
        GL11.glVertex2i(left, bottom);	// Bottom Left Of The Texture and Quad
        GL11.glTexCoord2f(1.0f, 0.0f);
        GL11.glVertex2i(right, bottom);	// Bottom Right Of The Texture and Quad
        GL11.glTexCoord2f(1.0f, 1.0f);
        GL11.glVertex2i(right, top);	// Top Right Of The Texture and Quad
        GL11.glTexCoord2f(0.0f, 1.0f);
        GL11.glVertex2i(left, top);	// Top Left Of The Texture and Quad
        GL11.glEnd();
    }

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
        GLImage textureImg = Main.loadImage(charSetImage);
        fontTextureHandle = Main.makeTexture(textureImg);
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
     * tutorial (http://nehe.gamedev.net). id
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
            Main.setOrthoOn(); //celej dialog je 2d
            // draw the text
            GL11.glTranslatef(x, y, 0);        // Position The Text (in pixels coords)
            for (int i = 0; i < msg.length(); i++) {
                GL11.glCallList(offset + msg.charAt(i));
            }
            // restore the original positions and views
            Main.setOrthoOff();
        }
    }

    /*
     public static String deleteSuffix(String file) {
     int x=file.length();
     while (x>=0){
     x--;
     if (file.charAt(x)=='.'){
     return file.substring(0,x);
                
     }
     if (file.charAt(x)==File.separatorChar)        {
     return file;
     }
     }
     return null;
     }
     **/
    @Override
    public void finalize() throws Throwable {
        super.finalize();
        destroyFont();
    }

}
