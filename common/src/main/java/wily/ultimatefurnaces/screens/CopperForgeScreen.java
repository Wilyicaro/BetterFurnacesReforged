package wily.ultimatefurnaces.screens;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import wily.betterfurnaces.screens.AbstractForgeScreen;
import wily.ultimatefurnaces.inventory.CopperForgeMenu;


public class CopperForgeScreen extends AbstractForgeScreen<CopperForgeMenu> {


    public CopperForgeScreen(CopperForgeMenu container, Inventory inv, Component name) {
        super(container, inv, name);
    }

}
