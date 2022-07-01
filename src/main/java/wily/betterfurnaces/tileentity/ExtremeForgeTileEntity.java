package wily.betterfurnaces.tileentity;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraftforge.common.ForgeConfigSpec;
import wily.betterfurnaces.Config;
import wily.betterfurnaces.init.Registration;
import wily.betterfurnaces.inventory.ExtremeForgeContainer;

public class ExtremeForgeTileEntity extends AbstractForgeTileEntity {
    public ExtremeForgeTileEntity() {
        super(Registration.EXTREME_FORGE_TILE.get());
    }

    @Override
    public ForgeConfigSpec.IntValue getCookTimeConfig() {
        return Config.extremeTierSpeed;
    }

    @Override
    public Container IcreateMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        return new ExtremeForgeContainer(i, level, worldPosition, playerInventory, playerEntity, this.fields);
    }

}
