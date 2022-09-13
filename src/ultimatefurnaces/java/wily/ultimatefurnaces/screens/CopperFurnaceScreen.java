package wily.ultimatefurnaces.screens;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import wily.ultimatefurnaces.inventory.CopperFurnaceContainer;
import wily.betterfurnaces.screens.AbstractFurnaceScreen;

@OnlyIn(Dist.CLIENT)
public class CopperFurnaceScreen extends AbstractFurnaceScreen<CopperFurnaceContainer> {


    public CopperFurnaceScreen(CopperFurnaceContainer container, PlayerInventory inv, ITextComponent name) {
        super(container, inv, name);
    }

}
