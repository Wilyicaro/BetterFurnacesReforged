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
import wily.ultimatefurnaces.inventory.NetherhotForgeMenu;

public class NetherhotForgeBlockEntity extends AbstractForgeBlockEntity {
    public NetherhotForgeBlockEntity(BlockPos pos, BlockState state) {
        super(RegistrationUF.NETHERHOT_FORGE_TILE.get(), pos, state);
    }

    @Override
    public int getCookTimeConfig() {
        return Config.netherhotTierSpeed;
    }

    @Override
    public AbstractContainerMenu IcreateMenu(int i, Inventory playerInventory, Player playerEntity) {
        return new NetherhotForgeMenu(i, level, worldPosition, playerInventory, playerEntity, this.fields);
    }
}
