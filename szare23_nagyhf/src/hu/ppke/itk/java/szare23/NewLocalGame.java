package hu.ppke.itk.java.szare23;

import hu.ppke.itk.java.szare23.client.*;
import hu.ppke.itk.java.szare23.gameLogic.GameLogic;
import hu.ppke.itk.java.szare23.gameLogic.LocalPlayer;
import hu.ppke.itk.java.szare23.gameLogic.PlayerCommunicator;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * lokális játék indításáért felelős osztály
 */

public class NewLocalGame extends HBox {

    private static final String FONT_COLOR = "#1b9c8e";
    private static final String FONT_COLOR2 = "#bfa127";
    protected int[] playersNum = new int[6];
    private VBox vBoxLeft, vBoxRight;
    private Label gameModes, f1, f2, inst, start, players, p1, p2, p3, p4, p5, p6, notEnough;

    protected GameLogic gameLogic;
    protected GameGUI gameGUI;
    protected GameTextUI gameTextUI;
    protected LocalGame localGame;
    protected PlayerCommunicator playerCommunicator;

    protected ArrayList<PlayerCommunicator> playerCommunicators = new ArrayList<PlayerCommunicator>();

    public NewLocalGame()
    {
       addEventFilter(KeyEvent.KEY_PRESSED, this::createKeyEventHandler);
       localGame = new LocalGame();
       playerCommunicator = new LocalPlayer(localGame, -1);
    }

    /**
     * billentyűkezelést végző függvény
     * @param ev billentyű
     */
    protected void createKeyEventHandler(KeyEvent ev) //billentyűkezelés
    {
        if(ev.getCode() == KeyCode.DIGIT8 || ev.getCode() == KeyCode.DIGIT9)
        {
            p1.setStyle("-fx-font-weight: bold;");
            playersNum[0] = 1;
        }
        if(ev.getCode() == KeyCode.N || ev.getCode() == KeyCode.M)
        {
            p2.setStyle("-fx-font-weight: bold;");
            playersNum[1] = 1;
        }
        if(ev.getCode() == KeyCode.K || ev.getCode() == KeyCode.L)
        {
            p3.setStyle("-fx-font-weight: bold;");
            playersNum[2] = 1;
        }
        if(ev.getCode() == KeyCode.X || ev.getCode() == KeyCode.C)
        {
            p4.setStyle("-fx-font-weight: bold;");
            playersNum[3] = 1;
        }
        if(ev.getCode() == KeyCode.A || ev.getCode() == KeyCode.S)
        {
            p5.setStyle("-fx-font-weight: bold;");
            playersNum[4] = 1;
        }
        if(ev.getCode() == KeyCode.DIGIT1 || ev.getCode() == KeyCode.DIGIT2)
        {
            p6.setStyle("-fx-font-weight: bold;");
            playersNum[5] = 1;
        }
        if(enoughPlayers())
        {
            notEnough.setVisible(false);
            if(ev.getCode() == KeyCode.F1)
            {
                getChildren().removeAll(vBoxLeft, vBoxRight);
                getChildren().add(gameGUI);
                gameGUI.requestFocus();
                startGame(false);
            }
            if(ev.getCode() == KeyCode.F2)
            {
                getChildren().removeAll(vBoxLeft, vBoxRight);
                getChildren().add(gameGUI);
                gameGUI.requestFocus();
                startGame(true);
            }
        }

    }

    /**
     * consolos verzióhoz annak felismerése, melyik játékossal játszik
     * @param s String, ami minden játékos billentyűjét tartalmazza
     */
    private void playerSelector(String s)
    {
        if(s.indexOf('8') != -1 || s.indexOf('9') != -1)
            {playersNum[0] = 1;}
        if(s.indexOf('n') != -1 || s.indexOf('m') != -1)
           { playersNum[1] = 1;}
        if(s.indexOf('k') != -1 || s.indexOf('l') != -1)
            {playersNum[2] = 1;}
        if(s.indexOf('x') != -1 || s.indexOf('c') != -1)
            {playersNum[3] = 1;}
        if(s.indexOf('a') != -1 || s.indexOf('s') != -1)
            {playersNum[4] = 1;}
        if(s.indexOf('1') != -1 || s.indexOf('2') != -1)
            {playersNum[5] = 1;}
    }

    /**
     * számontartja, van-e elég játékos a játékhoz
     * @return true, ha elég játékos kapcsolódott már
     */
    private boolean enoughPlayers() //
    {
        boolean enoguh = false;
        int s = 0;
        for(int i = 0; i < playersNum.length; ++i)
        {
            if(playersNum[i] == 1)
                ++s;
        }
        if(s >= 2)
            enoguh = true;
        return enoguh;
    }

    /**
     * grafikus játék elindítása
     */
    public void startGUI()
    {
        gameGUI = new GameGUI();
        localGame.setUI(gameGUI);
        gameTextUI = null;
        vBoxLeft = new VBox(20);
        vBoxRight = new VBox(20);
        gameModes = new Label("Game modes: ");
        gameModes.setTextFill(Color.valueOf(FONT_COLOR2));
        gameModes.setFont(Font.font("Arial", FontWeight.BOLD,40));
        f1 = new Label("F1: normal mode");
        f1.setTextFill(Color.valueOf(FONT_COLOR));
        f1.setFont(Font.font("Arial",20));
        f2 = new Label("F2: Normal mode with bonuses");
        f2.setTextFill(Color.valueOf(FONT_COLOR));
        f2.setFont(Font.font("Arial",20));
        inst = new Label("Press any of the control keys" + '\n' + "of a Player to join with it!");
        inst.setTextFill(Color.valueOf(FONT_COLOR));
        inst.setFont(Font.font("Arial",20));
        start = new Label("Press F1 or F2 to start game!");
        start.setTextFill(Color.valueOf(FONT_COLOR));
        start.setFont(Font.font("Arial", FontPosture.ITALIC, 20));
        notEnough = new Label("There isn't enough players for the game!" + '\n' + "Required at least 2 players!");
        notEnough.setTextFill(Color.RED);
        notEnough.setFont(Font.font("Arial", FontPosture.ITALIC, 15));

        vBoxRight.setMinWidth(190);
        vBoxLeft.setMinWidth(410);
        vBoxLeft.setMinHeight(370);
        vBoxLeft.getChildren().addAll(gameModes, f1, f2, inst, start, notEnough);
        vBoxLeft.setStyle("-fx-border-style: solid ; -fx-border-width: 5; -fx-border-color: grey;");


        players = new Label("Players:");
        players.setTextFill(Color.valueOf(FONT_COLOR2));
        players.setFont(Font.font("Arial", FontWeight.BOLD,60));
        p1 = new Label("Player 1: "  + '\t' + "8" + '\t' + "9");
        p1.setTextFill(Color.RED);
        p1.setFont(Font.font("Arial",15));
        p2 = new Label("Player 2: " + '\t' + "n" + '\t' + "m");
        p2.setTextFill(Color.YELLOW);
        p2.setFont(Font.font("Arial",15));
        p3 = new Label("Player 3: " + '\t' + "k" + '\t' + "l");
        p3.setTextFill(Color.BLUE);
        p3.setFont(Font.font("Arial",15));
        p4 = new Label("Player 4: " + '\t' + "x" + '\t' + "c");
        p4.setTextFill(Color.GREEN);
        p4.setFont(Font.font("Arial",15));
        p5 = new Label("Player 5: " + '\t' + "a" + '\t' + "s");
        p5.setTextFill(Color.DEEPPINK);
        p5.setFont(Font.font("Arial",15));
        p6 = new Label("Player 6: " + '\t' + "1" + '\t' + "2");
        p6.setTextFill(Color.TURQUOISE);
        p6.setFont(Font.font("Arial",15));

        vBoxRight.getChildren().addAll(players, p1, p2, p3, p4, p5, p6);

        getChildren().addAll(vBoxLeft, vBoxRight);
    }

    /**
     * console-os játék elindítása
     */
    public void startText()
    {
        String playerS = new String();
        while (!enoughPlayers())
        {
            System.out.println(ConsoleColors.RESET + "Select controllers! Required at least 2 players for a game!");
            System.out.println(ConsoleColors.RED + "Player1: 8-9, " + ConsoleColors.YELLOW + "Player2: n-m, " + ConsoleColors.BLUE + "Player3: k-l, "
                    + ConsoleColors.GREEN + "Player4: x-c, " +ConsoleColors.PURPLE + " Player5: a-s, " + ConsoleColors.CYAN + "Player6: 1-2");
            System.out.println(ConsoleColors.RESET + "Everyone type one of the desired controllers in any order! Like k8x means there will be blue, red and green players.");
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            try {
                playerS = reader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            playerSelector(playerS);
            if(!enoughPlayers())
                System.out.println(ConsoleColors.RED + "There isn't enough players for the game! ");
        }
        System.out.println(ConsoleColors.RESET + "Select game mode: " + '\t' + "1: normal mode, 2: normal mode with bonuses");

        Scanner in = new Scanner(System.in);
        if(in.nextInt() == 1)
        {
            gameTextUI = new GameTextUI();
            localGame.setUI(gameTextUI);
            gameGUI = null;
            startGame(false);
        }
        else if(in.nextInt() == 2)
        {
            gameTextUI = new GameTextUI();
            localGame.setUI(gameTextUI);
            gameGUI = null;
            startGame( true);
        }
    }

    /**
     * játék tényleges elindítása, játékosokat tartalmazó list elkészítése
     * @param bonuses
     */
    public void startGame(boolean bonuses)
    {
        for(int i =0; i < playersNum.length; ++i)
        {
            if(playersNum[i] == 1)
            {
                LocalPlayer localPlayer = new LocalPlayer(localGame, i);
                playerCommunicators.add(localPlayer);
            }
        }
        gameLogic = new GameLogic(playerCommunicators, bonuses);
        if(gameTextUI != null)
            gameTextUI.start();
    }


}
