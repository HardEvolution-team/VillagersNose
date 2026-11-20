package com.electrifiedded.villagersnose.renderer.supernose;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.passive.EntityVillager;

import java.util.Random;

/** Small villagers creep in one by one from the screen edges and stare at the player. */
public class AnimationPeek extends SuperNoseAnimation {
    /** start x, start y, end x, end y for each of the six peek spots */
    private static final double[][] SPOTS = {
            {3.0, 3.0, 2.0, 1.5},
            {-4.0, 1.0, -2.5, 0.5},
            {0.0, -3.0, -0.25, -2.0},
            {-3.0, -3.0, -2.5, -2.0},
            {0.0, 3.0, 0.0, 2.0},
            {3.0, -2.0, 2.5, -1.5},
    };

    private final Random rand;
    private final Peeker[] peekers = new Peeker[SPOTS.length];
    private boolean spawnNext = true;

    public AnimationPeek(Random rand) {
        super(800);
        this.rand = rand;
    }

    @Override
    public void render(float partialTicks) {
        GlStateManager.translate(0F, 0F, 1F);
        if (spawnNext) {
            spawnNext = false;
            int slot = rand.nextInt(SPOTS.length);
            if (peekers[slot] == null) peekers[slot] = new Peeker(SPOTS[slot], randomVillager(rand));
        }
        for (Peeker p : peekers) {
            if (p != null) p.render();
        }
    }

    private final class Peeker {
        private final EntityVillager villager;
        private final double endX, endY;
        private double x, y;
        private boolean arrived;

        Peeker(double[] spot, EntityVillager villager) {
            this.villager = villager;
            x = spot[0];
            y = spot[1];
            endX = spot[2];
            endY = spot[3];
        }

        void render() {
            if (Math.abs(x - endX) >= 0.3 || Math.abs(y - endY) >= 0.3) {
                x += (endX - x) * 0.01;
                y += (endY - y) * 0.01;
            } else if (!arrived) {
                arrived = true;
                spawnNext = true;
            }
            float heading = (float) Math.toDegrees(Math.atan2(endY - y, endX - x)) - 90F;
            GlStateManager.pushMatrix();
            GlStateManager.scale(0.4F, 0.6F, 0.5F);
            GlStateManager.translate(x, y, -3.0);
            GlStateManager.rotate(heading, 0F, 0F, 1F);
            GlStateManager.rotate(190F, 0.1F, 1F, 0F);
            draw(villager, 0F);
            GlStateManager.popMatrix();
        }
    }
}
