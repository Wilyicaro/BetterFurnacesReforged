package wily.betterfurnaces.screens;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import wily.betterfurnaces.inventory.IronFurnaceMenu;


public class IronFurnaceScreen extends AbstractFurnaceScreen<IronFurnaceMenu> {


    public IronFurnaceScreen(IronFurnaceMenu container, Inventory inv, Component name) {
        super(container, inv, name);
    }
}
