import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Square implements DrawableShape
{
    private double x, y, size;
    private Color color;

    public Square(double x, double y, double size, Color color) 
    {
        this.x = x;
        this.y = y;
        this.size = size;
        this.color = color;
    }

    @Override
    public void draw(GraphicsContext gc) 
    {
        gc.setFill(color);
        gc.fillRect(x - size / 2, y - size / 2, size, size);
    }

    @Override
    public double getArea()
    {
        return size * size;
    }

    @Override
    public String toString()
    {
        return "Square [x=" + x + ", y=" + y + ", size=" + size + ", color=" + color + "]";
    }
}
