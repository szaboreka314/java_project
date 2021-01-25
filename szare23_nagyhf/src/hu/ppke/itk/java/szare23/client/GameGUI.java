package hu.ppke.itk.java.szare23.client;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

/**
 * grafikus UI felület
 */

public class GameGUI extends HBox implements UserInterface{

    private Label[] playersLabel;
    private final Color[] COLORS = {Color.RED, Color.YELLOW, Color.BLUE, Color.GREEN, Color.DEEPPINK, Color.TURQUOISE};
    private Label labelGameOver;

    private VBox vBoxLeft, vBoxRight;
    private Pane paneField;

    protected String step = new String("");
    protected String nextStep = new String("");

    /**
     * az osztály konstruktora
     * itt állítok be minden grafikai dolgot
     */
    public GameGUI()
    {
        paneField = new Pane();
        vBoxRight = new VBox(20);
        vBoxLeft = new VBox(20);
        setSpacing(30);
        vBoxRight.setMinWidth(180);
        vBoxLeft.setMinWidth(410);
        vBoxLeft.setMinHeight(370);
        paneField.setStyle("-fx-border-style: solid outside; -fx-border-width: 5; -fx-border-color: grey;");
        paneField.setMinSize(400,300);
        paneField.setTranslateX(10);
        paneField.setTranslateY(50);
        vBoxRight.setTranslateY(50);

        playersLabel = new Label[6];
        for(int i = 0; i < playersLabel.length; ++i)
        {
            Label label = new Label("");
            label.setFont(Font.font("Arial",15));
            label.setTextFill(COLORS[i]);
            playersLabel[i] = label;
            vBoxRight.getChildren().add(label);
        }

        labelGameOver = new Label("Game Over!");
        labelGameOver.setFont(Font.font("Arial",25));
        labelGameOver.setTextFill(Color.RED);
        labelGameOver.setVisible(false);
        labelGameOver.setTranslateX(130);
        labelGameOver.setTranslateY(120);

        paneField.getChildren().add(labelGameOver);
        vBoxLeft.getChildren().add(paneField);
        getChildren().addAll(vBoxLeft, vBoxRight);

        addEventFilter(KeyEvent.KEY_PRESSED, this::keyEventHandler);
        addEventFilter(KeyEvent.KEY_RELEASED, ev->step="");
    }

    /**
     * billentyű kezelő függvény
     * @param ev gombnyomást vár
     */
    protected void keyEventHandler(KeyEvent ev)
    {
        if( ev.getCode() == KeyCode.N || ev.getCode() == KeyCode.K || ev.getCode() == KeyCode.X || ev.getCode() == KeyCode.A
                || ev.getCode() == KeyCode.M || ev.getCode() == KeyCode.L || ev.getCode() == KeyCode.C || ev.getCode() == KeyCode.S)
        {
            addStep(String.valueOf(ev.getCode()));
        }
        if(ev.getCode() == KeyCode.DIGIT8)
        {
            nextStep += "8";
        }
        if(ev.getCode() == KeyCode.DIGIT9)
        {
            nextStep += "9";
        }
        if(ev.getCode() == KeyCode.DIGIT1)
        {
            nextStep += "1";
        }
        if(ev.getCode() == KeyCode.DIGIT2)
        {
            nextStep += "2";
        }
    }

    /**
     * a grafikus felület frissítése
     * @param field a játékterület mátrixa
     */
    @Override
    public void updateUI(Integer[][] field) {
       Platform.runLater(new Runnable() {
           @Override
           public void run() {
               paneField.getChildren().removeAll();
               for(int i = 0; i < field.length; ++i)
               {
                   for(int j = 0; j < field[i].length; ++j)
                   {
                       Rectangle rectangle;
                       if(field[i][j] >= 0 && field[i][j] < 6)
                           rectangle = new Rectangle(10,10,COLORS[field[i][j]]);
                       else if(field[i][j] == 6) //bónusz
                           rectangle = new Rectangle(10,10,Color.WHITE);
                       else
                           rectangle = new Rectangle(0,0,Color.BLACK);
                       rectangle.relocate(j*10,i*10);
                       paneField.getChildren().add(rectangle);
                   }
               }
           }
       });
    }

    /**
     * a játékosok pontjainak frissítése
     * @param scores a játéklogika osztály pontokat tartalmazó tömbje
     */
    @Override
    public void updateScores(int[] scores) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                for(int i =0; i < scores.length; ++i)
                {
                    playersLabel[i].setText("Player" + Integer.toString(i+1) + ": " + '\t' +  scores[i]);
                    if(scores[i] == -1)
                        playersLabel[i].setVisible(false);
                }
            }
        });
    }

    /**
     * ha vége a játéknak, megjelenít egy Game Over feliratot
     * @param gameOver tart-e még a játék
     */
    @Override
    public void updateGame(boolean gameOver) {
        if(gameOver)
        {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    labelGameOver.setVisible(true);
                }
            });
        }

    }

    /**
     * a következő lépés beálíltása
     * @return olyan stringgel tér vissza, ami tartalmazza minden játékos lépésének a billentyűjét
     */
    @Override
    public String getStep() {
        //System.out.println(nextStep);
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
    public void addStep(String s) {
        synchronized (nextStep) {
            nextStep += s;
        }
    }


}
