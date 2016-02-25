/*
 * JoinedPlayer.java
 *
 * Created on 23. duben 2007, 10:19
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package InetGameControler;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

/**
 *
 * @author Jirka
 */
public class JoinedPlayer {

    private final String name;
    private final InetAddress ip;
    private final BufferedWriter out;
    private final BufferedReader in;

    private final Socket socket;

    public BufferedReader getIn() {
        return in;
    }

    public InetAddress getIp() {
        return ip;
    }

    public String getName() {
        return name;
    }

    public BufferedWriter getOut() {
        return out;
    }

    public Socket getSocket() {
        return socket;
    }

    /* Creates a new instance of JoinedPlayer */
    public JoinedPlayer(Socket socket, BufferedReader in, BufferedWriter out, String name) {
        this.socket = socket;
        this.ip = socket.getInetAddress();
        this.name = name;
        /*  try {
         in = new BufferedReader(
         new InputStreamReader(
         socket.getInputStream()));
      
         out = new BufferedWriter(
         new OutputStreamWriter(
         socket.getOutputStream()));
         } catch (IOException ex) {
         ex.printStackTrace();
         }*/
        this.in = in;
        this.out = out;

    }

    @Override
    public String toString() {
        return name;
    }

    public void messageToServer(String s) {

        try {
            System.out.println("client(untested)-sending: " + s);
            getOut().write(s + "\r\n");
            getOut().flush();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    protected void finalize() throws Throwable {
        getOut().close();
        getIn().close();
        getSocket().close();
    }

}
