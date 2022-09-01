package com.fallenreaper.createutilities.data;

import com.fallenreaper.createutilities.CreateUtilities;

public class PunchcardDoorInfo extends PunchcardInfo {


    public PunchcardDoorInfo() {
        super();

    }

    @Override
    public String getId() {
        return CreateUtilities.defaultResourceLocation("door_info").getPath();
    }
}
