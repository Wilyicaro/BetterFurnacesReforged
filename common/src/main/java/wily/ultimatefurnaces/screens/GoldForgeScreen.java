package wily.ultimatefurnaces.screens;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import wily.betterfurnaces.screens.AbstractForgeScreen;
import wily.ultimatefurnaces.inventory.GoldForgeMenu;


public class GoldForgeScreen extends AbstractForgeScreen<GoldForgeMenu> {


    public GoldForgeScreen(GoldForgeMenu container, Inventory inv, Component name) {
        super(container, inv, name);
    }

}
