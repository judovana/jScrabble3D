/*
 * MySAXApp.java
 *
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package requesty;

/**
 *
 * @author Jirka
 */
import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

public class ServerLoader extends DefaultHandler {

    private String s;
    private VocabularyServer loadedRD;
    public boolean finished = true;
    private final List<String> elementy = new ArrayList<String>();
       // public MainWindow okno=null; 

    public VocabularyServer getLoadedData() {

        return loadedRD;
    }

    public ServerLoader() {
        super();
    }

    public void setLoadedRD(VocabularyServer loadedRD) {
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
            if (elementy.get(elementy.size() - 1).equalsIgnoreCase("address")) {
                loadedRD.address = s;
            } else if (elementy.get(elementy.size() - 1).equalsIgnoreCase("key") && elementy.get(elementy.size() - 2).equalsIgnoreCase("item") && elementy.get(elementy.size() - 3).equalsIgnoreCase("post")) {
                loadedRD.postKeys.add(s);
            } else if (elementy.get(elementy.size() - 1).equalsIgnoreCase("value") && elementy.get(elementy.size() - 2).equalsIgnoreCase("item") && elementy.get(elementy.size() - 3).equalsIgnoreCase("post")) {
                loadedRD.postValues.add(s);
            } else if (elementy.get(elementy.size() - 1).equalsIgnoreCase("key") && elementy.get(elementy.size() - 2).equalsIgnoreCase("item") && elementy.get(elementy.size() - 3).equalsIgnoreCase("get")) {
                loadedRD.getKeys.add(s);
            } else if (elementy.get(elementy.size() - 1).equalsIgnoreCase("value") && elementy.get(elementy.size() - 2).equalsIgnoreCase("item") && elementy.get(elementy.size() - 3).equalsIgnoreCase("get")) {
                loadedRD.getValues.add(s);
            } else if (elementy.get(elementy.size() - 1).equalsIgnoreCase("word_found")) {
                loadedRD.word_found = s;
            } else if (elementy.get(elementy.size() - 1).equalsIgnoreCase("word_notfound")) {
                loadedRD.word_notfound = s;
            } else if (elementy.get(elementy.size() - 1).equalsIgnoreCase("max_word_length")) {
                loadedRD.max_word_length = Integer.parseInt(s);
            } else if (elementy.get(elementy.size() - 1).equalsIgnoreCase("prefered_method")) {
                if (s.equalsIgnoreCase("[!post!]")) {
                    loadedRD.prefered_method = VocabularyServer.VS_POST;
                }
                if (s.equalsIgnoreCase("[!get!]")) {
                    loadedRD.prefered_method = VocabularyServer.VS_GET;
                }
                if (s.equalsIgnoreCase("[!both!]")) {
                    loadedRD.prefered_method = VocabularyServer.VS_BOTH;
                }
            } else if (elementy.get(elementy.size() - 1).equalsIgnoreCase("prefered_search")) {
                if (s.equalsIgnoreCase("[!found!]")) {
                    loadedRD.prefered_search = VocabularyServer.VS_FOUND;
                }
                if (s.equalsIgnoreCase("[!notfound!]")) {
                    loadedRD.prefered_search = VocabularyServer.VS_NOTFOUND;
                }
                if (s.equalsIgnoreCase("[!both!]")) {
                    loadedRD.prefered_search = VocabularyServer.VS_BOTH;
                }
            } else if (elementy.get(elementy.size() - 1).equalsIgnoreCase("info")) {
                loadedRD.info = s;
            }
            if (elementy.get(elementy.size() - 1).equalsIgnoreCase("encoding")) {
                loadedRD.encoding = s;
            }

        }
    }

}
