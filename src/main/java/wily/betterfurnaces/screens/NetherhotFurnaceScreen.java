package wily.betterfurnaces.screens;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import wily.betterfurnaces.inventory.NetherhotFurnaceContainer;

@OnlyIn(Dist.CLIENT)
public class NetherhotFurnaceScreen extends AbstractFurnaceScreen<NetherhotFurnaceContainer> {


    public NetherhotFurnaceScreen(NetherhotFurnaceContainer container, PlayerInventory inv, ITextComponent name) {
        super(container, inv, name);
    }
}
