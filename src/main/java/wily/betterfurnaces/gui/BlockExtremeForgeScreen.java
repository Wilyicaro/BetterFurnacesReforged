package wily.betterfurnaces.gui;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import wily.betterfurnaces.container.ExtremeForgeMenu;

@OnlyIn(Dist.CLIENT)
public class BlockExtremeForgeScreen extends BlockForgeScreenBase<ExtremeForgeMenu> {


    public BlockExtremeForgeScreen(ExtremeForgeMenu container, Inventory inv, Component name) {
        super(container, inv, name);
    }
}
