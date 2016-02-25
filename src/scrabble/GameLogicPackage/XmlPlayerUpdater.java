/*
 * MySAXApp.java
 *
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package scrabble.GameLogicPackage;

/**
 *
 * @author Jirka
 */
import LetterPackage.*;
import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;
import scrabble.Playerspackage.Player;

public class XmlPlayerUpdater extends DefaultHandler {

    private String s;
    private ArrayList<Player> loadedRD;
    public boolean finished = true;
    private final List<String> elementy = new ArrayList<String>();
    private int nextx;
    private int nexty;
    private final LetterSet letterSet;
    private int current;
    private int total;
    private int currentid;
    private int letter;

    private int poiner;

    public int getPoiner() {
        return poiner;
    }

    public int getCurrent() {
        return current;
    }

    public ArrayList<Player> getLoadedData() {

        return loadedRD;
    }

    public XmlPlayerUpdater(LetterSet l) {
        super();
        letterSet = l;
    }

    public void setLoadedRD(ArrayList<Player> loadedRD) {
        this.loadedRD = loadedRD;
    }

    ////////////////////////////////////////////////////////////////////
    // Event handlers.
    ////////////////////////////////////////////////////////////////////
    @Override
    public void startDocument() {
        s = new String();
        System.out.println("Start document");
        if (loadedRD == null) {
            loadedRD = new ArrayList();
        }

        finished = false;

    }

    @Override
    public void endDocument() {
        System.out.println("End document");
        //if ((loadedRD.mapimagename!=null)&&(!loadedRD.mapimagename.trim().equals("")))
        //loadedRD.reLoadimage();
        finished = true;

    }

    @Override
    public void startElement(String uri, String name,
            String qName, Attributes atts) {

        s = new String();
	//if ("".equals (uri))
        //System.out.println("Start element: " + qName);
        //else
        //System.out.println("Start element: {" + uri + "}" + name);
        //for (int x=0;x<=atts.getLength()-1;x++)
        //System.out.println("attributes: " + atts.getValue(x));

        //loadedRD.nulanula.addElement(new NulaNulaProperties());
        //  }
        if (name.trim().equalsIgnoreCase("players")) {
            total = Integer.parseInt(atts.getValue("total"));
            current = Integer.parseInt(atts.getValue("current"));
            poiner = Integer.parseInt(atts.getValue("pointer"));
            if (loadedRD.size() < total) {
                for (int i = loadedRD.size(); i < total; i++) {
                    loadedRD.add(new Player(null, null, 0, null));
                }
            }
        }
        if (name.trim().equalsIgnoreCase("player")) {
            currentid = Integer.parseInt(atts.getValue("id"));
        }
        elementy.add(name);

        if (elementy.size() == 3 && elementy.get(elementy.size() - 1).equalsIgnoreCase("letters")) {
            loadedRD.get(currentid).setLetters(new ArrayList());
            loadedRD.get(currentid).getletters().ensureCapacity(7);
            for (int x = 0; x < 7; x++) {
                loadedRD.get(currentid).getletters().add(x, null);
            }
            letter = 0;
        }

        //  if (!elementy.get(0).equals("UnhappyTripFile")) this.endDocument();       
    }

    @Override
    public void endElement(String uri, String name, String qName) {
        //if ("".equals (uri))
        //System.out.println("End element: " + qName);
        //else
        //  System.out.println("End element:   {" + uri + "}" + name);
        sToServer();
        s = "";
        elementy.remove(elementy.size() - 1);
    }

    @Override
    public void characters(char ch[], int start, int length) {
        //System.out.print("Characters:    \"");
        for (int i = start; i < start + length; i++) {
            s = s + ch[i];
            switch (ch[i]) {
                case '\\':
                    //System.out.print("\\\\");
                    break;
                case '"':
                    //System.out.print("\\\"");
                    break;
                case '\n':
                    //System.out.print("\\n");
                    break;
                case '\r':
                    //System.out.print("\\r");
                    break;
                case '\t':
                    //System.out.print("\\t");
                    break;
                default:
                    //System.out.print(ch[i]);
                    break;
            }
        }
        //System.out.print("\"\n");
        s = s.trim();
        //System.out.print(s+"\n");

    }

    private void sToServer() {
        if (!s.equals("")) {

            if (elementy.size() == 3 && elementy.get(elementy.size() - 1).equalsIgnoreCase("name")) {
                loadedRD.get(currentid).setJmeno(s);
            }
            if (elementy.size() == 3 && elementy.get(elementy.size() - 1).equalsIgnoreCase("selected")) {
                loadedRD.get(currentid).setSelected(Integer.parseInt(s));
            }
            if (elementy.size() == 3 && elementy.get(elementy.size() - 1).equalsIgnoreCase("Score")) {
                loadedRD.get(currentid).setScore(Integer.parseInt(s));
            }
            if (elementy.size() == 3 && elementy.get(elementy.size() - 1).equalsIgnoreCase("AI")) {
                loadedRD.get(currentid).setAI(Integer.parseInt(s));
            }
            if (elementy.size() == 3 && elementy.get(elementy.size() - 1).equalsIgnoreCase("jokers")) {
                loadedRD.get(currentid).jokers = Integer.parseInt(s);
            }
            if (elementy.size() == 3 && elementy.get(elementy.size() - 1).equalsIgnoreCase("passes")) {
                loadedRD.get(currentid).setPasses(Integer.parseInt(s));
            }
            if (elementy.size() == 3 && elementy.get(elementy.size() - 1).equalsIgnoreCase("local")) {
                loadedRD.get(currentid).setLocal(Boolean.parseBoolean(s));
            }
            if (elementy.size() == 4 && elementy.get(elementy.size() - 1).equalsIgnoreCase("letter")) {
                SimpleLetter a = letterSet.createLetter(s);
                if (letter >= loadedRD.get(currentid).getletters().size()) {
                    loadedRD.get(currentid).getletters().add(a);
                } else {
                    loadedRD.get(currentid).getletters().set(letter, a);
                }
                letter++;
            }

        }

    }
}
