package wily.ultimatefurnaces.screens;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import wily.betterfurnaces.screens.AbstractFurnaceScreen;
import wily.ultimatefurnaces.inventory.CopperFurnaceMenu;

@OnlyIn(Dist.CLIENT)
public class CopperFurnaceScreen extends AbstractFurnaceScreen<CopperFurnaceMenu> {


    public CopperFurnaceScreen(CopperFurnaceMenu container, Inventory inv, Component name) {
        super(container, inv, name);
    }

}
