package wily.betterfurnaces.compat;

import java.util.function.Function;

import javax.annotation.Nullable;

import mcjty.theoneprobe.api.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import org.apache.logging.log4j.Level;
import wily.betterfurnaces.BetterFurnacesReforged;
import net.minecraft.world.World;


public class TopCompatibility {

	private static boolean registered;

	public static void register() {
		if (registered) return;
		registered = true;
		FMLInterModComms.sendFunctionMessage("theoneprobe", "getTheOneProbe", "wily.betterfurnaces.compat.TopCompatibility$GetTheOneProbe");
	}
	public static class GetTheOneProbe implements Function<ITheOneProbe, Void> {

		public static ITheOneProbe probe;

		@Nullable
		@Override
		public Void apply(ITheOneProbe theOneProbe) {
			probe = theOneProbe;
			BetterFurnacesReforged.LOGGER.log(Level.INFO, "Enabled support for The One Probe");
			probe.registerProvider(new IProbeInfoProvider() {
				@Override
				public String getID() {
					return BetterFurnacesReforged.MODID + ":default";
				}

				@Override
				public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData data) {
					if (blockState.getBlock() instanceof TOPInfoProvider) {
						TOPInfoProvider provider = (TOPInfoProvider) blockState.getBlock();
						provider.addProbeInfo(mode, probeInfo, player, world, blockState, data);
					}

				}
			});
			return null;
		}

		public interface TOPInfoProvider {
			void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData data);
		}
	}
	public static class MainCompatHandler {
		public static void registerTOP() {
			if (Loader.isModLoaded("theoneprobe")) {
				TopCompatibility.register();
			}
		}

	}

}
