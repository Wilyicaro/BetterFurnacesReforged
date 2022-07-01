package wily.ultimatefurnaces.tileentity;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraftforge.common.ForgeConfigSpec;
import wily.betterfurnaces.Config;
import wily.betterfurnaces.tileentity.AbstractForgeTileEntity;
import wily.ultimatefurnaces.inventory.NetherhotForgeContainer;
import wily.ultimatefurnaces.init.RegistrationUF;

public class NetherhotForgeTileEntity extends AbstractForgeTileEntity {
    public NetherhotForgeTileEntity() {
        super(RegistrationUF.NETHERHOT_FORGE_TILE.get());
    }

    @Override
    public ForgeConfigSpec.IntValue getCookTimeConfig() {
        return Config.netherhotTierSpeed;
    }

    @Override
    public String IgetName() {
        return "block.ultimatefurnaces_bfr.netherhot_forge";
    }

    @Override
    public Container IcreateMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        return new NetherhotForgeContainer(i, level, worldPosition, playerInventory, playerEntity, this.fields);
    }
}
