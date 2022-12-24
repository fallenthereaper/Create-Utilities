package com.fallenreaper.createutilities.mixins;

import com.fallenreaper.createutilities.core.data.ISweepParticleProvider;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public class PlayerMixin {

    @Inject(at = @At(value = "HEAD"),  method = "sweepAttack", remap = false, cancellable = true)
    public void createutilities_onSweepAttack(CallbackInfo ci) {
        Player player = (Player) (Object) this;
        Item pItemStack = ((Player)(Object)this).getItemInHand(InteractionHand.MAIN_HAND).getItem();
        if (pItemStack instanceof ISweepParticleProvider provider) {
            if(provider.onSweepAttack(player)) {
                ci.cancel();
            }
        }
    }
}