package wily.betterfurnaces.tileentity;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraftforge.common.ForgeConfigSpec;
import wily.betterfurnaces.Config;
import wily.betterfurnaces.container.BlockNetherhotFurnaceContainer;
import wily.betterfurnaces.init.Registration;

public class BlockNetherhotFurnaceTile extends BlockFurnaceTileBase {
    public BlockNetherhotFurnaceTile() {
        super(Registration.NETHERHOT_FURNACE_TILE.get());
    }

    @Override
    public ForgeConfigSpec.IntValue getCookTimeConfig() {
        return Config.netherhotTierSpeed;
    }

    @Override
    public String IgetName() {
        return "block.betterfurnacesreforged.netherhot_furnace";
    }

    @Override
    public Container IcreateMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        return new BlockNetherhotFurnaceContainer(i, level, worldPosition, playerInventory, playerEntity, this.fields);
    }

}
