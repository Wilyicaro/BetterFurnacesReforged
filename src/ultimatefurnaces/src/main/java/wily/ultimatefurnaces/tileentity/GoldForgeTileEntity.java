package wily.ultimatefurnaces.tileentity;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraftforge.common.ForgeConfigSpec;
import wily.betterfurnaces.Config;
import wily.ultimatefurnaces.inventory.GoldForgeContainer;
import wily.betterfurnaces.tileentity.AbstractForgeTileEntity;
import wily.ultimatefurnaces.init.RegistrationUF;

public class GoldForgeTileEntity extends AbstractForgeTileEntity {
    public GoldForgeTileEntity() {
        super(RegistrationUF.GOLD_FORGE_TILE.get());
    }

    @Override
    public ForgeConfigSpec.IntValue getCookTimeConfig() {
        return Config.goldTierSpeed;
    }

    @Override
    public String IgetName() {
        return "block.ultimatefurnaces_bfr.gold_forge";
    }

    @Override
    public Container IcreateMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        return new GoldForgeContainer(i, level, worldPosition, playerInventory, playerEntity, this.fields);
    }
}
