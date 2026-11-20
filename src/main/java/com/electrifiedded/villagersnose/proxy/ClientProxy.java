package com.electrifiedded.villagersnose.proxy;

import com.electrifiedded.archaiclib.legacy.registry.LegacyGameRegistry;
import com.electrifiedded.archaiclib.v1710.api.client.ArchaicMinecraftForgeClient;
import com.electrifiedded.archaiclib.v1710.api.client.IItemRenderer;
import com.electrifiedded.villagersnose.VillagersNose;
import com.electrifiedded.villagersnose.block.TileEntityVillagerCrop;
import com.electrifiedded.villagersnose.entity.EntityNoselessVillager;
import com.electrifiedded.villagersnose.handler.MusicHandler;
import com.electrifiedded.villagersnose.handler.SuperNoseHandler;
import com.electrifiedded.villagersnose.renderer.RenderNoseItems;
import com.electrifiedded.villagersnose.renderer.RenderNoselessVillager;
import com.electrifiedded.villagersnose.renderer.RenderVillagerCrop;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy {
    @Override
    public void preInit() {
        RenderingRegistry.registerEntityRenderingHandler(EntityNoselessVillager.class, RenderNoselessVillager::new);
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityVillagerCrop.class, new RenderVillagerCrop());
        registerItemRenderer(VillagersNose.itemNose, new RenderNoseItems.Nose());
        registerItemRenderer(VillagersNose.itemSuperNose, new RenderNoseItems.SuperNose());
    }

    @Override
    public void init() {
        MinecraftForge.EVENT_BUS.register(new SuperNoseHandler());
        MinecraftForge.EVENT_BUS.register(new MusicHandler());
    }

    private static void registerItemRenderer(Item item, IItemRenderer renderer) {
        ArchaicMinecraftForgeClient.registerItemRenderer(item, renderer);
        LegacyGameRegistry.registerItemModel(item, 0, item.getRegistryName().toString(), "inventory");
    }
}
