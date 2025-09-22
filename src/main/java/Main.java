
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
       DrawingController controller = new DrawingController();

        BorderPane root = controller.buildUI();
        Scene scene = new Scene(root, 800, 600);

        primaryStage.setTitle("The Funnest Drawing App");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
}
