/*
 * Settings.java
 *
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package scrabble.Settings;

import InetGameControler.JoinedPlayer;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

/**
 *
 * @author Jirka
 */
public class Settings {

    public boolean[] bools = new boolean[21];
    public String[] strings = new String[8];
    public int[] ints = new int[7];
    public static final int wordcheck = 0, playercheck = 1, multipleword = 2, randomizeplayers = 3, inputkonzole = 4, imserver = 5, inetcheck = 6, addtodic = 7, jokerchanging = 8,
            turncheck = 9, skipplayer = 10, lostturnafterjoker = 11, addServerAgreedWordToUsers = 12, aiIgnoreDesk = 13, onlyOnePasedddEnough = 14, aiIgnoreJokers = 15,
            inetgame = 16, swinggame = 17, desk3D = 18, sounds = 19, distributedAi = 20;
    public static final int letterset = 0, desk = 1, password = 2, cursor = 3, words = 4, ipaddress = 5, separator = 6, dysplaymode = 7;
    public static final int minconosletime = 0, maxcconsoletime = 1, maxcheckedlength = 2, emptyuseless = 3, turntime = 4, passestowin = 5, turn = 6;
    public String loadGame = null;
    public String[] dictionaries;
    public String[] servers;
    public ArrayList<JoinedPlayer> joinedPalyers = null;

    @Override
    public Object clone() throws CloneNotSupportedException {
        Settings s = new Settings();
        System.arraycopy(this.bools, 0, s.bools, 0, this.bools.length);
        System.arraycopy(this.ints, 0, s.ints, 0, this.ints.length);
        for (int i = 0; i < this.strings.length; i++) {
            if (this.strings[i] != null) {
                s.strings[i] = this.strings[i];
            } else {
                s.strings[i] = null;
            }
        }
        if (this.dictionaries != null) {
            s.dictionaries = new String[this.dictionaries.length];
            for (int i = 0; i < this.dictionaries.length; i++) {
                if (this.dictionaries[i] != null) {
                    s.dictionaries[i] = this.dictionaries[i];
                } else {
                    s.dictionaries[i] = null;
                }
            }
        }
        if (this.servers != null) {
            s.servers = new String[this.servers.length];
            for (int i = 0; i < this.servers.length; i++) {
                if (this.servers[i] != null) {
                    s.servers[i] = this.servers[i];
                } else {
                    s.servers[i] = null;
                }
            }
        }
        return s;
    }

    public boolean IsServer() {
        return bools[imserver];
    }

    public boolean isInet() {
        return bools[inetgame];
    }

    public void setSeparator(String s) {
        strings[separator] = s;
    }

    public String getSeparator() {
        return strings[separator];
    }

    public ArrayList<String> playersNames = new ArrayList<String>();
    public ArrayList<Integer> playersValues = new ArrayList<Integer>();

    /**
     * Creates a new instance of Settings
     */
    public Settings() {
        setSeparator(File.separator);
        bools[onlyOnePasedddEnough] = false;
        ints[passestowin] = 3;
        ints[turn] = 0;

    }

    public static String parsePlayerName(String record) {
        for (int x = record.length() - 1; x >= 0; x--) {
            if (record.charAt(x) == '[') {
                return record.substring(0, x);
            }
        }
        return record;
    }

    public static String parsePlayerBehaviour(String record) {
        for (int x = record.length() - 1; x >= 0; x--) {
            if (record.charAt(x) == '[') {
                return record.substring(x + 1, record.length() - 1);
            }
        }
        return record;
    }

    public static String parseDictName(String record) {
        for (int x = record.length() - 1; x >= 0; x--) {
            if (record.charAt(x) == '(') {
                return record.substring(0, x).trim();
            }
        }
        return record;
    }

    public static String parseDictWords(String record) {
        for (int x = record.length() - 1; x >= 0; x--) {
            if (record.charAt(x) == '(') {
                return record.substring(x + 1 + "cca".length(), record.length() - 1 - "words".length()).trim();
            }
        }
        return record;
    }

    public int getStrPointer(String key) {
        if (key.trim().equalsIgnoreCase("letterset")) {
            return 0;
        }
        if (key.trim().equalsIgnoreCase("desk")) {
            return 1;
        }
        if (key.trim().equalsIgnoreCase("password")) {
            return 2;
        }
        if (key.trim().equalsIgnoreCase("cursor")) {
            return 3;
        }
        if (key.trim().equalsIgnoreCase("words")) {
            return 4;
        }
        if (key.trim().equalsIgnoreCase("ipaddress")) {
            return 5;
        }
        if (key.trim().equalsIgnoreCase("separator")) {
            return 6;
        }
        if (key.trim().equalsIgnoreCase("dysplaymode")) {
            return 7;
        }
        return -1;

    }

    public String strToString(int i) {
        switch (i) {

            case (0):
                return "letterset";
            case (1):
                return "desk";
            case (2):
                return "password";
            case (3):
                return "cursor";
            case (4):
                return "words";
            case (5):
                return "ipaddress";
            case (6):
                return "separator";
            case (7):
                return "dysplaymode";
            default:
                return "unknown";
        }
    }

    public int getIntPointer(String key) {
        if (key.trim().equalsIgnoreCase("minconosletime")) {
            return 0;
        }
        if (key.trim().equalsIgnoreCase("maxcconsoletime")) {
            return 1;
        }
        if (key.trim().equalsIgnoreCase("maxcheckedlength")) {
            return 2;
        }
        if (key.trim().equalsIgnoreCase("emptyuseless")) {
            return 3;
        }
        if (key.trim().equalsIgnoreCase("turntime")) {
            return 4;
        }
        if (key.trim().equalsIgnoreCase("passestowin")) {
            return 5;
        }
        if (key.trim().equalsIgnoreCase("turn")) {
            return 6;
        }

        return -1;

    }

    public String intToString(int i) {
        switch (i) {

            case (0):
                return "minconosletime";
            case (1):
                return "maxcconsoletime";
            case (2):
                return "maxcheckedlength";
            case (3):
                return "emptyuseless";
            case (4):
                return "turntime";
            case (5):
                return "passestowin";
            case (6):
                return "turn";

            default:
                return "unknown";
        }
    }

    public int getBoolPointer(String key) {
        if (key.trim().equalsIgnoreCase("wordcheck")) {
            return 0;
        }
        if (key.trim().equalsIgnoreCase("playercheck")) {
            return 1;
        }
        if (key.trim().equalsIgnoreCase("multipleword")) {
            return 2;
        }
        if (key.trim().equalsIgnoreCase("randomizeplayers")) {
            return 3;
        }
        if (key.trim().equalsIgnoreCase("inputkonzole")) {
            return 4;
        }
        if (key.trim().equalsIgnoreCase("imserver")) {
            return 5;
        }
        if (key.trim().equalsIgnoreCase("inetcheck")) {
            return 6;
        }
        if (key.trim().equalsIgnoreCase("addtodic")) {
            return 7;
        }
        if (key.trim().equalsIgnoreCase("jokerchanging")) {
            return 8;
        }
        if (key.trim().equalsIgnoreCase("turncheck")) {
            return 9;
        }
        if (key.trim().equalsIgnoreCase("skipplayer")) {
            return 10;
        }
        if (key.trim().equalsIgnoreCase("lostturnafterjoker")) {
            return 11;
        }
        if (key.trim().equalsIgnoreCase("addServerAgreedWordToUsers")) {
            return 12;
        }
        if (key.trim().equalsIgnoreCase("aiIgnoreDesk")) {
            return 13;
        }
        if (key.trim().equalsIgnoreCase("onlyOnePasedddEnough")) {
            return 14;
        }
        if (key.trim().equalsIgnoreCase("aiIgnoreJokers")) {
            return 15;
        }
        if (key.trim().equalsIgnoreCase("inetgame")) {
            return 16;
        }
        if (key.trim().equalsIgnoreCase("swinggame")) {
            return 17;
        }
        if (key.trim().equalsIgnoreCase("desk3D")) {
            return 18;
        }
        if (key.trim().equalsIgnoreCase("sounds")) {
            return 19;
        }
        if (key.trim().equalsIgnoreCase("distributedAi")) {
            return 20;
        }
        return -1;

    }

    public String boolToString(int i) {
        switch (i) {
            case (0):
                return "wordcheck";
            case (1):
                return "playercheck";
            case (2):
                return "multipleword";
            case (3):
                return "randomizeplayers";
            case (4):
                return "inputkonzole";
            case (5):
                return "imserver";
            case (6):
                return "inetcheck";
            case (7):
                return "addtodic";
            case (8):
                return "jokerchanging";
            case (9):
                return "turncheck";
            case (10):
                return "skipplayer";
            case (11):
                return "lostturnafterjoker";
            case (12):
                return "addServerAgreedWordToUsers";
            case (13):
                return "aiIgnoreDesk";
            case (14):
                return "onlyOnePasedddEnough";
            case (15):
                return "aiIgnoreJokers";
            case (16):
                return "inetgame";
            case (17):
                return "swinggame";
            case (18):
                return "desk3D";
            case (19):
                return "sounds";
            case (20):
                return "distributedAi";
            default:
                return "unknown";
        }
    }

    public boolean getBoolValue(String key) {
        int a = getBoolPointer(key);
        if (a > -1) {
            return bools[a];
        } else {
            return false;
        }
    }

    public void setBoolValue(String key, boolean value) {
        int a = getBoolPointer(key);
        if (a > -1) {
            bools[a] = value;
        }

    }

    public int getIntValue(String key) {
        int a = getIntPointer(key);
        if (a > -1) {
            return ints[a];
        } else {
            return -1;
        }
    }

    public void setIntValue(String key, int value) {
        int a = getIntPointer(key);
        if (a > -1) {
            ints[a] = value;
        }

    }

    public String getStrValue(String key) {
        int a = getStrPointer(key);
        if (a > -1) {
            return strings[a];
        } else {
            return null;
        }
    }

    public void setStrValue(String key, String value) {
        int a = getStrPointer(key);
        if (a > -1) {
            strings[a] = value;
        }

    }

    public String writeSettings() {
        setSeparator(File.separator);
        String s = "";
        s += ("<scrabblesettings>\r\n");
        s += ("<basicsettings>\r\n");
        s += ("<bools>\r\n");
        for (int x = 0; x < bools.length; x++) {
            s += ("<" + boolToString(x) + ">\r\n");
            s += (Boolean.toString(bools[x]) + "\r\n");
            s += ("</" + boolToString(x) + ">\r\n");
        }
        s += ("</bools>\r\n");

        s += ("<ints>\r\n");
        for (int x = 0; x < ints.length; x++) {
            s += ("<" + intToString(x) + ">\r\n");
            s += (String.valueOf(ints[x]) + "\r\n");
            s += ("</" + intToString(x) + ">\r\n");
        }
        s += ("</ints>\r\n");

        s += ("<strings>\r\n");
        for (int x = 0; x < strings.length; x++) {
            s += ("<" + strToString(x) + ">\r\n");
            s += (strings[x] + "\r\n");
            s += ("</" + strToString(x) + ">\r\n");
        }
        s += ("</strings>\r\n");

        s += ("</basicsettings>\r\n");

        s += ("<lists>\r\n");

        s += ("<dicts l='" + dictionaries.length + "' >\r\n");
        for (String dictionarie : dictionaries) {
            s += ("<item>\r\n");
            s += (dictionarie + "\r\n");
            s += ("</item>\r\n");
        }

        s += ("</dicts>\r\n");

        s += ("<servers l='" + servers.length + "' > \r\n");
        for (String server : servers) {
            s += ("<item>\r\n");
            s += (server + "\r\n");
            s += ("</item>\r\n");
        }
        s += ("</servers>\r\n");

        s += ("</lists>\r\n");

        s += ("</scrabblesettings>\r\n");
        return s;
    }

    public void writeSettings(Writer write) throws IOException {
        write.write(writeSettings());

    }

    public void saveSettings(File f) {
        BufferedWriter write = null;
        try {

            write = new BufferedWriter(new FileWriter(f));
            writeSettings(write);
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

    public static Settings Load(File f) {
        Settings v = null;
        try {
            FileReader r;
            r = new FileReader(f);
            System.out.println(f);
            v = Read(r);
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }

        return v;
    }

    public static Settings Read(Reader r) {
        return Read(new InputSource(r));
    }

    public static Settings Read(InputSource source) {
        SettingsLoader handler = null;;
        XMLReader xr;
        try {

            xr = XMLReaderFactory.createXMLReader();
            handler = new SettingsLoader();

            xr.setContentHandler(handler);
	//xr.setErrorHandler(handler);

            // FileReader r = new FileReader(f); 
            //System.out.println(f);
            handler.setLoadedRD(new Settings());

            xr.parse(source);

            while (!handler.finished) {

            }
            xr = null;
            //r=null;

        } catch (SAXException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        Settings set = handler.getLoadedData();
        if (!set.getSeparator().equals(File.separator)) {
            if (set.dictionaries != null) {
                for (int i = 0; i < set.dictionaries.length; i++) {
                    if (set.dictionaries[i] != null) {
                        set.dictionaries[i] = set.dictionaries[i].replace((CharSequence) set.getSeparator(), (CharSequence) File.separator);
                    }
                }
            }

            if (set.servers != null) {
                for (int i = 0; i < set.servers.length; i++) {
                    if (set.servers[i] != null) {
                        set.servers[i] = set.servers[i].replace((CharSequence) set.getSeparator(), (CharSequence) File.separator);
                    }
                }
            }

            for (int i = 0; i < set.strings.length; i++) {
                if (set.strings[i] != null) {
                    set.strings[i] = set.strings[i].replace((CharSequence) set.getSeparator(), (CharSequence) File.separator);
                }
            }

            set.setSeparator(File.separator);
        }
        return set;
    }
}
