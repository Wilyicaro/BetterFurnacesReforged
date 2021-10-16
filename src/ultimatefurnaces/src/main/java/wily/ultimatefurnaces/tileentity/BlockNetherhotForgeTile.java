package wily.ultimatefurnaces.tileentity;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraftforge.common.ForgeConfigSpec;
import wily.betterfurnaces.Config;
import wily.betterfurnaces.tileentity.BlockForgeTileBase;
import wily.ultimatefurnaces.container.BlockGoldForgeContainer;
import wily.ultimatefurnaces.container.BlockNetherhotForgeContainer;
import wily.ultimatefurnaces.init.Registration;

public class BlockNetherhotForgeTile extends BlockForgeTileBase {
    public BlockNetherhotForgeTile() {
        super(Registration.NETHERHOT_FORGE_TILE.get());
    }

    @Override
    public ForgeConfigSpec.IntValue getCookTimeConfig() {
        return Config.netherhotTierSpeed;
    }

    @Override
    public String IgetName() {
        return "block.ultimatefurnaces_bfr.netherhot_forge";
    }

    @Override
    public Container IcreateMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        return new BlockNetherhotForgeContainer(i, level, worldPosition, playerInventory, playerEntity, this.fields);
    }
}
