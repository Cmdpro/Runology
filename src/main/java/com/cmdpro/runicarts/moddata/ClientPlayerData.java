package com.cmdpro.runicarts.moddata;

public class ClientPlayerData {
    private static float souls;
    private static int knowledge;
    private static int ancientknowledge;
    private static boolean canDoubleJump;
    public static void set(float souls, int knowledge, int ancientknowledge, boolean canDoubleJump) {
        ClientPlayerData.souls = souls;
        ClientPlayerData.knowledge = knowledge;
        ClientPlayerData.ancientknowledge = ancientknowledge;
        ClientPlayerData.canDoubleJump = canDoubleJump;
    }

    public static float getPlayerSouls() {
        return souls;
    }
    public static int getPlayerKnowledge() {
        return knowledge;
    }
    public static int getPlayerAncientKnowledge() {
        return ancientknowledge;
    }
    public static boolean getCanDoubleJump() {
        return canDoubleJump;
    }
}
