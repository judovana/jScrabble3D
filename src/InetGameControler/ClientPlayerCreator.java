/*
 * ClientControler.java
 *
 * Created on 20. duben 2007, 20:27
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package InetGameControler;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.swing.DefaultListModel;
import javax.swing.JDialog;
import org.xml.sax.InputSource;
import scrabble.Settings.Settings;
import scrabble.StartUpScrabble;

/**
 *
 * @author Jirka
 */
public class ClientPlayerCreator extends Thread {

    private Socket socket;
    private BufferedReader in;
    private BufferedWriter out;
    private DefaultListModel output;
    private String name;

    private Settings settings;
    public JoinedPlayer joinedPalyer;
    private JDialog blockdialog;
    private StartUpScrabble api;

    public Settings getSettings() {
        return settings;
    }

    /**
     * Creates a new instance of ClientControler
     */
    public ClientPlayerCreator(InetAddress server, DefaultListModel output, String name, javax.swing.JDialog blocker, StartUpScrabble api) {
        this.blockdialog = blocker;
        this.settings = settings;
        this.output = output;
        this.name = name;
        this.api = api;

        try {
            socket = new Socket(server, 8303);
            in = new BufferedReader(
                    new InputStreamReader(
                            socket.getInputStream()));
            out = new BufferedWriter(
                    new OutputStreamWriter(
                            socket.getOutputStream()));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        try {
            Thread.sleep(200);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        joinedPalyer = new JoinedPlayer(socket, in, out, name);

    }

    public void sentIdentification(String jmeno) throws IOException {
        out.write("sccabbleXOWQ18playernameFGH4463UIO-" + jmeno + "\r\n");
        out.flush();
        try {
            Thread.sleep(200);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        //String s=in.readLine();
    }

    public static void main(String args[]) {
    }

    protected void finalize() throws Throwable {
    //??  out.close();
        //??  in.close();

        //?? socket.close();
    }

    public BooleanAndMessage testAnswer() {
        try {

            String line;
            line = in.readLine();

            String s[] = line.trim().split("-");
            if (s.length > 0) {
                if (s[0].equals("sccabbleXOWQ18playeracceptedFGH4463UIO")) {
                    return new BooleanAndMessage(true, s[1]);
                } else if (s[0].equals("sccabbleXOWQ18playerrefusedFGH4463UIO")) {
                    return new BooleanAndMessage(false, s[1]);
                }

            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        try {
            Thread.sleep(200);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        return new BooleanAndMessage(false, "connection error");

    }

    public void run() {
        try {
            output.addElement("connecting to server");
            sentIdentification(name);
            BooleanAndMessage a = testAnswer();
            if (!a.meaning) {
                output.addElement("you were refused: " + a.message);
            } else {
                output.addElement("you were accepted: " + a.message);
                output.addElement("waiting for settings");
                WrappedInputStream wis = new WrappedInputStream(socket.getInputStream());
                InputStream inw = new InputStreamReporter(wis);
                InputSource source = new InputSource(inw);
                this.settings = Settings.Read(source);
                try {
                    Thread.sleep(200);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                wis.close();
                try {
                    Thread.sleep(200);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                output.addElement("settings recieved");
                Settings clon = new Settings();
                try {
                    clon = (Settings) getSettings().clone();
                } catch (CloneNotSupportedException ex) {
                    ex.printStackTrace();
                }
                api.applySettings(clon);
                Settings st = new Settings();
                api.setUpSettings(st);
                int csr = api.compareSetings(st, clon, output);
                if (csr == 0) {
                    api.jLabel21.setText("settings apply sucesfully");
                } else {
                    api.jLabel21.setText(csr + " warnings! Most inportant is letterset and desk.\nNo importance have cursor and consoletimes.\n Consult settings with your server via jabber");
                }

                output.addElement("waiting for signal to start,please wait");
                String s = in.readLine();
                blockdialog.setVisible(false);
            }
        } catch (UnknownHostException ex) {
            ex.printStackTrace();
            output.addElement("connecting error unknownhost");
        } catch (IOException ex) {
            output.addElement("connecting error io");
            ex.printStackTrace();
        }
    }

}
