import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.animation.PauseTransition;
import javafx.animation.ScaleTransition;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import javafx.stage.Stage;

public class Welcomescene{
        String normalStyle = 
        "-fx-background-color: #f59563;" +
        "-fx-text-fill: white;" +
        "-fx-font-size: 18px;" +
        "-fx-font-weight: bold;" +
        "-fx-padding: 12 30 12 30;" +
        "-fx-background-radius: 6;"+
        "-fx-focus-color: transparent;"+
        "-fx-faint-focus-color: transparent;";

    String hoverStyle = 
        "-fx-background-color: #f37e3a;" +  // ← only this changes
        "-fx-text-fill: beige;" +
        "-fx-font-size: 18px;" +
        "-fx-font-weight: bold;" +
        "-fx-padding: 12 30 12 30;" +
        "-fx-background-radius: 6;";
    String clickStyle = 
        "-fx-background-color: #b85b26;" +  // ← only this changes
        "-fx-text-fill: white;" +
        "-fx-font-size: 18px;" +
        "-fx-font-weight: bold;" +
        "-fx-padding: 12 30 12 30;" +
        "-fx-background-radius: 6;";
        
    public Scene getScene(Board matrix, Stage stage) {
        VBox layout = new VBox();
        layout.setSpacing(20);
        layout.setAlignment(Pos.TOP_CENTER);
        layout.setStyle("-fx-background-color: #e9e6da;");
        Image gif = new Image("file:C:\\Users\\Lenovo\\Desktop\\JAVA\\JavaFx\\HelloFx\\src\\2048.gif");
        ImageView gifView = new ImageView(gif);
        gifView.setFitWidth(300);
        gifView.setFitHeight(300);
        Rectangle cropGif = new Rectangle(50, 50, 200, 250);
        gifView.setClip(cropGif);
        //Label title = new Label("2048");
        //title.setStyle("-fx-font-size : 80px; -fx-font-wieght: bold;-fx-text-fill: #776e65; -fx-font-family: 'Impact';");
        Button startButton = new Button("Start Game");
        startButton.setStyle(normalStyle);
        startButton.setOnMouseEntered(e ->{
            ScaleTransition shrink = new ScaleTransition(Duration.millis(200), startButton);
            startButton.setStyle(hoverStyle);
            shrink.setToX(1.1);
            shrink.setToY(1.1);
            shrink.play();
        });
        startButton.setOnMouseExited(e-> {
            ScaleTransition shrink = new ScaleTransition(Duration.millis(200), startButton);
            startButton.setStyle(normalStyle);
            shrink.setToX(1);
            shrink.setToY(1);
            shrink.play();
        });
        startButton.setOnAction(e->{
            // System.out.println("Successfull");
            boolean isMaximized = stage.isMaximized();
            GameScene gScene = new GameScene();
            stage.setScene(gScene.setScene(matrix, stage));
                stage.setMaximized(false); // reset first
                stage.setMaximized(isMaximized); // then set
            
            startButton.setStyle(clickStyle);
            PauseTransition pause = new PauseTransition(Duration.millis(150));
            pause.setOnFinished(event -> {
                startButton.setStyle(hoverStyle);
            });
            pause.play();
        });

        startButton.setOnMousePressed(e->{
            ScaleTransition shrink = new ScaleTransition(Duration.millis(125), startButton);
            shrink.setToX(1);
            shrink.setToY(1);
            shrink.play();
        });

        startButton.setOnMouseReleased(e->{
            ScaleTransition grow = new ScaleTransition(Duration.millis(125), startButton);
            grow.setToX(1.1);
            grow.setToY(1.1);
            grow.play();
        });

        Label bestLabel = new Label("Best : ");
        Label bestScore = new Label(String.valueOf(matrix.getHighScore()));
        HBox highScore = new HBox(bestLabel, bestScore);
        highScore.setAlignment(Pos.TOP_CENTER);
        highScore.setStyle("-fx-font-size: 25px; -fx-font-family: 'Ariel Black'; -fx-font-weight: bold;");


        layout.getChildren().addAll(gifView, highScore, startButton);
        Scene scene = new Scene(layout,700,700);
        return scene;
    }
}