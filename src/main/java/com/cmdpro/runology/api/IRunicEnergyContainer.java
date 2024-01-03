package com.cmdpro.runology.api;

import java.util.Dictionary;
import java.util.Map;

public interface IRunicEnergyContainer {
    Map<String, Float> getRunicEnergy();
    public default void addRunicEnergy(String type, float amount) {
        if (getRunicEnergy().containsKey(type)) {
            getRunicEnergy().put(type, getRunicEnergy().get(type)+amount);
        } else {
            getRunicEnergy().put(type, amount);
        }
    }
    public default void removeRunicEnergy(String type, float amount) {
        if (getRunicEnergy().containsKey(type)) {
            getRunicEnergy().put(type, getRunicEnergy().get(type)-amount);
            if (getRunicEnergy().get(type) < 0f) {
                getRunicEnergy().put(type, 0f);
            }
        } else {
            getRunicEnergy().put(type, 0f);
        }
    }
}
