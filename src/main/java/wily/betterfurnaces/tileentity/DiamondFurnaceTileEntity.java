package wily.betterfurnaces.tileentity;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraftforge.common.ForgeConfigSpec;
import wily.betterfurnaces.Config;
import wily.betterfurnaces.init.Registration;
import wily.betterfurnaces.inventory.DiamondFurnaceContainer;

public class DiamondFurnaceTileEntity extends AbstractSmeltingTileEntity {
    public DiamondFurnaceTileEntity() {
        super(Registration.DIAMOND_FURNACE_TILE.get(), 6);
    }

    @Override
    public ForgeConfigSpec.IntValue getCookTimeConfig() {
        return Config.diamondTierSpeed;
    }

    @Override
    public Container IcreateMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        return new DiamondFurnaceContainer(i, level, worldPosition, playerInventory, playerEntity, this.fields);
    }

}
