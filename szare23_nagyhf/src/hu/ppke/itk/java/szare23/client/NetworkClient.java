package hu.ppke.itk.java.szare23.client;

import hu.ppke.itk.java.szare23.NewNetworkGame;
import hu.ppke.itk.java.szare23.gameLogic.*;
import javafx.application.Platform;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.function.Consumer;

/**
 * hálózati játék esetén a klienst emgvalósító osztály
 */

public class NetworkClient extends Thread implements MessageHandler{

    protected boolean isGUI, bonuses;
    protected Boolean isReady;
    protected int ind;
    protected NewNetworkGame networkGame;
    protected UserInterface userInterface;
    protected String uname;
    protected Socket clientSocket;
    protected ObjectOutputStream serverObjectWriter;
    protected ObjectInputStream serverObjectReader;
    protected Consumer<Serializable> receiveCallBack;

    public NetworkClient(NewNetworkGame networkGame)
    {
        this.networkGame = networkGame;
        bonuses = false;
    }

    /**
     * hálózati kommunikációért felel
     * szerializált adatot kap, amit az adat osztálya alapján dolgoz fel
     */
    @Override
    public void run() {
        receiveCallBack = data->{
            if(data.getClass().equals(String.class))
            {
                String s = (String) data;
                switch (s)
                {
                    case "ready":
                    {
                        networkGame.start();
                        break;
                    }
                    case "handle":
                    {
                        handle();
                        break;
                    }
                }
            }
            else if(data.getClass().equals(Game.class))
            {
                Game g = (Game) data;
                handle(g);
            }
            else if(data.getClass().equals(Boolean.class))
            {
                Boolean b = (Boolean) data;
                handleGame(b.booleanValue());
            }
        };
        try{
            try {
                Serializable accept = (Serializable) serverObjectReader.readObject();
                if (accept.getClass().equals(String.class)) {
                    String s = (String) accept;
                    if (s.equals("Successfully connected!")) {
                        Serializable acceptInd = (Serializable) serverObjectReader.readObject();
                        ind = (int) acceptInd;
                        if (isGUI) {
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    networkGame.connectedGUI(ind);
                                }
                            });
                        }
                        else
                        {
                            networkGame.connectedTUI(ind);
                        }
                        while (!isReady && !clientSocket.isClosed())
                        {
                            try {
                                sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        if(ind == 0 && bonuses == true)
                        {
                            serverObjectWriter.writeObject("bonuses");
                            //serverObjectWriter.flush();
                        }
                        serverObjectWriter.writeObject("ready");
                        //serverObjectWriter.flush();
                        while (true) {
                            try {
                                Serializable data = (Serializable) serverObjectReader.readObject();
                                receiveCallBack.accept(data);
                            } catch (ClassNotFoundException | IOException e) {
                                if (networkGame.isStart())
                                    userInterface.updateGame(true);
                                else
                                    networkGame.serverDown();
                                return;
                            }
                        }
                    }
                }
            } catch (ClassNotFoundException | IOException e) {
                networkGame.serverDown();
                return;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * beállítja, hogyha kész a játékos
     * @param bonuses a játék bónuszokkal vagy anélkül történik
     */
    public void setReady(boolean bonuses)
    {
        isReady = true;
        this.bonuses = bonuses;
    }

    /**
     * létrehozza a kapcsolatot a szerverrel
     * @param uname felhasználónév
     * @param host host neve
     * @param isGUI grafikus játék-e
     * @throws Exception bármilyen szerverkapcsolat-hiba
     */

    public void setConnection(String uname, String host,  boolean isGUI) throws Exception
    {
        this.uname = uname;
        this.isGUI = isGUI;
        isReady = Boolean.FALSE;

        clientSocket = new Socket(host, Server.PORT);
        serverObjectWriter = new ObjectOutputStream(clientSocket.getOutputStream());
        serverObjectReader = new ObjectInputStream(clientSocket.getInputStream());

    }

    /**
     * továbbítja az UI-nak a pályát és a pontokat
     * @param game Game osztály példány, amely tartalmazza a pályát és a pontokat
     */
    @Override
    public void handle(Game game) {
            userInterface.updateUI(game.getField());
            userInterface.updateScores(game.getScores());

    }

    /**
     * továbbítja az UI-nak, hogy tart-e még a játék
     * @param isGame tart-e még a játék
     */
    @Override
    public void handleGame(boolean isGame) {
        userInterface.updateGame(isGame);
    }

    /**
     * továbbítja a playercommunicatornak az UI-tól kapott következő lépést
     */
    @Override
    public void handle() { //uitól kap infót
        String step = userInterface.getStep().toUpperCase();
        if(step.equals("A"))
        {
            try {
                serverObjectWriter.writeObject(Action.TURN_LEFT);
                serverObjectWriter.reset();
                serverObjectWriter.flush();
            } catch (IOException e) {
                userInterface.updateGame(true);
            }
        }
        else if(step.equals("S"))
        {
            try {
                serverObjectWriter.writeObject(Action.TURN_RIGHT);
                serverObjectWriter.reset();
                serverObjectWriter.flush();
            } catch (IOException e) {
                userInterface.updateGame(true);
            }
        }
        else
        {
            try {
                serverObjectWriter.writeObject(Action.FORWARD);
                serverObjectWriter.reset();
                //serverObjectWriter.flush();
            } catch (IOException e) {
                userInterface.updateGame(true);
            }
        }
    }

    /**
     * beállítja az UI-t
     * @param userInterface a megfelelő típusó UI
     */
    @Override
    public void setUI(UserInterface userInterface) {
        this.userInterface = userInterface;
    }

    /**
     * továbbítaná a playerCommunicatorokat tartalmazó listet, de erre itt nincs szükség
     * @param playerCommunicators játékosokat tartalmazó list
     */
    @Override
    public void setPCs(ArrayList<PlayerCommunicator> playerCommunicators) {}
}
