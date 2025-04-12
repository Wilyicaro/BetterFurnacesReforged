package wily.betterfurnaces.init;

import com.mojang.serialization.Codec;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
//? if >=1.20.5 {
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.util.ColorRGBA;
import net.minecraft.network.codec.StreamCodec;
//?}
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import wily.betterfurnaces.BFRConfig;
import wily.betterfurnaces.blockentity.FactoryUpgradeSettings;
import wily.betterfurnaces.blocks.*;
import wily.betterfurnaces.inventory.*;
import wily.betterfurnaces.items.*;
import wily.betterfurnaces.network.SliderColorSyncPayload;
import wily.betterfurnaces.recipes.CobblestoneGeneratorRecipe;
import wily.factoryapi.FactoryAPIPlatform;
import wily.factoryapi.base.FactoryExtraMenuSupplier;
import wily.factoryapi.base.RegisterListing;
import wily.factoryapi.util.ColorUtil;

import java.util.List;
import java.util.stream.IntStream;

import static wily.betterfurnaces.BetterFurnacesReforged.MOD_ID;
import static wily.betterfurnaces.init.Registration.*;

public class ModObjects {
    public static final RegisterListing<Block> FURNACES = FactoryAPIPlatform.createRegister(MOD_ID, BuiltInRegistries.BLOCK);
    public static final RegisterListing<Block> BLOCKS = FactoryAPIPlatform.createRegister(MOD_ID, BuiltInRegistries.BLOCK);
    public static final RegisterListing<Item> ITEMS = FactoryAPIPlatform.createRegister(MOD_ID, BuiltInRegistries.ITEM);

    public static void init() {
        FURNACES.register();
        BLOCKS.register();
        ITEMS.register();
    }

    public static final RegisterListing.Holder<CreativeModeTab> ITEM_GROUP = TABS.add("bfr_creative_tab",()-> new CreativeModeTab.Builder(CreativeModeTab.Row.TOP, 0).title(Component.translatable("itemGroup." + MOD_ID + ".tab")).icon(()-> ModObjects.EXTREME_FURNACE.get().asItem().getDefaultInstance()).displayItems(((itemDisplayParameters, output) -> ITEMS.forEach(h-> output.accept(h.get().getDefaultInstance())))).build());

    private static Item.Properties uniqueStackItemProperties(ResourceLocation id){
        return FactoryAPIPlatform.setupItemProperties(new Item.Properties(), id).stacksTo(1);
    }

    public static final RegisterListing.Holder<RecipeSerializer<CobblestoneGeneratorRecipe>> COB_GENERATION_SERIALIZER = RECIPES_SERIALIZERS.add("rock_generating", () -> CobblestoneGeneratorRecipe.SERIALIZER);

    public static final RegisterListing.Holder<RecipeType<CobblestoneGeneratorRecipe>> ROCK_GENERATING_RECIPE = RECIPES.add("rock_generating", () -> new RecipeType<>() {});

    public static final RegisterListing.Holder<MenuType<SmeltingMenu>> FURNACE_CONTAINER = CONTAINERS.add("furnace", () -> FactoryExtraMenuSupplier.createMenuType((windowId, inv, data) -> new SmeltingMenu(windowId, inv.player.level(), data.get().readBlockPos(), inv)));
    public static final RegisterListing.Holder<MenuType<ForgeMenu>> FORGE_CONTAINER = CONTAINERS.add("forge", () -> FactoryExtraMenuSupplier.createMenuType((windowId, inv, data) -> new ForgeMenu(windowId, inv.player.level(), data.get().readBlockPos(), inv)));
    public static final RegisterListing.Holder<MenuType<ColorUpgradeItem.ColorUpgradeMenu>> COLOR_UPGRADE_CONTAINER = CONTAINERS.add("color_upgrade", () -> new MenuType<>(ColorUpgradeItem.ColorUpgradeMenu::new, FeatureFlags.VANILLA_SET));

    public static final RegisterListing.Holder<SmeltingBlock> IRON_FURNACE = registerBlockItem(FURNACES.add("iron_furnace", id -> new SmeltingBlock(FactoryAPIPlatform.setupBlockProperties(propertiesOf(Blocks.IRON_BLOCK), id), new SmeltingBlock.Tier("iron", BFRConfig.ironTierSpeed))), ITEMS);
    public static final RegisterListing.Holder<SmeltingBlock> GOLD_FURNACE = registerBlockItem(FURNACES.add("gold_furnace", id -> new SmeltingBlock(FactoryAPIPlatform.setupBlockProperties(propertiesOf(Blocks.GOLD_BLOCK), id), new SmeltingBlock.Tier("gold", BFRConfig.goldTierSpeed))), ITEMS);
    public static final RegisterListing.Holder<SmeltingBlock> DIAMOND_FURNACE = registerBlockItem(FURNACES.add("diamond_furnace", id -> new SmeltingBlock(FactoryAPIPlatform.setupBlockProperties(propertiesOf(Blocks.DIAMOND_BLOCK).noOcclusion(), id), new SmeltingBlock.Tier("diamond", BFRConfig.diamondTierSpeed))), ITEMS);
    public static final RegisterListing.Holder<SmeltingBlock> NETHERHOT_FURNACE = registerBlockItem(FURNACES.add("netherhot_furnace", id -> new SmeltingBlock(FactoryAPIPlatform.setupBlockProperties(propertiesOf(Blocks.REDSTONE_BLOCK), id), new SmeltingBlock.Tier("netherhot", BFRConfig.netherhotTierSpeed))), ITEMS);
    public static final RegisterListing.Holder<SmeltingBlock> EXTREME_FURNACE = registerBlockItem(FURNACES.add("extreme_furnace", id -> new SmeltingBlock(FactoryAPIPlatform.setupBlockProperties(propertiesOf(Blocks.DIAMOND_BLOCK).strength(20.0F, 3000.0F), id), new SmeltingBlock.Tier("extreme", BFRConfig.extremeTierSpeed))), ITEMS);
    public static final RegisterListing.Holder<ForgeBlock> EXTREME_FORGE = registerBlockItem(BLOCKS.add("extreme_forge", id -> new ForgeBlock(FactoryAPIPlatform.setupBlockProperties(propertiesOf(Blocks.NETHERITE_BLOCK).strength(30.0F, 6000.0F), id), BFRConfig.extremeTierSpeed)), ITEMS);

    public static final RegisterListing.Holder<CobblestoneGeneratorBlock> COBBLESTONE_GENERATOR = registerBlockItem(BLOCKS.add(CobblestoneGeneratorBlock.COBBLESTONE_GENERATOR, () -> new CobblestoneGeneratorBlock(FactoryAPIPlatform.setupBlockProperties(propertiesOf(Blocks.BLACKSTONE), ModObjects.COBBLESTONE_GENERATOR))), ITEMS);
    public static final RegisterListing.Holder<MenuType<CobblestoneGeneratorMenu>> COB_GENERATOR_CONTAINER = CONTAINERS.add(CobblestoneGeneratorBlock.COBBLESTONE_GENERATOR, () -> FactoryExtraMenuSupplier.createMenuType((windowId, inv, data) -> new CobblestoneGeneratorMenu(windowId, inv.player.level(), data.get().readBlockPos(), inv)));

    public static final RegisterListing.Holder<FuelVerifierBlock> FUEL_VERIFIER = registerBlockItem(BLOCKS.add(FuelVerifierBlock.FUEL_VERIFIER, () -> new FuelVerifierBlock(FactoryAPIPlatform.setupBlockProperties(propertiesOf(Blocks.COBBLESTONE), ModObjects.FUEL_VERIFIER))), ITEMS);
    public static final RegisterListing.Holder<MenuType<FuelVerifierMenu>> FUEL_VERIFIER_CONTAINER = CONTAINERS.add(FuelVerifierBlock.FUEL_VERIFIER, () -> FactoryExtraMenuSupplier.createMenuType((windowId, inv, data) -> new FuelVerifierMenu(windowId, inv.player.level(), data.get().readBlockPos(), inv)));

    public static final RegisterListing.Holder<BFRBlock> IRON_CONDUCTOR_BLOCK = registerBlockItem(BLOCKS.add("iron_conductor_block", () -> new BFRBlock(FactoryAPIPlatform.setupBlockProperties(propertiesOf(Blocks.IRON_BLOCK).strength(8.0F, 20.0F), ModObjects.IRON_CONDUCTOR_BLOCK))), ITEMS);
    public static final RegisterListing.Holder<BFRBlock> GOLD_CONDUCTOR_BLOCK = registerBlockItem(BLOCKS.add("gold_conductor_block", () -> new BFRBlock(FactoryAPIPlatform.setupBlockProperties(propertiesOf(Blocks.GOLD_BLOCK).strength(8.0F, 20.0F), ModObjects.GOLD_CONDUCTOR_BLOCK))), ITEMS);
    public static final RegisterListing.Holder<BFRBlock> NETHERHOT_CONDUCTOR_BLOCK = registerBlockItem(BLOCKS.add("netherhot_conductor_block", () -> new BFRBlock(FactoryAPIPlatform.setupBlockProperties(propertiesOf(Blocks.REDSTONE_BLOCK).strength(10.0F, 40.0F), ModObjects.NETHERHOT_CONDUCTOR_BLOCK))), ITEMS);


    public static BlockBehaviour.Properties propertiesOf(Block block){
        return /*? if >1.20.1 {*/BlockBehaviour.Properties.ofFullCopy/*?} else {*//*BlockBehaviour.Properties.copy*//*?}*/(block);
    }

    public static final RegisterListing.Holder<TierUpgradeItem> IRON_UPGRADE = ITEMS.add("iron_upgrade", id -> new TierUpgradeItem(uniqueStackItemProperties(id), Blocks.FURNACE, IRON_FURNACE.get()));
    public static final RegisterListing.Holder<TierUpgradeItem> GOLD_UPGRADE = ITEMS.add("gold_upgrade", id -> new TierUpgradeItem(uniqueStackItemProperties(id), IRON_FURNACE.get(), GOLD_FURNACE.get()));
    public static final RegisterListing.Holder<TierUpgradeItem> DIAMOND_UPGRADE = ITEMS.add("diamond_upgrade", id -> new TierUpgradeItem(uniqueStackItemProperties(id), GOLD_FURNACE.get(), DIAMOND_FURNACE.get()));
    public static final RegisterListing.Holder<TierUpgradeItem> NETHERHOT_UPGRADE = ITEMS.add("netherhot_upgrade", id -> new TierUpgradeItem(uniqueStackItemProperties(id), DIAMOND_FURNACE.get(), NETHERHOT_FURNACE.get()));
    public static final RegisterListing.Holder<TierUpgradeItem> EXTREME_UPGRADE = ITEMS.add("extreme_upgrade", id -> new TierUpgradeItem(uniqueStackItemProperties(id), NETHERHOT_FURNACE.get(), EXTREME_FURNACE.get()));

    public static final RegisterListing.Holder<FuelEfficiencyUpgradeItem> FUEL = ITEMS.add("fuel_efficiency_upgrade", id -> new FuelEfficiencyUpgradeItem(uniqueStackItemProperties(id).durability(256),2));
    public static final RegisterListing.Holder<OreProcessingUpgradeItem> ORE_PROCESSING = ITEMS.add("ore_processing_upgrade", id -> new OreProcessingUpgradeItem(uniqueStackItemProperties(id).durability(128),2,true,false));
    public static final RegisterListing.Holder<OreProcessingUpgradeItem> RAW_ORE_PROCESSING = ITEMS.add("raw_ore_processing_upgrade", id -> new OreProcessingUpgradeItem(uniqueStackItemProperties(id),2,false,true));
    public static final RegisterListing.Holder<FuelEfficiencyUpgradeItem> ADVANCED_FUEL = ITEMS.add("advanced_fuel_efficiency_upgrade", id -> new FuelEfficiencyUpgradeItem(uniqueStackItemProperties(id),2));
    public static final RegisterListing.Holder<OreProcessingUpgradeItem> ADVANCED_ORE_PROCESSING = ITEMS.add("advanced_ore_processing_upgrade", id -> new OreProcessingUpgradeItem(uniqueStackItemProperties(id),2,true,false));
    public static final RegisterListing.Holder<FactoryUpgradeItem> FACTORY = ITEMS.add("factory_upgrade", id -> new FactoryUpgradeItem(uniqueStackItemProperties(id), "factory", true,true,true,true));
    public static final RegisterListing.Holder<FactoryUpgradeItem> PIPING = ITEMS.add("piping_upgrade", id -> new FactoryUpgradeItem(uniqueStackItemProperties(id), "piping", false,false,true,false));
    public static final RegisterListing.Holder<FactoryUpgradeItem> OUTPUT = ITEMS.add("autooutput_upgrade", id -> new FactoryUpgradeItem(uniqueStackItemProperties(id), "output", true,false,true,false));
    public static final RegisterListing.Holder<FactoryUpgradeItem> INPUT = ITEMS.add("autoinput_upgrade", id -> new FactoryUpgradeItem(uniqueStackItemProperties(id), "input", false,true,true,false));
    public static final RegisterListing.Holder<FactoryUpgradeItem> REDSTONE = ITEMS.add("redstone_signal_upgrade", id -> new FactoryUpgradeItem(uniqueStackItemProperties(id), "redstone", false,false,false,true));
    public static final RegisterListing.Holder<ColorUpgradeItem> COLOR = ITEMS.add("color_upgrade", id -> new ColorUpgradeItem(uniqueStackItemProperties(id),"color"));
    public static final RegisterListing.Holder<LiquidFuelUpgradeItem> LIQUID = ITEMS.add("liquid_fuel_upgrade", id -> new LiquidFuelUpgradeItem(uniqueStackItemProperties(id),"liquid"));
    public static final RegisterListing.Holder<EnergyFuelUpgradeItem> ENERGY = ITEMS.add("energy_upgrade", id -> new EnergyFuelUpgradeItem(uniqueStackItemProperties(id), Component.translatable("tooltip." + MOD_ID + ".upgrade.energy", FactoryAPIPlatform.getPlatformEnergyComponent().getString()).setStyle(Style.EMPTY.applyFormat((ChatFormatting.GRAY)))));
    public static final RegisterListing.Holder<XpTankUpgradeItem> XP = ITEMS.add("xp_tank_upgrade", id -> new XpTankUpgradeItem(uniqueStackItemProperties(id),"xp"));
    public static final RegisterListing.Holder<UpgradeItem> BLAST = ITEMS.add("blasting_upgrade", id -> new UpgradeItem(uniqueStackItemProperties(id), UpgradeItem.Type.MODE, "blasting"));
    public static final RegisterListing.Holder<UpgradeItem> SMOKE = ITEMS.add("smoking_upgrade", id -> new UpgradeItem(uniqueStackItemProperties(id), UpgradeItem.Type.MODE ,"smoking"));
    public static final RegisterListing.Holder<GeneratorUpgradeItem> GENERATOR = ITEMS.add("generator_upgrade", id -> new GeneratorUpgradeItem(uniqueStackItemProperties(id)));
    public static final RegisterListing.Holder<StorageUpgradeItem> STORAGE = ITEMS.add("storage_upgrade", id -> new StorageUpgradeItem(uniqueStackItemProperties(id)));


    //? if >=1.20.5 {
    public record BlockTint(int red, int green, int blue){
        public static final Codec<BlockTint> CODEC = ColorRGBA.CODEC.xmap(rgba-> new BlockTint(rgba.rgba()), b-> new ColorRGBA(b.toRGB()));
        public static final BlockTint WHITE = new BlockTint(255,255, 255);
        public BlockTint withChannel(SliderColorSyncPayload.Channel channel, int value){
            return switch (channel){
                case R -> new BlockTint(value, green, blue);
                case G -> new BlockTint(red, value, blue);
                case B -> new BlockTint(red, green, value);
            };
        }

        public int getChannel(SliderColorSyncPayload.Channel channel){
            return switch (channel){
                case R -> red;
                case G -> green;
                case B -> blue;
            };
        }

        public BlockTint(int rgb){
            this(ColorUtil.toInt(ColorUtil.getRed(rgb)),ColorUtil.toInt(ColorUtil.getGreen(rgb)),ColorUtil.toInt(ColorUtil.getBlue(rgb)));
        }

        public BlockTint(List<Integer> color){
            this(color.get(0), color.get(1), color.get(2));
        }

        public List<Integer> toList(){
            return List.of(red, green, blue);
        }

        public int toRGB(){
            return ColorUtil.colorFromInt(red, green, blue, 0);
        }

        public float[] toFloatArray(){
            return new float[]{red / 255f, green / 255f, blue / 255f};
        }
    }

    public static final RegisterListing.Holder<DataComponentType<BlockTint>> BLOCK_TINT = DATA_COMPONENTS.add("upgrade_color",()-> DataComponentType.<BlockTint>builder().persistent(BlockTint.CODEC).networkSynchronized(ByteBufCodecs.INT.map(BlockTint::new, BlockTint::toRGB)).build());

    public record UpgradeSetting(int[] values){

    }

    public static RegisterListing.Holder<DataComponentType<UpgradeSetting>> registerFactoryUpgradeSetting(String id){
        return DATA_COMPONENTS.add(id,()-> DataComponentType.<UpgradeSetting>builder().persistent(Codec.INT_STREAM.xmap(i-> new UpgradeSetting(i.toArray()), setting-> IntStream.of(setting.values()))).networkSynchronized(StreamCodec.of((buf, setting)-> buf.writeVarIntArray(setting.values()), buf-> new UpgradeSetting(buf.readVarIntArray()))).build());
    }

    public static final RegisterListing.Holder<DataComponentType<UpgradeSetting>> SIDES_SETTING = registerFactoryUpgradeSetting("sides_setting");
    public static final RegisterListing.Holder<DataComponentType<UpgradeSetting>> AUTOIO_SETTING = registerFactoryUpgradeSetting("auto_input_output_setting");
    public static final RegisterListing.Holder<DataComponentType<UpgradeSetting>> REDSTONE_SETTING = registerFactoryUpgradeSetting("redstone_setting");

    public static RegisterListing.Holder<DataComponentType<UpgradeSetting>> getSettingComponent(FactoryUpgradeSettings.Type type){
        return switch (type){
            case SIDES -> SIDES_SETTING;
            case AUTO_IO -> AUTOIO_SETTING;
            case REDSTONE -> REDSTONE_SETTING;
        };
    }
    //?}
}
