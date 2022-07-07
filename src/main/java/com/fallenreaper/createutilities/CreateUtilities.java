package com.fallenreaper.createutilities;

import com.fallenreaper.createutilities.index.*;
import com.simibubi.create.CreateClient;
import com.simibubi.create.content.curiosities.weapons.BuiltinPotatoProjectileTypes;
import com.simibubi.create.content.schematics.SchematicProcessor;
import com.simibubi.create.content.schematics.filtering.SchematicInstances;
import com.simibubi.create.foundation.advancement.AllTriggers;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.networking.AllPackets;
import com.simibubi.create.foundation.worldgen.AllWorldFeatures;
import com.simibubi.create.repack.registrate.util.nullness.NonNullSupplier;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.stream.Collectors;

@Mod("createutilities")
public class CreateUtilities {


    // Directly reference a log4j logger.
    public static final Logger LOGGER = LogManager.getLogger();

    public static final String ID = "createutilities";

    private static final NonNullSupplier<CreateRegistrate> registrate = CreateRegistrate.lazy(ID);

    public CreateUtilities() {
        MinecraftForge.EVENT_BUS.register(this);
        onStart();

    }
    public static void onStart() {
        ModLoadingContext modLoadingContext = ModLoadingContext.get();

        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        IEventBus forgeEventBus = MinecraftForge.EVENT_BUS;

        modLoadingContext.registerConfig(ModConfig.Type.SERVER, CUConfig.SERVER_CONFIG, CUConfig.SERVER_FILENAME);
        // Register the setup method for modloading
        modEventBus.addListener(CreateUtilities::setup);
        modEventBus.addListener(CreateUtilities::doClientStuff);
        // Register the enqueueIMC method for modloading
        modEventBus.addListener(CreateUtilities::enqueueIMC);
        // Register the processIMC method for modloading
        modEventBus.addListener(CreateUtilities::processIMC);

        DistExecutor.unsafeRunWhenOn(Dist.CLIENT,
                () -> () -> CreateUtilitiesClient.onClientStartUp(modEventBus, forgeEventBus));
        registerModContents();

    }
    public static void registerModContents() {
        CUBlocks.register();
        CUItems.register();
        CUBlockPartials.init();
        CUBlockEntities.register();
        CUContainerTypes.register();
    }
    public static void init(final FMLCommonSetupEvent event) {
    }

    private static void doClientStuff(final FMLClientSetupEvent event) {
       event.enqueueWork(CUPonder::register);
    }

    private static void setup(final FMLCommonSetupEvent event) {
        // some preinit code
        LOGGER.info("HELLO FROM PREINIT");
        LOGGER.info("DIRT BLOCK >> {}", Blocks.DIRT.getRegistryName());
    }

    private static void enqueueIMC(final InterModEnqueueEvent event) {
        // some example code to dispatch IMC to another mod
        InterModComms.sendTo("createutilities", "helloworld", () -> {
            LOGGER.info("Hello world from the MDK");
            return "Hello world";
        });
    }

    private static void processIMC(final InterModProcessEvent event) {
        // some example code to receive and process InterModComms from other mods
        LOGGER.info("Got IMC {}", event.getIMCStream().
                map(m -> m.messageSupplier().get()).
                collect(Collectors.toList()));
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // do something when the server starts
        LOGGER.info("HELLO from server starting");
    }

    // You can use EventBusSubscriber to automatically subscribe events on the contained class (this is subscribing to the MOD
    // Event bus for receiving Registry Events)
    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        @SubscribeEvent
        public static void onBlocksRegistry(final RegistryEvent.Register<Block> blockRegistryEvent) {
            // register a new block here
            LOGGER.info("HELLO from Register Block");
        }
    }

    public static ResourceLocation defaultResourceLocation(String path) {
        return new ResourceLocation(ID, path);
    }

    @SuppressWarnings("deprecation")
    public static CreateRegistrate registrate() {

        LOGGER.info("Registrate created");
        return registrate.get();
    }
}
