package wily.betterfurnaces.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import wily.betterfurnaces.tileentity.BlockExtremeForgeTile;
import wily.betterfurnaces.tileentity.BlockForgeTileBase;

import javax.annotation.Nullable;
import java.util.Random;

public class BlockExtremeForge extends BlockForgeBase {

    public static final String EXTREME_FORGE = "extreme_forge";

    public BlockExtremeForge(Properties properties) {
        super(properties);
    }
    @Override
    public int getHarvestLevel(BlockState state) {
        return 4;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new BlockExtremeForgeTile();
    }
}
