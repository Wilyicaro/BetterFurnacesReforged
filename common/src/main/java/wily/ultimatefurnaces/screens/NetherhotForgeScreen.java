package wily.ultimatefurnaces.screens;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import wily.betterfurnaces.screens.AbstractForgeScreen;
import wily.ultimatefurnaces.inventory.NetherhotForgeMenu;


public class NetherhotForgeScreen extends AbstractForgeScreen<NetherhotForgeMenu> {


    public NetherhotForgeScreen(NetherhotForgeMenu container, Inventory inv, Component name) {
        super(container, inv, name);
    }

}
