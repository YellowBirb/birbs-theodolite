package yellowbirb.birbstheodolite.util.config.data;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.util.Set;

public class MathConfig {

    private boolean usePlayerEyeLevel;
    private float angleMargin;
    private float heightMargin;

    public MathConfig(boolean usePlayerEyeLevel, float angleMargin, float heightMargin) {
        this.usePlayerEyeLevel = usePlayerEyeLevel;
        this.angleMargin = angleMargin;
        this.heightMargin = heightMargin;
    }

    public static MathConfig defaults() {
        return new MathConfig(false, 1, 1);
    }

    public static JsonObject ensureCompleteKeySet(JsonObject configJson) {
        Set<String> keySet = configJson.keySet();

        if (!keySet.contains("usePlayerEyeLevel")) {
            configJson.add("usePlayerEyeLevel", new JsonPrimitive(defaults().isUsePlayerEyeLevel()));
        }

        if (!keySet.contains("angleMargin")) {
            configJson.add("angleMargin", new JsonPrimitive(defaults().getAngleMargin()));
        }

        if (!keySet.contains("heightMargin")) {
            configJson.add("heightMargin", new JsonPrimitive(defaults().getHeightMargin()));
        }

        return configJson;
    }

    public String toString(boolean pretty) {
        return pretty ?
                "{\nuPEL: "+isUsePlayerEyeLevel()+", \naM: "+getAngleMargin()+", \nhM: "+getHeightMargin()+"\n}" :
                "{uPEL: "+isUsePlayerEyeLevel()+", aM: "+getAngleMargin()+", hM: "+getHeightMargin()+"}";
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
