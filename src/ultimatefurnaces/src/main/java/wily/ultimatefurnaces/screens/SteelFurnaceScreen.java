package wily.ultimatefurnaces.screens;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import wily.betterfurnaces.screens.AbstractFurnaceScreen;
import wily.ultimatefurnaces.inventory.SteelFurnaceContainer;

@OnlyIn(Dist.CLIENT)
public class SteelFurnaceScreen extends AbstractFurnaceScreen<SteelFurnaceContainer> {


    public SteelFurnaceScreen(SteelFurnaceContainer container, PlayerInventory inv, ITextComponent name) {
        super(container, inv, name);
    }

}
