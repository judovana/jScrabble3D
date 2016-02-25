package scrabble.GameLogicPackage;

import LetterPackage.PlacedLetter;
import OGLaddons.OGLdialog;
import scrabble.DesksPackage.Desks3D;
import scrabble.GameDesktopPackage.GameDesktop3D;
import scrabble.Playerspackage.Player;
import scrabble.Settings.Settings;
import java.util.ArrayList;
import java.util.Collections;
import scrabble.DesksPackage.Desks;
import scrabble.GameDesktopPackage.GameDesktop;
import scrabble.Main;
/*
 * GameLogic.java
 *
 * Created on 12. b�ezen 2007, 16:44
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

/**
 *
 * @author Jirka
 */
public class GameLogic3D extends GameLogic {

    /**
     * Creates a new instance of GameLogic
     *
     * @param api
     */
    public GameLogic3D(Main api) {
        super(api);
    }

    @Override
    protected boolean areHolesInSortedBuffer(ArrayList<PlacedLetter> vector, GameDesktop turn) {
        GameDesktop3D turn3D = (GameDesktop3D) turn;
        if (vector.get(0).getUsedCompare() == PlacedLetter.COMPARE_BY_X) {
            int x = vector.get(0).getX() + 1;
            int y = vector.get(0).getY();
            int z = vector.get(0).getZ();
            while (turn3D.getLetterOnBoardIndependentOnBuffer(x, y, z) != null) {
                x++;
            }
            for (PlacedLetter vector1 : vector) {
                if (vector1.getX() > x) {
                    return true;
                }
            }

        }
        if (vector.get(0).getUsedCompare() == PlacedLetter.COMPARE_BY_Y) {
            int x = vector.get(0).getX();
            int y = vector.get(0).getY() - 1;
            int z = vector.get(0).getZ();
            while (turn3D.getLetterOnBoardIndependentOnBuffer(x, y, z) != null) {
                y--;
            }
            for (PlacedLetter vector1 : vector) {
                if (vector1.getY() < y) {
                    return true;
                }
            }

        }

        if (vector.get(0).getUsedCompare() == PlacedLetter.COMPARE_BY_Z) {
            int x = vector.get(0).getX();
            int y = vector.get(0).getY();
            int z = vector.get(0).getZ() + 1;
            while (turn3D.getLetterOnBoardIndependentOnBuffer(x, y, z) != null) {
                x++;
            }
            for (PlacedLetter vector1 : vector) {
                if (vector1.getZ() > z) {
                    return true;
                }
            }

        }

        return false;

    }

    @Override
    protected boolean areHolesInSortedBufferOfFirstTurn(ArrayList<PlacedLetter> vector) {
        if (vector.get(0).getUsedCompare() == PlacedLetter.COMPARE_BY_X) {
            for (int x = 0; x < vector.size() - 1; x++) {
                if (vector.get(x).getX() + 1 != vector.get(x + 1).getX()) {
                    return true;
                }
            }
        }

        if (vector.get(0).getUsedCompare() == PlacedLetter.COMPARE_BY_Y) {
            for (int x = 0; x < vector.size() - 1; x++) {
                if (vector.get(x).getY() - 1 != vector.get(x + 1).getY()) {
                    return true;
                }
            }

        }

        if (vector.get(0).getUsedCompare() == PlacedLetter.COMPARE_BY_Z) {
            for (int x = 0; x < vector.size() - 1; x++) {
                if (vector.get(x).getZ() - 1 != vector.get(x + 1).getZ()) {
                    return true;
                }
            }

        }

        return false;

    }

    @Override
    protected boolean letterOnStart(ArrayList<PlacedLetter> vector, Desks desk) {
        Desks3D desk3D = (Desks3D) desk;
        for (PlacedLetter vector1 : vector) {
            if ( //je nejake policko na startu?
                    desk3D.getPole3D().get(vector1.getZ())[vector1.getX()][vector1.getY()] == 1) {
                return true;
            }
        }
        return false;

    }

    @Override
    protected boolean allLettersInOneVector(ArrayList<PlacedLetter> vector) {
        boolean ok = true;//jsou vsecky pismenka v jednom vektoru?
        for (int x = 0; x < vector.size() - 1; x++) {
            if (vector.get(x).getX() != vector.get(x + 1).getX() || vector.get(x).getZ() != vector.get(x + 1).getZ()) {
                for (int y = 0; y < vector.size() - 1; y++) {
                    if (vector.get(y).getY() != vector.get(y + 1).getY() || vector.get(y).getZ() != vector.get(y + 1).getZ()) {
                        for (int z = 0; z < vector.size() - 1; z++) {
                            if (vector.get(z).getX() != vector.get(z + 1).getX() || vector.get(z).getY() != vector.get(z + 1).getY()) {
                                return false;
                            }
                        }
                        break;
                    }
                }
                break;
            }
        }
        return ok;
    }

    @Override
    protected boolean letterNearToOneOneBoard(ArrayList<PlacedLetter> vector, GameDesktop turn) {
        GameDesktop3D turn3D = (GameDesktop3D) turn;
        boolean ok = false;//dotyka se alsepon jedno z pismenek nejakeho jiz umisteneho?
        for (PlacedLetter vector1 : vector) {
            if (turn3D.getPolePrvek(vector1.getX() + 1, vector1.getY(), vector1.getZ()) != null || turn3D.getPolePrvek(vector1.getX(), vector1.getY() + 1, vector1.getZ()) != null || turn3D.getPolePrvek(vector1.getX(), vector1.getY() - 1, vector1.getZ()) != null || turn3D.getPolePrvek(vector1.getX() - 1, vector1.getY(), vector1.getZ()) != null || turn3D.getPolePrvek(vector1.getX(), vector1.getY(), vector1.getZ() + 1) != null || turn3D.getPolePrvek(vector1.getX(), vector1.getY(), vector1.getZ() - 1) != null) {
                ok = true;
                break;
            }
        }
        return ok != false;
    }

    @Override
    protected boolean oneLetterNearToOneOneBoard(PlacedLetter v, GameDesktop turn) {
        GameDesktop3D turn3D = (GameDesktop3D) turn;

        return turn3D.getPolePrvek(v.getX() + 1, v.getY(), v.getZ()) != null
                || turn3D.getPolePrvek(v.getX(), v.getY() + 1, v.getZ()) != null
                || turn3D.getPolePrvek(v.getX(), v.getY() - 1, v.getZ()) != null
                || turn3D.getPolePrvek(v.getX() - 1, v.getY(), v.getZ()) != null
                || turn3D.getPolePrvek(v.getX(), v.getY(), v.getZ() + 1) != null
                || turn3D.getPolePrvek(v.getX(), v.getY(), v.getZ() - 1) != null;
    }

    @Override
    protected boolean oneLetterNearToOneOneMarked(PlacedLetter v, ArrayList<PlacedLetter> vector) {

        return ((GameDesktop3D.getAnyNewhereInVector(vector, v.getX() + 1, v.getY(), v.getZ()) != null) && (GameDesktop3D.getAnyNewhereInVector(vector, v.getX() + 1, v.getY(), v.getZ()).mark == true))
                || ((GameDesktop3D.getAnyNewhereInVector(vector, v.getX(), v.getY() + 1, v.getZ()) != null) && (GameDesktop3D.getAnyNewhereInVector(vector, v.getX(), v.getY() + 1, v.getZ()).mark == true))
                || ((GameDesktop3D.getAnyNewhereInVector(vector, v.getX(), v.getY() - 1, v.getZ()) != null) && (GameDesktop3D.getAnyNewhereInVector(vector, v.getX(), v.getY() - 1, v.getZ()).mark == true))
                || ((GameDesktop3D.getAnyNewhereInVector(vector, v.getX() - 1, v.getY(), v.getZ()) != null) && (GameDesktop3D.getAnyNewhereInVector(vector, v.getX() - 1, v.getY(), v.getZ()).mark == true))
                || ((GameDesktop3D.getAnyNewhereInVector(vector, v.getX(), v.getY(), v.getZ() + 1) != null) && (GameDesktop3D.getAnyNewhereInVector(vector, v.getX(), v.getY(), v.getZ() + 1).mark == true))
                || ((GameDesktop3D.getAnyNewhereInVector(vector, v.getX(), v.getY(), v.getZ() - 1) != null) && (GameDesktop3D.getAnyNewhereInVector(vector, v.getX(), v.getY(), v.getZ() - 1).mark == true));
    }

    @Override
    protected boolean isStartAtMiddle(ArrayList<PlacedLetter> vector, Desks deska) {
        Desks3D desk = (Desks3D) deska;
        if (vector.size() % 2 == 1) {
            int index = (vector.size() - 1) / 2;
            if (isXvector(vector) == 1) {
                return desk.getPole3D().get(vector.get(0).getZ())[vector.get(index).getX()][vector.get(0).getY()] == 1;
            } else if (isXvector(vector) == 2) {
                return desk.getPole3D().get(vector.get(index).getZ())[vector.get(0).getX()][vector.get(0).getY()] == 1;
            } else {
                return desk.getPole3D().get(vector.get(0).getZ())[vector.get(0).getX()][vector.get(index).getY()] == 1;

            }
        } else {

            int index = (vector.size()) / 2;
            if (isXvector(vector) == 1) {
                return desk.getPole3D().get(vector.get(0).getZ())[vector.get(index).getX()][vector.get(0).getY()] == 1
                        || desk.getPole3D().get(vector.get(0).getZ())[vector.get(index - 1).getX()][vector.get(0).getY()] == 1;
            } else if (isXvector(vector) == 2) {
                return desk.getPole3D().get(vector.get(index).getZ())[vector.get(0).getX()][vector.get(0).getY()] == 1
                        || desk.getPole3D().get(vector.get(index - 1).getZ())[vector.get(0).getX()][vector.get(0).getY()] == 1;
            } else {
                return desk.getPole3D().get(vector.get(0).getZ())[vector.get(0).getX()][vector.get(index).getY()] == 1
                        || desk.getPole3D().get(vector.get(0).getZ())[vector.get(0).getX()][vector.get(index - 1).getY()] == 1;

            }

        }
    }

    @Override
    public boolean isDeskClear(GameDesktop turnek) {

        GameDesktop3D turn = (GameDesktop3D) turnek;
        for (int z = 0; z < turn.getLevels(); z++) {
            for (int x = 0; x < turn.getWidth(); x++) {
                for (int y = 0; y < turn.getHeight(); y++) {
                    if (turn.getPole3D().get(z)[x][y] != null) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    @Override
    public boolean fastIsDeskClear(GameDesktop turnek, Desks deska) {
        Desks3D desk = (Desks3D) deska;
        GameDesktop3D turn = (GameDesktop3D) turnek;
        for (PlacedLetter elem : desk.startFields) {
            if (turn.getPolePrvek(elem.getX(), elem.getY(), elem.getZ()) != null) {
                return false;
            }
        }

        return true;
    }

    @Override
    protected void sortOrientidVector(ArrayList<PlacedLetter> sorted) {
        if (isXvector(sorted) == 1) {
            Collections.sort(sorted);
        } else {
            Collections.sort(sorted);
            Collections.reverse(sorted);
        }
    }

    @Override
    protected ArrayList<PlacedLetter> sortLetters(ArrayList<PlacedLetter> placedLetters) {

        ArrayList<PlacedLetter> unsorted = placedLetters;
        if (isXvector(unsorted) == 1) {

            setVectorOrientation(unsorted, PlacedLetter.COMPARE_BY_X);
            sortOrientidVector(unsorted);
        } else if (isXvector(unsorted) == 2) {
            setVectorOrientation(unsorted, PlacedLetter.COMPARE_BY_Z);
            sortOrientidVector(unsorted);
        } else {
            setVectorOrientation(unsorted, PlacedLetter.COMPARE_BY_Y);
            sortOrientidVector(unsorted);
        }
        return placedLetters;
    }

    protected ArrayList<PlacedLetter> get3DWordFrom(ArrayList<PlacedLetter> vector, PlacedLetter from, GameDesktop turnek, boolean alwaysfirst) {

        GameDesktop3D turn = (GameDesktop3D) turnek;
        ArrayList<PlacedLetter> word = new ArrayList<PlacedLetter>();
        int x = from.getX();
        int y = from.getY();
        int z = from.getZ();
        z = z + 1;
        while (turn.getLetterOnBoardIndependentOnBuffer(x, y, z) != null) {
            if (turn.getPolePrvek(x, y, z) != null) {
                word.add(new PlacedLetter(x, y, z, turn.getLetterOnBoardIndependentOnBuffer(x, y, z)));
            } else {
                word.add(turn.getAnyNewhere(x, y, z));
            }
            GameDesktop3D.getAnyNewhereInVector(word, x, y, z).mark = true;
            z++;
        }

        x = from.getX();
        y = from.getY();
        z = from.getZ();
        z = z - 1;
        while (turn.getLetterOnBoardIndependentOnBuffer(x, y, z) != null) {
            if (turn.getPolePrvek(x, y, z) != null) {
                word.add(new PlacedLetter(x, y, z, turn.getLetterOnBoardIndependentOnBuffer(x, y, z)));
            } else {
                word.add(turn.getAnyNewhere(x, y, z));
            }
            GameDesktop3D.getAnyNewhereInVector(word, x, y, z).mark = true;
            z--;
        }

        if (alwaysfirst) {
            word.add(from);
        } else if (word.size() > 0) {
            word.add(from);
        }

//return readLetters(word);
        return word;
    }

    @Override
    protected ArrayList<PlacedLetter> getHorizontalWordFrom(ArrayList<PlacedLetter> vector, PlacedLetter from, GameDesktop turnek, boolean alwaysfirst) {

        GameDesktop3D turn = (GameDesktop3D) turnek;
        ArrayList<PlacedLetter> word = new ArrayList<PlacedLetter>();
        int x = from.getX();
        int y = from.getY();
        int z = from.getZ();
        x = x + 1;
        while (turn.getLetterOnBoardIndependentOnBuffer(x, y, z) != null) {
            if (turn.getPolePrvek(x, y, z) != null) {
                word.add(new PlacedLetter(x, y, z, turn.getLetterOnBoardIndependentOnBuffer(x, y, z)));
            } else {
                word.add(turn.getAnyNewhere(x, y, z));
            }
            GameDesktop3D.getAnyNewhereInVector(word, x, y, z).mark = true;
            x++;
        }

        x = from.getX();
        y = from.getY();
        z = from.getZ();
        x = x - 1;
        while (turn.getLetterOnBoardIndependentOnBuffer(x, y, z) != null) {
            if (turn.getPolePrvek(x, y, z) != null) {
                word.add(new PlacedLetter(x, y, z, turn.getLetterOnBoardIndependentOnBuffer(x, y, z)));
            } else {
                word.add(turn.getAnyNewhere(x, y, z));
            }
            GameDesktop3D.getAnyNewhereInVector(word, x, y, z).mark = true;
            x--;
        }

        if (alwaysfirst) {
            word.add(from);
        } else if (word.size() > 0) {
            word.add(from);
        }

//return readLetters(word);
        return word;
    }

    @Override
    protected ArrayList<PlacedLetter> getVerticalWordFrom(ArrayList<PlacedLetter> vector, PlacedLetter from, GameDesktop turnek, boolean alwaysfirst) {
        GameDesktop3D turn = (GameDesktop3D) turnek;
        ArrayList<PlacedLetter> word = new ArrayList<PlacedLetter>();
        int x = from.getX();
        int y = from.getY();
        int z = from.getZ();
        y = y + 1;
        while (turn.getLetterOnBoardIndependentOnBuffer(x, y, z) != null) {
            if (turn.getPolePrvek(x, y, z) != null) {
                word.add(new PlacedLetter(x, y, z, turn.getLetterOnBoardIndependentOnBuffer(x, y, z)));
            } else {
                word.add(turn.getAnyNewhere(x, y, z));
            }
            GameDesktop3D.getAnyNewhereInVector(word, x, y, z).mark = true;
            y++;
        }

        x = from.getX();
        y = from.getY();
        z = from.getZ();
        y = y - 1;
        while (turn.getLetterOnBoardIndependentOnBuffer(x, y, z) != null) {
            if (turn.getPolePrvek(x, y, z) != null) {
                word.add(new PlacedLetter(x, y, z, turn.getLetterOnBoardIndependentOnBuffer(x, y, z)));
            } else {
                word.add(turn.getAnyNewhere(x, y, z));
            }
            GameDesktop3D.getAnyNewhereInVector(word, x, y, z).mark = true;
            y--;
        }
        if (alwaysfirst) {
            word.add(from);
        } else if (word.size() > 0) {
            word.add(from);
        }

//return readLetters(word);
        return word;
    }

    protected int get3DWordValueFrom(ArrayList<PlacedLetter> vector, PlacedLetter from, GameDesktop turnek, boolean alwaysfirst, Desks deska) {
        Desks3D desk = (Desks3D) deska;
        GameDesktop3D turn = (GameDesktop3D) turnek;
        int suma = 0;
        int add;
        int letters = 0;

        int x = from.getX();
        int y = from.getY();
        int z = from.getZ();
        z = z + 1;
        while (turn.getLetterOnBoardIndependentOnBuffer(x, y, z) != null) {
            add = turn.getLetterOnBoardIndependentOnBuffer(x, y, z).getValue();
            if (turn.getPole3D().get(z)[x][y] == null) {
                add = add * desk.getLetterKoeficientAt(x, y, z);
            }
            if (turn.getPole3D().get(z)[x][y] == null) {
                GameDesktop3D.getAnyNewhereInVector(vector, x, y, z).mark = true;//spravne?
            }
            suma += add;
            letters++;
            z++;
        }

        x = from.getX();
        y = from.getY();
        z = from.getZ();
        z = z - 1;
        while (turn.getLetterOnBoardIndependentOnBuffer(x, y, z) != null) {
            add = turn.getLetterOnBoardIndependentOnBuffer(x, y, z).getValue();
            if (turn.getPole3D().get(z)[x][y] == null) {
                add = add * desk.getLetterKoeficientAt(x, y, z);
            }
            if (turn.getPole3D().get(z)[x][y] == null) {
                GameDesktop3D.getAnyNewhereInVector(vector, x, y, z).mark = true;//spravne?  
            }
            suma += add;
            letters++;
            z--;
        }

        if (alwaysfirst) {
            letters++;
            add = from.getLetter().getValue();
            x = from.getX();
            y = from.getY();
            z = from.getZ();
            add = add * desk.getLetterKoeficientAt(x, y, z);
            suma += add;
        } else if (letters > 0) {
            letters++;
            add = from.getLetter().getValue();
            x = from.getX();
            y = from.getY();
            z = from.getZ();
            add = add * desk.getLetterKoeficientAt(x, y, z);
            suma += add;
        }

        return suma;
    }

    @Override
    protected int getHorizontalWordValueFrom(ArrayList<PlacedLetter> vector, PlacedLetter from, GameDesktop turnek, boolean alwaysfirst, Desks deska) {
        Desks3D desk = (Desks3D) deska;
        GameDesktop3D turn = (GameDesktop3D) turnek;
        int suma = 0;
        int add;
        int letters = 0;

        int x = from.getX();
        int y = from.getY();
        int z = from.getZ();
        x = x + 1;
        while (turn.getLetterOnBoardIndependentOnBuffer(x, y, z) != null) {
            add = turn.getLetterOnBoardIndependentOnBuffer(x, y, z).getValue();
            if (turn.getPole3D().get(z)[x][y] == null) {
                add = add * desk.getLetterKoeficientAt(x, y, z);
            }
            if (turn.getPole3D().get(z)[x][y] == null) {
                GameDesktop3D.getAnyNewhereInVector(vector, x, y, z).mark = true;//spravne?
            }
            suma += add;
            letters++;
            x++;
        }

        x = from.getX();
        y = from.getY();
        z = from.getZ();
        x = x - 1;
        while (turn.getLetterOnBoardIndependentOnBuffer(x, y, z) != null) {
            add = turn.getLetterOnBoardIndependentOnBuffer(x, y, z).getValue();
            if (turn.getPole3D().get(z)[x][y] == null) {
                add = add * desk.getLetterKoeficientAt(x, y, z);
            }
            if (turn.getPole3D().get(z)[x][y] == null) {
                GameDesktop3D.getAnyNewhereInVector(vector, x, y, z).mark = true;//spravne?  
            }
            suma += add;
            letters++;
            x--;
        }

        if (alwaysfirst) {
            letters++;
            add = from.getLetter().getValue();
            x = from.getX();
            y = from.getY();
            z = from.getZ();
            add = add * desk.getLetterKoeficientAt(x, y, z);
            suma += add;
        } else if (letters > 0) {
            letters++;
            add = from.getLetter().getValue();
            x = from.getX();
            y = from.getY();
            z = from.getZ();
            add = add * desk.getLetterKoeficientAt(x, y, z);
            suma += add;
        }

        return suma;
    }

    @Override
    protected int getVerticalWordValueFrom(ArrayList<PlacedLetter> vector, PlacedLetter from, GameDesktop turnek, boolean alwaysfirst, Desks deska) {
        Desks3D desk = (Desks3D) deska;
        GameDesktop3D turn = (GameDesktop3D) turnek;
        int suma = 0;
        int add;
        int letters = 0;

        int x = from.getX();
        int y = from.getY();
        int z = from.getZ();
        y = y + 1;
        while (turn.getLetterOnBoardIndependentOnBuffer(x, y, z) != null) {
            add = turn.getLetterOnBoardIndependentOnBuffer(x, y, z).getValue();
            if (turn.getPole3D().get(z)[x][y] == null) {
                add = add * desk.getLetterKoeficientAt(x, y, z);
            }
            if (turn.getPole3D().get(z)[x][y] == null) {
                GameDesktop3D.getAnyNewhereInVector(vector, x, y, z).mark = true;//spravne?
            }
            suma += add;
            letters++;
            y++;
        }

        x = from.getX();
        y = from.getY();
        z = from.getZ();
        y = y - 1;
        while (turn.getLetterOnBoardIndependentOnBuffer(x, y, z) != null) {
            add = turn.getLetterOnBoardIndependentOnBuffer(x, y, z).getValue();
            if (turn.getPole3D().get(z)[x][y] == null) {
                add = add * desk.getLetterKoeficientAt(x, y, z);
            }
            if (turn.getPole3D().get(z)[x][y] == null) {
                GameDesktop3D.getAnyNewhereInVector(vector, x, y, z).mark = true;//spravne?
            }
            suma += add;
            letters++;
            y--;
        }

        if (alwaysfirst) {
            letters++;
            add = from.getLetter().getValue();
            x = from.getX();
            y = from.getY();
            z = from.getZ();
            add = add * desk.getLetterKoeficientAt(x, y, z);
            suma += add;
        } else if (letters > 0) {
            letters++;
            add = from.getLetter().getValue();
            x = from.getX();
            y = from.getY();
            z = from.getZ();
            add = add * desk.getLetterKoeficientAt(x, y, z);
            suma += add;
        }

        return suma;

    }

    @Override
    public WordsWithScore getWordsWithScore(ArrayList<PlacedLetter> vector, GameDesktop turnek, Desks deska) {
        Desks3D desk = (Desks3D) deska;
        GameDesktop3D turn = (GameDesktop3D) turnek;

        WordsWithScore vysledek = new WordsWithScore();
        if (vector.isEmpty()) {
            return vysledek;
        }
        int add;
        int k; //=1;
        String w;
        ArrayList<PlacedLetter> word;

        if (vector.size() == 1) {

            word = get3DWordFrom(vector, vector.get(0), turn, false);
            w = readLetters(word);
            add = getHorizontalWordValueFrom(vector, vector.get(0), turn, false, desk);
            k = desk.getWordKoeficientAt(
                    vector.get(0).getX(),
                    vector.get(0).getY(),
                    vector.get(0).getZ(),
                    turn
            );
            vysledek.score += k * add;
            if (!w.trim().equalsIgnoreCase("")) {
                vysledek.poorWords.add(w.trim());
                vysledek.lines.add(w + " (" + String.valueOf(add) + "*" + String.valueOf(k) + "=" + String.valueOf(add * k) + ")");
            }

            word = getHorizontalWordFrom(vector, vector.get(0), turn, false);
            w = readLetters(word);
            add = getHorizontalWordValueFrom(vector, vector.get(0), turn, false, desk);
            k = desk.getWordKoeficientAt(
                    vector.get(0).getX(),
                    vector.get(0).getY(),
                    vector.get(0).getZ(),
                    turn
            );
            vysledek.score += k * add;
            if (!w.trim().equalsIgnoreCase("")) {
                vysledek.poorWords.add(w.trim());
                vysledek.lines.add(w + " (" + String.valueOf(add) + "*" + String.valueOf(k) + "=" + String.valueOf(add * k) + ")");
            }

            word = getVerticalWordFrom(vector, vector.get(0), turn, false);
            w = readLetters(word);
            add = getVerticalWordValueFrom(vector, vector.get(0), turn, false, desk);
            k = desk.getWordKoeficientAt(
                    vector.get(0).getX(),
                    vector.get(0).getY(),
                    vector.get(0).getZ(),
                    turn
            );
            vysledek.score += k * add;
            if (!w.trim().equalsIgnoreCase("")) {
                vysledek.poorWords.add(w.trim());
                vysledek.lines.add(w + " (" + String.valueOf(add) + "*" + String.valueOf(k) + "=" + String.valueOf(add * k) + ")");
            }

        } else {

            for (PlacedLetter vector1 : vector) {
                vector1.mark = false;
            }
            int stmark = 0;
            while (stmark >= 0) {
                vector.get(stmark).mark = true;
                word = getHorizontalWordFrom(vector, vector.get(stmark), turn, true);
                if (word.size() > 1) {
                    w = readLetters(word).trim();
                    add = getHorizontalWordValueFrom(vector, vector.get(stmark), turn, true, desk);
                    k = 1;
                    for (PlacedLetter word1 : word) {
                        if (turn.getPole3D().get(word1.getZ())[word1.getX()][word1.getY()] == null) {
                            k = k * desk.getWordKoeficientAt(word1.getX(), word1.getY(), word1.getZ(), turn);
                        }
                    }
                    vysledek.score += add * k;
                    if (!w.trim().equalsIgnoreCase("")) {
                        vysledek.poorWords.add(w.trim());
                        vysledek.lines.add(w + " (" + String.valueOf(add) + "*" + String.valueOf(k) + "=" + String.valueOf(add * k) + ")");
                    }
                }
                stmark = -1;
                for (int x = 0; x < vector.size(); x++) {
                    if (!vector.get(x).mark) {
                        stmark = x;
                        break;
                    }
                }

            }

            stmark = 0;
            for (PlacedLetter vector1 : vector) {
                vector1.mark = false;
            }
            while (stmark >= 0) {
                vector.get(stmark).mark = true;
                word = getVerticalWordFrom(vector, vector.get(stmark), turn, true);
                if (word.size() > 1) {
                    w = readLetters(word).trim();
                    add = getVerticalWordValueFrom(vector, vector.get(stmark), turn, true, desk);
                    k = 1;
                    for (PlacedLetter word1 : word) {
                        if (turn.getPole3D().get(word1.getZ())[word1.getX()][word1.getY()] == null) {
                            k = k * desk.getWordKoeficientAt(word1.getX(), word1.getY(), word1.getZ(), turn);
                        }
                    }
                    vysledek.score += add * k;
                    if (!w.trim().equalsIgnoreCase("")) {
                        vysledek.poorWords.add(w.trim());
                        vysledek.lines.add(w + " (" + String.valueOf(add) + "*" + String.valueOf(k) + "=" + String.valueOf(add * k) + ")");
                    }
                }
                stmark = -1;
                for (int x = 0; x < vector.size(); x++) {
                    if (!vector.get(x).mark) {
                        stmark = x;
                        break;
                    }
                }

            }

//3d
            stmark = 0;
            for (PlacedLetter vector1 : vector) {
                vector1.mark = false;
            }
            while (stmark >= 0) {
                vector.get(stmark).mark = true;
                word = get3DWordFrom(vector, vector.get(stmark), turn, true);
                if (word.size() > 1) {
                    w = readLetters(word).trim();
                    add = get3DWordValueFrom(vector, vector.get(stmark), turn, true, desk);
                    k = 1;
                    for (PlacedLetter word1 : word) {
                        if (turn.getPole3D().get(word1.getZ())[word1.getX()][word1.getY()] == null) {
                            k = k * desk.getWordKoeficientAt(word1.getX(), word1.getY(), word1.getZ(), turn);
                        }
                    }
                    vysledek.score += add * k;
                    if (!w.trim().equalsIgnoreCase("")) {
                        vysledek.poorWords.add(w.trim());
                        vysledek.lines.add(w + " (" + String.valueOf(add) + "*" + String.valueOf(k) + "=" + String.valueOf(add * k) + ")");
                    }
                }
                stmark = -1;
                for (int x = 0; x < vector.size(); x++) {
                    if (!vector.get(x).mark) {
                        stmark = x;
                        break;
                    }
                }

            }

        }

        return vysledek;

    }
    /*
     *@deprecated
     */

    @Override
    public int testJoker(GameDesktop gameDesktop, int xx, int yy, Settings settings, Player hraje) {
        return 0;
    }
//chyby jen test joker

    @Override
    public int testJoker(GameDesktop gameDesktop, int xx, int yy, int zz, Settings settings, Player hraje) {
        ArrayList<PlacedLetter> jokerReplacement = new ArrayList<PlacedLetter>();

        jokerReplacement.add(new PlacedLetter(xx, yy, gameDesktop.getSelected()));

        ArrayList<String> s = getMultypleWords(jokerReplacement, gameDesktop);
        for (String item : s) {
            console.addItem(item + "[j]");
        }

        if (settings.bools[Settings.playercheck]) {
            int agreed = 0;
            int[] btns = new int[2];
            btns[0] = OGLdialog.MB_OK;
            btns[1] = OGLdialog.MB_NO;

            for (Player player : players) {
                if (player.getAI() == 0) {
                    ArrayList<String> dl = new ArrayList<String>();
                    dl.add(player.getJmeno() + ",");
                    dl.add("player " + hraje.getJmeno() + " REPLACED JOKER and have created these word(s)");
                    dl.addAll(s);
                    dl.add("do you agree with this turn?");
                    api.getOglDialog().execute(btns, dl, Main.DM.getWidth() / 2, Main.DM.getHeight() / 2);
                    if (api.globalReaction.reaction == OGLdialog.MR_OK) {
                        console.addItem(player.getJmeno() + " aggred");
                        agreed++;
                    } else if (api.globalReaction.reaction == OGLdialog.MR_NO) {
                        console.addItem(player.getJmeno() + " disaggre");
                    } else {
                        console.addItem(player.getJmeno() + " forced error");
                    }
                } //for
            }

            int a = getHumans();
            if (a == 2) {
                a = 3;
            }
            a++;

            if (agreed < a / 2) {
                return -9;
            }
        }

        if (settings.bools[Settings.inetcheck]) {
            int fuj = testWordsOnWeb(getMultypleWords(jokerReplacement, gameDesktop), settings, false);
            if (fuj > 0) {
                return -10;
            }
        }

        if (settings.bools[Settings.wordcheck]) {
            int fuj = testWordsInDictionary(getMultypleWords(jokerReplacement, gameDesktop), settings, false);
            if (fuj > 0) {
                return -10;
            }
        }
        return 0;
    }

    @Override
    public void finalize() throws Throwable {

    }

    @Override
    public ArrayList<String> getMultypleWords(ArrayList<PlacedLetter> vector, GameDesktop turn) {

        ArrayList<String> lines = new ArrayList<String>();
        if (vector.isEmpty()) {
            return lines;
        }
        String w;
        ArrayList<PlacedLetter> word;

        if (vector.size() == 1) {
            String o;
            sortLetters(vector);
            word = getHorizontalWordFrom(vector, vector.get(0), turn, false);
            o = readLetters(word);
            if (!o.trim().equalsIgnoreCase("")) {
                lines.add(o);
            }
            word = getVerticalWordFrom(vector, vector.get(0), turn, false);
            o = readLetters(word);
            if (!o.trim().equalsIgnoreCase("")) {
                lines.add(o);
            }
            word = get3DWordFrom(vector, vector.get(0), turn, false);
            o = readLetters(word);
            if (!o.trim().equalsIgnoreCase("")) {
                lines.add(o);
            }

        } else {

            for (PlacedLetter vector1 : vector) {
                vector1.mark = false;
            }
            int stmark = 0;
            while (stmark >= 0) {
                vector.get(stmark).mark = true;
                word = getHorizontalWordFrom(vector, vector.get(stmark), turn, true);
                w = readLetters(word).trim();
                if (w.length() > 1) {
                    lines.add(w);
                }
                stmark = -1;
                for (int x = 0; x < vector.size(); x++) {
                    if (!vector.get(x).mark) {
                        stmark = x;
                        break;
                    }
                }

            }
            stmark = 0;
            for (PlacedLetter vector1 : vector) {
                vector1.mark = false;
            }
            while (stmark >= 0) {
                vector.get(stmark).mark = true;
                word = getVerticalWordFrom(vector, vector.get(stmark), turn, true);
                w = readLetters(word).trim();
                if (w.length() > 1) {
                    lines.add(w);
                }
                stmark = -1;
                for (int x = 0; x < vector.size(); x++) {
                    if (!vector.get(x).mark) {
                        stmark = x;
                        break;
                    }
                }

            }

//3d
            stmark = 0;
            for (PlacedLetter vector1 : vector) {
                vector1.mark = false;
            }
            while (stmark >= 0) {
                vector.get(stmark).mark = true;
                word = get3DWordFrom(vector, vector.get(stmark), turn, true);
                w = readLetters(word).trim();
                if (w.length() > 1) {
                    lines.add(w);
                }
                stmark = -1;
                for (int x = 0; x < vector.size(); x++) {
                    if (!vector.get(x).mark) {
                        stmark = x;
                        break;
                    }
                }

            }

        }

        return lines;
    }

}//class

///2658
