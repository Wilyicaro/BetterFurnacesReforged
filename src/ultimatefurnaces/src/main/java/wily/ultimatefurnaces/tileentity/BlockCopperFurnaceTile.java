package wily.ultimatefurnaces.tileentity;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraftforge.common.ForgeConfigSpec;
import wily.betterfurnaces.Config;
import wily.betterfurnaces.tileentity.BlockFurnaceTileBase;
import wily.ultimatefurnaces.container.BlockCopperFurnaceContainer;
import wily.ultimatefurnaces.init.Registration;

public class BlockCopperFurnaceTile extends BlockFurnaceTileBase {
    public BlockCopperFurnaceTile() {
        super(Registration.COPPER_FURNACE_TILE.get());
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
        return new BlockCopperFurnaceContainer(i, level, worldPosition, playerInventory, playerEntity, this.fields);
    }
}
