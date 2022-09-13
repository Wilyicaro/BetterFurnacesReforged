package wily.ultimatefurnaces.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ForgeConfigSpec;
import wily.betterfurnaces.Config;
import wily.betterfurnaces.blockentity.AbstractForgeBlockEntity;
import wily.ultimatefurnaces.init.RegistrationUF;
import wily.ultimatefurnaces.inventory.DiamondForgeMenu;

public class DiamondForgeBlockEntity extends AbstractForgeBlockEntity {
    public DiamondForgeBlockEntity(BlockPos pos, BlockState state) {
        super(RegistrationUF.DIAMOND_FORGE_TILE.get(), pos, state);
    }

    @Override
    public ForgeConfigSpec.IntValue getCookTimeConfig() {
        return Config.diamondTierSpeed;
    }

    @Override
    public AbstractContainerMenu IcreateMenu(int i, Inventory playerInventory, Player playerEntity) {
        return new DiamondForgeMenu(i, level, worldPosition, playerInventory, playerEntity, this.fields);
    }
}
