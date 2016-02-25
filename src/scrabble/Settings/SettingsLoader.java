/*
 * MySAXApp.java
 *
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package scrabble.Settings;

/**
 *
 * @author Jirka
 */
import java.util.ArrayList;
import java.util.List;
import org.xml.sax.SAXException;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

public class SettingsLoader extends DefaultHandler {

    private String s;
    private Settings loadedRD;
    public boolean finished = true;
    private final List<String> elementy = new ArrayList<String>();
    private int dc = 0;
    private int sc = 0;
    // public MainWindow okno=null; 

    public Settings getLoadedData() {

        return loadedRD;
    }

    public SettingsLoader() {
        super();
    }

    public void setLoadedRD(Settings loadedRD) {
        this.loadedRD = loadedRD;
    }

    ////////////////////////////////////////////////////////////////////
    // Event handlers.
    ////////////////////////////////////////////////////////////////////
    @Override
    public void startDocument() {
        s = new String();
        System.out.println("Start document");
        //loadedRD=new VocabularyServer();
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
        //  if (okno!=null) okno.jProgressBar1.setValue(intercounter1+intercounter2);
        if (name.equalsIgnoreCase("dicts")) {
            loadedRD.dictionaries = new String[Integer.parseInt(atts.getValue("l").trim())];
        }
        if (name.equalsIgnoreCase("servers")) {
            loadedRD.servers = new String[Integer.parseInt(atts.getValue("l").trim())];
        }
        elementy.add(name);

        //  if (!elementy.get(0).equals("UnhappyTripFile")) this.endDocument();       
    }

    @Override
    public void endElement(String uri, String name, String qName) throws SAXException {
        //if ("".equals (uri))
        //System.out.println("End element: " + qName);
        //else
        //  System.out.println("End element:   {" + uri + "}" + name);
        sToServer();
        s = "";
        elementy.remove(elementy.size() - 1);
//        if (elementy.size()==0) {
        //          endDocument();

//           throw new SAXException(":(");
        //    }
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
            if (elementy.size() == 4 && elementy.get(elementy.size() - 2).equalsIgnoreCase("bools")) {
                loadedRD.setBoolValue(elementy.get(elementy.size() - 1), Boolean.parseBoolean(s));
            }
            if (elementy.size() == 4 && elementy.get(elementy.size() - 2).equalsIgnoreCase("ints")) {
                loadedRD.setIntValue(elementy.get(elementy.size() - 1), Integer.parseInt(s));
            }
            if (elementy.size() == 4 && elementy.get(elementy.size() - 2).equalsIgnoreCase("strings")) {
                loadedRD.setStrValue(elementy.get(elementy.size() - 1), s);
            }
            if (elementy.size() == 4 && elementy.get(elementy.size() - 2).equalsIgnoreCase("dicts")) {
                loadedRD.dictionaries[dc] = s;
                dc++;
            }

            if (elementy.size() == 4 && elementy.get(elementy.size() - 2).equalsIgnoreCase("servers")) {
                loadedRD.servers[sc] = s;
                sc++;
            }

        }

    }
}
