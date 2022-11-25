package com.fallenreaper.createutilities.content.items;

import com.fallenreaper.createutilities.CreateUtilities;
import com.fallenreaper.createutilities.utils.data.IThirdPersonAnimation;
import net.minecraft.ChatFormatting;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BaseItem extends Item implements IThirdPersonAnimation {
   // protected TranslatableComponent pTextComponent;
    private boolean hasDescription;
    private ChatFormatting[] chatFormatting;

    public BaseItem(Properties pProperties) {
        super(pProperties);
        this.hasDescription = false;
    }

    public BaseItem addDescription(ChatFormatting... formattings) {
        this.hasDescription = true;
        this.chatFormatting = formattings;
    //    this.pTextComponent = pTextComponent;
        return this;
    }


    @Override
    public void appendHoverText(@NotNull ItemStack pStack, @Nullable Level pLevel, @NotNull List<Component> pTooltipComponents, @NotNull TooltipFlag pIsAdvanced) {
        if(hasDescription) {
            for(ChatFormatting format : chatFormatting)
           pTooltipComponents.add(new TranslatableComponent(CreateUtilities.ID +"."+this.getDescriptionId(pStack)+"."+"description").withStyle((format)));
        }
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }

    @Override
    public <T extends LivingEntity> boolean poseArm(ItemStack itemStack, HumanoidArm arm, HumanoidModel<T> model, T entity, boolean rightHand) {
        return false;
    }
}
