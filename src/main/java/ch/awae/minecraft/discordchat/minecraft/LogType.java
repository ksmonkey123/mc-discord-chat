package ch.awae.minecraft.discordchat.minecraft;

public enum LogType {
    OUTPUT, ERROR;

    public static LogType fromErrorFlag(boolean errorFlag) {
        return errorFlag ? LogType.ERROR : LogType.OUTPUT;
    }
}
