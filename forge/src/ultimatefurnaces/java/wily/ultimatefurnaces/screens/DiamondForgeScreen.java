package wily.ultimatefurnaces.screens;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import wily.betterfurnaces.screens.AbstractForgeScreen;
import wily.ultimatefurnaces.inventory.DiamondForgeMenu;

@OnlyIn(Dist.CLIENT)
    public class DiamondForgeScreen extends AbstractForgeScreen<DiamondForgeMenu> {


    public DiamondForgeScreen(DiamondForgeMenu container, Inventory inv, Component name) {
        super(container, inv, name);
    }

}
