package wily.betterfurnaces.init;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import wily.betterfurnaces.BetterFurnacesReforged;
import wily.betterfurnaces.blocks.BlockBetterFurnace;
import wily.betterfurnaces.blocks.BlockConductor;
import wily.betterfurnaces.blocks.BlockForge;
import wily.betterfurnaces.blocks.BlockSmelting;
import wily.betterfurnaces.compat.TopCompatibility;
import wily.betterfurnaces.items.*;
import wily.betterfurnaces.tile.*;
import wily.betterfurnaces.tile.TileEntityNetherhotFurnace;
import wily.betterfurnaces.tile.TileEntityExtremeFurnace;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.registries.IForgeRegistry;

/**
 * Main object class for Furnace Overhaul.  Handles registration.
 * Fields are automatically populated by @ObjectHolder, since their field name is an all-caps version of their registry name.
 * @author Shadows
 *
 */
@EventBusSubscriber(modid = BetterFurnacesReforged.MODID)
@ObjectHolder(BetterFurnacesReforged.MODID)
public class ModObjects{

	public static final BlockSmelting IRON_FURNACE = null;
	public static final BlockSmelting GOLD_FURNACE = null;
	public static final BlockSmelting DIAMOND_FURNACE = null;
	public static final BlockSmelting NETHERHOT_FURNACE = null;
	public static final BlockSmelting EXTREME_FURNACE = null;
	public static final BlockForge EXTREME_FORGE = null;

	public static final ItemUpgradeDamage FUEL_EFFICIENCY_UPGRADE = null;
	public static final ItemUpgradeDamage ORE_PROCESSING_UPGRADE = null;
	public static final ItemUpgrade ADVANCED_FUEL_EFFICIENCY_UPGRADE = null;
	public static final ItemUpgrade ADVANCED_ORE_PROCESSING_UPGRADE = null;
	public static final ItemUpgrade LIQUID_FUEL_UPGRADE = null;
	public static final ItemUpgrade ENERGY_UPGRADE = null;
	public static final ItemUpgrade COLOR_UPGRADE = null;

	public static final ItemKit IRON_UPGRADE = null;
	public static final ItemKit GOLD_UPGRADE = null;
	public static final ItemKit DIAMOND_UPGRADE = null;
	public static final ItemKit NETHERHOT_UPGRADE = null;
	public static final ItemKit EXTREME_UPGRADE = null;

	public static List<Item> modelList = new ArrayList<>();

	@SubscribeEvent
	public static void blocks(Register<Block> e) {
		//Formatter::off
		registerBlocks(e.getRegistry(),
				new BlockConductor("iron_conductor_block"),
				new BlockConductor("gold_conductor_block"),
				new BlockConductor("netherhot_conductor_block"),
				new BlockBetterFurnace("iron_furnace", 1.5, TileEntitySmeltingBase::new),
				new BlockBetterFurnace("gold_furnace", 2, TileEntityGoldFurnace::new),
				new BlockBetterFurnace("diamond_furnace", 4, TileEntityDiamondFurnace::new),
				new BlockBetterFurnace("netherhot_furnace",8, TileEntityNetherhotFurnace::new),
				new BlockBetterFurnace("extreme_furnace", 50, TileEntityExtremeFurnace::new),
				new BlockForge("extreme_forge", 50, TileEntityForge::new));
		//Formatter::on
	}

	static void registerBlocks(IForgeRegistry<Block> reg, Block... blocks) {
		reg.registerAll(blocks);
		for (Block b : blocks) {
			Item i = new ItemBlock(b).setRegistryName(b.getRegistryName());
			i.addPropertyOverride(new ResourceLocation(BetterFurnacesReforged.MODID, "empty"),
					(stack, worldIn, entityIn) -> {
						NBTTagCompound nbt;
						if (stack.hasTagCompound()) {
							nbt = stack.getTagCompound();
						} else return 0.f;
						return (nbt.hasKey("red") && nbt.hasKey("green") && nbt.hasKey("blue")) ? 1.f : 0.f;

					});
			modelList.add(i);
			ForgeRegistries.ITEMS.register(i);
		}
	}

	@SubscribeEvent
	public static void items(Register<Item> e) {
		//Formatter::off
		registerItems(e.getRegistry(),
				new ItemUpgradeDamage("fuel_efficiency_upgrade", "info.betterfurnacesreforged.efficiency", 2,256),
				new ItemUpgradeDamage("ore_processing_upgrade", "info.betterfurnacesreforged.ores", 3,128),
				new ItemUpgrade("advanced_fuel_efficiency_upgrade", "info.betterfurnacesreforged.efficiency",2),
				new ItemUpgrade("advanced_ore_processing_upgrade", "info.betterfurnacesreforged.ores",3),
				new ItemUpgrade("liquid_fuel_upgrade", "info.betterfurnacesreforged.fluid",1),
				new ItemUpgrade("energy_upgrade", "info.betterfurnacesreforged.energy",1),
				new ItemUpgradeColor("color_upgrade", "info.betterfurnacesreforged.color"),
				new ItemIronKit(),
				new ItemKit("gold_upgrade", IRON_FURNACE, GOLD_FURNACE),
				new ItemKit("diamond_upgrade", GOLD_FURNACE, DIAMOND_FURNACE),
				new ItemKit("netherhot_upgrade", DIAMOND_FURNACE, NETHERHOT_FURNACE),
				new ItemKit("extreme_upgrade", NETHERHOT_FURNACE, EXTREME_FURNACE)
				);
		//Formatter::on
	}

	static void registerItems(IForgeRegistry<Item> reg, Item... items) {
		reg.registerAll(items);
		for (Item i : items)
			modelList.add(i);
	}


}
