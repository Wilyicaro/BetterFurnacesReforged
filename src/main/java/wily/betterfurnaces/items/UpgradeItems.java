package wily.betterfurnaces.items;

import net.minecraft.world.entity.item.ItemEntity;

import java.util.function.Predicate;

public class UpgradeItems implements Predicate<ItemEntity> {

    @Override
    public boolean test(ItemEntity item) {

        return true;
    }

}
