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

import java.util.Locale;

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
        private boolean enlarge3D;
        private Settings.Facing facing;

        OptonsScreen(Screen s) {
            super(Component.translatable("flat_items.mod_name"));
            this.s = s;
            layout = new HeaderAndFooterLayout(this);
            enabled = FlatItems.settings().enabled();
            affect3D = FlatItems.settings().affect3D();
            renderSides = FlatItems.settings().renderSides();
            enlarge3D = FlatItems.settings().enlarge3D();
            facing = FlatItems.settings().facing();
        }

        @Override public void onClose() {
            this.minecraft.setScreen(s);
            FlatItemsFabric.updateSettings(new FabricSettings(enabled, affect3D, renderSides, enlarge3D, facing));
        }

        @Override protected void init() {
            super.init();
            var x = width / 2 - 80;
            addRenderableWidget(CycleButton.onOffBuilder(enabled)
                    .withTooltip(unused -> Tooltip.create(Component.translatable("flat_items.setting.enabled.tooltip")))
                    .create(x, layout.getHeaderHeight() + 8, 150, 20, Component.translatable("flat_items.setting.enabled"), (c, b) -> enabled = b));
            addRenderableWidget(CycleButton.onOffBuilder(affect3D)
                    .withTooltip(unused -> Tooltip.create(Component.translatable("flat_items.setting.affect_3d.tooltip")))
                    .create(x, layout.getHeaderHeight() + 36, 150, 20, Component.translatable("flat_items.setting.affect_3d"), (c, b) -> affect3D = b));
            addRenderableWidget(CycleButton.onOffBuilder(renderSides)
                    .withTooltip(unused -> Tooltip.create(Component.translatable("flat_items.setting.render_sides.tooltip")))
                    .create(x, layout.getHeaderHeight() + 64, 150, 20, Component.translatable("flat_items.setting.render_sides"), (c, b) -> renderSides = b));
            addRenderableWidget(CycleButton.onOffBuilder(enlarge3D)
                    .withTooltip(unused -> Tooltip.create(Component.translatable("flat_items.setting.enlarge_3d.tooltip")))
                    .create(x, layout.getHeaderHeight() + 92, 150, 20, Component.translatable("flat_items.setting.enlarge_3d"), (c, b) -> enlarge3D = b));
            addRenderableWidget(CycleButton.<Settings.Facing>builder(f -> Component.translatable("flat_items.setting.facing." + f.name().toLowerCase(Locale.ENGLISH)))
                    .withInitialValue(facing)
                    .withTooltip(unused -> Tooltip.create(Component.translatable("flat_items.setting.facing.tooltip")))
                    .withValues(Settings.Facing.values())
                    .create(x, layout.getHeaderHeight() + 120, 150, 20, Component.translatable("flat_items.setting.facing"), (c, f) -> facing = f));
            layout.addToFooter(Button.builder(CommonComponents.GUI_DONE, ignored -> onClose()).width(200).build());
            layout.addTitleHeader(Component.translatable("flat_items.configuration.title"), font);
            layout.visitWidgets(this::addRenderableWidget);
            layout.arrangeElements();
        }
    }
}
