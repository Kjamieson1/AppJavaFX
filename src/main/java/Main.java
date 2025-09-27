import java.util.logging.Logger;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class Main extends Application {
    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

    @Override
    public void start(Stage primaryStage) {
        // Create main layout with modern styling
        VBox mainLayout = new VBox(20);
        mainLayout.setAlignment(Pos.CENTER);
        mainLayout.setPadding(new Insets(40));
        mainLayout.setStyle(
            "-fx-background: linear-gradient(to bottom, #e8f5e8, #f0f9f0);" // Light green gradient background
        );
        
        // Create header section
        VBox headerSection = createHeaderSection();
        
        // Create button container with modern card styling
        VBox buttonContainer = new VBox(15);
        buttonContainer.setAlignment(Pos.CENTER);
        buttonContainer.setPadding(new Insets(30));
        buttonContainer.setStyle(
            "-fx-background-color: white;" +
            "-fx-background-radius: 15;" +
            "-fx-border-radius: 15;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 20, 0, 0, 5);" +
            "-fx-border-color: #d4edda;" +
            "-fx-border-width: 1;"
        );
        
        // Create Patient Form button with modern green styling
        Button patientFormBtn = createModernButton(
            "📋 Patient Information Form",
            "#28a745", "#1e7e34", // Primary green colors
            200, 55
        );
        patientFormBtn.setOnAction(e -> {
            LOGGER.info("Opening Patient Form...");
            PatientFormGUI patientFormGUI = new PatientFormGUI();
            patientFormGUI.show();
        });
        
        // Create Patient Check-In button with accent green
        Button checkInBtn = createModernButton(
            "🏥 Start Patient Check-In",
            "#20c997", "#17a085", // Teal green colors
            200, 55
        );
        checkInBtn.setOnAction(e -> {
            LOGGER.info("Starting Patient Check-In workflow...");
            startCheckInWorkflow(primaryStage);
        });
        
        // Create View Saved Patients button with purple accent
        Button viewPatientsBtn = createModernButton(
            "� View Saved Patients",
            "#6f42c1", "#563d7c", // Purple accent for variety
            200, 55
        );
        viewPatientsBtn.setOnAction(e -> {
            LOGGER.info("Opening View Saved Patients...");
            PatientListViewGUI listView = new PatientListViewGUI();
            listView.show();
        });
        
        buttonContainer.getChildren().addAll(patientFormBtn, checkInBtn, viewPatientsBtn);
        
        mainLayout.getChildren().addAll(headerSection, buttonContainer);
        
        Scene scene = new Scene(mainLayout, 450, 550);
        primaryStage.setTitle("HealthCare Pro - Patient Management System");
        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
        primaryStage.show();
    }
    
    /**
     * Create modern header section
     */
    private VBox createHeaderSection() {
        VBox header = new VBox(10);
        header.setAlignment(Pos.CENTER);
        header.setPadding(new Insets(20));
        
        // Main titlef
        Label titleLabel = new Label("HealthCare Pro");
        titleLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 32));
        titleLabel.setStyle("-fx-text-fill: #1b5e20;"); // Dark green
        
        // Subtitle
        Label subtitleLabel = new Label("Patient Management System");
        subtitleLabel.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 16));
        subtitleLabel.setStyle("-fx-text-fill: #388e3c;"); // Medium green
        
        // Version/status indicator
        Label versionLabel = new Label("v2.0 • Professional Edition");
        versionLabel.setFont(Font.font("Segoe UI", FontWeight.LIGHT, 12));
        versionLabel.setStyle("-fx-text-fill: #81c784;"); // Light green
        
        header.getChildren().addAll(titleLabel, subtitleLabel, versionLabel);
        return header;
    }
    
    /**
     * Create a modern styled button
     */
    private Button createModernButton(String text, String primaryColor, String hoverColor, double width, double height) {
        Button button = new Button(text);
        button.setPrefSize(width, height);
        button.setFont(Font.font("Segoe UI", FontWeight.MEDIUM, 14));
        
        // Base styling
        button.setStyle(
            "-fx-background-color: " + primaryColor + ";" +
            "-fx-text-fill: white;" +
            "-fx-background-radius: 12;" +
            "-fx-border-radius: 12;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 8, 0, 0, 2);" +
            "-fx-cursor: hand;"
        );
        
        // Hover effects
        button.setOnMouseEntered(e -> 
            button.setStyle(
                "-fx-background-color: " + hoverColor + ";" +
                "-fx-text-fill: white;" +
                "-fx-background-radius: 12;" +
                "-fx-border-radius: 12;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 12, 0, 0, 4);" +
                "-fx-cursor: hand;" +
                "-fx-scale-x: 1.02;" +
                "-fx-scale-y: 1.02;"
            )
        );
        
        button.setOnMouseExited(e -> 
            button.setStyle(
                "-fx-background-color: " + primaryColor + ";" +
                "-fx-text-fill: white;" +
                "-fx-background-radius: 12;" +
                "-fx-border-radius: 12;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 8, 0, 0, 2);" +
                "-fx-cursor: hand;" +
                "-fx-scale-x: 1.0;" +
                "-fx-scale-y: 1.0;"
            )
        );
        
        return button;
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
    
    /**
     * Show a dialog with all saved patients
     */
    private void showSavedPatientsDialog(Stage parentStage) {
        PatientDataStorage storage = PatientDataStorage.getInstance();
        
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
        alert.initOwner(parentStage);
        alert.setTitle("Saved Patients");
        alert.setHeaderText("Patient Data Storage");
        
        if (storage.isEmpty()) {
            alert.setContentText("No patients have been saved yet.\n\n" +
                "Use 'Open Patient Form' or 'Start Patient Check-In' to add patients, " +
                "then click 'Save and Leave Patient' to save them to storage.");
        } else {
            StringBuilder content = new StringBuilder();
            content.append(storage.getStorageStatistics()).append("\n");
            content.append(storage.getAllPatientsSummary());
            alert.setContentText(content.toString());
        }
        
        // Make the dialog resizable and larger for better readability
        alert.setResizable(true);
        alert.getDialogPane().setPrefSize(600, 400);
        
        alert.showAndWait();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
