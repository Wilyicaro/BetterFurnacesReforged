package wily.ultimatefurnaces.screens;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import wily.betterfurnaces.screens.AbstractForgeScreen;
import wily.ultimatefurnaces.inventory.CopperForgeContainer;

@OnlyIn(Dist.CLIENT)
public class CopperForgeScreen extends AbstractForgeScreen<CopperForgeContainer> {


    public CopperForgeScreen(CopperForgeContainer container, PlayerInventory inv, ITextComponent name) {
        super(container, inv, name);
    }

}
