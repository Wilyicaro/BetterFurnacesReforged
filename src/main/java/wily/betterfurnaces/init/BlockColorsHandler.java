package wily.betterfurnaces.init;

import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import wily.betterfurnaces.blockentity.AbstractSmeltingBlockEntity;

import javax.annotation.Nullable;

public class BlockColorsHandler implements BlockColor {
    public static final BlockColor COLOR = new BlockColorsHandler();

    @Override
    public int getColor(BlockState blockState, @Nullable BlockAndTintGetter blockAndTintGetter, @Nullable BlockPos blockPos, int i) {
        if (blockAndTintGetter != null && blockAndTintGetter.getBlockEntity(blockPos) instanceof AbstractSmeltingBlockEntity) {
            AbstractSmeltingBlockEntity be = (AbstractSmeltingBlockEntity) blockAndTintGetter.getBlockEntity(blockPos);
            if (be.hasUpgrade(Registration.COLOR.get()) && (be.getUpgradeSlotItem(Registration.COLOR.get()).getTag() != null)) {
                return be.hex();
            }
        }
        return 0xFFFFFF;
    }
}
