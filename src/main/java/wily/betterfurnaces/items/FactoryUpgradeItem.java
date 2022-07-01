package wily.betterfurnaces.items;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.world.World;
import wily.betterfurnaces.tileentity.FurnaceSettings;

public class FactoryUpgradeItem extends UpgradeItem {
    public boolean canOutput;
    public boolean canInput;
    public boolean pipeSide;
    public boolean redstoneSignal;
    FurnaceSettings furnaceSettings;

    public FactoryUpgradeItem(Properties properties, String tooltip, boolean output, boolean input, boolean pipe, boolean redstone) {
        super(properties, 5, tooltip);
        canOutput = output;
        canInput = input;
        pipeSide = pipe;
        redstoneSignal = redstone;
        furnaceSettings = new FurnaceSettings() {
        };
    }

    public void inventoryTick(ItemStack stack, World world, Entity player, int slot, boolean selected) {
        super.inventoryTick(stack, world, player, slot, selected);
        CompoundNBT nbt;
        nbt = stack.getOrCreateTag();
        if (!(nbt.contains("Settings") && nbt.contains("AutoIO") && nbt.contains("Redstone"))) {
            placeConfig();
            furnaceSettings.write(nbt);
            stack.setTag(nbt);
        }
    }

    public void placeConfig() {

        if (this.furnaceSettings != null) {
            this.furnaceSettings.set(0, 2);
            this.furnaceSettings.set(1, 1);
            for (Direction dir : Direction.values()) {
                if (dir != Direction.DOWN && dir != Direction.UP) {
                    this.furnaceSettings.set(dir.ordinal(), 4);
                }
            }
        }

    }
}
