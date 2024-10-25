package com.goodbird.cnpccobblemonaddon.client.gui.quest;

import com.cobblemon.mod.common.api.pokemon.PokemonSpecies;
import com.cobblemon.mod.common.pokemon.Species;
import com.goodbird.cnpccobblemonaddon.quest.QuestPokeCatch;
import net.minecraft.client.gui.screens.Screen;
import noppes.npcs.client.gui.util.GuiNPCInterface;
import noppes.npcs.controllers.data.Quest;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.shared.client.gui.components.GuiButtonNop;
import noppes.npcs.shared.client.gui.components.GuiCustomScrollNop;
import noppes.npcs.shared.client.gui.components.GuiLabel;
import noppes.npcs.shared.client.gui.components.GuiTextFieldNop;
import noppes.npcs.shared.client.gui.listeners.ICustomScrollListener;
import noppes.npcs.shared.client.gui.listeners.ITextfieldListener;

import java.util.ArrayList;
import java.util.TreeMap;


public class GuiQuestTypePokeCatch extends GuiNPCInterface implements ITextfieldListener, ICustomScrollListener
{
    private Screen parent;
    private GuiCustomScrollNop scroll;

    private QuestPokeCatch quest;

    private GuiTextFieldNop lastActive;

    public GuiQuestTypePokeCatch(EntityNPCInterface npc, Quest q, Screen parent) {
        this.npc = npc;
        this.parent = parent;
        title = "Quest Catch Setup";

        quest = (QuestPokeCatch) q.questInterface;

        setBackground("menubg.png");
        imageWidth = 356;
        imageHeight = 216;
    }

    @Override
    public void init() {
        super.init();
        int i = 0;
        addLabel(new GuiLabel(0, "Select the pokemon types to catch", guiLeft + 4, guiTop + 50));
        for (String name : quest.targets.keySet()) {
            this.addTextField(new GuiTextFieldNop(i, this,  guiLeft + 4, guiTop + 70 + i * 22, 180, 20, name));
            this.addTextField(new GuiTextFieldNop(i + 3, this,  guiLeft + 186, guiTop + 70 + i * 22, 24, 20, quest.targets.get(name) + ""));
            this.getTextField(i+3).numbersOnly = true;
            this.getTextField(i+3).setMinMaxDefault(1, Integer.MAX_VALUE, 1);
            i++;
        }

        for(;i < 3; i++){
            this.addTextField(new GuiTextFieldNop(i, this,  guiLeft + 4, guiTop + 70 + i * 22, 180, 20, ""));
            this.addTextField(new GuiTextFieldNop(i + 3, this,  guiLeft + 186, guiTop + 70 + i * 22, 24, 20, "1"));
            this.getTextField(i+3).numbersOnly = true;
            this.getTextField(i+3).setMinMaxDefault(1, Integer.MAX_VALUE, 1);
        }
        ArrayList<String> list = new ArrayList<String>();

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
        this.addButton(new GuiButtonNop(this, 0, guiLeft + 4, guiTop + 140, 98, 20, "gui.back"));

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
        if(GuiTextFieldNop.isAnyActive() && GuiTextFieldNop.getActive().id < 3){
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
        TreeMap<String,Integer> map = new TreeMap<String,Integer>();
        for(int i = 0; i< 3; i++){
            String name = getTextField(i).getValue();
            if(name.isEmpty())
                continue;
            map.put(name, getTextField(i+3).getInteger());
        }
        quest.targets = map;
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
