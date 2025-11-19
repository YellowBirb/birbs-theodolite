package yellowbirb.birbstheodolite.render;

import lombok.experimental.UtilityClass;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import org.lwjgl.BufferUtils;
import yellowbirb.birbstheodolite.render.buffer.WorldBuffer;
import yellowbirb.birbstheodolite.render.shader.ShaderManager;

import java.awt.*;
import java.nio.FloatBuffer;
import java.util.Arrays;

import static org.lwjgl.opengl.GL33.*;

@UtilityClass
public class RenderUtil {

    public WorldBuffer startLines(WorldRenderContext ctx) {
        return new WorldBuffer(GL_LINES, ShaderManager.getPositionColorShader(), ctx);
    }

    public void endLines(WorldBuffer buffer) {
        boolean prevDepthTest = glIsEnabled(GL_DEPTH_TEST);
        if (!prevDepthTest) {
            glEnable(GL_DEPTH_TEST);
        }

        //glDepthRange(0, 0.5);
        boolean prevBlend = glIsEnabled(GL_BLEND);
        if (!prevBlend) {
            glEnable(GL_BLEND);
        }
        boolean prevDepthMask = glGetBoolean(GL_DEPTH_WRITEMASK);
        if (prevDepthMask) {
            glDepthMask(false);
        }

        //glBlendFuncSeparate(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA, GL_ONE, GL_ONE_MINUS_SRC_ALPHA);

        buffer.draw();


        if (prevDepthMask) {
            glDepthMask(true);
        }
        if (!prevBlend) {
            glDisable(GL_BLEND);
        }
        //glDepthRange(0, 1);
        //glClear(GL_DEPTH_BUFFER_BIT);
        if (!prevDepthTest) {
            glDisable(GL_DEPTH_TEST);
        }
    }

    public void drawLine(WorldBuffer buffer, float x1, float y1, float z1, float x2, float y2, float z2, Color color) {
        buffer.addVertex(x1, y1, z1, color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, color.getAlpha() / 255f);
        buffer.addVertex(x2, y2, z2, color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, color.getAlpha() / 255f);
    }

    public WorldBuffer startTri(WorldRenderContext context) {
        return new WorldBuffer(GL_TRIANGLES, ShaderManager.getPositionColorShader(), context);
    }

    public void endTri(WorldBuffer buffer) {
        glDepthRange(0, 0.7);
        boolean prevblend = glIsEnabled(GL_BLEND);
        if (!prevblend) {
            glEnable(GL_BLEND);
        }
        glBlendFuncSeparate(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA, GL_ONE, GL_ONE_MINUS_SRC_ALPHA);
        glDisable(GL_CULL_FACE);
        glDepthMask(false);
        buffer.draw();
        glDepthMask(true);
        glEnable(GL_CULL_FACE);
        if (!prevblend) {
            glDisable(GL_BLEND);
        }
        glDepthRange(0, 1);
    }

    public void drawTri(WorldBuffer buffer, float x1, float y1, float z1, float x2, float y2, float z2, float x3, float y3, float z3, Color color) {
        buffer.addVertex(x1, y1, z1, color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, color.getAlpha() / 255f);
        buffer.addVertex(x2, y2, z2, color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, color.getAlpha() / 255f);
        buffer.addVertex(x3, y3, z3, color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, color.getAlpha() / 255f);
    }
}
