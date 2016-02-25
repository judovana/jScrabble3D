/*
 * PaintComponent.java
 *
 * Created on 19. duben 2007, 9:46
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package Cammons;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.swing.JComponent;

/**
 *
 * @author Jirka
 */
public class PaintComponent extends JComponent {

    BufferedImage im;
    JComponent owener;
    double zoomed = 1;
    Graphics g;

    /**
     * Creates a new instance of PaintComponent
     */
    public PaintComponent() {
    }

    public void setzoom() {
        if (im == null) {
            return;
        }
        int origImW = im.getWidth();
        int origImH = im.getHeight();
        int myscreeenwidth = owener.getWidth();
        int myscreenheight = owener.getHeight();
        float nwImW = (float) ((double) origImW / (double) myscreeenwidth);
        float nwImH = (float) ((double) origImH / (double) myscreenheight);
        if (nwImH > nwImW) {
            nwImW = origImW / nwImH;
            nwImH = origImH / nwImH;
        } else {
            nwImH = origImH / nwImW;
            nwImW = origImW / nwImW;
        }
        nwImW = nwImW;
        nwImH = nwImH;
        zoomed = ((double) (nwImW / (double) im.getWidth()));

    }

    public void setIamge(BufferedImage i) {
        im = i;
        setzoom();
    }

    public void setOwener(JComponent owener) {
        this.owener = owener;
    }

    public void paint(Graphics g) {
        this.g = g;
        Graphics2D g2d = (Graphics2D) g;

        int l = 0, t = 0;
        if (im != null) {
                            //l=(int)(owener.getWidth()/2-(im.getWidth()*zoomed)/2);
            // t=(int)(owener.getHeight()/2-(im.getHeight()*zoomed)/2);
            if (l < 0) {
                l = 0;
            }
            if (t < 0) {
                t = 0;
            }
            if (zoomed != 1) {
                g2d.drawImage(im, l, t, (int) (zoomed * im.getWidth()), (int) (zoomed * im.getHeight()), this); //:((
            } else {
                g2d.drawImage(im, null, l, t);
            }
        }

    }

    protected void paintComponent(Graphics g) {
        paint(g);
    }

    public void repaint() {
        paint(g);
    }

};
