import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.logging.Logger;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

/**
 * Main GUI for patient check-in workflow.
 * This window appears after the check-in popup and guides staff through
 * the complete patient check-in process using the CheckInWorkflow.
 */
public class PatientCheckInGUI {
    
    private static final Logger LOGGER = Logger.getLogger(PatientCheckInGUI.class.getName());
    
    private Stage stage;
    private CheckInWorkflow checkInWorkflow;
    
    // Step tracking
    private int currentStep;
    private final int totalSteps = 7;
    
    // UI Components for each step
    private VBox mainLayout;
    private Label stepLabel;
    private Label progressLabel;
    private VBox contentArea;
    private HBox buttonArea;
    private Button nextButton;
    private Button backButton;
    private Button completeButton;
    
    // Step 1: Patient Identification
    private TextField firstNameField;
    private TextField lastNameField;
    private TextField dobField;
    
    // Step 2: Insurance Verification
    private TextField insuranceProviderField;
    private TextField policyNumberField;
    private TextField groupNumberField;
    
    // Step 3: Appointment Confirmation
    private DatePicker appointmentDatePicker;
    private TextField appointmentTimeField;
    private TextField doctorNameField;
    private ComboBox<String> appointmentTypeCombo;
    
    // Step 4: Contact Information Update
    private TextField phoneField;
    private TextField emailField;
    private TextField addressField;
    private TextField emergencyContactField;
    
    // Step 5: Payment Processing
    private TextField copayAmountField;
    private ComboBox<String> paymentMethodCombo;
    private TextField referenceNumberField;
    
    // Step 6: Health Screening
    private TextField temperatureField;
    private TextArea symptomsArea;
    private CheckBox recentTravelCheckBox;
    private CheckBox covidExposureCheckBox;
    
    // Step 7: Check-in Completion
    private ComboBox<String> waitingAreaCombo;
    private TextArea specialInstructionsArea;
    private TextArea summaryArea;
    
    /**
     * Constructor
     */
    public PatientCheckInGUI() {
        this.checkInWorkflow = new CheckInWorkflow();
        this.currentStep = 1;
        initializeGUI();
    }
    
    /**
     * Constructor with existing patient
     */
    public PatientCheckInGUI(NewPatient patient) {
        this.checkInWorkflow = new CheckInWorkflow(patient);
        this.currentStep = 1;
        initializeGUI();
    }
    
    private void initializeGUI() {
        stage = new Stage();
        stage.setTitle("Patient Check-In System");
        
        // Create main layout with modern gradient background
        mainLayout = new VBox(20);
        mainLayout.setPadding(new Insets(25));
        mainLayout.setStyle(
            "-fx-background-color: linear-gradient(to bottom, #e8f5e8, #f1f8e9);"
        );
        
        // Create header section
        createHeaderSection();
        
        // Create content area with modern card styling
        contentArea = new VBox(15);
        contentArea.setPadding(new Insets(25));
        contentArea.setStyle(
            "-fx-background-color: white;" +
            "-fx-background-radius: 15;" +
            "-fx-border-radius: 15;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 20, 0, 0, 5);" +
            "-fx-border-color: #c8e6c9;" +
            "-fx-border-width: 1;"
        );
        
        // Create button area
        createButtonArea();
        
        // Add to main layout
        mainLayout.getChildren().addAll(
            createHeaderSection(),
            contentArea,
            buttonArea
        );
        
        // Create scroll pane with modern styling
        ScrollPane scrollPane = new ScrollPane(mainLayout);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefViewportHeight(750);
        scrollPane.setStyle(
            "-fx-background: transparent;" +
            "-fx-background-color: transparent;"
        );
        
        // Load first step
        loadCurrentStep();
        
        // Create scene and show
        Scene scene = new Scene(scrollPane, 900, 800);
        stage.setScene(scene);
        stage.show();
    }
    
    private VBox createHeaderSection() {
        VBox header = new VBox(15);
        header.setPadding(new Insets(20));
        header.setStyle(
            "-fx-background-color: linear-gradient(to bottom, #2e7d32, #388e3c);" +
            "-fx-background-radius: 15;" +
            "-fx-border-radius: 15;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 15, 0, 0, 3);"
        );
        
        // Create top row with title and save button
        HBox topRow = new HBox();
        topRow.setAlignment(Pos.CENTER_LEFT);
        topRow.setSpacing(20);
        
        // Title with icon
        Label titleLabel = new Label("🏥 Patient Check-In System");
        titleLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 24));
        titleLabel.setStyle("-fx-text-fill: white;");
        
        // Spacer to push save button to the right
        Region spacer = new Region();
        HBox.setHgrow(spacer, javafx.scene.layout.Priority.ALWAYS);
        
        // Create modern "Save and Leave Patient" button for top right
        Button saveAndLeaveButton = new Button("💾 Save and Leave Patient");
        saveAndLeaveButton.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
        saveAndLeaveButton.setPrefSize(220, 45);
        saveAndLeaveButton.setStyle(
            "-fx-background-color: linear-gradient(to bottom, #ffa726, #ff9800);" +
            "-fx-text-fill: white;" +
            "-fx-background-radius: 22;" +
            "-fx-border-radius: 22;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0, 0, 3);" +
            "-fx-cursor: hand;"
        );
        
        // Add modern hover effect
        saveAndLeaveButton.setOnMouseEntered(e -> 
            saveAndLeaveButton.setStyle(
                "-fx-background-color: linear-gradient(to bottom, #ff9800, #f57c00);" +
                "-fx-text-fill: white;" +
                "-fx-background-radius: 22;" +
                "-fx-border-radius: 22;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 15, 0, 0, 5);" +
                "-fx-cursor: hand;" +
                "-fx-scale-x: 1.05;" +
                "-fx-scale-y: 1.05;"
            )
        );
        
        saveAndLeaveButton.setOnMouseExited(e -> 
            saveAndLeaveButton.setStyle(
                "-fx-background-color: linear-gradient(to bottom, #ffa726, #ff9800);" +
                "-fx-text-fill: white;" +
                "-fx-background-radius: 22;" +
                "-fx-border-radius: 22;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0, 0, 3);" +
                "-fx-cursor: hand;" +
                "-fx-scale-x: 1.0;" +
                "-fx-scale-y: 1.0;"
            )
        );
        
        topRow.getChildren().addAll(titleLabel, spacer, saveAndLeaveButton);
        
        // Set up save button action
        saveAndLeaveButton.setOnAction(e -> saveAndLeavePatient());
        
        // Create step info section
        VBox stepInfo = new VBox(5);
        stepInfo.setAlignment(Pos.CENTER);
        
        stepLabel = new Label();
        stepLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        stepLabel.setStyle("-fx-text-fill: white;");
        
        progressLabel = new Label();
        progressLabel.setFont(Font.font("Arial", 12));
        progressLabel.setStyle("-fx-text-fill: #cce7ff;");
        
        stepInfo.getChildren().addAll(stepLabel, progressLabel);
        
        header.getChildren().addAll(topRow, stepInfo);
        return header;
    }
    
    private void createButtonArea() {
        buttonArea = new HBox(20);
        buttonArea.setAlignment(Pos.CENTER_RIGHT);
        buttonArea.setPadding(new Insets(25, 0, 0, 0));
        
        // Modern Back Button
        backButton = new Button("← Back");
        backButton.setPrefSize(120, 45);
        backButton.setStyle(
            "-fx-background-color: linear-gradient(to bottom, #6c757d, #5a6268);" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 14px;" +
            "-fx-font-weight: bold;" +
            "-fx-background-radius: 22;" +
            "-fx-border-radius: 22;" +
            "-fx-cursor: hand;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 8, 0, 0, 2);"
        );
        
        // Back button hover effect
        backButton.setOnMouseEntered(e -> backButton.setStyle(
            "-fx-background-color: linear-gradient(to bottom, #5a6268, #495057);" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 14px;" +
            "-fx-font-weight: bold;" +
            "-fx-background-radius: 22;" +
            "-fx-border-radius: 22;" +
            "-fx-cursor: hand;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 12, 0, 0, 3);" +
            "-fx-scale-x: 1.05;" +
            "-fx-scale-y: 1.05;"
        ));
        
        backButton.setOnMouseExited(e -> backButton.setStyle(
            "-fx-background-color: linear-gradient(to bottom, #6c757d, #5a6268);" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 14px;" +
            "-fx-font-weight: bold;" +
            "-fx-background-radius: 22;" +
            "-fx-border-radius: 22;" +
            "-fx-cursor: hand;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 8, 0, 0, 2);" +
            "-fx-scale-x: 1.0;" +
            "-fx-scale-y: 1.0;"
        ));
        
        backButton.setOnAction(e -> goToPreviousStep());
        
        // Modern Next Button
        nextButton = new Button("Next →");
        nextButton.setPrefSize(120, 45);
        nextButton.setStyle(
            "-fx-background-color: linear-gradient(to bottom, #66bb6a, #4caf50);" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 14px;" +
            "-fx-font-weight: bold;" +
            "-fx-background-radius: 22;" +
            "-fx-border-radius: 22;" +
            "-fx-cursor: hand;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 8, 0, 0, 2);"
        );
        
        // Next button hover effect
        nextButton.setOnMouseEntered(e -> nextButton.setStyle(
            "-fx-background-color: linear-gradient(to bottom, #5cb85c, #449d44);" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 14px;" +
            "-fx-font-weight: bold;" +
            "-fx-background-radius: 22;" +
            "-fx-border-radius: 22;" +
            "-fx-cursor: hand;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 12, 0, 0, 3);" +
            "-fx-scale-x: 1.05;" +
            "-fx-scale-y: 1.05;"
        ));
        
        nextButton.setOnMouseExited(e -> nextButton.setStyle(
            "-fx-background-color: linear-gradient(to bottom, #66bb6a, #4caf50);" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 14px;" +
            "-fx-font-weight: bold;" +
            "-fx-background-radius: 22;" +
            "-fx-border-radius: 22;" +
            "-fx-cursor: hand;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 8, 0, 0, 2);" +
            "-fx-scale-x: 1.0;" +
            "-fx-scale-y: 1.0;"
        ));
        
        nextButton.setOnAction(e -> goToNextStep());
        
        // Modern Complete Button
        completeButton = new Button("✅ Complete Check-In");
        completeButton.setPrefSize(200, 45);
        completeButton.setStyle(
            "-fx-background-color: linear-gradient(to bottom, #2e7d32, #1b5e20);" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 16px;" +
            "-fx-font-weight: bold;" +
            "-fx-background-radius: 22;" +
            "-fx-border-radius: 22;" +
            "-fx-cursor: hand;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 10, 0, 0, 3);"
        );
        
        // Complete button hover effect
        completeButton.setOnMouseEntered(e -> completeButton.setStyle(
            "-fx-background-color: linear-gradient(to bottom, #1b5e20, #0d2a0e);" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 16px;" +
            "-fx-font-weight: bold;" +
            "-fx-background-radius: 22;" +
            "-fx-border-radius: 22;" +
            "-fx-cursor: hand;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.4), 15, 0, 0, 5);" +
            "-fx-scale-x: 1.05;" +
            "-fx-scale-y: 1.05;"
        ));
        
        completeButton.setOnMouseExited(e -> completeButton.setStyle(
            "-fx-background-color: linear-gradient(to bottom, #2e7d32, #1b5e20);" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 16px;" +
            "-fx-font-weight: bold;" +
            "-fx-background-radius: 22;" +
            "-fx-border-radius: 22;" +
            "-fx-cursor: hand;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 10, 0, 0, 3);" +
            "-fx-scale-x: 1.0;" +
            "-fx-scale-y: 1.0;"
        ));
        
        completeButton.setOnAction(e -> completeCheckIn());
        completeButton.setVisible(false);
        
        buttonArea.getChildren().addAll(backButton, nextButton, completeButton);
    }
    
    private void loadCurrentStep() {
        updateHeader();
        contentArea.getChildren().clear();
        
        switch (currentStep) {
            case 1:
                loadPatientIdentificationStep();
                break;
            case 2:
                loadInsuranceVerificationStep();
                break;
            case 3:
                loadAppointmentConfirmationStep();
                break;
            case 4:
                loadContactInformationStep();
                break;
            case 5:
                loadPaymentProcessingStep();
                break;
            case 6:
                loadHealthScreeningStep();
                break;
            case 7:
                loadCompletionStep();
                break;
        }
        
        updateButtons();
    }
    
    private void updateHeader() {
        String[] stepTitles = {
            "",
            "Patient Identification",
            "Insurance Verification", 
            "Appointment Confirmation",
            "Contact Information Update",
            "Payment Processing",
            "Health Screening",
            "Check-In Completion"
        };
        
        stepLabel.setText("Step " + currentStep + ": " + stepTitles[currentStep]);
        progressLabel.setText("Progress: " + currentStep + " of " + totalSteps + " steps completed");
    }
    
    private void updateButtons() {
        backButton.setDisable(currentStep == 1);
        nextButton.setVisible(currentStep < totalSteps);
        completeButton.setVisible(currentStep == totalSteps);
    }
    
    // Step 1: Patient Identification
    private void loadPatientIdentificationStep() {
        VBox stepContent = new VBox(15);
        stepContent.getChildren().add(new Label("Please verify patient identification information:"));
        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        
        grid.add(new Label("First Name:"), 0, 0);
        firstNameField = new TextField();
        firstNameField.setPromptText("Enter first name");
        grid.add(firstNameField, 1, 0);
        
        grid.add(new Label("Last Name:"), 0, 1);
        lastNameField = new TextField();
        lastNameField.setPromptText("Enter last name");
        grid.add(lastNameField, 1, 1);
        
        grid.add(new Label("Date of Birth:"), 0, 2);
        dobField = new TextField();
        dobField.setPromptText("MM/dd/yyyy");
        grid.add(dobField, 1, 2);
        
        stepContent.getChildren().add(grid);
        contentArea.getChildren().add(stepContent);
    }
    
    // Step 2: Insurance Verification
    private void loadInsuranceVerificationStep() {
        VBox stepContent = new VBox(15);
        stepContent.getChildren().add(new Label("Verify patient insurance information:"));
        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        
        grid.add(new Label("Insurance Provider:"), 0, 0);
        insuranceProviderField = new TextField();
        insuranceProviderField.setPromptText("Enter insurance provider name");
        grid.add(insuranceProviderField, 1, 0);
        
        grid.add(new Label("Policy Number:"), 0, 1);
        policyNumberField = new TextField();
        policyNumberField.setPromptText("Enter policy number");
        grid.add(policyNumberField, 1, 1);
        
        grid.add(new Label("Group Number:"), 0, 2);
        groupNumberField = new TextField();
        groupNumberField.setPromptText("Enter group number (optional)");
        grid.add(groupNumberField, 1, 2);
        
        stepContent.getChildren().add(grid);
        contentArea.getChildren().add(stepContent);
    }
    
    // Step 3: Appointment Confirmation
    private void loadAppointmentConfirmationStep() {
        VBox stepContent = new VBox(15);
        stepContent.getChildren().add(new Label("Confirm appointment details:"));
        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        
        grid.add(new Label("Appointment Date:"), 0, 0);
        appointmentDatePicker = new DatePicker(LocalDate.now());
        grid.add(appointmentDatePicker, 1, 0);
        
        grid.add(new Label("Appointment Time:"), 0, 1);
        appointmentTimeField = new TextField();
        appointmentTimeField.setPromptText("HH:mm (24-hour format)");
        grid.add(appointmentTimeField, 1, 1);
        
        grid.add(new Label("Doctor Name:"), 0, 2);
        doctorNameField = new TextField();
        doctorNameField.setPromptText("Enter doctor's name");
        grid.add(doctorNameField, 1, 2);
        
        grid.add(new Label("Appointment Type:"), 0, 3);
        appointmentTypeCombo = new ComboBox<>();
        appointmentTypeCombo.getItems().addAll(
            "Annual Physical", "Follow-up", "Consultation", 
            "Urgent Care", "Specialist Visit", "Lab Work", "Other"
        );
        appointmentTypeCombo.setPromptText("Select appointment type");
        grid.add(appointmentTypeCombo, 1, 3);
        
        stepContent.getChildren().add(grid);
        contentArea.getChildren().add(stepContent);
    }
    
    // Step 4: Contact Information Update
    private void loadContactInformationStep() {
        VBox stepContent = new VBox(15);
        stepContent.getChildren().add(new Label("Update contact information (optional):"));
        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        
        grid.add(new Label("Phone Number:"), 0, 0);
        phoneField = new TextField();
        phoneField.setPromptText("Enter phone number");
        grid.add(phoneField, 1, 0);
        
        grid.add(new Label("Email Address:"), 0, 1);
        emailField = new TextField();
        emailField.setPromptText("Enter email address");
        grid.add(emailField, 1, 1);
        
        grid.add(new Label("Address:"), 0, 2);
        addressField = new TextField();
        addressField.setPromptText("Enter current address");
        grid.add(addressField, 1, 2);
        
        grid.add(new Label("Emergency Contact:"), 0, 3);
        emergencyContactField = new TextField();
        emergencyContactField.setPromptText("Enter emergency contact info");
        grid.add(emergencyContactField, 1, 3);
        
        stepContent.getChildren().add(grid);
        contentArea.getChildren().add(stepContent);
    }
    
    // Step 5: Payment Processing
    private void loadPaymentProcessingStep() {
        VBox stepContent = new VBox(15);
        stepContent.getChildren().add(new Label("Process payment information:"));
        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        
        grid.add(new Label("Copay Amount:"), 0, 0);
        copayAmountField = new TextField();
        copayAmountField.setPromptText("Enter copay amount (0.00 if none)");
        grid.add(copayAmountField, 1, 0);
        
        grid.add(new Label("Payment Method:"), 0, 1);
        paymentMethodCombo = new ComboBox<>();
        paymentMethodCombo.getItems().addAll(
            "Cash", "Credit Card", "Debit Card", "Check", "Insurance Coverage", "No Payment Required"
        );
        paymentMethodCombo.setPromptText("Select payment method");
        grid.add(paymentMethodCombo, 1, 1);
        
        grid.add(new Label("Reference Number:"), 0, 2);
        referenceNumberField = new TextField();
        referenceNumberField.setPromptText("Enter reference/confirmation number");
        grid.add(referenceNumberField, 1, 2);
        
        stepContent.getChildren().add(grid);
        contentArea.getChildren().add(stepContent);
    }
    
    // Step 6: Health Screening
    private void loadHealthScreeningStep() {
        VBox stepContent = new VBox(15);
        stepContent.getChildren().add(new Label("Conduct health screening:"));
        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        
        grid.add(new Label("Temperature (°F):"), 0, 0);
        temperatureField = new TextField();
        temperatureField.setPromptText("Enter temperature reading");
        grid.add(temperatureField, 1, 0);
        
        grid.add(new Label("Current Symptoms:"), 0, 1);
        symptomsArea = new TextArea();
        symptomsArea.setPromptText("Enter any current symptoms or 'None'");
        symptomsArea.setPrefRowCount(3);
        grid.add(symptomsArea, 1, 1);
        
        recentTravelCheckBox = new CheckBox("Recent travel (within 14 days)");
        grid.add(recentTravelCheckBox, 0, 2, 2, 1);
        
        covidExposureCheckBox = new CheckBox("Recent COVID-19 exposure");
        grid.add(covidExposureCheckBox, 0, 3, 2, 1);
        
        stepContent.getChildren().add(grid);
        contentArea.getChildren().add(stepContent);
    }
    
    // Step 7: Check-in Completion
    private void loadCompletionStep() {
        VBox stepContent = new VBox(15);
        stepContent.getChildren().add(new Label("Complete check-in process:"));
        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        
        grid.add(new Label("Waiting Area Assignment:"), 0, 0);
        waitingAreaCombo = new ComboBox<>();
        waitingAreaCombo.getItems().addAll(
            "General Waiting Area", "Pediatric Waiting", "Private Room 1", 
            "Private Room 2", "Exam Room 101", "Exam Room 102", "Lab Waiting"
        );
        waitingAreaCombo.setPromptText("Select waiting area");
        grid.add(waitingAreaCombo, 1, 0);
        
        grid.add(new Label("Special Instructions:"), 0, 1);
        specialInstructionsArea = new TextArea();
        specialInstructionsArea.setPromptText("Enter any special instructions for clinical staff");
        specialInstructionsArea.setPrefRowCount(3);
        grid.add(specialInstructionsArea, 1, 1);
        
        stepContent.getChildren().add(grid);
        
        // Add summary section
        Label summaryLabel = new Label("Check-In Summary:");
        summaryLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        
        summaryArea = new TextArea();
        summaryArea.setEditable(false);
        summaryArea.setPrefRowCount(8);
        summaryArea.setText(checkInWorkflow.getCheckInSummary());
        
        stepContent.getChildren().addAll(summaryLabel, summaryArea);
        contentArea.getChildren().add(stepContent);
    }
    
    private void goToNextStep() {
        if (validateCurrentStep()) {
            processCurrentStep();
            if (currentStep < totalSteps) {
                currentStep++;
                loadCurrentStep();
            }
        }
    }
    
    private void goToPreviousStep() {
        if (currentStep > 1) {
            currentStep--;
            loadCurrentStep();
        }
    }
    
    private boolean validateCurrentStep() {
        switch (currentStep) {
            case 1:
                if (firstNameField.getText().trim().isEmpty() || 
                    lastNameField.getText().trim().isEmpty() || 
                    dobField.getText().trim().isEmpty()) {
                    showAlert("Validation Error", "Please fill in all required identification fields.");
                    return false;
                }
                break;
            case 2:
                if (insuranceProviderField.getText().trim().isEmpty() || 
                    policyNumberField.getText().trim().isEmpty()) {
                    showAlert("Validation Error", "Please fill in insurance provider and policy number.");
                    return false;
                }
                break;
            case 3:
                if (appointmentDatePicker.getValue() == null || 
                    appointmentTimeField.getText().trim().isEmpty() ||
                    doctorNameField.getText().trim().isEmpty() ||
                    appointmentTypeCombo.getValue() == null) {
                    showAlert("Validation Error", "Please fill in all appointment details.");
                    return false;
                }
                break;
            case 5:
                if (copayAmountField.getText().trim().isEmpty() || 
                    paymentMethodCombo.getValue() == null) {
                    showAlert("Validation Error", "Please enter copay amount and select payment method.");
                    return false;
                }
                break;
            case 6:
                if (temperatureField.getText().trim().isEmpty()) {
                    showAlert("Validation Error", "Please enter temperature reading.");
                    return false;
                }
                break;
            case 7:
                if (waitingAreaCombo.getValue() == null) {
                    showAlert("Validation Error", "Please select a waiting area assignment.");
                    return false;
                }
                break;
        }
        return true;
    }
    
    private void processCurrentStep() {
        try {
            switch (currentStep) {
                case 1:
                    checkInWorkflow.validatePatientIdentification(
                        firstNameField.getText().trim(),
                        lastNameField.getText().trim(),
                        dobField.getText().trim()
                    );
                    break;
                case 2:
                    checkInWorkflow.verifyInsuranceCoverage(
                        insuranceProviderField.getText().trim(),
                        policyNumberField.getText().trim(),
                        groupNumberField.getText().trim()
                    );
                    break;
                case 3:
                    LocalDateTime appointmentDateTime = LocalDateTime.of(
                        appointmentDatePicker.getValue(),
                        java.time.LocalTime.parse(appointmentTimeField.getText().trim())
                    );
                    checkInWorkflow.confirmScheduledAppointment(
                        appointmentDateTime,
                        doctorNameField.getText().trim(),
                        appointmentTypeCombo.getValue()
                    );
                    break;
                case 4:
                    checkInWorkflow.updateContactInformation(
                        phoneField.getText().trim(),
                        emailField.getText().trim(),
                        addressField.getText().trim(),
                        emergencyContactField.getText().trim()
                    );
                    break;
                case 5:
                    double amount = Double.parseDouble(copayAmountField.getText().trim());
                    checkInWorkflow.processPayment(
                        amount,
                        paymentMethodCombo.getValue(),
                        referenceNumberField.getText().trim()
                    );
                    break;
                case 6:
                    double temperature = Double.parseDouble(temperatureField.getText().trim());
                    checkInWorkflow.conductHealthScreening(
                        temperature,
                        symptomsArea.getText().trim(),
                        recentTravelCheckBox.isSelected(),
                        covidExposureCheckBox.isSelected()
                    );
                    break;
            }
        } catch (Exception e) {
            LOGGER.warning("Error processing step " + currentStep + ": " + e.getMessage());
            showAlert("Processing Error", "Error processing step: " + e.getMessage());
        }
    }
    
    private void completeCheckIn() {
        try {
            boolean success = checkInWorkflow.completeCheckIn(
                LocalDateTime.now(),
                waitingAreaCombo.getValue(),
                specialInstructionsArea.getText().trim()
            );
            
            if (success) {
                showAlert("Check-In Complete", 
                    "Patient check-in completed successfully!\n\n" +
                    "Patient: " + checkInWorkflow.getPatient().getFullName() + "\n" +
                    "Waiting Area: " + waitingAreaCombo.getValue());
                
                // Close the window
                stage.close();
            } else {
                showAlert("Error", "Failed to complete check-in. Please review all steps.");
            }
        } catch (Exception e) {
            LOGGER.warning("Error completing check-in: " + e.getMessage());
            showAlert("Error", "Error completing check-in: " + e.getMessage());
        }
    }
    
    /**
     * Save all patient data and exit the check-in process
     */
    private void saveAndLeavePatient() {
        try {
            // Collect all current form data into the workflow
            collectCurrentFormData();
            
            // Create a patient data object from current workflow state
            PatientDataObject patientData = new PatientDataObject(
                checkInWorkflow.getPatient(),
                checkInWorkflow.getCurrentSession()
            );
            
            // Collect additional form data that might not be in the workflow
            populatePatientDataFromGUI(patientData);
            
            // Save to storage
            PatientDataStorage storage = PatientDataStorage.getInstance();
            boolean saved = storage.savePatientData(patientData);
            
            if (saved) {
                // Show success message with summary
                showSaveSuccessDialog(patientData, storage);
                
                // Close the window
                stage.close();
            } else {
                showAlert("Save Error", "Failed to save patient data. Please try again.");
            }
            
        } catch (Exception e) {
            LOGGER.warning("Error saving patient data: " + e.getMessage());
            showAlert("Save Error", "Error saving patient data: " + e.getMessage());
        }
    }
    
    /**
     * Collect current form data and update the workflow
     */
    private void collectCurrentFormData() {
        try {
            // Always try to collect data from current step fields, even if validation hasn't passed
            switch (currentStep) {
                case 1:
                    if (firstNameField != null && lastNameField != null && dobField != null) {
                        checkInWorkflow.validatePatientIdentification(
                            firstNameField.getText().trim(),
                            lastNameField.getText().trim(),
                            dobField.getText().trim()
                        );
                    }
                    break;
                case 2:
                    if (insuranceProviderField != null && policyNumberField != null) {
                        checkInWorkflow.verifyInsuranceCoverage(
                            insuranceProviderField.getText().trim(),
                            policyNumberField.getText().trim(),
                            groupNumberField != null ? groupNumberField.getText().trim() : ""
                        );
                    }
                    break;
                case 3:
                    if (appointmentDatePicker != null && appointmentTimeField != null && 
                        doctorNameField != null && appointmentTypeCombo != null) {
                        try {
                            LocalDateTime appointmentDateTime = LocalDateTime.of(
                                appointmentDatePicker.getValue() != null ? appointmentDatePicker.getValue() : LocalDate.now(),
                                appointmentTimeField.getText().trim().isEmpty() ? 
                                    java.time.LocalTime.now() : 
                                    java.time.LocalTime.parse(appointmentTimeField.getText().trim())
                            );
                            checkInWorkflow.confirmScheduledAppointment(
                                appointmentDateTime,
                                doctorNameField.getText().trim(),
                                appointmentTypeCombo.getValue() != null ? appointmentTypeCombo.getValue() : "General"
                            );
                        } catch (Exception timeParseException) {
                            // Skip if time parsing fails
                        }
                    }
                    break;
                case 4:
                    if (phoneField != null || emailField != null || addressField != null || emergencyContactField != null) {
                        checkInWorkflow.updateContactInformation(
                            phoneField != null ? phoneField.getText().trim() : "",
                            emailField != null ? emailField.getText().trim() : "",
                            addressField != null ? addressField.getText().trim() : "",
                            emergencyContactField != null ? emergencyContactField.getText().trim() : ""
                        );
                    }
                    break;
                case 5:
                    if (copayAmountField != null && paymentMethodCombo != null) {
                        try {
                            double amount = copayAmountField.getText().trim().isEmpty() ? 
                                0.0 : Double.parseDouble(copayAmountField.getText().trim());
                            checkInWorkflow.processPayment(
                                amount,
                                paymentMethodCombo.getValue() != null ? paymentMethodCombo.getValue() : "No Payment Required",
                                referenceNumberField != null ? referenceNumberField.getText().trim() : ""
                            );
                        } catch (NumberFormatException numException) {
                            // Skip if amount parsing fails
                        }
                    }
                    break;
                case 6:
                    if (temperatureField != null) {
                        try {
                            double temperature = temperatureField.getText().trim().isEmpty() ? 
                                98.6 : Double.parseDouble(temperatureField.getText().trim());
                            checkInWorkflow.conductHealthScreening(
                                temperature,
                                symptomsArea != null ? symptomsArea.getText().trim() : "",
                                recentTravelCheckBox != null ? recentTravelCheckBox.isSelected() : false,
                                covidExposureCheckBox != null ? covidExposureCheckBox.isSelected() : false
                            );
                        } catch (NumberFormatException tempException) {
                            // Skip if temperature parsing fails
                        }
                    }
                    break;
                case 7:
                    // Don't complete check-in automatically, just collect the data
                    break;
            }
        } catch (Exception e) {
            // Log but don't fail the save operation
            LOGGER.info("Some form data could not be collected: " + e.getMessage());
        }
    }
    
    /**
     * Populate patient data object with additional GUI-specific data
     */
    private void populatePatientDataFromGUI(PatientDataObject patientData) {
        // Set additional data that might be in GUI fields but not in workflow
        
        // Insurance group number
        if (groupNumberField != null && !groupNumberField.getText().trim().isEmpty()) {
            patientData.setInsuranceGroupNumber(groupNumberField.getText().trim());
        }
        
        // Payment reference number
        if (referenceNumberField != null && !referenceNumberField.getText().trim().isEmpty()) {
            patientData.setPaymentReferenceNumber(referenceNumberField.getText().trim());
        }
        
        // Special instructions
        if (specialInstructionsArea != null && !specialInstructionsArea.getText().trim().isEmpty()) {
            patientData.setSpecialInstructions(specialInstructionsArea.getText().trim());
        }
        
        // Waiting area assignment
        if (waitingAreaCombo != null && waitingAreaCombo.getValue() != null) {
            patientData.setWaitingAreaAssignment(waitingAreaCombo.getValue());
        }
        
        // Add a session note about the save action
        patientData.addSessionNote("Patient data saved via 'Save and Leave Patient' on step " + currentStep);
    }
    
    /**
     * Show save success dialog with patient summary and storage statistics
     */
    private void showSaveSuccessDialog(PatientDataObject patientData, PatientDataStorage storage) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Patient Data Saved Successfully");
        alert.setHeaderText("Patient: " + patientData.getFullName());
        
        StringBuilder message = new StringBuilder();
        message.append("Patient data has been saved successfully!\n\n");
        message.append("Patient ID: ").append(patientData.getPatientId()).append("\n");
        message.append("Completion: ").append(patientData.getCompletionPercentage()).append("%\n");
        message.append("Current Step: ").append(currentStep).append(" of ").append(totalSteps).append("\n\n");
        
        message.append("STORAGE SUMMARY:\n");
        message.append("Total Patients Saved: ").append(storage.getPatientCount()).append("\n");
        message.append("Patients from Today: ").append(storage.getPatientsFromToday().size()).append("\n");
        message.append("Completed Check-ins: ").append(storage.getCompletedCheckIns().size()).append("\n\n");
        
        message.append("The patient can resume check-in later using Patient ID: ");
        message.append(patientData.getPatientId());
        
        alert.setContentText(message.toString());
        
        // Make the dialog resizable for long content
        alert.setResizable(true);
        alert.getDialogPane().setPrefSize(450, 300);
        
        alert.showAndWait();
    }
    
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    public void show() {
        stage.show();
    }
    
    public void close() {
        stage.close();
    }
    
    public CheckInWorkflow getCheckInWorkflow() {
        return checkInWorkflow;
    }
}