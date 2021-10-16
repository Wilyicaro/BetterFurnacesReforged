package wily.ultimatefurnaces.tileentity;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.RecipeItemHelper;
import net.minecraft.util.Direction;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.ForgeConfigSpec;
import wily.betterfurnaces.Config;
import wily.ultimatefurnaces.container.BlockGoldForgeContainer;
import wily.betterfurnaces.tileentity.BlockForgeTileBase;
import wily.ultimatefurnaces.container.BlockIronForgeContainer;
import wily.ultimatefurnaces.init.Registration;

import javax.annotation.Nullable;

public class BlockIronForgeTile extends BlockForgeTileBase {
    public BlockIronForgeTile() {
        super(Registration.IRON_FORGE_TILE.get());
    }

    @Override
    public ForgeConfigSpec.IntValue getCookTimeConfig() {
        return Config.ironTierSpeed;
    }

    @Override
    public String IgetName() {
        return "block.ultimatefurnaces_bfr.iron_forge";
    }

    @Override
    public Container IcreateMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        return new BlockIronForgeContainer(i, level, worldPosition, playerInventory, playerEntity, this.fields);
    }
}
