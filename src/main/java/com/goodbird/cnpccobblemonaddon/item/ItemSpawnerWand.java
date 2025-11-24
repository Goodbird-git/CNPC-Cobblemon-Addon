package com.goodbird.cnpccobblemonaddon.item;

import com.goodbird.cnpccobblemonaddon.constants.CMEnumGuiType;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import noppes.npcs.CustomNpcs;

public class ItemSpawnerWand  extends Item {
    public ItemSpawnerWand() {
        super((new Item.Properties()).stacksTo(1));
    }

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        if (level.isClientSide) {
            CustomNpcs.proxy.openGui(player, CMEnumGuiType.CobbleSpawnerSpawnerList);
        }
        return new InteractionResultHolder(InteractionResult.SUCCESS, itemstack);
    }

    public int getUseDuration(ItemStack p_77626_1_) {
        return 72000;
    }


    public ItemStack finishUsingItem(ItemStack stack, Level worldIn, LivingEntity playerIn) {
        return stack;
    }
}
