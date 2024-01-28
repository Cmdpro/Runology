package com.cmdpro.runology.datagen;

import com.cmdpro.runology.Runology;
import com.cmdpro.runology.init.ItemInit;
import com.cmdpro.runology.init.TagInit;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModItemTagGenerator extends ItemTagsProvider {
    public ModItemTagGenerator(PackOutput p_275343_, CompletableFuture<HolderLookup.Provider> p_275729_,
                               CompletableFuture<TagLookup<Block>> p_275322_, @Nullable ExistingFileHelper existingFileHelper) {
        super(p_275343_, p_275729_, p_275322_, Runology.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        this.tag(TagInit.Items.GAUNTLETS)
                .add(ItemInit.COPPERGAUNTLET.get())
                .add(ItemInit.MYSTERIUMGAUNTLET.get())
                .add(ItemInit.EMPOWEREDMYSTERIUMGAUNTLET.get());
        this.tag(ItemTags.ARROWS)
                .add(ItemInit.PURITYARROW.get());
    }
}