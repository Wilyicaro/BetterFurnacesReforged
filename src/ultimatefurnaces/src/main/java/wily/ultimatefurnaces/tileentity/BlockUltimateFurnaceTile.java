package wily.ultimatefurnaces.tileentity;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraftforge.common.ForgeConfigSpec;
import wily.betterfurnaces.Config;
import wily.ultimatefurnaces.container.BlockUltimateFurnaceContainer;
import wily.betterfurnaces.tileentity.BlockSmeltingTileBase;
import wily.ultimatefurnaces.init.Registration;

public class BlockUltimateFurnaceTile extends BlockSmeltingTileBase {
    public BlockUltimateFurnaceTile() {
        super(Registration.ULTIMATE_FURNACE_TILE.get(),6);
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
        return new BlockUltimateFurnaceContainer(i, level, worldPosition, playerInventory, playerEntity, this.fields);
    }
}
