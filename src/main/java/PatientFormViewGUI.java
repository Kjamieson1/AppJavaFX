import java.util.logging.Logger;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

// Class to display a form for a new patient or details of an existing patient
public class PatientFormViewGUI {

    private static final Logger LOGGER = Logger.getLogger(PatientFormViewGUI.class.getName());

    private Stage stage;
    private NewPatient currentPatient;
    private PatientDataObject patientData;
    
    // Form fields for patient Name
    private TextField firstName;
    private TextField lastName;

    public PatientFormViewGUI() 
    {
        this.currentPatient = new NewPatient();
        initializeGUI();
    }
    public PatientFormViewGUI(PatientDataObject pdo) 
    {
        this.patientData = pdo;
        this.currentPatient = null;
        initializeGUI();
    }
    
    private void initializeGUI() 
    {
        stage = new Stage();
        stage.setTitle("Current Patient Form");
        
        // Create main layout with styling
        VBox mainLayout = new VBox(0);
        mainLayout.setStyle(
            "-fx-background: linear-gradient(to bottom, #e8f5e8, #f0f9f0);" 
        );
        
        // Create scroll pane for the form with styling
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setStyle(
            "-fx-background: transparent;" +
            "-fx-background-color: transparent;"
        );
        
        // Content container that goes inside the scroll pane
        VBox content = new VBox(12);
        content.setPadding(new Insets(20));
        content.setAlignment(Pos.TOP_LEFT);

        
        final String primaryTextColor = "#1b5e20";

        if (patientData != null) 
        {
            Label header = new Label("Patient Details");
            header.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: " + primaryTextColor + ";");

            Label name = new Label("Name: " + safe(patientData.getFullName()));
            name.setStyle("-fx-text-fill: " + primaryTextColor + "; -fx-font-size: 14px;");

            // Date of birth and gender
            Label dobLabel = new Label("Date of Birth: " + safe(patientData.getDateOfBirth()));
            dobLabel.setStyle("-fx-text-fill: " + primaryTextColor + ";");
            Label genderLabel = new Label("Gender: " + safe(patientData.getGender()));
            genderLabel.setStyle("-fx-text-fill: " + primaryTextColor + ";");

            // Diagnoses
            Label diagLabel = new Label("Diagnoses:");
            diagLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: " + primaryTextColor + ";");
            ListView<String> diagnosesList = new ListView<>(FXCollections.observableArrayList(patientData.getDiagnoses()));
            diagnosesList.setPrefHeight(160);

            // Medications
            Label medsLabel = new Label("Medications:");
            medsLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: " + primaryTextColor + ";");
            ListView<String> medsList = new ListView<>(FXCollections.observableArrayList(patientData.getMedications()));
            medsList.setPrefHeight(120);

            // Allergies
            Label allergyLabel = new Label("Allergies:");
            allergyLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: " + primaryTextColor + ";");
            ListView<String> allergyList = new ListView<>(FXCollections.observableArrayList(patientData.getAllergies()));
            allergyList.setPrefHeight(100);

            // Container style for lists
            VBox diagContainer = new VBox(8);
            diagContainer.setPadding(new javafx.geometry.Insets(10));
            String diagBg = "linear-gradient(to bottom, #dff2df, #e8f6e8)";
            diagContainer.setStyle("-fx-background-color: " + diagBg + "; -fx-background-radius: 10; -fx-border-radius: 10; -fx-border-color: #cfe8cf; -fx-border-width: 1;");
            diagContainer.getChildren().addAll(diagLabel, diagnosesList, medsLabel, medsList, allergyLabel, allergyList);

            content.getChildren().addAll(header, name, dobLabel, genderLabel, diagContainer);
        } else 
        {
            Label header = new Label("New Patient (blank form)");
            header.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: " + primaryTextColor + ";");

            HBox names = new HBox(8);
            names.setAlignment(Pos.CENTER_LEFT);
            firstName = new TextField();
            firstName.setPromptText("First name");
            lastName = new TextField();
            lastName.setPromptText("Last name");
            names.getChildren().addAll(firstName, lastName);

            content.getChildren().addAll(header, names);
        }

        scrollPane.setContent(content);

        // Add header and form to main layout
        mainLayout.getChildren().addAll(scrollPane);
        
        // Create scene with modern styling
        Scene scene = new Scene(mainLayout, 450, 550);
        stage.setScene(scene);
        stage.centerOnScreen();
    }




    public void show() 
    {
        stage.show();
    }
    
    public void close() 
    {
        stage.close();
    }

    // Return a safe string for display; avoids NPEs when fields are null.
    private String safe(Object o) 
    {
        return o == null ? "" : String.valueOf(o);
    }
}