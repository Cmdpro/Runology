package com.cmdpro.runology.datagen;

import com.cmdpro.runology.Runology;
import com.cmdpro.runology.init.BlockInit;
import com.cmdpro.runology.init.TagInit;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagGenerator extends BlockTagsProvider {
    public ModBlockTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, Runology.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        this.tag(Tags.Blocks.ORES)
                .add(BlockInit.AIRORE.get())
                .add(BlockInit.WATERORE.get())
                .add(BlockInit.FIREORE.get())
                .add(BlockInit.EARTHORE.get())
                .add(BlockInit.MYSTERIUMORE.get());
        this.tag(TagInit.Blocks.MYSTERIUMOREREPLACEABLE)
                .addTag(Tags.Blocks.ORES);
        this.tag(TagInit.Blocks.SHATTERREMOVES)
                .addTag(BlockTags.LEAVES);
        this.tag(TagInit.Blocks.SHATTERSTONEREPLACEABLE)
                .addTag(BlockTags.DIRT)
                .addTag(BlockTags.BASE_STONE_OVERWORLD)
                .addTag(BlockTags.BASE_STONE_NETHER)
                .addTag(BlockTags.NYLIUM)
                .addTag(BlockTags.TERRACOTTA)
                .addTag(BlockTags.SAND)
                .add(Blocks.SNOW_BLOCK)
                .add(Blocks.POWDER_SNOW)
                .add(Blocks.COBBLESTONE)
                .add(Blocks.COBBLED_DEEPSLATE)
                .add(Blocks.SOUL_SAND)
                .add(Blocks.SOUL_SOIL)
                .add(Blocks.GRAVEL)
                .add(Blocks.SUSPICIOUS_GRAVEL)
                .add(Blocks.CALCITE)
                .add(Blocks.SMOOTH_BASALT)
                .add(Blocks.CLAY)
                .add(Blocks.DRIPSTONE_BLOCK)
                .add(Blocks.END_STONE)
                .add(Blocks.MOSS_BLOCK)
                .add(Blocks.FARMLAND);
        this.tag(TagInit.Blocks.SHATTERWOODREPLACEABLE)
                .addTags(BlockTags.LOGS);

        this.tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(BlockInit.RUNICWORKBENCH.get())
                .add(BlockInit.AIRORE.get())
                .add(BlockInit.WATERORE.get())
                .add(BlockInit.FIREORE.get())
                .add(BlockInit.EARTHORE.get())
                .add(BlockInit.SHATTERSTONE.get())
                .add(BlockInit.SHATTERSTONEBRICKS.get())
                .add(BlockInit.SHATTERSTONEWALL.get())
                .add(BlockInit.SHATTERSTONESTAIRS.get())
                .add(BlockInit.SHATTERSTONEBRICKWALL.get())
                .add(BlockInit.SHATTERSTONEBRICKSTAIRS.get())
                .add(BlockInit.SHATTERSTONEBRICKSLAB.get())
                .add(BlockInit.CRACKEDSHATTERSTONEBRICKS.get())
                .add(BlockInit.CHISELEDSHATTERSTONEBRICKS.get())
                .add(BlockInit.SHATTERSTONEPILLAR.get())
                .add(BlockInit.MYSTERIUMORE.get())
                .add(BlockInit.MYSTERIUMBLOCK.get())
                .add(BlockInit.RUNICWORKBENCH.get())
                .add(BlockInit.SHATTERCRYSTAL.get())
                .add(BlockInit.RUNICANALYZER.get())
                .add(BlockInit.RUNICCAULDRON.get())
                .add(BlockInit.ENDERTRANSPORTER.get());


        this.tag(BlockTags.NEEDS_IRON_TOOL)
                .add(BlockInit.AIRORE.get())
                .add(BlockInit.WATERORE.get())
                .add(BlockInit.FIREORE.get())
                .add(BlockInit.EARTHORE.get())
                .add(BlockInit.MYSTERIUMORE.get())
                .add(BlockInit.MYSTERIUMBLOCK.get());

        this.tag(BlockTags.WALLS)
                .add(BlockInit.SHATTERSTONEBRICKWALL.get())
                .add(BlockInit.SHATTERSTONEWALL.get());

        this.tag(BlockTags.BEACON_BASE_BLOCKS)
                .add(BlockInit.MYSTERIUMBLOCK.get());
    }
}
