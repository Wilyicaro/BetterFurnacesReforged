package wily.betterfurnaces.blocks;

import net.minecraft.state.IntegerProperty;

public abstract class AbstractFurnaceBlock extends AbstractSmeltingBlock {

    // 0= Furnace, 1= Blast Furnace, 2= Smoker
    public static final IntegerProperty TYPE = IntegerProperty.create("type", 0, 3);

    public AbstractFurnaceBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState().setValue(TYPE, 0));
    }
}
