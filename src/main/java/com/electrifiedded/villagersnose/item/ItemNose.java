package com.electrifiedded.villagersnose.item;

import com.electrifiedded.villagersnose.VillagersNose;
import com.electrifiedded.villagersnose.block.BlockVillagerCrop;
import com.electrifiedded.villagersnose.config.Config;
import com.electrifiedded.villagersnose.renderer.model.ModelNoseArmor;
import com.electrifiedded.villagersnose.util.NoseUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemNose extends ItemArmor implements IPlantable {

    @SideOnly(Side.CLIENT)
    private static ModelBiped armorModel;

    public ItemNose() {
        super(VillagersNose.materialNose, 0, EntityEquipmentSlot.HEAD);
        this.setMaxStackSize(64);
        this.setCreativeTab(CreativeTabs.MISC);
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing,
                                      float hitX, float hitY, float hitZ) {
        ItemStack stack = player.getHeldItem(hand);
        BlockPos up = pos.up();
        IBlockState soil = world.getBlockState(pos);
        if (facing == EnumFacing.UP
                && player.canPlayerEdit(pos, facing, stack)
                && player.canPlayerEdit(up, facing, stack)
                && soil.getBlock().canSustainPlant(soil, world, pos, EnumFacing.UP, this)
                && world.isAirBlock(up)) {
            int l = MathHelper.floor(player.rotationYaw * 4.0F / 360.0F + 0.5) & 3;
            if (l == 0) l = 2;
            else if (l == 2) l = 0;
            world.setBlockState(up, VillagersNose.blockVillagerCrop.getDefaultState().withProperty(BlockVillagerCrop.ROT, l), 3);
            if (!player.capabilities.isCreativeMode) stack.shrink(1);
            return EnumActionResult.SUCCESS;
        }
        return EnumActionResult.FAIL;
    }

    @Override
    public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack) {
        if (!world.isRemote && player.ticksExisted % 20 == 0
                && searchEmeraldsNearby(world, (int) player.posX, (int) player.posY + 1, (int) player.posZ)) {
            NoseUtils.playVillagerSoundAtEntity(player, 1.0F, 1.0F, 1.0F, 1.0F, player.getRNG());
        }
    }

    public static boolean searchEmeraldsNearby(World world, int posX, int posY, int posZ) {
        int range = Config.emeraldSearchRange;
        posX--;
        BlockPos.MutableBlockPos p = new BlockPos.MutableBlockPos();
        for (int x = posX - range; x <= posX + range; x++) {
            for (int y = posY - range; y <= posY + range; y++) {
                for (int z = posZ - range; z <= posZ + range; z++) {
                    p.setPos(x, y, z);
                    if (!world.isBlockLoaded(p)) continue;
                    net.minecraft.block.Block b = world.getBlockState(p).getBlock();
                    if (b == Blocks.EMERALD_ORE || b == Blocks.EMERALD_BLOCK) return true;
                }
            }
        }
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public ModelBiped getArmorModel(EntityLivingBase entity, ItemStack stack, EntityEquipmentSlot slot, ModelBiped _default) {
        if (armorModel == null) armorModel = new ModelNoseArmor(true);
        armorModel.setModelAttributes(_default);
        return armorModel;
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type) {
        return "villagersnose:textures/models/nose_armor.png";
    }

    @Override
    public EnumPlantType getPlantType(IBlockAccess world, BlockPos pos) {
        return EnumPlantType.Crop;
    }

    @Override
    public IBlockState getPlant(IBlockAccess world, BlockPos pos) {
        return VillagersNose.blockVillagerCrop.getDefaultState();
    }
}
