package wily.ultimatefurnaces.init;

import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockDisplayReader;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import wily.betterfurnaces.items.ItemColorUpgrade;
import wily.betterfurnaces.tileentity.BlockForgeTileBase;
import wily.betterfurnaces.tileentity.BlockSmeltingTileBase;

import javax.annotation.Nullable;

public class BlockColorsHandler implements IBlockColor {
    public static final IBlockColor COLOR = new BlockColorsHandler();

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
    public int getColor(BlockState blockState, @Nullable IBlockDisplayReader iBlockDisplayReader, @Nullable BlockPos blockPos, int i) {
        if (iBlockDisplayReader.getBlockEntity(blockPos) instanceof BlockSmeltingTileBase) {
            BlockSmeltingTileBase te = (BlockSmeltingTileBase) iBlockDisplayReader.getBlockEntity(blockPos);
            ItemStack stack = te.inventory.get(te.UPGRADECOLOR());
            if (stack.getItem() instanceof ItemColorUpgrade && (stack.getTag() != null)) {
                return te.hex(te.UPGRADECOLOR());
            }
        }
        return 0xFFFFFF;
    }
}
