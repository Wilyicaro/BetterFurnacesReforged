package wily.betterfurnaces.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ForgeConfigSpec;
import wily.betterfurnaces.Config;
import wily.betterfurnaces.container.NetherhotFurnaceMenu;
import wily.betterfurnaces.init.Registration;

public class NetherhotFurnaceBlockEntity extends BlockEntitySmeltingBase {
    public NetherhotFurnaceBlockEntity(BlockPos pos, BlockState state) {
        super(Registration.NETHERHOT_FURNACE_TILE.get(), pos, state,6);
    }

    @Override
    public ForgeConfigSpec.IntValue getCookTimeConfig() {
        return Config.netherhotTierSpeed;
    }

    @Override
    public AbstractContainerMenu IcreateMenu(int i, Inventory playerInventory, Player playerEntity) {
        return new NetherhotFurnaceMenu(i, level, worldPosition, playerInventory, playerEntity, this.fields);
    }

}
