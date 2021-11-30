package wily.betterfurnaces.init;

import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import wily.betterfurnaces.tileentity.BlockForgeTileBase;
import wily.betterfurnaces.tileentity.BlockFurnaceTileBase;

import javax.annotation.Nullable;

public class BlockColorsHandler implements BlockColor {
    public static final BlockColor COLOR = new BlockColorsHandler();

    @SubscribeEvent
    public static void registerBlockColors() {
        System.out.println("Registering block color handler");

        Minecraft.getInstance().getBlockColors().register(COLOR, Registration.IRON_FURNACE.get());
        Minecraft.getInstance().getBlockColors().register(COLOR, Registration.GOLD_FURNACE.get());
        Minecraft.getInstance().getBlockColors().register(COLOR, Registration.DIAMOND_FURNACE.get());
        Minecraft.getInstance().getBlockColors().register(COLOR, Registration.NETHERHOT_FURNACE.get());
        Minecraft.getInstance().getBlockColors().register(COLOR, Registration.EXTREME_FURNACE.get());
        Minecraft.getInstance().getBlockColors().register(COLOR, Registration.EXTREME_FORGE.get());
    }

    @Override
    public int getColor(BlockState blockState, @Nullable BlockAndTintGetter iBlockDisplayReader, @Nullable BlockPos blockPos, int i) {
        if (iBlockDisplayReader.getBlockEntity(blockPos) instanceof BlockFurnaceTileBase) {
            BlockFurnaceTileBase te = (BlockFurnaceTileBase) iBlockDisplayReader.getBlockEntity(blockPos);
            ItemStack stack = te.inventory.get(5);
            if (stack.getItem() == Registration.COLOR.get() && (stack.getTag() != null)) {
                return te.hex();
            }
        }
            if (iBlockDisplayReader.getBlockEntity(blockPos) instanceof BlockForgeTileBase) {
                BlockForgeTileBase te = (BlockForgeTileBase) iBlockDisplayReader.getBlockEntity(blockPos);
                    ItemStack stack = te.inventory.get(12);
                    if (stack.getItem() == Registration.COLOR.get() && (stack.getTag() != null)) {
                        return te.hex();
                    }
                }
        return 0xFFFFFF;
    }
}
