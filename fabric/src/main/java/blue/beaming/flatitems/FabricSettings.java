package blue.beaming.flatitems;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record FabricSettings(boolean enabled, boolean affect3D, boolean renderSides, boolean enlarge3D, Facing facing) implements Settings {
    public static final Codec<FabricSettings> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.BOOL.fieldOf("enabled").forGetter(Settings::enabled),
            Codec.BOOL.fieldOf("affect_3d").forGetter(Settings::affect3D),
            Codec.BOOL.fieldOf("render_sides").forGetter(Settings::renderSides),
            Codec.BOOL.fieldOf("enlarge_3d").forGetter(Settings::enlarge3D),
            Codec.STRING.xmap(Facing::valueOf, Facing::name).fieldOf("facing").forGetter(Settings::facing)
    ).apply(instance, FabricSettings::new));
    public FabricSettings() {
        this(true, false, false, false, Facing.SCREEN);
    }
}