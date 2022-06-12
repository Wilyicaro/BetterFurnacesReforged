package wily.ultimatefurnaces.gui;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import wily.betterfurnaces.gui.BlockFurnaceScreenBase;
import wily.ultimatefurnaces.container.BlockPlatinumFurnaceContainer;

@OnlyIn(Dist.CLIENT)
public class BlockPlatinumFurnaceScreen extends BlockFurnaceScreenBase<BlockPlatinumFurnaceContainer> {


    public BlockPlatinumFurnaceScreen(BlockPlatinumFurnaceContainer container, Inventory inv, Component name) {
        super(container, inv, name);
    }

}
