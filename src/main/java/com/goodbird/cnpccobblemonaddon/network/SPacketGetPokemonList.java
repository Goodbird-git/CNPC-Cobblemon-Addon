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

public class SPacketGetPokemonList extends PacketServerBasic {
    private BlockPos pos;
    public SPacketGetPokemonList() {

    }
    public SPacketGetPokemonList(BlockPos pos) {
        this.pos = pos;
    }

    public PermissionNode<Boolean> getPermission() {
        return CustomNpcsPermissions.NPC_GUI;
    }

    public static void encode(SPacketGetPokemonList msg, FriendlyByteBuf buf) {
        buf.writeLong(msg.pos.asLong());
    }

    public static SPacketGetPokemonList decode(FriendlyByteBuf buf) {
        return new SPacketGetPokemonList(BlockPos.of(buf.readLong()));
    }

    protected void handle() {
        sendPokemonList(this.player);
    }

    public void sendPokemonList(ServerPlayer player) {
        PokemonSpawnerTile tile = (PokemonSpawnerTile) player.level().getBlockEntity(pos);

        CompoundTag tagToSend = new CompoundTag();
        ListTag list = new ListTag();

        for(UUID id : tile.getLinkedToSpawner()){
            Entity entity = ((ServerLevel)player.level()).getEntity(id);
            if(entity instanceof PokemonEntity pokemon){
                CompoundTag entityData = new CompoundTag();
                entityData.putString("Name", pokemon.getName().getString());
                entityData.putInt("Level", pokemon.getPokemon().getLevel());
                entityData.putBoolean("Shiny", pokemon.getPokemon().getShiny());
                entityData.putLong("Pos", pokemon.getOnPos().asLong());
                entityData.putInt("Id", pokemon.getId());
                list.add(entityData);
            }
        }
        tagToSend.put("data", list);
        Packets.send(this.player, new PacketGuiData(tagToSend));
    }
}
