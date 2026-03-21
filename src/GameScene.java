import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class GameScene {
    private Label scoreValue;
    private Label movesValue;
    private Label bestScoreValue;
    private StackPane gameRoot = new StackPane(); // ← add this
    private BorderPane root = new BorderPane();
    
    String normalStyle = 
        "-fx-background-color: #f59563;" +
        "-fx-text-fill: white;" +
        "-fx-font-size: 30px;" +
        "-fx-font-weight: bold;" +
        "-fx-padding: 5 15 5 15;" +
        "-fx-background-radius: 6;"+
        "-fx-focus-color: transparent;"+
        "-fx-faint-focus-color: transparent;";

    String hoverStyle = 
        "-fx-background-color: #f37e3a;" +  // ← only this changes
        "-fx-text-fill: white;" +
        "-fx-font-size: 30px;" +
        "-fx-font-weight: bold;" +
        "-fx-padding: 5 15 5 15;" +
        "-fx-background-radius: 6;";
    String clickStyle = 
        "-fx-background-color: #b85b26;" +  // ← only this changes
        "-fx-text-fill: white;" +
        "-fx-font-size: 30px;" +
        "-fx-font-weight: bold;" +
        "-fx-padding: 5 15 5 15;" +
        "-fx-background-radius: 6;";
    
    private Color getTileColor(int value){
        switch (value) {
            case 2: return Color.web("#eee0da");
            case 4: return Color.web("#F0E4CC");
            case 8: return Color.web("#F2AB73");
            case 16: return Color.web("#FEA060");
            case 32: return Color.web("#FF8467");
            case 64: return Color.web("#f45128");
            case 128: return Color.web("#efd171");
            case 256: return Color.web("#ebcc4f");
            case 512: return Color.web("#edcc53");
            case 1024: return Color.web("#f2c53e");
            case 2048: return Color.web("#f0bb00");
            case 4096: return Color.web("#3e3e33");
            default: return Color.web("#8c8c8c");
        }
    }
    
    private boolean isMergedTile(int row, int col){
        for(int[] pos : Gamelogic.mergedPositions){
            if(pos[0] == row && pos[1] == col){
                return true;
            }
        }
        return false;
    }

    private Button createButton(String text){
        Button btn = new Button(text);
        btn.setStyle(normalStyle);
        
        btn.setOnMouseEntered(e -> {
            btn.setStyle(hoverStyle);
            ScaleTransition shrink = new ScaleTransition(Duration.millis(200), btn);
            shrink.setToX(1.1);
            shrink.setToY(1.1);
            shrink.play();
        });
        
        btn.setOnMouseExited(e -> {
            btn.setStyle(normalStyle);
            ScaleTransition shrink = new ScaleTransition(Duration.millis(200), btn);
            shrink.setToX(1);
            shrink.setToY(1);
            shrink.play();
        });
        
        btn.setOnMousePressed(e -> {
            btn.setStyle(clickStyle);
            ScaleTransition shrink = new ScaleTransition(Duration.millis(100), btn);
            shrink.setToX(0.95);
            shrink.setToY(0.95);
            shrink.play();
        });
        
        btn.setOnMouseReleased(e -> {
            btn.setStyle(normalStyle);
            ScaleTransition grow = new ScaleTransition(Duration.millis(100), btn);
            grow.setToX(1.0);
            grow.setToY(1.0);
            grow.play();
        });

        return btn;
    }

    private StackPane createOverlay(String gifPath, String message, Board matrix, GridPane gridPane, StackPane[][] matrixPane, Stage stage){
        root.setEffect(new GaussianBlur(10));
        
        StackPane overlay = new StackPane();
        overlay.setStyle("-fx-background-color: rgba(0,0,0,0.5);");
        overlay.setPrefSize(700, 700);
        
        Image gif = new Image("file:" + gifPath);
        ImageView gifView = new ImageView(gif);
        gifView.setFitWidth(300);
        gifView.setFitHeight(300);
        
        Label text = new Label(message);
        text.setStyle("-fx-font-size: 50; -fx-text-fill: yellow;");
        
        Button restartBtn = createButton("  Restart  ");
        restartBtn.setOnAction(e -> {
            new Gamelogic().restartGame(matrix);
            gameRoot.getChildren().remove(overlay); // remove overlay
            root.setEffect(null); // remove blur
            refresh(matrix, gridPane, matrixPane, -1, stage);
        });
        
        Button homeBtn = createButton("  🏠  ");
        homeBtn.setOnAction(e -> {
            boolean isMaximized = stage.isMaximized();
            stage.setScene(new Welcomescene().getScene(matrix, stage));
            stage.setMaximized(false);
            stage.setMaximized(isMaximized);
        });
        
        HBox buttons = new HBox(restartBtn, homeBtn);
        buttons.setAlignment(Pos.CENTER);
        buttons.setSpacing(20);
        
        VBox content = new VBox(gifView, text, buttons);
        content.setAlignment(Pos.CENTER);
        content.setSpacing(20);
        
        overlay.getChildren().add(content);
        return overlay;
    }

    public void refresh(Board matrix, GridPane gridPane, StackPane[][] matrixPane, int XY, Stage stage){
        try {
            int x = XY/5;
            int y = XY%5;
            File myFile = new File("C:\\Users\\Lenovo\\Desktop\\JAVA\\JavaFx\\HelloFx\\src\\savedGame.txt");
            try(Scanner reader = new Scanner(myFile)){
                gridPane.getChildren().clear();
                for(int i=0;i<5;i++){
                    for(int j=0;j<5;j++){
                            if(reader.hasNextInt()){
                                int value = reader.nextInt();
                                Rectangle bgTile = new Rectangle(100, 100);
                                bgTile.setFill(getTileColor(value));
                                bgTile.setArcHeight(15);
                                bgTile.setArcWidth(15);
                                Label number = new Label(value == 0 ? "" : String.valueOf(value));
                                number.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 25px;");
                                // StackPane wrapper = new StackPane();
                                // wrapper.setMinSize(100, 100);
                                // wrapper.setMaxSize(100, 100);
                                StackPane tile = new StackPane(bgTile, number);
                                // wrapper.getChildren().add(tile);
                                matrixPane[i][j] = tile;
                                // Rectangle clip = new Rectangle(100, 100);
                                // wrapper.setClip(clip);
                                gridPane.add(tile, j, i);
                                if(i==x && y==j){
                                    ScaleTransition shrink = new ScaleTransition(Duration.millis(300), matrixPane[x][y]);
                                    shrink.setToX(1.2);
                                    shrink.setToY(1.2);
                                    bgTile.setFill(Color.rgb(255, 233, 185));
                                    shrink.play();
                                    shrink.setOnFinished(e->{
                                        ScaleTransition grow = new ScaleTransition(Duration.millis(300), matrixPane[x][y]);
                                        grow.setToX(1);
                                        grow.setToY(1);
                                        bgTile.setFill(getTileColor(value));
                                        grow.play();
                                    });
                                }
                                if(isMergedTile(i, j)){
                                    // merge animation here
                                    final int row = i;
                                    final int col = j;
                                    ScaleTransition bounce = new ScaleTransition(Duration.millis(150), matrixPane[row][col]);
                                    bounce.setToX(1.2);
                                    bounce.setToY(1.2);
                                    bounce.setAutoReverse(true);
                                    bounce.setCycleCount(2);
                                    bounce.play();
                                }
                            }
                        }
                    }
                scoreValue.setText(String.valueOf(reader.nextInt()));
                movesValue.setText(String.valueOf(reader.nextInt()));
                bestScoreValue.setText(String.valueOf(reader.nextInt()));
                Gamelogic.mergedPositions.clear();
                Gamelogic gamelogic = new Gamelogic();
                isOver(gamelogic, matrix, gridPane, matrixPane, stage);
            }
        } catch (IOException e){
            System.out.println(new File("savedGame.txt").getAbsolutePath());
            System.out.println("Something is Wrong, data is not fetchable.");
        }
    }

    public void matrixScene(Scene scene, Board matrix, GridPane gridPane, StackPane[][] matrixPane, Stage stage){
        Gamelogic gamelogic = new Gamelogic();
        scene.setOnKeyPressed(e->{
            gamelogic.loadGame(matrix);
            switch(e.getCode()){
                case W:
                    System.out.println("W pressed!");
                    int XY = gamelogic.swipeUp(matrix.getMat_pos(), matrix);
                    gamelogic.saveGame(matrix, 5);
                    refresh(matrix, gridPane, matrixPane, XY, stage);
                    break;
                case S:
                    System.out.println("S pressed!");
                    int XY1 = gamelogic.swipeDown(matrix.getMat_pos(), matrix);
                    gamelogic.saveGame(matrix, 5);
                    refresh(matrix, gridPane, matrixPane, XY1, stage);
                    break;
                case A:
                    System.out.println("A pressed!");
                    int XY2 = gamelogic.swipeLeft(matrix.getMat_pos(), matrix);
                    gamelogic.saveGame(matrix, 5);
                    refresh(matrix, gridPane, matrixPane, XY2, stage);
                    break;
                case D:
                    System.out.println("D pressed!");
                    int XY3 = gamelogic.swipeRight(matrix.getMat_pos(), matrix);
                    gamelogic.saveGame(matrix, 5);
                    refresh(matrix, gridPane, matrixPane, XY3, stage);
                    break;
                default:
                    break;
            }
        });
    }
    
    void isOver(Gamelogic gamelogic, Board matrix, GridPane gridPane, StackPane[][] matrixPane, Stage stage){
        switch (gamelogic.isGameOver(matrix)) {
            case 1:
                StackPane overlay = createOverlay("file:C:\\Users\\Lenovo\\Desktop\\JAVA\\JavaFx\\HelloFx\\src\\2048.gif"
                ,"You Win! 🎉" , matrix, gridPane, matrixPane, stage);
                gameRoot.getChildren().add(overlay); // overlay on top!
                break;
            case 3:
                StackPane overlay1 = createOverlay("file:C:\\Users\\Lenovo\\Desktop\\JAVA\\JavaFx\\HelloFx\\src\\game-over-game.gif",
                " 😥 You Are Out of moves 😥 ", matrix, gridPane, matrixPane, stage);
                gameRoot.getChildren().add(overlay1);
                break;
            default:
            }
        }
    
    public Scene setScene(Board matrix, Stage stage) {

        // BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #ccc3a1;");
        Gamelogic gamelogic = new Gamelogic();

        //Label and Title for current Score.
        Label scoreTitle = new Label("Score");
        scoreValue = new Label(String.valueOf(matrix.getScore()));
        scoreValue.setStyle("-fx-font-size: 22px; -fx-text-fill: #c4bb9b; -fx-font-family: 'Ariel'; -fx-font-weight: bold;");
        scoreTitle.setStyle("-fx-font-size: 22px; -fx-text-fill: #f45128; -fx-font-family: 'Ariel'; -fx-font-weight: bold;");
        Rectangle scoreBox = new Rectangle(80, 40);
        scoreBox.setArcHeight(15);
        scoreBox.setArcWidth(15);
        scoreBox.setFill(Color.rgb(244, 81, 40));
        StackPane numberBox = new StackPane(scoreBox, scoreValue);
        VBox scoreArea = new VBox(scoreTitle, numberBox);
        scoreArea.setSpacing(5);
        scoreArea.setAlignment(Pos.CENTER);
        
        //Label and Title for current Moves.
        Label movesTitle = new Label("Moves");
        movesValue = new Label(String.valueOf(matrix.getMoves()));
        movesTitle.setStyle("-fx-font-size: 22px; -fx-font-family: 'Ariel Black'; -fx-font-weight: bold;");
        movesValue.setStyle("-fx-font-size: 22px; -fx-text-fill: #c4bb9b; -fx-font-family: 'Ariel'; -fx-font-weight: bold;");
        Rectangle movesBox = new Rectangle(80, 40);
        movesBox.setArcHeight(15);
        movesBox.setArcWidth(15);
        movesBox.setFill(Color.rgb(91, 81, 38));
        StackPane movesNumberBox = new StackPane(movesBox, movesValue);
        VBox movesArea = new VBox(movesTitle, movesNumberBox);
        movesArea.setSpacing(5);
        movesArea.setAlignment(Pos.CENTER);
        
        //Label and Title for current Best Score.
        Label bestScoreTitle = new Label("Best");
        bestScoreValue = new Label(String.valueOf(matrix.getHighScore()));
        bestScoreTitle.setStyle("-fx-font-size: 22px; -fx-text-fill: #361212; -fx-font-family: 'Ariel Black'; -fx-font-weight: bold;");
        bestScoreValue.setStyle("-fx-font-size: 22px; -fx-text-fill: #c4bb9b; -fx-font-family: 'Ariel'; -fx-font-weight: bold;");
        Rectangle bestScoreBox = new Rectangle(80, 40);
        bestScoreBox.setArcHeight(15);
        bestScoreBox.setArcWidth(15);
        bestScoreBox.setFill(Color.rgb(54, 18, 18));
        StackPane bestNumberBox = new StackPane(bestScoreBox, bestScoreValue);
        VBox bestScoreArea = new VBox(bestScoreTitle, bestNumberBox);
        bestScoreArea.setSpacing(5);
        bestScoreArea.setAlignment(Pos.CENTER);

        Button backButton = createButton("  🏠  ");
        HBox.setMargin(backButton, new Insets(20, 0, 3, 0));
        backButton.setOnAction(ev -> {
            Welcomescene welcomescene = new Welcomescene();
            boolean isMaximized = stage.isMaximized();
            stage.setScene(welcomescene.getScene(matrix, stage));
                stage.setMaximized(false); // reset first
                stage.setMaximized(isMaximized); // then set
        });
        
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setAlignment(Pos.CENTER);
        
        StackPane[][] matrixPane = new StackPane[5][5];
        
        Button restartButton = createButton("     Restart     ");
        restartButton.setOnAction(event->{
            gamelogic.restartGame(matrix);
            refresh(matrix, gridPane, matrixPane, -1, stage);
        });
        
        refresh(matrix, gridPane, matrixPane, -1, stage);
        
        HBox topbar = new HBox(scoreArea, movesArea, bestScoreArea, backButton);
        HBox bottomBar = new HBox(restartButton);
        topbar.setPadding(new Insets(25, 45, 15, 45));
        topbar.setAlignment(Pos.TOP_CENTER);
        topbar.setSpacing(40);
        bottomBar.setPadding(new Insets(8, 40, 40, 40));
        bottomBar.setAlignment(Pos.CENTER);
        bottomBar.setSpacing(40);
        root.setTop(topbar);
        root.setCenter(gridPane);
        root.setBottom(bottomBar);
        PauseTransition ppause = new PauseTransition(Duration.millis(10));
        ppause.setOnFinished(e->{
            isOver(gamelogic, matrix, gridPane, matrixPane, stage);
        });
        ppause.play();

        gameRoot.getChildren().add(root);
        Scene scene = new Scene(gameRoot,700,700);
        matrixScene(scene, matrix, gridPane, matrixPane, stage);

        return scene;
    }
}