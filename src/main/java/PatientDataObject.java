import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A JSON-like object that contains all patient data collected during the check-in process.
 * This object serves as a complete snapshot of patient information and can be easily
 * serialized to JSON or stored in a database.
 */
public class PatientDataObject {
    
    // Unique identifier for this patient record
    private String patientId;
    private LocalDateTime savedTimestamp;
    
    // Patient identification data
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String gender;
    private int age;
    
    // Contact information
    private String phoneNumber;
    private String email;
    private String address;
    private String emergencyContact;
    private String emergencyPhone;
    
    // Insurance information
    private String insuranceProvider;
    private String insurancePolicyNumber;
    private String insuranceGroupNumber;
    private boolean insuranceVerified;
    
    // Appointment information
    private LocalDateTime appointmentDateTime;
    private String doctorName;
    private String appointmentType;
    private boolean appointmentConfirmed;
    
    // Medical information
    private List<String> medications;
    private List<String> diagnoses;
    private List<String> allergies;
    private String patientPicturePath;
    
    // Payment information
    private double copayAmount;
    private String paymentMethod;
    private String paymentReferenceNumber;
    private boolean paymentProcessed;
    
    // Health screening data
    private double temperature;
    private String currentSymptoms;
    private boolean recentTravel;
    private boolean covidExposure;
    private boolean healthScreeningPassed;
    
    // Check-in completion data
    private LocalDateTime checkInStartTime;
    private LocalDateTime checkInCompleteTime;
    private String waitingAreaAssignment;
    private String specialInstructions;
    private boolean checkInComplete;
    
    // Session tracking
    private List<String> sessionNotes;
    private Map<String, Boolean> stepCompletionStatus;
    
    /**
     * Default constructor
     */
    public PatientDataObject() {
        this.patientId = generatePatientId();
        this.savedTimestamp = LocalDateTime.now();
        this.medications = new ArrayList<>();
        this.diagnoses = new ArrayList<>();
        this.allergies = new ArrayList<>();
        this.sessionNotes = new ArrayList<>();
        this.stepCompletionStatus = new HashMap<>();
        initializeStepCompletionStatus();
    }
    
    /**
     * Constructor from NewPatient and CheckInWorkflow
     */
    public PatientDataObject(NewPatient patient, CheckInWorkflow.CheckInSession session) {
        this();
        if (patient != null) {
            populateFromPatient(patient);
        }
        if (session != null) {
            populateFromSession(session);
        }
    }
    
    /**
     * Generate a unique patient ID
     */
    private String generatePatientId() {
        return "PAT" + System.currentTimeMillis() + "_" + (int)(Math.random() * 1000);
    }
    
    /**
     * Initialize step completion status map
     */
    private void initializeStepCompletionStatus() {
        stepCompletionStatus.put("identification", false);
        stepCompletionStatus.put("insurance", false);
        stepCompletionStatus.put("appointment", false);
        stepCompletionStatus.put("contact", false);
        stepCompletionStatus.put("payment", false);
        stepCompletionStatus.put("healthScreening", false);
        stepCompletionStatus.put("completion", false);
    }
    
    /**
     * Populate data from NewPatient object
     */
    private void populateFromPatient(NewPatient patient) {
        this.firstName = patient.getFirstName();
        this.lastName = patient.getLastName();
        this.dateOfBirth = patient.getDateOfBirth();
        this.gender = patient.getGender();
        this.age = patient.getAge();
        this.phoneNumber = patient.getPhoneNumber();
        this.email = patient.getEmail();
        this.address = patient.getAddress();
        this.emergencyContact = patient.getEmergencyContact();
        this.emergencyPhone = patient.getEmergencyPhone();
        this.insuranceProvider = patient.getInsuranceProvider();
        this.insurancePolicyNumber = patient.getInsurancePolicyNumber();
        this.patientPicturePath = patient.getPatientPicturePath();
        
        // Copy medical information
        this.medications.addAll(patient.getMedications());
        this.diagnoses.addAll(patient.getDiagnoses());
        this.allergies.addAll(patient.getAllergies());
    }
    
    /**
     * Populate data from CheckInSession
     */
    private void populateFromSession(CheckInWorkflow.CheckInSession session) {
        this.checkInStartTime = session.getCheckInStartTime();
        this.checkInCompleteTime = session.getCheckInCompleteTime();
        this.waitingAreaAssignment = session.getWaitingAreaAssignment();
        this.checkInComplete = session.isCheckInComplete();
        this.sessionNotes.addAll(session.getSessionNotes());
        
        // Update step completion status
        this.stepCompletionStatus.put("identification", session.isIdentificationVerified());
        this.stepCompletionStatus.put("insurance", session.isInsuranceVerified());
        this.stepCompletionStatus.put("appointment", session.isAppointmentConfirmed());
        this.stepCompletionStatus.put("contact", session.isContactInfoUpdated());
        this.stepCompletionStatus.put("payment", session.isPaymentProcessed());
        this.stepCompletionStatus.put("healthScreening", session.isHealthScreeningComplete());
        this.stepCompletionStatus.put("completion", session.isCheckInComplete());
    }
    
    // Getters and Setters
    
    public String getPatientId() { return patientId; }
    public void setPatientId(String patientId) { this.patientId = patientId; }
    
    public LocalDateTime getSavedTimestamp() { return savedTimestamp; }
    public void setSavedTimestamp(LocalDateTime savedTimestamp) { this.savedTimestamp = savedTimestamp; }
    
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    
    public String getFullName() { 
        return (firstName != null ? firstName : "") + " " + (lastName != null ? lastName : "");
    }
    
    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }
    
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    
    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }
    
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    
    public String getEmergencyContact() { return emergencyContact; }
    public void setEmergencyContact(String emergencyContact) { this.emergencyContact = emergencyContact; }
    
    public String getEmergencyPhone() { return emergencyPhone; }
    public void setEmergencyPhone(String emergencyPhone) { this.emergencyPhone = emergencyPhone; }
    
    public String getInsuranceProvider() { return insuranceProvider; }
    public void setInsuranceProvider(String insuranceProvider) { this.insuranceProvider = insuranceProvider; }
    
    public String getInsurancePolicyNumber() { return insurancePolicyNumber; }
    public void setInsurancePolicyNumber(String insurancePolicyNumber) { this.insurancePolicyNumber = insurancePolicyNumber; }
    
    public String getInsuranceGroupNumber() { return insuranceGroupNumber; }
    public void setInsuranceGroupNumber(String insuranceGroupNumber) { this.insuranceGroupNumber = insuranceGroupNumber; }
    
    public boolean isInsuranceVerified() { return insuranceVerified; }
    public void setInsuranceVerified(boolean insuranceVerified) { this.insuranceVerified = insuranceVerified; }
    
    public LocalDateTime getAppointmentDateTime() { return appointmentDateTime; }
    public void setAppointmentDateTime(LocalDateTime appointmentDateTime) { this.appointmentDateTime = appointmentDateTime; }
    
    public String getDoctorName() { return doctorName; }
    public void setDoctorName(String doctorName) { this.doctorName = doctorName; }
    
    public String getAppointmentType() { return appointmentType; }
    public void setAppointmentType(String appointmentType) { this.appointmentType = appointmentType; }
    
    public boolean isAppointmentConfirmed() { return appointmentConfirmed; }
    public void setAppointmentConfirmed(boolean appointmentConfirmed) { this.appointmentConfirmed = appointmentConfirmed; }
    
    public List<String> getMedications() { return new ArrayList<>(medications); }
    public void setMedications(List<String> medications) { this.medications = new ArrayList<>(medications); }
    public void addMedication(String medication) { this.medications.add(medication); }
    
    public List<String> getDiagnoses() { return new ArrayList<>(diagnoses); }
    public void setDiagnoses(List<String> diagnoses) { this.diagnoses = new ArrayList<>(diagnoses); }
    public void addDiagnosis(String diagnosis) { this.diagnoses.add(diagnosis); }
    
    public List<String> getAllergies() { return new ArrayList<>(allergies); }
    public void setAllergies(List<String> allergies) { this.allergies = new ArrayList<>(allergies); }
    public void addAllergy(String allergy) { this.allergies.add(allergy); }
    
    public String getPatientPicturePath() { return patientPicturePath; }
    public void setPatientPicturePath(String patientPicturePath) { this.patientPicturePath = patientPicturePath; }
    
    public double getCopayAmount() { return copayAmount; }
    public void setCopayAmount(double copayAmount) { this.copayAmount = copayAmount; }
    
    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    
    public String getPaymentReferenceNumber() { return paymentReferenceNumber; }
    public void setPaymentReferenceNumber(String paymentReferenceNumber) { this.paymentReferenceNumber = paymentReferenceNumber; }
    
    public boolean isPaymentProcessed() { return paymentProcessed; }
    public void setPaymentProcessed(boolean paymentProcessed) { this.paymentProcessed = paymentProcessed; }
    
    public double getTemperature() { return temperature; }
    public void setTemperature(double temperature) { this.temperature = temperature; }
    
    public String getCurrentSymptoms() { return currentSymptoms; }
    public void setCurrentSymptoms(String currentSymptoms) { this.currentSymptoms = currentSymptoms; }
    
    public boolean isRecentTravel() { return recentTravel; }
    public void setRecentTravel(boolean recentTravel) { this.recentTravel = recentTravel; }
    
    public boolean isCovidExposure() { return covidExposure; }
    public void setCovidExposure(boolean covidExposure) { this.covidExposure = covidExposure; }
    
    public boolean isHealthScreeningPassed() { return healthScreeningPassed; }
    public void setHealthScreeningPassed(boolean healthScreeningPassed) { this.healthScreeningPassed = healthScreeningPassed; }
    
    public LocalDateTime getCheckInStartTime() { return checkInStartTime; }
    public void setCheckInStartTime(LocalDateTime checkInStartTime) { this.checkInStartTime = checkInStartTime; }
    
    public LocalDateTime getCheckInCompleteTime() { return checkInCompleteTime; }
    public void setCheckInCompleteTime(LocalDateTime checkInCompleteTime) { this.checkInCompleteTime = checkInCompleteTime; }
    
    public String getWaitingAreaAssignment() { return waitingAreaAssignment; }
    public void setWaitingAreaAssignment(String waitingAreaAssignment) { this.waitingAreaAssignment = waitingAreaAssignment; }
    
    public String getSpecialInstructions() { return specialInstructions; }
    public void setSpecialInstructions(String specialInstructions) { this.specialInstructions = specialInstructions; }
    
    public boolean isCheckInComplete() { return checkInComplete; }
    public void setCheckInComplete(boolean checkInComplete) { this.checkInComplete = checkInComplete; }
    
    public List<String> getSessionNotes() { return new ArrayList<>(sessionNotes); }
    public void setSessionNotes(List<String> sessionNotes) { this.sessionNotes = new ArrayList<>(sessionNotes); }
    public void addSessionNote(String note) { this.sessionNotes.add(note); }
    
    public Map<String, Boolean> getStepCompletionStatus() { return new HashMap<>(stepCompletionStatus); }
    public void setStepCompletionStatus(Map<String, Boolean> stepCompletionStatus) { this.stepCompletionStatus = new HashMap<>(stepCompletionStatus); }
    public void setStepCompleted(String step, boolean completed) { this.stepCompletionStatus.put(step, completed); }
    
    /**
     * Get completion percentage (0-100)
     */
    public int getCompletionPercentage() {
        long completedSteps = stepCompletionStatus.values().stream().mapToLong(b -> b ? 1 : 0).sum();
        return (int) ((completedSteps * 100) / stepCompletionStatus.size());
    }
    
    /**
     * Convert to a JSON-like string representation
     */
    public String toJsonString() {
        StringBuilder json = new StringBuilder();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        
        json.append("{\n");
        json.append("  \"patientId\": \"").append(patientId).append("\",\n");
        json.append("  \"savedTimestamp\": \"").append(savedTimestamp.format(dateTimeFormatter)).append("\",\n");
        json.append("  \"personalInfo\": {\n");
        json.append("    \"firstName\": \"").append(firstName != null ? firstName : "").append("\",\n");
        json.append("    \"lastName\": \"").append(lastName != null ? lastName : "").append("\",\n");
        json.append("    \"dateOfBirth\": \"").append(dateOfBirth != null ? dateOfBirth.format(dateFormatter) : "").append("\",\n");
        json.append("    \"gender\": \"").append(gender != null ? gender : "").append("\",\n");
        json.append("    \"age\": ").append(age).append("\n");
        json.append("  },\n");
        
        json.append("  \"contactInfo\": {\n");
        json.append("    \"phoneNumber\": \"").append(phoneNumber != null ? phoneNumber : "").append("\",\n");
        json.append("    \"email\": \"").append(email != null ? email : "").append("\",\n");
        json.append("    \"address\": \"").append(address != null ? address : "").append("\",\n");
        json.append("    \"emergencyContact\": \"").append(emergencyContact != null ? emergencyContact : "").append("\",\n");
        json.append("    \"emergencyPhone\": \"").append(emergencyPhone != null ? emergencyPhone : "").append("\"\n");
        json.append("  },\n");
        
        json.append("  \"insuranceInfo\": {\n");
        json.append("    \"provider\": \"").append(insuranceProvider != null ? insuranceProvider : "").append("\",\n");
        json.append("    \"policyNumber\": \"").append(insurancePolicyNumber != null ? insurancePolicyNumber : "").append("\",\n");
        json.append("    \"groupNumber\": \"").append(insuranceGroupNumber != null ? insuranceGroupNumber : "").append("\",\n");
        json.append("    \"verified\": ").append(insuranceVerified).append("\n");
        json.append("  },\n");
        
        json.append("  \"appointmentInfo\": {\n");
        json.append("    \"dateTime\": \"").append(appointmentDateTime != null ? appointmentDateTime.format(dateTimeFormatter) : "").append("\",\n");
        json.append("    \"doctorName\": \"").append(doctorName != null ? doctorName : "").append("\",\n");
        json.append("    \"appointmentType\": \"").append(appointmentType != null ? appointmentType : "").append("\",\n");
        json.append("    \"confirmed\": ").append(appointmentConfirmed).append("\n");
        json.append("  },\n");
        
        json.append("  \"medicalInfo\": {\n");
        json.append("    \"medications\": ").append(medications.toString()).append(",\n");
        json.append("    \"diagnoses\": ").append(diagnoses.toString()).append(",\n");
        json.append("    \"allergies\": ").append(allergies.toString()).append(",\n");
        json.append("    \"patientPicture\": \"").append(patientPicturePath != null ? patientPicturePath : "").append("\"\n");
        json.append("  },\n");
        
        json.append("  \"paymentInfo\": {\n");
        json.append("    \"copayAmount\": ").append(copayAmount).append(",\n");
        json.append("    \"paymentMethod\": \"").append(paymentMethod != null ? paymentMethod : "").append("\",\n");
        json.append("    \"referenceNumber\": \"").append(paymentReferenceNumber != null ? paymentReferenceNumber : "").append("\",\n");
        json.append("    \"processed\": ").append(paymentProcessed).append("\n");
        json.append("  },\n");
        
        json.append("  \"healthScreening\": {\n");
        json.append("    \"temperature\": ").append(temperature).append(",\n");
        json.append("    \"symptoms\": \"").append(currentSymptoms != null ? currentSymptoms : "").append("\",\n");
        json.append("    \"recentTravel\": ").append(recentTravel).append(",\n");
        json.append("    \"covidExposure\": ").append(covidExposure).append(",\n");
        json.append("    \"passed\": ").append(healthScreeningPassed).append("\n");
        json.append("  },\n");
        
        json.append("  \"checkInInfo\": {\n");
        json.append("    \"startTime\": \"").append(checkInStartTime != null ? checkInStartTime.format(dateTimeFormatter) : "").append("\",\n");
        json.append("    \"completeTime\": \"").append(checkInCompleteTime != null ? checkInCompleteTime.format(dateTimeFormatter) : "").append("\",\n");
        json.append("    \"waitingArea\": \"").append(waitingAreaAssignment != null ? waitingAreaAssignment : "").append("\",\n");
        json.append("    \"specialInstructions\": \"").append(specialInstructions != null ? specialInstructions : "").append("\",\n");
        json.append("    \"complete\": ").append(checkInComplete).append(",\n");
        json.append("    \"completionPercentage\": ").append(getCompletionPercentage()).append("\n");
        json.append("  },\n");
        
        json.append("  \"sessionNotes\": ").append(sessionNotes.toString()).append(",\n");
        json.append("  \"stepCompletion\": {\n");
        stepCompletionStatus.forEach((step, completed) -> 
            json.append("    \"").append(step).append("\": ").append(completed).append(",\n"));
        if (!stepCompletionStatus.isEmpty()) {
            json.setLength(json.length() - 2); // Remove last comma
            json.append("\n");
        }
        json.append("  }\n");
        json.append("}");
        
        return json.toString();
    }
    
    @Override
    public String toString() {
        return String.format("PatientDataObject{id='%s', name='%s', completion=%d%%}", 
            patientId, getFullName().trim(), getCompletionPercentage());
    }
}