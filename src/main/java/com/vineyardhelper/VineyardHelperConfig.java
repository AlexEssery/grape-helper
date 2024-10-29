package com.vineyardhelper;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("vineyardhelper")
public interface VineyardHelperConfig extends Config {

    @ConfigItem(
            keyName = "objectIds",
            name = "Object IDs",
            description = "Object ID to highlight (fixed to 3005)"
    )
    default int objectIds() {
        return 3005;
    }
}
