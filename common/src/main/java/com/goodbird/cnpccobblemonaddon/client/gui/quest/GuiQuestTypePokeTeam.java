package com.goodbird.cnpccobblemonaddon.client.gui.quest;

import com.cobblemon.mod.common.api.pokemon.PokemonSpecies;
import com.cobblemon.mod.common.pokemon.Species;
import com.goodbird.cnpccobblemonaddon.quest.PokemonEntry;
import com.goodbird.cnpccobblemonaddon.quest.QuestPokeTeam;
import net.minecraft.client.gui.screens.Screen;
import noppes.npcs.client.gui.util.GuiNPCInterface;
import noppes.npcs.controllers.data.Quest;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.shared.client.gui.components.*;
import noppes.npcs.shared.client.gui.listeners.ICustomScrollListener;
import noppes.npcs.shared.client.gui.listeners.ITextfieldListener;

import java.util.ArrayList;

public class GuiQuestTypePokeTeam extends GuiNPCInterface implements ITextfieldListener, ICustomScrollListener
{
    private Screen parent;
    private GuiCustomScrollNop scroll;

    private QuestPokeTeam quest;

    private GuiTextFieldNop lastActive;

    public GuiQuestTypePokeTeam(EntityNPCInterface npc, Quest q, Screen parent) {
        this.npc = npc;
        this.parent = parent;
        title = "Quest Team Setup";

        quest = (QuestPokeTeam) q.questInterface;

        setBackground("menubg.png");
        imageWidth = 356;
        imageHeight = 216;
    }

    @Override
    public void init() {
        super.init();
        int i = 0;
        addLabel(new GuiLabel(0, "gui.setup.team.title", guiLeft + 4, guiTop + 30));
        guiTop-=15;
        addLabel(new GuiLabel(1, "gui.setup.pokemontype", guiLeft + 34, guiTop + 60));
        addLabel(new GuiLabel(2, "gui.setup.minLevel", guiLeft + 148, guiTop + 60));
        addLabel(new GuiLabel(3, "gui.setup.isshiny", guiLeft + 178, guiTop + 60));
        for (PokemonEntry entry : quest.targets) {
            int idOffset = i*3;
            this.addTextField(new GuiTextFieldNop(idOffset, this,  guiLeft + 4, guiTop + 70 + i * 22, 140, 20, entry.getType()));
            this.addTextField(new GuiTextFieldNop(idOffset + 1, this,  guiLeft + 150, guiTop + 70 + i * 22, 24, 20, entry.getMinLevel() + ""));
            this.getTextField(idOffset+1).numbersOnly = true;
            this.getTextField(idOffset+1).setMinMaxDefault(0, Integer.MAX_VALUE, 0);
            this.addButton(new GuiButtonYesNo(this, idOffset+2, guiLeft + 179, guiTop + 70 + i * 22, 24, 20, entry.isShiny(), (btn)-> entry.setShiny(((GuiButtonYesNo)btn).getBoolean())));
            i++;
        }

        for(;i < 6; i++){
            int idOffset = i*3;
            this.addTextField(new GuiTextFieldNop(idOffset, this,  guiLeft + 4, guiTop + 70 + i * 22, 140, 20, ""));
            this.addTextField(new GuiTextFieldNop(idOffset + 1, this,  guiLeft + 150, guiTop + 70 + i * 22, 24, 20, "0"));
            this.getTextField(idOffset + 1).numbersOnly = true;
            this.getTextField(idOffset + 1).setMinMaxDefault(0, Integer.MAX_VALUE, 0);
            this.addButton(new GuiButtonYesNo(this, idOffset+2, guiLeft + 179, guiTop + 70 + i * 22, 24, 20, false));
        }
        ArrayList<String> list = new ArrayList<>();

        for(Species species: PokemonSpecies.INSTANCE.getImplemented()){
            list.add(species.getResourceIdentifier().toString());
        }
        if(scroll == null)
            scroll = new GuiCustomScrollNop(this,0);
        scroll.setList(list);
        scroll.setSize(130, 198);
        scroll.guiLeft = guiLeft + 220;
        scroll.guiTop = guiTop + 14;
        addScroll(scroll);
        guiTop+=15;
        this.addButton(new GuiButtonNop(this, 0, guiLeft + 4, guiTop + 190, 98, 20, "gui.back"));

        scroll.visible = false;
        lastActive = null;
    }

    @Override
    public void buttonEvent(GuiButtonNop guibutton) {
        if (guibutton.id == 0) {
            close();
        }
    }
    @Override
    public boolean mouseClicked(double i, double j, int k) {
        boolean bo = super.mouseClicked(i, j, k);
        if(GuiTextFieldNop.isAnyActive() && (GuiTextFieldNop.getActive().id)%3==0){
            scroll.visible = true;
            lastActive = GuiTextFieldNop.getActive();
        }
        return bo;
    }

    @Override
    public void save() {
    }

    @Override
    public void unFocused(GuiTextFieldNop guiNpcTextField) {
        saveTargets();
    }

    private void saveTargets(){
        ArrayList<PokemonEntry> list = new ArrayList<>();
        for(int i = 0; i< 6; i++){
            String name = getTextField(i*3).getValue();
            if(name.isEmpty())
                continue;
            list.add(new PokemonEntry(name, ((GuiButtonYesNo)getButton(i*3+2)).getBoolean(), getTextField(i*3+1).getInteger()));
        }
        quest.targets = list;
    }
    @Override
    public void scrollClicked(double i, double j, int k, GuiCustomScrollNop guiCustomScroll) {
        if(lastActive != null){
            lastActive.setValue(guiCustomScroll.getSelected());
            saveTargets();
        }
    }

    @Override
    public void scrollDoubleClicked(String selection, GuiCustomScrollNop scroll) {}

}

