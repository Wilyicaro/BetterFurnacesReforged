package wily.betterfurnaces.handler;

import wily.betterfurnaces.BetterFurnacesReforged;
import wily.betterfurnaces.init.ModObjects;
import wily.betterfurnaces.inventory.ContainerBF;
import wily.betterfurnaces.tile.TileEntitySmeltingBase;
import wily.betterfurnaces.utils.FluidRenderUtil;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

public class GuiBF extends GuiContainer {

	private static final ResourceLocation TEXTURE = new ResourceLocation(BetterFurnacesReforged.MODID, "textures/gui/container/furnace_gui.png");
	private static final ResourceLocation WIDGETS = new ResourceLocation(BetterFurnacesReforged.MODID, "textures/gui/container/widgets.png");
	private final InventoryPlayer playerInventory;
	private final TileEntitySmeltingBase te;

	public GuiBF(InventoryPlayer player, TileEntitySmeltingBase te) {
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
		int l = this.getCookProgressScaled(24);
		this.drawTexturedModalRect(i + 79, j + 34, 176, 14, l + 1, 16);
		if (te.hasUpgrade(ModObjects.ENERGY_UPGRADE)) {
			this.mc.getTextureManager().bindTexture(WIDGETS);
			int k = this.getEnergyStoredScaled(34);
			this.drawTexturedModalRect(this.guiLeft + 31, this.guiTop + 17, 240, 0, 16, 34);
			this.drawTexturedModalRect(this.guiLeft + 31, this.guiTop + 17, 240, 34, 16, 34 - k);
		}
		if (te.hasUpgrade(ModObjects.LIQUID_FUEL_UPGRADE)){
			this.mc.getTextureManager().bindTexture(WIDGETS);
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
		if (te.hasUpgrade(ModObjects.LIQUID_FUEL_UPGRADE) && te.getTank().getFluid() != null && this.isPointInRegion(73, 49 + (21 - getFluidStoredScaled(21)), 20, getFluidStoredScaled(21), x, y)) {
			this.drawHoveringText(I18n.format("gui.betterfurnacesreforged.fluid", te.getTank().getFluid().getLocalizedName(), te.getTank().getFluidAmount()), x, y);
		}else if (te.hasUpgrade(ModObjects.ENERGY_UPGRADE)&& this.isPointInRegion(31,  17, 16, 34, x, y)) {
			this.drawHoveringText(I18n.format("gui.betterfurnacesreforged.energy", te.getEnergy() / 1000,te.MAX_ENERGY_STORED() / 1000), x, y);
		}
	}

	private int getEnergyStoredScaled(int pixels) {
		int cur = te.getEnergy();
		int max = te.MAX_ENERGY_STORED();
		return getPixels(cur, max, pixels);
	}

	private int getFluidStoredScaled(int pixels) {
		int cur = te.getTank().getFluidAmount();
		int max = te.LiquidCapacity();
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

	public TileEntitySmeltingBase getTE() {
		return te;
	}

	static int getPixels(float a, float b, int pixels) {
		return (int) Math.floor(a / b * pixels);
	}

}