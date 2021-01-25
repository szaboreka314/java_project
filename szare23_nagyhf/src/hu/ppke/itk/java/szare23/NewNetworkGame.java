package hu.ppke.itk.java.szare23;

import hu.ppke.itk.java.szare23.client.GameGUI;
import hu.ppke.itk.java.szare23.client.GameTextUI;
import hu.ppke.itk.java.szare23.client.NetworkClient;
import hu.ppke.itk.java.szare23.gameLogic.GameLogic;
import hu.ppke.itk.java.szare23.gameLogic.PlayerCommunicator;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * hálózati játék indításáért felelős osztály
 */
public class NewNetworkGame extends VBox {

    private static final String BUTTON_STYLE = "-fx-border-color: #1b9c8e; -fx-border-width: 5px; -fx-background-color: #000000; -fx-text-fill: #1b9c8e; -fx-font-weight: bold;";
    private static final String HOVERED_BUTTON_STYLE = "-fx-border-color: #bfa127; -fx-border-width: 5px; -fx-background-color: #000000; -fx-text-fill: #bfa127; -fx-font-weight: bold;";

    private static final String FONT_COLOR = "#1b9c8e";
    private static final String FONT_COLOR2 = "#bfa127";
    private TextArea uName, hostName;
    private Label wait, name, host, fill, success, waiting;
    private CheckBox bonuses;
    private Button buttonOK;

    private String username;
    private boolean isStart, withBonuses, graphic;

    protected GameLogic gameLogic;
    protected GameGUI gameGUI;
    protected GameTextUI gameTextUI;
    protected NetworkClient networkClient;

    /**
     * konstruktor
     */
    public NewNetworkGame()
    {
        isStart = false;
        networkClient = new NetworkClient(this);
        withBonuses = false;
    }

    /**
     * Console-os játék esetén ez az indító függvény
     */
    public void startText()
    {
        graphic = false;
        gameTextUI = new GameTextUI();
        networkClient.setUI(gameTextUI);
        gameGUI = null;
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String host = new String();
        System.out.println("Select a username and a host to play!");
        System.out.print("Username: ");
        try {
            username = br.readLine();
            System.out.print("Hostname: ");
            host = br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            networkClient.setConnection(username, host, false);
            System.out.println("Waiting for other players to join!");
            System.out.println("Hello " + username + "!" + '\n' + "Controllers:" + '\n' + "Move left: a" + '\n' + "Move right: s");
            networkClient.start();
        }
        catch (Exception e)
        {
            System.out.println("Server is down");
        }
    }

    /**
     * sikeres kapcsolódás esetén ezt a függvény hívja meg a kliens,
     * amennyiben console-os a játék
     * @param ind a játékos indexe
     */
    public void connectedTUI(int ind)
    {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String line;
        System.out.println("Successfully connected" + '\n' + "You gonna be Player" + (ind+1));
        if(ind == 0)
        {
            System.out.print("Play with bonuses? (yes/no)");
            try {
                line = br.readLine();
                if(line.equals("yes"))
                {
                    withBonuses = true;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Write ready, if you're ready!");
        try {
            line = br.readLine();
            if(line.toLowerCase().equals("ready"))
                networkClient.setReady(withBonuses);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * szerverhiba esetén kezeli a hibaüzeneteket
     */
    public void serverDown() {
        if(graphic)
        {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    waiting.setText("Server is down!");
                }
            });
        }
        else
            System.out.println("Server is down!");

    }

    /**
     * sikeres kapcsolódás esetén ezt a függvény hívja meg a kliens,
     *  amennyiben grafikus a játék
     *  @param ind a játékos indexe
     */
    public void connectedGUI(int ind)
    {
        this.setAlignment(Pos.CENTER);
        success = new Label("Successfully connected" + '\n' + "You gonna be Player" + (ind+1) +'\n' + "Click on Ready if you're ready!");
        success.setTextFill((Color.valueOf(FONT_COLOR)));
        success.setFont(Font.font("Arial", FontWeight.BOLD,15));
        success.setAlignment(Pos.CENTER);

        buttonOK = new Button("Ready");
        buttonOK.setMinWidth(100);
        buttonOK.setMinHeight(50);
        buttonOK.setStyle(BUTTON_STYLE);
        buttonOK.setOnMouseEntered(event->buttonOK.setStyle(HOVERED_BUTTON_STYLE));
        buttonOK.setOnMouseExited(event->buttonOK.setStyle(BUTTON_STYLE));

        bonuses = new CheckBox("play with bonuses");
        bonuses.setVisible(false);
        if(ind == 0)
        {
            bonuses.setVisible(true);
        }


        waiting = new Label("Waiting...");
        waiting.setTextFill((Color.valueOf(FONT_COLOR)));
        waiting.setFont(Font.font("Arial", FontWeight.BOLD,15));
        waiting.setAlignment(Pos.CENTER);
        waiting.setVisible(false);


        buttonOK.setOnAction(event->
        {
            if(bonuses.isSelected())
                withBonuses = true;
            networkClient.setReady(withBonuses);
            waiting.setVisible(true);
        });

        getChildren().addAll(success,bonuses, buttonOK, waiting);
    }

    /**
     * grafikus játék esetén az indító függvény
     */
    public void startGUI()
    {
        graphic = true;
        gameGUI = new GameGUI();
        networkClient.setUI(gameGUI);
        gameTextUI = null;
        this.setSpacing(20);
        this.setAlignment(Pos.CENTER);
        wait = new Label("Select a username and a host to play");
        wait.setTextFill(Color.valueOf(FONT_COLOR2));
        wait.setFont(Font.font("Arial", FontWeight.BOLD,30));
        name = new Label("username: ");
        name.setTextFill((Color.valueOf(FONT_COLOR)));
        name.setFont(Font.font("Arial", FontWeight.BOLD,20));
        host = new Label("hostname: ");
        host.setTextFill((Color.valueOf(FONT_COLOR)));
        host.setFont(Font.font("Arial", FontWeight.BOLD,20));
        fill = new Label("You have to fill the text areas!");
        fill.setTextFill((Color.DARKRED));
        fill.setFont(Font.font("Arial", FontWeight.BOLD,20));
        fill.setVisible(false);

        uName = new TextArea();
        uName.setPrefHeight(25);
        uName.setMaxWidth(200);
        hostName = new TextArea();
        hostName.setPrefHeight(25);
        hostName.setMaxWidth(200);
        hostName.setText("localhost");

        buttonOK = new Button("OK");
        buttonOK.setMinWidth(100);
        buttonOK.setMinHeight(50);
        buttonOK.setStyle(BUTTON_STYLE);
        buttonOK.setOnMouseEntered(event->buttonOK.setStyle(HOVERED_BUTTON_STYLE));
        buttonOK.setOnMouseExited(event->buttonOK.setStyle(BUTTON_STYLE));

        buttonOK.setOnAction(event->{
            if(uName.getText().equals(""))
            {
                fill.setVisible(true);
                uName.setStyle("-fx-border-color: #9c2836; -fx-border-width: 2px; -fx-background-color: #9c767c; -fx-text-fill: #000000;");
            }
            else if(hostName.getText().equals(""))
            {
                fill.setVisible(true);
                hostName.setStyle("-fx-border-color: #9c2836; -fx-border-width: 2px; -fx-background-color: #9c767c; -fx-text-fill: #000000;");
            }
            else
            {
                try {
                    networkClient.setConnection(username, hostName.getText(), true);
                    username = uName.getText();
                    getChildren().removeAll(uName,host, hostName,fill, buttonOK);
                    wait.setText("Waiting for other players to join!");
                    name.setText("Hello " + username + "!" + '\n' + "Controllers:" + '\n' + "Move left: a" + '\n' + "Move right: s");
                    name.setFont(Font.font("Arial", FontWeight.BOLD,15));
                    name.setAlignment(Pos.CENTER);
                    networkClient.start();
                }
                catch (Exception e)
                {
                    fill.setText("Server is down");
                    fill.setVisible(true);
                }

            }

        });

        getChildren().addAll(wait,name, uName, host, hostName, buttonOK, fill);
    }

    /**
     * amikor a szerver elindítja a játékot, ez a függvény hívódik meg
     */
    public void start()
    {
        if(graphic)
        {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    getChildren().removeAll(wait, name, waiting,bonuses,buttonOK, success);
                    getChildren().add(gameGUI);
                    gameGUI.requestFocus();
                    isStart = true;
                }
            });
        }
        else
        {
            isStart = true;
            gameTextUI.start();
        }
    }

    /**
     * @return elindult-e a játék
     */
    public boolean isStart() {
        return isStart;
    }
}
