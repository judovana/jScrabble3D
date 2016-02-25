/*
 * Servers.java
 *
 * Created on 4. duben 2007, 18:44
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package requesty;

import java.io.File;
import java.util.ArrayList;

/**
 *
 * @author Jirka
 */
public class Servers {

    private final ArrayList<VocabularyServer> vs;

    /* Creates a new instance of Servers */
    public Servers(String[] paths) {
        vs = new ArrayList<VocabularyServer>();
        for (String path : paths) {
            File f = new File(path);
            if (f.exists()) {
                vs.add(new VocabularyServer(f));
            } else {
                System.out.println("File " + path + " not found");
            }
        }
    }

    public ArrayList<wordTestResult> testWord(String w) {
        ArrayList<wordTestResult> vysledek = new ArrayList<wordTestResult>();

        for (VocabularyServer v : vs) {
            int r = v.testWord(w);
            vysledek.add(new wordTestResult(r, w, v.getAddress()));
        }
        return vysledek;
    }

    public int getNumOfServers() {
        return vs.size();
    }

    public static void main(String[] args) {
        String[] s = new String[2];
        s[0] = "D:\\skola\\seminarJava\\Scrabble\\Data\\servers\\www.ik.cz.xml";
        s[1] = "D:\\skola\\seminarJava\\Scrabble\\Data\\servers\\www.mellony.cz.xml";

        Servers a = new Servers(s);

        ArrayList<wordTestResult> q0 = a.testWord("slovan");
        ArrayList<wordTestResult> q1 = a.testWord("slovo");
        ArrayList<wordTestResult> q2 = a.testWord("xmsk");
        ArrayList<wordTestResult> q3 = a.testWord("ona");

        for (wordTestResult q01 : q0) {
            System.out.println(q01);
        }
        for (wordTestResult q11 : q1) {
            System.out.println(q11);
        }
        for (wordTestResult q21 : q2) {
            System.out.println(q21);
        }
        for (wordTestResult q31 : q3) {
            System.out.println(q31);
        }
    }
}
