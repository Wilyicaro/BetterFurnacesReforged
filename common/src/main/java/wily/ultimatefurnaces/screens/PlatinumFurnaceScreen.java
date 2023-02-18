package wily.ultimatefurnaces.screens;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import wily.betterfurnaces.screens.AbstractFurnaceScreen;
import wily.ultimatefurnaces.inventory.PlatinumFurnaceMenu;


public class PlatinumFurnaceScreen extends AbstractFurnaceScreen<PlatinumFurnaceMenu> {


    public PlatinumFurnaceScreen(PlatinumFurnaceMenu container, Inventory inv, Component name) {
        super(container, inv, name);
    }

}
