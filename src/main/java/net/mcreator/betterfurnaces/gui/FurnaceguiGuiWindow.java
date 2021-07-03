
package net.mcreator.betterfurnaces.gui;

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

import net.mcreator.betterfurnaces.procedures.TankguicondProcedure;
import net.mcreator.betterfurnaces.procedures.AdmupactiveProcedure;

import java.util.concurrent.atomic.AtomicInteger;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.matrix.MatrixStack;

import com.google.common.collect.ImmutableMap;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.IWorld;

@OnlyIn(Dist.CLIENT)
public class FurnaceguiGuiWindow extends ContainerScreen<FurnaceguiGui.GuiContainerMod> {
	private World world;
	private int x, y, z;
	private PlayerEntity entity;
	public FurnaceguiGuiWindow(FurnaceguiGui.GuiContainerMod container, PlayerInventory inventory, ITextComponent text) {
		super(container, inventory, text);
		this.world = container.world;
		this.x = container.x;
		this.y = container.y;
		this.z = container.z;
		this.entity = container.entity;
		this.xSize = 176;
		this.ySize = 166;
	}
	private static final ResourceLocation texture = new ResourceLocation("betterfurnacesreforged:textures/furnacegui.png");
	@Override
	public void render(MatrixStack ms, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(ms);
		super.render(ms, mouseX, mouseY, partialTicks);
		this.renderHoveredTooltip(ms, mouseX, mouseY);

        if (TankguicondProcedure.executeProcedure(ImmutableMap.of("x", x, "y", y, "z", z, "world", world)) &&
		 (mouseX > guiLeft + 73 && mouseX < guiLeft + 92 && mouseY > guiTop + 49 && mouseY < guiTop + 70))
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

	@Override
	protected void drawGuiContainerBackgroundLayer(MatrixStack ms, float partialTicks, int gx, int gy) {
		RenderSystem.color4f(1, 1, 1, 1);
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		Minecraft.getInstance().getTextureManager().bindTexture(texture);
		int k = (this.width - this.xSize) / 2;
		int l = (this.height - this.ySize) / 2;
		this.blit(ms, k, l, 0, 0, this.xSize, this.ySize, this.xSize, this.ySize);
		Minecraft.getInstance().getTextureManager().bindTexture(new ResourceLocation("betterfurnacesreforged:textures/processbar0.png"));
		this.blit(ms, this.guiLeft + 80, this.guiTop + 35, 0, 0, 22, 18, 22, 18);
		Minecraft.getInstance().getTextureManager().bindTexture(new ResourceLocation("betterfurnacesreforged:textures/burn0.png"));
		this.blit(ms, this.guiLeft + 53, this.guiTop + 35, 0, 0, 18, 18, 18, 18);
		double lavaanimation = (double) (new Object() {
				public int getFluidTankLevel(BlockPos pos, int tank) {
					AtomicInteger _retval = new AtomicInteger(0);
					TileEntity _ent = world.getTileEntity(pos);
					if (_ent != null)
						_ent.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null)
								.ifPresent(capability -> _retval.set(capability.getFluidInTank(tank).getAmount()));
					return _retval.get();
				}
			}.getFluidTankLevel(new BlockPos((int) x, (int) y, (int) z), 1));
		double progressanimation = (double) (new Object() {
			public double getValue(IWorld world, BlockPos pos, String tag) {
				TileEntity tileEntity = world.getTileEntity(pos);
				if (tileEntity != null)
					return tileEntity.getTileData().getDouble(tag);
				return -1;
			}
		}.getValue(world, new BlockPos((int) x, (int) y, (int) z), "cookPorcent"));
		double burnanimation = (double) (new Object() {
			public double getValue(IWorld world, BlockPos pos, String tag) {
				TileEntity tileEntity = world.getTileEntity(pos);
				if (tileEntity != null)
					return tileEntity.getTileData().getDouble(tag);
				return -1;
			}
		}.getValue(world, new BlockPos((int) x, (int) y, (int) z), "burnPorcent"));
		if (TankguicondProcedure.executeProcedure(ImmutableMap.of("x", x, "y", y, "z", z, "world", world))) {
			Minecraft.getInstance().getTextureManager().bindTexture(new ResourceLocation("betterfurnacesreforged:textures/lavatank.png"));
			this.blit(ms, this.guiLeft + 73, this.guiTop + 49, 0, 0, 20, 22, 20, 22);
			
		if (lavaanimation >= 500) {
			Minecraft.getInstance().getTextureManager().bindTexture(new ResourceLocation("betterfurnacesreforged:textures/lavatank_1.png"));
			this.blit(ms, this.guiLeft + 73, this.guiTop + 49, 0, 0, 20, 22, 20, 22);
		}
		if (lavaanimation >= 2000) {
			Minecraft.getInstance().getTextureManager().bindTexture(new ResourceLocation("betterfurnacesreforged:textures/lavatank_2.png"));
			this.blit(ms, this.guiLeft + 73, this.guiTop + 49, 0, 0, 20, 22, 20, 22);
		}
		if (lavaanimation >= 3000) {
			Minecraft.getInstance().getTextureManager().bindTexture(new ResourceLocation("betterfurnacesreforged:textures/lavatank_3.png"));
			this.blit(ms, this.guiLeft + 73, this.guiTop + 49, 0, 0, 20, 22, 20, 22);
		}
		if (lavaanimation >= 4000) {
			Minecraft.getInstance().getTextureManager().bindTexture(new ResourceLocation("betterfurnacesreforged:textures/lavatank_full.png"));
			this.blit(ms, this.guiLeft + 73, this.guiTop + 49, 0, 0, 20, 22, 20, 22);
		}
		}
		if (burnanimation >= 1) {
			Minecraft.getInstance().getTextureManager().bindTexture(new ResourceLocation("betterfurnacesreforged:textures/burn1.png"));
			this.blit(ms, this.guiLeft + 53, this.guiTop + 35, 0, 0, 18, 18, 18, 18);
		}
		if (burnanimation >= 11) {
			Minecraft.getInstance().getTextureManager().bindTexture(new ResourceLocation("betterfurnacesreforged:textures/burn2.png"));
			this.blit(ms, this.guiLeft + 53, this.guiTop + 35, 0, 0, 18, 18, 18, 18);
		}
		if (burnanimation >= 21) {
			Minecraft.getInstance().getTextureManager().bindTexture(new ResourceLocation("betterfurnacesreforged:textures/burn3.png"));
			this.blit(ms, this.guiLeft + 53, this.guiTop + 35, 0, 0, 18, 18, 18, 18);
		}
		if (burnanimation >= 31) {
			Minecraft.getInstance().getTextureManager().bindTexture(new ResourceLocation("betterfurnacesreforged:textures/burn4.png"));
			this.blit(ms, this.guiLeft + 53, this.guiTop + 35, 0, 0, 18, 18, 18, 18);
		}
		if (burnanimation >= 41) {
			Minecraft.getInstance().getTextureManager().bindTexture(new ResourceLocation("betterfurnacesreforged:textures/burn5.png"));
			this.blit(ms, this.guiLeft + 53, this.guiTop + 35, 0, 0, 18, 18, 18, 18);
		}
		if (burnanimation >= 51) {
			Minecraft.getInstance().getTextureManager().bindTexture(new ResourceLocation("betterfurnacesreforged:textures/burn6.png"));
			this.blit(ms, this.guiLeft + 53, this.guiTop + 35, 0, 0, 18, 18, 18, 18);
		}
		if (burnanimation >= 61) {
			Minecraft.getInstance().getTextureManager().bindTexture(new ResourceLocation("betterfurnacesreforged:textures/burn7.png"));
			this.blit(ms, this.guiLeft + 53, this.guiTop + 35, 0, 0, 18, 18, 18, 18);
		}
		if (burnanimation >= 71) {
			Minecraft.getInstance().getTextureManager().bindTexture(new ResourceLocation("betterfurnacesreforged:textures/burn8.png"));
			this.blit(ms, this.guiLeft + 53, this.guiTop + 35, 0, 0, 18, 18, 18, 18);
		}
		if (burnanimation >= 81) {
			Minecraft.getInstance().getTextureManager().bindTexture(new ResourceLocation("betterfurnacesreforged:textures/burn9.png"));
			this.blit(ms, this.guiLeft + 53, this.guiTop + 35, 0, 0, 18, 18, 18, 18);
		}
		if (burnanimation >= 91) {
			Minecraft.getInstance().getTextureManager().bindTexture(new ResourceLocation("betterfurnacesreforged:textures/burn10.png"));
			this.blit(ms, this.guiLeft + 53, this.guiTop + 35, 0, 0, 18, 18, 18, 18);
		}
		if (progressanimation >= 1) {
			Minecraft.getInstance().getTextureManager().bindTexture(new ResourceLocation("betterfurnacesreforged:textures/processbar1.png"));
			this.blit(ms, this.guiLeft + 80, this.guiTop + 35, 0, 0, 22, 18, 22, 18);
		}
		if (progressanimation >= 11) {
			Minecraft.getInstance().getTextureManager().bindTexture(new ResourceLocation("betterfurnacesreforged:textures/processbar2.png"));
			this.blit(ms, this.guiLeft + 80, this.guiTop + 35, 0, 0, 22, 18, 22, 18);
		}
		if (progressanimation >= 21) {
			Minecraft.getInstance().getTextureManager().bindTexture(new ResourceLocation("betterfurnacesreforged:textures/processbar3.png"));
			this.blit(ms, this.guiLeft + 80, this.guiTop + 35, 0, 0, 22, 18, 22, 18);
		}
		if (progressanimation >= 31) {
			Minecraft.getInstance().getTextureManager().bindTexture(new ResourceLocation("betterfurnacesreforged:textures/processbar4.png"));
			this.blit(ms, this.guiLeft + 80, this.guiTop + 35, 0, 0, 22, 18, 22, 18);
		}
		if (progressanimation >= 41) {
			Minecraft.getInstance().getTextureManager().bindTexture(new ResourceLocation("betterfurnacesreforged:textures/processbar5.png"));
			this.blit(ms, this.guiLeft + 80, this.guiTop + 35, 0, 0, 22, 18, 22, 18);
		}
		if (progressanimation >= 51) {
			Minecraft.getInstance().getTextureManager().bindTexture(new ResourceLocation("betterfurnacesreforged:textures/processbar6.png"));
			this.blit(ms, this.guiLeft + 80, this.guiTop + 35, 0, 0, 22, 18, 22, 18);
		}
		if (progressanimation >= 61) {
			Minecraft.getInstance().getTextureManager().bindTexture(new ResourceLocation("betterfurnacesreforged:textures/processbar7.png"));
			this.blit(ms, this.guiLeft + 80, this.guiTop + 35, 0, 0, 22, 18, 22, 18);
		}
		if (progressanimation >= 71) {
			Minecraft.getInstance().getTextureManager().bindTexture(new ResourceLocation("betterfurnacesreforged:textures/processbar8.png"));
			this.blit(ms, this.guiLeft + 80, this.guiTop + 35, 0, 0, 22, 18, 22, 18);
		}
		if (progressanimation >= 81) {
			Minecraft.getInstance().getTextureManager().bindTexture(new ResourceLocation("betterfurnacesreforged:textures/processbar9.png"));
			this.blit(ms, this.guiLeft + 80, this.guiTop + 35, 0, 0, 22, 18, 22, 18);
		}
		if (progressanimation >= 91) {
			Minecraft.getInstance().getTextureManager().bindTexture(new ResourceLocation("betterfurnacesreforged:textures/processbar10.png"));
			this.blit(ms, this.guiLeft + 80, this.guiTop + 35, 0, 0, 22, 18, 22, 18);
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
		this.font.drawString(ms, "" + (new Object() {
			public String getValue(BlockPos pos, String tag) {
				TileEntity tileEntity = world.getTileEntity(pos);
				if (tileEntity != null)
					return tileEntity.getTileData().getString(tag);
				return "";
			}
		}.getValue(new BlockPos((int) x, (int) y, (int) z), "furnacename")) + "", 53, 6, -13421773);
		if (AdmupactiveProcedure.executeProcedure(ImmutableMap.of("x", x, "y", y, "z", z, "world", world)))
			this.font.drawString(ms, "" + (new Object() {
				public double getValue(BlockPos pos, String tag) {
					TileEntity tileEntity = world.getTileEntity(pos);
					if (tileEntity != null)
						return tileEntity.getTileData().getDouble(tag);
					return 0;
				}
			}.getValue(new BlockPos((int) x, (int) y, (int) z), "cookPorcent")) + "%", 76, 38, -10066330);
		if (AdmupactiveProcedure.executeProcedure(ImmutableMap.of("x", x, "y", y, "z", z, "world", world)))
			this.font.drawString(ms, "" + (new Object() {
				public double getValue(BlockPos pos, String tag) {
					TileEntity tileEntity = world.getTileEntity(pos);
					if (tileEntity != null)
						return tileEntity.getTileData().getDouble(tag);
					return 0;
				}
			}.getValue(new BlockPos((int) x, (int) y, (int) z), "burnPorcent")) + "%", 49, 38, -3381760);
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
