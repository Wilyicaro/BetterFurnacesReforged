package wily.betterfurnaces.client.screen;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import wily.betterfurnaces.inventory.SmeltingMenu;

public class FurnaceScreen extends SmeltingScreen<SmeltingMenu>{
    public FurnaceScreen(SmeltingMenu smeltingMenu, Inventory inv, Component name) {
        super(smeltingMenu, inv, name);
    }
}
