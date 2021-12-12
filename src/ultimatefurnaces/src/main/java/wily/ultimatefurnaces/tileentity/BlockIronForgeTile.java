package wily.ultimatefurnaces.tileentity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ForgeConfigSpec;
import wily.betterfurnaces.Config;
import wily.betterfurnaces.blockentity.BlockForgeTileBase;
import wily.ultimatefurnaces.container.BlockIronForgeContainer;
import wily.ultimatefurnaces.init.Registration;

public class BlockIronForgeTile extends BlockForgeTileBase {
    public BlockIronForgeTile(BlockPos pos, BlockState state) {
        super(Registration.IRON_FORGE_TILE.get(), pos, state);
    }

    @Override
    public ForgeConfigSpec.IntValue getCookTimeConfig() {
        return Config.ironTierSpeed;
    }

    @Override
    public String IgetName() {
        return "block.ultimatefurnaces_bfr.iron_forge";
    }

    @Override
    public AbstractContainerMenu IcreateMenu(int i, Inventory playerInventory, Player playerEntity) {
        return new BlockIronForgeContainer(i, level, worldPosition, playerInventory, playerEntity, this.fields);
    }
}
