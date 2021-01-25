package hu.ppke.itk.java.szare23.client;

/**
 * The interface used by the "middle" communication part to send updates to the
 * UI.
 */
public interface UserInterface {
    /**
     * Pass the updates to the User Interface to update it.
     *
     * <p>If you want you can change the type from String if you have a good
     * reason just document it.
     * <p> You can even divide this to multiple functions like `updateScores`
     * and `updateExtraObjects` `updateMap` as fits.
     *
     * @param field a játékterület mátrixa
     */
    void updateUI(Integer[][] field); //messagehandler alapján frissít

    /**
     * @param scores a játéklogika osztály pontokat tartalmazó tömbje
     */
    void updateScores(int[] scores);

    /**
     * @param gameOver tart-e még a játék
     */
    void updateGame(boolean gameOver);

    /**
     * @return olyan stringgel tér vissza, ami tartalmazza minden játékos lépésének a billentyűjét
     */
    String getStep();
}
