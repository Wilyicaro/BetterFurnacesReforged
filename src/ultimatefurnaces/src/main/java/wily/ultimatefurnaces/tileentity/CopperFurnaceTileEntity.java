package wily.ultimatefurnaces.tileentity;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraftforge.common.ForgeConfigSpec;
import wily.betterfurnaces.Config;
import wily.betterfurnaces.tileentity.AbstractSmeltingTileEntity;
import wily.ultimatefurnaces.inventory.CopperFurnaceContainer;
import wily.ultimatefurnaces.init.RegistrationUF;

public class CopperFurnaceTileEntity extends AbstractSmeltingTileEntity {
    public CopperFurnaceTileEntity() {
        super(RegistrationUF.COPPER_FURNACE_TILE.get(),6);
    }

    @Override
    public ForgeConfigSpec.IntValue getCookTimeConfig() {
        return Config.copperTierSpeed;
    }

    @Override
    public String IgetName() {
        return "block.ultimatefurnaces_bfr.copper_furnace";
    }

    @Override
    public Container IcreateMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        return new CopperFurnaceContainer(i, level, worldPosition, playerInventory, playerEntity, this.fields);
    }
}
