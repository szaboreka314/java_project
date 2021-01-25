package hu.ppke.itk.java.szare23.gameLogic;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * szerver osztály, kezeli a hálózati kommunikáció szerver részét
 */
public class Server extends Thread {

    protected static ArrayList<PlayerCommunicator> playerCommunicators = new ArrayList<>();
    public static final int PORT = 10576;
    private static final int CONNECTION_CAPACITY = 6;
    private ServerSocket serverSocket;
    protected static GameLogic gameLogic;

    protected static int playerNum, ready;
    private static boolean withBonuses;

    public Server(){
        playerNum = 0;
        withBonuses = false;
        try {
            serverSocket = new ServerSocket(PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * játékosok felcsatlakozását kezeli
     */
    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                if(playerNum <= CONNECTION_CAPACITY)
                {
                    try {
                        Socket socket = serverSocket.accept();
                        NetworkPlayer networkPlayer = new NetworkPlayer(socket);
                        networkPlayer.start();
                        ++playerNum;
                        playerCommunicators.add(networkPlayer);
                    } catch (IOException e) {
                        try {
                            serverSocket.close();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                        throw new InterruptedException();
                    }
                }
            }
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * ha valamelyik játékos bezárja az ablakot, akkor kezeli, hogy mindenki más kapjon egy pontot
     */
    protected static void onClose()
    {
        if(playerCommunicators.size() > 1)
            gameLogic.onClose();
    }

    public static void setBonuses()
    {
        withBonuses = true;
    }

    /**
     * kezeli, hogy van-e elég játékos a hálózaton és ha igen, mindenki készen áll-e
     * @return
     */
    public static boolean isReady()
    {
        ++ready;
        if(playerNum >= 2 && ready == playerNum)
        {
            gameLogic = new GameLogic(playerCommunicators, withBonuses);
            return true;
        }
        return false;
    }

    /**
     * üzenetküldés mindenkinek
     * @param data üzenet
     */
    public static void sendAll(String data) {
        for (PlayerCommunicator pc: playerCommunicators) {
            NetworkPlayer np = (NetworkPlayer) pc;
            try {
                np.getClientObjectWriter().writeObject(data);
                np.getClientObjectWriter().flush();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public static void main(String[] args)
    {
        Server server = new Server();
        server.start();
    }
}
