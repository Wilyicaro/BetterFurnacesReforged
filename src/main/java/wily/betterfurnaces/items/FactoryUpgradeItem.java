package wily.betterfurnaces.items;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import wily.betterfurnaces.BetterFurnacesReforged;
import wily.betterfurnaces.tileentity.FactoryUpgradeSettings;

public class FactoryUpgradeItem extends UpgradeItem {
    public boolean canOutput;
    public boolean canInput;
    public boolean pipeSide;
    public boolean redstoneSignal;

    public FactoryUpgradeItem(Properties properties, String tooltip, boolean output, boolean input, boolean pipe, boolean redstone) {
        super(properties, 5, tooltip);
        canOutput = output;
        canInput = input;
        pipeSide = pipe;
        redstoneSignal = redstone;

    }

    public void inventoryTick(ItemStack stack, World world, Entity player, int slot, boolean selected) {
        super.inventoryTick(stack, world, player, slot, selected);
        if (!FactoryUpgradeSettings.containsAnyTag(stack)) createSettings(stack);

    }

    public FactoryUpgradeSettings createSettings(ItemStack stack){
        return new FactoryUpgradeSettings(()->stack);
    }

}
