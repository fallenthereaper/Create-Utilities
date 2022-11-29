package com.fallenreaper.createutilities.content.blocks.steam_furnace;

import com.fallenreaper.createutilities.utils.InteractionHandler;
import com.fallenreaper.createutilities.utils.data.IDevInfo;
import com.fallenreaper.createutilities.utils.data.blocks.InteractableBlockEntity;
import com.simibubi.create.AllItems;
import com.simibubi.create.Create;
import com.simibubi.create.content.contraptions.goggles.IHaveGoggleInformation;
import com.simibubi.create.content.contraptions.processing.EmptyingByBasin;
import com.simibubi.create.foundation.fluid.FluidHelper;
import com.simibubi.create.foundation.fluid.SmartFluidTank;
import com.simibubi.create.foundation.tileEntity.TileEntityBehaviour;
import com.simibubi.create.foundation.tileEntity.behaviour.fluid.SmartFluidTankBehaviour;
import com.simibubi.create.foundation.utility.*;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

import static com.fallenreaper.createutilities.content.blocks.steam_furnace.FurnaceState.LOADED;
import static com.fallenreaper.createutilities.content.blocks.steam_furnace.FurnaceState.PRODUCING;
import static com.fallenreaper.createutilities.content.blocks.steam_furnace.SteamFurnaceBlock.CREATIVE_LIT;
import static com.fallenreaper.createutilities.utils.MathUtil.formatTime;
import static net.minecraft.world.level.block.state.properties.BlockStateProperties.LIT;
import static net.minecraftforge.items.ItemHandlerHelper.canItemStacksStack;

public class SteamFurnaceBlockEntity extends InteractableBlockEntity implements IHaveGoggleInformation, ISteamProvider, IDevInfo {
 //   public SteamFurnaceBoilerData boiler;
    public float indicatorProgress;
    protected boolean hasFluidIn;
    protected FurnaceState furnaceState;
    protected SteamFurnaceItemHandler inventory;
    protected boolean hasFuel;
    protected LazyOptional<SteamFurnaceItemHandler> inventoryProvider;
    protected SmartFluidTankBehaviour tankBehaviour;
    protected int litTime = 0;
    protected boolean isCreative;
    protected float steamAmount;

    public SteamFurnaceBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        this.inventory = createItemHandler();
        inventoryProvider = LazyOptional.of(() -> inventory);
        this.furnaceState = FurnaceState.NONE;
    }

    public static boolean isFuel(ItemStack pStack) {
        return ForgeHooks.getBurnTime(pStack, null) > 0;
    }

    protected static float getIndicatorProgress(SteamFurnaceBlockEntity furnace) {
        return Mth.sin((furnace.indicatorProgress / 30) * Mth.PI / 2);
    }

    public static int getFuelBurnTime(ItemStack pFuel) {
        if (pFuel.isEmpty())
            return 0;
        if (!isFuel(pFuel))
            return 0;

        return ForgeHooks.getBurnTime(pFuel, null);
    }


    @Override
    public void addBehaviours(List<TileEntityBehaviour> behaviours) {
        super.addBehaviours(behaviours);
        tankBehaviour = createFluidHandler();
        this.getTank().setValidator((p -> FluidHelper.isWater(p.getFluid())));
        behaviours.add(tankBehaviour);
    }

    private SmartFluidTankBehaviour createFluidHandler() {
        return SmartFluidTankBehaviour.single(this, getTankCapacity());
    }

    private SteamFurnaceItemHandler createItemHandler() {
        return new SteamFurnaceItemHandler(this, 1);
    }

    private int getTankCapacity() {
        return 1024 / 2;
    }

    @Override
    public InteractionResult onInteract(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHitResult) {
        if (level == null)
            return InteractionResult.FAIL;

        ItemStack itemStack = pPlayer.getItemInHand(pHand).copy();
        if (level.getBlockEntity(pPos) instanceof SteamFurnaceBlockEntity) {
            ItemStack stackInSlot = getFuelStack().copy();
            //FLUIDS

            SoundEvent soundevent = null;
            BlockState fluidState = null;

            boolean present = itemStack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY)
                    .isPresent();
            if (pPlayer.getItemInHand(pHand).is(Items.BUCKET) && !isFuel(itemStack)) {
                if (hasFluid()) {
                    Fluid fluid = getTank().getFluid().getFluid();

                    FluidAttributes attributes = fluid.getAttributes();
                    soundevent = attributes.getFillSound();
                    if (soundevent == null)
                        soundevent =
                                FluidHelper.isTag(fluid, FluidTags.LAVA) ? SoundEvents.BUCKET_EMPTY_LAVA : SoundEvents.BUCKET_EMPTY;

                    this.getTank().drain(Math.min(getTankCapacity(), getTank().getFluidAmount()), IFluidHandler.FluidAction.EXECUTE);
                    if (pLevel.isClientSide())
                        pLevel.playSound(pPlayer, pPos, soundevent, SoundSource.BLOCKS, 1.0f, 1f);

                }
                return InteractionResult.SUCCESS;
            }

            if (present && !isFuel(itemStack)) {

                if (getTank() != null) {
                    if (!pPlayer.getItemInHand(pHand).is(Items.BUCKET)) {
                        Pair<FluidStack, ItemStack> emptyItem = EmptyingByBasin.emptyItem(pLevel, itemStack, true);
                        Fluid fluid = getTank().getFluid().getFluid();
                        fluidState = fluid.defaultFluidState()
                                .createLegacyBlock();
                        FluidAttributes attributes = fluid.getAttributes();
                        soundevent = attributes.getEmptySound();
                        if (soundevent == null)
                            soundevent =
                                    FluidHelper.isTag(fluid, FluidTags.LAVA) ? SoundEvents.BUCKET_EMPTY_LAVA : SoundEvents.BUCKET_EMPTY;
                        FluidStack fluidFromItem = emptyItem.getFirst();

                        this.getTank().fill(fluidFromItem, IFluidHandler.FluidAction.EXECUTE);


                        if (pLevel.isClientSide())
                            pLevel.playSound(pPlayer, pPos, soundevent, SoundSource.BLOCKS, 1.0f, 1f);

                    }
                }
                return InteractionResult.SUCCESS;
            }

            //FUEL ITEMS
            if (!hasFuel() && itemStack.isEmpty())
                return InteractionResult.FAIL;
            if (!itemStack.isEmpty() && !isFuel(itemStack))
                return InteractionResult.FAIL;

            if (!hasFuel()) {
                if (!itemStack.isEmpty() && isFuel(itemStack)) {
                    //  if (!pPlayer.isCreative()) {
                    pPlayer.setItemInHand(pHand, ItemStack.EMPTY);
                    setItemStack(itemStack);
                    if (getBurnTime() > 0)
                        setBurnTime(getFuelBurnTime(getFuelStack()));
                    if (pLevel.isClientSide())
                        pLevel.playSound(pPlayer, pPos, SoundEvents.FIRECHARGE_USE, SoundSource.BLOCKS, 0.70F - pLevel.getRandom().nextFloat() * 0.28f, 0.95F - pLevel.getRandom().nextFloat() * 0.8f);

                }
            }
            else if (!itemStack.isEmpty()) {
                if (canItemStacksStack(itemStack, stackInSlot)) {
                    //todo: fix this to not take any item  if the furnace has already a stack
                    int desiredAmount = Math.max(stackInSlot.getMaxStackSize() - stackInSlot.getCount(), 0);
                        stackInSlot.grow(Math.min(desiredAmount, itemStack.getCount()));
                        setItemStack(stackInSlot);
                        itemStack.shrink(desiredAmount);
                        pPlayer.setItemInHand(pHand, itemStack);
                    if (pLevel.isClientSide())
                        pLevel.playSound(pPlayer, pPos, SoundEvents.FIRECHARGE_USE, SoundSource.BLOCKS, 0.75F - pLevel.getRandom().nextFloat() * 0.28f, 0.95F - pLevel.getRandom().nextFloat() * 0.8f);

                }
            }
            else {
                setItemStack(ItemStack.EMPTY);
                pPlayer.setItemInHand(pHand, stackInSlot);
                if (getBurnTime() > 0)
                    setBurnTime(getFuelBurnTime(getFuelStack()));
                if (pLevel.isClientSide())
                    pLevel.playSound(pPlayer, pPos, SoundEvents.ARMOR_EQUIP_GENERIC, SoundSource.BLOCKS, 0.75F - pLevel.getRandom().nextFloat() * 0.28f, 1.0F - pLevel.getRandom().nextFloat() * 0.23f);

            }
            notifyUpdate();
            return InteractionResult.SUCCESS;
        }

        return super.onInteract(pState, pLevel, pPos, pPlayer, pHand, pHitResult);
    }

    public boolean isCreativeFuel() {
        return isCreative;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.getLevel() == null)
            return;


        boolean isClientSide = this.getLevel().isClientSide;
        this.hasFluidIn = !getTank().getFluid().isEmpty();
        this.hasFuel = !getFuelStack().isEmpty() && !getFuelStack().is(Items.AIR);
        this.isCreative = AllItems.CREATIVE_BLAZE_CAKE.isIn(getFuelStack()) || getFuelBurnTime(getFuelStack()) >= 2200000 ;

        if (litTime > 0 && !isCreativeFuel())
            litTime--;

        if (litTime <= 0 && hasFuel()) {
            setBurnTime(getFuelBurnTime(getFuelStack()));
            if (!isCreativeFuel())
                getFuelStack().shrink(1);
        }

        syncBlockState();
        syncFurnaceState();
        if (hasFuel()) {
            if (isClientSide) {
                spawnParticles();
            }
        }
    }

    public void syncFurnaceState() {
        if (getLevel() != null) {
            if (isBurning() && hasFluid()) {
                updateState(PRODUCING);
            } else if (isBurning()) {
                updateState(FurnaceState.LIT);
            } else if (hasFluid() && !isBurning()) {
                updateState(LOADED);
            } else {
                updateState(FurnaceState.NONE);
            }
        }
    }

    public void spawnParticles() {
        if (getLevel() == null)
            return;
        if (!getLevel().isClientSide())
            return;
        SimpleParticleType particleType;
        Vec3 pos = VecHelper.getCenterOf(this.worldPosition);
        Direction direction = getBlockState()
                .getValue(BlockStateProperties.HORIZONTAL_FACING);
        Vec3i N = direction.getOpposite().getNormal();
        Vec3 N2 = new Vec3(N.getX(), N.getY(), N.getZ());
        pos = pos.add(-N.getX() * 0.53, -0.1, -N.getZ() * 0.53);
        if (isProducing()) {
                particleType = isCreativeFuel() ? ParticleTypes.ELECTRIC_SPARK : ParticleTypes.LAVA;
            if (getLevel().getRandom().nextFloat() < 0.5F / 16F) {
                Vec3 random = VecHelper.offsetRandomly(Vec3.ZERO, Create.RANDOM, 0.1f);
                random = random.subtract(N2.scale(random.dot(N2)));
                pos = pos.add(random);
                getLevel().addParticle(particleType, pos.x, pos.y, pos.z, 0, 0, 0);
            }
        }

        double d0 = (double) getBlockPos().getX() + 0.5D;
        double d1 = getBlockPos().getY();
        double d2 = (double) getBlockPos().getZ() + 0.5D;
        if (Objects.requireNonNull(getLevel()).getRandom().nextDouble() < 0.1D) {
            getLevel().playLocalSound(d0, d1, d2, SoundEvents.BLASTFURNACE_FIRE_CRACKLE, SoundSource.BLOCKS, 1.0F, 1.0F, false);
        }

        Direction.Axis direction$axis = direction.getAxis();
        double d3 = 0.52D;
        double d4 = getLevel().getRandom().nextDouble() * 0.6D - 0.3D;
        double d5 = direction$axis == Direction.Axis.X ? (double) direction.getStepX() * 0.52D : d4;
        double d6 = getLevel().getRandom().nextDouble() * 9.0D / 16.0D;
        double d7 = direction$axis == Direction.Axis.Z ? (double) direction.getStepZ() * 0.52D : d4;
        getLevel().addParticle(ParticleTypes.SMOKE, d0 + d5, d1 + d6, d2 + d7, 0.0D, 0.0D, 0.0D);

    }

    private ItemStack getFuelStack() {

        return getInventory().getStackInSlot(0);
    }

    private void updateState(FurnaceState furnaceState) {
        this.furnaceState = furnaceState;
    }

    public boolean isBurning() {
        return getBurnTime() > 0;
    }

    //Credit: Create Aeronautics; Eriksson
    protected void initParticles() {
        assert level != null;
        level.playSound(null, worldPosition, SoundEvents.FIRECHARGE_USE, SoundSource.BLOCKS,
                .625f + level.random.nextFloat() * .325f, .75f - level.random.nextFloat() * .25f);
        SimpleParticleType particleType;
        Vec3 pos = VecHelper.getCenterOf(this.worldPosition);

        Direction direction = getBlockState()
                .getValue(BlockStateProperties.HORIZONTAL_FACING).getOpposite();
        Vec3i N = direction.getNormal();
        Vec3 N2 = new Vec3(N.getX(), N.getY(), N.getZ());
        pos = pos.add(-N.getX() * 0.53, -0.1, -N.getZ() * 0.53);
        Vec3 speed = VecHelper.offsetRandomly(Vec3.ZERO, Create.RANDOM, 0.01f).add(N2.scale(-0.03));
        for (int i = 0; i < 2; i++) {
            Vec3 random = VecHelper.offsetRandomly(Vec3.ZERO, Create.RANDOM, 0.1f);
            random = random.subtract(N2.scale(random.dot(N2)));
            pos = pos.add(random);
            level.addParticle(isCreativeFuel() ? ParticleTypes.SOUL_FIRE_FLAME : ParticleTypes.FLAME, pos.x, pos.y, pos.z, speed.x, speed.y, speed.z);
        }
    }

    public void syncBlockState() {
        if (getLevel() != null) {
            if (isBurning() && !this.getBlockState().getValue(LIT) && !isCreativeFuel()) {
                getLevel().setBlock(getBlockPos(), this.getBlockState().setValue(LIT, true), 3);
                if (getLevel().isClientSide())
                    initParticles();
                sendData();
            }
            if (!isBurning() && this.getBlockState().getValue(LIT) && !isCreativeFuel()) {
                getLevel().setBlock(getBlockPos(), this.getBlockState().setValue(LIT, false), 3);
                sendData();
            }
           else
               if (isBurning() && !this.getBlockState().getValue(CREATIVE_LIT) && isCreativeFuel()) {
                getLevel().setBlock(getBlockPos(), this.getBlockState().setValue(CREATIVE_LIT, true), 3);
                if (getLevel().isClientSide())
                    initParticles();
                sendData();
            }
          else
              if (!isBurning() && this.getBlockState().getValue(CREATIVE_LIT) && !isCreativeFuel()) {

                getLevel().setBlock(getBlockPos(), this.getBlockState().setValue(CREATIVE_LIT, false), 3);
                sendData();
            }
        }
    }

    public int getBurnTime() {
        return this.litTime;
    }

    public void setBurnTime(int litTime) {
        this.litTime = litTime;
    }

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        ItemStack fuelIn = getFuelStack();
        Component indent = new TextComponent(" ");
        Component indent1 = new TextComponent(spacing + " ");
        Component arrow = new TextComponent("->").withStyle(ChatFormatting.DARK_GRAY);
        Component time = getBurnTime() <= 0 ? new TextComponent("") : new TextComponent(formatTime(getMaxBurnTime(fuelIn))).withStyle(ChatFormatting.GOLD);
        String item = !hasFuel() ? "None" : fuelIn.getItem().getName(fuelIn).getString();
        Component in = new TextComponent(item + " " + (fuelIn.isEmpty() ? "" : "x") + (fuelIn.getCount() <= 0 ? "" : fuelIn.getCount())).withStyle(fuelIn.isEmpty() ? ChatFormatting.RED : ChatFormatting.GREEN);
        LangBuilder mb = Lang.translate("generic.unit.millibuckets");
        FluidStack fluidStackIn = getTank().getFluid();
        Component fluidName = new TranslatableComponent(fluidStackIn.getTranslationKey()).withStyle(ChatFormatting.AQUA);
        Component contained = new TextComponent(String.valueOf(fluidStackIn.getAmount())).plainCopy().append(mb.string()).withStyle(ChatFormatting.GOLD);
        Component slash = new TextComponent(" / ").withStyle(ChatFormatting.GRAY);
        Component capacity = new TextComponent(String.valueOf(getTankCapacity())).plainCopy().append(mb.string()).withStyle(ChatFormatting.DARK_GRAY);


        tooltip.add(indent1.plainCopy()
                .append("Steam Furnace Info:"));
        tooltip.add(arrow.plainCopy()
                .append(indent)
                .append("Burn Time" + ":").withStyle(ChatFormatting.GRAY).append(indent).append(isCreativeFuel() ? new TextComponent("Unlimited").withStyle(ChatFormatting.LIGHT_PURPLE) : time));
        tooltip.add(arrow.plainCopy().append(indent).append("Fuel" + ":").withStyle(ChatFormatting.GRAY).append(indent).append(in));

        if (hasFluid()) {
            /*
            tooltip.add(arrow.plainCopy()
                    .append(indent)
                    .append("Fluid: ").withStyle(ChatFormatting.GRAY)
                    .append(fluidName)
            );
         */
            tooltip.add(arrow.plainCopy()
                    .append(indent)
                    .append("Fluid Amount: ").withStyle(ChatFormatting.GRAY)
                    .append(contained)
                    .append(slash)
                    .append(capacity));
        }

        return true;
    }

    public boolean hasFuel() {
        if (isCreative)
            return true;

        return hasFuel;
    }

    public String getStateName() {
        var stateString = furnaceState.name();
        stateString = stateString.substring(0, 1).toUpperCase() + stateString.substring(1).toLowerCase();
        return stateString;
    }

    public boolean hasFluid() {
        return hasFluidIn;
    }

    public void setItemStack(ItemStack itemStack) {

        this.inventory.setStackInSlot(0, itemStack);

    }

    public SteamFurnaceItemHandler getInventory() {
        return this.inventory;
    }

    @Override
    public float getProducedSteam() {
        return 0;
    }

    @Override
    protected void write(CompoundTag compound, boolean clientPacket) {
        super.write(compound, clientPacket);
        // compound.put("BoilerData", boiler.write());
        compound.putBoolean("HasFuel", !getInventory().getStackInSlot(0).isEmpty());
        compound.put("FurnaceInventory", getInventory().serializeNBT());
        compound.putInt("BurnTime", getBurnTime());
        compound.putBoolean("HasFluid", this.hasFluidIn);
        compound.putBoolean("IsCreative", isCreative);
        NBTHelper.writeEnum(compound, "State", this.furnaceState);
    }

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        super.read(compound, clientPacket);
        //  boiler.read(compound.getCompound("BoilerData"), 2);
        this.hasFuel = compound.getBoolean("HasFuel");
        this.inventory.deserializeNBT(compound.getCompound("FurnaceInventory"));
        this.litTime = compound.getInt("BurnTime");
        this.hasFluidIn = compound.getBoolean("HasFluid");
        this.furnaceState = NBTHelper.readEnum(compound, "State", FurnaceState.class);
        this.isCreative = compound.getBoolean("IsCreative");

    }

    @Override
    public void addInteractionHandler(List<InteractionHandler> interactions) {
        //   SteamFurnaceInteraction furnaceInteraction = new SteamFurnaceInteraction(this, getTank());
        // interactions.add(furnaceInteraction);

    }

    public SmartFluidTank getTank() {
        return this.tankBehaviour.getPrimaryHandler();
    }

    @Override
    public String getProvidedInfo() {
        String timer = hasFuel() ? formatTime(getMaxBurnTime(getInventory().getStackInSlot(0))) : "None";
        return "HasFuel: " + hasFuel() + "; BurnTime: " + getBurnTime() + "; Timer: " + timer + "; State: " + getStateName();
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        inventoryProvider.invalidate();
        tankBehaviour.remove();
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, Direction side) {

        if (isItemHandlerCap(cap))
            return inventoryProvider.cast();
        if (isFluidHandlerCap(cap))
            return tankBehaviour.getCapability()
                    .cast();
        return super.getCapability(cap);
    }

    @Override
    protected boolean isFluidHandlerCap(Capability<?> cap) {
        return cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY;

    }

    @Override
    protected boolean isItemHandlerCap(Capability<?> cap) {
        return super.isItemHandlerCap(cap);
    }

    protected int getMaxBurnTime(ItemStack itemStack) {


        int seconds = getBurnTime();

        //convert to seconds
        int modifier = getFuelBurnTime(itemStack);

        return seconds + (modifier * itemStack.getCount());
    }


    private FurnaceState getState() {
        return this.furnaceState;
    }

    public boolean isProducing() {
        return getState() == FurnaceState.PRODUCING;
    }

    public boolean isPaused() {
        return getState() == FurnaceState.NONE;
    }

    public boolean isLoaded() {
        return getState() == FurnaceState.LOADED;
    }

    public boolean isLit() {
        return getState() == FurnaceState.LIT;
    }
}


