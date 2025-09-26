import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Concrete implementation of PatientForm that adds appointment tracking
 * and new appointment scheduling functionality.
 */
public class NewPatient extends PatientForm {
    
    // Appointment tracking
    private List<Appointment> appointments;
    private List<String> appointmentReports;
    
    /**
     * Inner class to represent an appointment
     */
    public static class Appointment {
        private LocalDateTime appointmentDateTime;
        private String doctorName;
        private String appointmentType;
        private String notes;
        private AppointmentStatus status;
        
        public enum AppointmentStatus {
            SCHEDULED, COMPLETED, CANCELLED, NO_SHOW
        }
        
        public Appointment(LocalDateTime appointmentDateTime, String doctorName, String appointmentType) {
            this.appointmentDateTime = appointmentDateTime;
            this.doctorName = doctorName;
            this.appointmentType = appointmentType;
            this.status = AppointmentStatus.SCHEDULED;
            this.notes = "";
        }
        
        // Getters and setters
        public LocalDateTime getAppointmentDateTime() { return appointmentDateTime; }
        public void setAppointmentDateTime(LocalDateTime appointmentDateTime) { this.appointmentDateTime = appointmentDateTime; }
        
        public String getDoctorName() { return doctorName; }
        public void setDoctorName(String doctorName) { this.doctorName = doctorName; }
        
        public String getAppointmentType() { return appointmentType; }
        public void setAppointmentType(String appointmentType) { this.appointmentType = appointmentType; }
        
        public String getNotes() { return notes; }
        public void setNotes(String notes) { this.notes = notes; }
        
        public AppointmentStatus getStatus() { return status; }
        public void setStatus(AppointmentStatus status) { this.status = status; }
        
        @Override
        public String toString() {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm");
            return String.format("%s - %s with Dr. %s (%s)", 
                appointmentDateTime.format(formatter), appointmentType, doctorName, status);
        }
    }
    
    /**
     * Default constructor
     */
    public NewPatient() {
        super();
        this.appointments = new ArrayList<>();
        this.appointmentReports = new ArrayList<>();
    }
    
    /**
     * Constructor with basic patient information
     */
    public NewPatient(String firstName, String lastName, LocalDate dateOfBirth, String gender) {
        super(firstName, lastName, dateOfBirth, gender);
        this.appointments = new ArrayList<>();
        this.appointmentReports = new ArrayList<>();
    }
    
    /**
     * Schedule a new appointment
     */
    public void scheduleAppointment(LocalDateTime appointmentDateTime, String doctorName, String appointmentType) {
        if (appointmentDateTime == null || doctorName == null || appointmentType == null) {
            throw new IllegalArgumentException("Appointment details cannot be null");
        }
        
        Appointment newAppointment = new Appointment(appointmentDateTime, doctorName, appointmentType);
        appointments.add(newAppointment);
        
        // Add to appointment reports
        String report = String.format("Appointment scheduled: %s on %s with Dr. %s", 
            appointmentType, 
            appointmentDateTime.format(DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm")), 
            doctorName);
        appointmentReports.add(report);
    }
    
    /**
     * Cancel an appointment
     */
    public boolean cancelAppointment(Appointment appointment) {
        if (appointment != null && appointments.contains(appointment)) {
            appointment.setStatus(Appointment.AppointmentStatus.CANCELLED);
            
            String report = String.format("Appointment cancelled: %s on %s with Dr. %s", 
                appointment.getAppointmentType(), 
                appointment.getAppointmentDateTime().format(DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm")), 
                appointment.getDoctorName());
            appointmentReports.add(report);
            
            return true;
        }
        return false;
    }
    
    /**
     * Mark an appointment as completed
     */
    public boolean completeAppointment(Appointment appointment, String notes) {
        if (appointment != null && appointments.contains(appointment)) {
            appointment.setStatus(Appointment.AppointmentStatus.COMPLETED);
            appointment.setNotes(notes != null ? notes : "");
            
            String report = String.format("Appointment completed: %s on %s with Dr. %s. Notes: %s", 
                appointment.getAppointmentType(), 
                appointment.getAppointmentDateTime().format(DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm")), 
                appointment.getDoctorName(),
                notes != null ? notes : "No notes");
            appointmentReports.add(report);
            
            return true;
        }
        return false;
    }
    
    /**
     * Get all appointments
     */
    public List<Appointment> getAppointments() {
        return new ArrayList<>(appointments);
    }
    
    /**
     * Get upcoming appointments (scheduled status and future date)
     */
    public List<Appointment> getUpcomingAppointments() {
        List<Appointment> upcoming = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
        
        for (Appointment appointment : appointments) {
            if (appointment.getStatus() == Appointment.AppointmentStatus.SCHEDULED 
                && appointment.getAppointmentDateTime().isAfter(now)) {
                upcoming.add(appointment);
            }
        }
        
        return upcoming;
    }
    
    /**
     * Get appointment history (completed, cancelled, no-show)
     */
    public List<Appointment> getAppointmentHistory() {
        List<Appointment> history = new ArrayList<>();
        
        for (Appointment appointment : appointments) {
            if (appointment.getStatus() != Appointment.AppointmentStatus.SCHEDULED) {
                history.add(appointment);
            }
        }
        
        return history;
    }
    
    /**
     * Get appointment tracking reports
     */
    public List<String> getAppointmentReports() {
        return new ArrayList<>(appointmentReports);
    }
    
    /**
     * Add a custom appointment report entry
     */
    public void addAppointmentReport(String report) {
        if (report != null && !report.trim().isEmpty()) {
            appointmentReports.add(report.trim());
        }
    }
    
    /**
     * Get next upcoming appointment
     */
    public Appointment getNextAppointment() {
        LocalDateTime now = LocalDateTime.now();
        Appointment nextAppointment = null;
        
        for (Appointment appointment : appointments) {
            if (appointment.getStatus() == Appointment.AppointmentStatus.SCHEDULED 
                && appointment.getAppointmentDateTime().isAfter(now)) {
                if (nextAppointment == null || 
                    appointment.getAppointmentDateTime().isBefore(nextAppointment.getAppointmentDateTime())) {
                    nextAppointment = appointment;
                }
            }
        }
        
        return nextAppointment;
    }
    
    /**
     * Implementation of abstract method from PatientForm
     */
    @Override
    public String getPatientSummary() {
        StringBuilder summary = new StringBuilder();
        summary.append("=== PATIENT SUMMARY ===\n");
        summary.append("Name: ").append(getFullName()).append("\n");
        summary.append("Age: ").append(getAge()).append("\n");
        summary.append("Gender: ").append(gender != null ? gender : "Not specified").append("\n");
        summary.append("Phone: ").append(phoneNumber != null ? phoneNumber : "Not provided").append("\n");
        summary.append("Email: ").append(email != null ? email : "Not provided").append("\n");
        
        if (!medications.isEmpty()) {
            summary.append("\nMedications: ").append(String.join(", ", medications)).append("\n");
        }
        
        if (!diagnoses.isEmpty()) {
            summary.append("Diagnoses: ").append(String.join(", ", diagnoses)).append("\n");
        }
        
        if (!allergies.isEmpty()) {
            summary.append("Allergies: ").append(String.join(", ", allergies)).append("\n");
        }
        
        summary.append("\n=== APPOINTMENT INFO ===\n");
        summary.append("Total appointments: ").append(appointments.size()).append("\n");
        
        Appointment next = getNextAppointment();
        if (next != null) {
            summary.append("Next appointment: ").append(next.toString()).append("\n");
        } else {
            summary.append("No upcoming appointments\n");
        }
        
        return summary.toString();
    }
    
    /**
     * Implementation of abstract method from PatientForm
     */
    @Override
    public boolean validatePatientData() {
        // Basic validation - require first name, last name, and date of birth
        return firstName != null && !firstName.trim().isEmpty() &&
               lastName != null && !lastName.trim().isEmpty() &&
               dateOfBirth != null;
    }
    
    /**
     * Get total number of appointments by status
     */
    public int getAppointmentCountByStatus(Appointment.AppointmentStatus status) {
        int count = 0;
        for (Appointment appointment : appointments) {
            if (appointment.getStatus() == status) {
                count++;
            }
        }
        return count;
    }
    
    /**
     * Clear all appointments (useful for testing or data reset)
     */
    public void clearAppointments() {
        appointments.clear();
        appointmentReports.clear();
    }
    
    @Override
    public String toString() {
        return super.toString() + " [Appointments: " + appointments.size() + "]";
    }
}