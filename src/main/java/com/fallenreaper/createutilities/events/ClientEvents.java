package com.fallenreaper.createutilities.events;

import com.simibubi.create.CreateClient;
import com.simibubi.create.content.contraptions.KineticDebugger;
import com.simibubi.create.content.contraptions.components.fan.AirCurrent;
import com.simibubi.create.content.contraptions.components.structureMovement.ContraptionHandler;
import com.simibubi.create.content.contraptions.components.structureMovement.chassis.ChassisRangeDisplay;
import com.simibubi.create.content.contraptions.components.structureMovement.interaction.controls.ControlsHandler;
import com.simibubi.create.content.contraptions.components.structureMovement.interaction.controls.TrainHUD;
import com.simibubi.create.content.contraptions.components.structureMovement.render.ContraptionRenderDispatcher;
import com.simibubi.create.content.contraptions.components.structureMovement.train.CouplingHandlerClient;
import com.simibubi.create.content.contraptions.components.structureMovement.train.CouplingPhysics;
import com.simibubi.create.content.contraptions.components.structureMovement.train.CouplingRenderer;
import com.simibubi.create.content.contraptions.components.structureMovement.train.capability.CapabilityMinecartController;
import com.simibubi.create.content.contraptions.relays.belt.item.BeltConnectorHandler;
import com.simibubi.create.content.curiosities.girder.GirderWrenchBehavior;
import com.simibubi.create.content.curiosities.toolbox.ToolboxHandlerClient;
import com.simibubi.create.content.curiosities.tools.BlueprintOverlayRenderer;
import com.simibubi.create.content.curiosities.tools.ExtendoGripRenderHandler;
import com.simibubi.create.content.curiosities.zapper.terrainzapper.WorldshaperRenderHandler;
import com.simibubi.create.content.logistics.block.depot.EjectorTargetHandler;
import com.simibubi.create.content.logistics.block.display.DisplayLinkBlockItem;
import com.simibubi.create.content.logistics.block.mechanicalArm.ArmInteractionPointHandler;
import com.simibubi.create.content.logistics.item.LinkedControllerClientHandler;
import com.simibubi.create.content.logistics.trains.CameraDistanceModifier;
import com.simibubi.create.content.logistics.trains.entity.TrainRelocator;
import com.simibubi.create.content.logistics.trains.management.edgePoint.TrackTargetingClient;
import com.simibubi.create.content.logistics.trains.track.CurvedTrackInteraction;
import com.simibubi.create.content.logistics.trains.track.TrackPlacement;
import com.simibubi.create.foundation.ponder.PonderTooltipHandler;
import com.simibubi.create.foundation.sound.SoundScapes;
import com.simibubi.create.foundation.tileEntity.behaviour.edgeInteraction.EdgeInteractionRenderer;
import com.simibubi.create.foundation.tileEntity.behaviour.filtering.FilteringRenderer;
import com.simibubi.create.foundation.tileEntity.behaviour.linked.LinkRenderer;
import com.simibubi.create.foundation.tileEntity.behaviour.scrollvalue.ScrollValueHandler;
import com.simibubi.create.foundation.tileEntity.behaviour.scrollvalue.ScrollValueRenderer;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import com.simibubi.create.foundation.utility.CameraAngleAnimationService;
import com.simibubi.create.foundation.utility.ServerSpeedProvider;
import com.simibubi.create.foundation.utility.placement.PlacementHelpers;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(Dist.CLIENT)
public class ClientEvents {
@SubscribeEvent
    public static void onRenderOverlay(RenderGameOverlayEvent.Text event) {
    Minecraft mc = Minecraft.getInstance();

    if (!mc.level.isClientSide())
        return;

    if(mc.player != null) {

        String fps = mc.fpsString;



        Player player = mc.player;
       // event.getLeft().add(fps);
    }
}
    @SubscribeEvent
    public static void onTick(TickEvent.ClientTickEvent event) {
        if (!isGameActive())
            return;

        Level world = Minecraft.getInstance().level;
        if (event.phase == TickEvent.Phase.START) {
            LinkedControllerClientHandler.tick();
            ControlsHandler.tick();
            AirCurrent.tickClientPlayerSounds();
            return;
        }

        SoundScapes.tick();
        AnimationTickHolder.tick();
        ScrollValueHandler.tick();

        CreateClient.SCHEMATIC_SENDER.tick();
        CreateClient.SCHEMATIC_AND_QUILL_HANDLER.tick();
        CreateClient.GLUE_HANDLER.tick();
        CreateClient.SCHEMATIC_HANDLER.tick();
        CreateClient.ZAPPER_RENDER_HANDLER.tick();
        CreateClient.POTATO_CANNON_RENDER_HANDLER.tick();
        CreateClient.SOUL_PULSE_EFFECT_HANDLER.tick(world);
        CreateClient.RAILWAYS.clientTick();

        ContraptionHandler.tick(world);
        CapabilityMinecartController.tick(world);
        CouplingPhysics.tick(world);

        PonderTooltipHandler.tick();
        // ScreenOpener.tick();
        ServerSpeedProvider.clientTick();
        BeltConnectorHandler.tick();
//		BeltSlicer.tickHoveringInformation();
        FilteringRenderer.tick();
        LinkRenderer.tick();
        ScrollValueRenderer.tick();
        ChassisRangeDisplay.tick();
        EdgeInteractionRenderer.tick();
        GirderWrenchBehavior.tick();
        WorldshaperRenderHandler.tick();
        CouplingHandlerClient.tick();
        CouplingRenderer.tickDebugModeRenders();
        KineticDebugger.tick();
        ExtendoGripRenderHandler.tick();
        // CollisionDebugger.tick();
        ArmInteractionPointHandler.tick();
        EjectorTargetHandler.tick();
        PlacementHelpers.tick();
        CreateClient.OUTLINER.tickOutlines();
        CreateClient.GHOST_BLOCKS.tickGhosts();
        ContraptionRenderDispatcher.tick(world);
        BlueprintOverlayRenderer.tick();
        ToolboxHandlerClient.clientTick();
        TrackTargetingClient.clientTick();
        TrackPlacement.clientTick();
        TrainRelocator.clientTick();
        DisplayLinkBlockItem.clientTick();
        CurvedTrackInteraction.clientTick();
        CameraDistanceModifier.tick();
        CameraAngleAnimationService.tick();
        TrainHUD.tick();
    }
    protected static boolean isGameActive() {
        return !(Minecraft.getInstance().level == null || Minecraft.getInstance().player == null);
    }

}
