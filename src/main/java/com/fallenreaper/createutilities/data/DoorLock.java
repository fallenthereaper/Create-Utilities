package com.fallenreaper.createutilities.data;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.UUID;

public class DoorLock {
   public BlockPos blockPos;
   public UUID id;
   public UUID ownerId;

    public DoorLock(BlockPos pos, UUID id, UUID ownerId) {
        this.blockPos = pos;
        this.id = id;
        this.ownerId = ownerId;
    }

    public CompoundTag write() {
        CompoundTag tag = new CompoundTag();
        tag.putUUID("Id", id);
        tag.putUUID("OwnerId", ownerId);
        tag.put("DoorPos", NbtUtils.writeBlockPos(blockPos));
        return tag;
    }

    public static DoorLock read(CompoundTag tag) {
        UUID owner = tag.getUUID("OwnerId");
        UUID ID = tag.getUUID("Id");
        BlockPos pos = NbtUtils.readBlockPos(tag.getCompound("DoorPos"));
        DoorLock doorLock = new DoorLock(pos, ID, owner);

        return doorLock;
    }
    public BlockPos getBlockPos() {

        return blockPos;
    }

    @Nullable
    public LivingEntity getOwner(Level level) {
        try {
            UUID uuid = ownerId;
            return uuid == null ? null
                    : level.getServer()
                    .getPlayerList()
                    .getPlayer(uuid);
        } catch (IllegalArgumentException illegalargumentexception) {
            return null;
        }
    }
}
