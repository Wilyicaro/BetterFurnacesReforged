package wily.betterfurnaces.gui;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import wily.betterfurnaces.container.BlockIronFurnaceContainer;

@OnlyIn(Dist.CLIENT)
public class BlockIronFurnaceScreen extends BlockFurnaceScreenBase<BlockIronFurnaceContainer> {


    public BlockIronFurnaceScreen(BlockIronFurnaceContainer container, Inventory inv, Component name) {
        super(container, inv, name);
    }
}
