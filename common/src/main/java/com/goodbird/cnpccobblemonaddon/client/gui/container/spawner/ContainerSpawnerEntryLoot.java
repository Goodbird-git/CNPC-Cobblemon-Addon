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

public class ContainerSpawnerEntryLoot extends AbstractContainerMenu {
    private SimpleContainer container;
    public BlockPos pos;
    public int row;
    public ContainerSpawnerEntryLoot(int containerId, Inventory playerInventory, BlockPos pos, int rowId){
        super(ModContainerRegistry.EDIT_ENTRY_LOOT_MENU.get(), containerId);
        container = new SimpleContainer(9);
        PokemonSpawnerTile tile = (PokemonSpawnerTile) playerInventory.player.level().getBlockEntity(pos);
        PokemonData data = tile.getData().getSpawnEntries().get(rowId).getPokemon();

        for(int i=0;i<9;i++) {
            container.setItem(i, data.getDropData().getDropItems()[i].getDropStackEntry());
        }

        container.addListener(p_18983_ -> {
            for(int i=0;i<9;i++) {
                data.getDropData().getDropItems()[i].setDropStackEntry(container.getItem(i));
            }
        });

        for(int i=0;i<9;i++) {
            addSlot(new Slot(container, i, 20, 40+i*20));
        }

        for(int i1 = 0; i1 < 3; i1++){
            for(int l1 = 0; l1 < 9; l1++){
                addSlot(new Slot(playerInventory, l1 + i1 * 9 + 9, l1 * 18 + 225, 145 + i1 * 18));
            }
        }

        for(int j1 = 0; j1 < 9; j1++){
            addSlot(new Slot(playerInventory, j1, j1 * 18 + 225, 203));
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
