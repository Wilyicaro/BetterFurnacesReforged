package wily.betterfurnaces.tileentity;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraftforge.common.ForgeConfigSpec;
import wily.betterfurnaces.Config;
import wily.betterfurnaces.container.BlockIronFurnaceContainer;
import wily.betterfurnaces.init.Registration;

public class BlockIronFurnaceTile extends BlockSmeltingTileBase {
    public BlockIronFurnaceTile() {
        super(Registration.IRON_FURNACE_TILE.get(),6);
    }

    @Override
    public ForgeConfigSpec.IntValue getCookTimeConfig() {
        return Config.ironTierSpeed;
    }

    @Override
    public String IgetName() {
        return "block.betterfurnacesreforged.iron_furnace";
    }

    @Override
    public Container IcreateMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        return new BlockIronFurnaceContainer(i, level, worldPosition, playerInventory, playerEntity, this.fields);
    }

}
