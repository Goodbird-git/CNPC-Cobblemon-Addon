package com.goodbird.cnpccobblemonaddon.block.tile;

import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.goodbird.cnpccobblemonaddon.data.PokemonSpawnerData;
import com.goodbird.cnpccobblemonaddon.registry.ModBlockRegistry;
import net.minecraft.client.renderer.entity.DisplayRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.*;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

public class PokemonSpawnerTile extends BlockEntity {
    private final PokemonSpawnerData data = new PokemonSpawnerData();
    private long nextSpawnTime = 0;

    private ArrayList<UUID> linkedToSpawner = new ArrayList<>();

    public PokemonSpawnerTile(BlockPos p_155229_, BlockState p_155230_) {
        super(ModBlockRegistry.POKE_SPAWNER_TILE.get(), p_155229_, p_155230_);
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        tag.put("data", data.serializeNBT());
        tag.putLong("nextSpawnTime", nextSpawnTime);
        ListTag list = new ListTag();
        for(UUID id: linkedToSpawner){
            list.add(StringTag.valueOf(id.toString()));
        }
        tag.put("linkedEntities", list);
        super.saveAdditional(tag);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        data.parseNBT(nbt.getCompound("data"));
        nextSpawnTime = nbt.getLong("nextSpawnTime");
        linkedToSpawner = new ArrayList<>();
        for(Tag tag: nbt.getList("linkedEntities", 8)){
            linkedToSpawner.add(UUID.fromString(tag.getAsString()));
        }
    }

    public PokemonSpawnerData getData() {
        return data;
    }

    public void doSpawnCycle() {
        int rolls = level.random.nextInt(data.getMaxSpawnsPerRound() + 1);
        for (int i = 0; i < rolls; i++) {
            PokemonSpawnerData.PokemonSpawnEntry entry = data.getRandomEntry(level);
            Entity entity = entry.getPokemon().createPokemon(level, new Random());
            entity.setPos(worldPosition.above().getCenter());
            level.addFreshEntity(entity);
        }
    }

    public void refreshUUIDS(){
        if(level instanceof ServerLevel) {
            for (int i=0; i<linkedToSpawner.size(); i++) {
                Entity entity = ((ServerLevel)level).getEntity(linkedToSpawner.get(i));
                if(entity==null || !entity.isAlive()) {
                    linkedToSpawner.remove(i);
                    i--;
                }
            }
            level.blockEntityChanged(worldPosition);
        }
    }

    public ArrayList<UUID> getLinkedToSpawner(){
        refreshUUIDS();
        return linkedToSpawner;
    }

    public static void tick(Level level, BlockPos pos, BlockState state, PokemonSpawnerTile tile) {
        if(level.isClientSide()) return;
        PokemonSpawnerData data = tile.getData();
        if(data.getSpawnEntries().isEmpty()) return;
        if (!level.hasNeighborSignal(pos) && data.isRedstoneControl()) return;
        if (level.getEntitiesOfClass(Player.class, tile.getBlockState().getShape(level, pos).bounds().move(pos).inflate(data.getPlayerDetectionRange())).isEmpty())
            return;

        long curTime = level.getGameTime();
        if (tile.nextSpawnTime < curTime) {
            tile.nextSpawnTime = curTime + level.random.nextIntBetweenInclusive(data.getTickRangeMin(), data.getTickRangeMax());
        }
        if (tile.nextSpawnTime == curTime) {
            int alreadySpawned = tile.getLinkedToSpawner().size();
            if(alreadySpawned>=data.getMaxSpawnsPerSpawner()){
                return;
            }
            int toSpawn = Math.min(level.random.nextInt(data.getMaxSpawnsPerRound()+1), data.getMaxSpawnsPerSpawner()-alreadySpawned);
            int spawned = 0;
            for (int i = 0; i < 40 && spawned < toSpawn; i++) {

                PokemonSpawnerData.PokemonSpawnEntry entry = data.getRandomEntry(level);
                PokemonEntity entity = entry.getPokemon().createPokemon(level, new Random());
                int x = pos.getX() + level.random.nextIntBetweenInclusive(-data.getSpawnRadius(), data.getSpawnRadius());
                int z = pos.getZ() + level.random.nextIntBetweenInclusive(-data.getSpawnRadius(), data.getSpawnRadius());
                int yGround = pos.getY() + (data.isCanSpawnUnder() ? level.random.nextIntBetweenInclusive(-data.getSpawnRadius(), data.getSpawnRadius()) : level.random.nextInt(data.getSpawnRadius()));
                BlockPos posGround = new BlockPos(x,yGround,z);
                BlockState stateGround = level.getBlockState(posGround);

                if(!data.isAllowWater()) {
                    while (!stateGround.entityCanStandOn(level, posGround, entity) && yGround >= (pos.getY() + (data.isCanSpawnUnder() ? -data.getSpawnRadius() : 0))) {
                        yGround--;
                        posGround = new BlockPos(x, yGround, z);
                        stateGround = level.getBlockState(posGround);
                    }
                }else{
                    while (!level.getBlockState(posGround.above()).getFluidState().is(FluidTags.WATER) && yGround > (pos.getY() + (data.isCanSpawnUnder() ? -data.getSpawnRadius() : 0))) {
                        yGround--;
                        posGround = new BlockPos(x, yGround, z);
                        stateGround = level.getBlockState(posGround);
                    }
                }

                if((stateGround.entityCanStandOn(level, posGround, entity) && (level.getBlockState(posGround.above()).isAir()) || (data.isAllowWater() && level.getBlockState(posGround.above()).getFluidState().is(FluidTags.WATER)))){
                    entity.setPos(posGround.above().getCenter());
                    if(data.isSafeSpawn()){
                        if(!data.isAllowWater() && !entity.checkSpawnObstruction(level)){
                            continue;
                        }
                        if(data.isAllowWater() && !level.isUnobstructed(entity)){
                            continue;
                        }
                    }

                    level.addFreshEntity(entity);
                    tile.linkedToSpawner.add(entity.getUUID());
                    level.blockEntityChanged(tile.worldPosition);
                    spawned++;
                }
            }
        }
    }
}
