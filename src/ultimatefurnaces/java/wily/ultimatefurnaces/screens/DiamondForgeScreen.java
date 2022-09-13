package wily.ultimatefurnaces.screens;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import wily.betterfurnaces.screens.AbstractForgeScreen;
import wily.ultimatefurnaces.inventory.DiamondForgeContainer;

@OnlyIn(Dist.CLIENT)
    public class DiamondForgeScreen extends AbstractForgeScreen<DiamondForgeContainer> {


    public DiamondForgeScreen(DiamondForgeContainer container, PlayerInventory inv, ITextComponent name) {
        super(container, inv, name);
    }

}
