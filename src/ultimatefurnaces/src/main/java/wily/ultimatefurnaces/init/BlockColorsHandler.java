package wily.ultimatefurnaces.init;

import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import wily.betterfurnaces.items.ItemColorUpgrade;
import wily.betterfurnaces.blockentity.BlockForgeTileBase;
import wily.betterfurnaces.blockentity.BlockFurnaceTileBase;

import javax.annotation.Nullable;

public class BlockColorsHandler implements BlockColor {
    public static final BlockColor COLOR = new BlockColorsHandler();

    @SubscribeEvent
    public static void registerBlockColors() {
        System.out.println("Registering block color handler");

        Minecraft.getInstance().getBlockColors().register(COLOR, Registration.COPPER_FURNACE.get());
        Minecraft.getInstance().getBlockColors().register(COLOR, Registration.ULTIMATE_FURNACE.get());
        Minecraft.getInstance().getBlockColors().register(COLOR, Registration.COPPER_FORGE.get());
        Minecraft.getInstance().getBlockColors().register(COLOR, Registration.IRON_FORGE.get());
        Minecraft.getInstance().getBlockColors().register(COLOR, Registration.GOLD_FORGE.get());
        Minecraft.getInstance().getBlockColors().register(COLOR, Registration.DIAMOND_FORGE.get());
        Minecraft.getInstance().getBlockColors().register(COLOR, Registration.NETHERHOT_FORGE.get());
        Minecraft.getInstance().getBlockColors().register(COLOR, Registration.ULTIMATE_FORGE.get());
    }

    @Override
    public int getColor(BlockState blockState, @Nullable BlockAndTintGetter iBlockDisplayReader, @Nullable BlockPos blockPos, int i) {
        if (iBlockDisplayReader.getBlockEntity(blockPos) instanceof BlockFurnaceTileBase) {
            BlockFurnaceTileBase te = (BlockFurnaceTileBase) iBlockDisplayReader.getBlockEntity(blockPos);
            ItemStack stack = te.inventory.get(5);
            if (stack.getItem() instanceof ItemColorUpgrade && (stack.getTag() != null)) {
                return te.hex();
            }
        }
            if (iBlockDisplayReader.getBlockEntity(blockPos) instanceof BlockForgeTileBase) {
                BlockForgeTileBase te = (BlockForgeTileBase) iBlockDisplayReader.getBlockEntity(blockPos);
                    ItemStack stack = te.inventory.get(12);
                    if (stack.getItem() instanceof ItemColorUpgrade && (stack.getTag() != null)) {
                        return te.hex();
                    }
                }
        return 0xFFFFFF;
    }
}
