package wily.ultimatefurnaces.screens;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import wily.betterfurnaces.screens.AbstractForgeScreen;
import wily.ultimatefurnaces.inventory.GoldForgeMenu;

@OnlyIn(Dist.CLIENT)
public class GoldForgeScreen extends AbstractForgeScreen<GoldForgeMenu> {


    public GoldForgeScreen(GoldForgeMenu container, Inventory inv, Component name) {
        super(container, inv, name);
    }

}
