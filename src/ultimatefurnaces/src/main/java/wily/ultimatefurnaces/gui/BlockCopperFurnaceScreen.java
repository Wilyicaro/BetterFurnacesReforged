package wily.ultimatefurnaces.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import wily.ultimatefurnaces.container.BlockCopperFurnaceContainer;
import wily.ultimatefurnaces.container.BlockUltimateFurnaceContainer;
import wily.betterfurnaces.gui.BlockFurnaceScreenBase;

@OnlyIn(Dist.CLIENT)
public class BlockCopperFurnaceScreen extends BlockFurnaceScreenBase<BlockCopperFurnaceContainer> {


    public BlockCopperFurnaceScreen(BlockCopperFurnaceContainer container, PlayerInventory inv, ITextComponent name) {
        super(container, inv, name);
    }

}
