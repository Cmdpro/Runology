package com.cmdpro.runology.api.guidebook;


import com.cmdpro.runology.data.entries.pages.crafting.types.CraftingTableType;
import com.cmdpro.runology.data.entries.pages.crafting.types.ShatterInfusionType;

import java.util.ArrayList;
import java.util.List;

public class CraftingTypes {
    public static List<CraftingType> types = new ArrayList<>();
    static {
        types.add(new CraftingTableType());
        types.add(new ShatterInfusionType());
    }
}
