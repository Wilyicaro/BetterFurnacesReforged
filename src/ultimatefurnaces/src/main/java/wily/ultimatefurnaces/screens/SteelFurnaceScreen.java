package wily.ultimatefurnaces.screens;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import wily.betterfurnaces.screens.AbstractFurnaceScreen;
import wily.ultimatefurnaces.inventory.SteelFurnaceMenu;

@OnlyIn(Dist.CLIENT)
public class SteelFurnaceScreen extends AbstractFurnaceScreen<SteelFurnaceMenu> {


    public SteelFurnaceScreen(SteelFurnaceMenu container, Inventory inv, Component name) {
        super(container, inv, name);
    }

}
