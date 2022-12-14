package com.fallenreaper.createutilities.core.data.punchcard;

import com.fallenreaper.createutilities.CreateUtilities;

public class PunchcardTrainTicket extends PunchcardInfo {


    public PunchcardTrainTicket() {
        super();
    }

    @Override
    public String getId() {
        return CreateUtilities.defaultResourceLocation("train_ticket").getPath();
    }
}
