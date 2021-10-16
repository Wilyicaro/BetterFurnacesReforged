package wily.betterfurnaces.tileentity;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraftforge.common.ForgeConfigSpec;
import wily.betterfurnaces.Config;
import wily.betterfurnaces.container.BlockExtremeForgeContainer;
import wily.betterfurnaces.init.Registration;

public class BlockExtremeForgeTile extends BlockForgeTileBase {
    public BlockExtremeForgeTile() {
        super(Registration.EXTREME_FORGE_TILE.get());
    }

    @Override
    public ForgeConfigSpec.IntValue getCookTimeConfig() {
        return Config.extremeTierSpeed;
    }

    @Override
    public String IgetName() {
        return "block.betterfurnacesreforged.extreme_forge";
    }

    @Override
    public Container IcreateMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        return new BlockExtremeForgeContainer(i, level, worldPosition, playerInventory, playerEntity, this.fields);
    }

}
