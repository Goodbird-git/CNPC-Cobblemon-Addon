package com.goodbird.cnpccobblemonaddon.fabric.client;

import com.goodbird.cnpccobblemonaddon.registry.ModContainerRegistry;
import dev.architectury.registry.menu.MenuRegistry;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

import java.util.function.Function;

public class MenuUtils {
    public static <T extends AbstractContainerMenu> MenuType<T> ofExtended(final ModContainerRegistry.ExtendedFactory<T, FriendlyByteBuf> factory) {
        return new ExtendedScreenHandlerType<>(factory::create, new StreamCodec<>() {
            @Override
            public FriendlyByteBuf decode(RegistryFriendlyByteBuf object) {
                int bytes = object.readInt();
                FriendlyByteBuf resBuf = new FriendlyByteBuf(Unpooled.buffer(65536));
                object.readBytes(resBuf, bytes);
                return resBuf;
            }

            @Override
            public void encode(RegistryFriendlyByteBuf object, FriendlyByteBuf object2) {
                object.writeInt(object2.readableBytes());
                object.writeBytes(object2);
            }
        });
    }
}
