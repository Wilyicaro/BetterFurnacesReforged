
package wily.betterfurnaces.gui;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.api.distmarker.Dist;

import net.minecraft.world.World;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.ResourceLocation;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.Minecraft;

import wily.betterfurnaces.procedures.TankguicondProcedure;
import wily.betterfurnaces.procedures.EnergyguicondProcedure;

import java.util.concurrent.atomic.AtomicInteger;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.matrix.MatrixStack;

import com.google.common.collect.ImmutableMap;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.IWorld;

@OnlyIn(Dist.CLIENT)
public class ForgeGuiWindow extends ContainerScreen<ForgeGui.GuiContainerMod> {
	private World world;
	private int x, y, z;
	private PlayerEntity entity;
	private Block block;
	public ForgeGuiWindow(ForgeGui.GuiContainerMod container, PlayerInventory inventory, ITextComponent text) {
		super(container, inventory, text);
		this.world = container.world;
		this.x = container.x;
		this.y = container.y;
		this.z = container.z;
		this.entity = container.entity;
		this.xSize = 176;
		this.ySize = 206;
	}
	private static final ResourceLocation texture = new ResourceLocation("betterfurnacesreforged:textures/forge_gui.png");
	@Override
	public void render(MatrixStack ms, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(ms);
		super.render(ms, mouseX, mouseY, partialTicks);
		this.renderHoveredTooltip(ms, mouseX, mouseY);
		    int energy = ((int)new Object() {
			public int getEnergyStored(BlockPos pos) {
				AtomicInteger _retval = new AtomicInteger(0);
				TileEntity _ent = world.getTileEntity(pos);
				if (_ent != null)
					_ent.getCapability(CapabilityEnergy.ENERGY, null).ifPresent(capability -> _retval.set(capability.getEnergyStored()));
				return _retval.get();
				}   
            }.getEnergyStored(new BlockPos((int) x, (int) y, (int) z)));
            int energylevel = (energy / 1000);
            if (EnergyguicondProcedure.executeProcedure(ImmutableMap.of("x", x, "y", y, "z", z, "world", world)) &&
		 (mouseX > guiLeft + 7 && mouseX < guiLeft + 23 && mouseY > guiTop + 60 && mouseY < guiTop + 95)){
            renderTooltip(ms, new StringTextComponent(energylevel + " kFE/32 kFE"), mouseX, mouseY);
		 }
        if (TankguicondProcedure.executeProcedure(ImmutableMap.of("x", x, "y", y, "z", z, "world", world)) &&
		 (mouseX > guiLeft + 26 && mouseX < guiLeft + 45 && mouseY > guiTop + 98 && mouseY < guiTop + 119))
            this.renderTooltip(ms, new StringTextComponent("Lava: " + (new Object() {
				public int getFluidTankLevel(BlockPos pos, int tank) {
					AtomicInteger _retval = new AtomicInteger(0);
					TileEntity _ent = world.getTileEntity(pos);
					if (_ent != null)
						_ent.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null)
								.ifPresent(capability -> _retval.set(capability.getFluidInTank(tank).getAmount()));
					return _retval.get();
				}
			}.getFluidTankLevel(new BlockPos((int) x, (int) y, (int) z), 1)) + " mB"), mouseX, mouseY);

	}
	@OnlyIn(Dist.CLIENT)
	public int getCookScaled(int pixels) {
		block = world.getBlockState(new BlockPos((int) x, (int) y, (int) z)).getBlock();
		int i = (int) world.getTileEntity(new BlockPos((int) x, (int) y, (int) z)).getTileData().getDouble("cookPorcent");
		int j = 100;
		return j != 0 && i != 0 ? i * pixels / j : 0;

	}
	@OnlyIn(Dist.CLIENT)
	public int getBurnLeftScaled(int pixels) {
		int i = 100;

		return (int) world.getTileEntity(new BlockPos((int) x, (int) y, (int) z)).getTileData().getDouble("burnPorcent") * pixels / i;
	}
	private int getFluidStoredScaled(int pixels) {
		int cur = world.getTileEntity(new BlockPos((int) x, (int) y, (int) z)).getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null).resolve().get().getFluidInTank(1).getAmount();
		int max = 8000;
		return cur * pixels / max;
	}
	private int getEnergyStoredScaled(int pixels) {
		int cur = world.getTileEntity(new BlockPos((int) x, (int) y, (int) z)).getCapability(CapabilityEnergy.ENERGY, null).resolve().get().getEnergyStored();
		int max = world.getTileEntity(new BlockPos((int) x, (int) y, (int) z)).getCapability(CapabilityEnergy.ENERGY, null).resolve().get().getMaxEnergyStored();
		return cur * pixels / max;
	}
	@Override
	protected void drawGuiContainerBackgroundLayer(MatrixStack ms, float partialTicks, int gx, int gy) {
		RenderSystem.color4f(1, 1, 1, 1);
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		Minecraft.getInstance().getTextureManager().bindTexture(texture);
		int k = (this.width - this.xSize) / 2;
		int l = (this.height - this.ySize) / 2;
		int i;
		i = this.getCookScaled(24);
		this.blit(ms, k, l, 0, 0, this.xSize, this.ySize);
		this.blit(ms, guiLeft + 80, guiTop + 80, 176, 14, i + 1, 16);
		if (world.getTileEntity(new BlockPos((int) x, (int) y, (int) z)).getTileData().getDouble("burnTime") > 0) {
			i = getBurnLeftScaled(13);
			this.blit(ms, guiLeft + 27, guiTop + 81 + 12 - i, 176, 12 - i, 14, i + 1);
			this.blit(ms, guiLeft + 45, guiTop + 81 + 12 - i, 176, 12 - i, 14, i + 1);
			this.blit(ms, guiLeft + 63, guiTop + 81 + 12 - i, 176, 12 - i, 14, i + 1);
		}
		double energyanimation = (double) (new Object() {
			public int getEnergyStored(BlockPos pos) {
				AtomicInteger _retval = new AtomicInteger(0);
				TileEntity _ent = world.getTileEntity(pos);
				if (_ent != null)
					_ent.getCapability(CapabilityEnergy.ENERGY, null).ifPresent(capability -> _retval.set(capability.getEnergyStored()));
				return _retval.get();
				}   
            }.getEnergyStored(new BlockPos((int) x, (int) y, (int) z)));

		if (EnergyguicondProcedure.executeProcedure(ImmutableMap.of("x", x, "y", y, "z", z, "world", world))) {
			i = getEnergyStoredScaled(34);
			this.blit(ms, guiLeft + 8, guiTop + 62, 176, 93, 16, 34);
			this.blit(ms, guiLeft + 8, guiTop + 62, 176, 127, 16, 34 - i);
		}
		if (TankguicondProcedure.executeProcedure(ImmutableMap.of("x", x, "y", y, "z", z, "world", world))) {
			this.blit(ms, guiLeft + 26, guiTop + 98, 176, 69, 20, 22);
			i = getFluidStoredScaled(21);
			FluidStack fluid;
			fluid =  world.getTileEntity(new BlockPos((int) x, (int) y, (int) z)).getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null).resolve().get().getFluidInTank(1);
			if (i > 0) {
				TextureAtlasSprite fluidSprite = this.minecraft.getAtlasSpriteGetter(PlayerContainer.LOCATION_BLOCKS_TEXTURE)
						.apply(fluid.getFluid().getAttributes().getStillTexture(fluid)
						);
				this.minecraft.getTextureManager().bindTexture(
						fluidSprite.getAtlasTexture().getTextureLocation()
				);
				this.blit(ms, this.guiLeft + 26, this.guiTop + 98 + 21 - i, this.getBlitOffset(), 20, i, fluidSprite);
			}
			Minecraft.getInstance().getTextureManager().bindTexture(texture);
			this.blit(ms, guiLeft + 26, guiTop + 98, 176, 47, 20, 22);
		}
		RenderSystem.disableBlend();
	}

	@Override
	public boolean keyPressed(int key, int b, int c) {
		if (key == 256) {
			this.minecraft.player.closeScreen();
			return true;
		}
		return super.keyPressed(key, b, c);
	}

	@Override
	public void tick() {
		super.tick();
	}

	@Override
	protected void drawGuiContainerForegroundLayer(MatrixStack ms, int mouseX, int mouseY) {
		String s = new ItemStack(world.getBlockState(new BlockPos((int) x, (int) y, (int) z)).getBlock()).getDisplayName().getString();
		this.font.drawString(ms, s, this.xSize / 2 - font.getStringWidth(s) / 2, 45, 4210752);
		this.font.drawString(ms, this.playerInventory.getDisplayName().getString(), 60, this.ySize - 92, 4210752);
	}
	@Override
	public void onClose() {
		super.onClose();
		Minecraft.getInstance().keyboardListener.enableRepeatEvents(false);
	}

	@Override
	public void init(Minecraft minecraft, int width, int height) {
		super.init(minecraft, width, height);
		minecraft.keyboardListener.enableRepeatEvents(true);
	}
}
