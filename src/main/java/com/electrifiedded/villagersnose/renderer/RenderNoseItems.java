package com.electrifiedded.villagersnose.renderer;

import com.electrifiedded.archaiclib.v1710.api.client.IItemRenderer;
import com.electrifiedded.villagersnose.renderer.model.ModelNoseArmor;
import com.electrifiedded.villagersnose.renderer.model.ModelSuperNose;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/** 1.7.10 IItemRenderers of the nose and the SuperNose (run through ArchaicLib's IItemRenderer bridge). */
@SideOnly(Side.CLIENT)
public final class RenderNoseItems {
    private RenderNoseItems() {}

    private abstract static class Base implements IItemRenderer {
        final ModelBase model;
        final ResourceLocation texture;

        Base(ModelBase model, String texture) {
            this.model = model;
            this.texture = new ResourceLocation("villagersnose", texture);
        }

        @Override
        public boolean handleRenderType(ItemStack item, ItemRenderType type) {
            return true;
        }

        @Override
        public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
            return true;
        }

        abstract void transform(ItemRenderType type);

        @Override
        public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
            GlStateManager.pushMatrix();
            GlStateManager.enableAlpha();
            transform(type);
            Minecraft.getMinecraft().getTextureManager().bindTexture(this.texture);
            this.model.render(null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.4F);
            GlStateManager.popMatrix();
        }
    }

    public static final class Nose extends Base {
        public Nose() {
            super(new ModelNoseArmor(false), "textures/models/nose_armor.png");
        }

        @Override
        void transform(ItemRenderType type) {
            switch (type) {
                case INVENTORY:
                    GlStateManager.scale(0.7F, 0.7F, 0.7F);
                    GlStateManager.translate(-2.0F, -1.2F, 0.0F);
                    break;
                case ENTITY:
                    GlStateManager.scale(0.3F, 0.3F, 0.3F);
                    GlStateManager.translate(0.0F, 0.5F, 2.0F);
                    break;
                default:
                    GlStateManager.scale(0.5F, 0.5F, 0.5F);
                    GlStateManager.translate(1.5F, 1.0F, 3.0F);
            }
        }
    }

    public static final class SuperNose extends Base {
        public SuperNose() {
            super(new ModelSuperNose(), "textures/models/super_nose.png");
        }

        @Override
        void transform(ItemRenderType type) {
            switch (type) {
                case INVENTORY:
                    GlStateManager.rotate(180.0F, 6.0F, 1.0F, 0.0F);
                    GlStateManager.scale(0.25, 0.25, 0.25);
                    GlStateManager.translate(0.4, 0.9, 0.0);
                    break;
                case ENTITY:
                    GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
                    GlStateManager.rotate(90.0F, 0.0F, 1.0F, 0.0F);
                    GlStateManager.scale(0.1, 0.1, 0.1);
                    GlStateManager.translate(0.0, 1.0, 0.0);
                    break;
                default:
                    GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
                    GlStateManager.scale(0.3, 0.3, 0.3);
                    GlStateManager.translate(-1.0, 0.0, 1.0);
            }
        }
    }
}
