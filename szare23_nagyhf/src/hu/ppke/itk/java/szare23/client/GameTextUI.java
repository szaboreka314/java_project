package hu.ppke.itk.java.szare23.client;

import hu.ppke.itk.java.szare23.ConsoleColors;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * consolos UI felület
 */

public class GameTextUI extends Thread implements UserInterface{

    private final String[] COLORS = {ConsoleColors.RED_BOLD, ConsoleColors.YELLOW_BOLD, ConsoleColors.BLUE_BOLD,
            ConsoleColors.GREEN_BOLD, ConsoleColors.PURPLE_BOLD, ConsoleColors.CYAN_BOLD, ConsoleColors.RESET };

    private boolean gameOver;
    private String step = "";
    private String nextStep = "";

    public GameTextUI() {
    }

    /**
     * console-ról beolvasott karaktereket kezeli
     */
    @Override
    public void run() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        try {
            while (!gameOver)
                addStep(reader.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * a console-on firssíti a pályát
     * @param field a játékterület mátrixa
     */
    @Override
    public void updateUI(Integer[][] field) {
        for(int i = 0; i < field.length; ++i)
        {
            for(int j = 0; j < field[i].length; ++j)
            {
                int f = field[i][j];
                if(f != -1 && f != 7)
                {
                    f+=1;
                    System.out.print(COLORS[field[i][j]] + f);
                }
                else
                    System.out.print(ConsoleColors.WHITE_BOLD+ 0);
                System.out.print(" ");
            }
            System.out.println();
        }
    }

    /**
     * az aktuális pontok megjelenítése a képernyőn
     * @param scores a játéklogika osztály pontokat tartalmazó tömbje
     */
    @Override
    public void updateScores(int[] scores) {
                for(int i =0; i < scores.length; ++i)
                {
                    if(scores[i] != -1)
                        System.out.print(COLORS[i] + "Player" + Integer.toString(i+1) + " : " + scores[i] + '\t');
                }
        System.out.println();

    }

    /**
     * ha vége a játéknak, piros Game Over feliratot ír a console-ra
     * @param gameOver tart-e még a játék
     */
    @Override
    public void updateGame(boolean gameOver) {
        this.gameOver = gameOver;
        if(this.gameOver)
            System.out.println(ConsoleColors.RED + "Game over!");
    }

    /**
     * a következő lépés beálíltása
     * @return olyan stringgel tér vissza, ami tartalmazza minden játékos lépésének a billentyűjét
     */
    @Override
    public String getStep() {
        synchronized (nextStep) {
            step = nextStep;
            nextStep = "";
        }
        return step;
    }

    /**
     * egy stringhez hozzáadja minden játékos megnyomott billentyűjét
     * @param s lenyomott billentyű kódja
     */
    public void addStep(String s)
    {
        synchronized (nextStep)
        {
            nextStep = s;
        }
    }
}
