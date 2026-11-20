package com.electrifiedded.villagersnose;

import com.electrifiedded.archaiclib.legacy.registry.LegacyGameRegistry;
import com.electrifiedded.archaiclib.legacy.registry.LegacyRecipes;
import com.electrifiedded.archaiclib.original.OriginalModAssets;
import com.electrifiedded.villagersnose.block.BlockVillagerCrop;
import com.electrifiedded.villagersnose.block.TileEntityVillagerCrop;
import com.electrifiedded.villagersnose.entity.EntityNoselessVillager;
import com.electrifiedded.villagersnose.handler.ShearsEventHandler;
import com.electrifiedded.villagersnose.item.ItemNose;
import com.electrifiedded.villagersnose.item.ItemSuperNose;
import com.electrifiedded.villagersnose.proxy.CommonProxy;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.VERSION,
        dependencies = "required-after:archaiclib@[1.8.0,)")
public class VillagersNose {

    public static final String MODID = Reference.MOD_ID;

    @Mod.Instance(MODID)
    public static VillagersNose instance;

    public static Item itemNose;
    public static Item itemSuperNose;
    public static Block blockVillagerCrop;
    public static ArmorMaterial materialNose;
    public static SoundEvent soundNoseMusic;

    @SidedProxy(clientSide = "com.electrifiedded.villagersnose.proxy.ClientProxy",
            serverSide = "com.electrifiedded.villagersnose.proxy.CommonProxy")
    public static CommonProxy proxy;

    public static final String ORIGINAL_NAME = "Villager's Nose 1.3c for 1.7.10 (by StarSheep)";
    public static final String ORIGINAL_URL = "https://www.curseforge.com/minecraft/mc-mods/villagers-nose";

    public VillagersNose() {
        // Our own sounds.json and lang files are used; the original's 1.7-format ones are ignored.
        OriginalModAssets.hide(MODID, "sounds.json");
        OriginalModAssets.hide(MODID, "lang/en_us.lang");
        OriginalModAssets.hide(MODID, "lang/ru_ru.lang");
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        OriginalModAssets.require(MODID, "VillagersNose", ORIGINAL_NAME, ORIGINAL_URL, MODID);
        materialNose = EnumHelper.addArmorMaterial("VILLAGERSNOSE_NOSE", MODID + ":nose", 0, new int[]{0, 0, 0, 0}, 15,
                SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, 0.0F);

        itemNose = LegacyGameRegistry.registerItem(new ItemNose().setTranslationKey(MODID + ".nose"), "nose");
        itemSuperNose = LegacyGameRegistry.registerItem(new ItemSuperNose().setTranslationKey(MODID + ".super_nose"), "super_nose");
        blockVillagerCrop = LegacyGameRegistry.registerBlock(new BlockVillagerCrop().setTranslationKey(MODID + ".villager_crop"),
                null, "villager_crop");
        GameRegistry.registerTileEntity(TileEntityVillagerCrop.class, new ResourceLocation(MODID, "villager_crop"));
        LegacyGameRegistry.registerModEntity(EntityNoselessVillager.class, "noseless_villager", 0, this, 32, 5, true);

        ResourceLocation music = new ResourceLocation(MODID, "nose_music");
        soundNoseMusic = new SoundEvent(music).setRegistryName(music);
        ForgeRegistries.SOUND_EVENTS.register(soundNoseMusic);

        proxy.preInit();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(new ShearsEventHandler());
        LegacyRecipes.addRecipe(new ItemStack(itemSuperNose),
                "GSG", " N ", " W ", 'G', "paneGlass", 'S', "stickWood", 'N', new ItemStack(itemNose),
                'W', new ItemStack(Blocks.WOOL, 1, 12));
        proxy.init();
    }
}
