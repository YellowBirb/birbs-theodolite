package yellowbirb.birbstheodolite;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import yellowbirb.birbstheodolite.event.GameMessageHandler;
import yellowbirb.birbstheodolite.render.RenderManager;

public class BirbsTheodoliteClient implements ClientModInitializer {

    public static final String MOD_ID = "birbs-theodolite";

    @Override
    public void onInitializeClient() {

        WorldRenderEvents.LAST.register(RenderManager::draw);

        ClientReceiveMessageEvents.GAME.register((message, overlay) -> GameMessageHandler.incoming(message));
    }
}