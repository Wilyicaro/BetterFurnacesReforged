package wily.betterfurnaces.init;

import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockDisplayReader;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import wily.betterfurnaces.tileentity.AbstractSmeltingTileEntity;

import javax.annotation.Nullable;

public class BlockColorsHandler implements IBlockColor {
    public static final IBlockColor COLOR = new BlockColorsHandler();

    @Override
    public int getColor(BlockState blockState, @Nullable IBlockDisplayReader iBlockDisplayReader, @Nullable BlockPos blockPos, int i) {
        if (iBlockDisplayReader != null && iBlockDisplayReader.getBlockEntity(blockPos) instanceof AbstractSmeltingTileEntity) {
            AbstractSmeltingTileEntity te = (AbstractSmeltingTileEntity) iBlockDisplayReader.getBlockEntity(blockPos);
            if (te.hasUpgrade(Registration.COLOR.get()) && (te.getUpgradeSlotItem(Registration.COLOR.get()).getTag() != null)) {
                return te.hex();
            }
        }
        return 0xFFFFFF;
    }
}
