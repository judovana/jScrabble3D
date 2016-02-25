/*
 * AsynchronousDialogExecuter.java
 *
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package scrabble;

import java.util.ArrayList;

/**
 *
 * @author Jirka
 */
public class AsynchronousDialogExecuter {

    private final int[] btns;

    private final ArrayList<String> context;

    private final int width;

    private final int height;

    public int[] getBtns() {
        return btns;
    }

    public ArrayList<String> getContext() {
        return context;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    /*
     * Creates a new instance of AsynchronousDialogExecuter
     */
    public AsynchronousDialogExecuter(int[] buttons, ArrayList<String> context, int width, int height) {
        this.btns = buttons;
        this.context = context;
        this.width = width;
        this.height = height;
    }

}
