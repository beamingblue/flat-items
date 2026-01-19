package blue.beaming.flatitems;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record FabricSettings(boolean enabled, boolean affect3D, boolean renderSides) implements Settings {
    public static final Codec<FabricSettings> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.BOOL.fieldOf("enabled").forGetter(Settings::enabled),
            Codec.BOOL.fieldOf("affect_3d").forGetter(Settings::affect3D),
            Codec.BOOL.fieldOf("render_sides").forGetter(Settings::renderSides)
    ).apply(instance, FabricSettings::new));
    public FabricSettings() {
        this(true, false, false);
    }
}