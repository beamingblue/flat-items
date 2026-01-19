package blue.beaming.flatitems;

import net.neoforged.neoforge.common.ModConfigSpec;

public final class NeoForgeSettings implements Settings {
    public static final NeoForgeSettings SETTINGS;
    public static final ModConfigSpec SPEC;

    public final ModConfigSpec.BooleanValue enabled;
    public final ModConfigSpec.BooleanValue affect3D;
    public final ModConfigSpec.BooleanValue renderSides;

    private NeoForgeSettings(ModConfigSpec.Builder builder) {
        enabled = builder.comment("With this option enabled, items will be rendered in 2D and they will always face the player.")
                .translation("flat_items.setting.enabled")
                .define("enabled", true);
        affect3D = builder.comment("By default, only 2D items are affected. This option also renders 3D models (such as blocks) as flat.")
                .translation("flat_items.setting.affect_3d")
                .define("affect_3d", false);
        renderSides = builder.comment("Renders the sides of items whilst still making them face the player.")
                .translation("flat_items.setting.render_sides")
                .define("render_sides", false);
    }

    static {
        var p = new ModConfigSpec.Builder().configure(NeoForgeSettings::new);
        SETTINGS = p.getLeft();
        SPEC = p.getRight();
    }

    @Override public boolean enabled() {
        return enabled.getAsBoolean();
    }

    @Override public boolean affect3D() {
        return affect3D.getAsBoolean();
    }

    @Override public boolean renderSides() {
        return renderSides.getAsBoolean();
    }
}
