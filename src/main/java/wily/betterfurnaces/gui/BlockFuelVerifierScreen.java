package wily.betterfurnaces.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import wily.betterfurnaces.BetterFurnacesReforged;
import wily.betterfurnaces.container.BlockFuelVerifierContainer;

@OnlyIn(Dist.CLIENT)
public abstract class BlockFuelVerifierScreen<T extends BlockFuelVerifierContainer> extends ContainerScreen<T> {

    public ResourceLocation GUI = new ResourceLocation(BetterFurnacesReforged.MOD_ID + ":" + "textures/container/fuel_verifier_gui.png");
    public static final ResourceLocation WIDGETS = new ResourceLocation(BetterFurnacesReforged.MOD_ID + ":" + "textures/container/widgets.png");
    PlayerInventory playerInv;
    ITextComponent name;

    public boolean add_button;
    public boolean sub_button;

    public BlockFuelVerifierScreen(T t, PlayerInventory inv, ITextComponent name) {
        super(t, inv, name);
        playerInv = inv;
        this.name = name;
    }
    public static class BlockFuelVerifierScreenDefiniton extends BlockFuelVerifierScreen<BlockFuelVerifierContainer> {
        public BlockFuelVerifierScreenDefiniton(BlockFuelVerifierContainer container, PlayerInventory inv, ITextComponent name) {
            super(container, inv, name);
        }
    }

    @Override
    public void render(MatrixStack matrix, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrix);
        super.render(matrix, mouseX, mouseY, partialTicks);
        this.renderTooltip(matrix, mouseX, mouseY);
    }

    @Override
    protected void init() {
        super.init();
    }


    @Override
    protected void renderLabels(MatrixStack matrix, int mouseX, int mouseY) {
        ITextComponent invname = this.playerInv.getDisplayName();
        ITextComponent burntime = new TranslationTextComponent("gui.betterfurnacesreforged.fuel.melts").append(new StringTextComponent(String.valueOf(((BlockFuelVerifierContainer) this.getMenu()).getBurnTime()))).append( new TranslationTextComponent("gui.betterfurnacesreforged.fuel.items"));
        int actualMouseX = mouseX - ((this.width - this.getXSize()) / 2);
        int actualMouseY = mouseY - ((this.height - this.getYSize()) / 2);
        //drawString(Minecraft.getInstance().fontRenderer, "Energy: " + getMenu().getEnergy(), 10, 10, 0xffffff);
        this.minecraft.font.draw(matrix, invname, 7, this.getYSize() - 93, 4210752);
        if (this.getMenu().getBurnTime() > 0)
        this.minecraft.font.draw(matrix, burntime, this.getXSize() / 2 - this.minecraft.font.width(burntime.getString()) / 2, 6, 4210752);
        else this.minecraft.font.draw(matrix, name, 7 + this.getXSize() / 2 - this.minecraft.font.width(name.getString()) / 2, 6, 4210752);
    }

    private void addTooltips(MatrixStack matrix, int mouseX, int mouseY) {

    }

    @Override
    protected void renderBg(MatrixStack matrix, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bind(GUI);
        int relX = (this.width - this.getXSize()) / 2;
        int relY = (this.height - this.getYSize()) / 2;
        this.blit(matrix, relX, relY, 0, 0, this.getXSize(), this.getYSize());
        int i;
        i = ((BlockFuelVerifierContainer) this.getMenu()).getBurnTimeScaled(26);
        if (i > 0) {
            this.blit(matrix, getGuiLeft() + 74, getGuiTop() + 13 + 26 - i, 176, 24 - i, 28, i + 2);
        }
    }


    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        double actualMouseX = mouseX - ((this.width - this.getXSize()) / 2);
        double actualMouseY = mouseY - ((this.height - this.getYSize()) / 2);
        return super.mouseClicked(mouseX, mouseY, button);
    }

}
