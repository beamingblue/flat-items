package blue.beaming.flatitems;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@Mod(value = FlatItems.MOD_ID, dist = Dist.CLIENT) public class FlatItemsNeoForge {
    public FlatItemsNeoForge(ModContainer container) {
        container.registerConfig(ModConfig.Type.CLIENT, NeoForgeSettings.SPEC);
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
        FlatItems.init(NeoForgeSettings.SETTINGS);
    }
}