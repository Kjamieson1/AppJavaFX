import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Storage manager for patient data objects.
 * This class manages an in-memory array of PatientDataObject instances
 * and provides methods for saving, retrieving, and managing patient data.
 * In the future, this can be easily extended to integrate with a database.
 */
public class PatientDataStorage {
    
    private static final Logger LOGGER = Logger.getLogger(PatientDataStorage.class.getName());
    
    // Singleton instance
    private static PatientDataStorage instance;
    
    // In-memory storage (will be replaced with database integration later)
    private List<PatientDataObject> savedPatients;
    
    /**
     * Private constructor for singleton pattern
     */
    private PatientDataStorage() {
        this.savedPatients = new ArrayList<>();
        LOGGER.info("PatientDataStorage initialized");
    }
    
    /**
     * Get singleton instance
     */
    public static synchronized PatientDataStorage getInstance() {
        if (instance == null) {
            instance = new PatientDataStorage();
        }
        return instance;
    }
    
    /**
     * Save a patient data object to storage
     * @param patientData The patient data to save
     * @return true if saved successfully, false otherwise
     */
    public boolean savePatientData(PatientDataObject patientData) {
        if (patientData == null) {
            LOGGER.warning("Cannot save null patient data");
            return false;
        }
        
        try {
            // Check if patient already exists (update scenario)
            Optional<PatientDataObject> existingPatient = findPatientById(patientData.getPatientId());
            
            if (existingPatient.isPresent()) {
                // Update existing patient
                int index = savedPatients.indexOf(existingPatient.get());
                savedPatients.set(index, patientData);
                LOGGER.info("Updated existing patient: " + patientData.getPatientId());
            } else {
                // Add new patient
                savedPatients.add(patientData);
                LOGGER.info("Saved new patient: " + patientData.getPatientId());
            }
            
            LOGGER.info("Total patients in storage: " + savedPatients.size());
            return true;
            
        } catch (Exception e) {
            LOGGER.severe("Error saving patient data: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Find a patient by ID
     * @param patientId The patient ID to search for
     * @return Optional containing the patient if found, empty otherwise
     */
    public Optional<PatientDataObject> findPatientById(String patientId) {
        if (patientId == null || patientId.trim().isEmpty()) {
            return Optional.empty();
        }
        
        return savedPatients.stream()
                .filter(patient -> patientId.equals(patient.getPatientId()))
                .findFirst();
    }
    
    /**
     * Find patients by name (first or last name contains search term)
     * @param searchTerm The search term
     * @return List of matching patients
     */
    public List<PatientDataObject> findPatientsByName(String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return new ArrayList<>();
        }
        
        String lowerSearchTerm = searchTerm.toLowerCase().trim();
        
        return savedPatients.stream()
                .filter(patient -> {
                    String firstName = patient.getFirstName();
                    String lastName = patient.getLastName();
                    return (firstName != null && firstName.toLowerCase().contains(lowerSearchTerm)) ||
                           (lastName != null && lastName.toLowerCase().contains(lowerSearchTerm));
                })
                .collect(Collectors.toList());
    }
    
    /**
     * Find patients by date of birth
     * @param dateOfBirth The date of birth to search for
     * @return List of matching patients
     */
    public List<PatientDataObject> findPatientsByDateOfBirth(LocalDate dateOfBirth) {
        if (dateOfBirth == null) {
            return new ArrayList<>();
        }
        
        return savedPatients.stream()
                .filter(patient -> dateOfBirth.equals(patient.getDateOfBirth()))
                .collect(Collectors.toList());
    }
    
    /**
     * Get all saved patients
     * @return List of all saved patients
     */
    public List<PatientDataObject> getAllPatients() {
        return new ArrayList<>(savedPatients);
    }
    
    /**
     * Get patients saved today
     * @return List of patients saved today
     */
    public List<PatientDataObject> getPatientsFromToday() {
        LocalDate today = LocalDate.now();
        
        return savedPatients.stream()
                .filter(patient -> patient.getSavedTimestamp().toLocalDate().equals(today))
                .collect(Collectors.toList());
    }
    
    /**
     * Get completed check-ins
     * @return List of patients with completed check-ins
     */
    public List<PatientDataObject> getCompletedCheckIns() {
        return savedPatients.stream()
                .filter(PatientDataObject::isCheckInComplete)
                .collect(Collectors.toList());
    }
    
    /**
     * Get incomplete check-ins
     * @return List of patients with incomplete check-ins
     */
    public List<PatientDataObject> getIncompleteCheckIns() {
        return savedPatients.stream()
                .filter(patient -> !patient.isCheckInComplete())
                .collect(Collectors.toList());
    }
    
    /**
     * Delete a patient by ID
     * @param patientId The patient ID to delete
     * @return true if deleted successfully, false otherwise
     */
    public boolean deletePatient(String patientId) {
        if (patientId == null || patientId.trim().isEmpty()) {
            return false;
        }
        
        boolean removed = savedPatients.removeIf(patient -> patientId.equals(patient.getPatientId()));
        
        if (removed) {
            LOGGER.info("Deleted patient: " + patientId);
        } else {
            LOGGER.warning("Patient not found for deletion: " + patientId);
        }
        
        return removed;
    }
    
    /**
     * Clear all patient data (use with caution!)
     */
    public void clearAllData() {
        int count = savedPatients.size();
        savedPatients.clear();
        LOGGER.warning("Cleared all patient data (" + count + " patients)");
    }
    
    /**
     * Get storage statistics
     * @return Storage statistics as a formatted string
     */
    public String getStorageStatistics() {
        int totalPatients = savedPatients.size();
        int completedCheckIns = getCompletedCheckIns().size();
        int incompleteCheckIns = getIncompleteCheckIns().size();
        int todaysPatients = getPatientsFromToday().size();
        
        StringBuilder stats = new StringBuilder();
        stats.append("=== PATIENT DATA STORAGE STATISTICS ===\n");
        stats.append("Total Patients: ").append(totalPatients).append("\n");
        stats.append("Completed Check-ins: ").append(completedCheckIns).append("\n");
        stats.append("Incomplete Check-ins: ").append(incompleteCheckIns).append("\n");
        stats.append("Patients from Today: ").append(todaysPatients).append("\n");
        
        if (totalPatients > 0) {
            double completionRate = (completedCheckIns * 100.0) / totalPatients;
            stats.append("Completion Rate: ").append(String.format("%.1f%%", completionRate)).append("\n");
        }
        
        return stats.toString();
    }
    
    /**
     * Export all patient data as JSON strings
     * @return List of JSON strings representing all patients
     */
    public List<String> exportAllPatientsAsJson() {
        return savedPatients.stream()
                .map(PatientDataObject::toJsonString)
                .collect(Collectors.toList());
    }
    
    /**
     * Get a summary of all patients
     * @return Formatted string with patient summaries
     */
    public String getAllPatientsSummary() {
        if (savedPatients.isEmpty()) {
            return "No patients saved in storage.";
        }
        
        StringBuilder summary = new StringBuilder();
        summary.append("=== ALL PATIENTS SUMMARY ===\n");
        summary.append("Total Patients: ").append(savedPatients.size()).append("\n\n");
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm");
        
        for (int i = 0; i < savedPatients.size(); i++) {
            PatientDataObject patient = savedPatients.get(i);
            summary.append(String.format("%d. %s (ID: %s)\n", 
                i + 1, patient.getFullName().trim(), patient.getPatientId()));
            summary.append("   Saved: ").append(patient.getSavedTimestamp().format(formatter));
            summary.append(" | Completion: ").append(patient.getCompletionPercentage()).append("%");
            summary.append(" | Status: ").append(patient.isCheckInComplete() ? "Complete" : "Incomplete");
            summary.append("\n");
            
            if (patient.getAppointmentDateTime() != null) {
                summary.append("   Appointment: ").append(patient.getAppointmentDateTime().format(formatter));
                if (patient.getDoctorName() != null) {
                    summary.append(" with Dr. ").append(patient.getDoctorName());
                }
                summary.append("\n");
            }
            summary.append("\n");
        }
        
        return summary.toString();
    }
    
    /**
     * Create a PatientDataObject from GUI form data
     * @param checkInGUI The check-in GUI with form data
     * @return PatientDataObject with collected form data
     */
    public static PatientDataObject createFromGUI(PatientCheckInGUI checkInGUI) {
        if (checkInGUI == null) {
            return new PatientDataObject();
        }
        
        CheckInWorkflow workflow = checkInGUI.getCheckInWorkflow();
        PatientDataObject patientData = new PatientDataObject(
            workflow.getPatient(), 
            workflow.getCurrentSession()
        );
        
        // Additional data that might not be captured in the workflow
        // This would be populated from the GUI fields directly if needed
        
        return patientData;
    }
    
    /**
     * Get the number of patients in storage
     * @return Number of saved patients
     */
    public int getPatientCount() {
        return savedPatients.size();
    }
    
    /**
     * Check if storage is empty
     * @return true if no patients are saved, false otherwise
     */
    public boolean isEmpty() {
        return savedPatients.isEmpty();
    }
}