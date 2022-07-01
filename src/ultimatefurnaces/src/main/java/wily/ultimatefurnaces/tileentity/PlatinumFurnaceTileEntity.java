package wily.ultimatefurnaces.tileentity;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraftforge.common.ForgeConfigSpec;
import wily.betterfurnaces.Config;
import wily.betterfurnaces.tileentity.AbstractSmeltingTileEntity;
import wily.ultimatefurnaces.inventory.PlatinumFurnaceContainer;
import wily.ultimatefurnaces.init.RegistrationUF;

public class PlatinumFurnaceTileEntity extends AbstractSmeltingTileEntity {
    public PlatinumFurnaceTileEntity() {
        super(RegistrationUF.PLATINUM_FURNACE_TILE.get(),6);
    }

    @Override
    public ForgeConfigSpec.IntValue getCookTimeConfig() {
        return Config.platinumTierSpeed;
    }

    @Override
    public Container IcreateMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        return new PlatinumFurnaceContainer(i, level, worldPosition, playerInventory, playerEntity, this.fields);
    }
}
