package wily.betterfurnaces.items;

import net.minecraft.ChatFormatting;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import wily.betterfurnaces.BetterFurnacesReforged;
import wily.betterfurnaces.blockentity.FurnaceSettings;

import java.util.List;

public class ItemUpgradeFactory extends ItemUpgrade {
    public boolean canOutput;
    public boolean canInput;
    public boolean pipeSide;
    public boolean redstoneSignal;
    FurnaceSettings furnaceSettings;

    public ItemUpgradeFactory(Properties properties, String tooltip, boolean output, boolean input, boolean pipe, boolean redstone) {
        super(properties,5, tooltip);
        canOutput = output;
        canInput = input;
        pipeSide = pipe;
        redstoneSignal = redstone;
        furnaceSettings = new FurnaceSettings() {
        };
    }
    public void inventoryTick(ItemStack stack, Level world, Entity player, int slot, boolean selected) {
        super.inventoryTick(stack, world, player, slot, selected);
        CompoundTag nbt;
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
