package wily.ultimatefurnaces.screens;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import wily.betterfurnaces.screens.AbstractFurnaceScreen;
import wily.ultimatefurnaces.inventory.AmethystFurnaceMenu;


public class AmethystFurnaceScreen extends AbstractFurnaceScreen<AmethystFurnaceMenu> {


    public AmethystFurnaceScreen(AmethystFurnaceMenu container, Inventory inv, Component name) {
        super(container, inv, name);
    }

}
