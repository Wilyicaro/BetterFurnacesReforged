package wily.betterfurnaces.gui;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fluids.FluidStack;
import org.lwjgl.glfw.GLFW;
import wily.betterfurnaces.BetterFurnacesReforged;
import wily.betterfurnaces.blocks.BlockForgeBase;
import wily.betterfurnaces.container.BlockForgeContainerBase;
import wily.betterfurnaces.init.Registration;
import wily.betterfurnaces.network.Messages;
import wily.betterfurnaces.network.PacketSettingsButton;
import wily.betterfurnaces.network.PacketOrientationButton;
import wily.betterfurnaces.network.PacketShowSettingsButton;
import wily.betterfurnaces.util.FluidRenderUtil;
import wily.betterfurnaces.util.StringHelper;

import java.util.List;

@OnlyIn(Dist.CLIENT)
public abstract class BlockForgeScreenBase<T extends BlockForgeContainerBase> extends AbstractContainerScreen<T> {

    public ResourceLocation GUI = new ResourceLocation(BetterFurnacesReforged.MOD_ID + ":" + "textures/container/forge_gui.png");
    public static final ResourceLocation WIDGETS = new ResourceLocation(BetterFurnacesReforged.MOD_ID + ":" + "textures/container/widgets.png");
    Inventory playerInv;
    Component name;

    @Override
    public int getXSize() {
        return 176;
    }
    @Override
    public int getYSize() {
        return 206;
    }
    public int getGuiLeft() {
        return (this.width - getXSize()) / 2;
    }

    public int getGuiTop() {
        return (this.height - getYSize()) / 2;
    }


    public boolean add_button;
    public boolean sub_button;

    public BlockForgeScreenBase(T t, Inventory inv, Component name) {
        super(t, inv, name);
        playerInv = inv;
        this.name = name;
    }

    @Override
    public void render(PoseStack matrix, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrix);
        super.render(matrix, mouseX, mouseY, partialTicks);
        this.renderTooltip(matrix, mouseX, mouseY);
    }

    @Override
    protected void init() {
        super.init();
    }


    @Override
    protected void renderLabels(PoseStack matrix, int mouseX, int mouseY) {
        Component invname = this.playerInv.getDisplayName();
        int actualMouseX = mouseX - ((this.width - this.getXSize()) / 2);
        int actualMouseY = mouseY - ((this.height - this.getYSize()) / 2);
        this.minecraft.font.draw(matrix, invname, this.getXSize() / 2 - this.minecraft.font.width(invname.getString()) / 2, this.getYSize() - 112, 4210752);
        this.minecraft.font.draw(matrix, name, 7 + this.getXSize() / 2 - this.minecraft.font.width(name.getString()) / 2, 26, 4210752);
        if (getMenu().te.hasUpgrade(Registration.LIQUID.get()) &&
                (mouseX > getGuiLeft() + 26 && mouseX < getGuiLeft() + 45 && mouseY > getGuiTop() + 98 && mouseY < getGuiTop() + 128))
            this.renderTooltip(matrix, new TextComponent(((BlockForgeContainerBase) this.getMenu()).getFluidStackStored().getDisplayName().getString() + ": " +((BlockForgeContainerBase) this.getMenu()).getFluidStackStored().getAmount() + " mB"), actualMouseX, actualMouseY);
        if (getMenu().te.hasUpgrade(Registration.ENERGY.get()) &&
                (mouseX > getGuiLeft() + 8 && mouseX < getGuiLeft() + 24 && mouseY > getGuiTop() + 62 && mouseY < getGuiTop() + 96))
            this.renderTooltip(matrix, new TextComponent(((BlockForgeContainerBase) this.getMenu()).getEnergyStored()/1000 + " kFE/" + ((BlockForgeContainerBase) this.getMenu()).getEnergyMaxStored()/1000 + "kFE"), actualMouseX, actualMouseY);
        if (getMenu().te.hasUpgrade(Registration.FACTORY.get())) {
            if (this.getMenu().showInventoryButtons() && this.getMenu().getRedstoneMode() == 4) {
                int comSub = this.getMenu().getComSub();
                int i = comSub > 9 ? 28 : 31;
                this.minecraft.font.draw(matrix, new TextComponent("" + comSub), i - 42, 118, 4210752);
            }

            this.addTooltips(matrix, actualMouseX, actualMouseY);
        }

    }

    private void addTooltips(PoseStack matrix, int mouseX, int mouseY) {
        if (!getMenu().showInventoryButtons()) {
            if (mouseX >= -20 && mouseX <= 0 && mouseY >= 52 && mouseY <= 74) {
                this.renderTooltip(matrix, new TranslatableComponent("tooltip." + BetterFurnacesReforged.MOD_ID + ".gui_open"), mouseX, mouseY);
            }
        } else {
            if (mouseX >= -13 && mouseX <= 0 && mouseY >= 52 && mouseY <= 74) {
                this.renderComponentTooltip(matrix, StringHelper.getShiftInfoGui(), mouseX, mouseY);
            } else if (mouseX >= -47 && mouseX <= -34 && mouseY >= 60 && mouseY <= 73) {
                List<Component> list = Lists.newArrayList();
                list.add(new TranslatableComponent("tooltip." + BetterFurnacesReforged.MOD_ID + ".gui_auto_input"));
                list.add(new TextComponent("" + this.getMenu().getAutoInput()));
                this.renderComponentTooltip(matrix, list, mouseX, mouseY);
            } else if(mouseX >= -15 && mouseX <= -2 && mouseY >= 75 && mouseY <= 88) {
                this.renderTooltip(matrix, new TranslatableComponent("tooltip." + BetterFurnacesReforged.MOD_ID + ".gui_show_orientation"), mouseX, mouseY);
            }else if (mouseX >= -29 && mouseX <= -16 && mouseY >= 60 && mouseY <= 73) {
                List<Component> list = Lists.newArrayList();
                list.add(new TranslatableComponent("tooltip." + BetterFurnacesReforged.MOD_ID + ".gui_auto_output"));
                list.add(new TextComponent("" + this.getMenu().getAutoOutput()));
                this.renderComponentTooltip(matrix, list, mouseX, mouseY);
            } else if (mouseX >= -32 && mouseX <= -23 && mouseY >= 79 && mouseY <= 88) {
                List<Component> list = Lists.newArrayList();
                list.add(new TranslatableComponent("tooltip." + BetterFurnacesReforged.MOD_ID + ".gui_top"));
                list.add(this.getMenu().getTooltip(1));
                this.renderComponentTooltip(matrix, list, mouseX, mouseY);
            } else if (mouseX >= -32 && mouseX <= -23 && mouseY >= 103 && mouseY <= 112) {
                List<Component> list = Lists.newArrayList();
                list.add(new TranslatableComponent("tooltip." + BetterFurnacesReforged.MOD_ID + ".gui_bottom"));
                list.add(this.getMenu().getTooltip(0));
                this.renderComponentTooltip(matrix, list, mouseX, mouseY);
            } else if (mouseX >= -32 && mouseX <= -23 && mouseY >= 91 && mouseY <= 100) {
                List<Component> list = Lists.newArrayList();
                if (isShiftKeyDown()) {
                    list.add(new TranslatableComponent("tooltip." + BetterFurnacesReforged.MOD_ID + ".gui_reset"));
                } else {
                    list.add(new TranslatableComponent("tooltip." + BetterFurnacesReforged.MOD_ID + ".gui_front"));
                    list.add(this.getMenu().getTooltip(this.getMenu().getIndexFront()));
                }
                this.renderComponentTooltip(matrix, list, mouseX, mouseY);
            } else if (mouseX >= -44 && mouseX <= -35 && mouseY >= 91 && mouseY <= 100) {
                List<Component> list = Lists.newArrayList();
                list.add(new TranslatableComponent("tooltip." + BetterFurnacesReforged.MOD_ID + ".gui_left"));
                list.add(this.getMenu().getTooltip(this.getMenu().getIndexLeft()));
                this.renderComponentTooltip(matrix, list, mouseX, mouseY);
            } else if (mouseX >= -20 && mouseX <= -11 && mouseY >= 91 && mouseY <= 100) {
                List<Component> list = Lists.newArrayList();
                list.add(new TranslatableComponent("tooltip." + BetterFurnacesReforged.MOD_ID + ".gui_right"));
                list.add(this.getMenu().getTooltip(this.getMenu().getIndexRight()));
                this.renderComponentTooltip(matrix, list, mouseX, mouseY);
            } else if (mouseX >= -20 && mouseX <= -11 && mouseY >= 103 && mouseY <= 112) {
                List<Component> list = Lists.newArrayList();
                list.add(new TranslatableComponent("tooltip." + BetterFurnacesReforged.MOD_ID + ".gui_back"));
                list.add(this.getMenu().getTooltip(this.getMenu().getIndexBack()));
                this.renderComponentTooltip(matrix, list, mouseX, mouseY);
            } else if (mouseX >= -47 && mouseX <= -34 && mouseY >= 118 && mouseY <= 131) {
                List<Component> list = Lists.newArrayList();
                list.add(new TranslatableComponent("tooltip." + BetterFurnacesReforged.MOD_ID + ".gui_redstone_ignored"));
                this.renderComponentTooltip(matrix, list, mouseX, mouseY);
            } else if (mouseX >= -31 && mouseX <= -18 && mouseY >= 118 && mouseY <= 131) {
                List<Component> list = Lists.newArrayList();
                if (isShiftKeyDown()) {
                    list.add(new TranslatableComponent("tooltip." + BetterFurnacesReforged.MOD_ID + ".gui_redstone_low"));
                } else {
                    list.add(new TranslatableComponent("tooltip." + BetterFurnacesReforged.MOD_ID + ".gui_redstone_high"));
                }
                this.renderComponentTooltip(matrix, list, mouseX, mouseY);
            } else if (mouseX >= -15 && mouseX <= -2 && mouseY >= 118 && mouseY <= 131) {
                List<Component> list = Lists.newArrayList();
                list.add(new TranslatableComponent("tooltip." + BetterFurnacesReforged.MOD_ID + ".gui_redstone_comparator"));
                this.renderComponentTooltip(matrix, list, mouseX, mouseY);
            } else if (mouseX >= -47 && mouseX <= -34 && mouseY >= 134 && mouseY <= 147) {
                List<Component> list = Lists.newArrayList();
                list.add(new TranslatableComponent("tooltip." + BetterFurnacesReforged.MOD_ID + ".gui_redstone_comparator_sub"));
                this.renderComponentTooltip(matrix, list, mouseX, mouseY);
            }

        }
    }

    @Override
    protected void renderBg(PoseStack matrix, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.clearColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, GUI);;
        int relX = (this.width - this.getXSize()) / 2;
        int relY = (this.height - this.getYSize()) / 2;
        this.blit(matrix, relX, relY, 0, 0, this.getXSize(), this.getYSize());
        int i;
        if (((BlockForgeContainerBase) this.getMenu()).BurnTimeGet() > 0) {
            i = ((BlockForgeContainerBase) this.getMenu()).getBurnLeftScaled(13);
             this.blit(matrix, getGuiLeft() + 28, getGuiTop() + 93 - i, 176, 12 - i, 14, i + 1);
             this.blit(matrix, getGuiLeft() + 46, getGuiTop() + 93 - i, 176, 12 - i, 14, i + 1);
             this.blit(matrix, getGuiLeft() + 64, getGuiTop() + 93 - i, 176, 12 - i, 14, i + 1);
        }

        i = ((BlockForgeContainerBase) this.getMenu()).getCookScaled(24);
        this.blit(matrix, getGuiLeft() + 80, getGuiTop() + 80, 176, 14, i + 1, 16);
        if (getMenu().te.hasUpgrade(Registration.ENERGY.get())){
            RenderSystem.setShaderTexture(0, WIDGETS);
            i = ((BlockForgeContainerBase) this.getMenu()).getEnergyStoredScaled(34);
            this.blit(matrix, getGuiLeft() + 8, getGuiTop() + 62, 240, 0, 16, 34);
            this.blit(matrix, getGuiLeft() + 8, getGuiTop() + 62, 240, 34, 16, 34-i);
        }
        if (getMenu().te.hasUpgrade(Registration.LIQUID.get())){
            RenderSystem.setShaderTexture(0, WIDGETS);
            this.blit(matrix, getGuiLeft() + 26, getGuiTop() + 98, 192, 38, 20, 22);
            FluidStack fluid =  ((BlockForgeContainerBase) this.getMenu()).getFluidStackStored();
            i = ((BlockForgeContainerBase) this.getMenu()).getFluidStoredScaled(21);
            if (i > 0) {
                TextureAtlasSprite fluidSprite = this.minecraft.getTextureAtlas(InventoryMenu.BLOCK_ATLAS)
                        .apply(fluid.getFluid().getAttributes().getStillTexture(fluid)
                        );
                RenderSystem.setShaderTexture(0, fluidSprite.atlas().location());
                this.blit(matrix, this.getGuiLeft() + 26, this.getGuiTop() + 98, this.getBlitOffset(), 20, 22, fluidSprite);
                RenderSystem.setShaderTexture(0, WIDGETS);
                this.blit(matrix, getGuiLeft() + 26, getGuiTop() + 98, 192, 38, 20, 22-i);

            }
            RenderSystem.setShaderTexture(0, WIDGETS);
            this.blit(matrix, getGuiLeft() + 26, getGuiTop() + 98, 192, 16, 20, 22);
        }
        if (this.getMenu().te.hasUpgrade(Registration.XP.get())){
            RenderSystem.setShaderTexture(0, WIDGETS);
            this.blit(matrix, getGuiLeft() + 126, getGuiTop() + 102, 208, 0, 16, 16);
            FluidStack fluid =  ((BlockForgeContainerBase) this.getMenu()).getFluidStackStored();
            i = ((BlockForgeContainerBase) this.getMenu()).getFluidStoredScaled(16);
            if (i > 0) {
                FluidRenderUtil.renderTiledFluid(matrix, this, 126, 102, 16, 16, fluid, false);
                RenderSystem.setShaderTexture(0, WIDGETS);
                this.blit(matrix, getGuiLeft() + 126, getGuiTop() + 102, 208, 0, 16, 16-i);

            }
            RenderSystem.setShaderTexture(0, WIDGETS);
            this.blit(matrix, getGuiLeft() + 126, getGuiTop() + 102, 192, 0, 16, 16);
        }
        if (getMenu().te.hasUpgrade(Registration.FACTORY.get())) {
            RenderSystem.setShaderTexture(0, WIDGETS);
            int actualMouseX = mouseX - ((this.width - this.getXSize()) / 2);
            int actualMouseY = mouseY - ((this.height - this.getYSize()) / 2);

            this.addInventoryButtons(matrix, actualMouseX, actualMouseY);
            this.addRedstoneButtons(matrix, actualMouseX, actualMouseY);
        }
    }


    private void addRedstoneButtons(PoseStack matrix, int mouseX, int mouseY) {
        if (this.getMenu().showInventoryButtons()) {
            this.blitRedstone(matrix);
            if (this.getMenu().getRedstoneMode() == 4) {
                int comSub = getMenu().getComSub();
                boolean flag = isShiftKeyDown();
                if (flag) {
                    if (comSub > 0) {
                        this.sub_button = true;
                        if (mouseX >= -31 && mouseX <= -18 && mouseY >= 86 && mouseY <= 147) {
                            this.blit(matrix, getGuiLeft() - 31, getGuiTop() + 134, 14, 0, 14, 14);
                        } else {
                            this.blit(matrix, getGuiLeft() - 31, getGuiTop() + 134, 0, 0, 14, 14);
                        }
                    } else {
                        this.sub_button = false;
                        this.blit(matrix, getGuiLeft() - 31, getGuiTop() + 134, 28, 0, 14, 14);
                    }

                } else {
                    if (comSub < 15) {
                        this.add_button = true;
                        if (mouseX >= -31 && mouseX <= -18 && mouseY >= 134 && mouseY <= 147) {
                            this.blit(matrix, getGuiLeft() - 31, getGuiTop() + 134, 14, 14, 14, 14);
                        } else {
                            this.blit(matrix, getGuiLeft() - 31, getGuiTop() + 134, 0, 14, 14, 14);
                        }
                    } else {
                        this.add_button = false;
                        this.blit(matrix, getGuiLeft() - 31, getGuiTop() + 134, 28, 14, 14, 14);

                    }
                }
            }
        }
    }

    private void addInventoryButtons(PoseStack matrix, int mouseX, int mouseY) {
        if (!getMenu().showInventoryButtons()) {
            this.blit(matrix, getGuiLeft() - 13, getGuiTop() + 52, 0, 28, 16, 19);
        } else if (getMenu().showInventoryButtons()) {
            this.blit(matrix, getGuiLeft() - 56, getGuiTop() + 52, 0, 47, 59, 107);
            if (mouseX >= -47 && mouseX <= -34 && mouseY >= 60 && mouseY <= 73 || this.getMenu().getAutoInput()) {
                this.blit(matrix, getGuiLeft() - 47, getGuiTop() + 60, 0, 189, 14, 14);
            }
            if (mouseX >= -29 && mouseX <= -16 && mouseY >= 60 && mouseY <= 73 || this.getMenu().getAutoOutput()) {
                this.blit(matrix, getGuiLeft() - 29, getGuiTop() + 60, 14, 189, 14, 14);
            }
            this.blit(matrix, getGuiLeft() - 15, getGuiTop() + 75, 168, 189, 14, 14);
            if (mouseX >= -15 && mouseX <= -2 && mouseY >= 75 && mouseY <= 88 || this.getMenu().te.getBlockState().getValue(BlockForgeBase.SHOW_ORIENTATION)){
                this.blit(matrix, getGuiLeft() - 15, getGuiTop() + 75, 182, 189, 14, 14);
            }
            this.blitIO(matrix);
        }


    }

    private void blitRedstone(PoseStack matrix) {
        boolean flag = isShiftKeyDown();
        if (flag) {
            this.blit(matrix, getGuiLeft() - 31, getGuiTop() + 118, 84, 189, 14, 14);
        }
        int setting = this.getMenu().getRedstoneMode();
        if (setting == 0) {
            this.blit(matrix, getGuiLeft() - 47, getGuiTop() + 118, 28, 189, 14, 14);
        } else if (setting == 1 && !flag) {
            this.blit(matrix, getGuiLeft() - 31, getGuiTop() + 118, 42, 189, 14, 14);
        } else if (setting == 2) {
            this.blit(matrix, getGuiLeft() - 31, getGuiTop() + 118, 98, 189, 14, 14);
        } else if (setting == 3) {
            this.blit(matrix, getGuiLeft() - 15, getGuiTop() + 118, 56, 189, 14, 14);
        } else if (setting == 4) {
            this.blit(matrix, getGuiLeft() - 47, getGuiTop() + 134, 70, 189, 14, 14);
        }

    }

    private void blitIO(PoseStack matrix) {
        int[] settings = new int[]{0, 0, 0, 0, 0, 0};
        int setting = this.getMenu().getSettingTop();
        if (setting == 1) {
            this.blit(matrix, getGuiLeft() - 32, getGuiTop() + 79, 0, 161, 10, 10);
        } else if (setting == 2) {
            this.blit(matrix, getGuiLeft() - 32, getGuiTop() + 79, 10, 161, 10, 10);
        } else if (setting == 3) {
            this.blit(matrix, getGuiLeft() - 32, getGuiTop() + 79, 20, 161, 10, 10);
        } else if (setting == 4) {
            this.blit(matrix, getGuiLeft() - 32, getGuiTop() + 79, 30, 161, 10, 10);
        }
        settings[1] = setting;

        setting = this.getMenu().getSettingBottom();
        if (setting == 1) {
            this.blit(matrix, getGuiLeft() - 32, getGuiTop() + 103, 0, 161, 10, 10);
        } else if (setting == 2) {
            this.blit(matrix, getGuiLeft() - 32, getGuiTop() + 103, 10, 161, 10, 10);
        } else if (setting == 3) {
            this.blit(matrix, getGuiLeft() - 32, getGuiTop() + 103, 20, 161, 10, 10);
        } else if (setting == 4) {
            this.blit(matrix, getGuiLeft() - 32, getGuiTop() + 103, 30, 161, 10, 10);
        }
        settings[0] = setting;
        setting = this.getMenu().getSettingFront();
        if (setting == 1) {
            this.blit(matrix, getGuiLeft() - 32, getGuiTop() + 91, 0, 161, 10, 10);
        } else if (setting == 2) {
            this.blit(matrix, getGuiLeft() - 32, getGuiTop() + 91, 10, 161, 10, 10);
        } else if (setting == 3) {
            this.blit(matrix, getGuiLeft() - 32, getGuiTop() + 91, 20, 161, 10, 10);
        } else if (setting == 4) {
            this.blit(matrix, getGuiLeft() - 32, getGuiTop() + 91, 30, 161, 10, 10);
        }
        settings[this.getMenu().getIndexFront()] = setting;
        setting = this.getMenu().getSettingBack();
        if (setting == 1) {
            this.blit(matrix, getGuiLeft() - 20, getGuiTop() + 103, 0, 161, 10, 10);
        } else if (setting == 2) {
            this.blit(matrix, getGuiLeft() - 20, getGuiTop() + 103, 10, 161, 10, 10);
        } else if (setting == 3) {
            this.blit(matrix, getGuiLeft() - 20, getGuiTop() + 103, 20, 161, 10, 10);
        } else if (setting == 4) {
            this.blit(matrix, getGuiLeft() - 20, getGuiTop() + 103, 30, 161, 10, 10);
        }
        settings[this.getMenu().getIndexBack()] = setting;
        setting = this.getMenu().getSettingLeft();
        if (setting == 1) {
            this.blit(matrix, getGuiLeft() - 44, getGuiTop() + 91, 0, 161, 10, 10);
        } else if (setting == 2) {
            this.blit(matrix, getGuiLeft() - 44, getGuiTop() + 91, 10, 161, 10, 10);
        } else if (setting == 3) {
            this.blit(matrix, getGuiLeft() - 44, getGuiTop() + 91, 20, 161, 10, 10);
        } else if (setting == 4) {
            this.blit(matrix, getGuiLeft() - 44, getGuiTop() + 91, 30, 161, 10, 10);
        }
        settings[this.getMenu().getIndexLeft()] = setting;
        setting = this.getMenu().getSettingRight();
        if (setting == 1) {
            this.blit(matrix, getGuiLeft() - 20, getGuiTop() + 91, 0, 161, 10, 10);
        } else if (setting == 2) {
            this.blit(matrix, getGuiLeft() - 20, getGuiTop() + 91, 10, 161, 10, 10);
        } else if (setting == 3) {
            this.blit(matrix, getGuiLeft() - 20, getGuiTop() + 91, 20, 161, 10, 10);
        } else if (setting == 4) {
            this.blit(matrix, getGuiLeft() - 20, getGuiTop() + 91, 30, 161, 10, 10);
        }
        settings[this.getMenu().getIndexRight()] = setting;
        boolean input = false;
        boolean output = false;
        boolean both = false;
        boolean fuel = false;
        for (int set : settings) {
            if (set == 1) {
                input = true;
            } else if (set == 2) {
                output = true;
            } else if (set == 3) {
                both = true;
            } else if (set == 4) {
                fuel = true;
            }
        }
        if (input || both) {
            this.blit(matrix, getGuiLeft() + 26, getGuiTop() + 61, 0, 171, 18, 18);
            this.blit(matrix, getGuiLeft() + 44, getGuiTop() + 61, 0, 171, 18, 18);
            this.blit(matrix, getGuiLeft() + 62, getGuiTop() + 61, 0, 171, 18, 18);
        }
        if (output || both) {
            this.blit(matrix, getGuiLeft() + 103, getGuiTop() + 75, 0, 229, 62, 26);
        }
        if (fuel) {
            this.blit(matrix, getGuiLeft() + 7, getGuiTop() + 99, 18, 171, 18, 18);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        double actualMouseX = mouseX - ((this.width - this.getXSize()) / 2);
        double actualMouseY = mouseY - ((this.height - this.getYSize()) / 2);
        this.mouseClickedRedstoneButtons(actualMouseX, actualMouseY);
        this.mouseClickedInventoryButtons(button, actualMouseX, actualMouseY);
        return super.mouseClicked(mouseX, mouseY, button);
    }

    public void mouseClickedInventoryButtons(int button, double mouseX, double mouseY) {
        boolean flag = button == GLFW.GLFW_MOUSE_BUTTON_2;
        if (!getMenu().showInventoryButtons()) {
            if (mouseX >= -10 && mouseX <= 0 && mouseY >= 52 && mouseY <= 74) {
                Messages.INSTANCE.sendToServer(new PacketShowSettingsButton(this.getMenu().getPos(), 1));
                Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1F));
            }
        } else {
            if (mouseX >= -10 && mouseX <= 0 && mouseY >= 52 && mouseY <= 74) {
                Messages.INSTANCE.sendToServer(new PacketShowSettingsButton(this.getMenu().getPos(), 0));
                Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1F));
            } else if (mouseX >= -47 && mouseX <= -34 && mouseY >= 60 && mouseY <= 73) {
                if (!this.getMenu().getAutoInput()) {
                    Messages.INSTANCE.sendToServer(new PacketSettingsButton(this.getMenu().getPos(), 6, 1));
                    Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 0.6F, 0.3F));
                } else {
                    Messages.INSTANCE.sendToServer(new PacketSettingsButton(this.getMenu().getPos(), 6, 0));
                    Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 0.6F, 0.3F));
                }

            } else if (mouseX >= -15 && mouseX <= -2 && mouseY >= 75 && mouseY <= 88) {
                if (!this.getMenu().te.getBlockState().getValue(BlockForgeBase.SHOW_ORIENTATION)) {
                    Messages.INSTANCE.sendToServer(new PacketOrientationButton(this.getMenu().getPos(), true));
                    Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 0.6F, 0.3F));
                } else {
                    Messages.INSTANCE.sendToServer(new PacketOrientationButton(this.getMenu().getPos(), false));
                    Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 0.6F, 0.3F));
                }

            }  else if (mouseX >= -29 && mouseX <= -16 && mouseY >= 60 && mouseY <= 73) {
                if (!this.getMenu().getAutoOutput()) {
                    Messages.INSTANCE.sendToServer(new PacketSettingsButton(this.getMenu().getPos(), 7, 1));
                    Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 0.6F, 0.3F));
                } else {
                    Messages.INSTANCE.sendToServer(new PacketSettingsButton(this.getMenu().getPos(), 7, 0));
                    Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 0.6F, 0.3F));
                }
            } else if (mouseX >= -32 && mouseX <= -23 && mouseY >= 79 && mouseY <= 88) {
                if (flag) {
                    sendToServerInverted(this.getMenu().getSettingTop(), 1);
                } else {
                    sendToServer(this.getMenu().getSettingTop(), 1);
                }
            } else if (mouseX >= -32 && mouseX <= -23 && mouseY >= 103 && mouseY <= 112) {
                if (flag) {
                    sendToServerInverted(this.getMenu().getSettingBottom(), 0);
                } else {
                    sendToServer(this.getMenu().getSettingBottom(), 0);
                }
            } else if (mouseX >= -32 && mouseX <= -23 && mouseY >= 91 && mouseY <= 100) {
                if (isShiftKeyDown()) {
                    Messages.INSTANCE.sendToServer(new PacketSettingsButton(this.getMenu().getPos(), 0, 0));
                    Messages.INSTANCE.sendToServer(new PacketSettingsButton(this.getMenu().getPos(), 1, 0));
                    Messages.INSTANCE.sendToServer(new PacketSettingsButton(this.getMenu().getPos(), 2, 0));
                    Messages.INSTANCE.sendToServer(new PacketSettingsButton(this.getMenu().getPos(), 3, 0));
                    Messages.INSTANCE.sendToServer(new PacketSettingsButton(this.getMenu().getPos(), 4, 0));
                    Messages.INSTANCE.sendToServer(new PacketSettingsButton(this.getMenu().getPos(), 5, 0));
                    Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 0.8F, 0.3F));
                } else {
                    if (flag) {
                        sendToServerInverted(this.getMenu().getSettingFront(), this.getMenu().getIndexFront());
                    } else {
                        sendToServer(this.getMenu().getSettingFront(), this.getMenu().getIndexFront());
                    }
                }
            } else if (mouseX >= -20 && mouseX <= -11 && mouseY >= 103 && mouseY <= 112) {
                if (flag) {
                    sendToServerInverted(this.getMenu().getSettingBack(), this.getMenu().getIndexBack());
                } else {
                    sendToServer(this.getMenu().getSettingBack(), this.getMenu().getIndexBack());
                }
            } else if (mouseX >= -44 && mouseX <= -35 && mouseY >= 91 && mouseY <= 100) {
                if (flag) {
                    sendToServerInverted(this.getMenu().getSettingLeft(), this.getMenu().getIndexLeft());
                } else {
                    sendToServer(this.getMenu().getSettingLeft(), this.getMenu().getIndexLeft());
                }
            } else if (mouseX >= -20 && mouseX <= -11 && mouseY >= 91 && mouseY <= 100) {
                if (flag) {
                    sendToServerInverted(this.getMenu().getSettingRight(), this.getMenu().getIndexRight());
                } else {
                    sendToServer(this.getMenu().getSettingRight(), this.getMenu().getIndexRight());
                }
            }
        }
    }

    private void sendToServer(int setting, int index) {
        Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 0.6F, 0.3F));
        if (setting <= 0) {
            Messages.INSTANCE.sendToServer(new PacketSettingsButton(this.getMenu().getPos(), index, 1));
        } else if (setting == 1) {
            Messages.INSTANCE.sendToServer(new PacketSettingsButton(this.getMenu().getPos(), index, 2));
        } else if (setting == 2) {
            Messages.INSTANCE.sendToServer(new PacketSettingsButton(this.getMenu().getPos(), index, 3));
        } else if (setting == 3) {
            Messages.INSTANCE.sendToServer(new PacketSettingsButton(this.getMenu().getPos(), index, 4));
        } else if (setting >= 4) {
            Messages.INSTANCE.sendToServer(new PacketSettingsButton(this.getMenu().getPos(), index, 0));
        }
    }

    private void sendToServerInverted(int setting, int index) {
        Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 0.3F, 0.3F));
        if (setting <= 0) {
            Messages.INSTANCE.sendToServer(new PacketSettingsButton(this.getMenu().getPos(), index, 4));
        } else if (setting == 1) {
            Messages.INSTANCE.sendToServer(new PacketSettingsButton(this.getMenu().getPos(), index, 0));
        } else if (setting == 2) {
            Messages.INSTANCE.sendToServer(new PacketSettingsButton(this.getMenu().getPos(), index, 1));
        } else if (setting == 3) {
            Messages.INSTANCE.sendToServer(new PacketSettingsButton(this.getMenu().getPos(), index, 2));
        } else if (setting >= 4) {
            Messages.INSTANCE.sendToServer(new PacketSettingsButton(this.getMenu().getPos(), index, 3));
        }
    }

    public void mouseClickedRedstoneButtons(double mouseX, double mouseY) {
        if (getMenu().showInventoryButtons()) {
            if (mouseX >= -31 && mouseX <= -18 && mouseY >= 132 && mouseY <= 147) {
                if (this.sub_button && isShiftKeyDown()) {
                    Messages.INSTANCE.sendToServer(new PacketSettingsButton(this.getMenu().getPos(), 9, this.getMenu().getComSub() - 1));
                    Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 0.3F, 0.3F));
                }
            }
            if (mouseX >= -31 && mouseX <= -18 && mouseY >= 132 && mouseY <= 147) {
                if (this.add_button && !isShiftKeyDown()) {
                    Messages.INSTANCE.sendToServer(new PacketSettingsButton(this.getMenu().getPos(), 9, this.getMenu().getComSub() + 1));
                    Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 0.6F, 0.3F));
                }
            }
            if (mouseX >= -47 && mouseX <= -34 && mouseY >= 118 && mouseY <= 131) {
                if (this.getMenu().getRedstoneMode() != 0) {
                    Messages.INSTANCE.sendToServer(new PacketSettingsButton(this.getMenu().getPos(), 8, 0));
                    Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 0.6F, 0.3F));
                }
            }

            if (mouseX >= -31 && mouseX <= -18 && mouseY >= 118 && mouseY <= 131) {
                if (this.getMenu().getRedstoneMode() != 1 && !isShiftKeyDown()) {
                    Messages.INSTANCE.sendToServer(new PacketSettingsButton(this.getMenu().getPos(), 8, 1));
                    Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 0.6F, 0.3F));
                }
                if (this.getMenu().getRedstoneMode() != 2 && isShiftKeyDown()) {
                    Messages.INSTANCE.sendToServer(new PacketSettingsButton(this.getMenu().getPos(), 8, 2));
                    Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 0.6F, 0.3F));
                }
            }

            if (mouseX >= -15 && mouseX <= -2 && mouseY >= 118 && mouseY <= 131) {
                if (this.getMenu().getRedstoneMode() != 3) {
                    Messages.INSTANCE.sendToServer(new PacketSettingsButton(this.getMenu().getPos(), 8, 3));
                    Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 0.6F, 0.3F));
                }
            }

            if (mouseX >= -47 && mouseX <= -34 && mouseY >= 134 && mouseY <= 147) {
                if (this.getMenu().getRedstoneMode() != 4) {
                    Messages.INSTANCE.sendToServer(new PacketSettingsButton(this.getMenu().getPos(), 8, 4));
                    Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 0.6F, 0.3F));
                }
            }
        }
    }

    public static boolean isShiftKeyDown() {
        return isKeyDown(GLFW.GLFW_KEY_LEFT_SHIFT) || isKeyDown(GLFW.GLFW_KEY_RIGHT_SHIFT);
    }

    public static boolean isKeyDown(int glfw) {
        InputConstants.Key key = InputConstants.Type.KEYSYM.getOrCreate(glfw);
        int keyCode = key.getValue();
        if (keyCode != InputConstants.UNKNOWN.getValue()) {
            long windowHandle = Minecraft.getInstance().getWindow().getWindow();
            try {
                if (key.getType() == InputConstants.Type.KEYSYM) {
                    return InputConstants.isKeyDown(windowHandle, keyCode);
                } /**else if (key.getType() == InputMappings.Type.MOUSE) {
                 return GLFW.glfwGetMouseButton(windowHandle, keyCode) == GLFW.GLFW_PRESS;
                 }**/
            } catch (Exception ignored) {
            }
        }
        return false;
    }
}
