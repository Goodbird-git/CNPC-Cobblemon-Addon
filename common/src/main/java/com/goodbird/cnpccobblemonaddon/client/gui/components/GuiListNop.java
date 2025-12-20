package com.goodbird.cnpccobblemonaddon.client.gui.components;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.Options;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.ContainerObjectSelectionList;
import net.minecraft.client.gui.components.OptionsList;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import noppes.npcs.shared.client.gui.components.GuiLabel;
import noppes.npcs.shared.client.gui.components.GuiTextFieldNop;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class GuiListNop extends ContainerObjectSelectionList<GuiListNop.Entry> {
    public GuiListNop(Minecraft p_94465_, int p_94466_, int p_94467_, int p_94468_, int p_94469_, int p_94470_) {
        super(p_94465_, p_94466_, p_94469_-p_94468_, p_94468_, p_94470_);
        this.centerListVertically = false;
    }

    @Override
    protected void renderListBackground(GuiGraphics guiGraphics) {

    }

    @Override
    protected void renderListSeparators(GuiGraphics guiGraphics) {
    }

    @Override
    public void clearEntries() {
        super.clearEntries();
    }

    @Override
    public int addEntry(Entry p_93487_) {
        return super.addEntry(p_93487_);
    }

    public int getRowWidth() {
        return 400;
    }

    protected int getScrollbarPosition() {
        return super.getScrollbarPosition() + 32;
    }

    @Override
    public boolean charTyped(char p_94683_, int p_94684_) {
        for(Entry entry: children()){
            entry.charTyped(p_94683_, p_94684_);
        }
        return super.charTyped(p_94683_, p_94684_);
    }

    @Environment(EnvType.CLIENT)
    public static class Entry extends ContainerObjectSelectionList.Entry<GuiListNop.Entry> {
        final List<AbstractWidget> children;

        public Entry(Object data, List<AbstractWidget> components) {
            this.children = ImmutableList.copyOf(components);
        }

//        public static GuiListNop.Entry big(int p_232539_) {
//            return new GuiListNop.Entry(null, ImmutableMap.of(p_232540_, p_232540_.createButton(p_232538_, p_232539_ / 2 - 155, 0, 310)));
//        }
//
//        public static GuiListNop.Entry small(Options p_232542_, int p_232543_, OptionInstance<?> p_232544_, @Nullable OptionInstance<?> p_232545_) {
//            AbstractWidget abstractwidget = p_232544_.createButton(p_232542_, p_232543_ / 2 - 155, 0, 150);
//            return p_232545_ == null ? new GuiListNop.Entry(null, ImmutableMap.of(p_232544_, abstractwidget)) : new GuiListNop.Entry(ImmutableMap.of(p_232544_, abstractwidget, p_232545_, p_232545_.createButton(p_232542_, p_232543_ / 2 - 155 + 160, 0, 150)));
//        }

        public void render(GuiGraphics p_281311_, int p_94497_, int p_94498_, int p_94499_, int p_94500_, int p_94501_, int p_94502_, int p_94503_, boolean p_94504_, float p_94505_) {
            this.children.forEach((p_280776_) -> {
                p_280776_.setY(p_94498_+ ((p_280776_ instanceof GuiLabel)?6:0));
                p_280776_.render(p_281311_, p_94502_, p_94503_, p_94505_);
            });
        }

        @Override
        public boolean charTyped(char p_94683_, int p_94684_) {
            for(AbstractWidget widget: children){
                if(widget instanceof GuiTextFieldNop){
                    widget.charTyped(p_94683_, p_94684_);
                }
            }
            return super.charTyped(p_94683_, p_94684_);
        }

        public List<? extends GuiEventListener> children() {
            return this.children;
        }

        public List<? extends NarratableEntry> narratables() {
            return this.children;
        }
    }
}