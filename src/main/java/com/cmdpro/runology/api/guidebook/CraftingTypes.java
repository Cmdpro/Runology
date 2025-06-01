package com.cmdpro.runology.api.guidebook;


import com.cmdpro.runology.data.entries.pages.crafting.types.CraftingTableType;

import java.util.ArrayList;
import java.util.List;

public class CraftingTypes {
    public static List<CraftingType> types = new ArrayList<>();
    static {
        types.add(new CraftingTableType());
    }
}
