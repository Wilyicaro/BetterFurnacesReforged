package wily.ultimatefurnaces.screens;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import wily.ultimatefurnaces.inventory.IronForgeContainer;
import wily.betterfurnaces.screens.AbstractForgeScreen;

@OnlyIn(Dist.CLIENT)
public class IronForgeScreen extends AbstractForgeScreen<IronForgeContainer> {


    public IronForgeScreen(IronForgeContainer container, PlayerInventory inv, ITextComponent name) {
        super(container, inv, name);
    }

}
