package wily.ultimatefurnaces.init;

import dev.architectury.registry.menu.MenuRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.Registry;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import wily.betterfurnaces.BetterFurnacesReforged;
import wily.betterfurnaces.items.OreProcessingUpgradeItem;
import wily.ultimatefurnaces.UltimateFurnaces;
import wily.ultimatefurnaces.blockentity.*;
import wily.ultimatefurnaces.blocks.*;
import wily.ultimatefurnaces.inventory.*;
import wily.ultimatefurnaces.items.*;

public class RegistrationUF {

    public static final DeferredRegister<Block> BLOCK_ITEMS = DeferredRegister.create(BetterFurnacesReforged.MOD_ID, Registry.BLOCK_REGISTRY);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(BetterFurnacesReforged.MOD_ID, Registry.ITEM_REGISTRY);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(BetterFurnacesReforged.MOD_ID, Registry.BLOCK_ENTITY_TYPE_REGISTRY);
    private static final DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister.create(BetterFurnacesReforged.MOD_ID, Registry.MENU_REGISTRY);

    public static void init() {
        BLOCK_ITEMS.register();
        BLOCK_ITEMS.forEach((b)-> ITEMS.getRegistrar().register( b.getId(),() -> new BlockItem(b.get(), defaultItemProperties())));
        ITEMS.register();
        BLOCK_ENTITIES.register();
        CONTAINERS.register();
    }



    private static Item.Properties defaultItemProperties(){ return  new Item.Properties().tab(UltimateFurnaces.ITEM_GROUP);}
    private static Item.Properties uniqueStackItemProperties(){ return  defaultItemProperties().stacksTo(1);}
    public static final RegistrySupplier<CopperFurnaceBlock> COPPER_FURNACE = BLOCK_ITEMS.register(CopperFurnaceBlock.COPPER_FURNACE, () -> new CopperFurnaceBlock(BlockBehaviour.Properties.copy(Blocks.COAL_BLOCK).strength(10.0F, 20.0F)));
    public static final RegistrySupplier<BlockEntityType<CopperFurnaceBlockEntity>> COPPER_FURNACE_TILE = BLOCK_ENTITIES.register(CopperFurnaceBlock.COPPER_FURNACE, () -> BlockEntityType.Builder.of(CopperFurnaceBlockEntity::new, COPPER_FURNACE.get()).build(null));

    public static final RegistrySupplier<MenuType<CopperFurnaceMenu>> COPPER_FURNACE_CONTAINER = CONTAINERS.register(CopperFurnaceBlock.COPPER_FURNACE,  () -> MenuRegistry.ofExtended((windowId, inv, data) -> new CopperFurnaceMenu(windowId, inv.player.level, data.readBlockPos(), inv, inv.player)));

    public static final RegistrySupplier<SteelFurnaceBlock> STEEL_FURNACE = BLOCK_ITEMS.register(SteelFurnaceBlock.STEEL_FURNACE, () -> new SteelFurnaceBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).strength(20.0F, 60.0F)));
    public static final RegistrySupplier<BlockEntityType<SteelFurnaceBlockEntity>> STEEL_FURNACE_TILE = BLOCK_ENTITIES.register(SteelFurnaceBlock.STEEL_FURNACE, () -> BlockEntityType.Builder.of(SteelFurnaceBlockEntity::new, STEEL_FURNACE.get()).build(null));

    public static final RegistrySupplier<MenuType<SteelFurnaceMenu>> STEEL_FURNACE_CONTAINER = CONTAINERS.register(SteelFurnaceBlock.STEEL_FURNACE, () -> MenuRegistry.ofExtended((windowId, inv, data) -> new SteelFurnaceMenu(windowId, inv.player.level, data.readBlockPos(), inv, inv.player)));

    public static final RegistrySupplier<AmethystFurnaceBlock> AMETHYST_FURNACE = BLOCK_ITEMS.register(AmethystFurnaceBlock.AMETHYST_FURNACE, () -> new AmethystFurnaceBlock(BlockBehaviour.Properties.copy(Blocks.AMETHYST_BLOCK).strength(20.0F, 30.0F)));
    public static final RegistrySupplier<BlockEntityType<AmethystFurnaceBlockEntity>> AMETHYST_FURNACE_TILE = BLOCK_ENTITIES.register(AmethystFurnaceBlock.AMETHYST_FURNACE, () -> BlockEntityType.Builder.of(AmethystFurnaceBlockEntity::new, AMETHYST_FURNACE.get()).build(null));

    public static final RegistrySupplier<MenuType<AmethystFurnaceMenu>> AMETHYST_FURNACE_CONTAINER = CONTAINERS.register(AmethystFurnaceBlock.AMETHYST_FURNACE,  () -> MenuRegistry.ofExtended((windowId, inv, data) -> new AmethystFurnaceMenu(windowId, inv.player.level, data.readBlockPos(), inv, inv.player)));

    public static final RegistrySupplier<PlatinumFurnaceBlock> PLATINUM_FURNACE = BLOCK_ITEMS.register(PlatinumFurnaceBlock.PLATINUM_FURNACE, () -> new PlatinumFurnaceBlock(BlockBehaviour.Properties.copy(Blocks.RAW_GOLD_BLOCK).strength(30.0F, 60.0F)));
    public static final RegistrySupplier<BlockEntityType<PlatinumFurnaceBlockEntity>> PLATINUM_FURNACE_TILE = BLOCK_ENTITIES.register(PlatinumFurnaceBlock.PLATINUM_FURNACE, () -> BlockEntityType.Builder.of(PlatinumFurnaceBlockEntity::new, PLATINUM_FURNACE.get()).build(null));

    public static final RegistrySupplier<MenuType<PlatinumFurnaceMenu>> PLATINUM_FURNACE_CONTAINER = CONTAINERS.register(PlatinumFurnaceBlock.PLATINUM_FURNACE,  () -> MenuRegistry.ofExtended((windowId, inv, data) -> new PlatinumFurnaceMenu(windowId, inv.player.level, data.readBlockPos(), inv, inv.player)));

    public static final RegistrySupplier<UltimateFurnaceBlock> ULTIMATE_FURNACE = BLOCK_ITEMS.register(UltimateFurnaceBlock.ULTIMATE_FURNACE, () -> new UltimateFurnaceBlock(BlockBehaviour.Properties.copy(Blocks.NETHERITE_BLOCK).strength(50.0F, 6000.0F)));
    public static final RegistrySupplier<BlockEntityType<UltimateFurnaceBlockEntity>> ULTIMATE_FURNACE_TILE = BLOCK_ENTITIES.register(UltimateFurnaceBlock.ULTIMATE_FURNACE, () -> BlockEntityType.Builder.of(UltimateFurnaceBlockEntity::new, ULTIMATE_FURNACE.get()).build(null));

    public static final RegistrySupplier<MenuType<UltimateFurnaceMenu>> ULTIMATE_FURNACE_CONTAINER = CONTAINERS.register(UltimateFurnaceBlock.ULTIMATE_FURNACE,  () -> MenuRegistry.ofExtended((windowId, inv, data) -> new UltimateFurnaceMenu(windowId, inv.player.level, data.readBlockPos(), inv, inv.player)));

    public static final RegistrySupplier<CopperForgeBlock> COPPER_FORGE = BLOCK_ITEMS.register(CopperForgeBlock.COPPER_FORGE, () -> new CopperForgeBlock(BlockBehaviour.Properties.copy(Blocks.COAL_BLOCK)));
    public static final RegistrySupplier<BlockEntityType<CopperForgeBlockEntity>> COPPER_FORGE_TILE = BLOCK_ENTITIES.register(CopperForgeBlock.COPPER_FORGE, () -> BlockEntityType.Builder.of(CopperForgeBlockEntity::new, COPPER_FORGE.get()).build(null));

    public static final RegistrySupplier<MenuType<CopperForgeMenu>> COPPER_FORGE_CONTAINER = CONTAINERS.register(CopperForgeBlock.COPPER_FORGE, () -> MenuRegistry.ofExtended((windowId, inv, data) -> new CopperForgeMenu(windowId, inv.player.level, data.readBlockPos(), inv, inv.player)));

    public static final RegistrySupplier<IronForgeBlock> IRON_FORGE = BLOCK_ITEMS.register(IronForgeBlock.IRON_FORGE, () -> new IronForgeBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)));
    public static final RegistrySupplier<BlockEntityType<IronForgeBlockEntity>> IRON_FORGE_TILE = BLOCK_ENTITIES.register(IronForgeBlock.IRON_FORGE, () -> BlockEntityType.Builder.of(IronForgeBlockEntity::new, IRON_FORGE.get()).build(null));

    public static final RegistrySupplier<MenuType<IronForgeMenu>> IRON_FORGE_CONTAINER = CONTAINERS.register(IronForgeBlock.IRON_FORGE,  () -> MenuRegistry.ofExtended((windowId, inv, data) -> new IronForgeMenu(windowId, inv.player.level, data.readBlockPos(), inv, inv.player)));

    public static final RegistrySupplier<GoldForgeBlock> GOLD_FORGE = BLOCK_ITEMS.register(GoldForgeBlock.GOLD_FORGE, () -> new GoldForgeBlock(BlockBehaviour.Properties.copy(Blocks.GOLD_BLOCK)));
    public static final RegistrySupplier<BlockEntityType<GoldForgeBlockEntity>> GOLD_FORGE_TILE = BLOCK_ENTITIES.register(GoldForgeBlock.GOLD_FORGE, () -> BlockEntityType.Builder.of(GoldForgeBlockEntity::new, GOLD_FORGE.get()).build(null));

    public static final RegistrySupplier<MenuType<GoldForgeMenu>> GOLD_FORGE_CONTAINER = CONTAINERS.register(GoldForgeBlock.GOLD_FORGE,  () -> MenuRegistry.ofExtended((windowId, inv, data) -> new GoldForgeMenu(windowId, inv.player.level, data.readBlockPos(), inv, inv.player)));

    public static final RegistrySupplier<DiamondForgeBlock> DIAMOND_FORGE = BLOCK_ITEMS.register(DiamondForgeBlock.DIAMOND_FORGE, () -> new DiamondForgeBlock(BlockBehaviour.Properties.copy(Blocks.DIAMOND_BLOCK)));
    public static final RegistrySupplier<BlockEntityType<DiamondForgeBlockEntity>> DIAMOND_FORGE_TILE = BLOCK_ENTITIES.register(DiamondForgeBlock.DIAMOND_FORGE, () -> BlockEntityType.Builder.of(DiamondForgeBlockEntity::new, DIAMOND_FORGE.get()).build(null));

    public static final RegistrySupplier<MenuType<DiamondForgeMenu>> DIAMOND_FORGE_CONTAINER = CONTAINERS.register(DiamondForgeBlock.DIAMOND_FORGE, () -> MenuRegistry.ofExtended((windowId, inv, data) -> new DiamondForgeMenu(windowId, inv.player.level, data.readBlockPos(), inv, inv.player)));

    public static final RegistrySupplier<NetherhotForgeBlock> NETHERHOT_FORGE = BLOCK_ITEMS.register(NetherhotForgeBlock.NETHERHOT_FORGE, () -> new NetherhotForgeBlock(BlockBehaviour.Properties.copy(Blocks.DIAMOND_BLOCK)));
    public static final RegistrySupplier<BlockEntityType<NetherhotForgeBlockEntity>> NETHERHOT_FORGE_TILE = BLOCK_ENTITIES.register(NetherhotForgeBlock.NETHERHOT_FORGE, () -> BlockEntityType.Builder.of(NetherhotForgeBlockEntity::new, NETHERHOT_FORGE.get()).build(null));

    public static final RegistrySupplier<MenuType<NetherhotForgeMenu>> NETHERHOT_FORGE_CONTAINER = CONTAINERS.register(NetherhotForgeBlock.NETHERHOT_FORGE,  () -> MenuRegistry.ofExtended((windowId, inv, data) -> new NetherhotForgeMenu(windowId, inv.player.level, data.readBlockPos(), inv, inv.player)));

    public static final RegistrySupplier<UltimateForgeBlock> ULTIMATE_FORGE = BLOCK_ITEMS.register(UltimateForgeBlock.ULTIMATE_FORGE, () -> new UltimateForgeBlock(BlockBehaviour.Properties.copy(Blocks.NETHERITE_BLOCK).strength(50.0F, 6000.0F)));
    public static final RegistrySupplier<BlockEntityType<UltimateForgeBlockEntity>> ULTIMATE_FORGE_TILE = BLOCK_ENTITIES.register(UltimateForgeBlock.ULTIMATE_FORGE, () -> BlockEntityType.Builder.of(UltimateForgeBlockEntity::new, ULTIMATE_FORGE.get()).build(null));

    public static final RegistrySupplier<MenuType<UltimateForgeMenu>> ULTIMATE_FORGE_CONTAINER = CONTAINERS.register(UltimateForgeBlock.ULTIMATE_FORGE,  () -> MenuRegistry.ofExtended((windowId, inv, data) -> new UltimateForgeMenu(windowId, inv.player.level, data.readBlockPos(), inv, inv.player)));

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
