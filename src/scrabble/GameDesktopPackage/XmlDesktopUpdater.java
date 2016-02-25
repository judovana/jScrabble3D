/*
 * MySAXApp.java
 *
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package scrabble.GameDesktopPackage;

/**
 *
 * @author Jirka
 */
import LetterPackage.*;
import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

public class XmlDesktopUpdater extends DefaultHandler {

    private String s;
    private GameDesktop loadedRD;
    public boolean finished = true;
    private final List<String> elementy = new ArrayList<String>();
    private int nextx;
    private int nexty;
    private final LetterSet letterSet;
    //private SimpleLetter field[][];
    // private ArrayList<PlacedLetter> placedLetters=new ArrayList();
    // public MainWindow okno=null; 

    public GameDesktop getLoadedData() {

        return loadedRD;
    }

    public XmlDesktopUpdater(LetterSet l) {
        super();
        letterSet = l;
    }

    public void setLoadedRD(GameDesktop loadedRD) {
        this.loadedRD = loadedRD;
    }

    ////////////////////////////////////////////////////////////////////
    // Event handlers.
    ////////////////////////////////////////////////////////////////////
    @Override
    public void startDocument() {
        s = new String();
        System.out.println("Start document");
        loadedRD.setPlacedLetters(new ArrayList());
        loadedRD.setSelected(null);
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
        if (name.trim().equalsIgnoreCase("field")) {
            loadedRD.setPole(new SimpleLetter[Integer.parseInt(atts.getValue("w"))][Integer.parseInt(atts.getValue("h"))]);
        }
        if (name.trim().equalsIgnoreCase("placedletter") || name.trim().equalsIgnoreCase("letter")) {
            nextx = Integer.parseInt(atts.getValue("x"));
            nexty = Integer.parseInt(atts.getValue("y"));
        }
        elementy.add(name);

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

            if (elementy.size() == 3 && elementy.get(elementy.size() - 1).equalsIgnoreCase("letter")) {
                loadedRD.setLetterDirect(letterSet.createLetter(s), nextx, nexty);
            }
            if (elementy.size() == 3 && elementy.get(elementy.size() - 1).equalsIgnoreCase("placedletter")) {
                loadedRD.addLeterToBuffer(nextx, nexty, letterSet.createLetter(s));
            }
            if (elementy.size() == 2 && elementy.get(elementy.size() - 1).equalsIgnoreCase("selected")) {
                loadedRD.setSelected(letterSet.createLetter(s));
            }
            if (elementy.size() == 3 && elementy.get(elementy.size() - 1).equalsIgnoreCase("cx")) {
                loadedRD.setCx(Integer.parseInt(s));

            }

            if (elementy.size() == 3 && elementy.get(elementy.size() - 1).equalsIgnoreCase("cy")) {
                loadedRD.setCy(Integer.parseInt(s));

            }

        }

    }
}
