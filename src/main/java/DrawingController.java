import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class DrawingController 
{
    private final ObservableList<DrawableShape> shapes = FXCollections.observableArrayList();
    private Canvas canvas;
    private GraphicsContext gc;

    private ToggleGroup shapeToggleGroup;
    private Label statusLabel;
    private Label countLabel;
    private Label areaLabel;
    private ListView<DrawableShape> shapeListView;

    public BorderPane buildUI() 
    {
        BorderPane root = new BorderPane();

        // Set background color for my mole eyes
        root.setStyle("-fx-background-color: Darkgray;");

        // Main Canvas
        canvas = new Canvas(700, 500);
        gc = canvas.getGraphicsContext2D();
        root.setCenter(canvas);

        // Controls for shapes
        HBox controls = new HBox(10);
        controls.setPadding(new Insets(10));

        RadioButton squareButton = new RadioButton("Square");
        RadioButton circleButton = new RadioButton("Circle");
        shapeToggleGroup = new ToggleGroup();
        squareButton.setToggleGroup(shapeToggleGroup);
        circleButton.setToggleGroup(shapeToggleGroup);
        squareButton.setSelected(true);

        Button clearButton = new Button("Clear");

        controls.getChildren().addAll(squareButton, circleButton, clearButton);
        root.setTop(controls);

        // Right info Panel
        VBox infoPanel = new VBox(10);
        infoPanel.setPadding(new Insets(10));

        countLabel = new Label();
        areaLabel = new Label();
        statusLabel = new Label("Current Shape: Square");

        shapeListView = new ListView<>(shapes);

        countLabel.textProperty().bind(Bindings.size(shapes).asString("Total Shapes: %d"));

        areaLabel.textProperty().bind(Bindings.createStringBinding(() -> {
            double total = shapes.stream().mapToDouble(DrawableShape::getArea).sum();
            return String.format("Total Area: %.2f", total);
        }, shapes));

        infoPanel.getChildren().addAll(countLabel, areaLabel, statusLabel, new Label("Shapes:"), shapeListView);
        root.setRight(infoPanel);

        // Event Handlers
        canvas.setOnMouseClicked(e -> {
            double x = e.getX();
            double y = e.getY();

            DrawableShape shape;
            if (shapeToggleGroup.getSelectedToggle() == squareButton) {
                shape = new Square(x, y, 60, Color.BLUE);
            } else {
                shape = new Circle(x, y, 30, Color.RED);
            }

            shapes.add(shape);
            redraw();
        });

        clearButton.setOnAction(e -> {
            shapes.clear();
            redraw();
        });

        return root;
    }

    private void redraw() 
    {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        for (DrawableShape shape : shapes) {
            shape.draw(gc);
        }
    }
}
