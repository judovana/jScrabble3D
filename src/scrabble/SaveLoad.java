/*
 * SaveLoad.java
 *
 * Created on 17. duben 2007, 11:28
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package scrabble;

import Cammons.zip;
import LetterPackage.LetterSet;
import MyOutputConsole.Console;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import scrabble.Playerspackage.Player;
import scrabble.Settings.Settings;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipOutputStream;
import scrabble.GameDesktopPackage.GameDesktop;
import scrabble.GameLogicPackage.GameLogic;

/**
 *
 * @author Jirka
 */
public class SaveLoad {

    /**
     * Creates a new instance of SaveLoad
     */
    public SaveLoad() {
    }

    /*save***
     *hystorie console
     *players
     *settings
     *gamedesktop
     *hraje - index
     *packet
     */
    public static void SaveGame(String kam, Console console, Settings settings, GameDesktop gameDesktop, GameLogic gameLogic, LetterSet letterSet, Player hraje, BufferedImage ss) {
        SaveGame(kam, console, settings, gameDesktop, gameLogic, letterSet, hraje, ss, true);
    }

    private static void SaveGame(String kam, Console console, Settings settings, GameDesktop gameDesktop, GameLogic gameLogic, LetterSet letterSet, Player hraje, BufferedImage ss, boolean hystori) {
        try {
            String s = System.getProperty("java.io.tmpdir") + File.separator;
            File consoleF = new File(s + "consolesave.txt");
            console.savehystory(consoleF, hystori);
            File settingsF = new File(s + "settingssave.xml");
            settings.saveSettings(settingsF);
            File playersF = new File(s + "playerssave.xml");
            gameLogic.savePlayers(playersF, hraje);
            File desktopF = new File(s + "desktopsave.xml");
            gameDesktop.saveDesktop(desktopF);
            File packetF = new File(s + "packetsave.xml");
            letterSet.savePocket(packetF);
            File ssF = null;
            if (ss != null) {
                ssF = new File(s + "ss.jpg");
                ImageIO.write(ss, "jpg", ssF);
            }
            File f = new File(kam);
            ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(f));
            zip.zipFile(consoleF, zos);
            zos.flush();
            zip.zipFile(settingsF, zos);
            zos.flush();
            zip.zipFile(playersF, zos);
            zos.flush();
            zip.zipFile(desktopF, zos);
            zos.flush();
            zip.zipFile(packetF, zos);
            zos.flush();
            if (ss != null) {
                zip.zipFile(ssF, zos);
                zos.flush();
            }
            zos.close();

            consoleF.delete();
            settingsF.delete();
            playersF.delete();
            packetF.delete();
            desktopF.delete();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static Player LoadGame(String odkud, Console console, GameDesktop gameDesktop, GameLogic gameLogic, LetterSet letterSet) {

        String s = System.getProperty("java.io.tmpdir") + File.separator;

        Player vysledek = null;

        try {
            zip.extract(odkud, s);
            File consoleF = new File(s + "consolesave.txt");
            console.LoadHystory(consoleF);
            //File settingsF = new File(s+"settingssave.xml");        
            //settings.Load(settingsF);
            File playersF = new File(s + "playerssave.xml");
            vysledek = gameLogic.LoadPlayers(playersF, letterSet);
            File desktopF = new File(s + "desktopsave.xml");
            gameDesktop.Update(desktopF, letterSet);
            File packetF = new File(s + "packetsave.xml");
            letterSet.Update(packetF);

        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return vysledek;
    }

    public static Player UpdateGame(String odkud, Console console, GameDesktop gameDesktop, GameLogic gameLogic, LetterSet letterSet) {

        String s = System.getProperty("java.io.tmpdir") + File.separator;

        Player vysledek = null;

        try {
            zip.extract(odkud, s);
            File consoleF = new File(s + "consolesave.txt");
            console.LoadHystory(consoleF);
            //File settingsF = new File(s+"settingssave.xml");        
            //settings.Load(settingsF);
            File playersF = new File(s + "playerssave.xml");
            vysledek = gameLogic.UpdatePlayers(playersF, letterSet);
            File desktopF = new File(s + "desktopsave.xml");
            gameDesktop.Update(desktopF, letterSet);
            File packetF = new File(s + "packetsave.xml");
            letterSet.Update(packetF);

        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return vysledek;
    }

    public static Settings LoadGameSettings(String odkud) {

        String s = System.getProperty("java.io.tmpdir") + File.separator;

        Settings st = null;

        try {
            zip.extract(odkud, s);

            File settingsF = new File(s + "settingssave.xml");
            st = Settings.Load(settingsF);

        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return st;
    }

    public static BufferedImage LoadGamePicture(String odkud) {
        if (odkud == null) {
            return null;
        }
        String s = System.getProperty("java.io.tmpdir") + File.separator;

        BufferedImage st = null;
        File imageF = new File(s + "ss.jpg");
        if (imageF.exists()) {
            imageF.delete();
        }

        try {
            zip.extract(odkud, s);
            if (imageF.exists()) {
                return ImageIO.read(imageF);
            } else {
                return null;
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return st;
    }

    public static void AutoSaveGame(int turn, Console console, Settings settings, GameDesktop gameDesktop, GameLogic gameLogic, LetterSet letterSet, Player hraje) {
        String kam = "saves" + File.separator + "autosaves" + File.separator + "as_" + String.valueOf(turn) + "_scr.zip";
        SaveGame(kam, console, settings, gameDesktop, gameLogic, letterSet, hraje, null, false);
    }

    public static Player AutooLoadGame(int turn, Console console, GameDesktop gameDesktop, GameLogic gameLogic, LetterSet letterSet) {

        String odkud = "saves" + File.separator + "autosaves" + File.separator + "as_" + String.valueOf(turn) + "_scr.zip";

        return UpdateGame(odkud, console, gameDesktop, gameLogic, letterSet);
    }

}
