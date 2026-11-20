package com.electrifiedded.villagersnose.item;

import com.electrifiedded.villagersnose.VillagersNose;
import com.electrifiedded.villagersnose.renderer.model.ModelSuperNose;
import com.electrifiedded.villagersnose.util.NoseUtils;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemSuperNose extends ItemArmor {

    @SideOnly(Side.CLIENT)
    private static ModelBiped armorModel;

    public ItemSuperNose() {
        super(VillagersNose.materialNose, 0, EntityEquipmentSlot.HEAD);
        this.setCreativeTab(CreativeTabs.MISC);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public ModelBiped getArmorModel(EntityLivingBase entity, ItemStack stack, EntityEquipmentSlot slot, ModelBiped _default) {
        if (armorModel == null) armorModel = new ModelSuperNose();
        armorModel.setModelAttributes(_default);
        return armorModel;
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type) {
        return "villagersnose:textures/models/super_nose.png";
    }

    @Override
    public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack) {
        if (player.onGround) {
            player.jump();
        }
        if (world.isRemote && player.ticksExisted % 10 == 0) {
            NoseUtils.playVillagerSoundForPlayer(player, 0.5F, 2.0F, 0.8F, 1.0F, player.getRNG());
        }
    }
}
