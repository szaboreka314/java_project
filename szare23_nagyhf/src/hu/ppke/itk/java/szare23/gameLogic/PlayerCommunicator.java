package hu.ppke.itk.java.szare23.gameLogic;

import hu.ppke.itk.java.szare23.client.MessageHandler;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Interface that determine the API that used by GameLogic to get steps from
 * players and send updates about the state.
 */
public interface PlayerCommunicator extends Serializable {

    /**
     * Access the step the player will take in this turn.
     *
     * @return The (last pressed) step of this turn. Either FORWARD, or TURN_**.
     */
    Action getStep(); //Ebben kapja meg a GameLogic az enumot, amit az UI állít be az input alapján

    /**
     * @param step a következő lépés
     */
    void setStep(Action step);

    /**
     * @return a játékos indexét
     */
    int getInd();

    /**
     * @param inGame játékban van-e még
     */
    void setInGame(boolean inGame);

    /**
     * @return játékban van-e még az adott játékos
     */
    boolean getInGame();

    /**
     * PlayerCommunicator tömb beállítása
     * @param playerCommunicators
     */
    void setPC(ArrayList<PlayerCommunicator> playerCommunicators);

    /**
     * Game osztály példányának továbbítása
     * @param game Game osztály példánya
     */
    void handle(Game game);

    /**
     * UI-tól kapott inputok kezelése
     */
    void handle();

    /**
     * Annak kezelése, hogy végetért-e a játék
     * @param game tart-e még a játék
     */
    void handleGame(boolean game);

    /**
     * @return utolsó X pozíció
     */
    int getLastPosX();

    /**
     * @param lastPosX új X pozíció
     */
    void setLastPosX(int lastPosX);

    /**
     * @return utolsó Y pozíció
     */
    int getLastPosY();

    /**
     * @param lastPosY új Y pozíció
     */
    void setLastPosY(int lastPosY);

    /**
     * @return aktuális irány
     */
    int getDirection();

    /**
     * @param direction új irány
     */
    void setDirection(int direction);

}

