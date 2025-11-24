package com.goodbird.cnpccobblemonaddon.registry;

import com.goodbird.cnpccobblemonaddon.block.PokemonSpawnerBlock;
import com.goodbird.cnpccobblemonaddon.block.tile.PokemonSpawnerTile;
import net.minecraft.Util;
import net.minecraft.util.datafix.fixes.References;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = "cnpccobblemonaddon", bus= Mod.EventBusSubscriber.Bus.MOD)
public class ModBlockRegistry {

    public static DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, "cnpccobblemonaddon");

    public static final RegistryObject<Block> POKE_SPAWNER = BLOCKS.register("pokespawner", PokemonSpawnerBlock::new);

    public static DeferredRegister<BlockEntityType<?>> TILES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, "cnpccobblemonaddon");

    public static final RegistryObject<BlockEntityType<PokemonSpawnerTile>> POKE_SPAWNER_TILE = TILES.register("pokespawner", ()->createTile("pokespawner", PokemonSpawnerTile::new, POKE_SPAWNER.get()));

    private static <T extends BlockEntity> BlockEntityType<T> createTile(String key, BlockEntityType.BlockEntitySupplier<T> factoryIn, Block... blocks){
        BlockEntityType.Builder<T> builder = BlockEntityType.Builder.of(factoryIn, blocks);
        return builder.build(Util.fetchChoiceType(References.BLOCK_ENTITY, key));
    }
}
