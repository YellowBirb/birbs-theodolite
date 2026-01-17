package yellowbirb.birbstheodolite.gui;

import net.minecraft.client.gui.DrawContext;

public class GeneralTab extends SettingsTab {
    public GeneralTab(int width, int height) {
        super(MenuScreen.Tab.General, width, height);
    }

    @Override
    public void init() {

    }

    @Override
    public void enable() {

    }

    @Override
    public void disable() {

    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta, int offsetX, int offsetY) {
        context.fill(offsetX, offsetY, this.getWidth() + offsetX, this.getHeight() + offsetY, 0x33FF0000);
    }
}
