package wily.betterfurnaces.tileentity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ForgeConfigSpec;
import wily.betterfurnaces.Config;
import wily.betterfurnaces.container.BlockDiamondFurnaceContainer;
import wily.betterfurnaces.init.Registration;

public class BlockDiamondFurnaceTile extends BlockFurnaceTileBase {
    public BlockDiamondFurnaceTile(BlockPos pos, BlockState state) {
        super(Registration.DIAMOND_FURNACE_TILE.get(), pos, state);
    }

    @Override
    public ForgeConfigSpec.IntValue getCookTimeConfig() {
        return Config.diamondTierSpeed;
    }

    @Override
    public String IgetName() {
        return "block.betterfurnacesreforged.diamond_furnace";
    }

    @Override
    public AbstractContainerMenu IcreateMenu(int i, Inventory playerInventory, Player playerEntity) {
        return new BlockDiamondFurnaceContainer(i, level, worldPosition, playerInventory, playerEntity, this.fields);
    }

}
