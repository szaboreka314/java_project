package hu.ppke.itk.java.szare23.gameLogic;

import hu.ppke.itk.java.szare23.client.LocalGame;
import hu.ppke.itk.java.szare23.client.MessageHandler;

import java.util.ArrayList;

/**
 * lokális játékosokat implementáló osztály
 */

public class LocalPlayer implements PlayerCommunicator {

    protected LocalGame localGame;
    private Action nextStep = Action.FORWARD;
    private int ind;
    private boolean inGame = true;
    private int lastPosX;
    private int lastPosY;
    private int score;
    private int direction;

    /**
     * @param localGame megkapja az aktuális játék messageHandlerét
     * @param ind index a színek, pontok és helyzetek könnyebb követhetősége érdekében
     */
    public LocalPlayer(LocalGame localGame, int ind){
        this.localGame = localGame;
        this.ind = ind;
        score = 0;
    }

    /**
     * a játéklogika tudja elérni a játékos következő lépését
     * @return a következő lépés
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
     * a mesageHandler ezen keresztül tudja beállítani a játékos következő lépését a kpaott input alapján
     * @param step a következő lépés
     */
    @Override
    public void setStep(Action step) {
        synchronized (nextStep)
        {nextStep = step;}
    }

    /**
     * @return visszaadja a játékos indexét
     */
    @Override
    public int getInd() {
        return ind;
    }

    /**
     * hamisra állítja, ha a játékos kieesett
     * @param inGame játékban van-e még
     */
    @Override
    public void setInGame(boolean inGame) {
        this.inGame = inGame;
    }

    /**
     * @return megadja, hogy a játékos játékban van-e még
     */
    @Override
    public boolean getInGame() {
        return inGame;
    }

    /**
     * odaadja a messageHandlernek a playerCOmmunicator listet
     * @param playerCommunicators PlayerCommunicatorokat tartalmazó list
     */
    @Override
    public void setPC(ArrayList<PlayerCommunicator> playerCommunicators) {
        localGame.setPCs(playerCommunicators);
    }

    /**
     * továbbítja a messageHandlernek a Game osztály egy példányát
     * @param game Game osztály egy példánya
     */
    @Override
    public void handle(Game game) {
        localGame.handle(game);
    }

    /**
     * meghívja a messageHandler handle függvényét
     */
    @Override
    public void handle() {
        localGame.handle();
    }


    /**
     * továbbítja a messageHandlernek, hogy tart-e még a játék
     * @param game tart-e még a játék
     */
    @Override
    public void handleGame(boolean game) {
        localGame.handleGame(game);
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
