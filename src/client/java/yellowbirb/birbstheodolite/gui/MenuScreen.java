package yellowbirb.birbstheodolite.gui;

import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import yellowbirb.birbstheodolite.BirbsTheodoliteClient;

import java.util.ArrayList;
import java.util.List;

public class MenuScreen extends Screen {

    public Screen parent;
    private final List<SettingsTab> tabs = new ArrayList<>();
    private Tab activeTab;
    float sidebarWidthDivisor;

    public MenuScreen(Text title, Screen parent) {
        this(title, parent, Tab.General, 7.5f);
    }

    public MenuScreen(Text title, Screen parent, Tab activeTab, float sidebarWidthDivisor) {
        super(title);
        this.parent = parent;
        this.activeTab = activeTab;
        this.sidebarWidthDivisor = sidebarWidthDivisor;
    }

    @Override
    protected void init() {

        this.clearChildren();
        this.tabs.clear();

        int sidebarWidth = Math.round(this.width/sidebarWidthDivisor);
        float b = sidebarWidth * ((float) 3 /4);

        this.addDrawable((context, mouseX, mouseY, deltaTicks) -> {
            context.fill(0, 0, sidebarWidth-3, height, 0xFF1B1B1F);
            context.fill(sidebarWidth-3, 0, sidebarWidth-2, height, 0xFF37373C);
            context.fill(sidebarWidth-2, 0, sidebarWidth, height, 0xFF56565B);

            context.drawTexture(RenderPipelines.GUI_TEXTURED, Identifier.of(BirbsTheodoliteClient.MOD_ID, "icon.png"), 0, 0, 0, sidebarWidth * 0.125f, sidebarWidth, sidebarWidth, sidebarWidth, (int) b, sidebarWidth, (int) b);

            if (textRenderer.getWidth("Birb's Theodolite") < sidebarWidth-6) {
                context.drawText(this.textRenderer, "Birb's Theodolite", sidebarWidth/2 - textRenderer.getWidth("Birb's Theodolite") / 2, sidebarWidth - this.textRenderer.fontHeight - 20, 0xFFFFFFFF, false);
            }
        });

        tabs.add(new GeneralTab(this.width + (-sidebarWidth), this.height));
        tabs.add(new MathTab(this.width + (-sidebarWidth), this.height));
        tabs.add(new ShapeTab(this.width + (-sidebarWidth), this.height));

        int i = 0;
        for (SettingsTab settingsTab : tabs) {
            ButtonWidget tabButton = ButtonWidget.builder(Text.literal(settingsTab.getTab().name()), (btn) -> this.activeTab = settingsTab.getTab()).dimensions(0, i*20 + sidebarWidth-10, sidebarWidth, 20).build();

            this.addDrawableChild(tabButton);
            i++;
        }

        for (SettingsTab settingsTab : tabs) {
            settingsTab.init();
        }
    }

    @Override
    public void close() {
        this.client.setScreen(parent);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {

        super.render(context, mouseX, mouseY, delta);

        tabs.get(activeTab.ordinal()).render(context, mouseX, mouseY, delta, Math.round(this.width/sidebarWidthDivisor), 0);

        /*
        TODO:
         check if buttons that aren't rendered are still there
         if yes see if those can be disabled
         */
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    public enum Tab
    {
        General,
        Math,
        Shape
    }
}
