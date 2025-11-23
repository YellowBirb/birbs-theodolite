package yellowbirb.birbstheodolite.render;

import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.VertexFormat;
import lombok.experimental.UtilityClass;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BuiltBuffer;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.util.BufferAllocator;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec3d;
import yellowbirb.birbstheodolite.render.shapes.RenderShape;

import java.util.OptionalDouble;
import java.util.OptionalInt;

@UtilityClass
public class Renderer {

    private static final BufferAllocator allocator = new BufferAllocator(RenderLayer.CUTOUT_BUFFER_SIZE);

    public void drawShape(WorldRenderContext ctx, RenderShape shape) {
        MatrixStack matrices = ctx.matrixStack();
        Vec3d cam = ctx.camera().getPos();

        assert matrices != null;
        matrices.push();
        matrices.translate(-cam.x, -cam.y, -cam.z);

        RenderPipeline pipeline = shape.getRenderPipeline();

        BufferBuilder bufferBuilder =
                new BufferBuilder(allocator, pipeline.getVertexFormatMode(), pipeline.getVertexFormat());

        shape.render(matrices, bufferBuilder);

        draw(shape.getRenderPipeline(), bufferBuilder.end());

        matrices.pop();
    }

    private void draw(RenderPipeline pipeline, BuiltBuffer buffer) {
        GpuBuffer vertices = pipeline.getVertexFormat().uploadImmediateVertexBuffer(buffer.getBuffer());

        GpuBuffer indices;
        VertexFormat.IndexType indexType;

        if (buffer.getSortedBuffer() == null) {
            RenderSystem.ShapeIndexBuffer shapeIndexBuffer = RenderSystem.getSequentialBuffer(buffer.getDrawParameters().mode());
            indices = shapeIndexBuffer.getIndexBuffer(buffer.getDrawParameters().indexCount());
            indexType = shapeIndexBuffer.getIndexType();
        } else {
            indices = pipeline.getVertexFormat().uploadImmediateIndexBuffer(buffer.getSortedBuffer());
            indexType = buffer.getDrawParameters().indexType();
        }

        try (RenderPass renderPass = RenderSystem.getDevice()
                .createCommandEncoder()
                .createRenderPass(MinecraftClient.getInstance().getFramebuffer().getColorAttachment(), OptionalInt.empty(), MinecraftClient.getInstance().getFramebuffer().getDepthAttachment(), OptionalDouble.empty())) {

            renderPass.setPipeline(pipeline);

            renderPass.setUniform("Projection", RenderSystem.getProjectionMatrix());

            if (RenderSystem.SCISSOR_STATE.isEnabled()) {
                renderPass.enableScissor(RenderSystem.SCISSOR_STATE);
            }


            renderPass.setVertexBuffer(0, vertices);

            renderPass.setIndexBuffer(indices, indexType);


            renderPass.drawIndexed(0, buffer.getDrawParameters().indexCount());
        }

        buffer.close();
    }

    public void close() {
        allocator.close();
    }
}
