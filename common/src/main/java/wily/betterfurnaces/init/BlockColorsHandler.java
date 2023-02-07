package wily.betterfurnaces.init;

import net.minecraft.client.color.block.BlockColor;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import wily.betterfurnaces.blockentity.AbstractSmeltingBlockEntity;

import org.jetbrains.annotations.Nullable;

public class BlockColorsHandler implements BlockColor {
    public static final BlockColor COLOR = new BlockColorsHandler();

    @Override
    public int getColor(BlockState blockState, @Nullable BlockAndTintGetter iBlockDisplayReader, @Nullable BlockPos blockPos, int i) {
        if (iBlockDisplayReader != null && iBlockDisplayReader.getBlockEntity(blockPos) instanceof AbstractSmeltingBlockEntity be) {
            if (be.hasUpgrade(Registration.COLOR.get()) && (be.getUpgradeSlotItem(Registration.COLOR.get()).getTag() != null)) {
                return be.hex();
            }
        }
        return 0xFFFFFF;
    }
}
