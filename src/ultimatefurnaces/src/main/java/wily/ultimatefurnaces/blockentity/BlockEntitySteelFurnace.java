package wily.ultimatefurnaces.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ForgeConfigSpec;
import wily.betterfurnaces.Config;
import wily.betterfurnaces.blockentity.BlockEntitySmeltingBase;
import wily.ultimatefurnaces.container.BlockSteelFurnaceContainer;
import wily.ultimatefurnaces.init.RegistrationUF;

public class BlockEntitySteelFurnace extends BlockEntitySmeltingBase {
    public BlockEntitySteelFurnace(BlockPos pos, BlockState state) {
        super(RegistrationUF.STEEL_FURNACE_TILE.get(), pos, state,6);
    }

    @Override
    public ForgeConfigSpec.IntValue getCookTimeConfig() {
        return Config.steelTierSpeed;
    }

    @Override
    public AbstractContainerMenu IcreateMenu(int i, Inventory playerInventory, Player playerEntity) {
        return new BlockSteelFurnaceContainer(i, level, worldPosition, playerInventory, playerEntity, this.fields);
    }
}
