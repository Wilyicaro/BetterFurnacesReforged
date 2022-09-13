package wily.ultimatefurnaces.screens;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import wily.betterfurnaces.screens.AbstractForgeScreen;
import wily.ultimatefurnaces.inventory.NetherhotForgeContainer;

@OnlyIn(Dist.CLIENT)
public class NetherhotForgeScreen extends AbstractForgeScreen<NetherhotForgeContainer> {


    public NetherhotForgeScreen(NetherhotForgeContainer container, PlayerInventory inv, ITextComponent name) {
        super(container, inv, name);
    }

}
