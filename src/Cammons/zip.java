package Cammons;

import java.io.*;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 * Class <code>zip</code> is a convenient utility that provides both writing and
 * reading from a file which is transparently compressed in zip.
 *
 *
 * @author Herv&eacute; Bitteur
 * @version $Id: zip.java,v 1.3 2006/01/16 22:05:46 hbitteur Exp $
 */
public class zip {
    //~ Methods -----------------------------------------------------------

    //--------------//
    // createReader //
    //--------------//
    /**
     * Create a Reader on a given file, by looking for a zip archive whose path
     * is the file path with ".zip" appended, and by reading the first entry in
     * this zip file.
     *
     * @param file the file (with no zip extension)
     *
     * @return a reader on the zip entry
     */
    public static Reader createReader(File file) {
        try {
            String path = file.getCanonicalPath();

            ZipFile zf = new ZipFile(path + ".zip");

            for (Enumeration entries = zf.entries();
                    entries.hasMoreElements();) {
                ZipEntry entry = (ZipEntry) entries.nextElement();
                InputStream is = zf.getInputStream(entry);

                return new InputStreamReader(is);
            }
        } catch (FileNotFoundException ex) {
            System.err.println(ex.toString());
            System.err.println(file + " not found");
        } catch (IOException ex) {
            System.err.println(ex.toString());
        }

        return null;
    }

    //--------------//
    // createWriter //
    //--------------//
    /**
     * Create a Writer on a given file, transparently compressing the data to a
     * zip file whose name is the provided file path, with a ".zip" extension
     * added.
     *
     *
     * @param file the file (with no zip extension)
     * @return a writer on the zip entry
     */
    public static Writer createWriter(File file) {
        try {
            String path = file.getCanonicalPath();
            FileOutputStream fos = new FileOutputStream(path + ".zip");
            ZipOutputStream zos = new ZipOutputStream(fos);
            ZipEntry ze = new ZipEntry(file.getName());
            zos.putNextEntry(ze);

            return new OutputStreamWriter(zos);
        } catch (FileNotFoundException ex) {
            System.err.println(ex.toString());
            System.err.println(file + " not found");
        } catch (Exception ex) {
            System.err.println(ex.toString());
        }

        return null;
    }

    //--------------------//
    // createInputStream //
    //-------------------//
    /**
     * Create a InputStream on a given file, by looking for a zip archive whose
     * path is the file path with ".zip" appended, and by reading the first
     * entry in this zip file.
     *
     * @param file the file (with no zip extension)
     *
     * @return a InputStream on the zip entry
     */
    public static InputStream createInputStream(File file) {
        try {
            String path = file.getCanonicalPath();
            //ZipFile zf = new ZipFile(path + ".zip");
            ZipFile zf = new ZipFile(path);

            for (Enumeration entries = zf.entries();
                    entries.hasMoreElements();) {
                ZipEntry entry = (ZipEntry) entries.nextElement();
                return zf.getInputStream(entry);
            }
        } catch (FileNotFoundException ex) {
            System.err.println(ex.toString());
            System.err.println(file + " not found");
        } catch (IOException ex) {
            System.err.println(ex.toString());
        }

        return null;
    }

    //--------------------//
    // createOutputStream //
    //-------------------//
    /**
     * Create a OutputStream on a given file, transparently compressing the data
     * to a zip file whose name is the provided file path, with a ".zip"
     * extension added.
     *
     *
     * @param file the file (with no zip extension)
     * @return a OutputStream on the zip entry
     */
    public static OutputStream createOutputStream(File file) {
        try {
            String path = file.getCanonicalPath();
            FileOutputStream fos = new FileOutputStream(path + ".zip");
            ZipOutputStream zos = new ZipOutputStream(fos);
            ZipEntry ze = new ZipEntry(file.getName());
            zos.putNextEntry(ze);

            return zos;
        } catch (FileNotFoundException ex) {
            System.err.println(ex.toString());
            System.err.println(file + " not found");
        } catch (Exception ex) {
            System.err.println(ex.toString());
        }

        return null;
    }

    /**
     *
     * try { //create a ZipOutputStream to zip the data to ZipOutputStream zos =
     * new ZipOutputStream(new FileOutputStream(".\\curDir.zip")); //assuming
     * that there is a directory named inFolder (If there //isn't create one) in
     * the same directory as the one the code runs from, //call the zipDir
     * method zipDir(".\\inFolder", zos); //close the stream zos.close(); }
     * catch(Exception e) { //handle exception }
     */
//here is the code for the method 
    public static void zipDir(String dir2zip, ZipOutputStream zos) {
        try {
            //create a new File object based on the directory we 
            // have to zip File    
            File zipDir = new File(dir2zip);
            //get a listing of the directory content 
            String[] dirList = zipDir.list();
            byte[] readBuffer = new byte[2156];
            int bytesIn = 0;
            //loop through dirList, and zip the files 
            for (int i = 0; i < dirList.length; i++) {
                File f = new File(zipDir, dirList[i]);
                if (f.isDirectory()) {
                    //if the File object is a directory, call this 
                    //function again to add its content recursively 
                    String filePath = f.getPath();
                    zipDir(filePath, zos);
                    //loop again 
                    continue;
                }
                //if we reached here, the File object f was not 
                // a directory 
                //create a FileInputStream on top of f 
                FileInputStream fis = new FileInputStream(f);
                // create a new zip entry 
                ZipEntry anEntry = new ZipEntry(f.getName()/* f.getPath() zmena by ja(jirka)*/);
                //place the zip entry in the ZipOutputStream object 
                zos.putNextEntry(anEntry);
                //now write the content of the file to the ZipOutputStream 
                while ((bytesIn = fis.read(readBuffer)) != -1) {
                    zos.write(readBuffer, 0, bytesIn);
                }
                //close the Stream 
                fis.close();
            }
        } catch (Exception e) {
            //handle exception 
        }

        //**
    }

    public static void extract(String zipFile, String dest)
            throws IOException {
        //if(!U.isBlank(dest))         dest += File.separator;
        ZipFile zip = new ZipFile(zipFile);
        //ZipOutputStream zip= new ZipOutputStream(new FileOutputStream(new File(zipFile))); nefaka
        Enumeration entries = zip.entries();
        while (entries.hasMoreElements()) {
            ZipEntry e = (ZipEntry) entries.nextElement();
            File f = new File(dest + Cammons.extractFileName(e.getName()));
            if (e.isDirectory()) {
                if (!f.exists() && !f.mkdirs()) {
                    throw new IOException("Couldn't create dir " + f);
                }
            } else {
                BufferedInputStream is = null;
                BufferedOutputStream os = null;
                try {
                    is = new BufferedInputStream(zip.getInputStream(e));
                    File destDir = f.getParentFile();
                    if (!destDir.exists() && !destDir.mkdirs()) {
                        throw new IOException("Couldn't create dir " + destDir);
                    }
                    os = new BufferedOutputStream(new FileOutputStream(f));
                    int b = -1;
                    while ((b = is.read()) != -1) {
                        os.write(b);
                    }
                } finally {
                    if (is != null) {
                        is.close();
                    }
                    if (os != null) {
                        os.close();
                    }
                }
            }
        }
    }

    public static void zipFile(String file2zip, ZipOutputStream zos) {
        try {
            //create a new File object based on the directory we 
            // have to zip File    
            File zipDir = new File(file2zip);

            byte[] readBuffer = new byte[2156];
            int bytesIn = 0;

            File f = zipDir;
            if (f.isDirectory()) {
            }
            //if we reached here, the File object f was not 
            // a directory 
            //create a FileInputStream on top of f 
            FileInputStream fis = new FileInputStream(f);
            // create a new zip entry 
            ZipEntry anEntry = new ZipEntry(f.getName()/* f.getPath()*/);
            //place the zip entry in the ZipOutputStream object 
            zos.putNextEntry(anEntry);
            //now write the content of the file to the ZipOutputStream 
            while ((bytesIn = fis.read(readBuffer)) != -1) {
                zos.write(readBuffer, 0, bytesIn);
            }
            //close the Stream 
            fis.close();

        } catch (Exception e) {
            //handle exception 
        }

        //**
    }

    public static void zipFile(File file2zip, ZipOutputStream zos) {
        try {
            //create a new File object based on the directory we 
            // have to zip File    
            File zipDir = file2zip;

            byte[] readBuffer = new byte[2156];
            int bytesIn = 0;

            File f = zipDir;
            if (f.isDirectory()) {
            }
            //if we reached here, the File object f was not 
            // a directory 
            //create a FileInputStream on top of f 
            FileInputStream fis = new FileInputStream(f);
            // create a new zip entry 
            ZipEntry anEntry = new ZipEntry(f.getName()/* f.getPath()*/);

            //place the zip entry in the ZipOutputStream object 
            zos.putNextEntry(anEntry);
            //now write the content of the file to the ZipOutputStream 
            while ((bytesIn = fis.read(readBuffer)) != -1) {
                zos.write(readBuffer, 0, bytesIn);
            }
            //close the Stream 
            fis.close();

        } catch (Exception e) {
            //handle exception 
        }

        //**
    }

    public static void main(String[] args) {

        try {
            extract("c:\\grr.zip", "d:\\oxo\\");
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

}
