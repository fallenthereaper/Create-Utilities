package com.fallenreaper.createutilities.content.items;

import com.fallenreaper.createutilities.core.data.ISweepParticleProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;

import static com.fallenreaper.createutilities.core.utils.MiscUtil.getAllWithinRadius;

public class ScytheItem extends SwordItem implements ISweepParticleProvider {

    public ScytheItem(Tier pTier, int pAttackDamageModifier, float pAttackSpeedModifier) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, new Properties().tab(CreativeModeTab.TAB_TOOLS));
    }



    public boolean hurtEnemy(ItemStack pStack, LivingEntity pTarget, LivingEntity pAttacker) {
        pStack.hurtAndBreak(2, pAttacker, (p_41007_) -> {
            p_41007_.broadcastBreakEvent(EquipmentSlot.MAINHAND);
        });
        return true;
    }
    //todo: properly code this; by adding a list of these blocks instead of checking one by one
    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        ItemStack itemStack = pContext.getItemInHand();
        BlockPos clickedBlock = pContext.getClickedPos();
        Player player = pContext.getPlayer();
        Level level = pContext.getLevel();
        assert player != null;
        boolean creative = player.isCreative();




                getAllWithinRadius(level, player, getRadius(), clickedBlock).forEach((blockPos -> {
                    BlockState blockState = level.getBlockState(blockPos);
                    Block block = blockState.getBlock();
                    if (block instanceof CropBlock cropBlock && cropBlock.isMaxAge(blockState)) {
                        if (!creative) {
                            player.causeFoodExhaustion(0.005F);
                        }
                        level.destroyBlock(blockPos, !creative, player);
                        player.awardStat(Stats.BLOCK_MINED.get(cropBlock));

                    }
                }
                ));


        return InteractionResult.SUCCESS;
}


    public int getRadius() {
        return (int) getTier().getSpeed() + 2;
    }







    public boolean mineBlock(ItemStack pStack, Level pLevel, BlockState pState, BlockPos pPos, LivingEntity pEntityLiving) {



        return super.mineBlock(pStack, pLevel, pState, pPos, pEntityLiving);
    }

    @Override
    public boolean onSweepAttack(Player player) {
        double d0 = (double)(-Mth.sin(player.getYRot() * ((float)Math.PI / 180F)));
        double d1 = (double)Mth.cos(player.getYRot() * ((float)Math.PI / 180F));
        if (player.level instanceof ServerLevel) {
            for (int i = 0; i < 10; i++) {
                ((ServerLevel) player.level).sendParticles(ParticleTypes.ELECTRIC_SPARK, player.getX() + d0 * 1.3, player.getY(0.5D), player.getZ() + d1 * 1.3, i, d0, 0.0D, d1, 0.0D);
            }

        }
        return true;
    }
}
