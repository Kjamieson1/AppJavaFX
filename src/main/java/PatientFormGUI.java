import java.io.File;
import java.util.logging.Logger;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * GUI class for displaying and managing patient form data
 */
public class PatientFormGUI {
    
    private static final Logger LOGGER = Logger.getLogger(PatientFormGUI.class.getName());
    
    private Stage stage;
    private NewPatient currentPatient;
    
    // Form fields for patient demographics
    private TextField firstNameField;
    private TextField lastNameField;
    private DatePicker dateOfBirthPicker;
    private ComboBox<String> genderComboBox;
    private TextField addressField;
    private TextField phoneField;
    private TextField emailField;
    private TextField emergencyContactField;
    private TextField emergencyPhoneField;
    private TextField insuranceProviderField;
    private TextField insurancePolicyField;
    
    // Patient picture
    private ImageView patientImageView;
    private String selectedImagePath;
    
    // Medical history lists
    private ListView<String> medicationsListView;
    private ListView<String> diagnosesListView;
    private ListView<String> allergiesListView;
    
    // Input fields for medical history
    private TextField medicationInput;
    private TextField diagnosisInput;
    private TextField allergyInput;
    
    public PatientFormGUI() {
        this.currentPatient = new NewPatient();
        initializeGUI();
    }
    
    private void initializeGUI() {
        stage = new Stage();
        stage.setTitle("HealthCare Pro - Patient Information");
        
        // Create main layout with modern styling
        VBox mainLayout = new VBox(0);
        mainLayout.setStyle(
            "-fx-background: linear-gradient(to bottom, #e8f5e8, #f0f9f0);" // Light green gradient
        );
        
        // Create header section with save button
        HBox headerSection = createModernHeaderSection();
        
        // Create scroll pane for the form with modern styling
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setStyle(
            "-fx-background: transparent;" +
            "-fx-background-color: transparent;"
        );
        
        VBox formContent = new VBox(25);
        formContent.setPadding(new Insets(30));
        formContent.getChildren().addAll(
            createModernPatientInfoSection(),
            createModernPatientPictureSection(),
            createModernMedicalHistorySection(),
            createModernButtonSection()
        );
        
        scrollPane.setContent(formContent);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefViewportHeight(600);
        
        // Add header and form to main layout
        mainLayout.getChildren().addAll(headerSection, scrollPane);
        
        // Create scene with modern styling
        Scene scene = new Scene(mainLayout, 900, 750);
        stage.setScene(scene);
        stage.centerOnScreen();
        
        // Load existing patient data if any
        loadPatientData();
    }
    
    /**
     * Create modern header section with title and save button
     */
    private HBox createModernHeaderSection() {
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER);
        header.setPadding(new Insets(25, 30, 25, 30));
        header.setStyle(
            "-fx-background: linear-gradient(to right, #1b5e20, #2e7d32);" // Dark green gradient
        );
        
        // Title section
        VBox titleSection = new VBox(5);
        
        Label titleLabel = new Label("Patient Information Form");
        titleLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 24));
        titleLabel.setStyle("-fx-text-fill: white;");
        
        Label subtitleLabel = new Label("Complete patient demographics and medical history");
        subtitleLabel.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 14));
        subtitleLabel.setStyle("-fx-text-fill: #c8e6c9;");
        
        titleSection.getChildren().addAll(titleLabel, subtitleLabel);
        
        // Create modern "Save and Leave Patient" button
        Button saveAndLeaveButton = createModernSaveButton();
        
        // Add spacer to push save button to the right
        HBox spacer = new HBox();
        HBox.setHgrow(spacer, javafx.scene.layout.Priority.ALWAYS);
        
        header.getChildren().addAll(titleSection, spacer, saveAndLeaveButton);
        return header;
    }
    
    /**
     * Create modern save button
     */
    private Button createModernSaveButton() {
        Button saveButton = new Button("💾 Save and Leave Patient");
        saveButton.setFont(Font.font("Segoe UI", FontWeight.BOLD, 13));
        saveButton.setPrefSize(200, 45);
        saveButton.setStyle(
            "-fx-background-color: #ffc107;" +
            "-fx-text-fill: #1b5e20;" +
            "-fx-background-radius: 12;" +
            "-fx-border-radius: 12;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 8, 0, 0, 2);" +
            "-fx-cursor: hand;"
        );
        
        // Hover effects
        saveButton.setOnMouseEntered(e -> 
            saveButton.setStyle(
                "-fx-background-color: #ffcd39;" +
                "-fx-text-fill: #1b5e20;" +
                "-fx-background-radius: 12;" +
                "-fx-border-radius: 12;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.4), 12, 0, 0, 4);" +
                "-fx-cursor: hand;" +
                "-fx-scale-x: 1.02;" +
                "-fx-scale-y: 1.02;"
            )
        );
        
        saveButton.setOnMouseExited(e -> 
            saveButton.setStyle(
                "-fx-background-color: #ffc107;" +
                "-fx-text-fill: #1b5e20;" +
                "-fx-background-radius: 12;" +
                "-fx-border-radius: 12;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 8, 0, 0, 2);" +
                "-fx-cursor: hand;" +
                "-fx-scale-x: 1.0;" +
                "-fx-scale-y: 1.0;"
            )
        );
        
        saveButton.setOnAction(e -> saveAndLeavePatient());
        return saveButton;
    }
    
    private VBox createModernPatientInfoSection() {
        VBox section = new VBox(20);
        section.setPadding(new Insets(25));
        section.setStyle(
            "-fx-background-color: white;" +
            "-fx-background-radius: 15;" +
            "-fx-border-radius: 15;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 15, 0, 0, 3);" +
            "-fx-border-color: #e8f5e8;" +
            "-fx-border-width: 1;"
        );
        
        // Section header
        Label sectionHeader = new Label("👤 Patient Demographics");
        sectionHeader.setFont(Font.font("Segoe UI", FontWeight.BOLD, 18));
        sectionHeader.setStyle("-fx-text-fill: #1b5e20;");
        
        GridPane grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(15);
        grid.setPadding(new Insets(15, 0, 0, 0));
        
        // Style for labels
        String labelStyle = "-fx-text-fill: #2e7d32; -fx-font-weight: bold; -fx-font-size: 14px;";
        
        // Row 0
        Label firstNameLabel = new Label("First Name:");
        firstNameLabel.setStyle(labelStyle);
        grid.add(firstNameLabel, 0, 0);
        firstNameField = createStyledTextField("Enter first name");
        grid.add(firstNameField, 1, 0);
        
        Label lastNameLabel = new Label("Last Name:");
        lastNameLabel.setStyle(labelStyle);
        grid.add(lastNameLabel, 2, 0);
        lastNameField = createStyledTextField("Enter last name");
        grid.add(lastNameField, 3, 0);
        
        // Row 1
        Label dobLabel = new Label("Date of Birth:");
        dobLabel.setStyle(labelStyle);
        grid.add(dobLabel, 0, 1);
        dateOfBirthPicker = new DatePicker();
        styleComponent(dateOfBirthPicker);
        grid.add(dateOfBirthPicker, 1, 1);
        
        Label genderLabel = new Label("Gender:");
        genderLabel.setStyle(labelStyle);
        grid.add(genderLabel, 2, 1);
        genderComboBox = new ComboBox<>();
        genderComboBox.getItems().addAll("Male", "Female", "Other", "Prefer not to say");
        styleComponent(genderComboBox);
        grid.add(genderComboBox, 3, 1);
        
        // Row 2
        Label addressLabel = new Label("Address:");
        addressLabel.setStyle(labelStyle);
        grid.add(addressLabel, 0, 2);
        addressField = createStyledTextField("Enter complete address");
        grid.add(addressField, 1, 2, 3, 1); // span 3 columns
        
        // Row 3
        Label phoneLabel = new Label("Phone:");
        phoneLabel.setStyle(labelStyle);
        grid.add(phoneLabel, 0, 3);
        phoneField = createStyledTextField("Enter phone number");
        grid.add(phoneField, 1, 3);
        
        Label emailLabel = new Label("Email:");
        emailLabel.setStyle(labelStyle);
        grid.add(emailLabel, 2, 3);
        emailField = createStyledTextField("Enter email address");
        grid.add(emailField, 3, 3);
        
        // Row 4
        Label emergencyContactLabel = new Label("Emergency Contact:");
        emergencyContactLabel.setStyle(labelStyle);
        grid.add(emergencyContactLabel, 0, 4);
        emergencyContactField = createStyledTextField("Enter emergency contact name");
        grid.add(emergencyContactField, 1, 4);
        
        Label emergencyPhoneLabel = new Label("Emergency Phone:");
        emergencyPhoneLabel.setStyle(labelStyle);
        grid.add(emergencyPhoneLabel, 2, 4);
        emergencyPhoneField = createStyledTextField("Enter emergency phone");
        grid.add(emergencyPhoneField, 3, 4);
        
        // Row 5
        Label insuranceProviderLabel = new Label("Insurance Provider:");
        insuranceProviderLabel.setStyle(labelStyle);
        grid.add(insuranceProviderLabel, 0, 5);
        insuranceProviderField = createStyledTextField("Enter insurance provider");
        grid.add(insuranceProviderField, 1, 5);
        
        Label policyLabel = new Label("Policy Number:");
        policyLabel.setStyle(labelStyle);
        grid.add(policyLabel, 2, 5);
        insurancePolicyField = createStyledTextField("Enter policy number");
        grid.add(insurancePolicyField, 3, 5);
        
        section.getChildren().addAll(sectionHeader, grid);
        return section;
    }
    
    /**
     * Create styled text field
     */
    private TextField createStyledTextField(String promptText) {
        TextField field = new TextField();
        field.setPromptText(promptText);
        styleComponent(field);
        return field;
    }
    
    /**
     * Apply consistent styling to form components
     */
    private void styleComponent(javafx.scene.control.Control component) {
        component.setStyle(
            "-fx-background-color: #f8f9fa;" +
            "-fx-border-color: #c8e6c9;" +
            "-fx-border-width: 1;" +
            "-fx-border-radius: 8;" +
            "-fx-background-radius: 8;" +
            "-fx-padding: 10;" +
            "-fx-font-size: 14px;" +
            "-fx-text-fill: #2e7d32;"
        );
        
        // Focus styling
        component.focusedProperty().addListener((obs, oldFocus, newFocus) -> {
            if (newFocus) {
                component.setStyle(
                    "-fx-background-color: white;" +
                    "-fx-border-color: #4caf50;" +
                    "-fx-border-width: 2;" +
                    "-fx-border-radius: 8;" +
                    "-fx-background-radius: 8;" +
                    "-fx-padding: 10;" +
                    "-fx-font-size: 14px;" +
                    "-fx-text-fill: #2e7d32;" +
                    "-fx-effect: dropshadow(gaussian, rgba(76, 175, 80, 0.3), 8, 0, 0, 2);"
                );
            } else {
                component.setStyle(
                    "-fx-background-color: #f8f9fa;" +
                    "-fx-border-color: #c8e6c9;" +
                    "-fx-border-width: 1;" +
                    "-fx-border-radius: 8;" +
                    "-fx-background-radius: 8;" +
                    "-fx-padding: 10;" +
                    "-fx-font-size: 14px;" +
                    "-fx-text-fill: #2e7d32;"
                );
            }
        });
    }
    
    private VBox createModernPatientPictureSection() {
        VBox section = new VBox(20);
        section.setPadding(new Insets(25));
        section.setStyle(
            "-fx-background-color: white;" +
            "-fx-background-radius: 15;" +
            "-fx-border-radius: 15;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 15, 0, 0, 3);" +
            "-fx-border-color: #e8f5e8;" +
            "-fx-border-width: 1;"
        );
        
        // Section header
        Label sectionHeader = new Label("📷 Patient Photo");
        sectionHeader.setFont(Font.font("Segoe UI", FontWeight.BOLD, 18));
        sectionHeader.setStyle("-fx-text-fill: #1b5e20;");
        
        // Picture container
        VBox pictureContainer = new VBox(15);
        pictureContainer.setAlignment(Pos.CENTER);
        
        // Image view for patient photo
        patientImageView = new ImageView();
        patientImageView.setFitWidth(150);
        patientImageView.setFitHeight(150);
        patientImageView.setPreserveRatio(true);
        patientImageView.setStyle(
            "-fx-background-color: #f1f8e9;" +
            "-fx-background-radius: 75;" +
            "-fx-border-radius: 75;" +
            "-fx-border-color: #4caf50;" +
            "-fx-border-width: 3;" +
            "-fx-effect: dropshadow(gaussian, rgba(76, 175, 80, 0.3), 10, 0, 0, 2);"
        );
        
        // Default placeholder if no image
        try {
            // Set a default placeholder image or icon
            patientImageView.setImage(new Image(getClass().getResourceAsStream("/placeholder-patient.png")));
        } catch (Exception e) {
            // If no placeholder image found, just use the styled empty view
            LOGGER.warning(() -> "No placeholder image found: " + e.getMessage());
        }
        
        // Upload button
        Button uploadButton = new Button("📁 Choose Photo");
        uploadButton.setStyle(
            "-fx-background-color: linear-gradient(to bottom, #66bb6a, #4caf50);" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 14px;" +
            "-fx-font-weight: bold;" +
            "-fx-padding: 12 24;" +
            "-fx-background-radius: 25;" +
            "-fx-border-radius: 25;" +
            "-fx-cursor: hand;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 8, 0, 0, 2);"
        );
        
        // Button hover effect
        uploadButton.setOnMouseEntered(e -> uploadButton.setStyle(
            "-fx-background-color: linear-gradient(to bottom, #5cb85c, #449d44);" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 14px;" +
            "-fx-font-weight: bold;" +
            "-fx-padding: 12 24;" +
            "-fx-background-radius: 25;" +
            "-fx-border-radius: 25;" +
            "-fx-cursor: hand;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.25), 12, 0, 0, 4);" +
            "-fx-scale-x: 1.05;" +
            "-fx-scale-y: 1.05;"
        ));
        
        uploadButton.setOnMouseExited(e -> uploadButton.setStyle(
            "-fx-background-color: linear-gradient(to bottom, #66bb6a, #4caf50);" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 14px;" +
            "-fx-font-weight: bold;" +
            "-fx-padding: 12 24;" +
            "-fx-background-radius: 25;" +
            "-fx-border-radius: 25;" +
            "-fx-cursor: hand;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 8, 0, 0, 2);" +
            "-fx-scale-x: 1.0;" +
            "-fx-scale-y: 1.0;"
        ));
        
        uploadButton.setOnAction(e -> selectPatientPicture());
        
        // Photo info label
        Label photoInfo = new Label("📝 Upload a clear, recent photo (JPG, PNG)");
        photoInfo.setStyle(
            "-fx-text-fill: #6b7280;" +
            "-fx-font-size: 12px;" +
            "-fx-font-style: italic;"
        );
        
        pictureContainer.getChildren().addAll(patientImageView, uploadButton, photoInfo);
        section.getChildren().addAll(sectionHeader, pictureContainer);
        
        return section;
    }
    
    private VBox createModernMedicalHistorySection() {
        VBox section = new VBox(20);
        section.setPadding(new Insets(25));
        section.setStyle(
            "-fx-background-color: white;" +
            "-fx-background-radius: 15;" +
            "-fx-border-radius: 15;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 15, 0, 0, 3);" +
            "-fx-border-color: #e8f5e8;" +
            "-fx-border-width: 1;"
        );
        
        // Section header
        Label sectionHeader = new Label("🏥 Medical History");
        sectionHeader.setFont(Font.font("Segoe UI", FontWeight.BOLD, 18));
        sectionHeader.setStyle("-fx-text-fill: #1b5e20;");
        
        HBox medicalBox = new HBox(20);
        
        // Medications section
        VBox medicationsBox = createModernMedicalListSection("💊 Medications:", 
            medicationsListView = new ListView<>(),
            medicationInput = new TextField(),
            "Add Medication",
            () -> addMedication());
        
        // Diagnoses section
        VBox diagnosesBox = createModernMedicalListSection("🩺 Diagnoses:", 
            diagnosesListView = new ListView<>(),
            diagnosisInput = new TextField(),
            "Add Diagnosis",
            () -> addDiagnosis());
        
        // Allergies section
        VBox allergiesBox = createModernMedicalListSection("⚠️ Allergies:", 
            allergiesListView = new ListView<>(),
            allergyInput = new TextField(),
            "Add Allergy",
            () -> addAllergy());
        
        medicalBox.getChildren().addAll(medicationsBox, diagnosesBox, allergiesBox);
        section.getChildren().addAll(sectionHeader, medicalBox);
        
        return section;
    }
    
    private VBox createModernMedicalListSection(String title, ListView<String> listView, 
                                         TextField inputField, String buttonText, Runnable addAction) {
        VBox box = new VBox(15);
        box.setPadding(new Insets(15));
        box.setStyle(
            "-fx-background-color: #f8fdf8;" +
            "-fx-background-radius: 10;" +
            "-fx-border-radius: 10;" +
            "-fx-border-color: #c8e6c9;" +
            "-fx-border-width: 1;"
        );
        
        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
        titleLabel.setStyle("-fx-text-fill: #2e7d32;");
        
        listView.setPrefHeight(120);
        listView.setPrefWidth(220);
        listView.setStyle(
            "-fx-background-color: white;" +
            "-fx-border-color: #c8e6c9;" +
            "-fx-border-width: 1;" +
            "-fx-border-radius: 8;" +
            "-fx-background-radius: 8;"
        );
        
        inputField.setPromptText("Enter " + title.toLowerCase().substring(2, title.length()-1));
        styleComponent(inputField);
        
        Button addButton = new Button("➕ " + buttonText);
        addButton.setStyle(
            "-fx-background-color: linear-gradient(to bottom, #66bb6a, #4caf50);" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 12px;" +
            "-fx-font-weight: bold;" +
            "-fx-padding: 8 16;" +
            "-fx-background-radius: 20;" +
            "-fx-border-radius: 20;" +
            "-fx-cursor: hand;"
        );
        addButton.setOnAction(e -> addAction.run());
        
        Button removeButton = new Button("🗑️ Remove");
        removeButton.setStyle(
            "-fx-background-color: linear-gradient(to bottom, #ef5350, #e53935);" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 12px;" +
            "-fx-font-weight: bold;" +
            "-fx-padding: 8 16;" +
            "-fx-background-radius: 20;" +
            "-fx-border-radius: 20;" +
            "-fx-cursor: hand;"
        );
        removeButton.setOnAction(e -> {
            String selected = listView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                listView.getItems().remove(selected);
                updatePatientMedicalHistory();
            }
        });
        
        HBox buttonBox = new HBox(8);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(addButton, removeButton);
        
        box.getChildren().addAll(titleLabel, listView, inputField, buttonBox);
        return box;
    }
    
    private VBox createModernButtonSection() {
        VBox section = new VBox(20);
        section.setAlignment(Pos.CENTER);
        section.setPadding(new Insets(30, 25, 25, 25));
        
        HBox buttonBox = new HBox(20);
        buttonBox.setAlignment(Pos.CENTER);
        
        // Save Button
        Button saveButton = new Button("💾 Save Patient");
        saveButton.setStyle(
            "-fx-background-color: linear-gradient(to bottom, #66bb6a, #4caf50);" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 16px;" +
            "-fx-font-weight: bold;" +
            "-fx-padding: 15 30;" +
            "-fx-background-radius: 25;" +
            "-fx-border-radius: 25;" +
            "-fx-cursor: hand;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0, 0, 3);"
        );
        
        // Save button hover effect
        saveButton.setOnMouseEntered(e -> saveButton.setStyle(
            "-fx-background-color: linear-gradient(to bottom, #5cb85c, #449d44);" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 16px;" +
            "-fx-font-weight: bold;" +
            "-fx-padding: 15 30;" +
            "-fx-background-radius: 25;" +
            "-fx-border-radius: 25;" +
            "-fx-cursor: hand;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 15, 0, 0, 5);" +
            "-fx-scale-x: 1.05;" +
            "-fx-scale-y: 1.05;"
        ));
        
        saveButton.setOnMouseExited(e -> saveButton.setStyle(
            "-fx-background-color: linear-gradient(to bottom, #66bb6a, #4caf50);" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 16px;" +
            "-fx-font-weight: bold;" +
            "-fx-padding: 15 30;" +
            "-fx-background-radius: 25;" +
            "-fx-border-radius: 25;" +
            "-fx-cursor: hand;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0, 0, 3);" +
            "-fx-scale-x: 1.0;" +
            "-fx-scale-y: 1.0;"
        ));
        
        saveButton.setOnAction(e -> savePatient());
        
        // Clear Button
        Button clearButton = new Button("🗑️ Clear Form");
        clearButton.setStyle(
            "-fx-background-color: linear-gradient(to bottom, #ffa726, #ff9800);" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 16px;" +
            "-fx-font-weight: bold;" +
            "-fx-padding: 15 30;" +
            "-fx-background-radius: 25;" +
            "-fx-border-radius: 25;" +
            "-fx-cursor: hand;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0, 0, 3);"
        );
        
        // Clear button hover effect
        clearButton.setOnMouseEntered(e -> clearButton.setStyle(
            "-fx-background-color: linear-gradient(to bottom, #ff9800, #f57c00);" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 16px;" +
            "-fx-font-weight: bold;" +
            "-fx-padding: 15 30;" +
            "-fx-background-radius: 25;" +
            "-fx-border-radius: 25;" +
            "-fx-cursor: hand;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 15, 0, 0, 5);" +
            "-fx-scale-x: 1.05;" +
            "-fx-scale-y: 1.05;"
        ));
        
        clearButton.setOnMouseExited(e -> clearButton.setStyle(
            "-fx-background-color: linear-gradient(to bottom, #ffa726, #ff9800);" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 16px;" +
            "-fx-font-weight: bold;" +
            "-fx-padding: 15 30;" +
            "-fx-background-radius: 25;" +
            "-fx-border-radius: 25;" +
            "-fx-cursor: hand;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0, 0, 3);" +
            "-fx-scale-x: 1.0;" +
            "-fx-scale-y: 1.0;"
        ));
        
        clearButton.setOnAction(e -> clearForm());
        
        // Check-in Button
        Button checkInButton = new Button("📋 Start Check-in");
        checkInButton.setStyle(
            "-fx-background-color: linear-gradient(to bottom, #42a5f5, #2196f3);" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 16px;" +
            "-fx-font-weight: bold;" +
            "-fx-padding: 15 30;" +
            "-fx-background-radius: 25;" +
            "-fx-border-radius: 25;" +
            "-fx-cursor: hand;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0, 0, 3);"
        );
        
        // Check-in button hover effect
        checkInButton.setOnMouseEntered(e -> checkInButton.setStyle(
            "-fx-background-color: linear-gradient(to bottom, #2196f3, #1976d2);" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 16px;" +
            "-fx-font-weight: bold;" +
            "-fx-padding: 15 30;" +
            "-fx-background-radius: 25;" +
            "-fx-border-radius: 25;" +
            "-fx-cursor: hand;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 15, 0, 0, 5);" +
            "-fx-scale-x: 1.05;" +
            "-fx-scale-y: 1.05;"
        ));
        
        checkInButton.setOnMouseExited(e -> checkInButton.setStyle(
            "-fx-background-color: linear-gradient(to bottom, #42a5f5, #2196f3);" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 16px;" +
            "-fx-font-weight: bold;" +
            "-fx-padding: 15 30;" +
            "-fx-background-radius: 25;" +
            "-fx-border-radius: 25;" +
            "-fx-cursor: hand;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0, 0, 3);" +
            "-fx-scale-x: 1.0;" +
            "-fx-scale-y: 1.0;"
        ));
        
        checkInButton.setOnAction(e -> startCheckIn());
        
        buttonBox.getChildren().addAll(saveButton, clearButton, checkInButton);
        section.getChildren().add(buttonBox);
        
        return section;
    }
    
    private void selectPatientPicture() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Patient Picture");
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );
        
        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            selectedImagePath = selectedFile.getAbsolutePath();
            try {
                Image image = new Image(selectedFile.toURI().toString());
                patientImageView.setImage(image);
                currentPatient.setPatientPicturePath(selectedImagePath);
            } catch (Exception e) {
                LOGGER.warning(() -> "Failed to load image: " + e.getMessage());
                showAlert("Error", "Failed to load the selected image.");
            }
        }
    }
    
    private void addMedication() {
        String medication = medicationInput.getText().trim();
        if (!medication.isEmpty()) {
            medicationsListView.getItems().add(medication);
            medicationInput.clear();
            updatePatientMedicalHistory();
        }
    }
    
    private void addDiagnosis() {
        String diagnosis = diagnosisInput.getText().trim();
        if (!diagnosis.isEmpty()) {
            diagnosesListView.getItems().add(diagnosis);
            diagnosisInput.clear();
            updatePatientMedicalHistory();
        }
    }
    
    private void addAllergy() {
        String allergy = allergyInput.getText().trim();
        if (!allergy.isEmpty()) {
            allergiesListView.getItems().add(allergy);
            allergyInput.clear();
            updatePatientMedicalHistory();
        }
    }
    
    private void updatePatientMedicalHistory() {
        currentPatient.clearMedications();
        currentPatient.clearDiagnoses();
        currentPatient.clearAllergies();
        
        medicationsListView.getItems().forEach(currentPatient::addMedication);
        diagnosesListView.getItems().forEach(currentPatient::addDiagnosis);
        allergiesListView.getItems().forEach(currentPatient::addAllergy);
    }
    
    private void savePatient() {
        try {
            // Save demographic information
            currentPatient.setFirstName(firstNameField.getText());
            currentPatient.setLastName(lastNameField.getText());
            currentPatient.setDateOfBirth(dateOfBirthPicker.getValue());
            currentPatient.setGender(genderComboBox.getValue());
            currentPatient.setAddress(addressField.getText());
            currentPatient.setPhoneNumber(phoneField.getText());
            currentPatient.setEmail(emailField.getText());
            currentPatient.setEmergencyContact(emergencyContactField.getText());
            currentPatient.setEmergencyPhone(emergencyPhoneField.getText());
            currentPatient.setInsuranceProvider(insuranceProviderField.getText());
            currentPatient.setInsurancePolicyNumber(insurancePolicyField.getText());
            
            // Update medical history
            updatePatientMedicalHistory();
            
            // Validate and save
            if (currentPatient.validatePatientData()) {
                currentPatient.savePatient();
                showAlert("Success", "Patient information saved successfully!");
                LOGGER.info(() -> "Patient saved: " + currentPatient.getFullName());
            } else {
                showAlert("Validation Error", "Please fill in all required fields (First Name, Last Name, Date of Birth).");
            }
        } catch (Exception e) {
            LOGGER.warning(() -> "Error saving patient: " + e.getMessage());
            showAlert("Error", "Failed to save patient information: " + e.getMessage());
        }
    }
    
    private void clearForm() {
        firstNameField.clear();
        lastNameField.clear();
        dateOfBirthPicker.setValue(null);
        genderComboBox.setValue(null);
        addressField.clear();
        phoneField.clear();
        emailField.clear();
        emergencyContactField.clear();
        emergencyPhoneField.clear();
        insuranceProviderField.clear();
        insurancePolicyField.clear();
        
        patientImageView.setImage(null);
        selectedImagePath = null;
        
        medicationsListView.getItems().clear();
        diagnosesListView.getItems().clear();
        allergiesListView.getItems().clear();
        
        currentPatient = new NewPatient();
        LOGGER.info("Form cleared");
    }
    
    private void loadPatientData() {
        // This method can be extended to load existing patient data
        // For now, it starts with a fresh patient
    }
    
    /**
     * Start the check-in process for the current patient
     */
    private void startCheckIn() {
        if (currentPatient == null) {
            showAlert("No Patient", "Please create a patient first before starting check-in.");
            return;
        }
        
        try {
            // Update current patient with form data
            updateCurrentPatientFromForm();
            
            // Create and show check-in GUI - it manages its own stage
            new PatientCheckInGUI(currentPatient);
            
        } catch (Exception e) {
            LOGGER.warning(() -> "Error starting check-in: " + e.getMessage());
            showAlert("Check-in Error", "Failed to start check-in process: " + e.getMessage());
        }
    }
    
    /**
     * Update the current patient object with data from form fields
     */
    private void updateCurrentPatientFromForm() {
        if (currentPatient == null) return;
        
        // Update basic info
        currentPatient.setFirstName(firstNameField.getText());
        currentPatient.setLastName(lastNameField.getText());
        currentPatient.setDateOfBirth(dateOfBirthPicker.getValue());
        currentPatient.setGender(genderComboBox.getValue());
        currentPatient.setAddress(addressField.getText());
        currentPatient.setPhoneNumber(phoneField.getText());
        currentPatient.setEmail(emailField.getText());
        currentPatient.setEmergencyContact(emergencyContactField.getText());
        currentPatient.setEmergencyPhone(emergencyPhoneField.getText());
        currentPatient.setInsuranceProvider(insuranceProviderField.getText());
        currentPatient.setInsurancePolicyNumber(insurancePolicyField.getText());
        
        // Update medical history from list views
        updatePatientMedicalHistory();
    }
    
    private void openAppointmentManager() {
        // Save current form data first
        savePatient();
        
        // Open appointment management window
        AppointmentManagerGUI appointmentGUI = new AppointmentManagerGUI(currentPatient);
        appointmentGUI.show();
    }
    
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    /**
     * Save all patient data and exit the form
     */
    private void saveAndLeavePatient() {
        try {
            // First save the current patient data normally
            savePatient();
            
            // Create a patient data object from current patient
            PatientDataObject patientData = createPatientDataFromForm();
            
            // Save to storage
            PatientDataStorage storage = PatientDataStorage.getInstance();
            boolean saved = storage.savePatientData(patientData);
            
            if (saved) {
                // Show success message with summary
                showSaveSuccessDialog(patientData, storage);
                
                // Close the window
                stage.close();
            } else {
                showAlert("Save Error", "Failed to save patient data to storage. Please try again.");
            }
            
        } catch (Exception e) {
            LOGGER.warning(() -> "Error saving patient data: " + e.getMessage());
            showAlert("Save Error", "Error saving patient data: " + e.getMessage());
        }
    }
    
    /**
     * Create PatientDataObject from current form data
     */
    private PatientDataObject createPatientDataFromForm() {
        // Update current patient with form data first
        updateCurrentPatientFromForm();
        
        // Create patient data object
        PatientDataObject patientData = new PatientDataObject();
        
        // Populate from current patient
        patientData.setFirstName(currentPatient.getFirstName());
        patientData.setLastName(currentPatient.getLastName());
        patientData.setDateOfBirth(currentPatient.getDateOfBirth());
        patientData.setGender(currentPatient.getGender());
        patientData.setAge(currentPatient.getAge());
        patientData.setPhoneNumber(currentPatient.getPhoneNumber());
        patientData.setEmail(currentPatient.getEmail());
        patientData.setAddress(currentPatient.getAddress());
        patientData.setEmergencyContact(currentPatient.getEmergencyContact());
        patientData.setEmergencyPhone(currentPatient.getEmergencyPhone());
        patientData.setInsuranceProvider(currentPatient.getInsuranceProvider());
        patientData.setInsurancePolicyNumber(currentPatient.getInsurancePolicyNumber());
        patientData.setPatientPicturePath(currentPatient.getPatientPicturePath());
        
        // Copy medical information
        patientData.setMedications(currentPatient.getMedications());
        patientData.setDiagnoses(currentPatient.getDiagnoses());
        patientData.setAllergies(currentPatient.getAllergies());
        
        // Mark identification as complete since we have a filled form
        patientData.setStepCompleted("identification", true);
        patientData.setStepCompleted("contact", true);
        
        // Add session note
        patientData.addSessionNote("Patient data entered via Patient Information Form");
        
        return patientData;
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
        message.append("Form Completion: ").append(patientData.getCompletionPercentage()).append("%\n");
        message.append("Data Source: Patient Information Form\n\n");
        
        message.append("STORAGE SUMMARY:\n");
        message.append("Total Patients Saved: ").append(storage.getPatientCount()).append("\n");
        message.append("Patients from Today: ").append(storage.getPatientsFromToday().size()).append("\n");
        message.append("Completed Check-ins: ").append(storage.getCompletedCheckIns().size()).append("\n\n");
        
        message.append("The patient can be accessed later using Patient ID: ");
        message.append(patientData.getPatientId());
        
        alert.setContentText(message.toString());
        
        // Make the dialog resizable for long content
        alert.setResizable(true);
        alert.getDialogPane().setPrefSize(450, 300);
        
        alert.showAndWait();
    }
    
    public void show() {
        stage.show();
    }
    
    public void close() {
        stage.close();
    }
}