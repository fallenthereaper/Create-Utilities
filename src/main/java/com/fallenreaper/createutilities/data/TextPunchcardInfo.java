package com.fallenreaper.createutilities.data;

import com.fallenreaper.createutilities.CreateUtilities;

public class TextPunchcardInfo extends PunchcardInfo{

    public TextPunchcardInfo() {
        super();
    }

    @Override
    public String getId() {
        return CreateUtilities.defaultResourceLocation("textcard_info").getPath();
    }
}
