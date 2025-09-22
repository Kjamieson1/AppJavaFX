import javafx.scene.canvas.GraphicsContext;

public interface DrawableShape 
{
    void draw(GraphicsContext gc);
    double getArea();
    String toString();
}