package wily.ultimatefurnaces.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ForgeConfigSpec;
import wily.betterfurnaces.Config;
import wily.betterfurnaces.blockentity.AbstractSmeltingBlockEntity;
import wily.ultimatefurnaces.init.RegistrationUF;
import wily.ultimatefurnaces.inventory.PlatinumFurnaceMenu;

public class PlatinumFurnaceBlockEntity extends AbstractSmeltingBlockEntity {
    public PlatinumFurnaceBlockEntity(BlockPos pos, BlockState state) {
        super(RegistrationUF.PLATINUM_FURNACE_TILE.get(), pos, state,6);
    }

    @Override
    public ForgeConfigSpec.IntValue getCookTimeConfig() {
        return Config.platinumTierSpeed;
    }

    @Override
    public AbstractContainerMenu IcreateMenu(int i, Inventory playerInventory, Player playerEntity) {
        return new PlatinumFurnaceMenu(i, level, worldPosition, playerInventory, playerEntity, this.fields);
    }
}
