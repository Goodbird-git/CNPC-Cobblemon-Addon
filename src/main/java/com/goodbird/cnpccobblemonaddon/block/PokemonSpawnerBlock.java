package com.goodbird.cnpccobblemonaddon.block;

import com.goodbird.cnpccobblemonaddon.block.tile.PokemonSpawnerTile;
import com.goodbird.cnpccobblemonaddon.constants.CMEnumGuiType;
import com.goodbird.cnpccobblemonaddon.registry.ModBlockRegistry;
import com.goodbird.cnpccobblemonaddon.registry.ModItemRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.GlassBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import noppes.npcs.CustomItems;
import noppes.npcs.CustomNpcsPermissions;
import noppes.npcs.blocks.BlockInterface;
import noppes.npcs.packets.server.SPacketGuiOpen;

import javax.annotation.Nullable;

public class PokemonSpawnerBlock extends BlockInterface {
    public PokemonSpawnerBlock() {
        super(Properties.copy(Blocks.BARRIER).sound(SoundType.METAL).noCollission());
    }

    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult ray) {
        if (level.isClientSide) {
            return InteractionResult.PASS;
        } else {
            ItemStack currentItem = player.getInventory().getSelected();
            if (currentItem.getItem() == ModItemRegistry.SPAWNER_WAND_ITEM.get() && CustomNpcsPermissions.hasPermission((ServerPlayer) player, CustomNpcsPermissions.EDIT_BLOCKS)) {
                SPacketGuiOpen.sendOpenGui(player, CMEnumGuiType.CobbleSpawnerMainSettings, null, pos);
                return InteractionResult.SUCCESS;
            } else {
                return InteractionResult.PASS;
            }
        }
    }

    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity entity, ItemStack item) {
        if (!level.isClientSide && entity instanceof Player) {
            SPacketGuiOpen.sendOpenGui((Player)entity, CMEnumGuiType.CobbleSpawnerMainSettings, null, pos);
        }
    }

    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new PokemonSpawnerTile(pos, state);
    }

    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type, ModBlockRegistry.POKE_SPAWNER_TILE.get(), PokemonSpawnerTile::tick);
    }

    public VoxelShape getVisualShape(BlockState p_48735_, BlockGetter p_48736_, BlockPos p_48737_, CollisionContext p_48738_) {
        return Shapes.empty();
    }

    public float getShadeBrightness(BlockState p_48731_, BlockGetter p_48732_, BlockPos p_48733_) {
        return 1.0F;
    }

    public boolean propagatesSkylightDown(BlockState p_48740_, BlockGetter p_48741_, BlockPos p_48742_) {
        return true;
    }

    public boolean skipRendering(BlockState p_53972_, BlockState p_53973_, Direction p_53974_) {
        return p_53973_.is(this) || super.skipRendering(p_53972_, p_53973_, p_53974_);
    }
}
