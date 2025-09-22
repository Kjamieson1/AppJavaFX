import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.logging.Logger;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * GUI class for managing patient appointments
 */
public class AppointmentManagerGUI {
    
    private static final Logger LOGGER = Logger.getLogger(AppointmentManagerGUI.class.getName());
    
    private Stage stage;
    private NewPatient patient;
    
    // Controls for scheduling new appointments
    private DatePicker appointmentDatePicker;
    private ComboBox<String> appointmentTimeComboBox;
    private TextField doctorNameField;
    private ComboBox<String> appointmentTypeComboBox;
    
    // Lists for displaying appointments
    private ListView<NewPatient.Appointment> upcomingAppointmentsListView;
    private ListView<NewPatient.Appointment> appointmentHistoryListView;
    private TextArea appointmentReportsTextArea;
    
    public AppointmentManagerGUI(NewPatient patient) {
        this.patient = patient;
        initializeGUI();
    }
    
    private void initializeGUI() {
        stage = new Stage();
        stage.setTitle("Appointment Manager - " + patient.getFullName());
        
        VBox mainLayout = new VBox(15);
        mainLayout.setPadding(new Insets(20));
        
        // Create tabs for different appointment management sections
        TabPane tabPane = new TabPane();
        
        Tab scheduleTab = new Tab("Schedule New Appointment");
        scheduleTab.setContent(createScheduleAppointmentSection());
        scheduleTab.setClosable(false);
        
        Tab upcomingTab = new Tab("Upcoming Appointments");
        upcomingTab.setContent(createUpcomingAppointmentsSection());
        upcomingTab.setClosable(false);
        
        Tab historyTab = new Tab("Appointment History");
        historyTab.setContent(createAppointmentHistorySection());
        historyTab.setClosable(false);
        
        Tab reportsTab = new Tab("Reports");
        reportsTab.setContent(createReportsSection());
        reportsTab.setClosable(false);
        
        tabPane.getTabs().addAll(scheduleTab, upcomingTab, historyTab, reportsTab);
        
        mainLayout.getChildren().add(tabPane);
        
        Scene scene = new Scene(mainLayout, 800, 600);
        stage.setScene(scene);
        
        // Load existing appointment data
        refreshAppointmentData();
    }
    
    private VBox createScheduleAppointmentSection() {
        VBox section = new VBox(15);
        section.setPadding(new Insets(20));
        
        Label titleLabel = new Label("Schedule New Appointment");
        titleLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(15);
        
        // Date selection
        grid.add(new Label("Appointment Date:"), 0, 0);
        appointmentDatePicker = new DatePicker();
        appointmentDatePicker.setValue(LocalDate.now().plusDays(1)); // Default to tomorrow
        grid.add(appointmentDatePicker, 1, 0);
        
        // Time selection
        grid.add(new Label("Appointment Time:"), 0, 1);
        appointmentTimeComboBox = new ComboBox<>();
        populateTimeComboBox();
        grid.add(appointmentTimeComboBox, 1, 1);
        
        // Doctor name
        grid.add(new Label("Doctor Name:"), 0, 2);
        doctorNameField = new TextField();
        doctorNameField.setPromptText("Enter doctor's name");
        grid.add(doctorNameField, 1, 2);
        
        // Appointment type
        grid.add(new Label("Appointment Type:"), 0, 3);
        appointmentTypeComboBox = new ComboBox<>();
        appointmentTypeComboBox.getItems().addAll(
            "Regular Checkup",
            "Follow-up",
            "Consultation",
            "Physical Exam",
            "Lab Results Review",
            "Specialist Referral",
            "Emergency Visit",
            "Other"
        );
        grid.add(appointmentTypeComboBox, 1, 3);
        
        Button scheduleButton = new Button("Schedule Appointment");
        scheduleButton.setOnAction(e -> scheduleNewAppointment());
        scheduleButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        
        section.getChildren().addAll(titleLabel, grid, scheduleButton);
        return section;
    }
    
    private VBox createUpcomingAppointmentsSection() {
        VBox section = new VBox(10);
        section.setPadding(new Insets(20));
        
        Label titleLabel = new Label("Upcoming Appointments");
        titleLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        
        upcomingAppointmentsListView = new ListView<>();
        upcomingAppointmentsListView.setPrefHeight(300);
        
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);
        
        Button cancelButton = new Button("Cancel Selected");
        cancelButton.setOnAction(e -> cancelSelectedAppointment());
        cancelButton.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");
        
        Button completeButton = new Button("Mark as Completed");
        completeButton.setOnAction(e -> completeSelectedAppointment());
        completeButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");
        
        Button refreshButton = new Button("Refresh");
        refreshButton.setOnAction(e -> refreshAppointmentData());
        
        buttonBox.getChildren().addAll(cancelButton, completeButton, refreshButton);
        
        section.getChildren().addAll(titleLabel, upcomingAppointmentsListView, buttonBox);
        return section;
    }
    
    private VBox createAppointmentHistorySection() {
        VBox section = new VBox(10);
        section.setPadding(new Insets(20));
        
        Label titleLabel = new Label("Appointment History");
        titleLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        
        appointmentHistoryListView = new ListView<>();
        appointmentHistoryListView.setPrefHeight(350);
        
        Button refreshButton = new Button("Refresh History");
        refreshButton.setOnAction(e -> refreshAppointmentData());
        
        section.getChildren().addAll(titleLabel, appointmentHistoryListView, refreshButton);
        return section;
    }
    
    private VBox createReportsSection() {
        VBox section = new VBox(10);
        section.setPadding(new Insets(20));
        
        Label titleLabel = new Label("Appointment Reports");
        titleLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        
        appointmentReportsTextArea = new TextArea();
        appointmentReportsTextArea.setPrefHeight(350);
        appointmentReportsTextArea.setEditable(false);
        
        Button refreshButton = new Button("Refresh Reports");
        refreshButton.setOnAction(e -> refreshReports());
        
        section.getChildren().addAll(titleLabel, appointmentReportsTextArea, refreshButton);
        return section;
    }
    
    private void populateTimeComboBox() {
        // Add time slots from 8 AM to 6 PM in 30-minute intervals
        for (int hour = 8; hour <= 18; hour++) {
            for (int minute = 0; minute < 60; minute += 30) {
                if (hour == 18 && minute > 0) break; // Stop at 6:00 PM
                LocalTime time = LocalTime.of(hour, minute);
                appointmentTimeComboBox.getItems().add(time.toString());
            }
        }
    }
    
    private void scheduleNewAppointment() {
        try {
            LocalDate date = appointmentDatePicker.getValue();
            String timeString = appointmentTimeComboBox.getValue();
            String doctorName = doctorNameField.getText().trim();
            String appointmentType = appointmentTypeComboBox.getValue();
            
            // Validation
            if (date == null) {
                showAlert("Validation Error", "Please select an appointment date.");
                return;
            }
            
            if (timeString == null || timeString.isEmpty()) {
                showAlert("Validation Error", "Please select an appointment time.");
                return;
            }
            
            if (doctorName.isEmpty()) {
                showAlert("Validation Error", "Please enter the doctor's name.");
                return;
            }
            
            if (appointmentType == null) {
                showAlert("Validation Error", "Please select an appointment type.");
                return;
            }
            
            // Parse time
            LocalTime time = LocalTime.parse(timeString);
            LocalDateTime appointmentDateTime = LocalDateTime.of(date, time);
            
            // Check if appointment is in the future
            if (appointmentDateTime.isBefore(LocalDateTime.now())) {
                showAlert("Validation Error", "Appointment must be scheduled for a future date and time.");
                return;
            }
            
            // Schedule the appointment
            patient.scheduleAppointment(appointmentDateTime, doctorName, appointmentType);
            
            showAlert("Success", "Appointment scheduled successfully!");
            LOGGER.info("New appointment scheduled for " + patient.getFullName());
            
            // Clear form and refresh data
            clearScheduleForm();
            refreshAppointmentData();
            
        } catch (Exception e) {
            LOGGER.warning("Error scheduling appointment: " + e.getMessage());
            showAlert("Error", "Failed to schedule appointment: " + e.getMessage());
        }
    }
    
    private void cancelSelectedAppointment() {
        NewPatient.Appointment selectedAppointment = upcomingAppointmentsListView.getSelectionModel().getSelectedItem();
        if (selectedAppointment == null) {
            showAlert("Selection Error", "Please select an appointment to cancel.");
            return;
        }
        
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Cancellation");
        confirmAlert.setHeaderText("Cancel Appointment");
        confirmAlert.setContentText("Are you sure you want to cancel this appointment?");
        
        if (confirmAlert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            patient.cancelAppointment(selectedAppointment);
            showAlert("Success", "Appointment cancelled successfully.");
            refreshAppointmentData();
        }
    }
    
    private void completeSelectedAppointment() {
        NewPatient.Appointment selectedAppointment = upcomingAppointmentsListView.getSelectionModel().getSelectedItem();
        if (selectedAppointment == null) {
            showAlert("Selection Error", "Please select an appointment to mark as completed.");
            return;
        }
        
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Complete Appointment");
        dialog.setHeaderText("Mark Appointment as Completed");
        dialog.setContentText("Enter notes for this appointment:");
        
        dialog.showAndWait().ifPresent(notes -> {
            patient.completeAppointment(selectedAppointment, notes);
            showAlert("Success", "Appointment marked as completed.");
            refreshAppointmentData();
        });
    }
    
    private void clearScheduleForm() {
        appointmentDatePicker.setValue(LocalDate.now().plusDays(1));
        appointmentTimeComboBox.setValue(null);
        doctorNameField.clear();
        appointmentTypeComboBox.setValue(null);
    }
    
    private void refreshAppointmentData() {
        // Refresh upcoming appointments
        upcomingAppointmentsListView.getItems().clear();
        upcomingAppointmentsListView.getItems().addAll(patient.getUpcomingAppointments());
        
        // Refresh appointment history
        appointmentHistoryListView.getItems().clear();
        appointmentHistoryListView.getItems().addAll(patient.getAppointmentHistory());
        
        // Refresh reports
        refreshReports();
    }
    
    private void refreshReports() {
        StringBuilder reports = new StringBuilder();
        reports.append("=== APPOINTMENT SUMMARY ===\n");
        reports.append("Patient: ").append(patient.getFullName()).append("\n");
        reports.append("Total Appointments: ").append(patient.getAppointments().size()).append("\n");
        reports.append("Upcoming: ").append(patient.getUpcomingAppointments().size()).append("\n");
        reports.append("Completed: ").append(patient.getAppointmentCountByStatus(NewPatient.Appointment.AppointmentStatus.COMPLETED)).append("\n");
        reports.append("Cancelled: ").append(patient.getAppointmentCountByStatus(NewPatient.Appointment.AppointmentStatus.CANCELLED)).append("\n\n");
        
        reports.append("=== APPOINTMENT REPORTS ===\n");
        for (String report : patient.getAppointmentReports()) {
            reports.append("• ").append(report).append("\n");
        }
        
        appointmentReportsTextArea.setText(reports.toString());
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