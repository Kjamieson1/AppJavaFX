# 🏥 Patient Management System - JavaFX Application

A modern, professional JavaFX-based patient management system designed for healthcare facilities. Features a clean, green-themed user interface with comprehensive patient check-in workflows, data management, and appointment scheduling capabilities.

## 🚀 Quick Start & Dependencies

### Prerequisites
- **Java 17 or higher** (JDK 17+ required)
- **Maven 3.6+** for dependency management and building
- **JavaFX 17+** (automatically handled by Maven)

### Installation & Setup

1. **Clone the repository:**
   ```bash
   git clone https://github.com/DrOwen101/AppJavaFX.git
   cd AppJavaFX
   ```

2. **Build the project:**
   ```bash
   mvn clean compile
   ```

3. **Run the application:**
   ```bash
   mvn javafx:run
   ```

### System Requirements
- **Operating System:** Windows 10+, macOS 10.14+, or Linux
- **Memory:** Minimum 512MB RAM available for the application
- **Display:** 1024x768 minimum resolution (1280x800+ recommended)

---

## 📋 Application Overview

The Patient Management System is a comprehensive healthcare application designed to streamline patient registration, check-in processes, and data management. Built with JavaFX, it provides a modern, intuitive interface that healthcare professionals can use efficiently in clinical settings.

### Key Features
- **Modern Green UI/UX Design** - Professional, calming interface optimized for healthcare environments
- **Patient Registration & Management** - Complete patient demographic and medical history tracking
- **7-Step Check-In Workflow** - Guided patient check-in process with validation
- **Data Persistence** - JSON-like data storage for patient records
- **Appointment Management** - Schedule and track patient appointments
- **Photo Management** - Patient photo upload and storage capabilities
- **Insurance Processing** - Insurance verification and policy management

---

## 👥 User Stories

### Primary User: **Nurse/Medical Staff**

#### Patient Registration & Management
- **As a nurse**, I need to **register new patients with their demographic information** so that I can **maintain accurate patient records and ensure proper identification during visits**.

- **As a nurse**, I need to **update existing patient information including contact details and insurance** so that I can **keep patient records current and ensure proper billing and communication**.

- **As a nurse**, I need to **upload and manage patient photos** so that I can **quickly identify patients and maintain visual records for security purposes**.

- **As a nurse**, I need to **record and update patient medical history including medications, diagnoses, and allergies** so that I can **provide safe, informed care and avoid adverse reactions**.

#### Check-In Workflow
- **As a nurse**, I need to **initiate a patient check-in process** so that I can **efficiently process patients and prepare them for their appointments**.

- **As a nurse**, I need to **validate patient identification during check-in** so that I can **ensure the correct patient is being treated and maintain HIPAA compliance**.

- **As a nurse**, I need to **verify insurance coverage during check-in** so that I can **confirm benefits and process copayments accurately**.

- **As a nurse**, I need to **confirm appointment details during check-in** so that I can **ensure patients are seen by the correct provider at the right time**.

- **As a nurse**, I need to **update patient contact information during check-in** so that I can **maintain current emergency contacts and communication preferences**.

- **As a nurse**, I need to **process copayments and collect payment information** so that I can **handle financial transactions efficiently and reduce billing delays**.

- **As a nurse**, I need to **conduct basic health screenings during check-in** so that I can **identify potential health concerns and prepare the clinical team**.

- **As a nurse**, I need to **assign patients to appropriate waiting areas** so that I can **manage patient flow and maintain organized clinical operations**.

#### Data Management & Persistence
- **As a nurse**, I need to **save patient data securely during the workflow** so that I can **prevent data loss and maintain comprehensive patient records**.

- **As a nurse**, I need to **access previously saved patient information** so that I can **provide continuity of care and review patient history quickly**.

- **As a nurse**, I need to **export patient data summaries** so that I can **share information with providers and maintain backup records**.

#### System Navigation & Workflow
- **As a nurse**, I need to **navigate between different system functions easily** so that I can **work efficiently during busy clinic hours**.

- **As a nurse**, I need to **return to the main menu from any workflow** so that I can **switch between patients or access different system functions quickly**.

- **As a nurse**, I need to **see clear visual feedback on form completion and validation** so that I can **ensure all required information is captured before proceeding**.

#### Error Handling & Recovery
- **As a nurse**, I need to **receive clear error messages when data is incomplete** so that I can **correct issues quickly and maintain workflow efficiency**.

- **As a nurse**, I need to **recover from system errors without losing patient data** so that I can **continue working without having to re-enter information**.

### Secondary User: **Medical Provider**

- **As a medical provider**, I need to **review patient check-in information before appointments** so that I can **prepare for patient encounters and provide informed care**.

- **As a medical provider**, I need to **access complete patient medical histories** so that I can **make informed clinical decisions and avoid contraindicated treatments**.

### Tertiary User: **System Administrator**

- **As a system administrator**, I need to **monitor system performance and data integrity** so that I can **ensure reliable operation and prevent data loss**.

- **As a system administrator**, I need to **manage system configurations and user access** so that I can **maintain security and customize the system for different healthcare environments**.

---

## 🏗️ Technical Architecture

### Application Structure
```
src/main/java/
├── Main.java                    # Application entry point with navigation
├── PatientForm.java             # Abstract base class for patient data
├── NewPatient.java              # Concrete patient implementation with appointments
├── PatientFormGUI.java          # Main patient registration form
├── PatientCheckInGUI.java       # 7-step check-in workflow interface
├── CheckInPopup.java            # Check-in initiation modal
├── PatientCheckInInterface.java # Check-in workflow interface definition
├── CheckInWorkflow.java         # Check-in business logic implementation
├── PatientDataObject.java       # JSON-like data container
├── PatientDataStorage.java      # Data persistence layer
└── AppointmentManagerGUI.java   # Appointment scheduling interface
```

### Design Patterns Used
- **Template Method Pattern** - `PatientForm` abstract class with concrete implementations
- **Interface Segregation** - `PatientCheckInInterface` for workflow definition
- **Singleton Pattern** - `PatientDataStorage` for centralized data management
- **Observer Pattern** - GUI event handling and state management
- **Command Pattern** - Button actions and workflow step navigation

### Data Flow
1. **Patient Registration** → PatientFormGUI → PatientForm/NewPatient
2. **Check-In Initiation** → CheckInPopup → PatientCheckInGUI
3. **Workflow Execution** → CheckInWorkflow → PatientDataObject
4. **Data Persistence** → PatientDataStorage → JSON-like storage
5. **Navigation** → Main → Various GUI components

---

## 🎨 UI/UX Features

### Modern Green Theme
- **Primary Colors:** Professional green palette (`#2e7d32`, `#4caf50`, `#66bb6a`)
- **Accent Colors:** Light green backgrounds (`#e8f5e8`, `#f1f8e9`)
- **Supporting Colors:** Orange for save actions, red for cancel/delete

### Design Elements
- **Card-based Layout** - Modern container design with shadows and rounded corners
- **Gradient Backgrounds** - Professional linear gradients throughout the interface
- **Hover Effects** - Interactive button animations with scale and shadow effects
- **Typography** - Segoe UI font family for modern, readable text
- **Icons** - Emoji-based icons for intuitive navigation and visual appeal

### Responsive Design
- **Scalable Interface** - Adapts to different screen sizes and resolutions
- **Consistent Spacing** - Uniform padding and margins throughout the application
- **Accessibility** - High contrast ratios and readable font sizes

---

## 🔧 Development & Customization

### Building from Source
```bash
# Clean and compile
mvn clean compile

# Run tests (when available)
mvn test

# Package application
mvn package

# Run with Maven
mvn javafx:run
```

### Configuration
- **Java Version:** Specified in `pom.xml` (currently Java 17)
- **JavaFX Version:** Managed via Maven dependencies (currently 17.0.10)
- **Logging:** Java Util Logging for system monitoring and debugging

### Extending the Application
- **Adding New Workflows:** Implement new GUI classes following the existing pattern
- **Custom Data Fields:** Extend `PatientForm` or `PatientDataObject` classes
- **Styling Changes:** Modify CSS-like styling strings in GUI classes
- **Data Storage:** Extend `PatientDataStorage` for database integration

---

## 📝 License & Support

This application is developed for educational and healthcare management purposes. For support, feature requests, or bug reports, please contact the development team or create an issue in the project repository.

### Version Information
- **Current Version:** 1.0-SNAPSHOT
- **JavaFX Version:** 17.0.10
- **Java Compatibility:** 17+
- **Last Updated:** September 2025

---

## 🤝 Contributing

We welcome contributions to improve the Patient Management System. Please follow the existing code style and patterns when submitting pull requests. Ensure all new features include appropriate user stories and maintain the professional green theme design language.