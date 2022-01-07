package wily.ultimatefurnaces.init;

import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockDisplayReader;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import wily.betterfurnaces.items.ItemUpgradeColor;
import wily.betterfurnaces.tileentity.BlockForgeTileBase;
import wily.betterfurnaces.tileentity.BlockSmeltingTileBase;

import javax.annotation.Nullable;

public class BlockColorsHandler extends wily.betterfurnaces.init.BlockColorsHandler {
    public static final IBlockColor COLOR = new wily.betterfurnaces.init.BlockColorsHandler();

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
}
