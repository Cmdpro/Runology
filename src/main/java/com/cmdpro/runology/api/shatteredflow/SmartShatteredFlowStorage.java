package com.cmdpro.runology.api.shatteredflow;

import net.minecraft.nbt.CompoundTag;

public class SmartShatteredFlowStorage extends ShatteredFlowStorage {
    public SmartShatteredFlowStorage() {
        this.amount = 0;
    }
    public CompoundTag toTag() {
        CompoundTag tag = new CompoundTag();
        tag.putInt("amount", amount);
        return tag;
    }
    public void fromTag(CompoundTag tag) {
        amount = tag.getInt("amount");
    }

    @Override
    public int addShatteredFlow(int amount) {
        if (this.amount < amount) {
            int diff = amount-(amount-this.amount);
            this.amount = amount;
            return diff;
        }
        return amount;
    }

    @Override
    public int removeShatteredFlow(int amount) {
        if (this.amount-amount >= 0) {
            this.amount -= amount;
            return 0;
        } else {
            int diff = (this.amount-amount)*-1;
            this.amount = 0;
            return diff;
        }
    }

    @Override
    public boolean canAddShatteredFlow(int amount) {
        return this.amount < amount;
    }
}
