package wily.betterfurnaces.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ForgeConfigSpec;
import wily.betterfurnaces.Config;
import wily.betterfurnaces.container.BlockExtremeForgeContainer;
import wily.betterfurnaces.init.Registration;

public class BlockEntityExtremeForge extends BlockEntityForgeBase {

    public static final String EXTREME_FORGE = "extreme_forge";

    public BlockEntityExtremeForge(BlockPos pos, BlockState state) {
        super(Registration.EXTREME_FORGE_TILE.get(), pos, state);
    }

    @Override
    public ForgeConfigSpec.IntValue getCookTimeConfig() {
        return Config.extremeTierSpeed;
    }

    @Override
    public AbstractContainerMenu IcreateMenu(int i, Inventory playerInventory, Player playerEntity) {
        return new BlockExtremeForgeContainer(i, level, worldPosition, playerInventory, playerEntity, this.fields);
    }

}
