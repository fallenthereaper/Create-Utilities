package com.fallenreaper.createutilities.events;

import com.fallenreaper.createutilities.CreateUtilities;
import com.fallenreaper.createutilities.content.armor.BrassJetPackModel;
import com.fallenreaper.createutilities.content.items.DevItem;
import com.fallenreaper.createutilities.content.items.PunchcardItem;
import com.fallenreaper.createutilities.utils.IHaveHiddenToolTip;
import com.fallenreaper.createutilities.utils.ToolTipHandler;
import com.fallenreaper.createutilities.utils.data.blocks.LiquidTankBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(Dist.CLIENT)
public class ClientEvents {
    public static ModelLayerLocation
            BRASS_JETPACK_LAYER = new ModelLayerLocation(new ResourceLocation(CreateUtilities.ID, "brass_jetpack"), "main");

    public static BrassJetPackModel BRASS_JETPACK_MODEL = null;

    @SubscribeEvent
    public static void onRenderOverlay(RenderGameOverlayEvent.Text event) {
        Minecraft mc = Minecraft.getInstance();
        PoseStack poseStack = event.getMatrixStack();
        List<Component> tooltip = new ArrayList<>();
        if (mc.level == null) return;
        if (!mc.level.isClientSide())
            return;

        Player player = mc.player;
        if (player == null)
            return;

        if (!(player.getItemInHand(InteractionHand.MAIN_HAND).getItem() instanceof DevItem))
            return;

        String fps = mc.fpsString;
        HitResult hitResult = mc.hitResult;
        if (hitResult instanceof BlockHitResult hitResult1) {
            BlockPos blockPos = hitResult1.getBlockPos();
            Level level = mc.level;
            if (level != null) {

                BlockEntity blockEntity = level.getBlockEntity(blockPos);
                if (blockEntity instanceof LiquidTankBlockEntity be) {

                    event.getLeft().add("[DEBUG]");
                    event.getLeft().add(be.getProvidedInfo().substring(0, 14));
                    event.getLeft().add(be.getProvidedInfo().substring(14, 35));
                    boolean i = be.getTank().getFluid().getAmount() != 0;
                    event.getLeft().add(be.getProvidedInfo().substring(35, 43  ) + ": " + i);


                }
            }
        }


        // event.getLeft().add(fps);
    }

    @SubscribeEvent
    public static void addToItemTooltip(ItemTooltipEvent event) {
        if (event.getPlayer() == null)
            return;

        if (event.getItemStack().getItem() instanceof IHaveHiddenToolTip item)
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

        if (player.getMainHandItem().isStackable()) {
            float mass = (float) player.getMainHandItem().getCount();
            player.getDeltaMovement().scale(1f / mass);
        }

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

    @SubscribeEvent
    public void stopRenderPlayer(RenderPlayerEvent.Pre event) {
/*
        LivingEntity pLivingEntity = event.getEntityLiving();
        Player player = event.getPlayer();
        PlayerRenderer renderer = event.getRenderer();


        if (pLivingEntity.getVehicle() != null && pLivingEntity.getVehicle() instanceof Llama contraptionEntity) {
            event.setCanceled(true);
            PoseStack poseStack = event.getPoseStack();
            poseStack.pushPose();
            float partialTick = event.getPartialTick();
            float angle = contraptionEntity.rotA;
            Quaternion pQuaternion = Vector3f.XP.rotationDegrees(angle);
            poseStack.mulPose(pQuaternion);
            event.getPoseStack().popPose();
            MinecraftForge.EVENT_BUS.post(new RenderLivingEvent.Post<>(pLivingEntity, event.getRenderer(), event.getPartialTick(), event.getPoseStack(), event.getMultiBufferSource(), event.getPackedLight()));
            return;
        }

*/


    }

    @SubscribeEvent
    public void renderPlayer(RenderPlayerEvent.Post event) {
        LivingEntity pLivingEntity = event.getEntityLiving();
        Player player = event.getPlayer();
        PlayerRenderer renderer = event.getRenderer();
        PoseStack poseStack = event.getPoseStack();
        float partialTick = event.getPartialTick();
        MultiBufferSource buffer = event.getMultiBufferSource();
        int packedLight = event.getPackedLight();

/*
        if (pLivingEntity.getVehicle() != null && pLivingEntity.getVehicle() instanceof ControlledContraptionEntity contraptionEntity) {
        }
*/

    }

}
