package yellowbirb.birbstheodolite;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientWorldEvents;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.SharedConstants;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.text.*;
import net.minecraft.util.Formatting;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import yellowbirb.birbstheodolite.event.GameMessageHandler;
import yellowbirb.birbstheodolite.render.RenderManager;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;

public class BirbsTheodoliteClient implements ClientModInitializer {

    public static final String MOD_ID = "birbs-theodolite";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    private static final String MODRINTH_PROJECT_VERSION_API_LINK = "https://api.modrinth.com/v2/project/birbs-theodolite/version";

    @Override
    public void onInitializeClient() {

        // draw stuff
        WorldRenderEvents.LAST.register(RenderManager::draw);

        // Listen to Messages sent by the server
        ClientReceiveMessageEvents.GAME.register((message, overlay) -> GameMessageHandler.incoming(message));

        // check for updates when joining a server
        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> checkForUpdate());

        // stop drawing stuff when leaving the lobby
        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> RenderManager.clear());
        ClientWorldEvents.AFTER_CLIENT_WORLD_CHANGE.register((client, world) -> RenderManager.clear());

        // manually stop drawing stuff
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) ->
                dispatcher.register(ClientCommandManager.literal("clearTheodolite").executes((context) -> {
                    RenderManager.clear();
                    context.getSource().getPlayer().sendMessage(Text.literal("§3[Birb's Theodolite] §aCleared all Objects drawn in the World!"), false);
                    return 1;
                }
        )));
    }

    private void checkForUpdate() {
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        // can only be called in-game, so there must for sure be a player riiiiight
        assert player != null;

        try {
            // get Versions from API
            String jsonString = IOUtils.toString(new URI(MODRINTH_PROJECT_VERSION_API_LINK).toURL().openStream(), StandardCharsets.UTF_8);
            JsonArray jsonArray = JsonParser.parseString(jsonString).getAsJsonArray();
            String mcVer = SharedConstants.getGameVersion().name();
            String highestVer = "";
            String highestVerID = "";

            // find latest version
            // assumes latest version is always first in Modrinth API
            for (int i = 0; i < jsonArray.size(); i++) {
                JsonArray gameVers = jsonArray.get(i).getAsJsonObject().get("game_versions").getAsJsonArray();
                for (int j = 0; j < gameVers.size(); j++) {
                    if (gameVers.get(j).getAsString().equals(mcVer)) {
                        highestVer = jsonArray.get(i).getAsJsonObject().get("version_number").getAsString();
                        highestVerID = jsonArray.get(i).getAsJsonObject().get("id").getAsString();
                        break;
                    }
                }
                if (!highestVer.isEmpty()) {
                    break;
                }
            }

            if (highestVer.isEmpty()) {
                player.sendMessage(Text.literal("§3[Birb's Theodolite] §cCould not find any version of the mod for this Minecraft Version in Modrinth API!"), false);
                return;
            }

            // give player link to highest found version if it is greater than version found in Mod's Metadata
            if (isLesserVerThan(FabricLoader.getInstance().getModContainer(MOD_ID).get().getMetadata().getVersion().getFriendlyString(), highestVer)) {
                String newVerLink = "https://modrinth.com/mod/birbs-theodolite/version/" + highestVerID;
                URI verURI = new URI(newVerLink);
                MutableText link = Text.literal("here");
                link.setStyle(link.getStyle()
                        .withFormatting(Formatting.BLUE, Formatting.UNDERLINE)
                        .withClickEvent(new ClickEvent.OpenUrl(verURI))
                        .withHoverEvent(new HoverEvent.ShowText(Text.literal(newVerLink))));
                player.sendMessage(Text.literal("§3[Birb's Theodolite] §aFound new version §r§e" + highestVer + "§a of Birb's Theodolite §r").append(link), false);
            }
        } catch (URISyntaxException | IOException e) {
            LOGGER.error(e.toString());
            player.sendMessage(Text.literal("§3[Birb's Theodolite] §cAn error occurred while trying to check for updates!"), false);
        }
    }

    // assumes no characters other than numbers and dots in version string
    private boolean isLesserVerThan(String ver1, String ver2) {
        String[] ver1arr = ver1.split("\\.");
        String[] ver2arr = ver2.split("\\.");

        for (int i = 0; i < ver1arr.length; i++) {
            if (Integer.parseInt(ver1arr[i]) > Integer.parseInt(ver2arr[i])) {
                return false;
            } else if (Integer.parseInt(ver1arr[i]) < Integer.parseInt(ver2arr[i])) {
                return true;
            }
        }
        return ver1arr.length < ver2arr.length;
    }
}