package com.fallenreaper.createutilities.content.blocks.steam_furnace;

import com.fallenreaper.createutilities.index.CUBlockEntities;
import com.fallenreaper.createutilities.index.CUBlockShapes;
import com.fallenreaper.createutilities.utils.ContainerUtil;
import com.fallenreaper.createutilities.utils.data.blocks.InteractableBlockEntity;
import com.simibubi.create.content.contraptions.base.HorizontalKineticBlock;
import com.simibubi.create.content.contraptions.wrench.IWrenchable;
import com.simibubi.create.foundation.block.ITE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.LIT;

public class SteamFurnaceBlock extends HorizontalKineticBlock implements ITE<SteamFurnaceBlockEntity>, IWrenchable {
    public static final BooleanProperty CREATIVE_LIT = BooleanProperty.create("creative_lit");
    public SteamFurnaceBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState().setValue(LIT, false));
        this.registerDefaultState(this.defaultBlockState().setValue(CREATIVE_LIT, false));
    }

    @Override
    public Direction.Axis getRotationAxis(BlockState state) {
        return state.getValue(HORIZONTAL_FACING)
                .getAxis();
    }
    @Override
    public Class<SteamFurnaceBlockEntity> getTileEntityClass() {
        return SteamFurnaceBlockEntity.class;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(LIT);
        builder.add(CREATIVE_LIT);
    }
    public static int getLightPower(BlockState state) {
        return state.getValue(LIT) ? 7 : state.getValue(CREATIVE_LIT) ? 12 : 0;
    }


    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        if (!pState.is(pNewState.getBlock())) {
            BlockEntity blockentity = pLevel.getBlockEntity(pPos);
            if (blockentity instanceof SteamFurnaceBlockEntity te) {
                if (pLevel instanceof ServerLevel) {

                        ContainerUtil.dropContents(pLevel, pPos, te.getInventory());
                        pLevel.removeBlockEntity(pPos);


                    pLevel.removeBlockEntity(pPos);
                }
            }

            super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
        }
    }

    @Override
    public BlockEntityType<? extends SteamFurnaceBlockEntity> getTileEntityType() {
        return CUBlockEntities.STEAM_FURNACE.get();
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult ray) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof InteractableBlockEntity be) {
            return be.onInteract(state, world, pos, player, hand, ray);
        }
        return super.use(state, world, pos, player, hand, ray);
    }
    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Override
    public void stepOn(Level pLevel, BlockPos pPos, BlockState pState, Entity pEntity) {
        BlockEntity te = pLevel.getBlockEntity(pPos);
        if(pState.hasBlockEntity()) {
            if(te instanceof SteamFurnaceBlockEntity be) {
                if (be.isProducing()) {
                    if (!pEntity.fireImmune() && pEntity instanceof LivingEntity && !EnchantmentHelper.hasFrostWalker((LivingEntity) pEntity)) {
                        pEntity.hurt(DamageSource.HOT_FLOOR, 1.0F/ 4.0F);
                     //   pLevel.playSound(null, pPos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 0.75f, 1.0f);
                    }
                }
            }
        }

        super.stepOn(pLevel, pPos, pState, pEntity);
    }


    @Override
    public VoxelShape getCollisionShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return CUBlockShapes.STEAM_FURNACE.get(pState.getValue(HORIZONTAL_FACING));
    }
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return CUBlockShapes.STEAM_FURNACE.get(state.getValue(HORIZONTAL_FACING));
    }
}
