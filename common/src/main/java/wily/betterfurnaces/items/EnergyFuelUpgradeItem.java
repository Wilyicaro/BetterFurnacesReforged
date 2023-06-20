package wily.betterfurnaces.items;

import me.shedaniel.architectury.platform.Platform;
import net.minecraft.network.chat.Component;
import wily.betterfurnaces.blockentity.InventoryBlockEntity;
import wily.betterfurnaces.init.Registration;

import java.util.List;

public class EnergyFuelUpgradeItem extends UpgradeItem {


    public EnergyFuelUpgradeItem(Properties properties, Component tooltip) {
        super(properties, 1, tooltip);
    }



    public boolean isUpgradeCompatibleWith(UpgradeItem upg) {
        return upg != Registration.GENERATOR.get();
    }
}
