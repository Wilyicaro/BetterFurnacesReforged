package wily.betterfurnaces.blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;


public abstract class AbstractFurnaceBlock extends AbstractSmeltingBlock {


    public AbstractFurnaceBlock(BlockBehaviour.Properties properties) {
        super(properties.destroyTime(3f));
        this.registerDefaultState(this.defaultBlockState().setValue(TYPE,0));
    }
}
