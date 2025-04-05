package wily.betterfurnaces;


import com.mojang.serialization.Codec;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluid;
import wily.betterfurnaces.util.BFRComponents;
import wily.factoryapi.FactoryAPI;
import wily.factoryapi.FactoryAPIPlatform;
import wily.factoryapi.base.config.FactoryConfig;
import wily.factoryapi.base.config.FactoryConfigControl;
import wily.factoryapi.base.config.FactoryConfigDisplay;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class BFRConfig {
    public static final List<ResourceLocation> supportedLiquidXps = new ArrayList<>(List.of(FactoryAPI.createLocation("mob_grinding_utils","fluid_xp"),FactoryAPI.createLocation("industrialforegoing:essence"),FactoryAPI.createLocation("cyclic","xpjuice"),FactoryAPI.createLocation("reliquary","xp_juice"),FactoryAPI.createLocation("kibe","liquid_xp")));

    public static ResourceLocation getLiquidXPType() {
        return xpFluidType.get();
    }
    public static Fluid getLiquidXP() {
        return FactoryAPIPlatform.getRegistryValue(getLiquidXPType(), BuiltInRegistries.FLUID);
    }

    public static String getLiquidXPMod() {
        return getLiquidXPType().getNamespace();
    }

    public static final Codec<List<ResourceLocation>> ID_LIST_CODEC = ResourceLocation.CODEC.listOf();
    public static final FactoryConfig.StorageHandler COMMON_STORAGE = new FactoryConfig.StorageHandler(true);
    public static final FactoryConfigControl.Int TIER_SPEED_CONTROL = new FactoryConfigControl.Int(1, ()-> 400, 400);

    public static <T> FactoryConfigDisplay<T> createSliderDisplay(String id){
        return new FactoryConfigDisplay.Instance<>(BFRComponents.optionsName(id), (c,v)-> Component.translatable("options.generic_value", c, v.toString()));
    }

    public static <T> FactoryConfigDisplay<T> createCyclingDisplay(String id){
        return new FactoryConfigDisplay.Instance<>(BFRComponents.optionsName(id),  (c,v)-> Component.literal(v.toString()));
    }

    public static final FactoryConfig<Boolean> checkUpdates = COMMON_STORAGE.register(FactoryConfig.create("checkUpdates", FactoryConfigDisplay.createToggle(BFRComponents.optionsName("checkUpdates")), FactoryConfigControl.TOGGLE, true, b-> {}, COMMON_STORAGE));
    public static final FactoryConfig<Integer> cacheCapacity = COMMON_STORAGE.register(FactoryConfig.create("cacheCapacity", createSliderDisplay("cacheCapacity"), new FactoryConfigControl.Int(1, ()-> 100, 100), 10, v-> {}, COMMON_STORAGE));
    public static final FactoryConfig<Integer> furnaceXPDropValue = COMMON_STORAGE.register(FactoryConfig.create("experienceDropValue", createSliderDisplay("experienceDropValue"), new FactoryConfigControl.Int(1, ()-> 500, 500), 1, v-> {}, COMMON_STORAGE));
    public static final FactoryConfig<Integer> furnaceAccumulatedXPDropValue = COMMON_STORAGE.register(FactoryConfig.create("accumulatedExperienceDropValue", createSliderDisplay("accumulatedExperienceDropValue"), new FactoryConfigControl.Int(1, ()-> 1000000, 1000000), 10000, v-> {}, COMMON_STORAGE));
    public static final FactoryConfig<Boolean> checkCommonOresName = COMMON_STORAGE.register(FactoryConfig.create("checkCommonOresName", FactoryConfigDisplay.createToggle(BFRComponents.optionsName("checkCommonOresName")), FactoryConfigControl.TOGGLE, false, b-> {}, COMMON_STORAGE));
    public static final FactoryConfig<Boolean> checkRawOresName = COMMON_STORAGE.register(FactoryConfig.create("checkRawOresName", FactoryConfigDisplay.createToggle(BFRComponents.optionsName("checkRawOresName")), FactoryConfigControl.TOGGLE, false, b-> {}, COMMON_STORAGE));
    public static final FactoryConfig<Integer> copperTierSpeed = COMMON_STORAGE.register(FactoryConfig.create("copperTierSpeed", createSliderDisplay("copperTierSpeed"), TIER_SPEED_CONTROL, 175, v-> {}, COMMON_STORAGE));
    public static final FactoryConfig<Integer> ironTierSpeed = COMMON_STORAGE.register(FactoryConfig.create("ironTierSpeed", createSliderDisplay("ironTierSpeed"), TIER_SPEED_CONTROL, 150, v-> {}, COMMON_STORAGE));
    public static final FactoryConfig<Integer> steelTierSpeed = COMMON_STORAGE.register(FactoryConfig.create("steelTierSpeed", createSliderDisplay("steelTierSpeed"), TIER_SPEED_CONTROL, 125, v-> {}, COMMON_STORAGE));
    public static final FactoryConfig<Integer> goldTierSpeed = COMMON_STORAGE.register(FactoryConfig.create("goldTierSpeed", createSliderDisplay("goldTierSpeed"), TIER_SPEED_CONTROL, 100, v-> {}, COMMON_STORAGE));
    public static final FactoryConfig<Integer> amethystTierSpeed = COMMON_STORAGE.register(FactoryConfig.create("amethystTierSpeed", createSliderDisplay("amethystTierSpeed"), TIER_SPEED_CONTROL, 75, v-> {}, COMMON_STORAGE));
    public static final FactoryConfig<Integer> diamondTierSpeed = COMMON_STORAGE.register(FactoryConfig.create("diamondTierSpeed", createSliderDisplay("diamondTierSpeed"), TIER_SPEED_CONTROL, 50, v-> {}, COMMON_STORAGE));
    public static final FactoryConfig<Integer> platinumTierSpeed = COMMON_STORAGE.register(FactoryConfig.create("platinumTierSpeed", createSliderDisplay("platinumTierSpeed"), TIER_SPEED_CONTROL, 25, v-> {}, COMMON_STORAGE));
    public static final FactoryConfig<Integer> netherhotTierSpeed = COMMON_STORAGE.register(FactoryConfig.create("netherhotTierSpeed", createSliderDisplay("netherhotTierSpeed"), TIER_SPEED_CONTROL, 8, v-> {}, COMMON_STORAGE));
    public static final FactoryConfig<Integer> extremeTierSpeed = COMMON_STORAGE.register(FactoryConfig.create("extremeTierSpeed", createSliderDisplay("extremeTierSpeed"), TIER_SPEED_CONTROL, 4, v-> {}, COMMON_STORAGE));
    public static final FactoryConfig<Integer> ultimateTierSpeed = COMMON_STORAGE.register(FactoryConfig.create("ultimateTierSpeed", createSliderDisplay("ultimateTierSpeed"), TIER_SPEED_CONTROL, 1, v-> {}, COMMON_STORAGE));
    public static final FactoryConfig<ResourceLocation> xpFluidType = COMMON_STORAGE.register(FactoryConfig.create("experienceFluidType", createCyclingDisplay("experienceFluidType"), new FactoryConfigControl.FromInt<>(supportedLiquidXps::get, supportedLiquidXps::indexOf, supportedLiquidXps::size), supportedLiquidXps.get(0), v-> {}, COMMON_STORAGE));
    public static final FactoryConfig<List<ResourceLocation>> additionalLiquidFuels = COMMON_STORAGE.register(FactoryConfig.create("additionalLiquidFuels", null, ()-> ID_LIST_CODEC, Collections.emptyList(), v-> {}, COMMON_STORAGE));

}
