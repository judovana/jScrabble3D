/*
 * Cammons.java
 *
 * Created on 22. duben 2007, 9:37
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package InetGameControler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 *
 * @author Jirka
 */
public class InetCammons {

    /**
     * Creates a new instance of Cammons
     */
    public InetCammons() {
    }

    public static void sendBytes(byte[] byteArray, OutputStream os) throws IOException {

        System.out.println("server> sending bytes:");
        byte[] mybytearray = byteArray;

        System.out.println("Sending...");
        os.write(mybytearray, 0, mybytearray.length);
        os.flush();
        System.out.println("sended");

    }

    public static String setLength(String s, int l) {
        while (s.length() < l) {
            s = s + " ";
        }
        return s;
    }

    public static byte[] reciveByteMessage(int delka, InputStream is) throws IOException {
        System.out.println("expected" + delka);
        int filesize = delka; // filesize temporary hardcoded
        long start = System.currentTimeMillis();
        int bytesRead;

        int current = 0;

        // received file
        byte[] mybytearray = new byte[filesize];
    //InputStream is = sock.getInputStream();

        bytesRead = is.read(mybytearray, 0, mybytearray.length);
        System.out.println("bytes " + bytesRead);
        current = bytesRead;
        //if (bytesRead>0 ) neconacteno=true;
        while (current < filesize && bytesRead > -1) {
            System.out.println("bytes " + bytesRead);
            bytesRead
                    = is.read(mybytearray, current, (mybytearray.length - current));
            current += bytesRead;
            //if (bytesRead>0 ) neconacteno=true;
        }

        long end = System.currentTimeMillis();
        System.out.println("time " + String.valueOf(end - start));

        //sock.close();
        return mybytearray;
    }

}
