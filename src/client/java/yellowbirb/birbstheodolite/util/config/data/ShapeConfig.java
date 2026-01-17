package yellowbirb.birbstheodolite.util.config.data;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import yellowbirb.birbstheodolite.util.config.Config;

import java.util.Set;

public class ShapeConfig {

    private Shape selectedShape;

    private RGBA boxyRingInnerColor;
    private RGBA boxyRingOuterCornerColor;
    private RGBA boxyRingOuterPlaneColor;
    private boolean boxyRingInnerVisibleThroughWalls;
    private boolean boxyRingOuterCornerVisibleThroughWalls;
    private boolean boxyRingOuterPlaneVisibleThroughWalls;

    public ShapeConfig(Shape selectedShape, RGBA boxyRingInnerColor, RGBA boxyRingOuterCornerColor, RGBA boxyRingOuterPlaneColor, boolean boxyRingInnerVisibleThroughWalls, boolean boxyRingOuterCornerVisibleThroughWalls, boolean boxyRingOuterPlaneVisibleThroughWalls) {
        this.selectedShape = selectedShape;
        setBoxyRingInnerColor(boxyRingInnerColor);
        setBoxyRingOuterCornerColor(boxyRingOuterCornerColor);
        setBoxyRingOuterPlaneColor(boxyRingOuterPlaneColor);
        this.boxyRingInnerVisibleThroughWalls = boxyRingInnerVisibleThroughWalls;
        this.boxyRingOuterCornerVisibleThroughWalls = boxyRingOuterCornerVisibleThroughWalls;
        this.boxyRingOuterPlaneVisibleThroughWalls = boxyRingOuterPlaneVisibleThroughWalls;
    }

    public static ShapeConfig defaults() {
        return new ShapeConfig(
                ShapeConfig.Shape.BOXY_RING,
                new RGBA(255, 0, 0, 255),
                new RGBA(0, 255, 0, 255),
                new RGBA(0, 255, 0, 50),
                true,
                true,
                false
        );
    }

    public static JsonObject ensureCompleteKeySet(JsonObject configJson) {
        Set<String> keySet = configJson.keySet();
        Gson gson = new Gson();

        if (!keySet.contains("selectedShape")) {
            configJson.add("selectedShape", new JsonPrimitive(defaults().getSelectedShape().name()));
        }

        if (!keySet.contains("boxyRingInnerColor")) {
            configJson.add("boxyRingInnerColor", JsonParser.parseString(gson.toJson(defaults().getBoxyRingInnerColor())));
        }
        else {
            configJson.add("boxyRingInnerColor", RGBA.ensureCompleteKeySet(configJson.get("boxyRingInnerColor").getAsJsonObject(), defaults().getBoxyRingInnerColor().getR(), defaults().getBoxyRingInnerColor().getG(), defaults().getBoxyRingInnerColor().getB(), defaults().getBoxyRingInnerColor().getA()));
        }

        if (!keySet.contains("boxyRingOuterCornerColor")) {
            configJson.add("boxyRingOuterCornerColor", JsonParser.parseString(gson.toJson(defaults().getBoxyRingOuterCornerColor())));
        }
        else {
            configJson.add("boxyRingOuterCornerColor", RGBA.ensureCompleteKeySet(configJson.get("boxyRingOuterCornerColor").getAsJsonObject(), defaults().getBoxyRingOuterCornerColor().getR(), defaults().getBoxyRingOuterCornerColor().getG(), defaults().getBoxyRingOuterCornerColor().getB(), defaults().getBoxyRingOuterCornerColor().getA()));
        }

        if (!keySet.contains("boxyRingOuterPlaneColor")) {
            configJson.add("boxyRingOuterPlaneColor", JsonParser.parseString(gson.toJson(defaults().getBoxyRingOuterPlaneColor())));
        }
        else {
            configJson.add("boxyRingOuterPlaneColor", RGBA.ensureCompleteKeySet(configJson.get("boxyRingOuterPlaneColor").getAsJsonObject(), defaults().getBoxyRingOuterPlaneColor().getR(), defaults().getBoxyRingOuterPlaneColor().getG(), defaults().getBoxyRingOuterPlaneColor().getB(), defaults().getBoxyRingOuterPlaneColor().getA()));
        }

        if (!keySet.contains("boxyRingInnerVisibleThroughWalls")) {
            configJson.add("boxyRingInnerVisibleThroughWalls", new JsonPrimitive(defaults().isBoxyRingInnerVisibleThroughWalls()));
        }

        if (!keySet.contains("boxyRingOuterCornerVisibleThroughWalls")) {
            configJson.add("boxyRingOuterCornerVisibleThroughWalls", new JsonPrimitive(defaults().isBoxyRingOuterCornerVisibleThroughWalls()));
        }

        if (!keySet.contains("boxyRingOuterPlaneVisibleThroughWalls")) {
            configJson.add("boxyRingOuterPlaneVisibleThroughWalls", new JsonPrimitive(defaults().isBoxyRingOuterPlaneVisibleThroughWalls()));
        }

        return configJson;
    }

    public String toString(boolean pretty) {
        String bRIC = getBoxyRingInnerColor() == null ? "null" : getBoxyRingInnerColor().toString(pretty);
        String bROCC = getBoxyRingOuterCornerColor() == null ? "null" : getBoxyRingOuterCornerColor().toString(pretty);
        String bROPC = getBoxyRingOuterPlaneColor() == null ? "null" : getBoxyRingOuterPlaneColor().toString(pretty);
        return pretty ?
                "{\nbRIC: "+bRIC+", \nbROCC: "+bROCC+", \nbROPC: "+bROPC+", \nbRIVTW: "+isBoxyRingInnerVisibleThroughWalls()+", \nbROCVTW: "+isBoxyRingOuterCornerVisibleThroughWalls()+", \nbROPVTW: "+isBoxyRingOuterPlaneVisibleThroughWalls()+"\n}" :
                "{bRIC: "+bRIC+", bROCC: "+bROCC+", bROPC: "+bROPC+", bRIVTW: "+isBoxyRingInnerVisibleThroughWalls()+", bROCVTW: "+isBoxyRingOuterCornerVisibleThroughWalls()+", bROPVTW: "+isBoxyRingOuterPlaneVisibleThroughWalls()+"}";
    }

    public Shape getSelectedShape() {
        return selectedShape;
    }

    public void setSelectedShape(Shape selectedShape) {
        this.selectedShape = selectedShape;
    }

    public RGBA getBoxyRingInnerColor() {
        return boxyRingInnerColor;
    }

    public void setBoxyRingInnerColor(RGBA boxyRingInnerColor) {
        if (boxyRingInnerColor == null) {
            this.boxyRingInnerColor = Config.defaults().getShapeConfig().getBoxyRingInnerColor();
        }
        else {
            this.boxyRingInnerColor = boxyRingInnerColor;
        }
    }

    public RGBA getBoxyRingOuterCornerColor() {
        return boxyRingOuterCornerColor;
    }

    public void setBoxyRingOuterCornerColor(RGBA boxyRingOuterCornerColor) {
        if (boxyRingOuterCornerColor == null) {
            this.boxyRingOuterCornerColor = Config.defaults().getShapeConfig().getBoxyRingOuterCornerColor();
        }
        else {
            this.boxyRingOuterCornerColor = boxyRingOuterCornerColor;
        }
    }

    public RGBA getBoxyRingOuterPlaneColor() {
        return boxyRingOuterPlaneColor;
    }

    public void setBoxyRingOuterPlaneColor(RGBA boxyRingOuterPlaneColor) {
        if (boxyRingOuterPlaneColor == null) {
            this.boxyRingOuterPlaneColor = Config.defaults().getShapeConfig().getBoxyRingOuterPlaneColor();
        }
        else {
            this.boxyRingOuterPlaneColor = boxyRingOuterPlaneColor;
        }
    }

    public boolean isBoxyRingInnerVisibleThroughWalls() {
        return boxyRingInnerVisibleThroughWalls;
    }

    public void setBoxyRingInnerVisibleThroughWalls(boolean boxyRingInnerVisibleThroughWalls) {
        this.boxyRingInnerVisibleThroughWalls = boxyRingInnerVisibleThroughWalls;
    }

    public boolean isBoxyRingOuterCornerVisibleThroughWalls() {
        return boxyRingOuterCornerVisibleThroughWalls;
    }

    public void setBoxyRingOuterCornerVisibleThroughWalls(boolean boxyRingOuterCornerVisibleThroughWalls) {
        this.boxyRingOuterCornerVisibleThroughWalls = boxyRingOuterCornerVisibleThroughWalls;
    }

    public boolean isBoxyRingOuterPlaneVisibleThroughWalls() {
        return boxyRingOuterPlaneVisibleThroughWalls;
    }

    public void setBoxyRingOuterPlaneVisibleThroughWalls(boolean boxyRingOuterPlaneVisibleThroughWalls) {
        this.boxyRingOuterPlaneVisibleThroughWalls = boxyRingOuterPlaneVisibleThroughWalls;
    }

    public enum Shape {
        BOXY_RING
    }
}
