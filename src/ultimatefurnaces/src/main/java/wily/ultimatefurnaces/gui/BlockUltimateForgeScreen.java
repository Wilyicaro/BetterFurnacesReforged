package wily.ultimatefurnaces.gui;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import wily.betterfurnaces.gui.BlockForgeScreenBase;
import wily.ultimatefurnaces.container.BlockNetherhotForgeContainer;
import wily.ultimatefurnaces.container.BlockUltimateForgeContainer;

@OnlyIn(Dist.CLIENT)
public class BlockUltimateForgeScreen extends BlockForgeScreenBase<BlockUltimateForgeContainer> {


    public BlockUltimateForgeScreen(BlockUltimateForgeContainer container, PlayerInventory inv, ITextComponent name) {
        super(container, inv, name);
    }

}
