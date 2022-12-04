package wily.betterfurnaces.items;

import dev.architectury.platform.Platform;
import net.minecraft.network.chat.Component;

public class EnergyFuelUpgradeItem extends UpgradeItem {


    public EnergyFuelUpgradeItem(Properties properties, Component tooltip) {
        super(properties, 1, tooltip);
    }

    @Override
    public boolean isEnabled() {
        return Platform.isForge() || Platform.isModLoaded("techreborn");
    }
}
