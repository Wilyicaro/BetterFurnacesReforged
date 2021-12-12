package wily.betterfurnaces.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import wily.betterfurnaces.init.Registration;
import wily.betterfurnaces.blockentity.BlockEntityExtremeForge;
import wily.betterfurnaces.blockentity.BlockEntityForgeBase;

import javax.annotation.Nullable;
import java.util.Random;

public class BlockExtremeForge extends BlockForgeBase {

    public static final String EXTREME_FORGE = "extreme_forge";

    public BlockExtremeForge(Properties properties) {
        super(properties);
    }
    @OnlyIn(Dist.CLIENT)
    @Override
    public void animateTick(BlockState state, Level world, BlockPos pos, Random rand) {
        if (state.getValue(BlockStateProperties.LIT)) {
            if (world.getBlockEntity(pos) == null)
            {
                return;
            }
            if (!(world.getBlockEntity(pos) instanceof BlockEntityForgeBase))
            {
                return;
            }
            BlockEntityForgeBase tile = ((BlockEntityForgeBase) world.getBlockEntity(pos));

            if (state.getValue(BlockStateProperties.FACING) == Direction.SOUTH){
                double d0 = (double) pos.getX() + 0.5D;
                double d1 = (double) pos.getY() + rand.nextDouble() * 6.0D / 16.0D;
                double d2 = (double) pos.getZ() + 0.5D;
                double d4 = rand.nextDouble() * 0.6D - 0.3D;
                if (rand.nextDouble() < 0.1D) {
                    world.playLocalSound(d0, pos.getY(), d2, SoundEvents.FURNACE_FIRE_CRACKLE, SoundSource.BLOCKS, 1.0F, 1.0F, false);
                }
                world.addParticle(ParticleTypes.SMOKE, d0 - 0.52D, d1, d2 + d4, 0.0D, 0.0D, 0.0D);
                world.addParticle(ParticleTypes.SOUL_FIRE_FLAME, d0 - 0.52D, d1, d2 + d4, 0.0D, 0.0D, 0.0D);
                world.addParticle(ParticleTypes.SMOKE, d0 + 0.52D, d1, d2 + d4, 0.0D, 0.0D, 0.0D);
                world.addParticle(ParticleTypes.SOUL_FIRE_FLAME, d0 + 0.52D, d1, d2 + d4, 0.0D, 0.0D, 0.0D);
                world.addParticle(ParticleTypes.SMOKE, d0 + d4, d1, d2 - 0.52D, 0.0D, 0.0D, 0.0D);
                world.addParticle(ParticleTypes.SOUL_FIRE_FLAME, d0 + d4, d1, d2 - 0.52D, 0.0D, 0.0D, 0.0D);
                world.addParticle(ParticleTypes.SMOKE, d0 + d4, d1, d2 + 0.52D, 0.0D, 0.0D, 0.0D);
                world.addParticle(ParticleTypes.SOUL_FIRE_FLAME, d0 + d4, d1, d2 + 0.52D, 0.0D, 0.0D, 0.0D);
            }else{
                for (int l = 0; l < 3; ++l) {
                    double d0 = (pos.getX() + rand.nextFloat());
                    double d1 = (pos.getY() + rand.nextFloat());
                    double d2 = (pos.getZ() + rand.nextFloat());
                    int i1 = rand.nextInt(2) * 2 - 1;
                    double d3 = (rand.nextFloat() - 0.5D) * 0.2D;
                    double d4 = (rand.nextFloat() - 0.5D) * 0.2D;
                    double d5 = (rand.nextFloat() - 0.5D) * 0.2D;
                    world.addParticle(ParticleTypes.DRIPPING_LAVA, d0, d1, d2, d3, d4, d5);

                }
            }
        }
    }
    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos p_153215_, BlockState p_153216_) {
        return new BlockEntityExtremeForge(p_153215_, p_153216_);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return createFurnaceTicker(level, type, Registration.EXTREME_FORGE_TILE.get());
    }
}
