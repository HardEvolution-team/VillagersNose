package com.electrifiedded.villagersnose.handler;

import com.electrifiedded.villagersnose.VillagersNose;
import com.electrifiedded.villagersnose.entity.EntityNoselessVillager;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemShears;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ShearsEventHandler {
    /** HIGHEST + fired before processInteract: shears win over any villager GUI (vanilla trade, Helpful Villagers dialog). */
    @SubscribeEvent(priority = net.minecraftforge.fml.common.eventhandler.EventPriority.HIGHEST)
    public void onEntityInteract(PlayerInteractEvent.EntityInteract event) {
        if (!(event.getTarget() instanceof EntityVillager)) return;
        EntityVillager villager = (EntityVillager) event.getTarget();
        EntityPlayer player = event.getEntityPlayer();
        ItemStack shears = event.getItemStack();
        if (villager.isChild() || villager.isDead || !(shears.getItem() instanceof ItemShears)) return;

        // cancel on both sides so the trade GUI does not open
        event.setCanceled(true);
        event.setCancellationResult(EnumActionResult.SUCCESS);
        World world = villager.world;
        if (world.isRemote) return;

        villager.playSound(SoundEvents.ENTITY_SHEEP_SHEAR, 1.0F, 1.0F);
        if (!player.capabilities.isCreativeMode) {
            shears.damageItem(1, player);
        }
        world.spawnEntity(new EntityItem(world, villager.posX, villager.posY + villager.getEyeHeight(), villager.posZ,
                new ItemStack(VillagersNose.itemNose)));
        EntityNoselessVillager noseless = new EntityNoselessVillager(villager);
        villager.setDead();
        world.spawnEntity(noseless);
    }
}
