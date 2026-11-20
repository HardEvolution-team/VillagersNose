package com.electrifiedded.villagersnose.renderer.supernose;

import com.electrifiedded.villagersnose.util.NoseUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.passive.EntityVillager;

import java.util.Random;

/** A short overlay "hallucination" played while wearing the SuperNose. Ticks forward until its duration ends. */
public abstract class SuperNoseAnimation {
    private final int duration;
    protected int age;

    protected SuperNoseAnimation(int duration) {
        this.duration = duration;
    }

    public void update() {
        if (age < duration) age++;
    }

    public boolean hasStopped() {
        return age >= duration;
    }

    public abstract void render(float partialTicks);

    protected static EntityVillager randomVillager(Random rand) {
        return NoseUtils.getRandomVillager(Minecraft.getMinecraft().world, rand);
    }

    protected static void draw(EntityVillager v, float partialTicks) {
        Minecraft.getMinecraft().getRenderManager().renderEntity(v, 0, 0, 0, 0F, partialTicks, false);
    }
}
