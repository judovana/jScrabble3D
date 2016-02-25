/*
 * IC.java
 *
 * Created on 30. duben 2007, 18:04
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package InetGameControler;

/**
 *
 * @author Jirka
 */
public class IC {

    public static final String[] commands = {
        /*0*/"ImIn!",
        /*1*/ "EveryBodyLoaded",
        /*2*/ "Players",/*.../name;local;behaviour/...*/
        /*3*/ "Console",/*Console-zpsfgfddf-zprava-zprava*/ //zpravy spojit
        /*4*/ "YourLetters",/*-pismenka*/
        /*5*/ "WhosTurnIs",/*-numIDhraje*/
        /*6*/ "ImMakingPass",
        /*7*/ "ClearYourDeskBuffer",
        /*8*/ "ImDead",
        /*9*/ "makingTurn",
        /*10*/ "Scores",
        /*11*/ "NewPlacedLetters",
        /*12*/ "MyTymeUp:(",
        /*13*/ "ChangingLetters",
        /*14*/ "SwappinhJoker",
        /*15*/ "SetLetterDirect",
        /*16*/ "SetSellectDirect",
        /*17*/ "DoYouWantRestart?",
        /*18*/ "Voting",
        /*19*/ "ClearYourself",
        /*20*/ "giveMePermutation", //level, od kam, pismenka
        /*21*/ "permutationResult", //value x y z (horiz?) placedLetters
        /*22*/ "DoYouAgreeWithTurn?",//dialogline-dialogline....
        /*23*/ "GameFinished!"

    };

    /**
     * Creates a new instance of IC
     */
    public IC() {
    }

}
