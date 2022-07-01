package wily.betterfurnaces.screens;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import wily.betterfurnaces.inventory.ExtremeForgeMenu;

@OnlyIn(Dist.CLIENT)
public class ExtremeForgeScreen extends AbstractForgeScreen<ExtremeForgeMenu> {


    public ExtremeForgeScreen(ExtremeForgeMenu container, Inventory inv, Component name) {
        super(container, inv, name);
    }
}
