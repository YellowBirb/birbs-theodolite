package yellowbirb.birbstheodolite.gui;

import net.minecraft.client.gui.DrawContext;

public class MathTab extends SettingsTab {
    public MathTab(int width, int height) {
        super(MenuScreen.Tab.Math, width, height);
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
        context.fill(offsetX, offsetY, this.getWidth() + offsetX, this.getHeight() + offsetY, 0x3300FF00);
    }
}
