package blue.beaming.flatitems;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

public class FlatItemsModMenu implements ModMenuApi {
    @Override public ConfigScreenFactory<@NotNull Screen> getModConfigScreenFactory() {
        return OptonsScreen::new;
    }

    private static class OptonsScreen extends Screen {
        private final HeaderAndFooterLayout layout;
        private final Screen s;

        private boolean enabled;
        private boolean affect3D;
        private boolean renderSides;

        OptonsScreen(Screen s) {
            super(Component.translatable("flat_items.mod_name"));
            this.s = s;
            layout = new HeaderAndFooterLayout(this);
            enabled = FlatItems.settings().enabled();
            affect3D = FlatItems.settings().affect3D();
            renderSides = FlatItems.settings().renderSides();
        }

        @Override public void onClose() {
            this.minecraft.setScreen(s);
            FlatItemsFabric.updateSettings(new FabricSettings(enabled, affect3D, renderSides));
        }

        @Override protected void init() {
            super.init();
            var x = width / 2 - 80;
            addRenderableWidget(CycleButton.onOffBuilder(enabled).withTooltip(unused -> Tooltip.create(Component.translatable("flat_items.setting.enabled.tooltip")))
                    .create(x, layout.getHeaderHeight() + 8, 150, 20, Component.translatable("flat_items.setting.enabled"), (c, b) -> enabled = b));
            addRenderableWidget(CycleButton.onOffBuilder(affect3D).withTooltip(unused -> Tooltip.create(Component.translatable("flat_items.setting.affect_3d.tooltip")))
                    .create(x, layout.getHeaderHeight() + 36, 150, 20, Component.translatable("flat_items.setting.affect_3d"), (c, b) -> affect3D = b));
            addRenderableWidget(CycleButton.onOffBuilder(renderSides).withTooltip(unused -> Tooltip.create(Component.translatable("flat_items.setting.render_sides.tooltip")))
                    .create(x, layout.getHeaderHeight() + 64, 150, 20, Component.translatable("flat_items.setting.render_sides"), (c, b) -> renderSides = b));
            layout.addToFooter(Button.builder(CommonComponents.GUI_DONE, ignored -> onClose()).width(200).build());
            layout.addTitleHeader(Component.translatable("flat_items.configuration.title"), font);
            layout.visitWidgets(this::addRenderableWidget);
            layout.arrangeElements();
        }
    }
}
