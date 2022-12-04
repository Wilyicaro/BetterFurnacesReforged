package wily.betterfurnaces.screens;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import wily.betterfurnaces.inventory.GoldFurnaceMenu;

public class GoldFurnaceScreen extends AbstractFurnaceScreen<GoldFurnaceMenu> {


    public GoldFurnaceScreen(GoldFurnaceMenu container, Inventory inv, Component name) {
        super(container, inv, name);
    }
}
