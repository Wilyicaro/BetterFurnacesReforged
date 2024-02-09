package wily.ultimatefurnaces.init;

import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import wily.betterfurnaces.BetterFurnacesReforged;
import wily.betterfurnaces.Config;
import wily.betterfurnaces.blocks.ForgeBlock;
import wily.betterfurnaces.blocks.SmeltingBlock;
import wily.betterfurnaces.items.OreProcessingUpgradeItem;
import wily.betterfurnaces.util.registration.SmeltingBlocks;
import wily.ultimatefurnaces.items.*;

import static wily.betterfurnaces.init.Registration.*;

public class ModObjectsUF {

    public static void init() {}
    public static final RegistrySupplier<CreativeModeTab> ITEM_GROUP =  TABS.register("tab_ultimate_furnaces",()-> CreativeTabRegistry.create(Component.translatable("itemGroup."+ BetterFurnacesReforged.MOD_ID + ".tab_ultimate"), ()-> ModObjectsUF.ULTIMATE_FURNACE.get().asItem().getDefaultInstance()));
    private static Item.Properties defaultItemProperties(){ return  new Item.Properties().arch$tab(ITEM_GROUP);}
    private static Item.Properties uniqueStackItemProperties(){ return  defaultItemProperties().stacksTo(1);}
    public static final RegistrySupplier<SmeltingBlock> COPPER_FURNACE = BLOCK_ITEMS.register(SmeltingBlocks.COPPER_FURNACE.getName(), () -> new SmeltingBlock(BlockBehaviour.Properties.copy(Blocks.COAL_BLOCK).strength(10.0F, 20.0F),defaultItemProperties(), Config.copperTierSpeed));
    public static final RegistrySupplier<SmeltingBlock> STEEL_FURNACE = BLOCK_ITEMS.register(SmeltingBlocks.STEEL_FURNACE.getName(), () -> new SmeltingBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).strength(20.0F, 60.0F),defaultItemProperties(), Config.steelTierSpeed));
    public static final RegistrySupplier<SmeltingBlock> AMETHYST_FURNACE = BLOCK_ITEMS.register(SmeltingBlocks.AMETHYST_FURNACE.getName(), () -> new SmeltingBlock(BlockBehaviour.Properties.copy(Blocks.AMETHYST_BLOCK).strength(20.0F, 30.0F),defaultItemProperties(), Config.amethystTierSpeed));
    public static final RegistrySupplier<SmeltingBlock> PLATINUM_FURNACE = BLOCK_ITEMS.register(SmeltingBlocks.PLATINUM_FURNACE.getName(), () -> new SmeltingBlock(BlockBehaviour.Properties.copy(Blocks.RAW_GOLD_BLOCK).strength(30.0F, 60.0F),defaultItemProperties(), Config.platinumTierSpeed));
    public static final RegistrySupplier<SmeltingBlock> ULTIMATE_FURNACE = BLOCK_ITEMS.register(SmeltingBlocks.ULTIMATE_FURNACE.getName(), () -> new SmeltingBlock(BlockBehaviour.Properties.copy(Blocks.NETHERITE_BLOCK).strength(50.0F, 6000.0F),defaultItemProperties(), Config.ultimateTierSpeed));

    public static final RegistrySupplier<ForgeBlock> COPPER_FORGE = BLOCK_ITEMS.register(SmeltingBlocks.COPPER_FORGE.getName(), () -> new ForgeBlock(BlockBehaviour.Properties.copy(Blocks.COAL_BLOCK),defaultItemProperties(),Config.copperTierSpeed));
    public static final RegistrySupplier<ForgeBlock> IRON_FORGE = BLOCK_ITEMS.register(SmeltingBlocks.IRON_FORGE.getName(), () -> new ForgeBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK),defaultItemProperties(), Config.ironTierSpeed));
    public static final RegistrySupplier<ForgeBlock> GOLD_FORGE = BLOCK_ITEMS.register(SmeltingBlocks.GOLD_FORGE.getName(), () -> new ForgeBlock(BlockBehaviour.Properties.copy(Blocks.GOLD_BLOCK),defaultItemProperties(), Config.goldTierSpeed));
    public static final RegistrySupplier<ForgeBlock> DIAMOND_FORGE = BLOCK_ITEMS.register(SmeltingBlocks.DIAMOND_FORGE.getName(), () -> new ForgeBlock(BlockBehaviour.Properties.copy(Blocks.DIAMOND_BLOCK),defaultItemProperties(), Config.diamondTierSpeed));
    public static final RegistrySupplier<ForgeBlock> NETHERHOT_FORGE = BLOCK_ITEMS.register(SmeltingBlocks.NETHERHOT_FORGE.getName(), () -> new ForgeBlock(BlockBehaviour.Properties.copy(Blocks.DIAMOND_BLOCK),defaultItemProperties(), Config.netherhotTierSpeed));
    public static final RegistrySupplier<ForgeBlock> ULTIMATE_FORGE = BLOCK_ITEMS.register(SmeltingBlocks.ULTIMATE_FORGE.getName(), () -> new ForgeBlock(BlockBehaviour.Properties.copy(Blocks.NETHERITE_BLOCK).strength(50.0F, 6000.0F),defaultItemProperties(), Config.ultimateTierSpeed));


    public static final RegistrySupplier<CopperUpgradeItem> COPPER_UPGRADE = ITEMS.register("copper_upgrade", () -> new CopperUpgradeItem(uniqueStackItemProperties()));

    public static final RegistrySupplier<IronUpgradeItem> IRON_UPGRADE = ITEMS.register("copper_iron_upgrade", () -> new IronUpgradeItem(uniqueStackItemProperties()));

    public static final RegistrySupplier<SteelUpgradeItem> STEEL_UPGRADE = ITEMS.register("steel_upgrade", () -> new SteelUpgradeItem(uniqueStackItemProperties()));

    public static final RegistrySupplier<GoldUpgradeItem> GOLD_UPGRADE = ITEMS.register("steel_gold_upgrade", () -> new GoldUpgradeItem(uniqueStackItemProperties()));

    public static final RegistrySupplier<AmethystUpgradeItem> AMETHYST_UPGRADE = ITEMS.register("amethyst_upgrade", () -> new AmethystUpgradeItem(uniqueStackItemProperties()));

    public static final RegistrySupplier<DiamondUpgradeItem> DIAMOND_UPGRADE = ITEMS.register("amethyst_diamond_upgrade", () -> new DiamondUpgradeItem(uniqueStackItemProperties()));

    public static final RegistrySupplier<PlatinumUpgradeItem> PLATINUM_UPGRADE = ITEMS.register("platinum_upgrade", () -> new PlatinumUpgradeItem(uniqueStackItemProperties()));

    public static final RegistrySupplier<NetherhotUpgradeItem> NETHERHOT_UPGRADE = ITEMS.register("platinum_netherhot_upgrade", () -> new NetherhotUpgradeItem(uniqueStackItemProperties()));

    public static final RegistrySupplier<UltimateUpgradeItem> ULTIMATE_UPGRADE = ITEMS.register("ultimate_upgrade", () -> new UltimateUpgradeItem(uniqueStackItemProperties()));

    public static final RegistrySupplier<OreProcessingUpgradeItem> UORE_PROCESSING = ITEMS.register("ultimate_ore_processing_upgrade", () -> new OreProcessingUpgradeItem(uniqueStackItemProperties(),4,true,true));

}
