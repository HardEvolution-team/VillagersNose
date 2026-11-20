package com.electrifiedded.villagersnose.renderer;

import com.electrifiedded.villagersnose.block.BlockVillagerCrop;
import com.electrifiedded.villagersnose.block.TileEntityVillagerCrop;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderVillagerCrop extends TileEntitySpecialRenderer<TileEntityVillagerCrop> {
    @Override
    public void render(TileEntityVillagerCrop te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        if (te.getWorld() == null) return;
        Entity villager = te.getRenderVillager();
        float scale = te.getRenderSize(partialTicks);
        if (scale <= 0.0F) return;
        IBlockState state = te.getWorld().getBlockState(te.getPos());
        float rotation = state.getBlock() instanceof BlockVillagerCrop ? state.getValue(BlockVillagerCrop.ROT) * 90.0F : 0.0F;
        GlStateManager.pushMatrix();
        GlStateManager.translate(x + 0.5, y, z + 0.5);
        GlStateManager.scale(scale, scale, scale);
        GlStateManager.rotate(rotation, 0.0F, 1.0F, 0.0F);
        Minecraft.getMinecraft().getRenderManager().renderEntity(villager, 0.0, 0.0, 0.0, 0.0F, 0.0F, false);
        GlStateManager.popMatrix();
    }
}
