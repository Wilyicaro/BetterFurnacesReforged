package wily.betterfurnaces.items;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.world.World;
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
        CompoundNBT nbt;
        nbt = stack.getOrCreateTag();
        if (!(nbt.contains("Settings") && nbt.contains("AutoIO") && nbt.contains("Redstone"))) {
            FactoryUpgradeSettings settings = getSettings(stack);
            placeConfig(settings);
            stack.setTag(nbt);
        }
    }
    public FactoryUpgradeSettings getSettings(ItemStack stack){
        return new FactoryUpgradeSettings(stack);
    }

    public void placeConfig(FactoryUpgradeSettings furnaceSettings) {

        if (furnaceSettings != null) {
            furnaceSettings.set(0, 2);
            furnaceSettings.set(1, 1);
            for (Direction dir : Direction.values()) {
                if (dir != Direction.DOWN && dir != Direction.UP) {
                    furnaceSettings.set(dir.ordinal(), 4);
                }
            }
        }

    }
}
