/*
 * wordTestResult.java
 *
 * Created on 5. duben 2007, 22:21
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package requesty;

/**
 *
 * @author Jirka
 */
public class wordTestResult {

    int stav;
    String slovo;
    String server;
    String stavString;

    public String getServer() {
        return server;
    }

    public String getSlovo() {
        return slovo;
    }

    public int getStav() {
        return stav;
    }

    public String getStavString() {
        return stavString;
    }

    /* Creates a new instance of wordTestResult */
    public wordTestResult(int stav, String slovo, String server) {
        this.server = server;
        this.stav = stav;
        this.slovo = slovo;
        switch (stav) {
            case (VocabularyServer.VS_FOUND):
                stavString = "Nalezeno";
                break;
            case (VocabularyServer.VS_NOTFOUND):
                stavString = "Nenalezeno";
                break;
            case (VocabularyServer.VS_TOLONG):
                stavString = "Nezkontolovano(moc dlouhe)";
                break;
            case (VocabularyServer.VS_NOTHING):
                stavString = "error";
                break;
            default:
                stavString = "fatal error";
                break;

        }
    }

    @Override
    public String toString() {
        return slovo + " " + stavString + " (" + server + ")";
    }

}
