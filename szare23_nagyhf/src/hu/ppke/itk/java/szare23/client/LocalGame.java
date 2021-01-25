package hu.ppke.itk.java.szare23.client;

import hu.ppke.itk.java.szare23.gameLogic.Action;
import hu.ppke.itk.java.szare23.gameLogic.Game;
import hu.ppke.itk.java.szare23.gameLogic.LocalPlayer;
import hu.ppke.itk.java.szare23.gameLogic.PlayerCommunicator;

import java.util.ArrayList;

public class LocalGame implements MessageHandler {

    UserInterface userInterface;
    ArrayList<PlayerCommunicator> playerCommunicators;


    public LocalGame() {

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
     * az inputon kapott billentyűk alapján lekezeli, melyik játékosnak mi lesz a következő lépése
     */
    @Override
    public void handle() {
        String step = userInterface.getStep().toUpperCase();
        synchronized (playerCommunicators){
        for (PlayerCommunicator pc: playerCommunicators) {
            switch (pc.getInd())
            {
                case 0:
                {
                    if(step.contains("8"))
                        {pc.setStep(Action.TURN_LEFT); break;}
                    if(step.contains("9"))
                        {pc.setStep(Action.TURN_RIGHT); break;}
                    pc.setStep(Action.FORWARD);
                    break;
                }
                case 1:
                {
                    if(step.contains("N"))
                    {pc.setStep(Action.TURN_LEFT); break;}
                    if(step.contains("M"))
                    {pc.setStep(Action.TURN_RIGHT); break;}
                    pc.setStep(Action.FORWARD);
                    break;
                }
                case 2:
                {
                    if(step.contains("K"))
                    {pc.setStep(Action.TURN_LEFT); break;}
                    if(step.contains("L"))
                    {pc.setStep(Action.TURN_RIGHT); break;}
                    pc.setStep(Action.FORWARD);
                    break;
                }
                case 3:
                {
                    if(step.contains("X"))
                    {pc.setStep(Action.TURN_LEFT); break;}
                    if(step.contains("9"))
                    {pc.setStep(Action.TURN_RIGHT); break;}
                    pc.setStep(Action.FORWARD);
                    break;
                }
                case 4:
                {
                    if(step.contains("A"))
                    {pc.setStep(Action.TURN_LEFT); break;}
                    if(step.contains("S"))
                    {pc.setStep(Action.TURN_RIGHT); break;}
                    pc.setStep(Action.FORWARD);
                    break;
                }
                case 5:
                {
                    if(step.contains("1"))
                    {pc.setStep(Action.TURN_LEFT); break;}
                    if(step.contains("2"))
                    {pc.setStep(Action.TURN_RIGHT); break;}
                    pc.setStep(Action.FORWARD);
                    break;
                }
            }
        }
        playerCommunicators.notifyAll();
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
     * beállítja a játékosok list-jét
     * @param playerCommunicators játékosokat tartalmazó list
     */
    @Override
    public void setPCs(ArrayList<PlayerCommunicator> playerCommunicators) {
        this.playerCommunicators = playerCommunicators;
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
}
