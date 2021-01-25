package hu.ppke.itk.java.szare23;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * console-os játékot indító Main osztály
 */
public class MainConsole {
    public static void main(String[] args)
    {
        System.out.println("What do you want to play? (local/network)");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        try {
            String line = br.readLine();
            if(line.toLowerCase().equals("local"))
            {
                NewLocalGame newLocalGame = new NewLocalGame();
                newLocalGame.startText();
            }
            else if(line.toLowerCase().equals("network"))
            {
                NewNetworkGame newNetworkGame = new NewNetworkGame();
                newNetworkGame.startText();
            }
            else
                System.out.println("There is no game like that!");
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
