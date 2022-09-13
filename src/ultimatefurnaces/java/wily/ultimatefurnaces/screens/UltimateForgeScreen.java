package wily.ultimatefurnaces.screens;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import wily.betterfurnaces.screens.AbstractForgeScreen;
import wily.ultimatefurnaces.inventory.UltimateForgeMenu;

@OnlyIn(Dist.CLIENT)
public class UltimateForgeScreen extends AbstractForgeScreen<UltimateForgeMenu> {


    public UltimateForgeScreen(UltimateForgeMenu container, Inventory inv, Component name) {
        super(container, inv, name);
    }

}
