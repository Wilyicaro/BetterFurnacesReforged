package wily.betterfurnaces.client.screen;

import net.minecraft.resources.ResourceLocation;
import wily.betterfurnaces.BetterFurnacesReforged;
import wily.factoryapi.base.FactoryDrawableType;
import wily.factoryapi.base.Progress;

public class BetterFurnacesDrawables {
    public static final ResourceLocation WIDGETS = new ResourceLocation(BetterFurnacesReforged.MOD_ID + ":" + "textures/container/widgets.png");
    public static FactoryDrawableType.DrawableProgress ENERGY_CELL = BFProgressType(Progress.Identifier.ENERGY_STORAGE,new int[]{240,0,16,34},false, FactoryDrawableType.Direction.VERTICAL);
    public static FactoryDrawableType.DrawableProgress THIN_ENERGY_CELL = BFProgressType(Progress.Identifier.ENERGY_STORAGE,new int[]{248,102,8,34},false, FactoryDrawableType.Direction.VERTICAL);

    public static FactoryDrawableType.DrawableImage MINI_FLUID_TANK =FactoryDrawableType.create(WIDGETS,192,0,16,16);
    public static FactoryDrawableType.DrawableImage FLUID_TANK = FactoryDrawableType.create(WIDGETS,192,16,20,22);
    public static FactoryDrawableType SLOT = FactoryDrawableType.create(WIDGETS,192,60,18,18);
    public static FactoryDrawableType BIG_SLOT = FactoryDrawableType.create(WIDGETS,210,60,26,26);
    public static FactoryDrawableType INPUT_SLOT_OUTLINE = FactoryDrawableType.create(WIDGETS,0,171,18,18);
    public static FactoryDrawableType FUEL_SLOT_OUTLINE = FactoryDrawableType.create(WIDGETS,18,171,18,18);
    public static FactoryDrawableType OUTPUT_SLOT_OUTLINE = FactoryDrawableType.create(WIDGETS,36,171,18,18);
    public static FactoryDrawableType BIG_OUTPUT_SLOT_OUTLINE = FactoryDrawableType.create(WIDGETS,0,203,26,26);
    public static FactoryDrawableType.DrawableProgress BFProgressType(Progress.Identifier identifier, int[] uvSize, boolean reverse, FactoryDrawableType.Direction plane) {
        return FactoryDrawableType.create(WIDGETS,uvSize[0],uvSize[1],uvSize[2],uvSize[3]).asProgress(identifier, reverse, plane);
    }

}
