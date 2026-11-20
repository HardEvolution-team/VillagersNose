package com.electrifiedded.villagersnose.handler;

import com.electrifiedded.villagersnose.VillagersNose;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class MusicHandler {
    private ISound music;

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent e) {
        if (e.phase != TickEvent.Phase.END) return;
        Minecraft mc = Minecraft.getMinecraft();
        SoundHandler snd = mc.getSoundHandler();
        if (mc.player == null) {
            if (this.music != null) { snd.stopSound(this.music); this.music = null; }
            return;
        }
        ItemStack helmet = mc.player.getItemStackFromSlot(EntityEquipmentSlot.HEAD);
        if (helmet.getItem() != VillagersNose.itemSuperNose) {
            if (this.music != null) { snd.stopSound(this.music); this.music = null; }
        } else if (this.music == null || !snd.isSoundPlaying(this.music)) {
            this.music = PositionedSoundRecord.getMasterRecord(VillagersNose.soundNoseMusic, 1.0F);
            snd.playSound(this.music);
        }
    }
}
