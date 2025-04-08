package wily.ultimatefurnaces.init;



import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import wily.betterfurnaces.BetterFurnacesReforged;
import wily.betterfurnaces.BFRConfig;
import wily.betterfurnaces.blocks.ForgeBlock;
import wily.betterfurnaces.blocks.SmeltingBlock;
import wily.betterfurnaces.init.ModObjects;
import wily.betterfurnaces.items.OreProcessingUpgradeItem;
import wily.betterfurnaces.items.TierUpgradeItem;
import wily.factoryapi.FactoryAPIPlatform;
import wily.factoryapi.base.RegisterListing;

import static wily.betterfurnaces.init.ModObjects.propertiesOf;
import static wily.betterfurnaces.init.Registration.*;

public class ModObjectsUF {
    public static final RegisterListing<Block> FURNACES = FactoryAPIPlatform.createRegister(BetterFurnacesReforged.MOD_ID, BuiltInRegistries.BLOCK);
    public static final RegisterListing<Block> FORGES = FactoryAPIPlatform.createRegister(BetterFurnacesReforged.MOD_ID, BuiltInRegistries.BLOCK);
    public static final RegisterListing<Item> ITEMS = FactoryAPIPlatform.createRegister(BetterFurnacesReforged.MOD_ID, BuiltInRegistries.ITEM);

    public static void init() {
        FURNACES.register();
        FORGES.register();
        ITEMS.register();
    }

    private static Item.Properties uniqueStackItemProperties(RegisterListing.Holder<? extends Item> item){
        return FactoryAPIPlatform.setupItemProperties(new Item.Properties(), item).stacksTo(1);
    }

    public static final RegisterListing.Holder<CreativeModeTab> ITEM_GROUP = TABS.add("tab_ultimate_furnaces",()-> CreativeModeTab.builder(CreativeModeTab.Row.TOP, 0).displayItems((itemDisplayParameters, output) -> ITEMS.forEach(s-> output.accept(s.get()))).title(Component.translatable("itemGroup."+ BetterFurnacesReforged.MOD_ID + ".tab_ultimate")).icon(()-> ModObjectsUF.ULTIMATE_FURNACE.get().asItem().getDefaultInstance()).build());


    public static final RegisterListing.Holder<SmeltingBlock> COPPER_FURNACE = registerBlockItem(FURNACES.add("copper_furnace", () -> new SmeltingBlock(FactoryAPIPlatform.setupBlockProperties(propertiesOf(Blocks.COPPER_BLOCK).strength(10.0F, 20.0F), ModObjectsUF.COPPER_FURNACE), BFRConfig.copperTierSpeed)), ITEMS);
    public static final RegisterListing.Holder<SmeltingBlock> STEEL_FURNACE = registerBlockItem(FURNACES.add("steel_furnace", () -> new SmeltingBlock(FactoryAPIPlatform.setupBlockProperties(propertiesOf(Blocks.IRON_BLOCK).strength(20.0F, 60.0F), ModObjectsUF.STEEL_FURNACE), BFRConfig.steelTierSpeed)), ITEMS);
    public static final RegisterListing.Holder<SmeltingBlock> AMETHYST_FURNACE = registerBlockItem(FURNACES.add("amethyst_furnace", () -> new SmeltingBlock(FactoryAPIPlatform.setupBlockProperties(propertiesOf(Blocks.AMETHYST_BLOCK).strength(20.0F, 30.0F), ModObjectsUF.AMETHYST_FURNACE), BFRConfig.amethystTierSpeed)), ITEMS);
    public static final RegisterListing.Holder<SmeltingBlock> PLATINUM_FURNACE = registerBlockItem(FURNACES.add("platinum_furnace", () -> new SmeltingBlock(FactoryAPIPlatform.setupBlockProperties(propertiesOf(Blocks.RAW_GOLD_BLOCK).strength(30.0F, 60.0F), ModObjectsUF.PLATINUM_FURNACE), BFRConfig.platinumTierSpeed)), ITEMS);
    public static final RegisterListing.Holder<SmeltingBlock> ULTIMATE_FURNACE = registerBlockItem(FURNACES.add("ultimate_furnace", () -> new SmeltingBlock(FactoryAPIPlatform.setupBlockProperties(propertiesOf(Blocks.NETHERITE_BLOCK).strength(50.0F, 6000.0F), ModObjectsUF.ULTIMATE_FURNACE), BFRConfig.ultimateTierSpeed)), ITEMS);

    public static final RegisterListing.Holder<ForgeBlock> COPPER_FORGE = registerBlockItem(FORGES.add("copper_forge", () -> new ForgeBlock(FactoryAPIPlatform.setupBlockProperties(propertiesOf(Blocks.COAL_BLOCK), ModObjectsUF.COPPER_FORGE), BFRConfig.copperTierSpeed)), ITEMS);
    public static final RegisterListing.Holder<ForgeBlock> IRON_FORGE = registerBlockItem(FORGES.add("iron_forge", () -> new ForgeBlock(FactoryAPIPlatform.setupBlockProperties(propertiesOf(Blocks.IRON_BLOCK), ModObjectsUF.IRON_FORGE), BFRConfig.ironTierSpeed)), ITEMS);
    public static final RegisterListing.Holder<ForgeBlock> STEEL_FORGE = registerBlockItem(FORGES.add("steel_forge", () -> new ForgeBlock(FactoryAPIPlatform.setupBlockProperties(propertiesOf(Blocks.IRON_BLOCK), ModObjectsUF.STEEL_FORGE), BFRConfig.steelTierSpeed)), ITEMS);
    public static final RegisterListing.Holder<ForgeBlock> GOLD_FORGE = registerBlockItem(FORGES.add("gold_forge", () -> new ForgeBlock(FactoryAPIPlatform.setupBlockProperties(propertiesOf(Blocks.GOLD_BLOCK), ModObjectsUF.GOLD_FORGE), BFRConfig.goldTierSpeed)), ITEMS);
    public static final RegisterListing.Holder<ForgeBlock> AMETHYST_FORGE = registerBlockItem(FORGES.add("amethyst_forge", () -> new ForgeBlock(FactoryAPIPlatform.setupBlockProperties(propertiesOf(Blocks.DIAMOND_BLOCK), ModObjectsUF.AMETHYST_FORGE), BFRConfig.amethystTierSpeed)), ITEMS);
    public static final RegisterListing.Holder<ForgeBlock> DIAMOND_FORGE = registerBlockItem(FORGES.add("diamond_forge", () -> new ForgeBlock(FactoryAPIPlatform.setupBlockProperties(propertiesOf(Blocks.DIAMOND_BLOCK), ModObjectsUF.DIAMOND_FORGE), BFRConfig.diamondTierSpeed)), ITEMS);
    public static final RegisterListing.Holder<ForgeBlock> PLATINUM_FORGE = registerBlockItem(FORGES.add("platinum_forge", () -> new ForgeBlock(FactoryAPIPlatform.setupBlockProperties(propertiesOf(Blocks.DIAMOND_BLOCK), ModObjectsUF.PLATINUM_FORGE), BFRConfig.platinumTierSpeed)), ITEMS);
    public static final RegisterListing.Holder<ForgeBlock> NETHERHOT_FORGE = registerBlockItem(FORGES.add("netherhot_forge", () -> new ForgeBlock(FactoryAPIPlatform.setupBlockProperties(propertiesOf(Blocks.DIAMOND_BLOCK), ModObjectsUF.NETHERHOT_FORGE), BFRConfig.netherhotTierSpeed)), ITEMS);
    public static final RegisterListing.Holder<ForgeBlock> ULTIMATE_FORGE = registerBlockItem(FORGES.add("ultimate_forge", () -> new ForgeBlock(FactoryAPIPlatform.setupBlockProperties(propertiesOf(Blocks.NETHERITE_BLOCK).strength(50.0F, 6000.0F), ModObjectsUF.ULTIMATE_FORGE), BFRConfig.ultimateTierSpeed)), ITEMS);


    public static final RegisterListing.Holder<TierUpgradeItem> COPPER_UPGRADE = ITEMS.add("copper_upgrade", () -> new TierUpgradeItem(uniqueStackItemProperties(ModObjectsUF.COPPER_UPGRADE), Blocks.FURNACE, COPPER_FURNACE.get()));
    public static final RegisterListing.Holder<TierUpgradeItem> IRON_UPGRADE = ITEMS.add("copper_iron_upgrade", () -> new TierUpgradeItem(uniqueStackItemProperties(ModObjectsUF.IRON_UPGRADE), COPPER_FURNACE.get(), ModObjects.IRON_FURNACE.get()));
    public static final RegisterListing.Holder<TierUpgradeItem> STEEL_UPGRADE = ITEMS.add("steel_upgrade", () -> new TierUpgradeItem(uniqueStackItemProperties(ModObjectsUF.STEEL_UPGRADE), ModObjects.IRON_FURNACE.get(), STEEL_FURNACE.get()));
    public static final RegisterListing.Holder<TierUpgradeItem> GOLD_UPGRADE = ITEMS.add("steel_gold_upgrade", () -> new TierUpgradeItem(uniqueStackItemProperties(ModObjectsUF.GOLD_UPGRADE), STEEL_FURNACE.get(), ModObjects.GOLD_FURNACE.get()));
    public static final RegisterListing.Holder<TierUpgradeItem> AMETHYST_UPGRADE = ITEMS.add("amethyst_upgrade", () -> new TierUpgradeItem(uniqueStackItemProperties(ModObjectsUF.AMETHYST_UPGRADE), ModObjects.GOLD_FURNACE.get(), AMETHYST_FURNACE.get()));
    public static final RegisterListing.Holder<TierUpgradeItem> DIAMOND_UPGRADE = ITEMS.add("amethyst_diamond_upgrade", () -> new TierUpgradeItem(uniqueStackItemProperties(ModObjectsUF.DIAMOND_UPGRADE), AMETHYST_FURNACE.get(), ModObjects.DIAMOND_FURNACE.get()));
    public static final RegisterListing.Holder<TierUpgradeItem> PLATINUM_UPGRADE = ITEMS.add("platinum_upgrade", () -> new TierUpgradeItem(uniqueStackItemProperties(ModObjectsUF.PLATINUM_UPGRADE), ModObjects.DIAMOND_FURNACE.get(), PLATINUM_FURNACE.get()));
    public static final RegisterListing.Holder<TierUpgradeItem> NETHERHOT_UPGRADE = ITEMS.add("platinum_netherhot_upgrade", () -> new TierUpgradeItem(uniqueStackItemProperties(ModObjectsUF.NETHERHOT_UPGRADE), PLATINUM_FURNACE.get(), ModObjects.NETHERHOT_FURNACE.get()));
    public static final RegisterListing.Holder<TierUpgradeItem> ULTIMATE_UPGRADE = ITEMS.add("ultimate_upgrade", () -> new TierUpgradeItem(uniqueStackItemProperties(ModObjectsUF.ULTIMATE_UPGRADE), ModObjects.EXTREME_FURNACE.get(), ULTIMATE_FURNACE.get()));
    public static final RegisterListing.Holder<OreProcessingUpgradeItem> ULTIMATE_ORE_PROCESSING = ITEMS.add("ultimate_ore_processing_upgrade", () -> new OreProcessingUpgradeItem(uniqueStackItemProperties(ModObjectsUF.ULTIMATE_ORE_PROCESSING),4,true,true));


}
