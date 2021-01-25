package hu.ppke.itk.java.szare23.client;

import hu.ppke.itk.java.szare23.gameLogic.Action;
import hu.ppke.itk.java.szare23.gameLogic.Game;
import hu.ppke.itk.java.szare23.gameLogic.PlayerCommunicator;

import java.util.ArrayList;

/**
 * The interface used by the UI to inform the game about events.
 */
public interface MessageHandler {
    /**
     * All relevant events from the UI should be transformed to a simple format
     * that the can be consumed by the "communication layer".
     *
     * <p> The parameter should be something uniform and not some event type
     * from JavaFX.
     * <p> However you can use your own event descriptor class, that might
     * be easier to parse.
     */

    /**
     * UI-nak továbbítja a pályát és a pontokat
     * @param game egy Game példány, amely tartalmazza a pályát és a pontokat
     */
    void handle(Game game);
    /**
     * @param isGame tart-e még a játék
     */
    void handleGame(boolean isGame);

    void handle(); //ez továbbítja az inputot enum formájában

    /**
     * @param userInterface a megfelelő típusó UI
     */
    void setUI(UserInterface userInterface);

    /**
     * @param playerCommunicators játékosokat tartalmazó list
     */
    void setPCs(ArrayList<PlayerCommunicator> playerCommunicators);
}
