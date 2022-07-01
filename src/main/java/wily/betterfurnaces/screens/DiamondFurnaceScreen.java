package wily.betterfurnaces.screens;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import wily.betterfurnaces.inventory.DiamondFurnaceMenu;

@OnlyIn(Dist.CLIENT)
public class DiamondFurnaceScreen extends AbstractFurnaceScreen<DiamondFurnaceMenu> {


    public DiamondFurnaceScreen(DiamondFurnaceMenu container, Inventory inv, Component name) {
        super(container, inv, name);
    }
}
