package wily.betterfurnaces.client.screen;

import net.minecraft.resources.ResourceLocation;
import wily.betterfurnaces.BetterFurnacesReforged;
import wily.factoryapi.FactoryAPI;
import wily.factoryapi.base.client.drawable.IFactoryDrawableType;

public class BetterFurnacesDrawables {
    public static final ResourceLocation WIDGETS = BetterFurnacesReforged.createModLocation("textures/container/widgets.png");
    public static final IFactoryDrawableType.DrawableProgress ENERGY_CELL = createProgress(new int[]{240,0,16,34},false, IFactoryDrawableType.Direction.VERTICAL);
    public static final IFactoryDrawableType.DrawableProgress THIN_ENERGY_CELL = createProgress(new int[]{248,102,8,34},false, IFactoryDrawableType.Direction.VERTICAL);

    public static final IFactoryDrawableType.DrawableImage MINI_FLUID_TANK = IFactoryDrawableType.create(WIDGETS,192,0,16,16);
    public static final IFactoryDrawableType.DrawableImage FLUID_TANK = IFactoryDrawableType.create(WIDGETS,192,16,20,22);
    public static final IFactoryDrawableType.DrawableImage FACTORY_UPGRADE_WINDOW = IFactoryDrawableType.create(WIDGETS,0,47,58, 107);
    public static final IFactoryDrawableType SLOT = IFactoryDrawableType.create(WIDGETS,192,60,18,18);
    public static final IFactoryDrawableType BIG_SLOT = IFactoryDrawableType.create(WIDGETS,210,60,26,26);
    public static final IFactoryDrawableType INPUT_SLOT_OUTLINE = IFactoryDrawableType.create(WIDGETS,0,171,18,18);
    public static final IFactoryDrawableType FUEL_SLOT_OUTLINE = IFactoryDrawableType.create(WIDGETS,18,171,18,18);
    public static final IFactoryDrawableType OUTPUT_SLOT_OUTLINE = IFactoryDrawableType.create(WIDGETS,36,171,18,18);
    public static final IFactoryDrawableType BIG_OUTPUT_SLOT_OUTLINE = IFactoryDrawableType.create(WIDGETS,0,203,26,26);
    public static final IFactoryDrawableType VANILLA_BUTTON = /*? if <1.20.2 {*//*IFactoryDrawableType.create(FactoryAPI.createVanillaLocation("textures/gui/widgets.png"),0,66,200,20)*//*?} else {*/IFactoryDrawableType.create(FactoryAPI.createVanillaLocation("widget/button"),200,20,true)/*?}*/;
    public static final IFactoryDrawableType VANILLA_BUTTON_BACKGROUND = /*? if <1.20.2 {*//*IFactoryDrawableType.create(FactoryAPI.createVanillaLocation("textures/gui/widgets.png"),0,46,200,20)*//*?} else {*/IFactoryDrawableType.create(FactoryAPI.createVanillaLocation("widget/button_disabled"),200,20,true)/*?}*/;
    public static final IFactoryDrawableType BUTTON = getButton(6);
    public static final IFactoryDrawableType SURFACE_BUTTON = getButton(7);
    public static final IFactoryDrawableType FACTORY_BUTTON = IFactoryDrawableType.create(WIDGETS,0,28,18,14);

    public static IFactoryDrawableType getButtonIcon(int id){
        return IFactoryDrawableType.create(WIDGETS,id*14,189,14,14);
    }

    public static IFactoryDrawableType getButton(int id){
        return IFactoryDrawableType.create(WIDGETS,id * 14,157,14,14);
    }

    public static IFactoryDrawableType.DrawableProgress createProgress(int[] uvSize, boolean reverse, IFactoryDrawableType.Direction plane) {
        return IFactoryDrawableType.create(WIDGETS,uvSize[0],uvSize[1],uvSize[2],uvSize[3]).asProgress(reverse, plane);
    }
}