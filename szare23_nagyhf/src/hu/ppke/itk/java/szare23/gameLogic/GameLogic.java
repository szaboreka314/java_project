package hu.ppke.itk.java.szare23.gameLogic;

import java.util.ArrayList;
import java.util.Random;

/**
 * játéklogika osztály, a játék motorja
 */
public class GameLogic implements Runnable {

    protected Integer[][] field = new Integer[30][40]; //-1: nincs ott semmmi/senki, 0-5: játékos, 6: bónusz, 7: lyuk
    protected int[] scores = {-1,-1,-1,-1,-1,-1};

    private boolean withBonuses;
    protected boolean gameOver = false;
    protected int playersInGame;

    protected Game game;

    protected final ArrayList<PlayerCommunicator> playerCommunicators;


    /**
     * játéklogika osztály konstruktora
     * @param playerCommunicators játékosokat tartalmazó list
     * @param withBonuses játék módot beálíltó paraméter
     */
    public GameLogic(ArrayList<PlayerCommunicator> playerCommunicators, boolean withBonuses) {

        this.playerCommunicators = playerCommunicators;
        playersInGame = playerCommunicators.size();
        this.withBonuses = withBonuses;

        ///pálya feltöltése -1-kel
        for(int i =0; i < field.length; ++i)
        {
            for(int j =0; j < field[i].length; ++j)
            {
                field[i][j] = -1;
            }
        }

        //játékosok random helyre helyezése
        Random random = new Random();
        for(PlayerCommunicator pc : this.playerCommunicators)
        {
            int i = pc.getInd();
            scores[i] = 0;
                int posx = random.nextInt(30);
                int posy = random.nextInt(40);
                if(field[posx][posy] == -1) //nincs még ott senki
                {
                    field[posx][posy] = i;
                    pc.setLastPosX(posx);
                    pc.setLastPosY(posy);
                    if(posx < 5)
                        pc.setDirection(2);
                    else if(posx > field.length-5)
                        pc.setDirection(0);
                    else if(posy < 5)
                        pc.setDirection(1);
                    else if(posy > field[0].length-5)
                        pc.setDirection(3);
                    else
                        pc.setDirection(random.nextInt(4));
                }
                else
                {
                    --i;
                }
                //pc.handleScores(scores);
        }

        game = new Game(field, scores);
        for(PlayerCommunicator pc : playerCommunicators)
        {
            pc.setPC(playerCommunicators);
            pc.handle(game);
        }

        new Thread(this).start();

    }

    /**
     * hálózati játék esetén, ha valaki bezárja az ablakot,
     * ez a függvény kezeli, hogy mindenki kap egy pontot
     */
    public void onClose()
    {
        synchronized (playerCommunicators)
        {
            --playersInGame;
            if(playersInGame <= 1)
                gameOver = true;
            for(PlayerCommunicator pc : playerCommunicators)
            {
                if(pc.getInGame())
                {
                    ++scores[pc.getInd()];

                }
            }
            game.setScores(scores);
            for(PlayerCommunicator pc : playerCommunicators)
            {
                if(pc.getInGame())
                {
                    pc.handle(game);
                    pc.handleGame(gameOver);
                }
            }
        }


    }

    /**
     * a játék működéséért felelős függvény
     */
    @Override
    public void run() {
        Random random = new Random();
        while(!gameOver)
        {
            try {
                Thread.sleep(400);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            synchronized (playerCommunicators){
            for (PlayerCommunicator pc: playerCommunicators) {
                if (pc.getInGame()) {
                    pc.handle();
                    int i = pc.getInd();
                    switch (pc.getStep()) //irány és a kapott lépés szerint beállítja a játékos következő pozícióját
                    {
                        case FORWARD: {
                            switch (pc.getDirection()) { //irány alapján nézi; 0:fel, 1:jobbra, 2:le, 3:balra
                                case 0:
                                    pc.setLastPosX(-1);
                                    break;
                                case 1:
                                    pc.setLastPosY(1);
                                    break;
                                case 2:
                                    pc.setLastPosX(1);
                                    break;
                                case 3:
                                    pc.setLastPosY(-1);
                                    break;
                            }
                            break;
                        }
                        case TURN_LEFT: {
                            switch (pc.getDirection()) {
                                case 0:
                                    pc.setLastPosY(-1);
                                    pc.setDirection(3);
                                    break;
                                case 1:
                                    pc.setLastPosX(-1);
                                    pc.setDirection(0);
                                    break;
                                case 2:
                                    pc.setLastPosY(1);
                                    pc.setDirection(1);
                                    break;
                                case 3:
                                    pc.setLastPosX(1);
                                    pc.setDirection(2);
                                    break;
                            }
                            break;
                        }
                        case TURN_RIGHT: {
                            switch (pc.getDirection()) {
                                case 0: {
                                    pc.setLastPosY(1);
                                    pc.setDirection(1);
                                    break;
                                }
                                case 1: {
                                    pc.setLastPosX(1);
                                    pc.setDirection(2);
                                    break;
                                }
                                case 2: {
                                    pc.setLastPosY(-1);
                                    pc.setDirection(3);
                                    break;
                                }
                                case 3: {
                                    pc.setLastPosX(-1);
                                    pc.setDirection(0);
                                    break;
                                }
                            }
                            break;
                        }
                    }
                    // szélek lekezelése
                    if (pc.getLastPosX() <= 0)
                        pc.setLastPosX(field.length);
                    else if (pc.getLastPosX() >= field.length)
                        pc.setLastPosX(-field.length);
                    else if (pc.getLastPosY() <= 0)
                        pc.setLastPosY(field[0].length);
                    else if (pc.getLastPosY() >= field[0].length)
                        pc.setLastPosY(-field[0].length);
                    else //minden más eset, amikor nem szélen van
                    {
                        if (field[pc.getLastPosX()][pc.getLastPosY()] != -1) //van ott valaki
                        {
                            if (field[pc.getLastPosX()][pc.getLastPosY()] == 6) //bónusz random 1-5 között
                            {
                                scores[i] += random.nextInt(5);
                                field[pc.getLastPosX()][pc.getLastPosY()] = pc.getInd();
                            }
                            else if(field[pc.getLastPosX()][pc.getLastPosY()] == 7) //lyuk
                            {
                                ++scores[i];
                                field[pc.getLastPosX()][pc.getLastPosY()] = i;
                            }
                            else //valakinek nekiment és kiesett
                            {
                                int getPoint = 1;
                                pc.setInGame(false);
                                --playersInGame;
                                for(PlayerCommunicator p : playerCommunicators)
                                {
                                    if(p.getInd() != i && p.getLastPosX() == pc.getLastPosX() && p.getLastPosY() == pc.getLastPosY()) // ketten léptek ugyanarra a mezőre
                                    {
                                        p.setInGame(false);
                                        --playersInGame;
                                        getPoint = 2;
                                    }
                                    if(p.getInd() != i)
                                    {
                                        scores[p.getInd()] += getPoint;
                                    }

                                }
                                if(playersInGame <= 1)
                                {
                                    gameOver = true;
                                    for(PlayerCommunicator p : playerCommunicators)
                                        p.handleGame(gameOver);
                                    return;
                                }

                            }
                        }
                        else //üres mező
                        {
                            if (random.nextInt(100) > 90) //10% esély a lyukra
                            {
                                field[pc.getLastPosX()][pc.getLastPosY()] = 7;
                            }
                            else
                            {
                                field[pc.getLastPosX()][pc.getLastPosY()] = i;
                            }
                        }
                    }
                    if(withBonuses)
                    {
                        if(random.nextInt(100)%30 == 0)
                        {
                            int posx = random.nextInt(30);
                            int posy = random.nextInt(40);
                            if(field[posx][posy] == -1)
                                field[posx][posy] = 6;
                        }
                    }

                }
            }
            }
            game.setField(field);
            game.setScores(scores);
            synchronized (playerCommunicators)
            {
                for(PlayerCommunicator pc : playerCommunicators)
                {
                    if(pc.getInGame())
                    {
                        pc.handle(game);
                        pc.handleGame(gameOver);
                    }
                }
            }
        }
    }
}
