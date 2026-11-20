package com.electrifiedded.villagersnose.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.VillagerRegistry;

import java.util.Random;

public class NoseUtils {
    public static final SoundEvent[] villagerSounds = {
            SoundEvents.ENTITY_VILLAGER_TRADING, SoundEvents.ENTITY_VILLAGER_AMBIENT,
            SoundEvents.ENTITY_VILLAGER_NO, SoundEvents.ENTITY_VILLAGER_YES};

    /** Copies rotation before spawning, so the spawn packet already carries the facing (replaces MessageRotateEntity). */
    public static void syncEntitiesRotation(EntityLivingBase target, EntityLivingBase source) {
        target.rotationYaw = target.prevRotationYaw = source.rotationYaw;
        target.rotationPitch = target.prevRotationPitch = source.rotationPitch;
        target.rotationYawHead = target.prevRotationYawHead = source.rotationYawHead;
        target.renderYawOffset = target.prevRenderYawOffset = source.renderYawOffset;
    }

    public static void setRotation(EntityLivingBase e, float yaw) {
        e.rotationYaw = e.prevRotationYaw = yaw;
        e.rotationYawHead = e.prevRotationYawHead = yaw;
        e.renderYawOffset = e.prevRenderYawOffset = yaw;
    }

    public static EntityVillager getRandomVillager(World world, Random random) {
        EntityVillager villager = new EntityVillager(world);
        VillagerRegistry.setRandomProfession(villager, random);
        return villager;
    }

    public static SoundEvent randomVillagerSound(Random random) {
        return villagerSounds[random.nextInt(villagerSounds.length)];
    }

    /** Server side: heard by everyone around the entity. */
    public static void playVillagerSoundAtEntity(Entity entity, float minPitch, float maxPitch, float minVolume, float maxVolume, Random random) {
        float pitch = minPitch + random.nextFloat() * (maxPitch - minPitch);
        float volume = minVolume + random.nextFloat() * (maxVolume - minVolume);
        entity.world.playSound(null, entity.posX, entity.posY, entity.posZ, randomVillagerSound(random), entity.getSoundCategory(), volume, pitch);
    }

    /** Client side: heard only by this player. */
    public static void playVillagerSoundForPlayer(EntityPlayer player, float minPitch, float maxPitch, float minVolume, float maxVolume, Random random) {
        float pitch = minPitch + random.nextFloat() * (maxPitch - minPitch);
        float volume = minVolume + random.nextFloat() * (maxVolume - minVolume);
        player.playSound(randomVillagerSound(random), volume, pitch);
    }

    public static void spawnEatingParticle(World world, Item item, int damage, double x, double y, double z, Random random) {
        world.spawnParticle(EnumParticleTypes.ITEM_CRACK, x, y, z,
                random.nextGaussian() * 0.15, random.nextDouble() * 0.2, random.nextGaussian() * 0.15,
                Item.getIdFromItem(item), damage);
    }
}
