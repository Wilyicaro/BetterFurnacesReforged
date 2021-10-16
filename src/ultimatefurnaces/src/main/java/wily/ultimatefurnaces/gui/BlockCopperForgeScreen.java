package wily.ultimatefurnaces.gui;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import wily.betterfurnaces.gui.BlockForgeScreenBase;
import wily.ultimatefurnaces.container.BlockCopperForgeContainer;

@OnlyIn(Dist.CLIENT)
public class BlockCopperForgeScreen extends BlockForgeScreenBase<BlockCopperForgeContainer> {


    public BlockCopperForgeScreen(BlockCopperForgeContainer container, PlayerInventory inv, ITextComponent name) {
        super(container, inv, name);
    }

}
