/*
 * Preparer.java
 *

 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package dictionaries;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author Jirka
 */
public class MyDictionariesPreparer {

    /**
     * Creates a new instance of Preparer
     */
    public MyDictionariesPreparer() {
    }

    //nahradilo /r znakem /r/n
    public static void prepare_sowpods(File input, File output) {

        BufferedReader read = null;
        BufferedWriter write = null;

        try {

            read = new BufferedReader(new FileReader(input));
            write = new BufferedWriter(new FileWriter(output));
            int a = read.read();
            while (a > -1) {

                char c = (char) a;
                String s;
                if (a == (char) 10) {
                    s = "\r\n";
                } else {
                    s = Character.toString(c);
                }
                write.write(s, 0, s.length());
                a = read.read();

            }

        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {

                write.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            try {

                read.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

    }

    //smaze mezery na koncich radku
    public static void prepare_angWordList(File input, File output) {

        BufferedReader read = null;
        BufferedWriter write = null;

        try {

            read = new BufferedReader(new FileReader(input));
            write = new BufferedWriter(new FileWriter(output));
            String s = read.readLine();
            while (s != null) {

                s = s.trim() + "\r\n";
                write.write(s, 0, s.length());
                s = read.readLine();

            }

        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {

                write.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            try {

                read.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

    }

    //nahradi znaky ", "  znakem /r/n
    public static void prepare_blex(File input, File output) {

        BufferedReader read = null;
        BufferedWriter write = null;

        try {

            FileReader fr = new FileReader(input);

            fr.getEncoding();
            read = new BufferedReader(fr);

            FileWriter fw = new FileWriter(output);
            fw.getEncoding();
            write = new BufferedWriter(fw);

            int a = read.read();
            while (a > -1) {

                char c = (char) a;
                String s = "";

                if (a == ',') {
                    a = read.read();
                    s = "\r\n";
                } else {
                    s = Character.toString(c);
                }

                write.write(s, 0, s.length());

                a = read.read();

            }

        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {

                write.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            try {

                read.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

    }

    public static void prepare_skN(File input, File output, int N) {

        BufferedReader read = null;
        BufferedWriter write = null;

        try {

            FileReader fr = new FileReader(input);

            fr.getEncoding();
            read = new BufferedReader(fr);

            FileWriter fw = new FileWriter(output);
            fw.getEncoding();
            write = new BufferedWriter(fw);
            int delkaslova = 0;
            int a = read.read();
            while (a > -1) {

                char c = (char) a;
                String s = "";

                if (a == ' ') {
                    a = read.read();
                    int pm = 1;//pocet mezer
                    while (a == ' ') {
                        pm++;
                        a = read.read();
                    }
                    if (pm <= 2) {
                        s = " " + Character.toString((char) a);
                    } else {
                        s = "\r\n" + Character.toString((char) a);
                        delkaslova = 1;
                    }
                } else {
                    s = Character.toString(c);
                    delkaslova++;
                    if (delkaslova == N) {
                        s = s + " ";
                    }
                }

                write.write(s, 0, s.length());

                a = read.read();

            }

        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {

                write.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            try {

                read.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

    }

    public static void main(String[] args) {
        // TODO code application logic here
        prepare_skN(new File("Data" + File.separator + "dictionaries" + File.separator + "cz-Sk3.txt"), new File("Data" + File.separator + "dictionaries" + File.separator + "GOOD_cz-Sk3.txt"), 3);

    }

}
