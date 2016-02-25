/*
 * vocabularyServer.java
 *
 * Created on 4. duben 2007, 18:30
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package requesty;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

/**
 *
 * @author Jirka
 */
public class VocabularyServer {

    String address;

    ArrayList<String> postKeys = new ArrayList<String>();
    ArrayList<String> postValues = new ArrayList<String>();

    ArrayList<String> getKeys = new ArrayList<String>();
    ArrayList<String> getValues = new ArrayList<String>();
    String encoding = "UTF-8";
    int max_word_length = 0;
    int prefered_method = 0;
    String word_found = "";
    String word_notfound = "";
    int prefered_search = 0;

    //<item popis="zda je preferovano hledat na strance retezec symbolizujici nalezeni">
    public static final int VS_FOUND = 10;
    ;
    //<item popis="zda je preferovano hledat na strance retezec symbolizujici nenalezeni">
  public static final int VS_NOTFOUND = 11;
    //<item popis="tento prediakt se nahradi slovem">
    public static final int VS_WORD = 10000;
    //<item popis="pouziti jen post">
    public static final int VS_POST = 100;
    //<item popis="pouziti jen get">
    public static final int VS_GET = 101;
    //<item popis="pouzit post i get v pripade prefered method, pouzit jak hlednai nalezeneho retezce, tak nenalezeneho v pripade word">
    public static final int VS_BOTH = 1000;
    //:)
    public static final int VS_NOTHING = 0;
    public static final int VS_TOLONG = -1;

    private XMLReader xr;

    private ServerLoader handler;

    String info;

    public String getInfo() {
        return info;
    }

    public String getAddress() {
        return address;
    }

    public ArrayList<String> getGetKeys() {
        return getKeys;
    }

    public ArrayList<String> getGetValues() {
        return getValues;
    }

    public int getMax_word_length() {
        return max_word_length;
    }

    public ArrayList<String> getPostKeys() {
        return postKeys;
    }

    public ArrayList<String> getPostValues() {
        return postValues;
    }

    public int getPrefered_method() {
        return prefered_method;
    }

    public int getPrefered_search() {
        return prefered_search;
    }

    public String getWord_found() {
        return word_found;
    }

    public String getWord_notfound() {
        return word_notfound;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setGetKeys(ArrayList<String> getKeys) {
        this.getKeys = getKeys;
    }

    public void setGetValues(ArrayList<String> getValues) {
        this.getValues = getValues;
    }

    public void setMax_word_length(int max_word_length) {
        this.max_word_length = max_word_length;
    }

    public void setPostKeys(ArrayList<String> postKeys) {
        this.postKeys = postKeys;
    }

    public void setPostValues(ArrayList<String> postValues) {
        this.postValues = postValues;
    }

    public void setWord_found(String word_found) {
        this.word_found = word_found;
    }

    public void setPrefered_method(int prefered_method) {
        this.prefered_method = prefered_method;
    }

    public void setPrefered_search(int prefered_search) {
        this.prefered_search = prefered_search;
    }

    public void setWord_notfound(String word_notfound) {
        this.word_notfound = word_notfound;
    }

    public String getEncoding() {
        return encoding;
    }

    public VocabularyServer() {

    }

    /**
     * Creates a new instance of vocabularyServer
     */
    public VocabularyServer(File f) {
        try {

            xr = XMLReaderFactory.createXMLReader();
            handler = new ServerLoader();

            xr.setContentHandler(handler);
            xr.setErrorHandler(handler);

            FileReader r = new FileReader(f);
            System.out.println(f);
            handler.setLoadedRD(this);
            xr.parse(new InputSource(r));

            while (!handler.finished) {

            }
            xr = null;
            r = null;

        } catch (SAXException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    /*public Object clone() throws CloneNotSupportedException{
     // Object a=super.clone();
     VocabularyServer a=new  VocabularyServer();
        
     return a;
     }*/
    public int testWord(String w) {
        if (w.length() > max_word_length) {
            return VS_TOLONG;
        }
        ArrayList<ArrayList<Integer>> v = replace(w);
        try {
            String page = "";
            //nahradit wordy!!
            if (prefered_method == VS_POST) {
                page = Requests.postRequest(address, postKeys.toArray(new String[0]), postValues.toArray(new String[0]), encoding);
            }

            if (prefered_method == VS_GET) {
                page = Requests.getRequest(address, getKeys.toArray(new String[0]), getValues.toArray(new String[0]), encoding);
            }
            if (prefered_method == VS_BOTH) {
                page = Requests.postRequest(Requests.createGetAdress(address, Requests.createGetData(getKeys.toArray(new String[0]), getValues.toArray(new String[0]), encoding)), postKeys.toArray(new String[0]), postValues.toArray(new String[0]), encoding);
            }
            if (page.trim().equalsIgnoreCase("")) {
                return VS_NOTHING;
            }
            if (prefered_search == VS_FOUND || prefered_search == VS_BOTH) {
                if (page.contains(word_found)) {
                    return VS_FOUND;
                }
            }
            if (prefered_search == VS_NOTFOUND || prefered_search == VS_BOTH) {
                if (page.contains(word_notfound)) {
                    return VS_NOTFOUND;
                }
            }
        } finally {
            returnBack(v);
        }

        if (getPrefered_search() == VS_FOUND) {
            return VS_NOTFOUND;
        }
        if (getPrefered_search() == VS_NOTFOUND) {
            return VS_FOUND;
        }
        return VS_NOTHING;
    }

    public void returnBack(ArrayList<ArrayList<Integer>> v) {
        for (int i = 0; i < v.get(0).size(); i++) {
            postKeys.set(v.get(0).get(i).intValue(), "[!word!]");
        }
        for (int i = 0; i < v.get(1).size(); i++) {
            postValues.set(v.get(1).get(i).intValue(), "[!word!]");
        }
        for (int i = 0; i < v.get(2).size(); i++) {
            getKeys.set(v.get(2).get(i).intValue(), "[!word!]");
        }
        for (int i = 0; i < v.get(3).size(); i++) {
            getValues.set(v.get(3).get(i).intValue(), "[!word!]");
        }
    }

    private ArrayList<ArrayList<Integer>> replace(String w) {
        ArrayList<ArrayList<Integer>> vysledek = new ArrayList<ArrayList<Integer>>();
        vysledek.add(replaceInArrayList(postKeys, "[!word!]", w));
        vysledek.add(replaceInArrayList(postValues, "[!word!]", w));
        vysledek.add(replaceInArrayList(getKeys, "[!word!]", w));
        vysledek.add(replaceInArrayList(getValues, "[!word!]", w));
        return vysledek;
    }

    private ArrayList<Integer> replaceInArrayList(ArrayList<String> kde, String co, String cim) {
        ArrayList<Integer> vysledek = new ArrayList<Integer>();
        for (int x = 0; x < kde.size(); x++) {
            if (kde.get(x).equalsIgnoreCase(co)) {
                vysledek.add(x);
                kde.set(x, cim);

            }
        }
        return vysledek;
    }

}
