package Cammons;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author Jirka
 */
public class ImageResizer {

    /**
     * Creates a new instance of ImageResizer
     */
    public ImageResizer() {
    }

    static private int binarizevalue(int i) {
        int po = 2;
        for (int x = 1; x <= 20; x++) {
            if (Math.abs(po - i) <= ((x + 1) / 2) + 1) {
                return po;
            }
            po *= 2;
        }

        return i;
    }

    static private boolean isNumberBinary(int value) {
        if (value < 0) {
            value = -value;
        };
        if (value == 0) {
            return false;
        }
        while (value > 1) {
            if (value % 2 == 0) {
                value = value / 2;
            } else {
                return false;
            }
        }
        return true;
    }

    static final public BufferedImage bufferedImageToBUfferedImage(BufferedImage srcimage, double factor) {
        int nwwidth = (int) (srcimage.getWidth() * factor);
        int nwheight = (int) (srcimage.getHeight() * factor);
        nwwidth = binarizevalue(nwwidth);
        nwheight = binarizevalue(nwheight);
        BufferedImage destimage = new BufferedImage(nwwidth, nwheight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = destimage.createGraphics();
        g2d.drawImage(srcimage, 0, 0, destimage.getWidth(), destimage.getHeight(), null);
        return destimage;

    }

    static final public BufferedImage bufferedImageToBUfferedImageALPHA(BufferedImage srcimage, double factor) {
        int nwwidth = (int) (srcimage.getWidth() * factor);
        int nwheight = (int) (srcimage.getHeight() * factor);
        nwwidth = binarizevalue(nwwidth);
        nwheight = binarizevalue(nwheight);
        BufferedImage destimage = new BufferedImage(nwwidth, nwheight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = destimage.createGraphics();
        g2d.drawImage(srcimage, 0, 0, destimage.getWidth(), destimage.getHeight(), null);
        return destimage;

    }

    static final public void resizeJpgFile(File odkud, File kam, double factor) throws IOException {
        BufferedImage srcimage = ImageIO.read(odkud);
        BufferedImage destimage = bufferedImageToBUfferedImage(srcimage, factor);
        ImageIO.write(destimage, "jpg", kam);
    }

    static final public void resizePNGFile(File odkud, File kam, double factor) throws IOException {
        BufferedImage srcimage = ImageIO.read(odkud);
        BufferedImage destimage = bufferedImageToBUfferedImageALPHA(srcimage, factor);
        ImageIO.write(destimage, "PNG", kam);
    }

    static final public String resizeJpgFileToTemp(File odkud, double factor) throws IOException {
        BufferedImage srcimage = ImageIO.read(odkud);
        File kam = File.createTempFile("ShadowSlider_" + odkud.getName() + "_", "_resized.jpg");
        resizeJpgFile(odkud, kam, factor);
        return kam.toString();
    }

    static final public void resizeBufferedImageToFile(BufferedImage srcimage, File kam, double factor) throws IOException {
        BufferedImage destimage = bufferedImageToBUfferedImage(srcimage, factor);
        ImageIO.write(destimage, "jpg", kam);
    }

    static final public String resizeBufferedImageToTmpFile(BufferedImage srcimage, String itsName, double factor) throws IOException {
        File kam = File.createTempFile("ShadowSlider_" + itsName + "_", "_resized.jpg");
        resizeBufferedImageToFile(srcimage, kam, factor);
        return kam.toString();
    }

    static BufferedImage convertHorizPanoramaToRatio(BufferedImage srcimage, double panoramafactor) {
        BufferedImage nwsrc = new BufferedImage(srcimage.getWidth(), srcimage.getWidth() / 2, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = nwsrc.createGraphics();
        g2d.drawImage(srcimage, 0, nwsrc.getHeight() / 2 - srcimage.getHeight() / 2, srcimage.getWidth(), srcimage.getHeight(), null);
        return nwsrc;
    }

    static BufferedImage greaterrBinaryImage(BufferedImage srcimage) {
        int x = 0;
        while (Math.pow(2d, (double) x) < srcimage.getWidth()) {
            x++;
        }
        int nwwidth = (int) Math.pow(2d, (double) x);

        x = 0;
        while (Math.pow(2d, (double) x) < srcimage.getHeight()) {
            x++;
        }
        int nwheight = (int) Math.pow(2d, (double) x);
        BufferedImage vysledek = new BufferedImage(nwwidth, nwheight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = vysledek.createGraphics();
        g2d.drawImage(srcimage,
                vysledek.getWidth() / 2 - srcimage.getWidth() / 2,
                vysledek.getHeight() / 2 - srcimage.getHeight() / 2,
                null);
        return vysledek;
    }

    static BufferedImage greaterrFramedImage(BufferedImage srcimage, int minvalue) {
        int x = minvalue;
        while (x < srcimage.getWidth()) {
            x += minvalue;
        }
        int nwwidth = x;

        x = minvalue;
        while (x < srcimage.getHeight()) {
            x += minvalue;
        }
        int nwheight = x;

        BufferedImage vysledek = new BufferedImage(nwwidth, nwheight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = vysledek.createGraphics();
        g2d.drawImage(srcimage,
                vysledek.getWidth() / 2 - srcimage.getWidth() / 2,
                vysledek.getHeight() / 2 - srcimage.getHeight() / 2,
                null);
        return vysledek;
    }

    static BufferedImage greaterrBinaryImageALPHA(BufferedImage srcimage) {
        int x = 0;
        while (Math.pow(2d, (double) x) < srcimage.getWidth()) {
            x++;
        }
        int nwwidth = (int) Math.pow(2d, (double) x);

        x = 0;
        while (Math.pow(2d, (double) x) < srcimage.getHeight()) {
            x++;
        }
        int nwheight = (int) Math.pow(2d, (double) x);
        BufferedImage vysledek = new BufferedImage(nwwidth, nwheight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = vysledek.createGraphics();
        g2d.drawImage(srcimage,
                vysledek.getWidth() / 2 - srcimage.getWidth() / 2,
                vysledek.getHeight() / 2 - srcimage.getHeight() / 2,
                null);
        return vysledek;
    }

    static BufferedImage greaterrFramedImageALPHA(BufferedImage srcimage, int minvalue) {
        int x = minvalue;
        while (x < srcimage.getWidth()) {
            x += minvalue;
        }
        int nwwidth = x;

        x = minvalue;
        while (x < srcimage.getHeight()) {
            x += minvalue;
        }
        int nwheight = x;

        BufferedImage vysledek = new BufferedImage(nwwidth, nwheight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = vysledek.createGraphics();
        g2d.drawImage(srcimage,
                vysledek.getWidth() / 2 - srcimage.getWidth() / 2,
                vysledek.getHeight() / 2 - srcimage.getHeight() / 2,
                null);
        return vysledek;
    }

    public static void resizePNGdir(File dir) {
        FileFilter f = new FileFilter() {

            public boolean accept(File pathname) {
                if (pathname.toString().indexOf(".png") == -1) {
                    return false;
                } else {
                    return true;
                }
            }
        };
        File[] soubory = dir.listFiles(f);
        for (int x = 0; x < soubory.length; x++) {
            try {
                resizePNGFile(soubory[x], soubory[x], 128d / 100d);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
//       resizePNGdir(new File("D:\\CD\\Context\\SSdata\\hand4"));
        for (int x = -10; x <= 4096; x++) {
            System.err.println(x + " " + isNumberBinary(x));
        }

    }

}
