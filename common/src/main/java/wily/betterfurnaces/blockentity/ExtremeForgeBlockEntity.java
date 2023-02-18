package wily.betterfurnaces.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;
import wily.betterfurnaces.Config;
import wily.betterfurnaces.init.Registration;
import wily.betterfurnaces.inventory.ExtremeForgeMenu;

public class ExtremeForgeBlockEntity extends AbstractForgeBlockEntity {

    public static final String EXTREME_FORGE = "extreme_forge";

    public ExtremeForgeBlockEntity(BlockPos pos, BlockState state) {
        super(Registration.EXTREME_FORGE_TILE.get(), pos, state);
    }

    @Override
    public int getCookTimeConfig() {
        return Config.extremeTierSpeed;
    }

    @Override
    public AbstractContainerMenu IcreateMenu(int i, Inventory playerInventory, Player playerEntity) {
        return new ExtremeForgeMenu(i, level, worldPosition, playerInventory, playerEntity, this.fields);
    }

}
