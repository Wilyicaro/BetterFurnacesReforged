package wily.betterfurnaces.screens;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

import wily.betterfurnaces.inventory.ExtremeForgeMenu;


public class ExtremeForgeScreen extends AbstractForgeScreen<ExtremeForgeMenu> {


    public ExtremeForgeScreen(ExtremeForgeMenu container, Inventory inv, Component name) {
        super(container, inv, name);
    }
}
