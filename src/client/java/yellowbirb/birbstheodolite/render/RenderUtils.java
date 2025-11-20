package yellowbirb.birbstheodolite.render;

import lombok.experimental.UtilityClass;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;

@UtilityClass
public class RenderUtils {

    public void renderSingleLine(WorldRenderContext ctx, float x0, float y0, float z0, float x1, float y1, float z1)
    {
        Vec3d cam = ctx.camera().getPos();
        VertexConsumerProvider vcp = ctx.consumers();
        VertexConsumer vertexConsumer = vcp.getBuffer(RenderLayer.getLines());

        x0 = x0 - (float) cam.getX();
        y0 = y0 - (float) cam.getY();
        z0 = z0 - (float) cam.getZ();
        x1 = x1 - (float) cam.getX();
        y1 = y1 - (float) cam.getY();
        z1 = z1 - (float) cam.getZ();

        MatrixStack.Entry erm = ctx.matrixStack().peek();

        Vector3f vertex0Pos = transformLineVertexPositions(erm.getPositionMatrix(), x0, y0, z0);
        Vector3f vertex1Pos = transformLineVertexPositions(erm.getPositionMatrix(), x1, y1, z1);

        Vector3f vertexNormals = getLineVertexNormals(erm.getNormalMatrix(), x0, y0, z0, x1, y1, z1);

        vertexConsumer.vertex(vertex0Pos.x, vertex0Pos.y, vertex0Pos.z)
                .color(1f, 0f, 0f, 1f)
                .normal(vertexNormals.x, vertexNormals.y, vertexNormals.z);

        vertexConsumer.vertex(vertex1Pos.x, vertex1Pos.y, vertex1Pos.z)
                .color(1f, 0f, 0f, 1f)
                .normal(vertexNormals.x, vertexNormals.y, vertexNormals.z);
    }

    private Vector3f getLineVertexNormals(Matrix3f normalMatrix, float x0, float y0, float z0, float x1, float y1, float z1)
    {
        Vector3f direction = new Vector3f(x1 - x0, y1 - y0, z1 - z0).normalize();
        Vector3f transformed = new Vector3f();
        normalMatrix.transform(direction, transformed);
        return transformed;
    }

    private Vector3f transformLineVertexPositions(Matrix4f positionMatrix, float x, float y, float z)
    {
        Vector3f pos = new Vector3f(x, y, z);
        Vector3f transformed = new Vector3f();
        positionMatrix.transformPosition(pos, transformed);
        return transformed;
    }
}
