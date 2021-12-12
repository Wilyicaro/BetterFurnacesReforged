package wily.ultimatefurnaces.tileentity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ForgeConfigSpec;
import wily.betterfurnaces.Config;
import wily.betterfurnaces.blockentity.BlockForgeTileBase;
import wily.ultimatefurnaces.container.BlockUltimateForgeContainer;
import wily.ultimatefurnaces.init.Registration;

public class BlockUltimateForgeTile extends BlockForgeTileBase {
    public BlockUltimateForgeTile(BlockPos pos, BlockState state) {
        super(Registration.ULTIMATE_FORGE_TILE.get(), pos, state);
    }

    @Override
    public ForgeConfigSpec.IntValue getCookTimeConfig() {
        return Config.ultimateTierSpeed;
    }

    @Override
    public String IgetName() {
        return "block.ultimatefurnaces_bfr.ultimate_forge";
    }

    @Override
    public AbstractContainerMenu IcreateMenu(int i, Inventory playerInventory, Player playerEntity) {
        return new BlockUltimateForgeContainer(i, level, worldPosition, playerInventory, playerEntity, this.fields);
    }
}
