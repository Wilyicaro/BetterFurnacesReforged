package wily.ultimatefurnaces.screens;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import wily.betterfurnaces.screens.AbstractFurnaceScreen;
import wily.ultimatefurnaces.inventory.SteelFurnaceMenu;


public class SteelFurnaceScreen extends AbstractFurnaceScreen<SteelFurnaceMenu> {


    public SteelFurnaceScreen(SteelFurnaceMenu container, Inventory inv, Component name) {
        super(container, inv, name);
    }

}
