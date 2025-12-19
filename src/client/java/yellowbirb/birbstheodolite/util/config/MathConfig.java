package yellowbirb.birbstheodolite.util.config;

public class MathConfig {

    private boolean usePlayerEyeLevel;
    private float angleMargin;
    private float heightMargin;

    public MathConfig(boolean usePlayerEyeLevel, float angleMargin, float heightMargin) {
        this.usePlayerEyeLevel = usePlayerEyeLevel;
        this.angleMargin = angleMargin;
        this.heightMargin = heightMargin;
    }

    public boolean isUsePlayerEyeLevel() {
        return usePlayerEyeLevel;
    }

    public void setUsePlayerEyeLevel(boolean usePlayerEyeLevel) {
        this.usePlayerEyeLevel = usePlayerEyeLevel;
    }

    public float getAngleMargin() {
        return angleMargin;
    }

    public void setAngleMargin(float angleMargin) {
        this.angleMargin = angleMargin;
    }

    public float getHeightMargin() {
        return heightMargin;
    }

    public void setHeightMargin(float heightMargin) {
        this.heightMargin = heightMargin;
    }
}
