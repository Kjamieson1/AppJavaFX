import java.util.List;
import java.util.logging.Logger;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

// Class to display a list of patients and allow selection to view details
public class PatientListViewGUI 
{

    private static final Logger LOGGER = Logger.getLogger(PatientListViewGUI.class.getName());

    private Stage stage;

    public PatientListViewGUI() 
    {
        initialize();
    }

    private void initialize() 
    {
        stage = new Stage();
        stage.setTitle("Saved Patients");

        SplitPane split = new SplitPane();

        // Apply app background
        VBox root = new VBox();
        root.setStyle("-fx-background: linear-gradient(to bottom, #e8f5e8, #f0f9f0);");

        // Left: list of patients
        ListView<PatientDataObject> listView = new ListView<>();
            try {
                List<PatientDataObject> all = PatientDataStorage.getInstance().getAllPatients();
                listView.setItems(FXCollections.observableArrayList(all));
            } catch (Exception e) {
                listView.setItems(FXCollections.observableArrayList());
            }

            listView.setCellFactory(lv -> new ListCell<>() 
            {
                @Override
                protected void updateItem(PatientDataObject item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else 
                    {
                        setText(item.getFullName().trim() + " (" + item.getPatientId() + ")");
                    }
                }
            });

    // Right: details and actions
    VBox details = new VBox(14);
    details.setPadding(new Insets(22));
    details.setAlignment(Pos.TOP_LEFT);

    final String primaryTextColor = "#1b5e20";

    Label title = new Label("Patient Details");
    title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 18));
    title.setStyle("-fx-text-fill: " + primaryTextColor + ";");

    Label nameLabel = new Label("Name: ");
    nameLabel.setStyle("-fx-text-fill: " + primaryTextColor + "; -fx-font-size: 14px;");
    Label idLabel = new Label("ID: ");
    idLabel.setStyle("-fx-text-fill: " + primaryTextColor + ";");
    Label phoneLabel = new Label("Phone: ");
    phoneLabel.setStyle("-fx-text-fill: " + primaryTextColor + ";");
    Label emailLabel = new Label("Email: ");
    emailLabel.setStyle("-fx-text-fill: " + primaryTextColor + ";");
    Label emergencyLabel = new Label("Emergency Contact: ");
    emergencyLabel.setStyle("-fx-text-fill: " + primaryTextColor + ";");
    Label emergencyPhoneLabel = new Label("Emergency Phone: ");
    emergencyPhoneLabel.setStyle("-fx-text-fill: " + primaryTextColor + ";");
    Label insuranceLabel = new Label("Insurance Provider: ");
    insuranceLabel.setStyle("-fx-text-fill: " + primaryTextColor + ";");
    Label policyLabel = new Label("Policy Number: ");
    policyLabel.setStyle("-fx-text-fill: " + primaryTextColor + ";");

    Button openBtn = new Button("Open medical view");
    openBtn.setStyle("-fx-background-color: #66bb6a; -fx-text-fill: white; -fx-background-radius: 10; -fx-padding: 8 14 8 14;");
    openBtn.setDisable(true);

    details.getChildren().addAll(title, nameLabel, idLabel, phoneLabel, emailLabel, emergencyLabel, emergencyPhoneLabel, insuranceLabel, policyLabel, openBtn);

        // Selection handling
        listView.getSelectionModel().selectedItemProperty().addListener((obs, oldV, newV) -> {
            if (newV == null) 
            {
                nameLabel.setText("Name: ");
                idLabel.setText("ID: ");
                phoneLabel.setText("Phone: ");
                emailLabel.setText("Email: ");
                emergencyLabel.setText("Emergency Contact: ");
                emergencyPhoneLabel.setText("Emergency Phone: ");
                insuranceLabel.setText("Insurance Provider: ");
                policyLabel.setText("Policy Number: ");
                openBtn.setDisable(true);
            } else 
            {
                nameLabel.setText("Name: " + newV.getFullName().trim());
                idLabel.setText("ID: " + newV.getPatientId());
                phoneLabel.setText("Phone: " + (newV.getPhoneNumber() == null ? "" : newV.getPhoneNumber()));
                emailLabel.setText("Email: " + (newV.getEmail() == null ? "" : newV.getEmail()));
                emergencyLabel.setText("Emergency Contact: " + (newV.getEmergencyContact() == null ? "" : newV.getEmergencyContact()));
                emergencyPhoneLabel.setText("Emergency Phone: " + (newV.getEmergencyPhone() == null ? "" : newV.getEmergencyPhone()));
                insuranceLabel.setText("Insurance Provider: " + (newV.getInsuranceProvider() == null ? "" : newV.getInsuranceProvider()));
                policyLabel.setText("Policy Number: " + (newV.getInsurancePolicyNumber() == null ? "" : newV.getInsurancePolicyNumber()));
                openBtn.setDisable(false);
            }
        });

        // Double click to open
        listView.setOnMouseClicked(evt -> {
            if (evt.getClickCount() == 2) 
            {
                PatientDataObject sel = listView.getSelectionModel().getSelectedItem();
                if (sel != null) openFullView(sel);
            }
        });

        openBtn.setOnAction(e -> {
            PatientDataObject sel = listView.getSelectionModel().getSelectedItem();
            if (sel != null) openFullView(sel);
        });

    split.getItems().addAll(listView, details);
    split.setDividerPositions(0.45);

    root.getChildren().add(split);

    Scene scene = new Scene(root, 800, 500);
    stage.setScene(scene);
    }

    private void openFullView(PatientDataObject pdo) 
    {
        try {
            PatientFormViewGUI view = new PatientFormViewGUI(pdo);
            view.show();
        } catch (Exception e) {
            LOGGER.warning("Failed to open patient view: " + e.getMessage());
        }
    }

    public void show() { stage.show(); }
}
