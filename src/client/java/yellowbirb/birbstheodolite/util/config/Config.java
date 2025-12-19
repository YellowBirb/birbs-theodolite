package yellowbirb.birbstheodolite.util.config;

public class Config {

    private MathConfig mathConfig;
    private ShapeConfig shapeConfig;

    public Config(MathConfig mathConfig, ShapeConfig shapeConfig) {
        this.mathConfig = mathConfig;
        this.shapeConfig = shapeConfig;
    }

    public static Config defaults() {
        return new Config(
                new MathConfig(
                        false,
                        1,
                        1
                ),
                new ShapeConfig(
                        ShapeConfig.Shape.BOXY_RING
                )
        );
    }

    public MathConfig getMathConfig() {
        return mathConfig;
    }

    public void setMathConfig(MathConfig mathConfig) {
        this.mathConfig = mathConfig;
    }

    public ShapeConfig getShapeConfig() {
        return shapeConfig;
    }

    public void setShapeConfig(ShapeConfig shapeConfig) {
        this.shapeConfig = shapeConfig;
    }
}
