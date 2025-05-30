package com.cmdpro.runology.api.guidebook;

import com.cmdpro.datanessence.data.datatablet.pages.crafting.types.*;

import java.util.ArrayList;
import java.util.List;

public class CraftingTypes {
    public static List<CraftingType> types = new ArrayList<>();
    static {
        types.add(new CraftingTableType());
    }
}
