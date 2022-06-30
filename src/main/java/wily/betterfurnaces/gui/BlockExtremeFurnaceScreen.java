package wily.betterfurnaces.gui;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import wily.betterfurnaces.container.ExtremeFurnaceMenu;

@OnlyIn(Dist.CLIENT)
public class BlockExtremeFurnaceScreen extends BlockFurnaceScreenBase<ExtremeFurnaceMenu> {


    public BlockExtremeFurnaceScreen(ExtremeFurnaceMenu container, Inventory inv, Component name) {
        super(container, inv, name);
    }
}
