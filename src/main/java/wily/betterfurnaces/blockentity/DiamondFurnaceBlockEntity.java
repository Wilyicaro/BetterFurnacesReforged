package wily.betterfurnaces.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ForgeConfigSpec;
import wily.betterfurnaces.Config;
import wily.betterfurnaces.container.DiamondFurnaceMenu;
import wily.betterfurnaces.init.Registration;

public class DiamondFurnaceBlockEntity extends BlockEntitySmeltingBase {
    public DiamondFurnaceBlockEntity(BlockPos pos, BlockState state) {
        super(Registration.DIAMOND_FURNACE_TILE.get(), pos, state,6);
    }

    @Override
    public ForgeConfigSpec.IntValue getCookTimeConfig() {
        return Config.diamondTierSpeed;
    }

    @Override
    public AbstractContainerMenu IcreateMenu(int i, Inventory playerInventory, Player playerEntity) {
        return new DiamondFurnaceMenu(i, level, worldPosition, playerInventory, playerEntity, this.fields);
    }

}
