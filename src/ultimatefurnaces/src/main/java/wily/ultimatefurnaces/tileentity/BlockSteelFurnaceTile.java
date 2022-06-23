package wily.ultimatefurnaces.tileentity;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraftforge.common.ForgeConfigSpec;
import wily.betterfurnaces.Config;
import wily.betterfurnaces.tileentity.BlockSmeltingTileBase;
import wily.ultimatefurnaces.container.BlockSteelFurnaceContainer;
import wily.ultimatefurnaces.init.RegistrationUF;

public class BlockSteelFurnaceTile extends BlockSmeltingTileBase {
    public BlockSteelFurnaceTile() {
        super(RegistrationUF.STEEL_FURNACE_TILE.get(),6);
    }

    @Override
    public ForgeConfigSpec.IntValue getCookTimeConfig() {
        return Config.steelTierSpeed;
    }

    @Override
    public Container IcreateMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        return new BlockSteelFurnaceContainer(i, level, worldPosition, playerInventory, playerEntity, this.fields);
    }
}
