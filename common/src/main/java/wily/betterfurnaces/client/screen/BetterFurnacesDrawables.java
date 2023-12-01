package wily.betterfurnaces.client.screen;

import net.minecraft.resources.ResourceLocation;
import wily.betterfurnaces.BetterFurnacesReforged;
import wily.factoryapi.base.Progress;
import wily.factoryapi.base.client.drawable.IFactoryDrawableType;

public class BetterFurnacesDrawables {
    public static final ResourceLocation WIDGETS = new ResourceLocation(BetterFurnacesReforged.MOD_ID + ":" + "textures/container/widgets.png");
    public static IFactoryDrawableType.DrawableProgress ENERGY_CELL = BFProgressType(Progress.Identifier.ENERGY_STORAGE,new int[]{240,0,16,34},false, IFactoryDrawableType.Direction.VERTICAL);
    public static IFactoryDrawableType.DrawableProgress THIN_ENERGY_CELL = BFProgressType(Progress.Identifier.ENERGY_STORAGE,new int[]{248,102,8,34},false, IFactoryDrawableType.Direction.VERTICAL);

    public static IFactoryDrawableType.DrawableImage MINI_FLUID_TANK =IFactoryDrawableType.create(WIDGETS,192,0,16,16);
    public static IFactoryDrawableType.DrawableImage FLUID_TANK = IFactoryDrawableType.create(WIDGETS,192,16,20,22);
    public static IFactoryDrawableType.DrawableImage FACTORY_UPGRADE_WINDOW = IFactoryDrawableType.create(WIDGETS,0,47,58, 107);
    public static IFactoryDrawableType SLOT = IFactoryDrawableType.create(WIDGETS,192,60,18,18);
    public static IFactoryDrawableType BIG_SLOT = IFactoryDrawableType.create(WIDGETS,210,60,26,26);
    public static IFactoryDrawableType INPUT_SLOT_OUTLINE = IFactoryDrawableType.create(WIDGETS,0,171,18,18);
    public static IFactoryDrawableType FUEL_SLOT_OUTLINE = IFactoryDrawableType.create(WIDGETS,18,171,18,18);
    public static IFactoryDrawableType OUTPUT_SLOT_OUTLINE = IFactoryDrawableType.create(WIDGETS,36,171,18,18);
    public static IFactoryDrawableType BIG_OUTPUT_SLOT_OUTLINE = IFactoryDrawableType.create(WIDGETS,0,203,26,26);
    public static IFactoryDrawableType VANILLA_BUTTON = IFactoryDrawableType.create(new ResourceLocation( "widget/button"),200,20,true);
    public static IFactoryDrawableType VANILLA_BUTTON_BACKGROUND = IFactoryDrawableType.create(new ResourceLocation( "widget/button_disabled"),200,20,true);
    public static IFactoryDrawableType BUTTON = getButton(6);
    public static IFactoryDrawableType SURFACE_BUTTON = getButton(7);
    public static IFactoryDrawableType FACTORY_BUTTON = IFactoryDrawableType.create(WIDGETS,0,28,18,14);
    public static IFactoryDrawableType getButtonIcon(int id){return IFactoryDrawableType.create(WIDGETS,id*14,189,14,14);}
    public static IFactoryDrawableType getButton(int id){return IFactoryDrawableType.create(WIDGETS,id* 14,157,14,14);}
    public static IFactoryDrawableType.DrawableProgress BFProgressType(Progress.Identifier identifier, int[] uvSize, boolean reverse, IFactoryDrawableType.Direction plane) {
        return IFactoryDrawableType.create(WIDGETS,uvSize[0],uvSize[1],uvSize[2],uvSize[3]).asProgress(identifier, reverse, plane);
    }

}
