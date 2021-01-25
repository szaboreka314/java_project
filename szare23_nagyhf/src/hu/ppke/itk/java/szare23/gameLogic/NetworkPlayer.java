package hu.ppke.itk.java.szare23.gameLogic;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.function.Consumer;

/**
 * hálózati játék esetén a szerverasszisztenst kezelő osztály
 */

public class NetworkPlayer extends Thread implements PlayerCommunicator {

    private Socket socket;
    protected PrintWriter clientWriter;
    protected ObjectOutputStream clientObjectWriter;
    protected ObjectInputStream clientObjectReader;
    protected Consumer<Serializable> receiveCallBack;
    private static int ID = 0;

    private Action nextStep = Action.FORWARD;
    private int ind;
    private Boolean inGame = true;
    private int lastPosX;
    private int lastPosY;
    private int score;
    private int direction;

    /**
     * konstruktor, itt kezeli a szervertől kapott üzeneteket
     * @param socket a kliens socket-e
     * @throws IOException
     */
    public NetworkPlayer(Socket socket) throws IOException { //ServerAssistant

        this.ind = ID++;
        score = 0;
        this.socket = socket;
        this.clientWriter = new PrintWriter(this.socket.getOutputStream());
        this.clientObjectReader = new ObjectInputStream(this.socket.getInputStream());
        this.clientObjectWriter = new ObjectOutputStream(this.socket.getOutputStream());
        receiveCallBack = data->{
            //TODO
            if (data.getClass().equals(String.class)) {
                String s = (String) data;
                switch (s)
                {

                    case "bonuses":
                    {
                        System.out.println("np bonuses");
                        Server.setBonuses();
                        break;
                    }
                    case "ready":
                    {
                        if(Server.isReady())
                            Server.sendAll(s);
                        break;
                    }
                }
            }
            else if(data.getClass().equals(Action.class))
            {
                Action step = (Action) data;
                setStep(step);
            }
        };
    }

    /**
     * a szerverrel való folyamatos kommunikáció megvalósítása
     */
    @Override
    public void run() {
        try{
            clientObjectWriter.writeObject("Successfully connected!");
            clientObjectWriter.writeObject(ind);
            String line;
            while (true)
            {
                try {
                    Serializable data = (Serializable) clientObjectReader.readObject();
                    receiveCallBack.accept(data);
                }
                catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        catch (Exception e)
        {
            setInGame(false);
            Server.onClose();
        }
    }

    /**
     * @return a játékos legutóbbi lépése
     */
    @Override
    public Action getStep() {
        Action s;
        synchronized (nextStep)
        {
            s = nextStep;
        }
        return s;
    }

    /**
     * beállítja a játékos következő lépését
     * @param step a következő lépés
     */
    @Override
    public void setStep(Action step) {
        synchronized (nextStep)
        {
            nextStep = step;
        }
    }

    /**
     * @return a játékos indexe
     */
    @Override
    public int getInd() {
        return ind;
    }

    /**
     * beállítható, hogy egy játékos kiesett a játékból
     * @param inGame játékban van-e még
     */
    @Override
    public void setInGame(boolean inGame) {
        this.inGame = inGame;
    }

    /**
     * @return a játékos játékban van-e még
     */
    @Override
    public boolean getInGame() {
        return inGame;
    }

    /**
     * a szerver így el tudja kérni a kliensekkel való kommunikációhoz
     * @return kliens üzenetküldésre szolgáló OutputStream-je
     */
    public ObjectOutputStream getClientObjectWriter() {
        return clientObjectWriter;
    }

    /**
     * kliensnek elküldi a Game osztály egy példányát
     * @param game a Game osztály egy példánya
     */
    @Override
    public void handle(Game game) {
        try {
            clientObjectWriter.writeObject(game);
            clientObjectWriter.reset();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * beálíltai a messageHandlernek a playerCommunicatorok listjét, de erre it nincs szükség
     * @param playerCommunicators
     */
    @Override
    public void setPC(ArrayList<PlayerCommunicator> playerCommunicators) { }

    /**
     * üzenetet küld a klienseknek, hogy hívják meg a handle függvényüket
     */
    @Override
    public void handle() {
        try {
            clientObjectWriter.writeObject("handle");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * továbbítja a kliensnek, hogy tart-e még a játék
     * @param game tart-e még a játék
     */
    @Override
    public void handleGame(boolean game) {
        try {
            Boolean b = game;
            clientObjectWriter.writeObject(b);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @return utolsó X pozíció
     */
    @Override
    public int getLastPosX() {
        return lastPosX;
    }

    /**
     * @param lastPosX utolsó X pozíció
     */
    @Override
    public void setLastPosX(int lastPosX) {
        this.lastPosX += lastPosX;
    }

    /**
     * @return utolsó Y pozíció
     */
    @Override
    public int getLastPosY() {
        return lastPosY;
    }

    /**
     * @param lastPosY utolsó Y pozíció
     */
    @Override
    public void setLastPosY(int lastPosY) {
        this.lastPosY += lastPosY;
    }

    /**
     * @return játékos iránya
     */
    @Override
    public int getDirection() {
        return direction;
    }

    /**
     * @param direction új irány
     */
    @Override
    public void setDirection(int direction) {
        this.direction = direction;
    }

}
