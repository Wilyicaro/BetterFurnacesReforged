package wily.betterfurnaces.tileentity;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraftforge.common.ForgeConfigSpec;
import wily.betterfurnaces.Config;
import wily.betterfurnaces.container.BlockDiamondFurnaceContainer;
import wily.betterfurnaces.init.Registration;

public class BlockDiamondFurnaceTile extends BlockFurnaceTileBase {
    public BlockDiamondFurnaceTile() {
        super(Registration.DIAMOND_FURNACE_TILE.get());
    }

    @Override
    public ForgeConfigSpec.IntValue getCookTimeConfig() {
        return Config.diamondTierSpeed;
    }

    @Override
    public String IgetName() {
        return "block.betterfurnacesreforged.diamond_furnace";
    }

    @Override
    public Container IcreateMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        return new BlockDiamondFurnaceContainer(i, level, worldPosition, playerInventory, playerEntity, this.fields);
    }

}
