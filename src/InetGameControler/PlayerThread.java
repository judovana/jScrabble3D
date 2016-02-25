/*
 * PlayerThread.java
 *
 * Created on 22. duben 2007, 9:29
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package InetGameControler;

import java.io.IOException;
import java.util.ArrayList;
import scrabble.Main;
import scrabble.Playerspackage.Player;

/**
 *
 * @author Jirka
 */
public abstract class PlayerThread extends Thread {

    private Player player;

    private Main api;

    private boolean konec = true;

    private boolean synchornousReadln = false;

    public boolean apiCantContinue = true;

    private boolean startedplayerthread = false;

    private ArrayList<Player> players;

    public ArrayList<Player> getPlayers() {
        return players;
    }

    /**
     * Creates a new instance of PlayerThread
     */
    public PlayerThread() {
        super();
    }

    public PlayerThread(Player player, Main api) {
        super();
        this.player = player;
        this.api = api;

    }

    @Override
    public void run() {
        System.out.println("client thread  for inet game started");
        startedplayerthread = true;
        if (player.getNetwork() == null) {
            konec = false;
        }
        while (konec) {
            try {
                String s = player.getNetwork().getIn().readLine();
                proceedMessage(s.trim());
            } catch (IOException ex) {
                System.out.println("error encoured; termination(1)");
                ex.printStackTrace();
            }

        }
        if (konec) {
            System.out.println("error encoured; termination(2)");
        }
        System.out.println("server for player thread finished");

    }

    public void messageToServer(String s) {
        if (player.getNetwork() != null) {
            try {
                System.out.println("client(" + player.getJmeno() + ")-sending: " + s);
                player.getNetwork().getOut().write(s + "\r\n");
                player.getNetwork().getOut().flush();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public String SynchornousReadln() {
        synchornousReadln = true;
        String s = null;
        try {
            s = player.getNetwork().getIn().readLine();
            proceedMessage(s.trim());
        } catch (IOException ex) {
            System.out.println("error encoured; termination(1)-" + player.getJmeno());
            ex.printStackTrace();
        } finally {
            synchornousReadln = false;
        }
        return s;
    }

    private void proceedMessage(String string) {
        System.out.println("client(" + player.getJmeno() + ")-reading: " + string);
        String[] c = string.split("-");
        if (c.length <= 0) {
            return;
        }

        if (c[0].equalsIgnoreCase(IC.commands[1])) {
            System.out.println("client-command accepted: api can continue");
            apiCantContinue = false;

        }

        if (c[0].equalsIgnoreCase(IC.commands[8])) {
            System.out.println("client-command accepted: kill comunication");
            try {
                player.getNetwork().getOut().close();
                player.getNetwork().getIn().close();
                player.getNetwork().getSocket().close();
                player.setNetwork(null);
            } catch (IOException ex) {
                ex.printStackTrace();
            }

        }

        if (c[0].equalsIgnoreCase(IC.commands[2])) {
            System.out.println("client-command accepted: players specifications");
            players = new ArrayList();
            String[] cc = c[1].split("/");
            for (int i = 0; i < cc.length; i++) {
                String[] ccc = cc[i].split(";");
                Player p = new Player(ccc[0], player.getLetterSet(), Integer.parseInt(ccc[2]), player.getSettings());
                int local = Integer.parseInt(ccc[1]);
                if (local == 1) {
                    p.setLocal(true);
                    p.setNetwork(this.player.getNetwork());
                    this.player = p;
                } else {
                    p.setLocal(false);
                }
                p.setAiEngine(api.getAIengine(p.getAI()));
                players.add(p);

            }
            System.out.println("players ready " + players.size());
        }

        proceedMessage(c);

    }

    public abstract void proceedMessage(String c[]);

    protected void finalize() throws Throwable {
        messageToServer(IC.commands[8]);
        player.getNetwork().finalize();
    }

}
