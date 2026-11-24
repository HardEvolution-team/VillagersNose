package com.electrifiedded.villagersnose.block;

import com.electrifiedded.villagersnose.config.Config;
import com.electrifiedded.villagersnose.util.NoseUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.VillagerRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

public class TileEntityVillagerCrop extends TileEntity implements ITickable {
    private int growth = 0;
    /** Forge profession registry name of the villager that is growing; empty until chosen. */
    private String profession = "";
    private int lastSyncedPercent = -1;
    private final Random random = new com.electrifiedded.archaiclib.util.FastRandom();

    @SideOnly(Side.CLIENT)
    private EntityVillager renderVillager;

    @Override
    public void update() {
        if (this.world.isRemote) {
            this.clientTick();
            return;
        }
        if (this.profession.isEmpty()) {
            this.profession = NoseUtils.getRandomVillager(this.world, this.random).getProfessionForge().getRegistryName().toString();
            this.sync();
        }
        if (this.growth < Config.villagerGrowth) {
            this.increaseGrowth(this.random.nextInt(5));
        } else {
            EntityVillager villager = com.electrifiedded.villagersnose.util.VillagerFactory.createGrown(this.world, this.pos, this.getProfessionEntry());
            int rotate = this.world.getBlockState(this.pos).getBlock() instanceof BlockVillagerCrop
                    ? this.world.getBlockState(this.pos).getValue(BlockVillagerCrop.ROT) : 0;
            if (rotate == 1) rotate = 3;
            else if (rotate == 3) rotate = 1;
            villager.setLocationAndAngles(this.pos.getX() + 0.5, this.pos.getY(), this.pos.getZ() + 0.5, 90.0F * rotate, 0.0F);
            NoseUtils.setRotation(villager, 90.0F * rotate);
            this.world.setBlockToAir(this.pos);
            this.world.spawnEntity(villager);
        }
    }

    public VillagerRegistry.VillagerProfession getProfessionEntry() {
        if (this.profession.isEmpty()) return null;
        return ForgeRegistries.VILLAGER_PROFESSIONS.getValue(new ResourceLocation(this.profession));
    }

    /** Client-side smoothed growth: the server only syncs whole percents, so predict the average rate (2/tick). */
    @SideOnly(Side.CLIENT)
    private float clientGrowth = -1, prevClientGrowth = -1;

    @SideOnly(Side.CLIENT)
    private void clientTick() {
        if (this.clientGrowth < 0) this.clientGrowth = this.growth;
        this.prevClientGrowth = this.clientGrowth;
        float target = this.growth;
        float lead = Config.villagerGrowth / 100.0F + 2.0F; // may run ahead of the last sync by up to one sync step
        if (this.clientGrowth < target - 2.0F) this.clientGrowth += (target - this.clientGrowth) * 0.25F; // bonemeal jump
        else this.clientGrowth = Math.min(this.clientGrowth + 2.0F, target + lead);
        this.clientGrowth = Math.min(this.clientGrowth, Config.villagerGrowth);
    }

    @SideOnly(Side.CLIENT)
    public float getRenderSize(float partialTicks) {
        if (this.clientGrowth < 0) return this.getSize();
        float g = this.prevClientGrowth + (this.clientGrowth - this.prevClientGrowth) * partialTicks;
        return g / Config.villagerGrowth;
    }

    public float getSize() {
        return (float) this.growth / Config.villagerGrowth;
    }

    public boolean canBonemeal() {
        return Config.isVillagerBonemealable && this.growth < Config.villagerGrowth;
    }

    public void bonemeal(Random random) {
        if (random.nextInt(2) == 0) {
            int i = Math.max(1, Config.villagerGrowth / 100);
            this.increaseGrowth(i + random.nextInt(i * 3));
        }
    }

    public void increaseGrowth(int amount) {
        if (amount == 0) return;
        this.growth = Math.min(this.growth + amount, Config.villagerGrowth);
        this.markDirty();
        int percent = (int) (this.getSize() * 100);
        if (percent != this.lastSyncedPercent) this.sync();
    }

    private void sync() {
        this.lastSyncedPercent = (int) (this.getSize() * 100);
        IBlockState state = this.world.getBlockState(this.pos);
        this.world.notifyBlockUpdate(this.pos, state, state, 3);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        this.growth = nbt.getInteger("Growth");
        this.profession = nbt.getString("ProfessionName");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setInteger("Growth", this.growth);
        nbt.setString("ProfessionName", this.profession);
        return nbt;
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        return this.writeToNBT(new NBTTagCompound());
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(this.pos, 0, this.getUpdateTag());
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        String old = this.profession;
        this.readFromNBT(pkt.getNbtCompound());
        if (!old.equals(this.profession)) this.renderVillager = null;
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
        return oldState.getBlock() != newState.getBlock();
    }

    /** Fake villager rendered by the TESR (client only, never spawned). */
    @SideOnly(Side.CLIENT)
    public EntityVillager getRenderVillager() {
        if (this.renderVillager == null || this.renderVillager.world != this.world) {
            this.renderVillager = new EntityVillager(this.world);
            VillagerRegistry.VillagerProfession prof = this.getProfessionEntry();
            if (prof != null) this.renderVillager.setProfession(prof);
        }
        return this.renderVillager;
    }
}
