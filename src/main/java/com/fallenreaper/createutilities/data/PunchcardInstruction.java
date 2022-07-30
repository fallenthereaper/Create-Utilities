package com.fallenreaper.createutilities.data;

import com.fallenreaper.createutilities.CreateUtilities;
import net.minecraft.nbt.CompoundTag;

public class PunchcardInstruction extends PunchcardInfo{


    public PunchcardInstruction() {
        super();
    }

    @Override
    public CompoundTag getDataInfo() {
        return super.getDataInfo();
    }

    @Override
    public String getId() {
        return CreateUtilities.defaultResourceLocation("instruction").getPath();
    }
}
