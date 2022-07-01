package wily.ultimatefurnaces.tileentity;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraftforge.common.ForgeConfigSpec;
import wily.betterfurnaces.Config;
import wily.betterfurnaces.tileentity.AbstractForgeTileEntity;
import wily.ultimatefurnaces.inventory.CopperForgeContainer;
import wily.ultimatefurnaces.init.RegistrationUF;

public class CopperForgeTileEntity extends AbstractForgeTileEntity {
    public CopperForgeTileEntity() {
        super(RegistrationUF.COPPER_FORGE_TILE.get());
    }

    @Override
    public ForgeConfigSpec.IntValue getCookTimeConfig() {
        return Config.copperTierSpeed;
    }

    @Override
    public String IgetName() {
        return "block.ultimatefurnaces_bfr.copper_forge";
    }

    @Override
    public Container IcreateMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        return new CopperForgeContainer(i, level, worldPosition, playerInventory, playerEntity, this.fields);
    }
}
