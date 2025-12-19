package yellowbirb.birbstheodolite.gui;

import com.google.common.collect.Lists;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.util.List;

public class MenuScreen extends Screen {

    public Screen parent;
    private final List<Drawable> drawables = Lists.newArrayList();

    public MenuScreen(Text title, Screen parent) {
        super(title);
        this.parent = parent;
    }

    @Override
    protected  void init() {
        /*ButtonWidget buttonWidget = ButtonWidget.builder(Text.of("Hello World"), (btn) -> {
            System.out.println("Button Click");
        }).dimensions(40, 40, 120, 20).build();

        //this.addDrawableChild(buttonWidget);*/
    }

    @Override
    public void close() {
        this.client.setScreen(parent);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context, mouseX, mouseY, delta);

        int sidebarWidth = 150;
        context.fill(0, 0, sidebarWidth-3, this.height, 0xFF1B1B1F);
        context.fill(sidebarWidth-3, 0, sidebarWidth-2, this.height, 0xFF37373C);
        context.fill(sidebarWidth-2, 0, sidebarWidth, this.height, 0xFF56565B);

        for(Drawable drawable : this.drawables) {
            drawable.render(context, mouseX, mouseY, delta);
        }
    }

    @Override
    protected <T extends Element & Drawable & Selectable> T addDrawableChild(T drawableElement) {
        this.drawables.add(drawableElement);
        return (T)this.addSelectableChild(drawableElement);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
