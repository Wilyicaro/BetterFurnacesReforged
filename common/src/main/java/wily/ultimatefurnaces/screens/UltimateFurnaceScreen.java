package wily.ultimatefurnaces.screens;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import wily.betterfurnaces.screens.AbstractFurnaceScreen;
import wily.ultimatefurnaces.inventory.UltimateFurnaceMenu;


public class UltimateFurnaceScreen extends AbstractFurnaceScreen<UltimateFurnaceMenu> {


    public UltimateFurnaceScreen(UltimateFurnaceMenu container, Inventory inv, Component name) {
        super(container, inv, name);
    }

}
