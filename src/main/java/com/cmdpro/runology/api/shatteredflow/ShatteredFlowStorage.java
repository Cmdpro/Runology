package com.cmdpro.runology.api.shatteredflow;

public abstract class ShatteredFlowStorage {
    public int amount;
    public abstract int addShatteredFlow(int amount);
    public abstract int removeShatteredFlow(int amount);
    public abstract boolean canAddShatteredFlow(int amount);
}