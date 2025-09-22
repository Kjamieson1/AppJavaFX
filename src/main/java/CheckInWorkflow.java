import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Concrete implementation of PatientCheckInInterface that handles the complete
 * patient check-in workflow and integrates with the NewPatient class.
 */
public class CheckInWorkflow implements PatientCheckInInterface {
    
    private static final Logger LOGGER = Logger.getLogger(CheckInWorkflow.class.getName());
    
    private NewPatient patient;
    private CheckInSession currentSession;
    
    /**
     * Inner class to track check-in session information
     */
    public static class CheckInSession {
        private LocalDateTime checkInStartTime;
        private LocalDateTime checkInCompleteTime;
        private boolean identificationVerified;
        private boolean insuranceVerified;
        private boolean appointmentConfirmed;
        private boolean contactInfoUpdated;
        private boolean paymentProcessed;
        private boolean healthScreeningComplete;
        private boolean checkInComplete;
        private List<String> sessionNotes;
        private String waitingAreaAssignment;
        
        public CheckInSession() {
            this.checkInStartTime = LocalDateTime.now();
            this.sessionNotes = new ArrayList<>();
            this.identificationVerified = false;
            this.insuranceVerified = false;
            this.appointmentConfirmed = false;
            this.contactInfoUpdated = false;
            this.paymentProcessed = false;
            this.healthScreeningComplete = false;
            this.checkInComplete = false;
        }
        
        // Getters and setters
        public LocalDateTime getCheckInStartTime() { return checkInStartTime; }
        public LocalDateTime getCheckInCompleteTime() { return checkInCompleteTime; }
        public void setCheckInCompleteTime(LocalDateTime checkInCompleteTime) { this.checkInCompleteTime = checkInCompleteTime; }
        
        public boolean isIdentificationVerified() { return identificationVerified; }
        public void setIdentificationVerified(boolean identificationVerified) { this.identificationVerified = identificationVerified; }
        
        public boolean isInsuranceVerified() { return insuranceVerified; }
        public void setInsuranceVerified(boolean insuranceVerified) { this.insuranceVerified = insuranceVerified; }
        
        public boolean isAppointmentConfirmed() { return appointmentConfirmed; }
        public void setAppointmentConfirmed(boolean appointmentConfirmed) { this.appointmentConfirmed = appointmentConfirmed; }
        
        public boolean isContactInfoUpdated() { return contactInfoUpdated; }
        public void setContactInfoUpdated(boolean contactInfoUpdated) { this.contactInfoUpdated = contactInfoUpdated; }
        
        public boolean isPaymentProcessed() { return paymentProcessed; }
        public void setPaymentProcessed(boolean paymentProcessed) { this.paymentProcessed = paymentProcessed; }
        
        public boolean isHealthScreeningComplete() { return healthScreeningComplete; }
        public void setHealthScreeningComplete(boolean healthScreeningComplete) { this.healthScreeningComplete = healthScreeningComplete; }
        
        public boolean isCheckInComplete() { return checkInComplete; }
        public void setCheckInComplete(boolean checkInComplete) { this.checkInComplete = checkInComplete; }
        
        public List<String> getSessionNotes() { return new ArrayList<>(sessionNotes); }
        public void addSessionNote(String note) { this.sessionNotes.add(note); }
        
        public String getWaitingAreaAssignment() { return waitingAreaAssignment; }
        public void setWaitingAreaAssignment(String waitingAreaAssignment) { this.waitingAreaAssignment = waitingAreaAssignment; }
        
        public boolean isReadyForCompletion() {
            return identificationVerified && insuranceVerified && appointmentConfirmed && 
                   contactInfoUpdated && paymentProcessed && healthScreeningComplete;
        }
    }
    
    /**
     * Constructor to initialize check-in workflow
     */
    public CheckInWorkflow() {
        this.patient = new NewPatient();
        this.currentSession = new CheckInSession();
    }
    
    /**
     * Constructor with existing patient
     */
    public CheckInWorkflow(NewPatient patient) {
        this.patient = patient != null ? patient : new NewPatient();
        this.currentSession = new CheckInSession();
    }
    
    @Override
    public boolean validatePatientIdentification(String firstName, String lastName, String dateOfBirth) {
        try {
            if (firstName == null || firstName.trim().isEmpty() ||
                lastName == null || lastName.trim().isEmpty() ||
                dateOfBirth == null || dateOfBirth.trim().isEmpty()) {
                LOGGER.warning("Patient identification validation failed: Missing required fields");
                return false;
            }
            
            // Parse date of birth to validate format
            LocalDate dob = LocalDate.parse(dateOfBirth, DateTimeFormatter.ofPattern("MM/dd/yyyy"));
            
            // Update patient information
            patient.setFirstName(firstName.trim());
            patient.setLastName(lastName.trim());
            patient.setDateOfBirth(dob);
            
            currentSession.setIdentificationVerified(true);
            currentSession.addSessionNote("Patient identification verified: " + patient.getFullName());
            
            LOGGER.info("Patient identification validated for: " + patient.getFullName());
            return true;
            
        } catch (DateTimeParseException e) {
            LOGGER.warning("Invalid date format for date of birth: " + dateOfBirth);
            return false;
        } catch (Exception e) {
            LOGGER.warning("Error validating patient identification: " + e.getMessage());
            return false;
        }
    }
    
    @Override
    public boolean verifyInsuranceCoverage(String insuranceProvider, String policyNumber, String groupNumber) {
        try {
            if (insuranceProvider == null || insuranceProvider.trim().isEmpty() ||
                policyNumber == null || policyNumber.trim().isEmpty()) {
                LOGGER.warning("Insurance verification failed: Missing provider or policy number");
                return false;
            }
            
            // Simulate insurance verification (in real system, this would call insurance API)
            patient.setInsuranceProvider(insuranceProvider.trim());
            patient.setInsurancePolicyNumber(policyNumber.trim());
            
            // Simulate verification delay and random success/failure for demo
            boolean verificationSuccess = !policyNumber.trim().equals("INVALID");
            
            if (verificationSuccess) {
                currentSession.setInsuranceVerified(true);
                currentSession.addSessionNote("Insurance verified: " + insuranceProvider + " Policy: " + policyNumber);
                LOGGER.info("Insurance coverage verified for patient: " + patient.getFullName());
            } else {
                currentSession.addSessionNote("Insurance verification failed: Invalid policy number");
                LOGGER.warning("Insurance verification failed for policy: " + policyNumber);
            }
            
            return verificationSuccess;
            
        } catch (Exception e) {
            LOGGER.warning("Error verifying insurance coverage: " + e.getMessage());
            return false;
        }
    }
    
    @Override
    public boolean confirmScheduledAppointment(LocalDateTime appointmentDateTime, String doctorName, String appointmentType) {
        try {
            if (appointmentDateTime == null || doctorName == null || doctorName.trim().isEmpty() ||
                appointmentType == null || appointmentType.trim().isEmpty()) {
                LOGGER.warning("Appointment confirmation failed: Missing required information");
                return false;
            }
            
            // Check if patient has a matching appointment
            List<NewPatient.Appointment> appointments = patient.getUpcomingAppointments();
            boolean appointmentFound = false;
            
            for (NewPatient.Appointment appointment : appointments) {
                if (appointment.getDoctorName().equalsIgnoreCase(doctorName.trim()) &&
                    appointment.getAppointmentType().equalsIgnoreCase(appointmentType.trim()) &&
                    appointment.getAppointmentDateTime().toLocalDate().equals(appointmentDateTime.toLocalDate())) {
                    appointmentFound = true;
                    break;
                }
            }
            
            // If no existing appointment, create one for demo purposes
            if (!appointmentFound) {
                patient.scheduleAppointment(appointmentDateTime, doctorName.trim(), appointmentType.trim());
                currentSession.addSessionNote("New appointment created during check-in");
            }
            
            currentSession.setAppointmentConfirmed(true);
            currentSession.addSessionNote("Appointment confirmed: " + appointmentType + " with Dr. " + doctorName);
            
            LOGGER.info("Appointment confirmed for patient: " + patient.getFullName());
            return true;
            
        } catch (Exception e) {
            LOGGER.warning("Error confirming appointment: " + e.getMessage());
            return false;
        }
    }
    
    @Override
    public boolean updateContactInformation(String phoneNumber, String email, String address, String emergencyContact) {
        try {
            boolean updated = false;
            
            if (phoneNumber != null && !phoneNumber.trim().isEmpty()) {
                patient.setPhoneNumber(phoneNumber.trim());
                updated = true;
            }
            
            if (email != null && !email.trim().isEmpty()) {
                patient.setEmail(email.trim());
                updated = true;
            }
            
            if (address != null && !address.trim().isEmpty()) {
                patient.setAddress(address.trim());
                updated = true;
            }
            
            if (emergencyContact != null && !emergencyContact.trim().isEmpty()) {
                patient.setEmergencyContact(emergencyContact.trim());
                updated = true;
            }
            
            if (updated) {
                currentSession.setContactInfoUpdated(true);
                currentSession.addSessionNote("Contact information updated during check-in");
                LOGGER.info("Contact information updated for patient: " + patient.getFullName());
            }
            
            return true; // Always return true as this step is optional
            
        } catch (Exception e) {
            LOGGER.warning("Error updating contact information: " + e.getMessage());
            return false;
        }
    }
    
    @Override
    public boolean processPayment(double copayAmount, String paymentMethod, String referenceNumber) {
        try {
            if (copayAmount < 0) {
                LOGGER.warning("Invalid copay amount: " + copayAmount);
                return false;
            }
            
            if (copayAmount == 0.0) {
                currentSession.setPaymentProcessed(true);
                currentSession.addSessionNote("No copay required");
                LOGGER.info("No copay required for patient: " + patient.getFullName());
                return true;
            }
            
            if (paymentMethod == null || paymentMethod.trim().isEmpty()) {
                LOGGER.warning("Payment method not specified for copay amount: " + copayAmount);
                return false;
            }
            
            // Simulate payment processing
            boolean paymentSuccess = !paymentMethod.equalsIgnoreCase("DECLINED");
            
            if (paymentSuccess) {
                currentSession.setPaymentProcessed(true);
                currentSession.addSessionNote(String.format("Payment processed: $%.2f via %s (Ref: %s)", 
                    copayAmount, paymentMethod, referenceNumber != null ? referenceNumber : "N/A"));
                LOGGER.info(String.format("Payment processed for patient %s: $%.2f", 
                    patient.getFullName(), copayAmount));
            } else {
                currentSession.addSessionNote("Payment declined: " + paymentMethod);
                LOGGER.warning("Payment declined for patient: " + patient.getFullName());
            }
            
            return paymentSuccess;
            
        } catch (Exception e) {
            LOGGER.warning("Error processing payment: " + e.getMessage());
            return false;
        }
    }
    
    @Override
    public boolean conductHealthScreening(double temperature, String symptoms, boolean recentTravel, boolean covidExposure) {
        try {
            boolean screeningPassed = true;
            StringBuilder screeningNotes = new StringBuilder("Health screening completed: ");
            
            // Check temperature
            if (temperature > 100.4) {
                screeningPassed = false;
                screeningNotes.append("FEVER DETECTED (").append(temperature).append("°F) ");
            } else {
                screeningNotes.append("Temperature normal (").append(temperature).append("°F) ");
            }
            
            // Check symptoms
            if (symptoms != null && !symptoms.trim().isEmpty()) {
                screeningNotes.append("Symptoms reported: ").append(symptoms.trim()).append(" ");
                if (symptoms.toLowerCase().contains("fever") || 
                    symptoms.toLowerCase().contains("cough") || 
                    symptoms.toLowerCase().contains("difficulty breathing")) {
                    screeningPassed = false;
                }
            } else {
                screeningNotes.append("No symptoms reported ");
            }
            
            // Check travel and exposure
            if (recentTravel) {
                screeningNotes.append("Recent travel: YES ");
            }
            if (covidExposure) {
                screeningNotes.append("COVID exposure: YES ");
                screeningPassed = false;
            }
            
            currentSession.setHealthScreeningComplete(true);
            currentSession.addSessionNote(screeningNotes.toString());
            
            if (!screeningPassed) {
                currentSession.addSessionNote("ALERT: Patient requires additional screening or isolation");
                LOGGER.warning("Health screening alert for patient: " + patient.getFullName());
            } else {
                LOGGER.info("Health screening passed for patient: " + patient.getFullName());
            }
            
            return screeningPassed;
            
        } catch (Exception e) {
            LOGGER.warning("Error conducting health screening: " + e.getMessage());
            return false;
        }
    }
    
    @Override
    public boolean completeCheckIn(LocalDateTime checkInTime, String waitingAreaAssignment, String specialInstructions) {
        try {
            if (!currentSession.isReadyForCompletion()) {
                LOGGER.warning("Cannot complete check-in: Required steps not completed");
                return false;
            }
            
            currentSession.setCheckInCompleteTime(checkInTime != null ? checkInTime : LocalDateTime.now());
            currentSession.setWaitingAreaAssignment(waitingAreaAssignment);
            currentSession.setCheckInComplete(true);
            
            String completionNote = "Check-in completed at " + 
                currentSession.getCheckInCompleteTime().format(DateTimeFormatter.ofPattern("HH:mm"));
            
            if (waitingAreaAssignment != null && !waitingAreaAssignment.trim().isEmpty()) {
                completionNote += " - Assigned to: " + waitingAreaAssignment.trim();
            }
            
            if (specialInstructions != null && !specialInstructions.trim().isEmpty()) {
                completionNote += " - Special instructions: " + specialInstructions.trim();
            }
            
            currentSession.addSessionNote(completionNote);
            
            // Add check-in report to patient's appointment reports
            patient.addAppointmentReport("Patient checked in successfully on " + 
                LocalDate.now().format(DateTimeFormatter.ofPattern("MM/dd/yyyy")));
            
            LOGGER.info("Check-in completed for patient: " + patient.getFullName());
            return true;
            
        } catch (Exception e) {
            LOGGER.warning("Error completing check-in: " + e.getMessage());
            return false;
        }
    }
    
    // Additional utility methods
    
    public NewPatient getPatient() {
        return patient;
    }
    
    public CheckInSession getCurrentSession() {
        return currentSession;
    }
    
    public String getCheckInSummary() {
        StringBuilder summary = new StringBuilder();
        summary.append("=== CHECK-IN SUMMARY ===\n");
        summary.append("Patient: ").append(patient.getFullName()).append("\n");
        summary.append("Started: ").append(currentSession.getCheckInStartTime().format(DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm"))).append("\n");
        
        if (currentSession.isCheckInComplete()) {
            summary.append("Completed: ").append(currentSession.getCheckInCompleteTime().format(DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm"))).append("\n");
            summary.append("Waiting Area: ").append(currentSession.getWaitingAreaAssignment() != null ? currentSession.getWaitingAreaAssignment() : "Not assigned").append("\n");
        }
        
        summary.append("\nStatus:\n");
        summary.append("✓ Identification: ").append(currentSession.isIdentificationVerified() ? "VERIFIED" : "PENDING").append("\n");
        summary.append("✓ Insurance: ").append(currentSession.isInsuranceVerified() ? "VERIFIED" : "PENDING").append("\n");
        summary.append("✓ Appointment: ").append(currentSession.isAppointmentConfirmed() ? "CONFIRMED" : "PENDING").append("\n");
        summary.append("✓ Contact Info: ").append(currentSession.isContactInfoUpdated() ? "UPDATED" : "PENDING").append("\n");
        summary.append("✓ Payment: ").append(currentSession.isPaymentProcessed() ? "PROCESSED" : "PENDING").append("\n");
        summary.append("✓ Health Screening: ").append(currentSession.isHealthScreeningComplete() ? "COMPLETE" : "PENDING").append("\n");
        summary.append("✓ Check-in: ").append(currentSession.isCheckInComplete() ? "COMPLETE" : "PENDING").append("\n");
        
        if (!currentSession.getSessionNotes().isEmpty()) {
            summary.append("\nSession Notes:\n");
            for (String note : currentSession.getSessionNotes()) {
                summary.append("- ").append(note).append("\n");
            }
        }
        
        return summary.toString();
    }
    
    public void resetCheckInSession() {
        this.currentSession = new CheckInSession();
        LOGGER.info("Check-in session reset for patient: " + patient.getFullName());
    }
}