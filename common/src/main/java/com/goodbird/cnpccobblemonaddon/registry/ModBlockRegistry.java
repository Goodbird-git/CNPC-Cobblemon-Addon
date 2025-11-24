package com.goodbird.cnpccobblemonaddon.registry;

import com.goodbird.cnpccobblemonaddon.block.PokemonSpawnerBlock;
import com.goodbird.cnpccobblemonaddon.block.tile.PokemonSpawnerTile;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.Util;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.util.datafix.fixes.References;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class ModBlockRegistry {

    public static DeferredRegister<Block> BLOCKS = DeferredRegister.create("cnpccobblemonaddon", Registries.BLOCK);

    public static final RegistrySupplier<Block> POKE_SPAWNER = BLOCKS.register("pokespawner", PokemonSpawnerBlock::new);

    public static DeferredRegister<BlockEntityType<?>> TILES = DeferredRegister.create("cnpccobblemonaddon", Registries.BLOCK_ENTITY_TYPE);

    public static final RegistrySupplier<BlockEntityType<PokemonSpawnerTile>> POKE_SPAWNER_TILE = TILES.register("pokespawner", ()->createTile("pokespawner", PokemonSpawnerTile::new, POKE_SPAWNER.get()));

    private static <T extends BlockEntity> BlockEntityType<T> createTile(String key, BlockEntityType.BlockEntitySupplier<T> factoryIn, Block... blocks){
        BlockEntityType.Builder<T> builder = BlockEntityType.Builder.of(factoryIn, blocks);
        return builder.build(Util.fetchChoiceType(References.BLOCK_ENTITY, key));
    }
}
