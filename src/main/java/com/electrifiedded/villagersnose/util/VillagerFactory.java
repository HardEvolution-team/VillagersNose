package com.electrifiedded.villagersnose.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.registry.VillagerRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Creates the villagers this mod spawns. Soft compat with Helpful Villagers (no compile dependency):
 * when it is installed, noses grow into its villagers, and sheared villagers of any villager subclass
 * come back as the same entity type. Every foreign call is guarded and falls back to a vanilla villager.
 */
public final class VillagerFactory {
    private static final Logger LOG = LogManager.getLogger("VillagersNose");
    public static final String HV_MODID = "helpfulvillagers";
    private static final ResourceLocation HV_VILLAGER = new ResourceLocation(HV_MODID, "villager");
    private static Boolean hvLoaded;

    private VillagerFactory() {}

    public static boolean helpfulVillagersLoaded() {
        if (hvLoaded == null) hvLoaded = Loader.isModLoaded(HV_MODID);
        return hvLoaded;
    }

    /** Villager grown from a planted nose (server side). */
    public static EntityVillager createGrown(World world, BlockPos pos, VillagerRegistry.VillagerProfession profession) {
        if (helpfulVillagersLoaded()) {
            try {
                Entity e = EntityList.createEntityByIDFromName(HV_VILLAGER, world);
                if (e instanceof EntityVillager) {
                    EntityVillager v = (EntityVillager) e;
                    v.setLocationAndAngles(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, 0.0F, 0.0F);
                    // same as Helpful Villagers' own children: lets it pick a job etc.
                    v.onInitialSpawn(world.getDifficultyForLocation(pos), null);
                    if (profession != null) v.setProfession(profession);
                    return v;
                }
            } catch (Throwable t) {
                LOG.warn("Could not create a Helpful Villagers villager, using a vanilla one", t);
            }
        }
        EntityVillager v = new EntityVillager(world);
        if (profession != null) v.setProfession(profession);
        return v;
    }

    /** Full NBT including the entity id, so the exact type (vanilla or modded) can be recreated. */
    public static NBTTagCompound save(EntityVillager villager) {
        NBTTagCompound tag = new NBTTagCompound();
        villager.writeToNBT(tag);
        ResourceLocation id = EntityList.getKey(villager);
        if (id != null) tag.setString("id", id.toString());
        return tag;
    }

    /** Recreates the villager saved by {@link #save}; old saves without an id become vanilla villagers. */
    public static EntityLivingBase restore(World world, NBTTagCompound tag) {
        if (tag != null && tag.hasKey("id")) {
            try {
                Entity e = EntityList.createEntityFromNBT(tag, world);
                if (e instanceof EntityLivingBase) return (EntityLivingBase) e;
            } catch (Throwable t) {
                LOG.warn("Could not restore villager of type {}, using a vanilla one", tag.getString("id"), t);
            }
        }
        EntityVillager v = new EntityVillager(world);
        if (tag != null && !tag.isEmpty()) {
            try {
                NBTTagCompound copy = tag.copy();
                copy.removeTag("id");
                v.readFromNBT(copy);
            } catch (Throwable t) {
                LOG.warn("Broken villager data, spawning a fresh villager", t);
                v = new EntityVillager(world);
            }
        }
        return v;
    }
}
