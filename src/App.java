import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class App extends Application{
    public static void main(String[] args) {
        launch(args);
    }
    
     public void start(Stage stage) throws Exception{
        Board matrix = new Board();
        Welcomescene welcome = new Welcomescene();
        GameScene gScene = new GameScene();
        //stage.setScene(gScene.setScene(matrix, stage));
        stage.setScene(welcome.getScene(matrix, stage));
        stage.setTitle("2048");
        stage.show();

        Image image = new Image("download.png");
        stage.getIcons().add(image);
    }
}