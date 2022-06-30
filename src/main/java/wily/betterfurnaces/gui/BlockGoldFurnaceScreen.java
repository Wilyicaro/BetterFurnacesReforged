package wily.betterfurnaces.gui;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import wily.betterfurnaces.container.GoldFurnaceMenu;

@OnlyIn(Dist.CLIENT)
public class BlockGoldFurnaceScreen extends BlockFurnaceScreenBase<GoldFurnaceMenu> {


    public BlockGoldFurnaceScreen(GoldFurnaceMenu container, Inventory inv, Component name) {
        super(container, inv, name);
    }
}
