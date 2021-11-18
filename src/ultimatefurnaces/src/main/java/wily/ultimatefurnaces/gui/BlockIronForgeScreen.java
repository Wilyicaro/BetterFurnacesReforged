package wily.ultimatefurnaces.gui;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import wily.betterfurnaces.gui.BlockForgeScreenBase;
import wily.ultimatefurnaces.container.BlockIronForgeContainer;

@OnlyIn(Dist.CLIENT)
public class BlockIronForgeScreen extends BlockForgeScreenBase<BlockIronForgeContainer> {


    public BlockIronForgeScreen(BlockIronForgeContainer container, Inventory inv, Component name) {
        super(container, inv, name);
    }

}
