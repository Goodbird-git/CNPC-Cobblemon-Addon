package com.goodbird.cnpccobblemonaddon.client.gui.container.spawner;

import com.goodbird.cnpccobblemonaddon.block.tile.PokemonSpawnerTile;
import com.goodbird.cnpccobblemonaddon.data.PokemonData;
import com.goodbird.cnpccobblemonaddon.registry.ModContainerRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class ContainerSpawnerEntry extends AbstractContainerMenu {
    private SimpleContainer container;
    public BlockPos pos;
    public int row;
    public ContainerSpawnerEntry(int containerId, Inventory playerInventory, BlockPos pos, int rowId){
        super(ModContainerRegistry.EDIT_ENTRY_MENU.get(), containerId);
        container = new SimpleContainer(3);
        PokemonSpawnerTile tile = (PokemonSpawnerTile) playerInventory.player.level().getBlockEntity(pos);
        PokemonData data = tile.getData().getSpawnEntries().get(rowId).getPokemon();

        for(int i=0;i<3;i++) {
            container.setItem(i, data.getHeldItem(i).getDropStackEntry());
        }

        container.addListener(p_18983_ -> {
            for(int i=0;i<3;i++) {
                data.getHeldItem(i).setHeldStackEntry(container.getItem(i));
            }
        });

        addSlot(new Slot(container, 0, 160, 72));
        addSlot(new Slot(container, 1, 238, 72));
        addSlot(new Slot(container, 2, 312, 72));

        for(int i1 = 0; i1 < 3; i1++){
            for(int l1 = 0; l1 < 9; l1++){
                addSlot(new Slot(playerInventory, l1 + i1 * 9 + 9, l1 * 18 + 15, 145 + i1 * 18));
            }
        }

        for(int j1 = 0; j1 < 9; j1++){
            addSlot(new Slot(playerInventory, j1, j1 * 18 + 15, 203));
        }
        this.pos = pos;
        this.row = rowId;
    }

    @Override
    public ItemStack quickMoveStack(Player par1Player, int i){
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player entityplayer) {
        return true;
    }
}
