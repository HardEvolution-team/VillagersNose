package com.electrifiedded.villagersnose.renderer.supernose;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.passive.EntityVillager;

import java.util.Random;

/** A villager facing the player and slowly turning left and right. */
public class AnimationRotate extends SuperNoseAnimation {
    private final EntityVillager villager;

    public AnimationRotate(Random rand) {
        super(200);
        villager = randomVillager(rand);
    }

    @Override
    public void render(float partialTicks) {
        float sway = (float) Math.cos(age / 8.0) * 20F;
        GlStateManager.scale(0.3F, 0.59F, 0.3F);
        GlStateManager.translate(0F, -1F, -2F);
        GlStateManager.rotate(180F + sway, 0F, 1F, 0F);
        draw(villager, partialTicks);
    }
}
