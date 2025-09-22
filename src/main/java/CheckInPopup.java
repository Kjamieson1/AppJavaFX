import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * Popup window that initiates the patient check-in process.
 * This modal dialog displays a message asking staff to check in the next patient
 * and provides a Start button to begin the check-in workflow.
 */
public class CheckInPopup {
    
    private Stage popupStage;
    private boolean startButtonClicked;
    private Runnable onStartCallback;
    
    /**
     * Constructor to create the check-in popup
     * @param parentStage The parent stage (can be null)
     */
    public CheckInPopup(Stage parentStage) {
        initializePopup(parentStage);
    }
    
    /**
     * Constructor with callback for when Start button is clicked
     * @param parentStage The parent stage (can be null)
     * @param onStartCallback Callback to execute when Start is clicked
     */
    public CheckInPopup(Stage parentStage, Runnable onStartCallback) {
        this.onStartCallback = onStartCallback;
        initializePopup(parentStage);
    }
    
    private void initializePopup(Stage parentStage) {
        this.startButtonClicked = false;
        
        // Create the popup stage
        popupStage = new Stage();
        popupStage.initStyle(StageStyle.UTILITY);
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.setTitle("Patient Check-In");
        popupStage.setResizable(false);
        
        // Set parent if provided
        if (parentStage != null) {
            popupStage.initOwner(parentStage);
        }
        
        // Create the main layout
        VBox mainLayout = new VBox(20);
        mainLayout.setAlignment(Pos.CENTER);
        mainLayout.setPadding(new Insets(30, 40, 30, 40));
        mainLayout.setStyle("-fx-background-color: #f0f8ff; -fx-border-color: #4682b4; -fx-border-width: 2;");
        
        // Create the main message label
        Label messageLabel = new Label("Please check in your next patient");
        messageLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        messageLabel.setStyle("-fx-text-fill: #2c3e50;");
        
        // Create instruction label
        Label instructionLabel = new Label("Click 'Start' to begin the check-in process");
        instructionLabel.setFont(Font.font("Arial", 12));
        instructionLabel.setStyle("-fx-text-fill: #34495e;");
        
        // Create the Start button
        Button startButton = new Button("Start");
        startButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        startButton.setPrefSize(100, 40);
        startButton.setStyle(
            "-fx-background-color: #3498db; " +
            "-fx-text-fill: white; " +
            "-fx-background-radius: 5; " +
            "-fx-border-radius: 5; " +
            "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.3), 2, 0, 0, 1);"
        );
        
        // Add hover effect to Start button
        startButton.setOnMouseEntered(e -> 
            startButton.setStyle(
                "-fx-background-color: #2980b9; " +
                "-fx-text-fill: white; " +
                "-fx-background-radius: 5; " +
                "-fx-border-radius: 5; " +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.4), 3, 0, 0, 2);"
            )
        );
        
        startButton.setOnMouseExited(e -> 
            startButton.setStyle(
                "-fx-background-color: #3498db; " +
                "-fx-text-fill: white; " +
                "-fx-background-radius: 5; " +
                "-fx-border-radius: 5; " +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.3), 2, 0, 0, 1);"
            )
        );
        
        // Set button action
        startButton.setOnAction(e -> handleStartButtonClick());
        
        // Create Cancel button (optional)
        Button cancelButton = new Button("Cancel");
        cancelButton.setFont(Font.font("Arial", 12));
        cancelButton.setPrefSize(80, 30);
        cancelButton.setStyle(
            "-fx-background-color: #95a5a6; " +
            "-fx-text-fill: white; " +
            "-fx-background-radius: 5; " +
            "-fx-border-radius: 5;"
        );
        
        cancelButton.setOnMouseEntered(e -> 
            cancelButton.setStyle(
                "-fx-background-color: #7f8c8d; " +
                "-fx-text-fill: white; " +
                "-fx-background-radius: 5; " +
                "-fx-border-radius: 5;"
            )
        );
        
        cancelButton.setOnMouseExited(e -> 
            cancelButton.setStyle(
                "-fx-background-color: #95a5a6; " +
                "-fx-text-fill: white; " +
                "-fx-background-radius: 5; " +
                "-fx-border-radius: 5;"
            )
        );
        
        cancelButton.setOnAction(e -> handleCancelButtonClick());
        
        // Add components to layout
        mainLayout.getChildren().addAll(
            messageLabel,
            instructionLabel,
            startButton,
            cancelButton
        );
        
        // Create and set the scene
        Scene scene = new Scene(mainLayout, 350, 200);
        popupStage.setScene(scene);
        
        // Center the popup on screen
        popupStage.centerOnScreen();
    }
    
    /**
     * Handle Start button click
     */
    private void handleStartButtonClick() {
        startButtonClicked = true;
        
        // Execute callback if provided
        if (onStartCallback != null) {
            onStartCallback.run();
        }
        
        // Close the popup
        popupStage.close();
    }
    
    /**
     * Handle Cancel button click
     */
    private void handleCancelButtonClick() {
        startButtonClicked = false;
        popupStage.close();
    }
    
    /**
     * Show the popup and wait for user interaction
     * @return true if Start was clicked, false if Cancel was clicked or popup was closed
     */
    public boolean showAndWait() {
        popupStage.showAndWait();
        return startButtonClicked;
    }
    
    /**
     * Show the popup without blocking
     */
    public void show() {
        popupStage.show();
    }
    
    /**
     * Close the popup
     */
    public void close() {
        popupStage.close();
    }
    
    /**
     * Check if the Start button was clicked
     * @return true if Start was clicked, false otherwise
     */
    public boolean wasStartButtonClicked() {
        return startButtonClicked;
    }
    
    /**
     * Set the callback to execute when Start button is clicked
     * @param callback The callback to execute
     */
    public void setOnStartCallback(Runnable callback) {
        this.onStartCallback = callback;
    }
    
    /**
     * Get the popup stage (useful for positioning or additional configuration)
     * @return The popup stage
     */
    public Stage getPopupStage() {
        return popupStage;
    }
    
    /**
     * Set custom message text
     * @param message The message to display
     */
    public void setMessage(String message) {
        // This would require keeping a reference to the label
        // For now, the message is fixed during initialization
        // Could be enhanced to support dynamic messages
    }
    
    /**
     * Static convenience method to show popup and get result
     * @param parentStage The parent stage
     * @return true if Start was clicked, false otherwise
     */
    public static boolean showCheckInPrompt(Stage parentStage) {
        CheckInPopup popup = new CheckInPopup(parentStage);
        return popup.showAndWait();
    }
    
    /**
     * Static convenience method to show popup with callback
     * @param parentStage The parent stage
     * @param onStartCallback Callback to execute when Start is clicked
     */
    public static void showCheckInPrompt(Stage parentStage, Runnable onStartCallback) {
        CheckInPopup popup = new CheckInPopup(parentStage, onStartCallback);
        popup.show();
    }
}