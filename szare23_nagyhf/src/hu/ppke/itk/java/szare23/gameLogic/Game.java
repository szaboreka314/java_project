package hu.ppke.itk.java.szare23.gameLogic;

import java.io.Serializable;

/**
 * szerializáció miatt külön osztályban kezelem a pályát és a pontokat
 */

public class Game implements Serializable {

    private Integer[][] field;
    private int[] scores;

    Game(Integer[][] field, int[] scores)
    {
        this.field = field;
        this.scores = scores;
    }

    public Integer[][] getField() {
        return field;
    }

    public void setField(Integer[][] field) {
        this.field = field;
    }

    public int[] getScores() {
        return scores;
    }

    public void setScores(int[] scores) {
        this.scores = scores;
    }
}
