package com.fallenreaper.createutilities.mixins;

import com.fallenreaper.createutilities.core.data.LivingEntityModelAccessor;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
@Mixin(LivingEntityRenderer.class)
public class LivingEntityMixin<T extends LivingEntity, M extends EntityModel<T>> implements LivingEntityModelAccessor<T, M> {
        @Shadow
        protected M model;

        @Override
        public M getModel() {
            return model;
    }
}
