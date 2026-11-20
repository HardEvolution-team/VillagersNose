package com.electrifiedded.villagersnose.config;

import com.electrifiedded.villagersnose.Reference;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/** Same options as the 1.7.10 config; the in-game config GUI comes from Forge's @Config system. */
@net.minecraftforge.common.config.Config(modid = Reference.MOD_ID, name = "VillagersNose")
public class Config {

    @net.minecraftforge.common.config.Config.Comment("Time it takes to grow a villager.")
    @net.minecraftforge.common.config.Config.RangeInt(min = 1)
    public static int villagerGrowth = 5000;

    @net.minecraftforge.common.config.Config.Comment("Does villager's nose grow back?")
    public static boolean doesNoseGrowBack = true;

    @net.minecraftforge.common.config.Config.Comment("Time it takes to grow a villager nose back.")
    @net.minecraftforge.common.config.Config.RangeInt(min = 1)
    public static int noseGrowth = 10000;

    @net.minecraftforge.common.config.Config.Comment("Emerald search range.")
    @net.minecraftforge.common.config.Config.RangeInt(min = 0, max = 50)
    public static int emeraldSearchRange = 3;

    @net.minecraftforge.common.config.Config.Comment("Is villager crop bonemealable?")
    public static boolean isVillagerBonemealable = true;

    @net.minecraftforge.common.config.Config.Comment("Can give golden apples to a noseless villager to make his nose grow instantly?")
    public static boolean canFeedGoldenApples = true;

    @Mod.EventBusSubscriber(modid = Reference.MOD_ID)
    public static class Events {
        @SubscribeEvent
        public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
            if (Reference.MOD_ID.equals(event.getModID())) {
                ConfigManager.sync(Reference.MOD_ID, net.minecraftforge.common.config.Config.Type.INSTANCE);
            }
        }
    }
}
