package com.cmdpro.runology.moddata;

public class ClientPlayerData {
    private static int runicKnowledge;
    private static float currentChunkInstability;
    public static void set(int runicKnowledge, float currentChunkInstability) {
        ClientPlayerData.runicKnowledge = runicKnowledge;
        ClientPlayerData.currentChunkInstability = currentChunkInstability;
    }
    public static int getPlayerRunicKnowledge() {
        return runicKnowledge;
    }
    public static float getPlayerChunkInstability() {
        return currentChunkInstability;
    }
}
