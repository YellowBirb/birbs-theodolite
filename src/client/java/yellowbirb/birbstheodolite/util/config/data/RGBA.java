package yellowbirb.birbstheodolite.util.config.data;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.util.Set;

public class RGBA {
    private int r;
    private int g;
    private int b;
    private int a;

    public RGBA(int r, int g, int b, int a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }

    public static JsonObject ensureCompleteKeySet(JsonObject configJson, int r, int g, int b, int a) {
        Set<String> keySet = configJson.keySet();

        if (!keySet.contains("r")) {
            configJson.add("r", new JsonPrimitive(r));
        }

        if (!keySet.contains("g")) {
            configJson.add("g", new JsonPrimitive(g));
        }

        if (!keySet.contains("b")) {
            configJson.add("b", new JsonPrimitive(b));
        }

        if (!keySet.contains("a")) {
            configJson.add("a", new JsonPrimitive(a));
        }

        return configJson;
    }

    public String toString(boolean pretty) {
        return pretty ?
                "{\nr: "+getR()+", \ng:"+getG()+" , \nb:"+getB()+" , \na:"+getA()+"\n}" :
                "{r: "+getR()+", g:"+getG()+" , b:"+getB()+" , a:"+getA()+"}";
    }

    public int getR() {
        return r;
    }

    public void setR(int r) {
        this.r = Math.min(Math.max(r, 0), 255);
    }

    public int getG() {
        return g;
    }

    public void setG(int g) {
        this.g = Math.min(Math.max(g, 0), 255);
    }

    public int getB() {
        return b;
    }

    public void setB(int b) {
        this.b = Math.min(Math.max(b, 0), 255);
    }

    public int getA() {
        return a;
    }

    public void setA(int a) {
        this.a = Math.min(Math.max(a, 0), 255);
    }
}
