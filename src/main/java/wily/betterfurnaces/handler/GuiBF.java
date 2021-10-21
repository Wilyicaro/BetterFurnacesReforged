package wily.betterfurnaces.handler;

import wily.betterfurnaces.BetterFurnacesReforged;
import wily.betterfurnaces.inventory.ContainerBF;
import wily.betterfurnaces.tile.TileEntityIronFurnace;
import wily.betterfurnaces.upgrade.Upgrades;
import wily.betterfurnaces.utils.FluidRenderUtil;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

public class GuiBF extends GuiContainer {

	private static final ResourceLocation TEXTURE = new ResourceLocation(BetterFurnacesReforged.MODID, "textures/gui/container/furnace_gui.png");
	private final InventoryPlayer playerInventory;
	private final TileEntityIronFurnace te;

	public GuiBF(InventoryPlayer player, TileEntityIronFurnace te) {
		super(new ContainerBF(player, te));
		this.te = te;
		this.playerInventory = player;
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		this.renderHoveredToolTip(mouseX, mouseY);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(TEXTURE);
		int i = (this.width - this.xSize) / 2;
		int j = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);

		if (te.isBurning()) {
			int k = this.getBurnLeftScaled(13);
			this.drawTexturedModalRect(i + 55, j + 37 + 12 - k, 176, 12 - k, 14, k + 1);
		}
		if (te.hasUpgrade(Upgrades.LIQUID_FUEL)) {
			this.drawTexturedModalRect(this.guiLeft + 73, this.guiTop + 49, 176, 69, 20, 22);
		}
		int l = this.getCookProgressScaled(24);
		this.drawTexturedModalRect(i + 79, j + 34, 176, 14, l + 1, 16);
		if (te.hasUpgrade(Upgrades.LIQUID_FUEL)){
			this.drawTexturedModalRect(this.guiLeft + 73, this.guiTop + 49, 192, 38, 20, 22);
			this.zLevel++;
		this.drawTexturedModalRect(this.guiLeft + 73, this.guiTop + 49, 192, 16, 20, 22);
		this.zLevel--;

		int am = getFluidStoredScaled(21);
		if (am > 0) {
			GlStateManager.pushMatrix();
			GlStateManager.translate(0, 21 - am, 0);
			FluidRenderUtil.renderTiledFluid(this.guiLeft + 73, this.guiTop + 49, 20, am, 0, te.getTank().getFluid());
			GlStateManager.popMatrix();
		}
		}
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		String s = this.te.getBlockType().getLocalizedName();
		this.fontRenderer.drawString(s, this.xSize / 2 - this.fontRenderer.getStringWidth(s) / 2, 6, 4210752);
		this.fontRenderer.drawString(this.playerInventory.getDisplayName().getUnformattedText(), 7, this.ySize - 92, 4210752);
	}

	@Override
	protected void renderHoveredToolTip(int x, int y) {
		super.renderHoveredToolTip(x, y);
		if (te.hasUpgrade(Upgrades.LIQUID_FUEL) && te.getTank().getFluid() != null && this.isPointInRegion(73, 49 + (21 - getFluidStoredScaled(21)), 20, getFluidStoredScaled(21), x, y)) {
			this.drawHoveringText(I18n.format("gui.betterfurnacesreforged.fluid", te.getTank().getFluid().getLocalizedName(), te.getTank().getFluidAmount()), x, y);
		}
	}


	private int getFluidStoredScaled(int pixels) {
		int cur = te.getTank().getFluidAmount();
		int max = 4000;
		return getPixels(cur, max, pixels);
	}

	private int getCookProgressScaled(int pixels) {
		int cur = te.getCurrentCookTime();
		int max = te.getCookTime();
		return getPixels(cur, max, pixels);
	}

	private int getBurnLeftScaled(int pixels) {
		return getPixels(te.getBurnTime(), Math.max(1, te.getFuelLength()), pixels);
	}

	public TileEntityIronFurnace getTE() {
		return te;
	}

	static int getPixels(float a, float b, int pixels) {
		return (int) Math.floor(a / b * pixels);
	}

}