package yellowbirb.birbstheodolite.render.shapes;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;

public interface RenderShape {

    default RenderPipeline getRenderPipeline() {
        if (isVisibleThroughWalls()) {
            return getSeeThroughRenderPipeline();
        } else {
            return getNonSeeThroughRenderPipeline();
        }
    }

    boolean isVisibleThroughWalls();

    RenderPipeline getSeeThroughRenderPipeline();

    RenderPipeline getNonSeeThroughRenderPipeline();

    void render(MatrixStack matrixStack, VertexConsumer vertexConsumer);

}
