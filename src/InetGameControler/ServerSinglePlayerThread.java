
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
import scrabble.Main;
import scrabble.Playerspackage.Player;

/**
 *
 * @author Jirka
 */
public abstract class ServerSinglePlayerThread extends Thread {

    private Player player;

    private Main api;

    private boolean konec = true;

    private boolean synchornousReadln = false;

    public boolean myPlyerIn = false;

    private boolean startedserverthread = false;

    public Player getPlayer() {
        return player;
    }

    /**
     * Creates a new instance of PlayerThread
     */
    public ServerSinglePlayerThread() {
        super();
    }

    public ServerSinglePlayerThread(Player player, Main api) {
        super();
        this.player = player;
        this.api = api;

    }

    public void run() {
        System.out.println("server thread  for inet game started - this is for palyer-" + player.getJmeno());
        startedserverthread = true;
        if (player.getNetwork() == null) {
            konec = false;
        }
        while (konec) {
            try {
                System.out.println("server for " + player.getJmeno() + " waiting");
                String s = player.getNetwork().getIn().readLine();
                proceedMessage(s.trim());
            } catch (IOException ex) {
                System.out.println("error encoured; termination(1)-" + player.getJmeno());
                ex.printStackTrace();
                try {

                    player.getNetwork().finalize();
                } catch (Throwable ex2) {
                    ex2.printStackTrace();
                }

            }

        }
        if (konec) {
            System.out.println("error encoured; termination(2)-" + player.getJmeno());
        }
        System.out.println("server for player thread finished-" + player.getJmeno());

    }

    public void messageToPlayer(String s) {
        if (player.getNetwork() != null) {
            try {
                System.out.println("Server-sending: " + s);
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
        System.out.println("Server-reading: " + string);
        String[] c = string.split("-");
        if (c.length <= 0) {
            return;
        }
        if (c[0].equalsIgnoreCase(IC.commands[0])) {
            System.out.println("Server-command accepted: my player in");
            myPlyerIn = true;
        }

        if (c[0].equalsIgnoreCase(IC.commands[8])) {
            System.out.println("Server-command accepted: kill comunication");
            try {
                player.getNetwork().getOut().close();
                player.getNetwork().getIn().close();
                player.getNetwork().getSocket().close();
                player.setNetwork(null);
            } catch (IOException ex) {
                ex.printStackTrace();
            }

        }
        proceedMessage(c);

    }

    public abstract void proceedMessage(String c[]);

    protected void finalize() throws Throwable {
        messageToPlayer(IC.commands[8]);
        player.getNetwork().finalize();
    }

}
