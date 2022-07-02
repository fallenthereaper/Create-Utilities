package com.fallenreaper.createutilities.events;

import com.fallenreaper.createutilities.content.blocks.typewriter.TypewriterContainer;
import com.fallenreaper.createutilities.content.blocks.typewriter.TypewriterScreen;
import com.fallenreaper.createutilities.index.CUContainerTypes;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber
public class CommonEvents {
    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {

        MenuScreens.register(CUContainerTypes.TYPEWRITER_MENUTYPE.get(), TypewriterScreen::new);

    }
}
