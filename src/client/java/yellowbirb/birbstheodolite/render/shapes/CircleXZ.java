package yellowbirb.birbstheodolite.render.shapes;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;
import org.joml.Vector3f;
import yellowbirb.birbstheodolite.render.CustomRenderPipelines;
import yellowbirb.birbstheodolite.util.config.data.RGBA;

import static java.lang.Math.*;

public class CircleXZ implements RenderShape{

    public final float radius;
    public final int segmentAmount;
    public final float x;
    public final float y;
    public final float z;
    public final int r;
    public final int g;
    public final int b;
    public final int a;
    public final boolean visibleThroughWalls;

    public CircleXZ(float radius, int segmentAmount, float x, float y, float z, int r, int g, int b, int a, boolean visibleThroughWalls) {
        this.radius = radius;
        this.segmentAmount = max(8, segmentAmount);
        this.x = x;
        this.y = y;
        this.z = z;
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
        this.visibleThroughWalls = visibleThroughWalls;
    }

    public CircleXZ(float radius, int segmentAmount, float x, float y, float z, RGBA color, boolean visibleThroughWalls) {
        this(radius, segmentAmount, x, y, z, color.getR(), color.getG(), color.getB(), color.getA(), visibleThroughWalls);
    }

    public CircleXZ(float radius, float segmentLength, float x, float y, float z, int r, int g, int b, int a, boolean visibleThroughWalls) {
        this(radius, max(8, (int) round((PI * radius * 2) / segmentLength)), x, y, z, r, g, b, a, visibleThroughWalls);
    }

    public CircleXZ(float radius, float segmentLength, float x, float y, float z, RGBA color, boolean visibleThroughWalls) {
        this(radius, segmentLength, x, y, z, color.getR(), color.getG(), color.getB(), color.getA(), visibleThroughWalls);
    }

    @Override
    public boolean isVisibleThroughWalls() {
        return this.visibleThroughWalls;
    }

    @Override
    public RenderPipeline getSeeThroughRenderPipeline() {
        return CustomRenderPipelines.LINE_STRIP_THROUGH_WALLS;
    }

    @Override
    public RenderPipeline getNonSeeThroughRenderPipeline() {
        return CustomRenderPipelines.LINE_STRIP;
    }

    @Override
    public void render(MatrixStack matrixStack, VertexConsumer vertexConsumer) {
        MatrixStack.Entry entry = matrixStack.peek();

        for (int i = 0; i <= segmentAmount; i++) {

            float x1 = circleX(i, segmentAmount, radius, x);
            float x2 = circleX(i + 1, segmentAmount, radius, x);
            float z1 = circleZ(i, segmentAmount, radius, z);
            float z2 = circleZ(i + 1, segmentAmount, radius, z);

            Vector3f normalVec = new Vector3f(x2-x1, 0, z2-z1).normalize();

            vertexConsumer.vertex(entry, x1, y, z1)
                    .color(r, g, b, a)
                    .normal(entry, normalVec);
        }
    }

    private float circleX(int i, int segmentAmount, float radius, float x) {
        float angle = (float) (((double) i /segmentAmount) * (2 * PI));
        float xOffset = (float) (cos(angle) * radius);
        return x + xOffset;
    }

    private float circleZ(int i, int segmentAmount, float radius, float z) {
        float angle = (float) (((double) i /segmentAmount) * (2 * PI));
        float zOffset = (float) (sin(angle) * radius);
        return z + zOffset;
    }
}
