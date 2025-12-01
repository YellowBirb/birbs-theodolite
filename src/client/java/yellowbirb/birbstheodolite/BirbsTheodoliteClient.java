package yellowbirb.birbstheodolite;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import yellowbirb.birbstheodolite.event.GameMessageHandler;
import yellowbirb.birbstheodolite.render.RenderManager;

public class BirbsTheodoliteClient implements ClientModInitializer {

    public static final String MOD_ID = "birbs-theodolite";

    @Override
    public void onInitializeClient() {

        WorldRenderEvents.LAST.register(RenderManager::draw);

        ClientReceiveMessageEvents.GAME.register((message, overlay) -> GameMessageHandler.incoming(message));
        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> RenderManager.clear()); // disconnect event, test!

        // for 1.0.1: draw between lines
        // then config
        // after config: eye level, line thickness uniform
    }
}