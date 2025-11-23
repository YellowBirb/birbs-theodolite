package yellowbirb.birbstheodolite;

import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.context.CommandContext;
import lombok.Getter;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.text.Text;
import yellowbirb.birbstheodolite.render.Renderer;
import yellowbirb.birbstheodolite.render.shapes.CircleXZ;
import yellowbirb.birbstheodolite.render.shapes.Line;

public class BirbsTheodoliteClient implements ClientModInitializer {

    @Getter
    private static BirbsTheodoliteClient instance;

    public static final String MOD_ID = "birbs-theodolite";

    @Override
    public void onInitializeClient() {
        instance = this;

        WorldRenderEvents.LAST.register(ctx -> {
            Renderer.drawShape(ctx, new Line(0, 0, 0, 0, 1, 0, 255, 0, 0, 255, false));

            Renderer.drawShape(ctx, new CircleXZ(3f, 0.5f, 0f, 3f, 0f, 255, 255, 0, 255, true));
        });

        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            dispatcher.register(ClientCommandManager.literal("drawdiamond")
                    .then(ClientCommandManager.argument("x", FloatArgumentType.floatArg())
                            .then(ClientCommandManager.argument("y", FloatArgumentType.floatArg())
                                    .then(ClientCommandManager.argument("z", FloatArgumentType.floatArg())
                                            .executes(this::executeTestCommand)))));
        });

        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            dispatcher.register(ClientCommandManager.literal("stopdraw").executes(this::executeStopDraw));
        });
    }

    private int executeTestCommand(CommandContext<FabricClientCommandSource> context) {
        float x = FloatArgumentType.getFloat(context, "x");
        float y = FloatArgumentType.getFloat(context, "y");
        float z = FloatArgumentType.getFloat(context, "z");
        context.getSource().sendFeedback(Text.literal("Called /drawdiamond " + x + " " + y + " " + z));
        return 1;
    }

    private int executeStopDraw(CommandContext<FabricClientCommandSource> context) {
        context.getSource().sendFeedback(Text.literal("Called /stopdraw"));
        return 1;
    }
}