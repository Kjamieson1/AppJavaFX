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
        stage.setTitle("Patient Information Form");
        
        // Create main layout
        VBox mainLayout = new VBox(10);
        mainLayout.setPadding(new Insets(20));
        
        // Create scroll pane for the form
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(mainLayout);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefViewportHeight(600);
        
        // Add sections to the form
        mainLayout.getChildren().addAll(
            createPatientInfoSection(),
            createPatientPictureSection(),
            createMedicalHistorySection(),
            createButtonSection()
        );
        
        // Create scene and set stage
        Scene scene = new Scene(scrollPane, 800, 700);
        stage.setScene(scene);
        
        // Load existing patient data if any
        loadPatientData();
    }
    
    private VBox createPatientInfoSection() {
        VBox section = new VBox(10);
        section.getChildren().add(new Label("Patient Demographics:"));
        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10));
        
        // Row 0
        grid.add(new Label("First Name:"), 0, 0);
        firstNameField = new TextField();
        grid.add(firstNameField, 1, 0);
        
        grid.add(new Label("Last Name:"), 2, 0);
        lastNameField = new TextField();
        grid.add(lastNameField, 3, 0);
        
        // Row 1
        grid.add(new Label("Date of Birth:"), 0, 1);
        dateOfBirthPicker = new DatePicker();
        grid.add(dateOfBirthPicker, 1, 1);
        
        grid.add(new Label("Gender:"), 2, 1);
        genderComboBox = new ComboBox<>();
        genderComboBox.getItems().addAll("Male", "Female", "Other", "Prefer not to say");
        grid.add(genderComboBox, 3, 1);
        
        // Row 2
        grid.add(new Label("Address:"), 0, 2);
        addressField = new TextField();
        grid.add(addressField, 1, 2, 3, 1); // span 3 columns
        
        // Row 3
        grid.add(new Label("Phone:"), 0, 3);
        phoneField = new TextField();
        grid.add(phoneField, 1, 3);
        
        grid.add(new Label("Email:"), 2, 3);
        emailField = new TextField();
        grid.add(emailField, 3, 3);
        
        // Row 4
        grid.add(new Label("Emergency Contact:"), 0, 4);
        emergencyContactField = new TextField();
        grid.add(emergencyContactField, 1, 4);
        
        grid.add(new Label("Emergency Phone:"), 2, 4);
        emergencyPhoneField = new TextField();
        grid.add(emergencyPhoneField, 3, 4);
        
        // Row 5
        grid.add(new Label("Insurance Provider:"), 0, 5);
        insuranceProviderField = new TextField();
        grid.add(insuranceProviderField, 1, 5);
        
        grid.add(new Label("Policy Number:"), 2, 5);
        insurancePolicyField = new TextField();
        grid.add(insurancePolicyField, 3, 5);
        
        section.getChildren().add(grid);
        return section;
    }
    
    private VBox createPatientPictureSection() {
        VBox section = new VBox(10);
        section.getChildren().add(new Label("Patient Picture:"));
        
        HBox pictureBox = new HBox(10);
        pictureBox.setAlignment(Pos.CENTER_LEFT);
        
        patientImageView = new ImageView();
        patientImageView.setFitHeight(100);
        patientImageView.setFitWidth(100);
        patientImageView.setPreserveRatio(true);
        patientImageView.setStyle("-fx-border-color: gray; -fx-border-width: 1;");
        
        Button selectPictureButton = new Button("Select Picture");
        selectPictureButton.setOnAction(e -> selectPatientPicture());
        
        pictureBox.getChildren().addAll(patientImageView, selectPictureButton);
        section.getChildren().add(pictureBox);
        
        return section;
    }
    
    private VBox createMedicalHistorySection() {
        VBox section = new VBox(10);
        section.getChildren().add(new Label("Medical History:"));
        
        HBox medicalBox = new HBox(20);
        
        // Medications section
        VBox medicationsBox = createMedicalListSection("Medications:", 
            medicationsListView = new ListView<>(),
            medicationInput = new TextField(),
            "Add Medication",
            () -> addMedication());
        
        // Diagnoses section
        VBox diagnosesBox = createMedicalListSection("Diagnoses:", 
            diagnosesListView = new ListView<>(),
            diagnosisInput = new TextField(),
            "Add Diagnosis",
            () -> addDiagnosis());
        
        // Allergies section
        VBox allergiesBox = createMedicalListSection("Allergies:", 
            allergiesListView = new ListView<>(),
            allergyInput = new TextField(),
            "Add Allergy",
            () -> addAllergy());
        
        medicalBox.getChildren().addAll(medicationsBox, diagnosesBox, allergiesBox);
        section.getChildren().add(medicalBox);
        
        return section;
    }
    
    private VBox createMedicalListSection(String title, ListView<String> listView, 
                                         TextField inputField, String buttonText, Runnable addAction) {
        VBox box = new VBox(5);
        box.getChildren().add(new Label(title));
        
        listView.setPrefHeight(100);
        listView.setPrefWidth(200);
        
        inputField.setPromptText("Enter " + title.toLowerCase().substring(0, title.length()-1));
        
        Button addButton = new Button(buttonText);
        addButton.setOnAction(e -> addAction.run());
        
        Button removeButton = new Button("Remove Selected");
        removeButton.setOnAction(e -> {
            String selected = listView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                listView.getItems().remove(selected);
                updatePatientMedicalHistory();
            }
        });
        
        HBox buttonBox = new HBox(5);
        buttonBox.getChildren().addAll(addButton, removeButton);
        
        box.getChildren().addAll(listView, inputField, buttonBox);
        return box;
    }
    
    private VBox createButtonSection() {
        VBox section = new VBox(10);
        section.setAlignment(Pos.CENTER);
        
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);
        
        Button saveButton = new Button("Save Patient");
        saveButton.setOnAction(e -> savePatient());
        
        Button clearButton = new Button("Clear Form");
        clearButton.setOnAction(e -> clearForm());
        
        Button appointmentButton = new Button("Manage Appointments");
        appointmentButton.setOnAction(e -> openAppointmentManager());
        
        buttonBox.getChildren().addAll(saveButton, clearButton, appointmentButton);
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
                LOGGER.warning("Failed to load image: " + e.getMessage());
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
                LOGGER.info("Patient saved: " + currentPatient.getFullName());
            } else {
                showAlert("Validation Error", "Please fill in all required fields (First Name, Last Name, Date of Birth).");
            }
        } catch (Exception e) {
            LOGGER.warning("Error saving patient: " + e.getMessage());
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
    
    public void show() {
        stage.show();
    }
    
    public void close() {
        stage.close();
    }
}