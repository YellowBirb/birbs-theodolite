package yellowbirb.birbstheodolite.util.config;

public class ShapeConfig {

    private Shape selectedShape;

    public ShapeConfig(Shape selectedShape) {
        this.selectedShape = selectedShape;
    }

    public Shape getSelectedShape() {
        return selectedShape;
    }

    public void setSelectedShape(Shape selectedShape) {
        this.selectedShape = selectedShape;
    }

    public enum Shape {
        BOXY_RING
    }
}
