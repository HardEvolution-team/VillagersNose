package com.electrifiedded.villagersnose.entity;

import com.electrifiedded.villagersnose.config.Config;
import com.electrifiedded.villagersnose.util.NoseUtils;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.INpc;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

public class EntityNoselessVillager extends EntityCreature implements INpc {
    private static final DataParameter<Integer> NOSE = EntityDataManager.createKey(EntityNoselessVillager.class, DataSerializers.VARINT);
    /** Forge profession registry name, used by the renderer for the skin. */
    private static final DataParameter<String> PROFESSION = EntityDataManager.createKey(EntityNoselessVillager.class, DataSerializers.STRING);

    /** Full NBT of the villager this one was before shearing; restored when the nose grows back. */
    private NBTTagCompound original = new NBTTagCompound();

    public EntityNoselessVillager(World world) {
        super(world);
        this.setSize(0.6F, 1.95F);
        ((PathNavigateGround) this.getNavigator()).setBreakDoors(true);
        ((PathNavigateGround) this.getNavigator()).setCanSwim(true);
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new EntityAIAvoidEntity<>(this, EntityZombie.class, 8.0F, 0.6, 0.6));
        this.tasks.addTask(2, new EntityAIMoveIndoors(this));
        this.tasks.addTask(3, new EntityAIRestrictOpenDoor(this));
        this.tasks.addTask(4, new EntityAIOpenDoor(this, true));
        this.tasks.addTask(5, new EntityAIMoveTowardsRestriction(this, 0.6));
        this.tasks.addTask(6, new EntityAIWatchClosest2(this, EntityPlayer.class, 3.0F, 1.0F));
        this.tasks.addTask(6, new EntityAIWatchClosest2(this, EntityVillager.class, 5.0F, 0.02F));
        this.tasks.addTask(6, new EntityAIWatchClosest2(this, EntityNoselessVillager.class, 5.0F, 0.02F));
        this.tasks.addTask(6, new EntityAIWander(this, 0.6));
        this.tasks.addTask(7, new EntityAIWatchClosest(this, EntityLiving.class, 8.0F));
    }

    public EntityNoselessVillager(EntityVillager villager) {
        this(villager.world);
        this.original = com.electrifiedded.villagersnose.util.VillagerFactory.save(villager);
        this.setProfessionName(villager.getProfessionForge().getRegistryName().toString());
        if (villager.hasCustomName()) {
            this.setCustomNameTag(villager.getCustomNameTag());
        }
        this.setLocationAndAngles(villager.posX, villager.posY, villager.posZ, villager.rotationYaw, villager.rotationPitch);
        NoseUtils.syncEntitiesRotation(this, villager);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(NOSE, 0);
        this.dataManager.register(PROFESSION, "minecraft:farmer");
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.5);
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        if (Config.doesNoseGrowBack && !this.world.isRemote && !this.isDead) {
            if (this.getNoseGrowth() < Config.noseGrowth) {
                if (this.rand.nextInt(3) == 0) {
                    this.setNoseGrowth(this.getNoseGrowth() + 1);
                }
            } else {
                NoseUtils.playVillagerSoundAtEntity(this, 1.0F, 1.0F, 1.0F, 1.0F, this.rand);
                this.turnIntoVillager();
            }
        }
    }

    private void turnIntoVillager() {
        if (this.isDead) return;
        net.minecraft.entity.EntityLivingBase villager = com.electrifiedded.villagersnose.util.VillagerFactory.restore(this.world, this.original);
        // keep the name given meanwhile; health is fresh like in the original
        if (this.hasCustomName()) villager.setCustomNameTag(this.getCustomNameTag());
        villager.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
        NoseUtils.syncEntitiesRotation(villager, this);
        villager.motionX = villager.motionY = villager.motionZ = 0;
        villager.fallDistance = 0;
        this.setDead();
        this.world.spawnEntity(villager);
    }

    @Override
    public boolean processInteract(EntityPlayer player, EnumHand hand) {
        ItemStack held = player.getHeldItem(hand);
        if (Config.canFeedGoldenApples && held.getItem() == Items.GOLDEN_APPLE && held.getMetadata() == 1) {
            if (!this.world.isRemote) {
                this.playSound(SoundEvents.ENTITY_GENERIC_EAT, 1.0F, 1.0F);
                if (!player.capabilities.isCreativeMode) held.shrink(1);
                this.turnIntoVillager();
            } else {
                for (int i = 0; i < 10; i++) {
                    NoseUtils.spawnEatingParticle(this.world, Items.GOLDEN_APPLE, 0,
                            this.posX, this.posY + this.getEyeHeight(), this.posZ, this.rand);
                }
            }
            return true;
        }
        if (hand == EnumHand.MAIN_HAND) {
            if (this.world.isRemote) {
                player.sendMessage(new TextComponentTranslation("translation.villagersnose.tradeRefusal"));
            } else {
                NoseUtils.playVillagerSoundAtEntity(this, 1.0F, 1.0F, 1.0F, 1.0F, this.rand);
            }
        }
        return true;
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        super.writeEntityToNBT(nbt);
        nbt.setInteger("NoseGrowth", this.getNoseGrowth());
        nbt.setTag("Original", this.original);
        nbt.setString("ProfessionName", this.getProfessionName());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        super.readEntityFromNBT(nbt);
        this.setNoseGrowth(nbt.getInteger("NoseGrowth"));
        this.original = nbt.getCompoundTag("Original");
        if (nbt.hasKey("ProfessionName")) {
            this.setProfessionName(nbt.getString("ProfessionName"));
        } else if (this.original.hasKey("ProfessionName")) {
            this.setProfessionName(this.original.getString("ProfessionName"));
        }
    }

    @Override
    protected boolean canDespawn() {
        return false;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_VILLAGER_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.ENTITY_VILLAGER_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_VILLAGER_DEATH;
    }

    @Override
    public boolean canBeLeashedTo(EntityPlayer player) {
        return false;
    }

    @Override
    public float getEyeHeight() {
        return 1.62F;
    }

    public int getNoseGrowth() {
        return this.dataManager.get(NOSE);
    }

    public void setNoseGrowth(int nose) {
        this.dataManager.set(NOSE, Math.max(0, nose));
    }

    public String getProfessionName() {
        return this.dataManager.get(PROFESSION);
    }

    public void setProfessionName(String name) {
        this.dataManager.set(PROFESSION, name);
    }
}
