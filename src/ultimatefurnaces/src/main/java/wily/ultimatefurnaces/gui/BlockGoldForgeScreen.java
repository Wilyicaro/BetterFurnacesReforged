package wily.ultimatefurnaces.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import wily.ultimatefurnaces.container.BlockGoldForgeContainer;
import wily.betterfurnaces.gui.BlockForgeScreenBase;

@OnlyIn(Dist.CLIENT)
public class BlockGoldForgeScreen extends BlockForgeScreenBase<BlockGoldForgeContainer> {


    public BlockGoldForgeScreen(BlockGoldForgeContainer container, PlayerInventory inv, ITextComponent name) {
        super(container, inv, name);
    }

}
