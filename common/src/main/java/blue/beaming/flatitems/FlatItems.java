package blue.beaming.flatitems;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FlatItems {
    public static final Logger log = LoggerFactory.getLogger("Flat Items");
    public static final String MOD_ID = "flat_items";

    public static Settings settings() {
        return settings;
    }

    private static Settings settings;

    public static void init(Settings s) {
        settings = s;
    }

    public static void updateSettings(Settings s) {
        settings = s;
    }
}