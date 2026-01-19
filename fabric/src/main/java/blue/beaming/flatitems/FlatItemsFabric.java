package blue.beaming.flatitems;

import com.google.gson.JsonParser;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.JsonOps;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;

public class FlatItemsFabric implements ClientModInitializer {
    public static final Path SETTINGS_PATH = FabricLoader.getInstance().getConfigDir().resolve("flat_items.json");

    @Override public void onInitializeClient() {
        try (BufferedReader reader = Files.newBufferedReader(SETTINGS_PATH)) {
            var settings = FabricSettings.CODEC.decode(JsonOps.INSTANCE, JsonParser.parseReader(reader)).result()
                    .map(Pair::getFirst)
                    .orElse(new FabricSettings());
            FlatItems.init(settings);
        } catch (NoSuchFileException ignored) {
            FlatItems.init(new FabricSettings());
        } catch (IOException e) {
            FlatItems.log.error("Failed to load settings from {}", SETTINGS_PATH, e);
            FlatItems.init(new FabricSettings());
        }
    }

    public static void updateSettings(FabricSettings settings) {
        FlatItems.updateSettings(settings);
        try (BufferedWriter writer = Files.newBufferedWriter(SETTINGS_PATH)) {
            writer.write(FabricSettings.CODEC.encodeStart(JsonOps.INSTANCE, settings).getOrThrow(IOException::new).toString());
        } catch (IOException e) {
            FlatItems.log.error("Failed to write settings to {}", SETTINGS_PATH, e);
        }
    }
}
