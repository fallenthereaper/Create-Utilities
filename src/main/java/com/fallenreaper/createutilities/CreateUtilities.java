package com.fallenreaper.createutilities;

import com.fallenreaper.createutilities.core.data.Interactable;
import com.fallenreaper.createutilities.core.data.doorlock.DoorLockManager;
import com.fallenreaper.createutilities.core.events.CommonEvents;
import com.fallenreaper.createutilities.index.*;
import com.fallenreaper.createutilities.networking.ModPackets;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.LangBuilder;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
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
import org.apache.logging.log4j.util.Supplier;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Mod(CreateUtilities.ID)
public class CreateUtilities {


    // Directly reference a log4j logger.
    public static final Logger LOGGER = LogManager.getLogger();

    public static final String ID = "createutilities";
    public static final String MOD_VERSION = "0.1";
    public static final LangBuilder ModLangBuilder = Lang.builder(ID);
    public static final CreativeModeTab TAB = new CreativeModeTab(ID) {
        @Override
        public @NotNull ItemStack makeIcon() {

            return CUBlocks.TYPEWRITER.asStack().copy();
        }


        @Override
        public @NotNull Component getDisplayName() {
            return Component.literal("Create Utilities");
        }
    };
    public static final Interactable.IDraggable test = (mouseX, mouseY, coords, rightClick) -> System.out.println(mouseX);


    private static final NonNullSupplier<CreateRegistrate> registrate = () -> CreateRegistrate.create(ID);
    public static List<Block> BLOCKLIST = new ArrayList<>();
    public static DoorLockManager DOORLOCK_MANAGER = new DoorLockManager();


    public CreateUtilities() {
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new CommonEvents());
        onStart();

    }

    public static void onStart() {
        ModPackets.registerPackets();
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
        CUBlockPartials.register();
        CUBlockEntities.register();
        CUContainerTypes.register();
        CUFluids.register();
        //
        addToBlockList(() -> Blocks.FURNACE);
        addToBlockList(() -> Blocks.CRAFTING_TABLE);
       // addToBlockList(CUBlocks.STEAM_FURNACE::get);
    }

    public static void addToBlockList(Supplier<Block> sup) {
        if (!(sup.get() == null))
            BLOCKLIST.add(sup.get());
    }

    private static void doClientStuff(final FMLClientSetupEvent event) {
        event.enqueueWork(CUPonder::register);
    }

    private static void setup(final FMLCommonSetupEvent event) {
        // some preinit code
        LOGGER.info("HELLO FROM PREINIT");
        LOGGER.info("DIRT BLOCK >> {}", Blocks.DIRT.getName());
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

    public static ResourceLocation defaultResourceLocation(String path) {
        return new ResourceLocation(ID, path);
    }

    @SuppressWarnings("deprecation")
    public static CreateRegistrate registrate() {

        LOGGER.info("Registrate created");
        return registrate.get();
    }


    // You can use EventBusSubscriber to automatically subscribe events on the contained class (this is subscribing to the MOD
    // Event bus for receiving Registry Events)
}
