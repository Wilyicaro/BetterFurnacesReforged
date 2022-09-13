package wily.ultimatefurnaces.screens;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import wily.ultimatefurnaces.inventory.UltimateFurnaceContainer;
import wily.betterfurnaces.screens.AbstractFurnaceScreen;

@OnlyIn(Dist.CLIENT)
public class UltimateFurnaceScreen extends AbstractFurnaceScreen<UltimateFurnaceContainer> {


    public UltimateFurnaceScreen(UltimateFurnaceContainer container, PlayerInventory inv, ITextComponent name) {
        super(container, inv, name);
    }

}
