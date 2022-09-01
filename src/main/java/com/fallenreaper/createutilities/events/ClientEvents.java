package com.fallenreaper.createutilities.events;

import com.fallenreaper.createutilities.CreateUtilities;
import com.fallenreaper.createutilities.content.armor.BrassJetPackModel;
import com.fallenreaper.createutilities.content.items.PunchcardItem;
import com.fallenreaper.createutilities.utils.IHaveHiddenToolTip;
import com.fallenreaper.createutilities.utils.ToolTipHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(Dist.CLIENT)
public class ClientEvents {
    public static ModelLayerLocation
            BRASS_JETPACK_LAYER = new ModelLayerLocation(new ResourceLocation(CreateUtilities.ID, "brass_jetpack"), "main");

    public static BrassJetPackModel BRASS_JETPACK_MODEL = null;

    @SubscribeEvent
    public static void onRenderOverlay(RenderGameOverlayEvent.Text event) {
        Minecraft mc = Minecraft.getInstance();

        if (!mc.level.isClientSide())
            return;

        if (mc.player != null) {

            String fps = mc.fpsString;


            Player player = mc.player;
            // event.getLeft().add(fps);
        }
    }
    @SubscribeEvent
    public static void addToItemTooltip(ItemTooltipEvent event) {
        if (event.getPlayer() == null)
            return;

        if(event.getItemStack().getItem() instanceof IHaveHiddenToolTip item)
            ToolTipHandler.registerToolTip(event.getToolTip(), event.getItemStack(), event.getPlayer(), item.getKey());

        PunchcardItem.addToolTip(event.getToolTip(), event.getItemStack());
    }

    @SubscribeEvent
    public static void onTick(TickEvent.ClientTickEvent event) {
        if (!isGameActive())
            return;

        Level world = Minecraft.getInstance().level;
        if (event.phase == TickEvent.Phase.START) {
            return;
        }

        Player player = Minecraft.getInstance().player;

        if (player == null)
            return;
        if (world == null)
            return;


        for (ItemStack item : player.getInventory().items)
            if (item.getItem() instanceof PunchcardItem) {
                if (item.hasTag() && item.getTag().contains("Key")) {
                    CompoundTag stackTag = item.getTag();

                    //      BlockPos selectedBlockPos = CreateUtilities.DOORLOCK_MANAGER.dataStored.get(stackTag.getUUID("Key")).blockPos;


                    //     if (!(player.level.getBlockState(selectedBlockPos).getBlock() instanceof LockSlidingDoor))
                    //        return;
                    //   item.setTag(null);

                }
            }
        PunchcardItem.clientTick();


    }

    protected static boolean isGameActive() {
        return !(Minecraft.getInstance().level == null || Minecraft.getInstance().player == null);
    }

    @SubscribeEvent
    public void onRegisterLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(BRASS_JETPACK_LAYER, BRASS_JETPACK_MODEL::createBodyLayer);

    }

    @SubscribeEvent
    public void onRegisterLayers(EntityRenderersEvent.AddLayers event) {
        BRASS_JETPACK_MODEL = new BrassJetPackModel(event.getEntityModels().bakeLayer(BRASS_JETPACK_LAYER));
    }


}
