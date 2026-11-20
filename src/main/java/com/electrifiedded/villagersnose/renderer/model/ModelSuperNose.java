package com.electrifiedded.villagersnose.renderer.model;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * "SuperNose 9000" head piece: nose, glasses with temples and a mustache, all attached to the biped head.
 * The box layout has to match the UV map of the original super_nose.png (35x23), which is served from the
 * player's copy of the original jar.
 */
@SideOnly(Side.CLIENT)
public class ModelSuperNose extends ModelBiped {
    private static final float TILT = -0.1047198F; // ~6 degrees down
    private static final float SPLAY = 0.0698132F; // ~4 degrees outwards

    /** u, v, x, y, z, w, h, d, rotX, rotY */
    private static final float[][] PARTS = {
            {0, 10, -1.0F, -4.0F, -6.0F, 2, 4, 2, 0, 0},           // nose
            {0, 0, -5.0F, -6.0F, -5.0F, 4, 4, 1, 0, 0},            // left lens
            {0, 5, 1.0F, -6.0F, -5.0F, 4, 4, 1, 0, 0},             // right lens
            {8, 10, -1.0F, -5.0F, -5.0F, 2, 1, 1, 0, 0},           // bridge
            {16, 0, -5.2F, -6.0F, -4.5F, 1, 2, 8, TILT, -SPLAY},   // left temple
            {10, 0, 4.2F, -5.0F, 2.5F, 1, 2, 2, TILT, SPLAY},      // right ear hook
            {16, 0, 4.2F, -6.0F, -4.5F, 1, 2, 8, TILT, SPLAY},     // right temple
            {10, 0, -5.2F, -5.0F, 2.5F, 1, 2, 2, TILT, -SPLAY},    // left ear hook
            {0, 18, -3.5F, 0.0F, -5.0F, 7, 1, 1, 0, 0},            // mustache middle
            {0, 16, -2.5F, -1.0F, -5.0F, 5, 1, 1, 0, 0},           // mustache top
            {0, 20, -4.0F, 1.0F, -5.0F, 3, 1, 1, 0, 0},            // mustache left tip
            {0, 20, 1.0F, 1.0F, -5.0F, 3, 1, 1, 0, 0},             // mustache right tip
    };

    public ModelSuperNose() {
        super(0.0F, 0.0F, 0, 0);
        textureWidth = 35;
        textureHeight = 23;
        for (float[] p : PARTS) {
            ModelRenderer part = new ModelRenderer(this, (int) p[0], (int) p[1]);
            part.setTextureSize(textureWidth, textureHeight);
            part.mirror = true;
            part.addBox(p[2], p[3], p[4], (int) p[5], (int) p[6], (int) p[7]);
            part.rotateAngleX = p[8];
            part.rotateAngleY = p[9];
            bipedHead.addChild(part);
        }
    }

    @Override
    public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw,
                       float headPitch, float scale) {
        setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entity);
        GlStateManager.pushMatrix();
        if (isSneak) GlStateManager.translate(0.0F, 0.2F, 0.0F);
        bipedHead.render(scale);
        GlStateManager.popMatrix();
    }
}
