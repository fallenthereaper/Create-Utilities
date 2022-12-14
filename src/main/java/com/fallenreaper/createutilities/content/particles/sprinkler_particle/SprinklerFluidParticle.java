package com.fallenreaper.createutilities.content.particles.sprinkler_particle;

import com.simibubi.create.content.contraptions.fluids.potion.PotionFluid;
import com.simibubi.create.foundation.utility.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraftforge.fluids.FluidStack;

public class SprinklerFluidParticle extends TextureSheetParticle {
    private final float uo;
    private final float vo;
    private FluidStack fluid;

    public SprinklerFluidParticle(ClientLevel pLevel, FluidStack fluidStack, double pX, double pY, double pZ, float uo, float vo) {
        super(pLevel, pX, pY, pZ);
        float vo1;
        float uo1;
        this.fluid = fluidStack;

        uo1 = uo;
        vo1 = vo;
        this.setSprite(Minecraft.getInstance()
                .getTextureAtlas(InventoryMenu.BLOCK_ATLAS)
                .apply(fluid.getFluid()
                        .getAttributes().getFlowingTexture()));

        this.gravity = 1.0F;
        this.rCol = 0.8F;
        this.gCol = 0.8F;
        this.bCol = 0.8F;
        this.multiplyColor(fluid.getFluid()
                .getAttributes()
                .getColor(fluid));

        this.xd = pX;
        this.yd = pY;
        this.zd = pZ;

        this.quadSize /= 2.0F;
        uo1 = this.random.nextFloat() * 3.0F;
        this.uo = uo1;
        vo1 = this.random.nextFloat() * 3.0F;
        this.vo = vo1;
    }




    @Override
    protected int getLightColor(float p_189214_1_) {
        int brightnessForRender = super.getLightColor(p_189214_1_);
        int skyLight = brightnessForRender >> 20;
        int blockLight = (brightnessForRender >> 4) & 0xf;
        blockLight = Math.max(blockLight, fluid.getFluid()
                .getAttributes()
                .getLuminosity(fluid));
        return (skyLight << 20) | (blockLight << 4);
    }

    protected void multiplyColor(int color) {
        this.rCol *= (float) (color >> 16 & 255) / 255.0F;
        this.gCol *= (float) (color >> 8 & 255) / 255.0F;
        this.bCol *= (float) (color & 255) / 255.0F;
    }

    protected float getU0() {
        return this.sprite.getU((double) ((this.uo + 1.0F) / 4.0F * 16.0F));
    }

    protected float getU1() {
        return this.sprite.getU((double) (this.uo / 4.0F * 16.0F));
    }

    protected float getV0() {
        return this.sprite.getV((double) (this.vo / 4.0F * 16.0F));
    }

    protected float getV1() {
        return this.sprite.getV((double) ((this.vo + 1.0F) / 4.0F * 16.0F));
    }

    @Override
    public void tick() {
        super.tick();
        if (!canEvaporate())
            return;
        if (onGround)
            remove();
        if (!removed)
            return;
        if (!onGround && level.random.nextFloat() < 1 / 32f)
            return;

        Color color = new Color(fluid.getFluid()
                .getAttributes()
                .getColor(fluid));
        level.addParticle(ParticleTypes.ENTITY_EFFECT, x, y, z, color.getRedAsFloat(), color.getGreenAsFloat(), color.getBlueAsFloat());
    }

    protected boolean canEvaporate() {
        return fluid.getFluid() instanceof PotionFluid;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

}
