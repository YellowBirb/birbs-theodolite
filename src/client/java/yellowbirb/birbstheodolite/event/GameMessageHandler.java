package yellowbirb.birbstheodolite.event;

import lombok.experimental.UtilityClass;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.text.*;
import yellowbirb.birbstheodolite.BirbsTheodoliteClient;
import yellowbirb.birbstheodolite.render.RenderManager;
import yellowbirb.birbstheodolite.render.shapes.CircleXZ;
import yellowbirb.birbstheodolite.render.shapes.InterCircleStrip;
import yellowbirb.birbstheodolite.util.config.Config;
import yellowbirb.birbstheodolite.util.config.ShapeConfig;

import java.util.Optional;

import static java.lang.Math.*;

@UtilityClass
public class GameMessageHandler {

    private final String THEODOLITE_MESSAGE = "The target is around [0-9]+ blocks (below|above), at a [1-9][0-9]? degrees angle!";
    private final String PELT_REWARD_MESSAGE = "Killing the animal rewarded you [1-9][0-9]? pelts.";

    private final String[] RELEVANT_MESSAGES = {THEODOLITE_MESSAGE, PELT_REWARD_MESSAGE};


    private int isRelevant(String msg) {

        if (msg == null || msg.isEmpty() || msg.startsWith(" ")) return -1;

        for (int i = 0; i < RELEVANT_MESSAGES.length; i++) {
            if (msg.charAt(0) == RELEVANT_MESSAGES[i].charAt(0)) {
                if (msg.matches(RELEVANT_MESSAGES[i])) {
                    return i;
                }
            }
        }
        return -1;
    }

    public void incoming(Text text) {

        final String[] messageArr = {""};

        text.visit((string) -> {
            messageArr[0] = messageArr[0] + string;
            return Optional.empty();
        });

        String message = messageArr[0];

        int messageType = isRelevant(message);

        if (messageType == -1) return;

        switch (messageType) {
            case 0:
                onReceiveTheodoliteMessage(message);
                break;
            case 1:
                onReceivePeltRewardMessage();
                break;
        }
    }

    private void onReceiveTheodoliteMessage(String message) {
        String[] words = message.split("\\s");

        int deltaY = Integer.parseInt(words[4]);
        double alpha = Math.toRadians(90 - Integer.parseInt(words[9]));

        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        // can only be called in-game, so there must for sure be a player riiiiight
        assert player != null;

        if (Integer.parseInt(words[9]) == 0) {
            MinecraftClient.getInstance().player.sendMessage(MutableText.of(PlainTextContent.of("§3[Birb's Theodolite] §cCannot calculate with 0 degree angle")), false);
            return;
        }

        if (words[6].equals("below,")) {
            deltaY = -deltaY;
        }

        Config config = BirbsTheodoliteClient.config;

        switch (config.getShapeConfig().getSelectedShape()) {
            case BOXY_RING:
                drawBoxyRing(alpha, deltaY, player, config);
                break;
            default:
                BirbsTheodoliteClient.LOGGER.warn("Reached default case for selected Shape, reverting to default");
                BirbsTheodoliteClient.config.getShapeConfig().setSelectedShape(Config.defaults().getShapeConfig().getSelectedShape());
                drawBoxyRing(alpha, deltaY, player, config);
                break;
        }


    }

    private void drawBoxyRing(double alpha, int deltaY, ClientPlayerEntity player, Config config) {
        double margin_rad = Math.toRadians(config.getMathConfig().getAngleMargin());

        float radius1_1 = (float) (Math.abs(Math.tan(alpha + margin_rad) * (deltaY + config.getMathConfig().getHeightMargin())));
        float radius1_2 = (float) (Math.abs(Math.tan(alpha + margin_rad) * (deltaY - config.getMathConfig().getHeightMargin())));

        float radius2 = (float) (Math.abs(Math.tan(alpha) * deltaY));

        float radius3_1 = (float) (Math.abs(Math.tan(alpha - margin_rad) * (deltaY + config.getMathConfig().getHeightMargin())));
        float radius3_2 = (float) (Math.abs(Math.tan(alpha - margin_rad) * (deltaY - config.getMathConfig().getHeightMargin())));

        float playerX = (float) player.getX();
        float playerY = (float) player.getY();
        float playerZ = (float) player.getZ();

        if (config.getMathConfig().isUsePlayerEyeLevel()) {
            playerY = (float) player.getEyeY();
        }

        float segmentLength = 0.5f;
        // resampling segmentAmount to draw clean InterCircleStrips
        int segmentAmount = max(8, (int) round((PI * max(radius1_1, max(radius1_2, max(radius2, max(radius3_1, radius3_2)))) * 2) / segmentLength));
        float lowY = playerY + deltaY - config.getMathConfig().getHeightMargin();
        float highY = playerY + deltaY + config.getMathConfig().getHeightMargin();

        RenderManager.add(new CircleXZ(radius1_1, segmentAmount, playerX, highY, playerZ, 0, 255, 0, 255, true));
        RenderManager.add(new CircleXZ(radius1_2, segmentAmount, playerX, lowY , playerZ, 0, 255, 0, 255, true));

        RenderManager.add(new CircleXZ(radius2, segmentAmount, playerX, playerY + deltaY, playerZ, 255, 0, 0, 255, true));

        RenderManager.add(new CircleXZ(radius3_1, segmentAmount, playerX, highY, playerZ, 0, 255, 0, 255, true));
        RenderManager.add(new CircleXZ(radius3_2, segmentAmount, playerX, lowY , playerZ, 0, 255, 0, 255, true));

        RenderManager.add(new InterCircleStrip(radius1_1, radius1_2, segmentAmount, playerX, highY, lowY , playerZ, 0, 255, 0, 50, true));
        RenderManager.add(new InterCircleStrip(radius3_1, radius3_2, segmentAmount, playerX, highY, lowY , playerZ, 0, 255, 0, 50, true));
        RenderManager.add(new InterCircleStrip(radius1_1, radius3_1, segmentAmount, playerX, highY, highY, playerZ, 0, 255, 0, 50, true));
        RenderManager.add(new InterCircleStrip(radius1_2, radius3_2, segmentAmount, playerX, lowY , lowY , playerZ, 0, 255, 0, 50, true));
    }

    private void onReceivePeltRewardMessage() {
        RenderManager.clear();
    }

}
