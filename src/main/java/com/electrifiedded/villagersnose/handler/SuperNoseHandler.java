package com.electrifiedded.villagersnose.handler;

import com.electrifiedded.villagersnose.VillagersNose;
import com.electrifiedded.villagersnose.renderer.supernose.AnimationPeek;
import com.electrifiedded.villagersnose.renderer.supernose.AnimationRotate;
import com.electrifiedded.villagersnose.renderer.supernose.AnimationRunning;
import com.electrifiedded.villagersnose.renderer.supernose.SuperNoseAnimation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import java.util.Random;

/** "Hallucinations" drawn over the screen while wearing the SuperNose 9000. */
@SideOnly(Side.CLIENT)
public class SuperNoseHandler {
    private SuperNoseAnimation animation;
    private final Random random = new Random();

    private static boolean wearing() {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.player == null) return false;
        ItemStack helmet = mc.player.getItemStackFromSlot(EntityEquipmentSlot.HEAD);
        return helmet.getItem() == VillagersNose.itemSuperNose;
    }

    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent.Post event) {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.currentScreen != null || mc.world == null || event.getType() != RenderGameOverlayEvent.ElementType.ALL) return;
        if (!wearing()) {
            this.animation = null;
            return;
        }
        if (this.animation == null) {
            this.addAnimation();
            return;
        }
        if (this.animation.hasStopped()) {
            this.animation = null;
            return;
        }
        float pt = event.getPartialTicks();
        GlStateManager.pushAttrib();
        GlStateManager.matrixMode(GL11.GL_PROJECTION);
        GlStateManager.pushMatrix();
        GlStateManager.loadIdentity();
        GlStateManager.matrixMode(GL11.GL_MODELVIEW);
        GlStateManager.pushMatrix();
        GlStateManager.loadIdentity();
        GlStateManager.clear(GL11.GL_DEPTH_BUFFER_BIT);
        GlStateManager.enableDepth();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        RenderHelper.enableStandardItemLighting();
        try {
            this.animation.render(pt);
        } finally {
            RenderHelper.disableStandardItemLighting();
            GlStateManager.disableDepth();
            GlStateManager.matrixMode(GL11.GL_PROJECTION);
            GlStateManager.popMatrix();
            GlStateManager.matrixMode(GL11.GL_MODELVIEW);
            GlStateManager.popMatrix();
            GlStateManager.popAttrib();
            // pushAttrib does not sync GlStateManager's cache: reset what the HUD expects explicitly
            GlStateManager.disableLighting();
            GlStateManager.disableRescaleNormal();
            GlStateManager.enableAlpha();
            GlStateManager.enableBlend();
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            Minecraft.getMinecraft().getTextureManager().bindTexture(net.minecraft.client.gui.Gui.ICONS);
        }
    }

    /** Animation time advanced per client tick (the 1.7 original advanced it per frame, i.e. FPS-dependent). */
    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END && this.animation != null && !Minecraft.getMinecraft().isGamePaused()) {
            this.animation.update();
            this.animation.update();
            this.animation.update();
        }
    }

    private void addAnimation() {
        switch (this.random.nextInt(3)) {
            case 0: this.animation = new AnimationRotate(this.random); break;
            case 1: this.animation = new AnimationRunning(this.random); break;
            default: this.animation = new AnimationPeek(this.random); break;
        }
    }
}
