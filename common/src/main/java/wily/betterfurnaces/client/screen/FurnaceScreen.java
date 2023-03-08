package wily.betterfurnaces.client.screen;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

import wily.betterfurnaces.inventory.SmeltingMenu;


public class FurnaceScreen extends SmeltingScreen<SmeltingMenu> {

    public FurnaceScreen(SmeltingMenu t, Inventory inv, Component name) {
        super(t, inv, name);
    }
}
