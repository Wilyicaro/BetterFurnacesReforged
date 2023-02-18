package wily.betterfurnaces.screens;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

import wily.betterfurnaces.inventory.AbstractFurnaceMenu;


public abstract class AbstractFurnaceScreen<T extends AbstractFurnaceMenu> extends AbstractSmeltingScreen<T> {

    public AbstractFurnaceScreen(T t, Inventory inv, Component name) {
        super(t, inv, name);
    }
}
