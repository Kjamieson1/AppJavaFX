import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Circle implements DrawableShape 
{
    private double centerX, centerY, radius;
    private Color color;

    public Circle(double centerX, double centerY, double radius, Color color) 
    {
        this.centerX = centerX;
        this.centerY = centerY;
        this.radius = radius;
        this.color = color;
    }

    @Override
    public void draw(GraphicsContext gc) 
    {
        gc.setFill(color);
        gc.fillOval(centerX - radius, centerY - radius, radius * 2, radius * 2);
    }

    @Override
    public double getArea()
    {
        return Math.PI * radius * radius;
    }

    @Override
    public String toString()
    {
        return "Circle [centerX=" + centerX + ", centerY=" + centerY + ", radius=" + radius + ", color=" + color + "]";
    }
}

