package wily.ultimatefurnaces.tileentity;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraftforge.common.ForgeConfigSpec;
import wily.betterfurnaces.Config;
import wily.betterfurnaces.tileentity.AbstractSmeltingTileEntity;
import wily.ultimatefurnaces.inventory.AmethystFurnaceContainer;
import wily.ultimatefurnaces.init.RegistrationUF;

public class AmethystFurnaceTileEntity extends AbstractSmeltingTileEntity {
    public AmethystFurnaceTileEntity() {
        super(RegistrationUF.AMETHYST_FURNACE_TILE.get(),6);
    }

    @Override
    public ForgeConfigSpec.IntValue getCookTimeConfig() {
        return Config.amethystTierSpeed;
    }

    @Override
    public Container IcreateMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        return new AmethystFurnaceContainer(i, level, worldPosition, playerInventory, playerEntity, this.fields);
    }
}
