package wily.betterfurnaces.screens;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import wily.betterfurnaces.inventory.ExtremeForgeContainer;

@OnlyIn(Dist.CLIENT)
public class ExtremeForgeScreen extends AbstractForgeScreen<ExtremeForgeContainer> {


    public ExtremeForgeScreen(ExtremeForgeContainer container, PlayerInventory inv, ITextComponent name) {
        super(container, inv, name);
    }
}
