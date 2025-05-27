package net.badyfatycaty.explorersofether.screen.custom;

import com.mojang.blaze3d.systems.RenderSystem;
import net.badyfatycaty.explorersofether.ExplorersofEther;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class ForgeScreen extends AbstractContainerScreen<ForgeMenu> {
    private static final ResourceLocation GUI_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(ExplorersofEther.MOD_ID,"textures/gui/container/forge.png");
    private static final ResourceLocation ARROW_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(ExplorersofEther.MOD_ID,"textures/gui/sprites/container/forge/burn_progress.png");
    private static final ResourceLocation FIRE_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(ExplorersofEther.MOD_ID,"textures/gui/sprites/container/forge/lit_progress.png");

    public ForgeScreen(ForgeMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float v, int i, int j) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, GUI_TEXTURE);

        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        guiGraphics.blit(GUI_TEXTURE, x, y, 0, 0, imageWidth, imageHeight);

        if (menu.hasFuel()) {
            int burn = menu.getScaledBurnProgress();
            RenderSystem.setShaderTexture(0, FIRE_TEXTURE);
            // Draw from top, shrink downward as burn decreases
            guiGraphics.blit(FIRE_TEXTURE, x + 81, y + 38 + (14 - burn), 0, 14 - burn, 14, burn, 14, 14);
        }

        if (menu.isCrafting()) {
            int progress = menu.getScaledArrowProgress();
            RenderSystem.setShaderTexture(0, ARROW_TEXTURE);
            guiGraphics.blit(ARROW_TEXTURE, x + 103, y + 36, 0, 0, progress, 16, 24, 16);
        }
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        this.renderTooltip(pGuiGraphics, pMouseX, pMouseY);
    }
}