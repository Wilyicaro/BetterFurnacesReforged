package wily.betterfurnaces.gui;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import wily.betterfurnaces.container.BlockNetherhotFurnaceContainer;

@OnlyIn(Dist.CLIENT)
public class BlockNetherhotFurnaceScreen extends BlockFurnaceScreenBase<BlockNetherhotFurnaceContainer> {


    public BlockNetherhotFurnaceScreen(BlockNetherhotFurnaceContainer container, PlayerInventory inv, ITextComponent name) {
        super(container, inv, name);
    }
}
