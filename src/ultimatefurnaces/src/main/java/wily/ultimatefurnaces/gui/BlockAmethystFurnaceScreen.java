package wily.ultimatefurnaces.gui;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import wily.betterfurnaces.gui.BlockFurnaceScreenBase;
import wily.ultimatefurnaces.container.BlockAmethystFurnaceContainer;

@OnlyIn(Dist.CLIENT)
public class BlockAmethystFurnaceScreen extends BlockFurnaceScreenBase<BlockAmethystFurnaceContainer> {


    public BlockAmethystFurnaceScreen(BlockAmethystFurnaceContainer container, Inventory inv, Component name) {
        super(container, inv, name);
    }

}
