package wily.betterfurnaces.screens;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import wily.betterfurnaces.inventory.ExtremeFurnaceMenu;


public class ExtremeFurnaceScreen extends AbstractFurnaceScreen<ExtremeFurnaceMenu> {


    public ExtremeFurnaceScreen(ExtremeFurnaceMenu container, Inventory inv, Component name) {
        super(container, inv, name);
    }
}
