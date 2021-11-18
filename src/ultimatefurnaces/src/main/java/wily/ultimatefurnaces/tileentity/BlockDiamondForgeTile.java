package wily.ultimatefurnaces.tileentity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ForgeConfigSpec;
import wily.betterfurnaces.Config;
import wily.betterfurnaces.tileentity.BlockForgeTileBase;
import wily.ultimatefurnaces.container.BlockDiamondForgeContainer;
import wily.ultimatefurnaces.init.Registration;

public class BlockDiamondForgeTile extends BlockForgeTileBase {
    public BlockDiamondForgeTile(BlockPos pos, BlockState state) {
        super(Registration.DIAMOND_FORGE_TILE.get(), pos, state);
    }

    @Override
    public ForgeConfigSpec.IntValue getCookTimeConfig() {
        return Config.diamondTierSpeed;
    }

    @Override
    public String IgetName() {
        return "block.ultimatefurnaces_bfr.diamond_forge";
    }

    @Override
    public AbstractContainerMenu IcreateMenu(int i, Inventory playerInventory, Player playerEntity) {
        return new BlockDiamondForgeContainer(i, level, worldPosition, playerInventory, playerEntity, this.fields);
    }
}
