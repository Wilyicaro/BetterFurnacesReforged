package wily.betterfurnaces.items;

import wily.betterfurnaces.BetterFurnacesReforged;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

public class ItemCFurnace extends Item {
    public ItemCFurnace(String name) {
        this.setUnlocalizedName(BetterFurnacesReforged.MODID + "." + name);
        this.setRegistryName(new ResourceLocation(BetterFurnacesReforged.MODID, name));
        this.setMaxStackSize(1);
    }
}
