/*
 * ServerControler.java
 *
 * Created on 20. duben 2007, 20:26
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package InetGameControler;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import javax.swing.DefaultListModel;
import scrabble.Settings.Settings;

/**
 *
 * @author Jirka
 */
public class ServerPlayerCreator extends Thread {

    ServerSocket socket;

    BufferedReader in;
    BufferedWriter out;
    Socket akcepted;
    private final ArrayList<JoinedPlayer> joinedPlayers = new ArrayList();
    private final DefaultListModel outputPlayers;

    private boolean finished = false;

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public ArrayList<JoinedPlayer> getJoinedPlayers() {
        return joinedPlayers;
    }

    private Settings settings;

    /**
     * Creates a new instance of ServerControler
     */
    public ServerPlayerCreator(DefaultListModel outputPlayers, Settings settings) throws IOException {
        this.outputPlayers = outputPlayers;
        this.settings = settings;

        socket = new ServerSocket(8303);

    }

    public void run() {

        try {

            while (!finished) {
                System.out.println("waitting");
                akcepted = socket.accept();
                in = new BufferedReader(
                        new InputStreamReader(
                                akcepted.getInputStream()));
                out = new BufferedWriter(
                        new OutputStreamWriter(
                                akcepted.getOutputStream()));
                try {
                    Thread.sleep(200);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                String line = in.readLine();
                String s[] = line.trim().split("-");
                if (s.length > 0) {
                    if (s[0].equals("sccabbleXOWQ18playernameFGH4463UIO") && s.length > 1) {
                        System.out.println("player trying to join: " + s[1]);
                        boolean jetam = false;
                        //kontrola zdali tam je
                        if (!jetam) {
                            JoinedPlayer jp = new JoinedPlayer(akcepted, in, out, s[1]);
                            joinedPlayers.add(jp);
                            if (outputPlayers != null) {
                                outputPlayers.addElement(jp);
                            }
                            out.write("sccabbleXOWQ18playeracceptedFGH4463UIO-you have been accepted like: " + s[1] + "\r\n");
                            out.flush();
                            try {
                                Thread.sleep(200);
                            } catch (InterruptedException ex) {
                                ex.printStackTrace();
                            }
                            WrappedOutputStream wos = new WrappedOutputStream(akcepted.getOutputStream());
                            String ss = settings.writeSettings();
                            byte[] b = ss.getBytes();
                            wos.write(b, 0, b.length);
                            wos.flush();
                            try {
                                Thread.sleep(200);
                            } catch (InterruptedException ex) {
                                ex.printStackTrace();
                            }
                            wos.close();
                            try {
                                Thread.sleep(200);
                            } catch (InterruptedException ex) {
                                ex.printStackTrace();
                            }
                    //out.flush();

                        } else {
                            out.write("sccabbleXOWQ18playerrefusedFGH4463UIO-you are already signed: " + s[1] + "\r\n");
                            out.flush();
                            try {
                                Thread.sleep(200);
                            } catch (InterruptedException ex) {
                                ex.printStackTrace();
                            }
                        }
                    }

                }

            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {

        }

        System.out.println("finished");

    }

    public static void main(String args[]) {

    }

    protected void finalize() throws Throwable {
//NO!-player must do it      out.close();
//NO!-player must do it      in.close();
//NO! player must do it      akcepted.close();
//??      socket.close();
    }

}
