/*
 * Iterations.java
 *
 * Created on 9. duben 2007, 12:40
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package scrabble.ScrabbleAI;

import Cammons.Cammons;
import InetGameControler.IC;
import InetGameControler.ServerSinglePlayerThread;
import LetterPackage.SimpleLetter;
import LetterPackage.LetterSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import scrabble.Settings.Settings;

/**
 *
 * @author Jirka
 */
abstract class Iterations {

    protected Settings settings;
    private final ArrayList<AIresult> distributedResults = new ArrayList();
    protected AIresult vysledek;

    /**
     * Creates a new instance of Iterations
     */
    public Iterations(Settings settings) {
        this.settings = settings;
        //ml=settings.ints[settings.maxcheckedlength];
    }

    public void addDistributedresult(String[] c, LetterSet letterSet) {
        AIresult x = new AIresult();
        x.value = Integer.parseInt(c[1]);
        x.x = Integer.parseInt(c[2]);
        x.y = Integer.parseInt(c[3]);
        x.z = Integer.parseInt(c[4]);
        x.horiz = Integer.parseInt(c[5]);

        try {
            for (int i = 6; i < c.length; i++) {
             //A[5, 4, 3]

                x.slovo.add(letterSet.parsePalcedLetter(c[i]));

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        distributedResults.add(x);
    }

    protected void permutujONE(Object[] pole) {

        if (pole.length <= 0) {
            return;
        }
        for (Object pole1 : pole) {
            Object[] a = {pole1};
            proceedPermutation(a);
        }

    }

    protected void permutujTWO(Object[] pole) {

        if (pole.length <= 1) {
            return;
        }

        for (int i = 0; i < pole.length; i++) {
            for (int j = 0; j < pole.length; j++) {
                if (i != j) {
                    Object[] a = {pole[i], pole[j]};
                    proceedPermutation(a);
                }
            }
        }

    }

    protected void permutujTHRE(Object[] pole) {

        if (pole.length <= 2) {
            return;
        }
        boolean[] b = new boolean[7];
        nastav(b);

        for (int i = 0; i < pole.length; i++) {
            b[i] = false;
            for (int j = 0; j < pole.length; j++) {
                if (b[j]) {
                    b[j] = false;
                    for (int k = 0; k < pole.length; k++) {
                        if (b[k]) {
                            Object[] a = {pole[i], pole[j], pole[k]};
                            proceedPermutation(a);
                        }
                    }
                    b[j] = true;
                }
            }
            b[i] = true;
        }

    }

    protected void permutujFOUR(Object[] pole) {

        if (pole.length <= 3) {
            return;
        }
        boolean[] b = new boolean[7];
        nastav(b);

        for (int i = 0; i < pole.length; i++) {
            b[i] = false;
            for (int j = 0; j < pole.length; j++) {
                if (b[j]) {
                    b[j] = false;
                    for (int k = 0; k < pole.length; k++) {
                        if (b[k]) {
                            b[k] = false;
                            for (int l = 0; l < pole.length; l++) {
                                if (b[l]) {

                                    Object[] a = {pole[i], pole[j], pole[k], pole[l]};
                                    proceedPermutation(a);
                                }
                            }
                            b[k] = true;
                        }
                    }
                    b[j] = true;
                }
            }
            b[i] = true;
        }
    }

    protected void permutujFIVE(Object[] pole) {

        if (pole.length <= 4) {
            return;
        }
        boolean[] b = new boolean[7];
        nastav(b);

        for (int i = 0; i < pole.length; i++) {
            b[i] = false;
            for (int j = 0; j < pole.length; j++) {
                if (b[j]) {
                    b[j] = false;
                    for (int k = 0; k < pole.length; k++) {
                        if (b[k]) {
                            b[k] = false;
                            for (int l = 0; l < pole.length; l++) {
                                if (b[l]) {
                                    b[l] = false;
                                    for (int m = 0; m < pole.length; m++) {
                                        if (b[m]) {

                                            Object[] a = {pole[i], pole[j], pole[k], pole[l], pole[m]};

                                            proceedPermutation(a);
                                        }
                                    }
                                    b[l] = true;
                                }
                            }
                            b[k] = true;
                        }
                    }
                    b[j] = true;
                }
            }
            b[i] = true;
        }
    }

    protected void permutujSIX(Object[] pole) {

        if (pole.length <= 5) {
            return;
        }
        boolean[] b = new boolean[7];
        nastav(b);

        for (int i = 0; i < pole.length; i++) {
            b[i] = false;
            for (int j = 0; j < pole.length; j++) {
                if (b[j]) {
                    b[j] = false;
                    for (int k = 0; k < pole.length; k++) {
                        if (b[k]) {
                            b[k] = false;
                            for (int l = 0; l < pole.length; l++) {
                                if (b[l]) {
                                    b[l] = false;
                                    for (int m = 0; m < pole.length; m++) {
                                        if (b[m]) {
                                            b[m] = false;
                                            for (int n = 0; n < pole.length; n++) {
                                                if (b[n]) {
                                                    Object[] a = {pole[i], pole[j], pole[k], pole[l], pole[m], pole[n]};
                                                    proceedPermutation(a);
                                                }
                                            }
                                            b[m] = true;
                                        }
                                    }
                                    b[l] = true;
                                }
                            }
                            b[k] = true;
                        }
                    }
                    b[j] = true;
                }
            }
            b[i] = true;
        }
    }

    protected void permutujSEVEN(Object[] pole) {

        if (pole.length <= 6) {
            return;
        }
        boolean[] b = new boolean[7];
        nastav(b);

        for (int i = 0; i < pole.length; i++) {
            b[i] = false;
            for (int j = 0; j < pole.length; j++) {
                if (b[j]) {
                    b[j] = false;
                    for (int k = 0; k < pole.length; k++) {
                        if (b[k]) {
                            b[k] = false;
                            for (int l = 0; l < pole.length; l++) {
                                if (b[l]) {
                                    b[l] = false;
                                    for (int m = 0; m < pole.length; m++) {
                                        if (b[m]) {
                                            b[m] = false;
                                            for (int n = 0; n < pole.length; n++) {
                                                if (b[n]) {
                                                    b[n] = false;
                                                    for (int o = 0; o < pole.length; o++) {
                                                        if (b[o]) {
                                                            Object[] a = {pole[i], pole[j], pole[k], pole[l], pole[m], pole[n], pole[o]};
                                                            proceedPermutation(a);
                                                        }
                                                    }
                                                    b[n] = true;
                                                }
                                            }
                                            b[m] = true;
                                        }
                                    }
                                    b[l] = true;
                                }
                            }
                            b[k] = true;
                        }
                    }
                    b[j] = true;
                }
            }
            b[i] = true;
        }
    }

    private void nastav(boolean[] a) {
        for (int i = 0; i < a.length; i++) {
            a[i] = true;
        }
    }

    abstract void proceedPermutation(Object[] pole);

    protected Object[] denull(Object[] pole) {
        int n = 0;
        for (Object pole1 : pole) {
            if (pole1 != null) {
                n++;
            }
        }
        Object[] p = new Object[n];
        int m = 0;
        for (int i = 0; i < pole.length; i++) {
            if (pole[i] != null) {
                p[i - m] = pole[i];
            } else {
                m++;
            }
        }
        //pole=p; ?  
        return p;
    }

    protected void permutujONEdistributed(Object[] pole, int from, int to) {

        if (pole.length <= 0) {
            return;
        }

        if (from < 0) {
            from = 0;//return;
        }
        if (to >= pole.length) {
            to = pole.length - 1;//return;
        }
        if (to < from) {
            to = from;
        }

        for (int i = from; i < to; i++) {
            Object[] a = {pole[i]};
            proceedPermutation(a);
        }

    }

    protected void permutujTWOdistributed(Object[] pole, int from, int to) {

        if (pole.length <= 1) {
            return;
        }

        if (from < 0) {
            from = 0;//return;
        }
        if (to >= pole.length) {
            to = pole.length - 1;//return;
        }
        if (to < from) {
            to = from;
        }

        for (int i = from; i < to; i++) {
            for (int j = 0; j < pole.length; j++) {
                if (i != j) {
                    Object[] a = {pole[i], pole[j]};
                    proceedPermutation(a);
                }
            }
        }

    }

    protected void permutujTHREdistributed(Object[] pole, int from, int to) {

        if (pole.length <= 2) {
            return;
        }
        boolean[] b = new boolean[7];
        nastav(b);

        if (from < 0) {
            from = 0;//return;
        }
        if (to >= pole.length) {
            to = pole.length - 1;//return;
        }
        if (to < from) {
            to = from;
        }

        for (int i = from; i < to; i++) {
            b[i] = false;
            for (int j = 0; j < pole.length; j++) {
                if (b[j]) {
                    b[j] = false;
                    for (int k = 0; k < pole.length; k++) {
                        if (b[k]) {
                            Object[] a = {pole[i], pole[j], pole[k]};
                            proceedPermutation(a);
                        }
                    }
                    b[j] = true;
                }
            }
            b[i] = true;
        }

    }

    protected void permutujFOURdistributed(Object[] pole, int from, int to) {

        if (pole.length <= 3) {
            return;
        }
        boolean[] b = new boolean[7];
        nastav(b);

        if (from < 0) {
            from = 0;//return;
        }
        if (to >= pole.length) {
            to = pole.length - 1;//return;
        }
        if (to < from) {
            to = from;
        }

        for (int i = from; i < to; i++) {
            b[i] = false;
            for (int j = 0; j < pole.length; j++) {
                if (b[j]) {
                    b[j] = false;
                    for (int k = 0; k < pole.length; k++) {
                        if (b[k]) {
                            b[k] = false;
                            for (int l = 0; l < pole.length; l++) {
                                if (b[l]) {

                                    Object[] a = {pole[i], pole[j], pole[k], pole[l]};
                                    proceedPermutation(a);
                                }
                            }
                            b[k] = true;
                        }
                    }
                    b[j] = true;
                }
            }
            b[i] = true;
        }
    }

    protected void permutujFIVEdistributed(Object[] pole, int from, int to) {

        if (pole.length <= 4) {
            return;
        }
        boolean[] b = new boolean[7];
        nastav(b);

        if (from < 0) {
            from = 0;//return;
        }
        if (to >= pole.length) {
            to = pole.length - 1;//return;
        }
        if (to < from) {
            to = from;
        }

        for (int i = from; i < to; i++) {
            b[i] = false;
            for (int j = 0; j < pole.length; j++) {
                if (b[j]) {
                    b[j] = false;
                    for (int k = 0; k < pole.length; k++) {
                        if (b[k]) {
                            b[k] = false;
                            for (int l = 0; l < pole.length; l++) {
                                if (b[l]) {
                                    b[l] = false;
                                    for (int m = 0; m < pole.length; m++) {
                                        if (b[m]) {

                                            Object[] a = {pole[i], pole[j], pole[k], pole[l], pole[m]};

                                            proceedPermutation(a);
                                        }
                                    }
                                    b[l] = true;
                                }
                            }
                            b[k] = true;
                        }
                    }
                    b[j] = true;
                }
            }
            b[i] = true;
        }
    }

    protected void permutujSIXdistributed(Object[] pole, int from, int to) {

        if (pole.length <= 5) {
            return;
        }
        boolean[] b = new boolean[7];
        nastav(b);

        if (from < 0) {
            from = 0;//return;
        }
        if (to >= pole.length) {
            to = pole.length - 1;//return;
        }
        if (to < from) {
            to = from;
        }

        for (int i = from; i < to; i++) {
            b[i] = false;
            for (int j = 0; j < pole.length; j++) {
                if (b[j]) {
                    b[j] = false;
                    for (int k = 0; k < pole.length; k++) {
                        if (b[k]) {
                            b[k] = false;
                            for (int l = 0; l < pole.length; l++) {
                                if (b[l]) {
                                    b[l] = false;
                                    for (int m = 0; m < pole.length; m++) {
                                        if (b[m]) {
                                            b[m] = false;
                                            for (int n = 0; n < pole.length; n++) {
                                                if (b[n]) {
                                                    Object[] a = {pole[i], pole[j], pole[k], pole[l], pole[m], pole[n]};
                                                    proceedPermutation(a);
                                                }
                                            }
                                            b[m] = true;
                                        }
                                    }
                                    b[l] = true;
                                }
                            }
                            b[k] = true;
                        }
                    }
                    b[j] = true;
                }
            }
            b[i] = true;
        }
    }

    protected void permutujSEVENdistributed(Object[] pole, int from, int to) {

        if (pole.length <= 6) {
            return;
        }
        boolean[] b = new boolean[7];
        nastav(b);

        if (from < 0) {
            from = 0;//return;
        }
        if (to >= pole.length) {
            to = pole.length - 1;//return;
        }
        if (to < from) {
            to = from;
        }

        for (int i = from; i < to; i++) {
            b[i] = false;
            for (int j = 0; j < pole.length; j++) {
                if (b[j]) {
                    b[j] = false;
                    for (int k = 0; k < pole.length; k++) {
                        if (b[k]) {
                            b[k] = false;
                            for (int l = 0; l < pole.length; l++) {
                                if (b[l]) {
                                    b[l] = false;
                                    for (int m = 0; m < pole.length; m++) {
                                        if (b[m]) {
                                            b[m] = false;
                                            for (int n = 0; n < pole.length; n++) {
                                                if (b[n]) {
                                                    b[n] = false;
                                                    for (int o = 0; o < pole.length; o++) {
                                                        if (b[o]) {
                                                            Object[] a = {pole[i], pole[j], pole[k], pole[l], pole[m], pole[n], pole[o]};
                                                            proceedPermutation(a);
                                                        }
                                                    }
                                                    b[n] = true;
                                                }
                                            }
                                            b[m] = true;
                                        }
                                    }
                                    b[l] = true;
                                }
                            }
                            b[k] = true;
                        }
                    }
                    b[j] = true;
                }
            }
            b[i] = true;
        }
    }

    protected int[] distribuce(int kolika) {
        if (kolika < 1) {
            return null;
        }
        switch (kolika) {
            case 1:
                int[] a = {1, 7};
                return a;
            case 2:
                int[] aa = {1, 4, 7};
                return aa;
            case 3:
                int[] aaa = {1, 3, 5, 7};
                return aaa;
            case 4:
                int[] aaaa = {1, 2, 4, 6, 7};
                return aaaa;
            case 5:
                int[] aaaaa = {1, 2, 4, 5, 6, 7};
                return aaaaa;
            case 6:
                int[] aaaaaa = {1, 2, 3, 4, 5, 6, 7};
                return aaaaaa;
            default:
                int[] d = {1, 1, 2, 3, 4, 5, 6, 7};
                return d;
        }
    }

    protected int[] getMeze(int kolika, int kolikaty) {
        if (kolikaty < 1 || kolikaty > kolika) {
            return null;
        }
        int[] p = distribuce(kolika);
        if (p == null) {
            return null;
        }
        int[] r = new int[2];
        /*switch (kolika){
         case 1:
         if (kolikaty==1) return p; else return null;
         case 2:
         switch (kolikaty){
         case 1 : r[0]=p[0];r[1]=p[1];break;
         case 2 : r[0]=p[1]+1;r[1]=p[2];break;
         default: r=null;
         }
         break;
         case 3:
         switch (kolikaty){
         case 1 : r[0]=p[0];r[1]=p[1];break;
         case 2 : r[0]=p[1]+1;r[1]=p[2];break;
         case 3 : r[0]=p[2]+1;r[1]=p[3];break;
         default: r=null;
         }
         break;
          
         case 4:
         switch (kolikaty){
         case 1 : r[0]=p[0];r[1]=p[1];break;
         case 2 : r[0]=p[1]+1;r[1]=p[2];break;
         case 3 : r[0]=p[2]+1;r[1]=p[3];break;
         case 4 : r[0]=p[3]+1;r[1]=p[4];break;
         default: r=null;
         }
         break;
           
         case 5:
         switch (kolikaty){
         case 1 : r[0]=p[0];r[1]=p[1];break;
         case 2 : r[0]=p[1]+1;r[1]=p[2];break;
         case 3 : r[0]=p[2]+1;r[1]=p[3];break;
         case 4 : r[0]=p[3]+1;r[1]=p[4];break;
         case 5 : r[0]=p[4]+1;r[1]=p[5];break;
         default: r=null;
         }
         break;
         case 6:
         switch (kolikaty){
         case 1 : r[0]=p[0];r[1]=p[1];break;
         case 2 : r[0]=p[1]+1;r[1]=p[2];break;
         case 3 : r[0]=p[2]+1;r[1]=p[3];break;
         case 4 : r[0]=p[3]+1;r[1]=p[4];break;
         case 5 : r[0]=p[4]+1;r[1]=p[5];break;
         case 6 : r[0]=p[5]+1;r[1]=p[6];break;
         default: r=null;
         }
         break;
           
         default:
         switch (kolikaty){
         case 1 : r[0]=p[0];r[1]=p[1];break;
         case 2 : r[0]=p[1]+1;r[1]=p[2];break;
         case 3 : r[0]=p[2]+1;r[1]=p[3];break;
         case 4 : r[0]=p[3]+1;r[1]=p[4];break;
         case 5 : r[0]=p[4]+1;r[1]=p[5];break;
         case 6 : r[0]=p[5]+1;r[1]=p[6];break;
         case 7 : r[0]=p[5]+1;r[1]=p[7];break;
         default: r=null;
         }
         break;       
         }
         */
        if (kolikaty == 1) {
            r[0] = p[0];
            r[1] = p[1];
        } else {
            r[0] = p[kolikaty - 1] + 1;
            r[1] = p[kolikaty];
        }
        return r;

    }

    public void permutujDistributed(int level, ArrayList letters, ArrayList<ServerSinglePlayerThread> distibuted) {
        final ArrayList<ServerSinglePlayerThread> distributed = distibuted;
        distributedResults.clear();
        //vsem poslat  co chci (stupen permutace,odkolika,dokolika,z ceho) pomoci getMeze...
        String pismenka = "";
        for (Object letter : letters) {
            if (letter != null) {
                pismenka += letter;
            }
        }
        for (int x = 0; x < distibuted.size(); x++) {
            int[] p = getMeze(distibuted.size() + 1, x + 2);
            String zprava = IC.commands[20]
                    + "-" + level
                    + "-" + p[0]
                    + "-" + p[1]
                    + "-" + pismenka;
            distibuted.get(x).messageToPlayer(zprava);
        }
        //sam si zapermutovat
        int[] p = getMeze(distibuted.size() + 1, 1);
        switch (level) {
            case 7:
                permutujSEVENdistributed(denull(letters.toArray()), p[0], p[1]);
                break;
            case 6:
                permutujSIXdistributed(denull(letters.toArray()), p[0], p[1]);
                break;
            case 5:
                permutujFIVEdistributed(denull(letters.toArray()), p[0], p[1]);
                break;
            case 4:
                permutujFOURdistributed(denull(letters.toArray()), p[0], p[1]);
                break;
            case 3:
                permutujTHREdistributed(denull(letters.toArray()), p[0], p[1]);
                break;
            case 2:
                permutujTWOdistributed(denull(letters.toArray()), p[0], p[1]);
                break;
            case 1:
                permutujONEdistributed(denull(letters.toArray()), p[0], p[1]);
                break;
        }
            //od vsech precist

        Thread wait = new Thread() {
            @Override
            public void run() {
                for (ServerSinglePlayerThread elem : distributed) {
                    elem.SynchornousReadln();
                }
            }
        };
        wait.start();

        while (distributedResults.size() < distibuted.size()) {
            try {
                Thread.sleep(200);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
        //rozhodnout ktery vzit
        distributedResults.add(vysledek);
        Collections.sort(distributedResults);
        Collections.reverse(distributedResults);
        vysledek = distributedResults.get(0);
    }

    public String giveMePermutation(String[] c, LetterSet l) {
        vysledek = new AIresult();
        int level = Integer.parseInt(c[1]);
        int from = Integer.parseInt(c[2]);
        int to = Integer.parseInt(c[3]);
        String letters = "";
        if (c.length >= 5) {
            letters = c[4];
        }
        SimpleLetter[] pole = new SimpleLetter[letters.length()];
        for (int i = 0; i < letters.length(); i++) {
            pole[i] = l.createLetter(letters.substring(i, i + 1));
        }
        if (pole.length > 0) {
            vysledek = new AIresult();
            switch (level) {
                case 1:
                    if (settings.ints[settings.maxcheckedlength] > 0) {
                        permutujONEdistributed(denull(pole), from, to);
                    }
                    break;
                case 2:
                    if (settings.ints[settings.maxcheckedlength] > 1) {
                        permutujTWOdistributed(denull(pole), from, to);
                    }
                    break;
                case 3:
                    if (settings.ints[settings.maxcheckedlength] > 2) {
                        permutujTHREdistributed(denull(pole), from, to);
                    }
                    break;
                case 4:
                    if (settings.ints[settings.maxcheckedlength] > 3) {
                        permutujFOURdistributed(denull(pole), from, to);
                    }
                    break;
                case 5:
                    if (settings.ints[settings.maxcheckedlength] > 4) {
                        permutujFIVEdistributed(denull(pole), from, to);
                    }
                    break;
                case 6:
                    if (settings.ints[settings.maxcheckedlength] > 5) {
                        permutujSIXdistributed(denull(pole), from, to);
                    }
                    break;
                case 7:
                    if (settings.ints[settings.maxcheckedlength] > 6) {
                        permutujSEVENdistributed(denull(pole), from, to);
                    }
                    break;

            }
        }
        String s = IC.commands[21]
                + "-" + vysledek.value
                + "-" + vysledek.x
                + "-" + vysledek.y
                + "-" + vysledek.z
                + "-" + vysledek.horiz
                + "" + Cammons.vytvorZpravuOTahu(vysledek.slovo);

        return s;
    }

}
