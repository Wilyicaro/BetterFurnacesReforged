package wily.betterfurnaces.screens;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import wily.betterfurnaces.BetterFurnacesReforged;
import wily.betterfurnaces.inventory.UpgradeContainerBase;
import wily.betterfurnaces.items.ColorUpgradeItem;

@OnlyIn(Dist.CLIENT)
public abstract class AbstractUpgradeScreen<T extends UpgradeContainerBase> extends ContainerScreen<T> {
    public ResourceLocation GUI = new ResourceLocation(BetterFurnacesReforged.MOD_ID + ":" + "textures/container/upgrades_gui.png");
    private final ITextComponent name;

    public AbstractUpgradeScreen(T t, PlayerInventory inv, ITextComponent name) {

        super(t, inv, name);
        this.name = name;
    }

    @Override
    public int getXSize() {
        return 176;
    }

    @Override
    public int getYSize() {
        return 94;
    }

    public int getGuiLeft() {
        return (this.width - getXSize()) / 2;
    }

    public int getGuiTop() {
        return (this.height - getYSize()) / 2;
    }

    @Override
    public void render(MatrixStack matrix, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrix);
        super.render(matrix, mouseX, mouseY, partialTicks);
        renderColorFurnace(matrix, partialTicks, mouseX, mouseY);
    }

    @Override
    protected void renderLabels(MatrixStack matrix, int mouseX, int mouseY) {
        int actualMouseX = mouseX - ((this.width - this.getXSize()) / 2);
        int actualMouseY = mouseY - ((this.height - this.getYSize()) / 2);
    }

    @Override
    protected void init() {
        super.init();
    }

    @Override
    protected void renderBg(MatrixStack matrix, float partialTicks, int mouseX, int mouseY) {
        ItemStack stack = ((ColorUpgradeItem.ContainerColorUpgrade) this.getMenu()).itemStackBeingHeld;
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bind(GUI);
        int relX = (this.width - this.getXSize()) / 2;
        int relY = (this.height - this.getYSize()) / 2;
        this.blit(matrix, relX, relY, 0, 0, this.getXSize(), this.getYSize());
        itemRenderer.renderGuiItem(stack, this.getGuiLeft() + 154, this.getGuiTop() + 6);
        this.font.draw(matrix, name, width / 2 - this.font.width(name) / 2, this.getGuiTop() + 8, 4210752);
    }

    protected void renderColorFurnace(MatrixStack matrix, float partialTicks, int mouseX, int mouseY) {
    }

}
