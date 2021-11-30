package wily.betterfurnaces.gui;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import wily.betterfurnaces.container.BlockGoldFurnaceContainer;

@OnlyIn(Dist.CLIENT)
public class BlockGoldFurnaceScreen extends BlockFurnaceScreenBase<BlockGoldFurnaceContainer> {


    public BlockGoldFurnaceScreen(BlockGoldFurnaceContainer container, Inventory inv, Component name) {
        super(container, inv, name);
    }
}
