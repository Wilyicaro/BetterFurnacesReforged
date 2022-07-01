package wily.ultimatefurnaces.tileentity;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraftforge.common.ForgeConfigSpec;
import wily.betterfurnaces.Config;
import wily.betterfurnaces.tileentity.AbstractForgeTileEntity;
import wily.ultimatefurnaces.inventory.DiamondForgeContainer;
import wily.ultimatefurnaces.init.RegistrationUF;

public class DiamondForgeTileEntity extends AbstractForgeTileEntity {
    public DiamondForgeTileEntity() {
        super(RegistrationUF.DIAMOND_FORGE_TILE.get());
    }

    @Override
    public ForgeConfigSpec.IntValue getCookTimeConfig() {
        return Config.diamondTierSpeed;
    }

    @Override
    public String IgetName() {
        return "block.ultimatefurnaces_bfr.diamond_forge";
    }

    @Override
    public Container IcreateMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        return new DiamondForgeContainer(i, level, worldPosition, playerInventory, playerEntity, this.fields);
    }
}
