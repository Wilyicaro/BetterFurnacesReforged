package wily.betterfurnaces.items;

import java.util.List;

import wily.betterfurnaces.BetterFurnacesReforged;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemUpgradeDamage extends ItemUpgrade {


    public ItemUpgradeDamage(String name, String tooltipKey, int Type, int damage) {
        super(name, tooltipKey, Type);
        this.setMaxDamage(damage);
    }

}
