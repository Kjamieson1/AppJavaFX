import java.time.LocalDateTime;

/**
 * Interface defining the contract for patient check-in workflow.
 * This interface provides methods for handling the complete patient check-in process
 * including validation, verification, and appointment management.
 */
public interface PatientCheckInInterface {
    
    /**
     * Validates basic patient identification information
     * @param firstName Patient's first name
     * @param lastName Patient's last name
     * @param dateOfBirth Patient's date of birth as string
     * @return true if patient identification is valid, false otherwise
     */
    boolean validatePatientIdentification(String firstName, String lastName, String dateOfBirth);
    
    /**
     * Verifies patient insurance information and coverage
     * @param insuranceProvider Name of the insurance provider
     * @param policyNumber Insurance policy number
     * @param groupNumber Insurance group number (optional)
     * @return true if insurance is verified and active, false otherwise
     */
    boolean verifyInsuranceCoverage(String insuranceProvider, String policyNumber, String groupNumber);
    
    /**
     * Confirms the patient's scheduled appointment for today
     * @param appointmentDateTime The scheduled appointment date and time
     * @param doctorName Name of the doctor for the appointment
     * @param appointmentType Type of appointment (e.g., consultation, follow-up)
     * @return true if appointment is confirmed, false if no matching appointment found
     */
    boolean confirmScheduledAppointment(LocalDateTime appointmentDateTime, String doctorName, String appointmentType);
    
    /**
     * Updates patient contact information during check-in
     * @param phoneNumber Current phone number
     * @param email Current email address
     * @param address Current address
     * @param emergencyContact Emergency contact information
     * @return true if contact information is successfully updated, false otherwise
     */
    boolean updateContactInformation(String phoneNumber, String email, String address, String emergencyContact);
    
    /**
     * Processes co-payment collection for the visit
     * @param copayAmount Amount of co-payment required
     * @param paymentMethod Method of payment (cash, card, etc.)
     * @param referenceNumber Payment reference number
     * @return true if payment is successfully processed, false otherwise
     */
    boolean processPayment(double copayAmount, String paymentMethod, String referenceNumber);
    
    /**
     * Collects and updates patient health screening information
     * @param temperature Patient's current temperature
     * @param symptoms List of current symptoms reported by patient
     * @param recentTravel Whether patient has traveled recently
     * @param covidExposure Whether patient has had recent COVID exposure
     * @return true if health screening is complete and patient is cleared, false if further screening needed
     */
    boolean conductHealthScreening(double temperature, String symptoms, boolean recentTravel, boolean covidExposure);
    
    /**
     * Completes the check-in process and notifies clinical staff
     * @param checkInTime The time when check-in was completed
     * @param waitingAreaAssignment Assigned waiting area or room number
     * @param specialInstructions Any special instructions for the clinical staff
     * @return true if check-in is successfully completed, false if there are issues
     */
    boolean completeCheckIn(LocalDateTime checkInTime, String waitingAreaAssignment, String specialInstructions);
}