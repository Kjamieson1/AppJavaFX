import java.util.logging.Logger;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {
    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

    @Override
    public void start(Stage primaryStage) {
        // Create main layout
        VBox mainLayout = new VBox(15);
        mainLayout.setAlignment(Pos.CENTER);
        mainLayout.setPadding(new Insets(30));
        
        // Create Patient Form button
        Button patientFormBtn = new Button("Open Patient Form");
        patientFormBtn.setPrefSize(200, 50);
        patientFormBtn.setOnAction(e -> {
            LOGGER.info("Opening Patient Form...");
            PatientFormGUI patientFormGUI = new PatientFormGUI();
            patientFormGUI.show();
        });
        
        // Create Patient Check-In button
        Button checkInBtn = new Button("Start Patient Check-In");
        checkInBtn.setPrefSize(200, 50);
        checkInBtn.setStyle("-fx-background-color: #28a745; -fx-text-fill: white; -fx-font-size: 14px;");
        checkInBtn.setOnAction(e -> {
            LOGGER.info("Starting Patient Check-In workflow...");
            startCheckInWorkflow(primaryStage);
        });
        
        // Add hover effects
        checkInBtn.setOnMouseEntered(e -> 
            checkInBtn.setStyle("-fx-background-color: #218838; -fx-text-fill: white; -fx-font-size: 14px;"));
        checkInBtn.setOnMouseExited(e -> 
            checkInBtn.setStyle("-fx-background-color: #28a745; -fx-text-fill: white; -fx-font-size: 14px;"));
        
        mainLayout.getChildren().addAll(patientFormBtn, checkInBtn);
        
        Scene scene = new Scene(mainLayout, 350, 250);
        primaryStage.setTitle("JavaFX Demo - Patient Management System");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    /**
     * Starts the patient check-in workflow with popup
     */
    private void startCheckInWorkflow(Stage parentStage) {
        // Show the check-in popup first
        CheckInPopup popup = new CheckInPopup(parentStage, () -> {
            // This callback is executed when the Start button is clicked
            LOGGER.info("Check-in popup Start button clicked - opening check-in GUI");
            
            // Create and show the patient check-in GUI
            PatientCheckInGUI checkInGUI = new PatientCheckInGUI();
            checkInGUI.show();
        });
        
        // Show the popup and wait for user interaction
        boolean startClicked = popup.showAndWait();
        
        if (startClicked) {
            LOGGER.info("Check-in workflow initiated successfully");
        } else {
            LOGGER.info("Check-in workflow cancelled by user");
        }
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
