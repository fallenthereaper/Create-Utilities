package com.fallenreaper.createutilities.content.blocks.sliding_door;

import com.fallenreaper.createutilities.CreateUtilities;
import com.fallenreaper.createutilities.data.DoorLock;
import com.simibubi.create.content.curiosities.deco.SlidingDoorTileEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.UUID;

public class LockSlidingDoorBlockEntity extends SlidingDoorTileEntity {
    private DoorLock doorLock;
    boolean hasBound = false;
//todo change addTotool tip so it has the punchcard item instead of googles and it will show stuff related to it
    public LockSlidingDoorBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public void createLock(DoorLock doorLock) {
        CreateUtilities.DOORLOCK_MANAGER.add(doorLock);
       this.doorLock = doorLock;
       this.hasBound = true;
       sendData();
    }

    @Override
    protected void write(CompoundTag tag, boolean clientPacket) {
        if(doorLock != null) {
            tag.putUUID("LockKey", this.doorLock.id);
        }
    }

    @Override
    protected void read(CompoundTag tag, boolean clientPacket) {
        if(doorLock != null) {
            this.doorLock.id = tag.getUUID("LockKey");
        }
    }

    public UUID getUUID() {
        return doorLock.id;
    }
}
