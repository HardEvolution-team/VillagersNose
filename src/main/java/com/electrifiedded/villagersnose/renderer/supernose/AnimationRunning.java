package com.electrifiedded.villagersnose.renderer.supernose;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.passive.EntityVillager;

import java.util.Random;

/** A villager running across the screen from a random side. */
public class AnimationRunning extends SuperNoseAnimation {
    private static final int DURATION = 400;
    private final EntityVillager villager;
    private final float direction;

    public AnimationRunning(Random rand) {
        super(DURATION);
        villager = randomVillager(rand);
        direction = rand.nextBoolean() ? -1F : 1F;
    }

    @Override
    public void render(float partialTicks) {
        double progress = (age - DURATION / 2) / 30.0;
        GlStateManager.scale(0.35F, 0.67F, 0.35F);
        GlStateManager.translate(0F, -1.5F, -2F);
        GlStateManager.rotate(95F * direction, 0F, 1F, 0F);
        GlStateManager.translate(0.0, 0.0, progress);
        // fake walking: drive the limb swing from the animation clock
        villager.prevLimbSwingAmount = villager.limbSwingAmount;
        villager.limbSwingAmount = (float) Math.cos(age / 8.0);
        villager.limbSwing = (float) Math.sin(age / 4.0);
        draw(villager, partialTicks);
    }
}
