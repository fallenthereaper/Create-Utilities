package com.fallenreaper.createutilities.networking;

import com.fallenreaper.createutilities.CreateUtilities;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class PacketHandler {
    protected static final String PROTOCOL = "1";
    protected static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(new ResourceLocation(CreateUtilities.ID, "main"), () -> PROTOCOL,
            PROTOCOL::equals,
            PROTOCOL::equals
    );
}
