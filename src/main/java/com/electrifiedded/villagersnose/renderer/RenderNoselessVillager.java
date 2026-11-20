package com.electrifiedded.villagersnose.renderer;

import com.electrifiedded.villagersnose.entity.EntityNoselessVillager;
import com.electrifiedded.villagersnose.renderer.model.ModelNoselessVillager;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.VillagerRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderNoselessVillager extends RenderLiving<EntityNoselessVillager> {
    private static final ResourceLocation DEFAULT = new ResourceLocation("textures/entity/villager/villager.png");

    public RenderNoselessVillager(RenderManager manager) {
        super(manager, new ModelNoselessVillager(0.0F), 0.5F);
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityNoselessVillager villager) {
        VillagerRegistry.VillagerProfession prof = ForgeRegistries.VILLAGER_PROFESSIONS.getValue(new ResourceLocation(villager.getProfessionName()));
        return prof != null ? prof.getSkin() : DEFAULT;
    }

    @Override
    protected void preRenderCallback(EntityNoselessVillager entity, float partialTickTime) {
        GlStateManager.scale(0.9375F, 0.9375F, 0.9375F);
    }
}
