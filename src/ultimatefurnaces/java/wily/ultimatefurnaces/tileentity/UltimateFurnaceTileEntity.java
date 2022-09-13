package wily.ultimatefurnaces.tileentity;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraftforge.common.ForgeConfigSpec;
import wily.betterfurnaces.Config;
import wily.ultimatefurnaces.inventory.UltimateFurnaceContainer;
import wily.betterfurnaces.tileentity.AbstractSmeltingTileEntity;
import wily.ultimatefurnaces.init.RegistrationUF;

public class UltimateFurnaceTileEntity extends AbstractSmeltingTileEntity {
    public UltimateFurnaceTileEntity() {
        super(RegistrationUF.ULTIMATE_FURNACE_TILE.get(),6);
    }

    @Override
    public ForgeConfigSpec.IntValue getCookTimeConfig() {
        return Config.ultimateTierSpeed;
    }

    @Override
    public String IgetName() {
        return "block.ultimatefurnaces_bfr.ultimate_furnace";
    }

    @Override
    public Container IcreateMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        return new UltimateFurnaceContainer(i, level, worldPosition, playerInventory, playerEntity, this.fields);
    }
}
