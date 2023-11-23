package wily.betterfurnaces.blocks;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import wily.betterfurnaces.init.ModObjects;

import java.util.Collections;
import java.util.List;

public class BFRBlock extends Block {
    public Item.Properties itemProperties = ModObjects.defaultItemProperties();

    public BFRBlock(Properties properties) {
        super(properties.strength(3f));
    }
    @Override
    public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
        List<ItemStack> dropsOriginal = super.getDrops(state, builder);
        if (!dropsOriginal.isEmpty())
            return dropsOriginal;
        return Collections.singletonList(new ItemStack(this, 1));
    }
}
