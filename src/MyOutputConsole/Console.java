/*
 * Console.java
 *
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package MyOutputConsole;

import InetGameControler.IC;
import InetGameControler.PlayerThread;
import InetGameControler.ServerSinglePlayerThread;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author Jirka
 */
public class Console {

    private final long minTime;
    private final long maxTime;
    private final ArrayList<ConsoleItem> consoleItems;
    private final ArrayList<String> history = new ArrayList<String>();
    private boolean paused = false;
    private long timepaused = 0;
    private ArrayList<ServerSinglePlayerThread> shutedUpList;
    private PlayerThread shutedUpThread;
    private PlayerThread out = null;
    private ArrayList<ServerSinglePlayerThread> serverThreads = null;

    public void setOut(PlayerThread out) {
        this.out = out;
        this.shutedUpThread = out;
    }

    public void savehystory(File f, boolean hystori) {
        if (hystori) {
            allToHistory();
        }
        BufferedWriter write = null;
        try {

            write = new BufferedWriter(new FileWriter(f));

            for (String history1 : history) {
                write.write(history1 + "\r\n");
            }

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

    public void LoadHystory(File f) {
        consoleItems.clear();
        history.clear();

        BufferedReader read = null;
        try {

            read = new BufferedReader(new FileReader(f));
            String a = read.readLine();
            while (a != null) {
                history.add(a);
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

    public void pause() {
        timepaused = System.currentTimeMillis();
        paused = true;
    }

    public void unPause() {///jakto ze se neodpauzoava???
        long diff = System.currentTimeMillis() - timepaused;
        for (ConsoleItem consoleItem : consoleItems) {
            consoleItem.setTime(consoleItem.getTime() + diff);
        }
        paused = false;
    }

    public boolean isPaused() {
        return paused;
    }

    public ArrayList<ConsoleItem> getConsoleItems() {
        return consoleItems;
    }

    /*
     * Creates a new instance of Console
     */
    public Console(long minTime, long maxTime) {

        this.minTime = minTime;
        this.maxTime = maxTime;
        consoleItems = new ArrayList<ConsoleItem>();

    }

    public Console(long minTime, long maxTime, PlayerThread out) {

        this.minTime = minTime;
        this.maxTime = maxTime;
        consoleItems = new ArrayList<ConsoleItem>();
        this.out = out;

    }

    public void addItem(String value) {

        long time = System.currentTimeMillis();
        if (out != null) {
            out.messageToServer(IC.commands[3] + "-" + value);
        }
        if (serverThreads != null) {
            for (ServerSinglePlayerThread elem : serverThreads) {
                elem.messageToPlayer(IC.commands[3] + "-" + value);
            }
        }
        if (consoleItems.size() > 0) {
            long lastTime = this.consoleItems.get(consoleItems.size() - 1).getTime();
            if (time <= lastTime) {
                time = lastTime;
            }
            if (time <= lastTime + minTime) {
                time = lastTime + minTime;
            }
            consoleItems.add(new ConsoleItem(time, value));
        } else {
            consoleItems.add(new ConsoleItem(time + maxTime, value));
        }
    }

    public void addItemSilent(String value) {
        ArrayList<ServerSinglePlayerThread> b = serverThreads;
        PlayerThread a = out;
        out = null;
        serverThreads = null;
        addItem(value);
        out = a;
        serverThreads = b;

    }

    public String getConsoleItemText(int x) {
        String navrat = "";
        if (x >= 0 && x < getConsoleItems().size()) {
            if (getConsoleItem(x) != null) {
                navrat = getConsoleItem(x).getString();
            }
        }
        return navrat;
    }

    public ConsoleItem getConsoleItem(int x) {
        ConsoleItem navrat = null;
        if (x >= 0 && x < getConsoleItems().size()) {
            if (getConsoleItems().get(x) != null) {
                navrat = getConsoleItems().get(x);
            }
        }
        return navrat;
    }

    public ConsoleItem getConsoleItem(String text) {

        for (int x = 0; x < consoleItems.size(); x++) {
            if (getConsoleItem(x) != null) {
                if (getConsoleItem(x).getString().equals(text)) {
                    return getConsoleItem(x);
                }
            }
        }

        return null;
    }

    public boolean updateItem(int x, String text) {
        ConsoleItem a = getConsoleItem(x);
        if (a == null) {
            return false;
        }
        long time = System.currentTimeMillis();
        a.sertString(text);
        for (int i = x; i < consoleItems.size(); i++) {
            a = getConsoleItem(i);
            time += minTime;
            a.setTime(time);
        }
        return true;
    }

    public boolean updateItem(String key, String text) {
        int index = -1;
        for (int x = consoleItems.size() - 1; x >= 0; x--) {
            if (getConsoleItem(x) != null) {
                if (getConsoleItem(x).getString().equals(key)) {
                    index = x;
                    break;
                }
            }
        }
        if (index == -1) {
            return false;
        }
        return updateItem(index, text);

    }

    public void checkDeletions() {
        if (!paused) {
            for (int x = 0; x < consoleItems.size(); x++) {
                if (System.currentTimeMillis() < consoleItems.get(x).getTime()) {
                    break;
                } else {
                    history.add(consoleItems.get(x).getString());
                    consoleItems.remove(x);
                    x--;
                }
            }
        }

    }

    public void allToHistory() {
        for (ConsoleItem consoleItem : consoleItems) {
            history.add(consoleItem.getString());
        }
        consoleItems.clear();

    }

    public void recycleItem() {
        int s = history.size();
        if (s == 0) {
            return;
        }
        addItemSilent(history.get(s - 1));
        history.remove(s - 1);

    }

    public void setMultyple(ArrayList<ServerSinglePlayerThread> serverThreads) {
        this.serverThreads = serverThreads;
        this.shutedUpList = serverThreads;
    }

    public void shutUp(boolean b) {
        if (b) {
            shutedUpList = serverThreads;
            shutedUpThread = out;
            serverThreads = null;
            out = null;
        } else {
            serverThreads = shutedUpList;
            out = shutedUpThread;
        }

    }

}
