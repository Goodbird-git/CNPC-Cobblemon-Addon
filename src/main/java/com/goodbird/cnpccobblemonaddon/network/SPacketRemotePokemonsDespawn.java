package com.goodbird.cnpccobblemonaddon.network;

import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.goodbird.cnpccobblemonaddon.block.tile.PokemonSpawnerTile;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.server.permission.nodes.PermissionNode;
import noppes.npcs.CustomNpcsPermissions;
import noppes.npcs.packets.PacketServerBasic;
import noppes.npcs.packets.Packets;
import noppes.npcs.packets.client.PacketGuiData;

import java.util.UUID;

public class SPacketRemotePokemonsDespawn extends PacketServerBasic {
    private BlockPos pos;
    public SPacketRemotePokemonsDespawn() {

    }
    public SPacketRemotePokemonsDespawn(BlockPos pos) {
        this.pos = pos;
    }

    public PermissionNode<Boolean> getPermission() {
        return CustomNpcsPermissions.NPC_GUI;
    }

    public static void encode(SPacketRemotePokemonsDespawn msg, FriendlyByteBuf buf) {
        buf.writeLong(msg.pos.asLong());
    }

    public static SPacketRemotePokemonsDespawn decode(FriendlyByteBuf buf) {
        return new SPacketRemotePokemonsDespawn(BlockPos.of(buf.readLong()));
    }

    protected void handle() {
        sendPokemonList(this.player);
    }

    public void sendPokemonList(ServerPlayer player) {
        PokemonSpawnerTile tile = (PokemonSpawnerTile) player.level().getBlockEntity(pos);

        for(UUID id : tile.getLinkedToSpawner()){
            Entity entity = ((ServerLevel)player.level()).getEntity(id);
            if(entity instanceof PokemonEntity pokemon){
                pokemon.setQueuedToDespawn(true);
            }
        }
        tile.refreshUUIDS();
    }
}
