package wily.betterfurnaces.blocks;
import net.minecraft.world.level.block.EntityBlock;


public abstract class AbstractFurnaceBlock extends AbstractSmeltingBlock implements EntityBlock {


    public AbstractFurnaceBlock(Properties properties) {
        super(properties.destroyTime(3f));
        this.registerDefaultState(this.defaultBlockState().setValue(TYPE,0));
    }
}
