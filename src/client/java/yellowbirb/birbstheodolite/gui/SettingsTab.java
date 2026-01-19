package yellowbirb.birbstheodolite.gui;

import net.minecraft.client.gui.DrawContext;

public abstract class SettingsTab {
    private MenuScreen.Tab tab;
    private int width;
    private int height;

    public SettingsTab(MenuScreen.Tab tab, int width, int height) {
        this.tab = tab;
        this.width = width;
        this.height = height;
    }

    public abstract void init();

    public abstract void enable();

    public abstract void disable();

    public abstract void render(DrawContext context, int mouseX, int mouseY, float delta, int offsetX, int offsetY);

    public MenuScreen.Tab getTab() {
        return tab;
    }

    public void setTab(MenuScreen.Tab tab) {
        this.tab = tab;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}