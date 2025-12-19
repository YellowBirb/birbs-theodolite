package yellowbirb.birbstheodolite.gui;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

import java.io.IOException;

public class MenuScreen extends Screen {

    public Screen parent;

    public MenuScreen(Text title, Screen parent) {
        super(title);
        this.parent = parent;
    }

    @Override
    protected  void init() {
        ButtonWidget buttonWidget = ButtonWidget.builder(Text.of("Hello World"), (btn) -> {
            System.out.println("Button Click");
            String currentPath;
            try {
                currentPath = new java.io.File(".").getCanonicalPath();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            System.out.println("Current dir:" + currentPath);
            String currentDir = System.getProperty("user.dir");
            System.out.println("Current dir using System:" + currentDir);
        }).dimensions(40, 40, 120, 20).build();

        this.addDrawableChild(buttonWidget);
    }

    @Override
    public void close() {
        this.client.setScreen(parent);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);

        // Minecraft doesn't have a "label" widget, so we'll have to draw our own text.
        // We'll subtract the font height from the Y position to make the text appear above the button.
        // Subtracting an extra 10 pixels will give the text some padding.
        // textRenderer, text, x, y, color, hasShadow
        context.drawText(this.textRenderer, "Special Button", 40, 40 - this.textRenderer.fontHeight - 10, 0xFFFFFFFF, true);
    }
}
