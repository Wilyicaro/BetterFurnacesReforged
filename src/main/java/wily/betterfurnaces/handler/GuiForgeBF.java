package wily.betterfurnaces.handler;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import wily.betterfurnaces.BetterFurnacesReforged;
import wily.betterfurnaces.init.ModObjects;
import wily.betterfurnaces.inventory.FContainerBF;
import wily.betterfurnaces.tile.TileEntityForge;
import wily.betterfurnaces.utils.FluidRenderUtil;

public class GuiForgeBF extends GuiContainer {
	protected int xSize = 176;
	protected int ySize = 206;
	private static final ResourceLocation TEXTURE = new ResourceLocation(BetterFurnacesReforged.MODID, "textures/gui/container/forge_gui.png");
	private static final ResourceLocation WIDGETS = new ResourceLocation(BetterFurnacesReforged.MODID, "textures/gui/container/widgets.png");
	private final InventoryPlayer playerInventory;
	private final TileEntityForge tf;

	public GuiForgeBF(InventoryPlayer player, TileEntityForge tf) {
		super(new FContainerBF(player, tf));
		this.tf = tf;
		this.playerInventory = player;
	}
	@Override
	public void initGui() {
		super.initGui();
		this.guiLeft = (this.width - this.xSize) / 2;
		this.guiTop = (this.height - this.ySize) / 2;
		this.buttonList.clear();
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
		this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, 206);

		if (tf.isBurning()) {
			int k = this.getBurnLeftScaled(13);
			this.drawTexturedModalRect(this.guiLeft + 28, this.guiTop + 93 - k, 176, 12 - k, 14, k + 1);
			this.drawTexturedModalRect(this.guiLeft + 46, this.guiTop + 93 - k, 176, 12 - k, 14, k + 1);
			this.drawTexturedModalRect(this.guiLeft + 64, this.guiTop + 93 - k, 176, 12 - k, 14, k + 1);
		}
		int l = this.getCookProgressScaled(24);
		this.drawTexturedModalRect(this.guiLeft + 80, this.guiTop + 80, 176, 14, l + 1, 16);
		if (tf.hasUpgrade(ModObjects.ENERGY_UPGRADE)) {
			this.mc.getTextureManager().bindTexture(WIDGETS);
			int k = this.getEnergyStoredScaled(34);
			this.drawTexturedModalRect(this.guiLeft + 8, this.guiTop + 62, 240, 0, 16, 34);
			this.drawTexturedModalRect(this.guiLeft + 8, this.guiTop + 62, 240, 34, 16, 34 - k);
		}
		if (tf.hasUpgrade(ModObjects.LIQUID_FUEL_UPGRADE)){
			this.mc.getTextureManager().bindTexture(WIDGETS);
			this.drawTexturedModalRect(this.guiLeft + 26, this.guiTop + 98, 192, 38, 20, 22);
			this.zLevel++;
		this.drawTexturedModalRect(this.guiLeft + 26, this.guiTop + 98, 192, 16, 20, 22);
		this.zLevel--;

		int am = getFluidStoredScaled(21);
		if (am > 0) {
			GlStateManager.pushMatrix();
			GlStateManager.translate(0, 21 - am, 0);
			FluidRenderUtil.renderTiledFluid(this.guiLeft + 26, this.guiTop + 98, 20, am, 0, tf.getTank().getFluid());
			GlStateManager.popMatrix();
		}
		}
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		String s = this.tf.getBlockType().getLocalizedName();
		this.fontRenderer.drawString(s, this.xSize / 2 - this.fontRenderer.getStringWidth(s) / 2, 46, 4210752);
		this.fontRenderer.drawString(this.playerInventory.getDisplayName().getUnformattedText(), 60, this.ySize - 92, 4210752);
	}

	@Override
	protected void renderHoveredToolTip(int x, int y) {
		super.renderHoveredToolTip(x, y);
		if (tf.hasUpgrade(ModObjects.LIQUID_FUEL_UPGRADE) && tf.getTank().getFluid() != null && this.isPointInRegion(26, 98 + (21 - getFluidStoredScaled(21)), 20, getFluidStoredScaled(21), x, y)) {
			this.drawHoveringText(I18n.format("gui.betterfurnacesreforged.fluid", tf.getTank().getFluid().getLocalizedName(), tf.getTank().getFluidAmount()), x, y);
		} else if (tf.hasUpgrade(ModObjects.ENERGY_UPGRADE)&& this.isPointInRegion(8,  62, 16, 34, x, y)) {
			this.drawHoveringText(I18n.format("gui.betterfurnacesreforged.energy", tf.getEnergy() / 1000,tf.MAX_ENERGY_STORED() / 1000), x, y);
		}
	}

	private int getEnergyStoredScaled(int pixels) {
		int cur = tf.getEnergy();
		int max = tf.MAX_ENERGY_STORED();
		return getPixels(cur, max, pixels);
	}

	private int getFluidStoredScaled(int pixels) {
		int cur = tf.getTank().getFluidAmount();
		int max = tf.LiquidCapacity();
		return getPixels(cur, max, pixels);
	}

	private int getCookProgressScaled(int pixels) {
		int cur = tf.getCurrentCookTime();
		int max = tf.getCookTime();
		return getPixels(cur, max, pixels);
	}

	private int getBurnLeftScaled(int pixels) {
		if (tf.isEnergy() && tf.getEnergy() >= tf.getEnergyUse()) return pixels;
		return getPixels(tf.getBurnTime(), Math.max(1, tf.getFuelLength()), pixels);
	}

	public TileEntityForge getTE() {
		return tf;
	}

	static int getPixels(float a, float b, int pixels) {
		return (int) Math.floor(a / b * pixels);
	}

}