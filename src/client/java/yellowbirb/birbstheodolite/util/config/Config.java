package yellowbirb.birbstheodolite.util.config;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import yellowbirb.birbstheodolite.util.config.data.MathConfig;
import yellowbirb.birbstheodolite.util.config.data.ShapeConfig;

import java.util.Set;

public class Config {

    private MathConfig mathConfig;
    private ShapeConfig shapeConfig;

    public Config(MathConfig mathConfig, ShapeConfig shapeConfig) {
        setMathConfig(mathConfig);
        setShapeConfig(shapeConfig);
    }

    public static Config defaults() {
        return new Config(MathConfig.defaults(), ShapeConfig.defaults());
    }

    public static JsonObject ensureCompleteKeySet(JsonObject configJson) {
        Set<String> keySet = configJson.keySet();
        Gson gson = new Gson();

        if (!keySet.contains("mathConfig")) {
            configJson.add("mathConfig", JsonParser.parseString(gson.toJson(MathConfig.defaults())));
        }
        else {
            configJson.add("mathConfig", MathConfig.ensureCompleteKeySet(configJson.get("mathConfig").getAsJsonObject()));
        }

        if (!keySet.contains("shapeConfig")) {
            configJson.add("shapeConfig", JsonParser.parseString(gson.toJson(MathConfig.defaults())));
        }
        else {
            configJson.add("shapeConfig", ShapeConfig.ensureCompleteKeySet(configJson.get("shapeConfig").getAsJsonObject()));
        }

        return configJson;
    }

    public String toString(boolean pretty) {
        return pretty ?
                "{\nmathConfig:"+getMathConfig().toString(true)+", \nshapeConfig:"+getShapeConfig().toString(true)+"\n}" :
                "{mathConfig: "+getMathConfig().toString(false)+", shapeConfig:"+getShapeConfig().toString(false)+"}";
    }

    public MathConfig getMathConfig() {
        return mathConfig;
    }

    public void setMathConfig(MathConfig mathConfig) {
        if (mathConfig == null) {
            this.mathConfig = defaults().getMathConfig();
        }
        else {
            this.mathConfig = mathConfig;
        }
    }

    public ShapeConfig getShapeConfig() {
        return shapeConfig;
    }

    public void setShapeConfig(ShapeConfig shapeConfig) {
        if (shapeConfig == null) {
            this.shapeConfig = defaults().getShapeConfig();
        }
        else {
            this.shapeConfig = shapeConfig;
        }
    }
}
