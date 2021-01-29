package ch.awae.minecraft.discordchat.minecraft;

import ch.awae.minecraft.discordchat.persistence.model.Mapping;

public interface LogStatementProcessor {
    void process(Mapping mapping, String line, LogType type);
}
