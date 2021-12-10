package wily.betterfurnaces.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ForgeConfigSpec;
import wily.betterfurnaces.Config;
import wily.betterfurnaces.container.BlockIronFurnaceContainer;
import wily.betterfurnaces.init.Registration;

public class BlockEntityIronFurnace extends BlockEntitySmeltingBase {
    public BlockEntityIronFurnace(BlockPos pos, BlockState state) {
        super(Registration.IRON_FURNACE_TILE.get(), pos, state,6);
    }

    @Override
    public ForgeConfigSpec.IntValue getCookTimeConfig() {
        return Config.ironTierSpeed;
    }

    @Override
    public String IgetName() {
        return "block.betterfurnacesreforged.iron_furnace";
    }

    @Override
    public AbstractContainerMenu IcreateMenu(int i, Inventory playerInventory, Player playerEntity) {
        return new BlockIronFurnaceContainer(i, level, worldPosition, playerInventory, playerEntity, this.fields);
    }

}
