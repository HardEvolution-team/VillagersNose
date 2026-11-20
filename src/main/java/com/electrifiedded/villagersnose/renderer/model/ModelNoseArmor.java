package com.electrifiedded.villagersnose.renderer.model;

import com.electrifiedded.villagersnose.item.ItemNose;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelNoseArmor extends ModelBiped {
    public ModelRenderer modelNose = new ModelRenderer(this, 0, 0);
    private final boolean isArmor;

    public ModelNoseArmor(boolean isArmor) {
        super(0.0F, 0.0F, 0, 0);
        this.isArmor = isArmor;
        this.modelNose.setTextureSize(8, 6);
        this.modelNose.addBox(-1.0F, -1.0F, -6.0F, 2, 4, 2, 0.0F);
        this.modelNose.setRotationPoint(0.0F, -2.0F, 0.0F);
        this.bipedHead.addChild(this.modelNose);
    }

    @Override
    public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        this.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entity);
        this.modelNose.offsetY = 0.0F;
        if (this.isArmor && entity instanceof EntityPlayer
                && ItemNose.searchEmeraldsNearby(entity.world, (int) entity.posX, (int) entity.posY, (int) entity.posZ)) {
            this.modelNose.offsetY = MathHelper.cos(ageInTicks) * 0.03F;
        }
        GlStateManager.pushMatrix();
        if (this.isSneak) GlStateManager.translate(0.0F, 0.2F, 0.0F);
        this.bipedHead.render(scale);
        GlStateManager.popMatrix();
    }
}
