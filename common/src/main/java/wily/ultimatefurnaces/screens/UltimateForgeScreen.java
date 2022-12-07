package wily.ultimatefurnaces.screens;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import wily.betterfurnaces.screens.AbstractForgeScreen;
import wily.ultimatefurnaces.inventory.UltimateForgeMenu;


public class UltimateForgeScreen extends AbstractForgeScreen<UltimateForgeMenu> {


    public UltimateForgeScreen(UltimateForgeMenu container, Inventory inv, Component name) {
        super(container, inv, name);
    }

}
