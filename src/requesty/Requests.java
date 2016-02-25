package requesty;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
/*
 * Main.java
 *
 * Created on 4. duben 2007, 17:48
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

/**
 *
 * @author Jirka
 */
public class Requests {

    /* Creates a new instance of Main */
    public static String postRequest(String adress, String[] postKeys, String[] postValues, String encoding) {
        String vysledek = "";
        System.out.println("request send");

        // Construct data
        String data = createGetData(postKeys, postValues, encoding);
        try {
            URL url;
            url = new URL(adress);

            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            System.out.println(adress);
            if (data.trim().length() > 0) {
                System.out.println(data);
                wr.write(data);
            }

            wr.flush();

            // Get the response
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;

            while ((line = rd.readLine()) != null) {
                // System.out.println(line); // Process line...
                vysledek = vysledek + line;
            }
            wr.close();
            rd.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return vysledek;
    }

    public static String getRequest(String adress, String[] getKeys, String[] getValues, String encoding) {
        String vysledek = "";

        String data = createGetData(getKeys, getValues, encoding);

        vysledek = postRequest(createGetAdress(adress, data), new String[0], new String[0], encoding);
        return vysledek;
    }

    public static String createGetData(String[] getKeys, String[] getValues, String encoding) {
        if (getKeys.length != getKeys.length) {
            return "";
        }
        String data = "";
        try {
            if (getKeys.length > 0) {
                if (encoding.equalsIgnoreCase("NONE")) {
                    data = getKeys[0] + "=" + getValues[0];
                } else {
                    data = URLEncoder.encode(getKeys[0], encoding) + "=" + URLEncoder.encode(getValues[0], encoding);
                }

                for (int x = 1; x < getKeys.length; x++) {
                    if (encoding.equalsIgnoreCase("NONE")) {
                        data += "&" + getKeys[x] + "=" + getValues[x];
                    } else {
                        data += "&" + URLEncoder.encode(getKeys[x], encoding) + "=" + URLEncoder.encode(getValues[x], encoding);
                    }
                }
            }

        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
        }

        return data;

    }

    public static String createGetAdress(String adress, String data) {
        return adress + "?" + data;
    }

    public Requests() {
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String[] keys = new String[1];
        keys[0] = "S";

        String[] values = new String[1];
        values[0] = "slovo";
        String r = postRequest("http://www.mellony.cz/hledani/search.php", keys, values, "UTF-8");
        if (r.contains("je ve slovníku")) {
            System.out.println("ok");
        }

    }

}
