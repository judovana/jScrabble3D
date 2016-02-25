    /*
 * Dictionary.java
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
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.regex.Pattern;
import LetterPackage.LetterSet;

/**
 *
 * @author Jirka
 */
public class MyDictionary {

    Dictionary<String, DictionaryWord> slova;
    File soubor;
    public static final int MODEL_BEGIN = 1;
    public static final int MODEL_END = 2;
    public static final int MODEL_EVERYWHERE = 3;
    public static final int MODEL_REGEX = 4;

    private final LetterSet ls;

    public File getDictionary() {
        return soubor;
    }

    private void nactiObsah(File f, Dictionary<String, DictionaryWord> slova) {
        BufferedReader read = null;

        try {

            read = new BufferedReader(new FileReader(f));

            String a = read.readLine();
            while (a != null) {

                if (a.indexOf(' ') >= 0) {
                    String[] s = a.split(" ");
                    String sw = s[0];
                    String hint = "";
                    for (int x = 1; x < s.length; x++) {
                        hint = hint + " " + s[x];
                    }
                    hint = hint.trim();
                    if (s.length > 1) {
                        slova.put(sw.toUpperCase().trim(), new DictionaryWord(sw.toUpperCase().trim(), hint, ls));
                    } else {
                        slova.put(sw.toUpperCase().trim(), new DictionaryWord(sw.toUpperCase().trim(), "", ls));
                    }
                } else {
                    slova.put(a.toUpperCase().trim(), new DictionaryWord(a.toUpperCase().trim(), "", ls));
                }

                a = read.readLine();

            }

        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {

            try {

                read.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

    }

    /*
     * Creates a new instance of Dictionary
     */
    public MyDictionary(String[] dictionariesfilenames, LetterPackage.LetterSet ls) {
        slova = new Hashtable<String, DictionaryWord>();
        this.ls = ls;
        for (int x = 0; x < dictionariesfilenames.length; x++) {
            File f = new File(dictionariesfilenames[x]);
            if (f.exists()) {

                nactiObsah(f, slova);
                System.out.println("Dictionary loaded " + dictionariesfilenames[x]);

            } else {
                System.out.println("Dictionary not found " + dictionariesfilenames[x]);
            }
        }

    }

    public static int compWordMaxLength(String[] dictionariesfilenames) {

        int maxlength = 0;
        for (String dictionariesfilename : dictionariesfilenames) {
            File f = new File(dictionariesfilename);
            if (f.exists()) {
                maxlength = Math.max(maxlength, onesMaxLength(f));
                System.out.println("Dictionary searched through" + dictionariesfilename);
            } else {
                System.out.println("Dictionary not searched through " + dictionariesfilename);
            }
        }

        return maxlength;
    }

    public boolean isWordInDictionary(String slovo) {
        if (getWordFromDictionary(slovo) == null) {
            return false;
        } else {
            return true;
        }
    }

    public DictionaryWord getWordFromDictionary(String slovo) {
        DictionaryWord navrat = null;

        navrat = this.slova.get(slovo);

        return navrat;

    }

    public void addWord(String slovo) {
        slova.put(slovo.trim(), new DictionaryWord(slovo.trim().toUpperCase(), "", ls));
        saveUserWord(slovo);
    }

    public void addWordWithHint(String slovo, String hint) {
        slova.put(slovo.trim(), new DictionaryWord(slovo.trim().toUpperCase(), hint.trim(), ls));
        saveUserWord(slovo + " " + hint);
    }

    public void saveUserWord(String slovo) {
        BufferedWriter write = null;
        File output = new File("Data" + File.separator + "dictionaries" + File.separator + "user.txt");

        try {

            FileWriter writer = new FileWriter(output, true);

            write = new BufferedWriter(writer);

            slovo = slovo.trim() + "\r\n";
            write.write(slovo, 0, slovo.length());

          //write.append(slovo.subSequence(0,slovo.length())); fuj fuj
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {

                write.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }

        }

    }

    private static int onesMaxLength(File f) {
        BufferedReader read = null;
        int maxlength = 0;

        try {

            read = new BufferedReader(new FileReader(f));

            String a = read.readLine();
            while (a != null) {

                if (a.indexOf(' ') >= 0) {
                    String[] s = a.split(" ");
                    String sw = s[0].trim();
                    maxlength = Math.max(maxlength, sw.length());
               //String hint="";
                    //for (int x=1;x<s.length;x++)hint=hint+" "+s[x];
                    //hint=hint.trim();
                    //if (s.length>1) slova.put(sw.toUpperCase().trim(),new DictionaryWord(sw.toUpperCase().trim(),hint));
                    //else  slova.put(sw.toUpperCase().trim(),new DictionaryWord(sw.toUpperCase().trim(),""));
                } else {
                    //slova.put(a.toUpperCase().trim(),new DictionaryWord(a.toUpperCase().trim(),""));
                    maxlength = Math.max(maxlength, a.trim().length());
                }

                a = read.readLine();

            }

        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {

            try {

                read.close();

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        return maxlength;

    }

    public ArrayList<String> getAll(String slovo, int mode) {
        ArrayList<String> vysledek = new ArrayList<String>();
        Enumeration<String> i = ((Hashtable) slova).keys();

        while (i.hasMoreElements()) {
            String o = (i.nextElement());

            if (o.length() > slovo.length() || mode == MODEL_REGEX) {
                switch (mode) {
                    case MODEL_BEGIN:

                        if (o/*.getValue()*/.toUpperCase().indexOf(slovo) == 0) {
                            vysledek.add(o);
                        }
                        break;

                    case MODEL_END:
                        if (o/*.getValue()*/.toUpperCase().indexOf(slovo) == o.length() - slovo.length()) {
                            vysledek.add(o);
                        }
                        break;

                    case MODEL_EVERYWHERE:
                        if (o/*.getValue()*/.toUpperCase().indexOf(slovo) >= 0) {
                            vysledek.add(o);
                        }
                        break;
                    case MODEL_REGEX:

                        if (Pattern.matches(slovo, o/*.getValue()*/.toUpperCase().subSequence(0, o.length()))) {
                            vysledek.add(o);
                        }
                        break;

                }
            }

        }

        return vysledek;
    }

}
