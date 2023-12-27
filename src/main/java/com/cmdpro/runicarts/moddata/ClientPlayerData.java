package com.cmdpro.runicarts.moddata;

public class ClientPlayerData {
    private static int runicKnowledge;
    public static void set(int runicKnowledge) {
        ClientPlayerData.runicKnowledge = runicKnowledge;
    }
    public static int getPlayerRunicKnowledge() {
        return runicKnowledge;
    }
}
