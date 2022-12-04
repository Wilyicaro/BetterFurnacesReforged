package wily.betterfurnaces.screens;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import wily.betterfurnaces.inventory.NetherhotFurnaceMenu;


public class NetherhotFurnaceScreen extends AbstractFurnaceScreen<NetherhotFurnaceMenu> {


    public NetherhotFurnaceScreen(NetherhotFurnaceMenu container, Inventory inv, Component name) {
        super(container, inv, name);
    }
}
