package hu.ppke.itk.java.szare23;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

/**
 * Grafikus játékot indító Main osztály
 */
public class MainGraphic extends Application {

    private static final String BUTTON_STYLE = "-fx-border-color: #1b9c8e; -fx-border-width: 5px; -fx-background-color: #000000; -fx-text-fill: #1b9c8e; -fx-font-weight: bold;";
    private static final String HOVERED_BUTTON_STYLE = "-fx-border-color: #bfa127; -fx-border-width: 5px; -fx-background-color: #000000; -fx-text-fill: #bfa127; -fx-font-weight: bold;";

    private VBox root;
    private Button buttonLocal, buttonNetwork;
    private AnchorPane anchorPane;
    private HBox hBox;
    protected MenuBar mBar;
    protected Menu mGame;
    protected MenuItem mNewGame, mExit;

    protected NewLocalGame newLocalGame = null;
    protected NewNetworkGame newNetworkGame = null;

    @Override
    public void start(Stage primaryStage) throws Exception {
        newLocalGame = new NewLocalGame();
        newNetworkGame = new NewNetworkGame();
        root = new VBox();
        hBox = new HBox(50);
        primaryStage.setTitle("JATacka");
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.setResizable(false);
        root.setStyle("-fx-background-color: #000000;");

        ////////////menubar///////////
        mBar = new MenuBar();
        mGame = new Menu("Game");
        mBar.getMenus().add(mGame);

        //mNewGame = new MenuItem("New Game");
        mExit = new MenuItem("Exit");
        mGame.getItems().addAll(mExit);

        root.getChildren().add(mBar);
        root.getChildren().add(new Pane());

        primaryStage.setOnCloseRequest(event -> System.exit(0));
        mExit.setOnAction(event -> System.exit(0));


        ////////buttons settigns///////////
        buttonLocal = new Button("Local");
        buttonSettings(buttonLocal);
        buttonNetwork = new Button("Network");
        buttonSettings(buttonNetwork);

        buttonLocal.setOnAction(event->{
            newLocalGame.startGUI();
            root.getChildren().set(2,newLocalGame);
            newLocalGame.requestFocus();
            hBox.getChildren().removeAll(buttonLocal, buttonNetwork);
            //meghívja a megfelelő local game részeket
        });

        buttonNetwork.setOnAction(event->{
            newNetworkGame.startGUI();
            root.getChildren().set(2,newNetworkGame);
            newNetworkGame.requestFocus();
            hBox.getChildren().removeAll(buttonLocal, buttonNetwork);
            //meghívja a megfelelő network game részeket
        });

        hBox.getChildren().addAll(buttonLocal,buttonNetwork);
        anchorPane = new AnchorPane(hBox);
        AnchorPane.setLeftAnchor(hBox,180.0);
        AnchorPane.setTopAnchor(hBox, 150.0);
        root.getChildren().add(anchorPane);



        primaryStage.show();
    }

    protected void buttonSettings(Button btn)
    {
        btn.setMinWidth(100);
        btn.setMinHeight(50);
        btn.setStyle(BUTTON_STYLE);
        btn.setOnMouseEntered(event->btn.setStyle(HOVERED_BUTTON_STYLE));
        btn.setOnMouseExited(event->btn.setStyle(BUTTON_STYLE));
    }

    public static void main(String[] args) {
        launch(args);
    }
}
